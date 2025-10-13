package com.purple.purpleaicode.ai.tools;

import cn.hutool.json.JSONObject;

public abstract class BaseTool {

    /**
     * 获取工具的英文名称
     * @return 工具的英文名称
     */
    public abstract String getToolName();

    /**
     * 获取工具的中文显示名称
     * @return 工具的中文名称
     */
    public abstract String getDisplayName();

    /**
     * 生成工具请求时的返回值（显示给用户）
     * @return 工具请求显示内容
     */
    public String generateToolRequestResponse() {
        return String.format("\n\n[选择工具] %s\n\n", getDisplayName());
    }

    /**
     * 生成工具执行结果格式（保存到数据库）
     * @param arguments 工具执行参数
     * @return 格式化的工具执行结果
     */
    public abstract String generateToolExecutedResult(JSONObject arguments);
}
