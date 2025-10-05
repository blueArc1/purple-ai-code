package com.purple.purpleaicode.ai;

import com.purple.purpleaicode.ai.model.HtmlCodeResult;
import com.purple.purpleaicode.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeGeneratorServiceFactoryTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generatorHtmlCode() {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("生成一个登录页面，总共不超出20行代码");
        Assertions.assertNotNull(result);
    }

    @Test
    void generateMulteFileCode() {
        MultiFileCodeResult multiFileCode = aiCodeGeneratorService.generateMultiFileCode("生成一个登录页面，总共不超出20行代码");
        Assertions.assertNotNull(multiFileCode);
    }
}