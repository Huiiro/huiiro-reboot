package com.huii.message.core.service.impl;

import com.huii.message.core.service.MailService;
import com.huii.message.domain.MsgMailTemplate;
import com.huii.message.enums.MailType;
import com.huii.message.exception.MailException;
import com.huii.message.helper.MailHelper;
import com.huii.message.mapper.MsgMailTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 邮件服务实现
 *
 * @author huii
 */
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final MailHelper mailHelper;
    private final MsgMailTemplateMapper mailTemplateMapper;

    @Override
    public void send(String template, Map<String, String> params, String... emails) {

        MsgMailTemplate msgTemplate = mailTemplateMapper.loadTemplate(template);
        if (ObjectUtils.isEmpty(msgTemplate)) {
            throw new MailException("模板加载失败");
        }
        String mailType = msgTemplate.getMailType();
        String content = contentBuilder(msgTemplate.getMailContent(), params);
        if (MailType.TEXT.getValue().equals(mailType)) {
            sendText(msgTemplate.getMailSubject(), content, emails);
        } else if (MailType.HTML.getValue().equals(mailType)) {
            sendHtml(msgTemplate.getMailSubject(), content, emails);
        } else if (MailType.ATTACH.getValue().equals(mailType)) {
            sendAttachment(msgTemplate.getMailSubject(), content, msgTemplate.getMailAttachFile(), emails);
        }
    }


    @Override
    public void sendText(String subject, String content, String... emails) {
        mailHelper.sendSimpleMail(subject, content, emails);
    }

    @Override
    public void sendHtml(String subject, String content, String... emails) {
        mailHelper.sendHtmlMail(subject, content, emails);
    }

    @Override
    public void sendAttachment(String subject, String content, String filePath, String... emails) {
        mailHelper.sendAttachmentsMail(subject, content, filePath, emails);
    }

    /**
     * 参数构造器
     * 参数占位符的格式为: ${var1}
     */
    private String contentBuilder(String mailContent, Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            mailContent = mailContent.replace(placeholder, entry.getValue());
        }
        return mailContent;
    }
}
