package com.purple.purpleaicode.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@EqualsAndHashCode
@Data
public class UserUpdateMyselfRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 用户头像
     */
    @Size(max = 5 * 1024 * 1024, message = "文件大小超过5MB")
    private MultipartFile userAvatar; // Spring的MultipartFile类型

    /**
     * 简介
     */
    private String userProfile;

}