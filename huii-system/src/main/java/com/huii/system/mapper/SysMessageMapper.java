package com.huii.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huii.common.core.model.base.BaseMapperPlus;
import com.huii.system.domain.SysMessage;
import com.huii.system.domain.vo.SysMessageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysMessageMapper extends BaseMapperPlus<SysMessage> {

    /**
     * 查询个人消息
     *
     * @param page         page
     * @param queryWrapper queryWrapper
     * @return pageVo
     */
    Page<SysMessageVo> selectMyMessageList(@Param("page") Page<SysMessage> page,
                                           @Param(Constants.WRAPPER) Wrapper<SysMessage> queryWrapper);
}
