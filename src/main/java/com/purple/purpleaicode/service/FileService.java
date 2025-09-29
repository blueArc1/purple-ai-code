package com.purple.purpleaicode.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FileService {
    @Value("${storage.base-path}")
    private String basePath;

    public String save(MultipartFile file) {
        // 文件MIME类型检查
        if(!Arrays.asList("image/jpeg", "image/png").contains(file.getContentType())) {
            throw new IllegalArgumentException("无效图片格式");
        }
        // 创建存储目录
        File dir = new File(basePath);
        if(!dir.exists()) dir.mkdirs();

        // 生成唯一文件名
        String ext = file.getOriginalFilename().substring(
                file.getOriginalFilename().lastIndexOf("."));
        String filename = UUID.randomUUID() + ext;

        // 保存文件
        Path path = Paths.get(basePath, filename);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        return filename; // 返回相对路径
        return basePath + "/" + filename; // 返回相对路径
    }
}
