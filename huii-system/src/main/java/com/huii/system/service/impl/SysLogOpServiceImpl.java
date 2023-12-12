package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.system.domain.SysLogOp;
import com.huii.system.mapper.SysLogOpMapper;
import com.huii.system.service.SysLogOpService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SysLogOpServiceImpl extends ServiceImpl<SysLogOpMapper, SysLogOp> implements SysLogOpService {

    private final SysLogOpMapper sysLogOpMapper;

    @Override
    public Page selectSysLogOpList(SysLogOp sysLogOp, PageParam pageParam) {
        IPage<SysLogOp> iPage = new PageParamUtils<SysLogOp>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(sysLogOp)));
    }

    @Override
    public List<SysLogOp> selectSysLogOpList(SysLogOp sysLogOp) {
        return sysLogOpMapper.selectList(wrapperBuilder(sysLogOp));
    }

    @Override
    public void removeBatchByIds(Long[] ids) {
        sysLogOpMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void removeAll() {
        sysLogOpMapper.delete(null);
    }

    private LambdaQueryWrapper<SysLogOp> wrapperBuilder(SysLogOp logOp) {
        Map<String, Object> params = logOp.getParams();
        LambdaQueryWrapper<SysLogOp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ObjectUtils.isNotEmpty(logOp.getOpType()), SysLogOp::getOpType, logOp.getOpType())
                .eq(StringUtils.isNotBlank(logOp.getOpMarkFlag()), SysLogOp::getOpMarkFlag, logOp.getOpMarkFlag())
                .eq(StringUtils.isNotBlank(logOp.getOpStatus()), SysLogOp::getOpStatus, logOp.getOpStatus())
                .eq(StringUtils.isNotBlank(logOp.getOpRequest()), SysLogOp::getOpRequest, logOp.getOpRequest())
                .like(StringUtils.isNotBlank(logOp.getOpUserName()), SysLogOp::getOpUserName, logOp.getOpUserName())
                .like(StringUtils.isNotBlank(logOp.getOpMethodName()), SysLogOp::getOpMethodName, logOp.getOpMethodName())
                .like(StringUtils.isNotBlank(logOp.getOpIp()), SysLogOp::getOpIp, logOp.getOpIp())
                .like(StringUtils.isNotBlank(logOp.getOpAddress()), SysLogOp::getOpAddress, logOp.getOpAddress())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) && ObjectUtils.isNotEmpty(params.get("endTime")),
                        SysLogOp::getOpTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")))
                .orderByAsc(SysLogOp::getOpTime);
        String costTime = logOp.getOpCostTime();
        if (ObjectUtils.isEmpty(costTime)) {
            return wrapper;
        }
        String[] split = costTime.split("-");
        if (split.length == 2) {
            int le = Integer.parseInt(split[0]);
            int ge = Integer.parseInt(split[1]);
            wrapper.between(SysLogOp::getOpCostTime, le, ge);
        } else if (split.length == 1) {
            String pattern = "^([<>]=?)?(\\d+)$";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(costTime);
            if(m.find()) {
                String operator = m.group(1);
                int value = Integer.parseInt(m.group(2));
                if ("<".equals(operator)) {
                    wrapper.lt(SysLogOp::getOpCostTime, value);
                } else if ("<=".equals(operator)) {
                    wrapper.le(SysLogOp::getOpCostTime, value);
                } else if (">".equals(operator)) {
                    wrapper.gt(SysLogOp::getOpCostTime, value);
                } else if (">=".equals(operator)) {
                    wrapper.ge(SysLogOp::getOpCostTime, value);
                } else {
                    wrapper.eq(SysLogOp::getOpCostTime, value);
                }
            }
        }
        return wrapper;
    }
}
