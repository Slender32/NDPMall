package com.slender.message;

public interface FilterMessage {
    String REQUEST_BODY_PARAMETER_ERROR = "请求体参数错误";
    String REQUEST_METHOD_ERROR="请求方法错误";
    String REQUEST_BODY_ERROR="请求体错误";
    String REQUEST_BODY_NULL_ERROR="请求体为空";
    String EMAIL_ERROR = "邮箱错误";
    String CAPTCHA_EXPIRED = "验证码已过期";
    String CAPTCHA_ERROR = "验证码错误";
    String LOGOUT_SUCCESS = "登出成功";

    static String getMessage(String column) {
        return (switch (column) {
            case "user_name" -> "用户名";
            case "email" -> "邮箱";
            case "phone_number" -> "手机号";
            default -> "<Unknown>";
        }) + "或密码错误";
    }

    String LOGIN_SUCCESS = "登录成功";
}
