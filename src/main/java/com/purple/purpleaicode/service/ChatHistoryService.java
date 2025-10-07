package com.purple.purpleaicode.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.purple.purpleaicode.model.dto.chat.ChatHistoryQueryRequest;
import com.purple.purpleaicode.model.entity.ChatHistory;
import com.purple.purpleaicode.model.entity.User;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author purple
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 加载对话历史
     * @param appId 应用ID
     * @param chatMemory 消息窗口
     * @param maxCount 最大数量
     * @return 加载数量
     */
    int loadChatHistory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);

    /**
     * 新增对话历史
     * @param appId 应用ID
     * @param message 用户消息
     * @param messageType 消息类型
     * @param userId 用户ID
     * @return 保存结果
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * 根据应用ID删除所有对话历史
     * @param appId 应用ID
     * @return 是否删除成功
     */
    boolean deleteByAppId(Long appId);

    /**
     * 获取查询包装类
     * @param chatHistoryQueryRequest 查询请求
     * @return 查询条件
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    /**
     * 游标查询对话历史
     * @param appId 应用ID
     * @param pageSize 每页大小
     * @param lastCreateTime 上次查询最后一条记录的创建时间
     * @param loginUser 登录用户
     * @return 对话历史结果
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize, LocalDateTime lastCreateTime
            , User loginUser);
}
