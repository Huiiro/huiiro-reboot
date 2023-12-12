package com.huii.system.service;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.system.domain.SysNotice;

public interface SysNoticeService {
    Page selectNoticeList(SysNotice sysNotice, PageParam pageParam);

    SysNotice selectNoticeById(Long id);

    void checkInsert(SysNotice sysNotice);

    void insertNotice(SysNotice sysNotice);

    void checkUpdate(SysNotice sysNotice);

    void updateNotice(SysNotice sysNotice);

    void deleteNotice(Long[] ids);
}
