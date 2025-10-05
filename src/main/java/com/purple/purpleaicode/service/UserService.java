package com.purple.purpleaicode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.purple.purpleaicode.model.dto.user.UserQueryRequest;
import com.purple.purpleaicode.model.entity.User;
import com.purple.purpleaicode.model.vo.LoginUserVO;
import com.purple.purpleaicode.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author purple
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册方法
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return long 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 获取脱敏的已登录用户信息
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 用户登录。
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param request       请求
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户。
     *
     * @param request 请求
     * @return 当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户注销
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取单个用户信息
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取用户列表
     * @param userList
     * @return
     */
    public List<UserVO> getUserVOList(List<User> userList);

    /**
     * 将查询请求转为QueryWrapper对象
     * @param userQueryRequest 用户查询请求
     * @return QueryWrapper对象
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 密码加密
     * @param password 密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String password);
}
