package com.huii.system.service;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.system.domain.SysLogLogin;

import java.util.List;

public interface SysLogLoginService {

    /**
     * 查询登录日志分页
     *
     * @param sysLogLogin sysLogLogin
     * @param pageParam   pageParam
     * @return page
     */
    Page selectSysLogLoginList(SysLogLogin sysLogLogin, PageParam pageParam);

    /**
     * 查询登录日志集合
     *
     * @param sysLogLogin sysLogLogin
     * @return list
     */
    List<SysLogLogin> selectSysLogLoginList(SysLogLogin sysLogLogin);

    /**
     * 删除登录日志
     *
     * @param ids ids
     */
    void removeBatchByIds(Long[] ids);

    /**
     * 删除全部登录日志
     */
    void removeAll();
}
