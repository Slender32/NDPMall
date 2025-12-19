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
    String CAPTCHA_NOT_FOUND = "验证码不存在";
    String CAPTCHA_ERROR = "验证码错误";
    String EMAIL_ERROR = "邮箱错误";
    String REQUEST_READ_ERROR = "HttpServletRequest读取失败";
    String LOCAL_CACHE_ERROR = "本地缓存错误";
    String LOGIN_STATUS_ERROR = "登陆状态错误";
    String USER_NOT_FOUND = "用户不存在";
    String CAPTCHA_PERSISTENCE_ERROR = "验证码储存错误";
    String ILLEGAL_OPERATION = "非法操作";
    String ADDRESS_NOT_FOUND = "地址不存在";
    String MERCHANT_NOT_FOUND = "商家不存在";
    String PRODUCT_NOT_FOUND = "产品不存在";
    String ORDER_NOT_FOUND = "订单不存在";
    String ORDER_NOT_PAID_SUCCESS = "订单未支付成功";
    String OSS_ERROR = "上传文件失败";
    String FILE_NAME_NOT_FOUND = "文件名为空";
    String FILE_NAME_ERROR = "文件名不含后缀";
    String EXCEL_EXPORT_ERROR = "excel导出失败";
    String LOGIN_ERROR = "登陆失败";
    String DUPLICATE_FAVOURITE = "重复收藏";
    String FAVOURITE_NOT_FOUND = "未收藏该商品";
    String REVIEW_NOT_FOUND = "评论未找到";

    static String getLoginMessage(String column) {
        return (switch (column) {
            case "user_name" -> "用户名";
            case "email" -> "邮箱";
            case "phone_number" -> "手机号";
            default -> "<Unknown>";
        }) + "或密码错误";
    }
}
