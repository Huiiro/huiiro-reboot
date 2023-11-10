package com.huii.message.core.service;

import java.util.Map;

/**
 * 邮箱服务模板接口
 *
 * @author huii
 */
public interface MailService {

    /**
     * 发送模板邮件
     *
     * @param template 模板
     * @param params   模板参数
     * @param emails   邮箱
     */
    void send(String template, Map<String, String> params, String... emails);

    /**
     * 发送普通邮件
     *
     * @param subject 主题
     * @param content 内容
     * @param emails  邮箱
     */
    void sendText(String subject, String content, String... emails);

    /**
     * 发送html邮件
     *
     * @param subject 主题
     * @param content 内容
     * @param emails  邮箱
     */
    void sendHtml(String subject, String content, String... emails);

    /**
     * 发送附件邮件
     *
     * @param subject  主题
     * @param content  内容
     * @param filePath 文件路径
     * @param emails   邮箱
     */
    void sendAttachment(String subject, String content, String filePath, String... emails);
}
