package com.huii.system.service;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.system.domain.SysLogOp;

import java.util.List;

public interface SysLogOpService {
    Page selectSysLogOpList(SysLogOp sysLogOp, PageParam pageParam);

    List<SysLogOp> selectSysLogOpList(SysLogOp sysLogOp);

    void updateLogOpFlagStatus(SysLogOp sysLogOp);

    void removeBatchByIds(Long[] ids);

    void removeAll();

}
