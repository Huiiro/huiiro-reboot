package com.huii.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.core.domain.SysDicData;
import com.huii.common.core.domain.SysDicType;
import com.huii.common.core.service.DicService;
import com.huii.common.utils.StreamUtils;
import com.huii.system.mapper.SysDicDataMapper;
import com.huii.system.mapper.SysDicTypeMapper;
import com.huii.system.service.SysDicTypeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysDicTypeServiceImpl extends ServiceImpl<SysDicTypeMapper, SysDicType>
        implements SysDicTypeService, DicService {

    private final SysDicTypeMapper sysDicTypeMapper;
    private final SysDicDataMapper sysDicDataMapper;

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
        //TODO load from cache
        List<SysDicData> dicData = new ArrayList<>();
        //else load from this and cache
        //SpringUtils.getAopProxy(this).selectDictDataByType(dictType);
        return dicData;
    }


}
