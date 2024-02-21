package com.huii.message.controller;

import com.huii.common.annotation.Log;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.message.domain.MsgSendTemplate;
import com.huii.message.service.MsgSendTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 推送消息模板控制层实现
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
@Validated
@RestController
@RequestMapping("/msg/send")
@RequiredArgsConstructor
public class MsgSendTemplateController extends BaseController {

    private final MsgSendTemplateService msgSendTemplateService;

    /**
     * 查询推送消息模板分页
     */
    @GetMapping("/list")
    public R<Page> getList(MsgSendTemplate msgSendTemplate, PageParam pageParam) {
        Page page = msgSendTemplateService.selectMsgSendTemplateList(msgSendTemplate, pageParam);
        return R.ok(page);
    }

    /**
     * 查询推送消息模板
     */
    @GetMapping(value = "/{id}")
    public R<MsgSendTemplate> getMsgSendTemplate(@PathVariable Long id) {
        return R.ok(msgSendTemplateService.selectMsgSendTemplateById(id));
    }

    /**
     * 添加推送消息模板
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:send:add')")
    @PostMapping("/insert")
    @Log(value = "添加推送消息模板", opType = OpType.INSERT)
    public R<Void> insertMsgSendTemplate(@Validated @RequestBody MsgSendTemplate msgSendTemplate) {
        msgSendTemplateService.checkInsert(msgSendTemplate);
        msgSendTemplateService.insertMsgSendTemplate(msgSendTemplate);
        return saveSuccess();
    }

    /**
     * 更新推送消息模板
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:send:edit')")
    @PostMapping("/update")
    @Log(value = "更新推送消息模板", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateMsgSendTemplate(@Validated @RequestBody MsgSendTemplate msgSendTemplate) {
        msgSendTemplateService.checkUpdate(msgSendTemplate);
        msgSendTemplateService.updateMsgSendTemplate(msgSendTemplate);
        return updateSuccess();
    }

    /**
     * 删除推送消息模板
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:send:delete')")
    @PostMapping("/delete")
    @Log(value = "删除推送消息模板", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteMsgSendTemplate(@RequestBody Long[] ids) {
        msgSendTemplateService.deleteMsgSendTemplate(ids);
        return deleteSuccess();
    }

    /**
     * 推送消息
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:send:edit')")
    @PostMapping("/send")
    @Log(value = "推送消息", opType = OpType.UPDATE)
    public R<Void> sendMsg(@Validated @RequestBody MsgSendTemplate msgSendTemplate) {
        msgSendTemplateService.runMessageSend(msgSendTemplate);
        return R.ok("消息推送成功");
    }
}