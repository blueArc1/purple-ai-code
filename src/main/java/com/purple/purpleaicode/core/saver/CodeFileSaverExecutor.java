package com.purple.purpleaicode.core.saver;

import com.purple.purpleaicode.ai.model.HtmlCodeResult;
import com.purple.purpleaicode.ai.model.MultiFileCodeResult;
import com.purple.purpleaicode.exception.BusinessException;
import com.purple.purpleaicode.exception.ErrorCode;
import com.purple.purpleaicode.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * 代码文件保存执行器
 * 根据代码生成类型执行相应的保存逻辑
 */
public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaverTemplate = new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaverTemplate = new MultiFileCodeFileSaverTemplate();

    /**
     * 执行代码文件保存（使用appId)
     * @param codeResult 代码结果对象
     * @param codeGenType 代码生成类型
     * @param appId 应用 ID
     * @return 保存的目录
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenType, Long appId) {
        return switch (codeGenType) {
            case HTML -> htmlCodeFileSaverTemplate.saveCode((HtmlCodeResult) codeResult, appId);
            case MULTI_FILE -> multiFileCodeFileSaverTemplate.saveCode((MultiFileCodeResult) codeResult, appId);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型" + codeGenType.getValue());
        };
    }
}
