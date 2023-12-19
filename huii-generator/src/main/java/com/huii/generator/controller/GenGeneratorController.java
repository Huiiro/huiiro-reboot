package com.huii.generator.controller;

import com.huii.common.annotation.Anonymous;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.generator.entity.GenColumn;
import com.huii.generator.entity.GenTable;
import com.huii.generator.service.GenTableService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Anonymous
@Validated
@RestController
@RequestMapping("/gen")
@RequiredArgsConstructor
public class GenGeneratorController extends BaseController {

    private final GenTableService genTableService;

    /**
     * 从DB加载表数据
     */
    @GetMapping("/list/db")
    public R<Page> loadDbData(GenTable genTable, PageParam pageParam) {
        Page page = genTableService.loadDbData(genTable, pageParam);
        return R.ok(page);
    }

    /**
     * 获取 table list
     */
    @GetMapping("/list")
    public R<Page> getList(GenTable genTable, PageParam pageParam) {
        Page page = genTableService.selectTableList(genTable, pageParam);
        return R.ok(page);
    }

    /**
     * 查询具体 table
     */
    @GetMapping("/{id}")
    public R<GenTable> getOne(@PathVariable Long id) {
        GenTable table = genTableService.selectOne(id);
        return R.ok(table);
    }

    /**
     * 查询具体 table
     */
    @GetMapping("/name")
    public R<List<GenColumn>> getOne(@RequestParam String tableName) {
        List<GenColumn> list = genTableService.selectColumnsByTableName(tableName);
        return R.ok(list);
    }

    /**
     * 添加 table
     */
    @PreAuthorize("@ap.hasAuth('tool:gen:add')")
    @PostMapping("/insert")
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> insertTable(@Validated @RequestBody String[] tables) {
        genTableService.insertTable(tables);
        return saveSuccess();
    }

    /**
     * 更新具体 table
     */
    @PreAuthorize("@ap.hasAuth('tool:gen:edit')")
    @PostMapping("/update")
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateTable(@Validated @RequestBody GenTable genTable) {
        genTableService.updateTable(genTable);
        return updateSuccess();
    }

    /**
     * 删除具体 table
     */
    @PreAuthorize("@ap.hasAuth('tool:gen:delete')")
    @PostMapping("/delete")
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteTable(@RequestBody Long[] ids) {
        genTableService.deleteTable(ids);
        return deleteSuccess();
    }

    /**
     * 生成code
     */
    @PreAuthorize("@ap.hasAuth('tool:gen:export')")
    @PostMapping("/code")
    public void genCode(@RequestBody Long[] ids, HttpServletResponse response) {
        List<GenTable> list = genTableService.selectBatchByIds(ids);
        genTableService.genCode(list, response);
    }

    /**
     * 同步结构
     */
    @PreAuthorize("@ap.hasAuth('tool:gen:sync')")
    @GetMapping("/sync/{id}")
    public void sync(@PathVariable Long id) {

    }
}
