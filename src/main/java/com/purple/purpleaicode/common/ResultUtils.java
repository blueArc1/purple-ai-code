package com.purple.purpleaicode.common;

import com.purple.purpleaicode.exception.ErrorCode;

public class ResultUtils {

    /**
     * 成功
     * @param <T>   泛型类型，表示响应数据的类型
     * @param data  要返回的数据
     * @return      响应
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     * @param errorCode 错误码
     * @return          响应
     */
    public static BaseResponse<?> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     * @param code      错误码
     * @param message   错误信息
     * @return          响应
     */
    public static BaseResponse<?> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 失败
     *
     * @param errorCode 错误码
     * @return 响应
     */
    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
