package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.CacheConstants;
import com.huii.common.core.domain.SysDicData;
import com.huii.common.core.domain.SysDicType;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.service.DicService;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.StreamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.system.mapper.SysDicDataMapper;
import com.huii.system.mapper.SysDicTypeMapper;
import com.huii.system.service.SysDicTypeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysDicTypeServiceImpl extends ServiceImpl<SysDicTypeMapper, SysDicType>
        implements SysDicTypeService, DicService {

    private final SysDicTypeMapper sysDicTypeMapper;
    private final SysDicDataMapper sysDicDataMapper;
    private final RedisTemplateUtils redisTemplateUtils;

    @Override
    public void clearCache() {
        Collection<String> keys = redisTemplateUtils.keys(CacheConstants.SYS_DICT + "*");
        redisTemplateUtils.deleteObject(keys);
    }

    @PostConstruct
    @Override
    public void load() {
        List<SysDicType> dicTypes = sysDicTypeMapper.selectList(null);
        dicTypes.forEach(type -> {
            List<SysDicData> dicData = sysDicDataMapper.selectList(new LambdaQueryWrapper<SysDicData>()
                    .eq(SysDicData::getDataType, type.getDicType())
                    .orderByAsc(SysDicData::getDataSeq));
            redisTemplateUtils.setCacheList(CacheConstants.SYS_DICT + type.getDicType(), dicData);
        });
    }

    @Override
    public void refreshCache() {
        clearCache();
        load();
    }

    @Override
    public Page selectTypeList(SysDicType sysDicType, PageParam pageParam) {
        IPage<SysDicType> iPage = new PageParamUtils<SysDicType>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(sysDicType)));
    }

    @Override
    public SysDicType selectDicTypeById(Long id) {
        return sysDicTypeMapper.selectById(id);
    }

    @Override
    public SysDicType selectDicTypeByDicType(String dicType) {
        return sysDicTypeMapper.selectByDicType(dicType);
    }

    @Override
    public void checkInsertDicType(SysDicType sysDicType) {
        if (sysDicTypeMapper.exists(new LambdaQueryWrapper<SysDicType>()
                .eq(SysDicType::getTypeName, sysDicType.getTypeName()))) {
            throw new ServiceException("字典名称重复");
        }
        if (sysDicTypeMapper.exists(new LambdaQueryWrapper<SysDicType>()
                .eq(SysDicType::getDicType, sysDicType.getDicType()))) {
            throw new ServiceException("字典类型重复");
        }
    }

    @Override
    public void insertDicType(SysDicType sysDicType) {
        sysDicTypeMapper.insert(sysDicType);
    }

    @Override
    public void checkUpdateDicType(SysDicType sysDicType) {
        SysDicType oldOne = sysDicTypeMapper.selectById(sysDicType.getTypeId());
        if (!StringUtils.equals(sysDicType.getTypeName(), oldOne.getTypeName())) {
            if (sysDicTypeMapper.exists(new LambdaQueryWrapper<SysDicType>()
                    .eq(SysDicType::getTypeName, sysDicType.getTypeName()))) {
                throw new ServiceException("字典名称重复");
            }
        }
        if (!StringUtils.equals(sysDicType.getDicType(), oldOne.getDicType())) {
            if (sysDicTypeMapper.exists(new LambdaQueryWrapper<SysDicType>()
                    .eq(SysDicType::getDicType, sysDicType.getDicType()))) {
                throw new ServiceException("字典类型重复");
            }
        }
    }

    @Override
    public void updateDicType(SysDicType sysDicType) {
        sysDicTypeMapper.updateById(sysDicType);
    }

    @Override
    public void deleteDicType(Long[] ids) {
        sysDicDataMapper.delete(new LambdaQueryWrapper<SysDicData>()
                .in(SysDicData::getDataType, Arrays.asList(ids)));
        sysDicTypeMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public String getDicValue(String type, String label, String separator) {
        Map<String, String> map = StreamUtils.toMap(loadExcelCastData(type), SysDicData::getDataLabel, SysDicData::getDataValue);
        if (StringUtils.containsAny(label, separator)) {
            return Arrays.stream(label.split(separator))
                    .map(l -> map.getOrDefault(l, StringUtils.EMPTY))
                    .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(label, StringUtils.EMPTY);
        }
    }

    @Override
    public String getDicLabel(String type, String value, String separator) {
        Map<String, String> map = StreamUtils.toMap(loadExcelCastData(type), SysDicData::getDataValue, SysDicData::getDataLabel);
        if (StringUtils.containsAny(value, separator)) {
            return Arrays.stream(value.split(separator))
                    .map(v -> map.getOrDefault(v, StringUtils.EMPTY))
                    .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(value, StringUtils.EMPTY);
        }
    }

    private List<SysDicData> loadExcelCastData(String type) {
        List<SysDicData> cacheList = redisTemplateUtils.getCacheList(CacheConstants.SYS_DICT + type);
        if(!cacheList.isEmpty()) {
            return cacheList;
        }
        return sysDicDataMapper.selectByType(type);
    }

    private LambdaQueryWrapper<SysDicType> wrapperBuilder(SysDicType type) {
        Map<String, Object> params = type.getParams();
        LambdaQueryWrapper<SysDicType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(type.getTypeName()), SysDicType::getTypeName, type.getTypeName())
                .like(StringUtils.isNotBlank(type.getDicType()), SysDicType::getDicType, type.getDicType())
                .eq(ObjectUtils.isNotEmpty(type.getTypeStatus()), SysDicType::getTypeStatus, type.getTypeStatus())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) && ObjectUtils.isNotEmpty(params.get("endTime")),
                        SysDicType::getCreateTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")))
                .orderByAsc(SysDicType::getCreateTime);
        return wrapper;
    }


}
