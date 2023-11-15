package com.huii.common.enums;

import com.huii.common.utils.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回结果枚举类
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum ResType {
    STATUS_UNAUTHORIZED(401, "status.unauthorized", "登录信息失效，请重新登录"),
    STATUS_FORBIDDEN(403, "status.forbidden", "未持有相关权限，请联系管理员"),
    STATUS_NOT_FOUND(404, "status.not.found", "您访问的页面或资源不存在"),
    STATUS_METHOD_NOT_ALLOWED(405, "status.method.not.allowed", "不支持的访问方法"),
    STATUS_TOKEN_EXPIRED(407, "status.token.expired", "登录信息失效，请重新登录"),
    STATUS_REQUEST_TIMEOUT(408, "status.request.timeout", "请求超时"),
    STATUS_UNSUPPORTED_MEDIA_TYPE(415, "status.unsupported.media.type", "不支持的媒体类型"),
    STATUS_TOO_MANY_REQUESTS(429, "status.too.many.requests", "请求频繁，请稍后尝试"),
    STATUS_SERVER_ERROR(500, "status.server.error", "服务器繁忙，请稍后尝试"),
    STATUS_UNKNOWN_ERROR(501, "status.unknown.error", "遇到未知问题了，请稍后再试吧"),

    USER_BANNED(1001, "user.banned", "账户已被禁用"),
    USER_NOT_EXIST(1002, "user.not.exist", "用户不存在"),
    USER_NOT_LOGIN(1003, "user.not.login", "用户未登录"),
    USER_NOT_REGISTER(1004, "user.not.register", "用户未注册"),
    USER_NOT_ALLOW_IP(1005, "user.not.allow.ip", "您所在的地区暂时无法访问"),

    USER_USERNAME_NOT_BLANK(1201, "user.username.not.blank", "用户名不能为空"),
    USER_USERNAME_LENGTH_VALID(1202, "user.username.length.valid", "用户名长度必须在{min}至{max}位之间"),
    USER_PASSWORD_NOT_BLANK(1203, "user.password.not.blank", "密码不能为空"),
    USER_PASSWORD_LENGTH_VALID(1204, "user.password.length.valid", "密码长度必须在{min}至{max}位之间"),
    USER_EMAIL_NOT_BLANK(1205, "user.email.not.blank", "邮箱不能为空"),
    USER_EMAIL_LENGTH_VALID(1206, "user.email.length.valid", "邮箱长度必须在{min}至{max}位之间"),
    USER_EMAIL_FORM_INVALID(1207, "user.email.form.invalid", "邮箱格式错误"),
    USER_PHONE_NOT_BLANK(1208, "user.phone.not.blank", "手机号码不能为空"),
    USER_PHONE_LENGTH_VALID(1209, "user.phone.length.valid", "手机号码长度必须在{min}至{max}位之间"),
    USER_PHONE_FORM_INVALID(1210, "user.phone.form.invalid", "手机号码格式错误"),

    USER_REGISTER_SUCCESS(0, "user.register.success", "注册成功"),
    USER_REGISTER_FAIL(1301, "user.register.fail", "注册失败"),
    USER_REGISTER_CLOSE(1302, "user.register.close", "暂未开放注册功能"),
    USER_REGISTER_NAME_REPEAT(1303, "user.register.name.repeat", "该名称已被注册"),

    USER_LOGIN_SUCCESS(0, "user.login.success", "登录成功"),
    USER_LOGIN_PASSWORD_ERROR(1401, "user.login.password.error", "用户名或密码错误"),
    USER_LOGIN_TRY_MAX(1402, "user.login.try.max", "账号已暂时冻结，请{var1}分钟后再试"),

    USER_LOGOUT_SUCCESS(0, "user.logout.success", "退出登录成功"),

    USER_INFO_UPDATE_SUCCESS(0, "user.info.update.success", "用户信息已更新"),
    USER_PASSWORD_UPDATE_SUCCESS(0, "user.password.update.success", "密码已更新，请重新登录"),

    USER_CAPTCHA_SEND_SUCCESS(0, "user.captcha.send.success", "验证码已发送"),
    USER_CAPTCHA_SEND_FAIL(1000, "user.captcha.send.fail", "验证码发送失败"),
    USER_CAPTCHA_ERROR(1403, "user.captcha.error", "验证码错误"),
    USER_CAPTCHA_EXPIRED(1404, "user.captcha.expired", "验证码不存在或已失效"),
    USER_CAPTCHA_NOT_BLANK(1405, "user.captcha.not.blank", "验证码不为空"),
    USER_CAPTCHA_NOT_PASS(1406, "user.captcha.not.pass", "验证未通过"),

    ANNOTATION_XSS_DEFAULT(2001, "annotation.xss.default", "含有非法字符"),
    ANNOTATION_RATE_LIMIT_DEFAULT(2002, "annotation.rate.limit.default", "您的请求过于频繁，请稍后再试"),
    ANNOTATION_REPEAT_SUBMIT_DEFAULT(2003, "annotation.repeat.submit.default", "不允许重复提交，请稍候再试"),

    COMMON_QUERY_SUCCESS(0, "common.query.success", "查询成功"),
    COMMON_GRANT_SUCCESS(0, "common.grant.success", "授权成功"),
    COMMON_CLEAN_SUCCESS(0, "common.clean.success", "清空成功"),
    COMMON_INSERT_SUCCESS(0, "common.insert.success", "添加成功"),
    COMMON_UPDATE_SUCCESS(0, "common.update.success", "更新成功"),
    COMMON_DELETE_SUCCESS(0, "common.delete.success", "删除成功"),
    COMMON_IMPORT_SUCCESS(0, "common.import.success", "导入成功"),
    COMMON_EXPORT_SUCCESS(0, "common.export.success", "导出成功"),
    COMMON_SUBMIT_SUCCESS(0, "common.submit.success", "提交成功"),
    COMMON_PUBLISH_SUCCESS(0, "common.publish.success", "发表成功"),
    COMMON_REQUEST_SUCCESS(0, "common.request.success", "请求成功"),
    COMMON_GENERATE_SUCCESS(0, "common.generate.success", "生成成功"),
    COMMON_IMPORT_ERROR(7001, "common.import.error", "导入失败"),
    COMMON_EXPORT_ERROR(7002, "common.export.error", "导出失败"),
    ;

    private final Integer code;
    private final String i18n;
    private final String message;

    /**
     * 获取I18n信息
     */
    public static String getI18nMessage(ResType resType) {
        return MessageUtils.message(resType.getI18n());
    }
}
