package com.purple.purpleaicode.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.purple.purpleaicode.exception.ErrorCode;
import com.purple.purpleaicode.exception.ThrowUtils;
import com.purple.purpleaicode.manager.CosManager;
import com.purple.purpleaicode.service.ScreenshotService;
import com.purple.purpleaicode.utils.WebScreenshotUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
public class ScreenshotServiceImpl implements ScreenshotService {

    @Resource
    private CosManager cosManager;

    /**
     * 生成并上传截图
     *
     * @param webUrl 网页URL
     * @return 截图的URL
     */
    @Override
    public String generateAndUploadScreenshot(String webUrl) {
        ThrowUtils.throwIf(StrUtil.isBlank(webUrl), ErrorCode.PARAMS_ERROR, "网页URL不能为空");
        log.info("开始生成网页截图，URL：{}", webUrl);
        // 1.生成本地截图
        String localScreenShotPath = WebScreenshotUtils.saveWebPageScreenshot(webUrl);
        ThrowUtils.throwIf(StrUtil.isBlank(localScreenShotPath), ErrorCode.OPERATION_ERROR, "本地截图生成失败");
        try {
            // 2.上传到对象存储
            String cosUrl = uploadScreenshotToCOS(localScreenShotPath);
            ThrowUtils.throwIf(StrUtil.isBlank(cosUrl), ErrorCode.OPERATION_ERROR, "截图上传对象存储失败");
            log.info("网页截图生成并上传成功：{} -> {}", webUrl, cosUrl);
            return cosUrl;
        } finally {
            // 3.清理本地文件
            cleanupLocalFile(localScreenShotPath);
        }
    }

    /**
     * 删除对象存储中的截图
     * @param fullUrl 截图在对象存储中的完整URL
     * @return 删除是否成功
     */
    @Override
    public boolean deleteCosScreenshot(String fullUrl) {
        ThrowUtils.throwIf(StrUtil.isBlank(fullUrl), ErrorCode.PARAMS_ERROR, "COS完整访问URL不能为空");
        return cosManager.safeDeleteObjectByFullUrl(fullUrl);
    }

    /**
     * 上传截图到对象存储
     * @param localScreenShotPath 本地截图路径
     * @return 截图在对象存储中的URL，失败返回null
     */
    private String uploadScreenshotToCOS(String localScreenShotPath) {
        if (StrUtil.isBlank(localScreenShotPath)) {
            return null;
        }
        File screenshotFile = new File(localScreenShotPath);
        if (!screenshotFile.exists()) {
            log.error("截图文件不存在：{}", localScreenShotPath);
            return null;
        }
        // 生成 COS 对象键
        String fileName = UUID.randomUUID().toString().substring(0, 8) + "_compressd.jpg";
        String cosKey = generateScreenshotKey(fileName);
        return cosManager.uploadFile(cosKey, screenshotFile);
    }

    /**
     * 生成截图的对象存储键
     * 格式：/screenshots/2023/11/11/xxxxxxxx.jpg
     * @param fileName 文件名
     * @return COS 对象键
     */
    private String generateScreenshotKey(String fileName) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("/screenshots/%s/%s", datePath, fileName);
    }

    /**
     * 清理本地截图文件
     * @param localFilePath 本地截图文件路径
     */
    private void cleanupLocalFile(String localFilePath) {
        File localFile = new File(localFilePath);
        if (localFile.exists()) {
            File parentDir = localFile.getParentFile();
            FileUtil.del(parentDir);
            log.info("本地截图文件已清理：{}", localFilePath);
        }
    }
}
