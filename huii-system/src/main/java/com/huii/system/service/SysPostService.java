package com.huii.system.service;

import com.huii.common.core.domain.SysPost;
import com.huii.common.core.model.Label;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;

import java.util.List;

public interface SysPostService {

    /**
     * 查询岗位分页
     *
     * @param sysPost   sysPost
     * @param pageParam pageParam
     * @return page
     */
    Page selectPostList(SysPost sysPost, PageParam pageParam);

    /**
     * 查询岗位集合
     *
     * @param sysPost sysPost
     * @return list
     */
    List<SysPost> selectPostList(SysPost sysPost);

    /**
     * 查询岗位标签
     *
     * @return list
     */
    List<Label> selectPostsAll();

    /**
     * 查询用户岗位
     *
     * @param userId userId
     * @return list
     */
    List<Long> selectUserPostIds(Long userId);

    /**
     * 查询岗位
     *
     * @param id id
     * @return sysPost
     */
    SysPost selectPostById(Long id);

    /**
     * 检查插入数据
     *
     * @param sysPost sysPost
     */
    void checkInsert(SysPost sysPost);

    /**
     * 添加岗位
     *
     * @param sysPost sysPost
     */
    void insertPost(SysPost sysPost);

    /**
     * 校验更新数据
     *
     * @param sysPost sysPost
     */
    void checkUpdate(SysPost sysPost);

    /**
     * 更新岗位
     *
     * @param sysPost sysPost
     */
    void updatePost(SysPost sysPost);

    /**
     * 删除岗位
     *
     * @param ids ids
     */
    void deletePosts(Long[] ids);
}
