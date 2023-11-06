package com.huii.common.jackson;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.huii.common.annotation.SensitiveColumn;
import com.huii.common.jackson.service.SensitiveService;
import com.huii.common.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

/**
 * 脱敏策略实现
 *
 * @author huii
 * @see com.huii.common.strategy.SensitiveStrategy
 */
@Slf4j
public class SensitiveColumnSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private final HashMap<String, Function<String, String>> map = new HashMap<>();
    private String currentStrategy;

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try {
            SensitiveService sensitiveService = SpringUtils.getBean(SensitiveService.class);
            if (ObjectUtil.isNotNull(sensitiveService) && sensitiveService.isSensitive()) {
                jsonGenerator.writeString(map.get(currentStrategy).apply(s));
            } else {
                jsonGenerator.writeString(s);
            }
        } catch (BeansException e) {
            log.error("脱敏实现类获取失败, {}", e.getMessage());
            jsonGenerator.writeString(s);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        SensitiveColumn sensitiveColumn = beanProperty.getAnnotation(SensitiveColumn.class);
        if (Objects.nonNull(sensitiveColumn) && Objects.equals(String.class, beanProperty.getType().getRawClass())) {
            if (!map.containsKey(sensitiveColumn.strategy().getName())) {
                map.put(sensitiveColumn.strategy().getName(), sensitiveColumn.strategy().getDesensitized());
            }
            currentStrategy = sensitiveColumn.strategy().getName();
            return this;
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
