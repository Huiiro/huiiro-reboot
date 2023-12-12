package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.system.domain.SysNotice;
import com.huii.system.mapper.SysNoticeMapper;
import com.huii.system.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {

    private final SysNoticeMapper sysNoticeMapper;

    @Override
    public Page selectNoticeList(SysNotice sysNotice, PageParam pageParam) {
        IPage<SysNotice> iPage = new PageParamUtils<SysNotice>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(sysNotice)));
    }

    @Override
    public SysNotice selectNoticeById(Long id) {
        return sysNoticeMapper.selectById(id);
    }

    @Override
    public void checkInsert(SysNotice sysNotice) {
        if (sysNoticeMapper.exists(new LambdaQueryWrapper<SysNotice>()
                .eq(SysNotice::getNoticeTitle, sysNotice.getNoticeTitle()))) {
            throw new ServiceException("通知标题重复");
        }
    }

    @Override
    public void insertNotice(SysNotice sysNotice) {
        sysNoticeMapper.insert(sysNotice);
    }

    @Override
    public void checkUpdate(SysNotice sysNotice) {
        SysNotice oldOne = sysNoticeMapper.selectById(sysNotice.getNoticeId());
        if (!StringUtils.equals(sysNotice.getNoticeTitle(), oldOne.getNoticeTitle())) {
            if (sysNoticeMapper.exists(new LambdaQueryWrapper<SysNotice>()
                    .eq(SysNotice::getNoticeTitle, sysNotice.getNoticeTitle()))) {
                throw new ServiceException("通知标题重复");
            }
        }
    }

    @Override
    public void updateNotice(SysNotice sysNotice) {
        sysNoticeMapper.updateById(sysNotice);
    }

    @Override
    public void deleteNotice(Long[] ids) {
        sysNoticeMapper.deleteBatchIds(Arrays.asList(ids));
    }

    private LambdaQueryWrapper<SysNotice> wrapperBuilder(SysNotice notice) {
        Map<String, Object> params = notice.getParams();
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(notice.getNoticeTitle()), SysNotice::getNoticeTitle, notice.getNoticeTitle())
                .eq(ObjectUtils.isNotEmpty(notice.getNoticeStatus()), SysNotice::getNoticeStatus, notice.getNoticeStatus())
                .eq(ObjectUtils.isNotEmpty(notice.getNoticeType()), SysNotice::getNoticeType, notice.getNoticeType())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) && ObjectUtils.isNotEmpty(params.get("endTime")),
                        SysNotice::getCreateTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")))
                .orderByAsc(SysNotice::getCreateTime);
        return wrapper;
    }
}
