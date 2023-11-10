package com.huii.message.core.service.impl;

import com.huii.message.core.service.MailService;
import com.huii.message.helper.MailHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final MailHelper mailHelper;

    @Override
    public void send(String template, Map<String, String> params, String... emails) {
        //TODO
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
}
