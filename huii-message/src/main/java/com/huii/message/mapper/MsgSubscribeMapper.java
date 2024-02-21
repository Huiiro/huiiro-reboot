package com.huii.message.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huii.common.core.model.Label;
import com.huii.common.core.model.base.BaseMapperPlus;
import com.huii.message.domain.MsgSubscribe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  订阅详情数据层接口
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
@Mapper
public interface MsgSubscribeMapper extends BaseMapperPlus<MsgSubscribe> {

    /**
     * selectMsgSubscribeUserList
     *
     * @param userId  userId
     * @param page    page
     * @param wrapper wrapper
     * @return page
     */
    Page<MsgSubscribe> selectMsgSubscribeUserList(@Param("userId") Long userId,
                                                  @Param("page") Page<MsgSubscribe> page,
                                                  @Param(Constants.WRAPPER) LambdaQueryWrapper<MsgSubscribe> wrapper);

    /**
     * 获取订阅信息
     *
     * @return list
     */
    List<Label> selectLabelList();


    /**
     * 查询未订阅用户
     *
     * @param sid sid
     * @return idList
     */
    List<Long> selectNonSubscribeUserIds(Long sid);

    /**
     * 查询已订阅用户
     *
     * @param sid sid
     * @return idList
     */
    List<Long> selectSubscribeUserIds(Long sid);
}
