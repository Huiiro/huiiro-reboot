package com.huii.system.service;

import com.huii.common.core.domain.SysPost;
import com.huii.common.core.model.Label;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;

import java.util.List;

public interface SysPostService {
    Page selectPostList(SysPost sysPost, PageParam pageParam);

    List<SysPost> selectPostList(SysPost sysPost);

    List<Label> selectPostsAll();

    List<Long> selectUserPostIds(Long userId);

    SysPost selectPostById(Long id);

    void checkInsert(SysPost sysPost);

    void insertPost(SysPost sysPost);

    void checkUpdate(SysPost sysPost);

    void updatePost(SysPost sysPost);

    void deletePosts(Long[] ids);
}
