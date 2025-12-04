package com.slender.message;

public interface ExceptionMessage {
    String TOKEN_NOT_FOUND = "请先登录";
    String INTERNAL_ERROR = "服务器异常";
    String TOKEN_SIGNATURE_ERROR = "token签名错误";
    String TOKEN_EXPIRE_ERROR = "token已过期";
    String LOGIN_NOT_EXPIRED_ERROR = "登录还未过期";
    String LOGIN_EXPIRED_ERROR = "登陆已过期";
    String BLOCK_ERROR = "你已进入黑名单";
    String REQUEST_METHOD_ERROR="请求方法错误";
    String REQUEST_BODY_ERROR="请求体错误";
    String AUTHORITY_ERROR = "权限不足";
    String UNKNOWN_ERROR = "未知错误";
    String HAS_LOGIN_ERROR = "你已登录";
    String CAPTCHA_NOT_FOUND = "验证码未找到";
    String CAPTCHA_ERROR = "验证码错误";
    String EMAIL_ERROR = "邮箱错误";
    String REQUEST_READ_ERROR = "HttpServletRequest读取失败";

    static String getLoginMessage(String column) {
        return (switch (column) {
            case "user_name" -> "用户名";
            case "email" -> "邮箱";
            case "phone_number" -> "手机号";
            default -> "<Unknown>";
        }) + "或密码错误";
    }
}
