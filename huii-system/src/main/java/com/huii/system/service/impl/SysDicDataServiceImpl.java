package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.core.domain.SysDicData;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.system.mapper.SysDicDataMapper;
import com.huii.system.service.SysDicDataService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SysDicDataServiceImpl extends ServiceImpl<SysDicDataMapper, SysDicData> implements SysDicDataService {

    private final SysDicDataMapper sysDicDataMapper;

    @Override
    public Page selectDataList(SysDicData sysDicData, PageParam pageParam) {
        IPage<SysDicData> iPage = new PageParamUtils<SysDicData>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(sysDicData)));
    }

    @Override
    public List<SysDicData> selectDataList(SysDicData sysDicData) {
        return sysDicDataMapper.selectList(wrapperBuilder(sysDicData));
    }

    @Override
    public SysDicData selectDicDataById(Long id) {
        return sysDicDataMapper.selectById(id);
    }

    @Override
    public void checkInsertDicData(SysDicData sysDicData) {
    }

    @Override
    public void insertDicData(SysDicData sysDicData) {
        sysDicDataMapper.insert(sysDicData);
    }

    @Override
    public void checkUpdateDicData(SysDicData sysDicData) {
    }

    @Override
    public void updateDicData(SysDicData sysDicData) {
        sysDicDataMapper.updateById(sysDicData);
    }

    @Override
    public void deleteDicData(Long[] ids) {
        sysDicDataMapper.deleteBatchIds(Arrays.asList(ids));
    }

    private LambdaQueryWrapper<SysDicData> wrapperBuilder(SysDicData data) {
        Map<String, Object> params = data.getParams();
        LambdaQueryWrapper<SysDicData> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(data.getDataName()), SysDicData::getDataName, data.getDataName())
                .eq(StringUtils.isNotBlank(data.getDataType()), SysDicData::getDataType, data.getDataType())
                .eq(ObjectUtils.isNotEmpty(data.getDataStatus()), SysDicData::getDataStatus, data.getDataStatus())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) && ObjectUtils.isNotEmpty(params.get("endTime")),
                        SysDicData::getCreateTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")))
                .orderByAsc(SysDicData::getDataSeq)
                .orderByAsc(SysDicData::getCreateTime);
        return wrapper;
    }
}
