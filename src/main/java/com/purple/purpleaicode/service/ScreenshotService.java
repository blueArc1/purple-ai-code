package com.purple.purpleaicode.service;

public interface ScreenshotService {

    /**
     * 生成并上传截图
     * @param webUrl 网页URL
     * @return 截图的URL
     */
    String generateAndUploadScreenshot(String webUrl);

    /**
     * 删除对象存储中的截图
     * @param fullUrl 截图在对象存储中的完整URL
     * @return 删除是否成功
     */
    boolean deleteCosScreenshot(String fullUrl);
}