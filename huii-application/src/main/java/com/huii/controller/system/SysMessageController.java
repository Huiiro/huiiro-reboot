package com.huii.controller.system;

import com.huii.common.annotation.Log;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.system.domain.SysMessage;
import com.huii.system.domain.dto.SysMessageDto;
import com.huii.system.service.SysMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/system/message")
@RequiredArgsConstructor
public class SysMessageController extends BaseController {

    private final SysMessageService sysMessageService;

    /**
     * 获取未读消息
     */
    @GetMapping("/unread")
    public R<Map<String, String>> getUnReadMessageCount() {
        Map<String, String> map = new HashMap<>();
        Long id = safeGetUserId();
        if (id != null) {
            Long count = sysMessageService.getUnReadMessageCount(id);
            map.put("count", count.toString());
        }
        return R.ok(map);
    }

    /**
     * 获取个人消息列表
     */
    @GetMapping("/list/my")
    public R<Page> getMyMessageList(SysMessage sysMessage, PageParam pageParam) {
        sysMessage.setReceiveId(getUserId());
        Page page = sysMessageService.selectMyMessageList(sysMessage, pageParam);
        return R.ok(page);
    }

    /**
     * 获取消息列表
     */
    @GetMapping("/list")
    public R<Page> getList(SysMessage sysMessage, PageParam pageParam) {
        Page page = sysMessageService.selectMessageList(sysMessage, pageParam);
        return R.ok(page);
    }

    /**
     * 获取消息
     */
    @GetMapping(value = "/{id}")
    public R<SysMessage> getMessage(@PathVariable Long id) {
        return R.ok(sysMessageService.selectMessageById(id));
    }

    /**
     * 添加消息
     */
    @PreAuthorize("@ap.hasAuth('system:message:add')")
    @PostMapping("/insert")
    @Log(value = "添加消息", opType = OpType.INSERT)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> insertMessage(@Validated @RequestBody SysMessageDto sysMessage) {
        sysMessage.setSendId(getUserId());
        sysMessageService.checkInsert(sysMessage);
        sysMessageService.insertMessage(sysMessage);
        return saveSuccess();
    }

    /**
     * 更新消息
     */
    @PreAuthorize("@ap.hasAuth('system:message:edit')")
    @PostMapping("/update")
    @Log(value = "更新消息", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateMessage(@Validated @RequestBody SysMessage sysMessage) {
        sysMessageService.checkUpdate(sysMessage);
        sysMessageService.updateMessage(sysMessage);
        return updateSuccess();
    }

    /**
     * 删除消息
     */
    @PreAuthorize("@ap.hasAuth('system:message:delete')")
    @PostMapping("/delete")
    @Log(value = "删除消息", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteMessage(@RequestBody Long[] ids) {
        sysMessageService.deleteMessage(ids);
        return deleteSuccess();
    }
}
