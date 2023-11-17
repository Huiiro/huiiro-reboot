package com.huii.common.utils.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;


/**
 * Redis Serializer
 * redis序列化工具
 * 从fastjson升级为jackson
 *
 * @author huii
 **/
public class RedisSerialJacksonUtils<T> implements RedisSerializer<T> {

    private final Class<T> clazz;
    private final ObjectMapper objectMapper;

    public RedisSerialJacksonUtils(Class<T> clazz, ObjectMapper objectMapper) {
        super();
        this.clazz = clazz;
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        try {
            return objectMapper.writeValueAsBytes(t);
        } catch (Exception e) {
            throw new SerializationException("Error serializing object to byte[]", e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing object from byte[]", e);
        }
    }
}
