package com.purple.purpleaicode.manager;

import cn.hutool.core.util.StrUtil;
import com.purple.purpleaicode.config.CosClientConfig;
import com.purple.purpleaicode.exception.ErrorCode;
import com.purple.purpleaicode.exception.ThrowUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * COS对象存储管理器
 */
@Component
@Slf4j
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    /**
     * 上传对象
     * @param key 唯一键
     * @param file 文件
     * @return 上传结果
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 上传文件到 COS 并返回访问 URL
     * @param key COS对象键（完整路径）
     * @param file 文件
     * @return 文件的访问URL，失败返回null
     */
    public String uploadFile(String key, File file) {
        // 上传文件
        PutObjectResult result = putObject(key, file);
        if (result != null) {
            // 构建访问URL
            String url = String.format("%s%s", cosClientConfig.getHost(), key);
            log.info("文件上传COS成功：{} -> {}", file.getName(), url);
            return url;
        } else {
            log.error("文件上传COS失败：{}，返回结果为空", file.getName());
            return null;
        }
    }

    /**
     * 通过COS完整URL安全删除对象（包含错误处理和重试机制）
     * @param fullUrl COS完整访问URL（如：https://bucket.region.cos.cloud.tencent.com/key）
     * @return 删除是否成功
     */
    public boolean safeDeleteObjectByFullUrl(String fullUrl) {
        if (StrUtil.isBlank(fullUrl)) {
            return false;
        }
        String key = this.extractkeyFromUrl(fullUrl);
        ThrowUtils.throwIf(key == null, ErrorCode.OPERATION_ERROR, "无效的COS key：" + key);
        try {
            // 1.检查对象是否存在
            if (!cosClient.doesObjectExist(cosClientConfig.getBucket(), key)) {
                log.warn("COS对象不存在：{}", key);
                return true;
            }
            // 2.删除对象
            cosClient.deleteObject(cosClientConfig.getBucket(), key);
            log.info("删除COS对象成功：{}", key);
            return true;
        } catch (CosServiceException e) {
            // 服务端错误（5xx），尝试重试
            if (e.getStatusCode() >= 500 && e.getStatusCode() < 600) {
                log.warn("COS服务端错误，尝试重试删除: {}, error={}", key, e.getMessage());
                return retryDelete(key, 3);
            }
            // 客户端错误（4xx），不重试
            log.error("COS客户端错误: {}, error={}, message={}", key, e.getErrorCode(), e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("删除COS封面文件时发生意外错误: {}", key, e);
            return false;
        }
    }

    /**
     * 重试删除COS对象（指数退避）
     * @param key 对象键
     * @param maxRetries 最大重试次数
     */
    private boolean retryDelete(String key, int maxRetries) {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                Thread.sleep(1000 * (attempts + 1)); // 指数退避
                cosClient.deleteObject(cosClientConfig.getBucket(), key);
                return true;
            } catch (Exception e) {
                attempts++;
                if (attempts >= maxRetries) {
                    log.error("重试删除COS对象失败 (max retries reached): {}", key);
                }
            }
        }
        return false;
    }

    /**
     * 从COS完整URL中提取对象键（key）
     * 例如：https://bucket.region.cos.cloud.tencent.com/screenshots/2025/01/01/file.jpg
     * 提取为：/screenshots/2025/01/01/file.jpg
     */
    private String extractkeyFromUrl(String fullUrl) {
        try {
            URI uri = new URI(fullUrl);
            return uri.getPath();
        } catch (Exception e) {
            log.error("无效的COS URL：{}", fullUrl, e);
            return null;
        }
    }
}
