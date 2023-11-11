package com.huii.auth.service.impl;

import cn.hutool.core.codec.Base64Encoder;
import com.google.code.kaptcha.Producer;
import com.huii.auth.config.properties.LoginProperties;
import com.huii.auth.core.entity.Captcha;
import com.huii.auth.core.entity.LoginEntity;
import com.huii.auth.core.entity.PointDto;
import com.huii.auth.core.entity.RectangleDto;
import com.huii.auth.kaptcha.KaptchaService;
import com.huii.auth.service.LoginCaptchaService;
import com.huii.auth.utils.CaptchaGenerator;
import com.huii.auth.utils.ExpressionGenerator;
import com.huii.common.constants.CacheConstants;
import com.huii.common.constants.CaptchaConstants;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.message.core.service.impl.AliyunSmsServiceImpl;
import com.huii.message.core.service.impl.MailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LoginCaptchaServiceImpl implements LoginCaptchaService {

    static final String BASE64_PREFIX = "data:image/jpeg;base64,";
    private final LoginProperties loginProperties;
    private final RedisTemplateUtils redisTemplateUtils;
    private final KaptchaService kaptchaService;
    private final AliyunSmsServiceImpl smsService;
    private final MailServiceImpl mailService;

    @Override
    public void checkVerifyCode(String username, LoginEntity entity) {
        if (loginProperties.getAuthTries() <= 0) {
            return;
        }
        Integer cacheRetryTimes = redisTemplateUtils.getCacheObject(CacheConstants.VERIFY_TIMES + username);
        if (cacheRetryTimes != null && cacheRetryTimes > loginProperties.getAuthTries()) {
            //校验验证码
            String code = entity.getCode();
            String cacheCode = redisTemplateUtils.getCacheObject(CacheConstants.VERIFY_CODE + entity.getUuid());
            if (ObjectUtils.isEmpty(cacheCode)) {
                ResType type = ResType.USER_CAPTCHA_EXPIRED;
                throw new ServiceException(type.getCode(), MessageUtils.message(type.getI18n()));
            }
            if (!cacheCode.equals(code)) {
                ResType type = ResType.USER_CAPTCHA_ERROR;
                throw new ServiceException(type.getCode(), MessageUtils.message(type.getI18n()));
            }
            redisTemplateUtils.deleteObject(CacheConstants.VERIFY_CODE + entity.getUuid());
        }
    }

    @Override
    public void checkSlideCode(String key, Integer value) {
        Integer text = redisTemplateUtils.getCacheObject(CacheConstants.VERIFY_CODE + key);

        if (ObjectUtils.isEmpty(text)) {
            ResType rs = ResType.USER_CAPTCHA_EXPIRED;
            throw new ServiceException(rs.getCode(), MessageUtils.message(rs.getI18n()));
        }
        if (Math.abs(text - value) > CaptchaConstants.CAPTCHA_SLIDE_ALLOW_DEVIATION) {
            ResType rs = ResType.USER_CAPTCHA_NOT_PASS;
            throw new ServiceException(rs.getCode(), MessageUtils.message(rs.getI18n()));
        }
    }

    @Override
    public void checkClickTextCode(String key, PointDto[] points) {
        RectangleDto[] dto = redisTemplateUtils.getCacheObject(CacheConstants.VERIFY_CODE + key);
        if (dto.length != points.length || points.length <= 3) {
            ResType rs = ResType.USER_CAPTCHA_NOT_PASS;
            throw new ServiceException(rs.getCode(), MessageUtils.message(rs.getI18n()));
        }
        boolean flag = false;
        for (int i = 0; i < dto.length; i++) {
            if (Math.abs(dto[i].getX() - Math.floor(points[i].getX())) <=
                    (double) dto[i].getWidth() / 2 + CaptchaConstants.CAPTCHA_CLICK_ALLOW_WIDTH_DEVIATION) {
                flag = true;
            }
            if (Math.abs(dto[i].getY() - Math.floor(points[i].getY())) <=
                    (double) dto[i].getHeight() / 2 + CaptchaConstants.CAPTCHA_CLICK_ALLOW_HEIGHT_DEVIATION) {
                flag = true;
            }
            if (flag) {
                ResType rs = ResType.USER_CAPTCHA_NOT_PASS;
                throw new ServiceException(rs.getCode(), MessageUtils.message(rs.getI18n()));
            }
        }
    }

    @Override
    public Map<String, Object> createTextCaptcha(Integer minute) {
        Producer producer = kaptchaService.kaptchaProvider();
        String key = UUID.randomUUID().toString();
        String code = producer.createText();
        Map<String, Object> map = codeMapBuilder(key, code, code);
        saveCode(key, code, minute);
        map.remove("code");
        return map;
    }

    @Override
    public Map<String, Object> createCalcCaptcha(Integer minute) {
        ExpressionGenerator.ExpressionResult result = ExpressionGenerator.generateExpression();
        String key = UUID.randomUUID().toString();
        String code = Integer.toString(result.result);
        Map<String, Object> map = codeMapBuilder(key, code, result.expression);
        saveCode(key, code, minute);
        map.remove("code");
        return map;
    }

    @Override
    public Captcha createSlideCaptcha(Captcha captcha, Integer minute) {
        Captcha slideCaptcha = CaptchaGenerator.createImgSlideCaptcha(captcha);
        saveCode(captcha.getNonceStr(), captcha.getBlockX(), minute);
        slideCaptcha.setBlockX(null);
        return slideCaptcha;
    }

    @Override
    public Captcha createClickTextCaptcha(Captcha captcha, Integer minute) {
        Captcha clickCaptcha = CaptchaGenerator.createClickTextCaptcha(captcha);
        saveCode(captcha.getNonceStr(), captcha.getRectangles(), minute);
        captcha.setRectangles(null);
        return clickCaptcha;
    }

    @Override
    public void createLoginSmsCode(String phone, String template, Integer minute) {
        String random = CaptchaGenerator.createRandom(6);
        Map<String, String> params = new HashMap<>();
        params.put("code", random);
        smsService.send(phone, "SMS_272180191", params);
        saveCode(phone, random, minute);
    }

    @Override
    public void createLoginEmailCode(String email, String template, Integer minute) {
        String random = CaptchaGenerator.createRandom(6);
        String subject = "【huii】您的验证码为：" + random;
        String content = "您好,您的验证码为： " + random + "  验证码有效期为" + minute + "分钟，请勿泄露于他人!";
        mailService.sendText(subject, content, email);
        saveCode(email, random, minute);
    }

    private Map<String, Object> codeMapBuilder(String key, String code, String text) {
        try {
            Producer producer = kaptchaService.kaptchaProvider();
            BufferedImage image = producer.createImage(text);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            String captcha = BASE64_PREFIX + Base64Encoder.encode(outputStream.toByteArray());
            HashMap<String, Object> map = new HashMap<>();
            map.put("key", key);
            map.put("code", code);
            map.put("captcha", captcha);
            return map;
        } catch (IOException e) {
            throw new ServiceException("验证码生成异常");
        }
    }

    private void saveCode(String keySuffix, Object value, Integer minutes) {
        redisTemplateUtils.setCacheObject(CacheConstants.VERIFY_CODE + keySuffix, value, minutes, TimeUnit.MINUTES);
    }
}
