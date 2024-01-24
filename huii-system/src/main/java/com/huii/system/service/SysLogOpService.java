package com.huii.system.service;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.system.domain.SysLogOp;

import java.util.List;

public interface SysLogOpService {

    /**
     * 查询系统日志分页
     *
     * @param sysLogOp  sysLogOp
     * @param pageParam pageParam
     * @return page
     */
    Page selectSysLogOpList(SysLogOp sysLogOp, PageParam pageParam);

    /**
     * 查询系统日志集合
     *
     * @param sysLogOp sysLogOp
     * @return list
     */
    List<SysLogOp> selectSysLogOpList(SysLogOp sysLogOp);

    /**
     * 标记日志
     *
     * @param sysLogOp sysLogOp
     */
    void updateLogOpFlagStatus(SysLogOp sysLogOp);

    /**
     * 删除系统日志
     *
     * @param ids ids
     */
    void removeBatchByIds(Long[] ids);

    /**
     * 删除全部系统日志
     */
    void removeAll();

}
