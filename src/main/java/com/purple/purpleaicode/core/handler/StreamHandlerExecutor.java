package com.purple.purpleaicode.core.handler;

import com.purple.purpleaicode.model.entity.User;
import com.purple.purpleaicode.model.enums.CodeGenTypeEnum;
import com.purple.purpleaicode.service.ChatHistoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * 流处理执行器
 */
@Slf4j
@Component
public class StreamHandlerExecutor {

    @Resource
    private JsonMessageStreamHandler jsonMessageStreamHandler;

    /**
     * 创建流处理器并处理聊天历史记录
     * @param originFlux            原始流
     * @param chatHistoryService    聊天历史服务
     * @param appId                 应用ID
     * @param loginUser             登录用户
     * @param codeGenType           代码生成类型
     * @return 处理后的流
     */
    public Flux<String> doExcute(Flux<String> originFlux, ChatHistoryService chatHistoryService,
                                 long appId, User loginUser, CodeGenTypeEnum codeGenType) {
        return switch (codeGenType) {
            case VUE_PROJECT -> // 使用注入的组件实例
                jsonMessageStreamHandler.handle(originFlux, chatHistoryService, appId, loginUser);
            case HTML, MULTI_FILE -> // 简单文本处理器不需要依赖注入
                new SimpleTextStreamHandler().handle(originFlux, chatHistoryService, appId, loginUser);
        };
    }
}
