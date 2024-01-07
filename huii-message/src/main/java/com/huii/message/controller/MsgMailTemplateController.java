package com.huii.message.controller;

import com.huii.common.annotation.Log;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.message.domain.MsgMailTemplate;
import com.huii.message.service.MsgMailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 邮件模板服务层实现
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
@Validated
@RestController
@RequestMapping("/msg/mail")
@RequiredArgsConstructor
public class MsgMailTemplateController extends BaseController {

    private final MsgMailTemplateService msgMailTemplateService;

    /**
     * 查询邮件模板分页
     */
    @GetMapping("/list")
    public R<Page> getList(MsgMailTemplate msgMailTemplate, PageParam pageParam) {
        Page page = msgMailTemplateService.selectMsgMailTemplateList(msgMailTemplate, pageParam);
        return R.ok(page);
    }

    /**
     * 查询邮件模板
     */
    @GetMapping(value = "/{id}")
    public R<MsgMailTemplate> getMsgMailTemplate(@PathVariable Long id) {
        return R.ok(msgMailTemplateService.selectMsgMailTemplateById(id));
    }

    /**
     * 添加邮件模板
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:mail:add')")
    @PostMapping("/insert")
    @Log(value = "添加邮件模板", opType = OpType.INSERT)
    public R<Void> insertMsgMailTemplate(@Validated @RequestBody MsgMailTemplate msgMailTemplate) {
        msgMailTemplateService.insertMsgMailTemplate(msgMailTemplate);
        return saveSuccess();
    }

    /**
     * 更新邮件模板
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:mail:edit')")
    @PostMapping("/update")
    @Log(value = "更新邮件模板", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateMsgMailTemplate(@Validated @RequestBody MsgMailTemplate msgMailTemplate) {
        msgMailTemplateService.updateMsgMailTemplate(msgMailTemplate);
        return updateSuccess();
    }

    /**
     * 删除邮件模板
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:mail:delete')")
    @PostMapping("/delete")
    @Log(value = "删除邮件模板", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteMsgMailTemplate(@RequestBody Long[] ids) {
        msgMailTemplateService.deleteMsgMailTemplate(ids);
        return deleteSuccess();
    }
}