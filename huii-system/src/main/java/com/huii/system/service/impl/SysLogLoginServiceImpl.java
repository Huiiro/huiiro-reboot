package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.system.domain.SysLogLogin;
import com.huii.system.mapper.SysLogLoginMapper;
import com.huii.system.service.SysLogLoginService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SysLogLoginServiceImpl extends ServiceImpl<SysLogLoginMapper, SysLogLogin> implements SysLogLoginService {

    private final SysLogLoginMapper sysLogLoginMapper;

    @Override
    public Page selectSysLogLoginList(SysLogLogin sysLogLogin, PageParam pageParam) {
        IPage<SysLogLogin> iPage = new PageParamUtils<SysLogLogin>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(sysLogLogin)));
    }

    @Override
    public List<SysLogLogin> selectSysLogLoginList(SysLogLogin sysLogLogin) {
        return sysLogLoginMapper.selectList(wrapperBuilder(sysLogLogin));
    }

    @Override
    public void removeBatchByIds(Long[] ids) {
        sysLogLoginMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void removeAll() {
        sysLogLoginMapper.delete(null);
    }

    private LambdaQueryWrapper<SysLogLogin> wrapperBuilder(SysLogLogin logLogin) {
        Map<String, Object> params = logLogin.getParams();
        LambdaQueryWrapper<SysLogLogin> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(logLogin.getLoginUserName()), SysLogLogin::getLoginUserName, logLogin.getLoginUserName())
                .like(StringUtils.isNotBlank(logLogin.getLoginIp()), SysLogLogin::getLoginIp, logLogin.getLoginIp())
                .like(StringUtils.isNotBlank(logLogin.getLoginAddress()), SysLogLogin::getLoginAddress, logLogin.getLoginAddress())
                .like(StringUtils.isNotBlank(logLogin.getLoginOs()), SysLogLogin::getLoginOs, logLogin.getLoginOs())
                .like(StringUtils.isNotBlank(logLogin.getLoginBrowser()), SysLogLogin::getLoginBrowser, logLogin.getLoginBrowser())
                .eq(ObjectUtils.isNotEmpty(logLogin.getLoginType()), SysLogLogin::getLoginType, logLogin.getLoginType())
                .eq(StringUtils.isNotBlank(logLogin.getLoginStatus()), SysLogLogin::getLoginStatus, logLogin.getLoginStatus())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) && ObjectUtils.isNotEmpty(params.get("endTime")),
                        SysLogLogin::getLoginTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")))
                .orderByAsc(SysLogLogin::getLoginTime);
        return wrapper;
    }
}
