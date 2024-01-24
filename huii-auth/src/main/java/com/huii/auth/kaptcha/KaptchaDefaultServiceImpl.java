package com.huii.auth.kaptcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.stereotype.Service;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;

/**
 * kaptcha实现类
 *
 * @author huii
 */
@Service
public class KaptchaDefaultServiceImpl implements KaptchaService {
    @Override
    public DefaultKaptcha kaptchaProvider() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty(KAPTCHA_BORDER, "no");
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "45");
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "5");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "40");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier,cmr10,宋体,楷体,微软雅黑");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
