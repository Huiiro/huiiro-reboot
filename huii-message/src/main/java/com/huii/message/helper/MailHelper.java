package com.huii.message.helper;

import com.huii.message.config.properties.MailProperties;
import com.huii.message.exception.MailException;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * 邮件发送助手
 *
 * @author huii
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MailHelper {

    private final MailProperties mailProperties;
    private JavaMailSender mailSender;
    private boolean enableSend;

    @PostConstruct
    private void init() {
        enableSend = mailProperties.getEnableMail().equals("true");
        if (enableSend) {
            JavaMailSenderImpl myMailSender = new JavaMailSenderImpl();
            myMailSender.setDefaultEncoding("UTF-8");
            myMailSender.setHost(mailProperties.getHost());
            myMailSender.setProtocol(mailProperties.getProtocol());
            myMailSender.setPort(mailProperties.getPort());
            myMailSender.setUsername(mailProperties.getUsername());
            myMailSender.setPassword(mailProperties.getPassword());
            this.mailSender = myMailSender;
        }
    }

    /**
     * 简单文本邮件
     *
     * @param to      接收者邮件
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String subject, String content, String... to) {
        enableSend();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(mailProperties.getUsername());
        mailSender.send(message);
    }

    /**
     * HTML 文本邮件
     *
     * @param to      接收者邮件
     * @param subject 邮件主题
     * @param content HTML内容
     */
    public void sendHtmlMail(String subject, String content, String... to) {
        enableSend();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom(mailProperties.getUsername(), mailProperties.getSender());
            mailSender.send(message);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 附件邮件
     *
     * @param to       接收者邮件
     * @param subject  邮件主题
     * @param content  HTML内容
     * @param filePath 附件路径
     */
    public void sendAttachmentsMail(String subject, String content, String filePath, String... to) {
        enableSend();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom(mailProperties.getUsername(), mailProperties.getSender());
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = file.getFilename();
            if (StringUtils.isNotEmpty(fileName)) {
                helper.addAttachment(fileName, file);
            }
            mailSender.send(message);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void enableSend() {
        if (!enableSend) {
            throw new MailException("邮件功能未开启");
        }
    }
}