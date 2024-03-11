package com.huii.generator.controller;

import com.huii.common.annotation.Anonymous;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.generator.entity.GenColumn;
import com.huii.generator.entity.GenTable;
import com.huii.generator.enums.SqlType;
import com.huii.generator.enums.VueType;
import com.huii.generator.exception.GenGeneratorException;
import com.huii.generator.service.GenTableService;
import com.huii.generator.utils.CharacterEscapeUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代码生成控制器
 *
 * @author huii
 */
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
        if (genTable.getClassName().equals(genTable.getVariableName())) {
            throw new GenGeneratorException("变量名称不能与实体类名称相同");
        }
        if (!genTable.getRequestUrl().startsWith("/") || genTable.getRequestUrl().endsWith("/")) {
            throw new GenGeneratorException("请求路径不符合规范，必须为\"/api/example\"的形式");
        }
        if (StringUtils.isNotBlank(genTable.getPackageName())
                && !CharacterEscapeUtils.isValidPackageName(genTable.getPackageName())) {
            throw new GenGeneratorException("包名不符合规范，必须为\"com.huii.example\"的形式");
        }
        if (StringUtils.isNotBlank(genTable.getModuleName())
                && !CharacterEscapeUtils.isValidModuleName(genTable.getModuleName())) {
            throw new GenGeneratorException("模块名称不符合规范，必须为\"module.example\"的形式");
        }
        if (StringUtils.isBlank(genTable.getModuleFunctionDesc())) {
            throw new GenGeneratorException("模块作用描述不能为空");
        }
        if (StringUtils.isNotBlank(genTable.getSqlType()) && !SqlType.support(genTable.getSqlType())) {
            throw new GenGeneratorException("不支持的数据库类型");
        }
        if (StringUtils.isNotBlank(genTable.getFrontendType()) && !VueType.support(genTable.getFrontendType())) {
            throw new GenGeneratorException("不支持的前端版本类型");
        }
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
    public R<Void> sync(@PathVariable Long id) {
        genTableService.sync(id);
        return R.failed("暂不支持该功能");
    }
}
