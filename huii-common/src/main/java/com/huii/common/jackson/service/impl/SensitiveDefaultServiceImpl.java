package com.huii.common.jackson.service.impl;

import com.huii.common.jackson.service.SensitiveService;
import org.springframework.stereotype.Service;

/**
 * 脱敏策略默认实现
 *
 * @author huii
 */
@Service
public class SensitiveDefaultServiceImpl implements SensitiveService {

    @Override
    public boolean isSensitive() {
        //TODO
        //admin return false else return true
        return true;
    }
}
