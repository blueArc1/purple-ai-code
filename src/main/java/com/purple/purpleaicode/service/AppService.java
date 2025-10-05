package com.purple.purpleaicode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.purple.purpleaicode.model.dto.app.AppQueryRequest;
import com.purple.purpleaicode.model.dto.app.AppVO;
import com.purple.purpleaicode.model.entity.App;
import com.purple.purpleaicode.model.entity.User;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author purple
 */
public interface AppService extends IService<App> {

    /**
     * 聊天生成代码
     * @param appId 应用 ID
     * @param message 消息
     * @param loginUser 登录用户
     * @return 代码
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    /**
     * 部署应用
     * @param appId 应用 ID
     * @param loginUser 登录用户
     * @return 部署URL
     */
    String deployApp(Long appId, User loginUser);

    /**
     * 获取AppVO
     * @param app 应用
     * @return AppVO
     */
    AppVO getAppVO(App app);

    /**
     * 获取查询对象
     * @param appQueryRequest
     * @return 查询对象
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 获取应用列表
     * @param appList 原应用列表
     * @return 应用列表
     */
    List<AppVO> getAppVOList(List<App> appList);
}
