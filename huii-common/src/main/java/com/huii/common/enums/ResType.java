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

    SYS_CACHE_REFRESH_SUCCESS(0, "sys.cache.refresh.success", "缓存刷新成功"),
    SYS_ILLEGAL_INPUT_ARG(500, "sys.illegal.input.arg", "非法的参数输入"),
    SYS_AUTO_INJECT_ERROR(500, "sys.auto.inject.error", "自动注入异常"),
    SYS_CODE_GEN_ERROR(500, "sys.code.gen.error", "验证码生成失败"),

    SYS_ADMIN_NOT_ALLOW_UPDATE(5000, "sys.admin.not.allow.update", "无法修改管理员信息"),
    SYS_ADMIN_NOT_ALLOW_BAN(5001, "sys.admin.not.allow.ban", "无法禁用管理员角色"),
    SYS_ADMIN_NOT_ALLOW_DELETE(5002, "sys.admin.not.allow.delete", "无法删除管理员"),
    SYS_CURRENT_USER_NOT_ALLOW_DELETE(5002, "sys.current.user.not.allow.delete", "无法删除当前用户"),

    SYS_CONFIG_NAME_REPEAT(5100, "sys.config.name.repeat", "配置名称重复"),
    SYS_CONFIG_KEY_REPEAT(5101, "sys.config.key.repeat", "配置key重复"),

    SYS_DEPT_NAME_REPEAT(5110, "sys.dept.name.repeat", "部门名称重复"),
    SYS_DEPT_NOT_ALLOW_SET_PARENT(5111, "sys.dept.not.allow.set.parent", "不允许的操作,无法将自己作为上级部门"),
    SYS_DEPT_EXISTS_CHILDREN(5112, "sys.dept.exists.children", "不允许的操作,该部门下存在子部门"),
    SYS_DEPT_EXISTS_USER(5113, "sys.dept.exists.user", "不允许的操作,存在用户已分配该部门"),

    SYS_DIC_TYPE_NAME_REPEAT(5120, "sys.dic.type.name.repeat", "字典名称重复"),
    SYS_DIC_TYPE_REPEAT(5121, "sys.dic.type.repeat", "字典类型重复"),

    SYS_FILE_UPLOAD_FAIL(5130, "sys.file.upload.fail", "文件上传失败"),
    SYS_FILE_DELETE_FAIL(5131, "sys.file.delete.fail", "文件删除失败"),
    SYS_FILE_NOT_EXIST(5132, "sys.file.not.exist", "文件不存在或已被删除"),

    SYS_MENU_NAME_REPEAT(5140, "sys.menu.name.repeat", "菜单名称字段重复"),
    SYS_MENU_AUTH_REPEAT(5141, "sys.menu.auth.repeat", "菜单权限字段重复"),
    SYS_MENU_PATH_REPEAT(5142, "sys.menu.path.repeat", "菜单路径字段重复"),
    SYS_MENU_NOT_ALLOW_SET_PARENT(5143, "sys.menu.not.allow.set.parent", "不允许的操作,无法将自己作为上级菜单"),
    SYS_MENU_EXISTS_CHILDREN(5144, "sys.menu.exists.children", "不允许的操作,该菜单下存在子菜单"),
    SYS_MENU_EXISTS_ROLE(5145, "sys.menu.exists.user", "不允许的操作,存在角色已分配该菜单"),

    SYS_NOTICE_TITLE_REPEAT(5150, "sys.notice.title.repeat", "通知标题重复"),

    SYS_POST_NAME_REPEAT(5160, "sys.post.name.repeat", "岗位名称重复"),
    SYS_POST_KEY_REPEAT(5161, "sys.post.key.repeat", "岗位编码重复"),
    SYS_POST_EXISTS_USER(5162, "sys.post.exists.user", "不允许的操作,{post}岗位下已分配用户"),

    SYS_ROLE_NAME_REPEAT(5170, "sys.role.name.repeat", "角色名称重复"),
    SYS_ROLE_KEY_REPEAT(5171, "sys.role.key.repeat", "角色编码重复"),
    SYS_ROLE_EXISTS_USER(5172, "sys.role.exists.user", "不允许的操作,{post}角色下已分配用户"),

    SYS_USER_NAME_REPEAT(5180, "sys.user.name.repeat", "用户名称重复"),
    SYS_USER_PHONE_REPEAT(5181, "sys.user.phone.repeat", "手机号码重复"),
    SYS_USER_EMAIL_REPEAT(5182, "sys.user.email.repeat", "邮箱号码重复"),

    GEN_MULTI_PRIMARY_KEY(5190,"gen.multi.primary.key", "存在多个主键，请检查"),
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
