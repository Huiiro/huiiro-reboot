package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysPost;
import com.huii.common.core.model.Label;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.system.domain.SysUserPost;
import com.huii.system.mapper.SysPostMapper;
import com.huii.system.mapper.SysUserPostMapper;
import com.huii.system.service.SysPostService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    private final SysPostMapper sysPostMapper;
    private final SysUserPostMapper sysUserPostMapper;

    @Override
    public Page selectPostList(SysPost sysPost, PageParam pageParam) {
        IPage<SysPost> iPage = new PageParamUtils<SysPost>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(sysPost)));
    }

    @Override
    public List<SysPost> selectPostList(SysPost sysPost) {
        return sysPostMapper.selectList(wrapperBuilder(sysPost));
    }

    @Override
    public List<Label> selectPostsAll() {
        List<SysPost> posts = sysPostMapper.selectList(null);
        return posts.stream()
                .filter(f -> SystemConstants.STATUS_1.equals(f.getPostStatus()))
                .map(m -> new Label(m.getPostId(), m.getPostName())).toList();
    }

    @Override
    public List<Long> selectUserPostIds(Long userId) {
        return sysUserPostMapper.selectList(new LambdaQueryWrapper<SysUserPost>()
                        .eq(SysUserPost::getUserId, userId))
                .stream().map(SysUserPost::getPostId).toList();
    }

    @Override
    public SysPost selectPostById(Long id) {
        return sysPostMapper.selectById(id);
    }

    @Override
    public void checkInsert(SysPost sysPost) {
        if (sysPostMapper.exists(new LambdaQueryWrapper<SysPost>()
                .eq(SysPost::getPostName, sysPost.getPostName()))) {
            ResType resType = ResType.SYS_POST_NAME_REPEAT;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
        if (sysPostMapper.exists(new LambdaQueryWrapper<SysPost>()
                .eq(SysPost::getPostKey, sysPost.getPostKey()))) {
            ResType resType = ResType.SYS_POST_KEY_REPEAT;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
    }

    @Override
    public void insertPost(SysPost sysPost) {
        sysPostMapper.insert(sysPost);
    }

    @Override
    public void checkUpdate(SysPost sysPost) {
        SysPost oldOne = sysPostMapper.selectById(sysPost.getPostId());
        if (!StringUtils.equals(sysPost.getPostName(), oldOne.getPostName())) {
            if (sysPostMapper.exists(new LambdaQueryWrapper<SysPost>()
                    .eq(SysPost::getPostName, sysPost.getPostName()))) {
                ResType resType = ResType.SYS_POST_NAME_REPEAT;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
        }
        if (!StringUtils.equals(sysPost.getPostKey(), oldOne.getPostKey())) {
            if (sysPostMapper.exists(new LambdaQueryWrapper<SysPost>()
                    .eq(SysPost::getPostKey, sysPost.getPostKey()))) {
                ResType resType = ResType.SYS_POST_KEY_REPEAT;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
        }
    }

    @Override
    public void updatePost(SysPost sysPost) {
        sysPostMapper.updateById(sysPost);
    }

    @Override
    public void deletePosts(Long[] ids) {
        for (Long id : ids) {
            boolean exists = sysUserPostMapper.exists(new LambdaQueryWrapper<SysUserPost>()
                    .eq(SysUserPost::getPostId, id));
            if (exists) {
                SysPost sysPost = selectPostById(id);
                ResType resType = ResType.SYS_POST_EXISTS_USER;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n(), sysPost.getPostName()));
            }
        }

        List<Long> posts = Arrays.asList(ids);
        sysPostMapper.deleteBatchIds(posts);
        sysUserPostMapper.delete(new LambdaQueryWrapper<SysUserPost>()
                .in(SysUserPost::getPostId, posts));
    }

    private LambdaQueryWrapper<SysPost> wrapperBuilder(SysPost post) {
        Map<String, Object> params = post.getParams();
        LambdaQueryWrapper<SysPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(post.getPostName()), SysPost::getPostName, post.getPostName())
                .like(StringUtils.isNotBlank(post.getPostKey()), SysPost::getPostKey, post.getPostKey())
                .eq(ObjectUtils.isNotEmpty(post.getPostStatus()), SysPost::getPostStatus, post.getPostStatus())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) && ObjectUtils.isNotEmpty(params.get("endTime")),
                        SysPost::getCreateTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")))
                .orderByAsc(SysPost::getPostSeq)
                .orderByAsc(SysPost::getCreateTime);
        return wrapper;
    }
}
