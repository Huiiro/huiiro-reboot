package com.huii.controller.system;

import com.huii.common.annotation.Log;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.system.domain.SysNotice;
import com.huii.system.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/system/notice")
@RequiredArgsConstructor
public class SysNoticeController extends BaseController {

    private final SysNoticeService sysNoticeService;

    /**
     * 获取通知列表
     */
    @GetMapping("/list")
    public R<Page> getList(SysNotice sysNotice, PageParam pageParam) {
        Page page = sysNoticeService.selectNoticeList(sysNotice, pageParam);
        return R.ok(page);
    }

    /**
     * 获取通知
     */
    @GetMapping(value = "/{id}")
    public R<SysNotice> getNotice(@PathVariable Long id) {
        return R.ok(sysNoticeService.selectNoticeById(id));
    }

    /**
     * 添加通知
     */
    @PreAuthorize("@ap.hasAuth('system:notice:add')")
    @PostMapping("/insert")
    @Log(value = "添加通知", opType = OpType.INSERT)
    public R<Void> insertNotice(@Validated @RequestBody SysNotice sysNotice) {
        sysNoticeService.checkInsert(sysNotice);
        sysNoticeService.insertNotice(sysNotice);
        return saveSuccess();
    }

    /**
     * 更新通知
     */
    @PreAuthorize("@ap.hasAuth('system:notice:edit')")
    @PostMapping("/update")
    @Log(value = "更新通知", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateNotice(@Validated @RequestBody SysNotice sysNotice) {
        sysNoticeService.checkUpdate(sysNotice);
        sysNoticeService.updateNotice(sysNotice);
        return updateSuccess();
    }

    /**
     * 删除通知
     */
    @PreAuthorize("@ap.hasAuth('system:notice:delete')")
    @PostMapping("/delete")
    @Log(value = "删除通知", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteNotice(@RequestBody Long[] ids) {
        sysNoticeService.deleteNotice(ids);
        return deleteSuccess();
    }
}
