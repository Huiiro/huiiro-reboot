package com.huii.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.model.Label;
import com.huii.common.core.model.PageParam;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.PageParamUtils;
import com.huii.message.domain.MsgSubscribe;
import com.huii.message.domain.MsgSubscribeUser;
import com.huii.message.mapper.MsgSubscribeMapper;
import com.huii.message.mapper.MsgSubscribeUserMapper;
import com.huii.message.service.MsgSubscribeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MsgSubscribeServiceImpl extends ServiceImpl<MsgSubscribeMapper, MsgSubscribe>
        implements MsgSubscribeService {

    private final MsgSubscribeMapper msgSubscribeMapper;
    private final MsgSubscribeUserMapper msgSubscribeUserMapper;

    @Override
    public com.huii.common.core.model.Page selectMsgSubscribeList(MsgSubscribe msgSubscribe, PageParam pageParam) {
        IPage<MsgSubscribe> iPage = new PageParamUtils<MsgSubscribe>().getPageInfo(pageParam);
        return new com.huii.common.core.model.Page(this.page(iPage, wrapperBuilder(msgSubscribe)));
    }

    @Override
    public com.huii.common.core.model.Page selectMsgSubscribeUserList(Long userId, MsgSubscribe msgSubscribe, PageParam pageParam) {
        Page<MsgSubscribe> pageInfo = new PageParamUtils<MsgSubscribe>().getPage(pageParam);
        Page<MsgSubscribe> page = msgSubscribeMapper.selectMsgSubscribeUserList(userId, pageInfo, wrapperBuilder(msgSubscribe));
        return new com.huii.common.core.model.Page(page);
    }

    @Override
    public List<Label> getLabelList() {
        return msgSubscribeMapper.selectLabelList();
    }

    @Override
    public MsgSubscribe selectMsgSubscribeById(Long id) {
        return msgSubscribeMapper.selectById(id);
    }

    @Override
    public void checkInsert(MsgSubscribe msgSubscribe) {
        if (msgSubscribeMapper.exists(new LambdaQueryWrapper<MsgSubscribe>()
                .eq(MsgSubscribe::getSubName, msgSubscribe.getSubName()))) {
            throw new RuntimeException("订阅名称重复");
        }
    }

    @Override
    public void insertMsgSubscribe(MsgSubscribe msgSubscribe) {
        msgSubscribeMapper.insert(msgSubscribe);
    }

    @Override
    public void checkUpdate(MsgSubscribe msgSubscribe) {
        MsgSubscribe oldOne = msgSubscribeMapper.selectById(msgSubscribe.getSubId());
        if (!StringUtils.equals(msgSubscribe.getSubName(), oldOne.getSubName())) {
            if (msgSubscribeMapper.exists(new LambdaQueryWrapper<MsgSubscribe>()
                    .eq(MsgSubscribe::getSubName, msgSubscribe.getSubName()))) {
                throw new RuntimeException("订阅名称重复");
            }
        }
    }

    @Override
    public void updateMsgSubscribe(MsgSubscribe msgSubscribe) {
        msgSubscribeMapper.updateById(msgSubscribe);
    }

    @Override
    public void deleteMsgSubscribe(Long[] ids) {
        msgSubscribeMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void subscribe(Long userId, Long id) {
        MsgSubscribe msgSubscribe = msgSubscribeMapper.selectById(id);
        if (ObjectUtils.isEmpty(msgSubscribe)) {
            throw new ServiceException("订阅不存在");
        }
        if (msgSubscribe.getSubStatus().equals(SystemConstants.STATUS_0)) {
            throw new ServiceException("当前不可订阅");
        }
        MsgSubscribeUser subOne = msgSubscribeUserMapper.selectOne(new LambdaQueryWrapper<MsgSubscribeUser>()
                .eq(MsgSubscribeUser::getUserId, userId)
                .eq(MsgSubscribeUser::getSubId, id));
        if (ObjectUtils.isNotEmpty(msgSubscribe)) {
            msgSubscribeMapper.deleteById(subOne);
        } else {
            MsgSubscribeUser subscribeUser = new MsgSubscribeUser();
            subscribeUser.setSubId(id);
            subscribeUser.setUserId(userId);
            msgSubscribeUserMapper.insert(subscribeUser);
        }
    }

    @Override
    public List<Long> queryNonSubscribeUserIds(Long sid) {
        return msgSubscribeMapper.selectNonSubscribeUserIds(sid);
    }

    @Override
    public List<Long> querySubscribeUserIds(Long sid) {
        return msgSubscribeMapper.selectSubscribeUserIds(sid);
    }

    @Override
    public void subscribeUser(Long sid, Long[] userIds) {
        if (userIds.length == 0) {
            return;
        }
        ArrayList<MsgSubscribeUser> list = new ArrayList<>(userIds.length);
        for (Long userId : userIds) {
            MsgSubscribeUser subscribeUser = new MsgSubscribeUser();
            subscribeUser.setUserId(userId);
            subscribeUser.setSubId(sid);
            list.add(subscribeUser);
        }
        msgSubscribeUserMapper.insertBatch(list);
    }

    @Override
    public void unsubscribeUser(Long sid, Long[] userIds) {
        if (userIds.length == 0) {
            return;
        }
        msgSubscribeUserMapper.delete(new LambdaQueryWrapper<MsgSubscribeUser>()
                .eq(MsgSubscribeUser::getSubId, sid)
                .in(MsgSubscribeUser::getUserId, Arrays.asList(userIds)));
    }

    private LambdaQueryWrapper<MsgSubscribe> wrapperBuilder(MsgSubscribe msgSubscribe) {
        LambdaQueryWrapper<MsgSubscribe> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ObjectUtils.isNotEmpty(msgSubscribe.getSubStatus()), MsgSubscribe::getSubStatus, msgSubscribe.getSubStatus())
                .like(ObjectUtils.isNotEmpty(msgSubscribe.getSubName()), MsgSubscribe::getSubName, msgSubscribe.getSubName());
        return wrapper;
    }
}
