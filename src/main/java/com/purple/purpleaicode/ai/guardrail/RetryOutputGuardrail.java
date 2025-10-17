package com.purple.purpleaicode.ai.guardrail;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RetryOutputGuardrail implements OutputGuardrail {

    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        String response = responseFromLLM.text();
        // 检查响应是否为空或过短
        if (response == null || response.trim().isEmpty()) {
            return reprompt("响应内容为空", "请重新生成完整的内容");
        }
        if (response.trim().length() < 10) {
            return reprompt("响应内容过短", "请提供更详细的内容");
        }
        // 检查是否包含敏感信息或不当内容
        String sensitiveContent = containsSensitiveContent(response);
        if (!sensitiveContent.equals("")) {
            return reprompt("包含敏感信息",
                    "请重新生成内容，避免包含敏感信息:" + sensitiveContent);
        }
        return success();
    }

    /**
     * 检查是否包含敏感内容
     */
    private String containsSensitiveContent(String response) {
        if (response == null || response.isEmpty()) {
            return "";
        }

        String lowerResponse = response.toLowerCase();
        String[] sensitiveWords = {
                "密码", "password", "secret", "token",
                "api key", "私钥", "证书", "credential"
        };

        List<String> foundWords = Arrays.stream(sensitiveWords)
                .filter(word -> lowerResponse.contains(word))
                .collect(Collectors.toList());

        return String.join(",", foundWords);
    }
}