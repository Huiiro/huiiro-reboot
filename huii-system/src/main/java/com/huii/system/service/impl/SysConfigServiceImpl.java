package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.CacheConstants;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.system.domain.SysConfig;
import com.huii.system.mapper.SysConfigMapper;
import com.huii.system.service.SysConfigService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    private final SysConfigMapper sysConfigMapper;
    private final RedisTemplateUtils redisTemplateUtils;

    @Override
    public void clearCache() {
        Collection<String> keys = redisTemplateUtils.keys(CacheConstants.SYS_CONFIG + "*");
        redisTemplateUtils.deleteObject(keys);
    }

    @PostConstruct
    @Override
    public void load() {
        List<SysConfig> configs = sysConfigMapper.selectList(null);
        configs.forEach(config -> redisTemplateUtils.setCacheObject(CacheConstants.SYS_CONFIG + config.getConfigKey(),
                config.getConfigValue()));
    }

    @Override
    public void refreshCache() {
        clearCache();
        load();
    }

    @Override
    public Page selectConfigList(SysConfig sysConfig, PageParam pageParam) {
        IPage<SysConfig> iPage = new PageParamUtils<SysConfig>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(sysConfig)));
    }

    @Override
    public SysConfig selectConfigById(Long id) {
        return sysConfigMapper.selectById(id);
    }

    @Override
    public void checkInsert(SysConfig sysConfig) {
        if (sysConfigMapper.exists(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigName, sysConfig.getConfigName()))) {
            ResType resType = ResType.SYS_CONFIG_NAME_REPEAT;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
        if (sysConfigMapper.exists(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, sysConfig.getConfigKey()))) {
            ResType resType = ResType.SYS_CONFIG_KEY_REPEAT;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
    }

    @Override
    public void insertConfig(SysConfig sysConfig) {
        sysConfigMapper.insert(sysConfig);
    }

    @Override
    public void checkUpdate(SysConfig sysConfig) {
        SysConfig oldOne = sysConfigMapper.selectById(sysConfig.getConfigId());
        if (!StringUtils.equals(sysConfig.getConfigName(), oldOne.getConfigName())) {
            if (sysConfigMapper.exists(new LambdaQueryWrapper<SysConfig>()
                    .eq(SysConfig::getConfigName, sysConfig.getConfigName()))) {
                ResType resType = ResType.SYS_CONFIG_NAME_REPEAT;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
        }
        if (!StringUtils.equals(sysConfig.getConfigKey(), oldOne.getConfigKey())) {
            if (sysConfigMapper.exists(new LambdaQueryWrapper<SysConfig>()
                    .eq(SysConfig::getConfigKey, sysConfig.getConfigKey()))) {
                ResType resType = ResType.SYS_CONFIG_KEY_REPEAT;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
        }
    }

    @Override
    public void updateConfig(SysConfig sysConfig) {
        sysConfigMapper.updateById(sysConfig);
    }

    @Override
    public void deleteConfig(Long[] ids) {
        sysConfigMapper.deleteBatchIds(Arrays.asList(ids));
    }

    private LambdaQueryWrapper<SysConfig> wrapperBuilder(SysConfig config) {
        Map<String, Object> params = config.getParams();
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(config.getConfigName()), SysConfig::getConfigName, config.getConfigName())
                .like(StringUtils.isNotBlank(config.getConfigKey()), SysConfig::getConfigKey, config.getConfigKey())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) && ObjectUtils.isNotEmpty(params.get("endTime")),
                        SysConfig::getCreateTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")));
        return wrapper;
    }
}
