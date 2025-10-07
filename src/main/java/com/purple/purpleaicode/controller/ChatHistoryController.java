package com.purple.purpleaicode.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.purple.purpleaicode.annotation.AuthCheck;
import com.purple.purpleaicode.common.BaseResponse;
import com.purple.purpleaicode.common.ResultUtils;
import com.purple.purpleaicode.constant.UserConstant;
import com.purple.purpleaicode.exception.BusinessException;
import com.purple.purpleaicode.exception.ErrorCode;
import com.purple.purpleaicode.exception.ThrowUtils;
import com.purple.purpleaicode.model.dto.chat.ChatHistoryQueryRequest;
import com.purple.purpleaicode.model.entity.ChatHistory;
import com.purple.purpleaicode.model.entity.User;
import com.purple.purpleaicode.service.ChatHistoryService;
import com.purple.purpleaicode.service.UserService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 对话历史控制器
 *
 * @author purple
 */
@RestController
@RequestMapping("/chatHistory")
public class ChatHistoryController {

    @Resource
    private ChatHistoryService chatHistoryService;

    @Resource
    private UserService userService;

    /**
     * 游标查询对话历史
     *
     * @param chatHistoryQueryRequest 查询请求
     * @param request                 HTTP请求
     * @return 对话历史结果
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<ChatHistory>> listAppChatHistory(
            @RequestBody ChatHistoryQueryRequest chatHistoryQueryRequest,
            HttpServletRequest request) {
        // 参数校验
        ThrowUtils.throwIf(chatHistoryQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 执行分页查询
        Page<ChatHistory> page = chatHistoryService.listAppChatHistoryByPage(chatHistoryQueryRequest.getAppId(),
                chatHistoryQueryRequest.getPageSize(), chatHistoryQueryRequest.getLastCreateTime(), loginUser);

        return ResultUtils.success(page);
    }

    /**
     * 管理员分页查询所有对话历史
     *
     * @param chatHistoryQueryRequest 查询请求
     * @return 分页结果
     */
    @PostMapping("/admin/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<ChatHistory>> listAllChatHistoryByPageForAdmin(
            @RequestBody ChatHistoryQueryRequest chatHistoryQueryRequest) {
        // 参数校验
        ThrowUtils.throwIf(chatHistoryQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = chatHistoryQueryRequest.getPageNum();
        long pageSize = chatHistoryQueryRequest.getPageSize();

        // 查询数据
        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(chatHistoryQueryRequest);
        Page<ChatHistory> page = chatHistoryService.page(Page.of(pageNum, pageSize), queryWrapper);

        return ResultUtils.success(page);
    }
}
