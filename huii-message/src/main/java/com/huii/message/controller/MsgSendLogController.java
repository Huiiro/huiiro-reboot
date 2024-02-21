package com.huii.message.controller;

import com.huii.common.annotation.Log;
import com.huii.common.annotation.RepeatSubmit;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.common.utils.BeanCopyUtils;
import com.huii.common.utils.ExcelUtils;
import com.huii.message.domain.MsgSendLog;
import com.huii.message.domain.vo.MsgSendLogExportVo;
import com.huii.message.service.MsgSendLogService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息推送日志控制层实现
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
@Validated
@RestController
@RequestMapping("/msg/send/log")
@RequiredArgsConstructor
public class MsgSendLogController extends BaseController {

    private final MsgSendLogService msgSendLogService;

    /**
     * 查询消息推送日志分页
     */
    @GetMapping("/list")
    public R<Page> getList(MsgSendLog msgSendLog, PageParam pageParam) {
        Page page = msgSendLogService.selectMsgSendLogList(msgSendLog, pageParam);
        return R.ok(page);
    }

    @PreAuthorize("@ap.hasAuth('msg:send:log:export')")
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/export")
    @Log(value = "导出任务日志", opType = OpType.EXPORT)
    public void exportJobLog(MsgSendLog msgSendLog, HttpServletResponse response) {
        List<MsgSendLog> list = msgSendLogService.selectMsgSendLogList(msgSendLog);
        List<MsgSendLogExportVo> vos = BeanCopyUtils.copyList(list, MsgSendLogExportVo.class);
        ExcelUtils.exportExcel(null, vos, MsgSendLogExportVo.class, response);
    }

    /**
     * 删除消息推送日志
     */
    @PreAuthorize("@ap.hasAuth('msg:send:log:delete')")
    @PostMapping("/delete")
    @Log(value = "删除消息推送日志", opType = OpType.DELETE)
    public R<Void> deleteMsgSendLog(@RequestBody Long[] ids) {
        msgSendLogService.deleteMsgSendLog(ids);
        return deleteSuccess();
    }

    /**
     * 删除全部消息推送日志
     */
    @PreAuthorize("@ap.hasAuth('msg:send:log:delete')")
    @PostMapping("/delete/all")
    @Log(value = "删除消息推送日志", opType = OpType.DELETE)
    public R<Void> deleteMsgSendLogAll() {
        msgSendLogService.deleteMsgSendLogAll();
        return deleteSuccess();
    }
}
