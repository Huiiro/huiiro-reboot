package com.huii.system.service;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.system.domain.SysNotice;

public interface SysNoticeService {

    /**
     * 查询系统消息分页
     *
     * @param sysNotice sysNotice
     * @param pageParam pageParam
     * @return page
     */
    Page selectNoticeList(SysNotice sysNotice, PageParam pageParam);

    /**
     * 查询系统消息
     *
     * @param id id
     * @return sysNotice
     */
    SysNotice selectNoticeById(Long id);

    /**
     * 校验插入数据
     *
     * @param sysNotice sysNotice
     */
    void checkInsert(SysNotice sysNotice);

    /**
     * 添加系统消息
     *
     * @param sysNotice sysNotice
     */
    void insertNotice(SysNotice sysNotice);

    /**
     * 校验修改数据
     *
     * @param sysNotice sysNotice
     */
    void checkUpdate(SysNotice sysNotice);

    /**
     * 修改系统消息
     *
     * @param sysNotice sysNotice
     */
    void updateNotice(SysNotice sysNotice);

    /**
     * 删除系统消息
     *
     * @param ids ids
     */
    void deleteNotice(Long[] ids);
}
