package com.huii.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.model.PageParam;
import com.huii.common.enums.ResType;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.PageParamUtils;
import com.huii.generator.config.TemplateConfig;
import com.huii.generator.config.properties.GenProperties;
import com.huii.generator.entity.GenColumn;
import com.huii.generator.entity.GenTable;
import com.huii.generator.enums.FormType;
import com.huii.generator.enums.TemplateType;
import com.huii.generator.enums.WrapperType;
import com.huii.generator.exception.GenGeneratorException;
import com.huii.generator.mapper.GenColumnMapper;
import com.huii.generator.mapper.GenTableMapper;
import com.huii.generator.service.GenTableService;
import com.huii.generator.utils.CharacterEscapeUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements GenTableService {

    private final GenTableMapper genTableMapper;
    private final GenColumnMapper genColumnMapper;
    private final GenProperties genProperties;

    @Override
    public com.huii.common.core.model.Page loadDbData(GenTable genTable, PageParam pageParam) {
        Page<GenTable> page = new PageParamUtils<GenTable>().getPage(pageParam);
        Page<GenTable> result = genTableMapper.loadDbData(page, genTable);
        return new com.huii.common.core.model.Page(result);
    }

    @Override
    public com.huii.common.core.model.Page selectTableList(GenTable genTable, PageParam pageParam) {
        IPage<GenTable> iPage = new PageParamUtils<GenTable>().getPageInfo(pageParam);
        return new com.huii.common.core.model.Page(this.page(iPage, wrapperBuilder(genTable)));
    }

    @Override
    public GenTable selectOne(Long id) {
        List<GenColumn> columns = genColumnMapper.selectList(new LambdaQueryWrapper<GenColumn>()
                .eq(GenColumn::getTableId, id)
                .orderByAsc(GenColumn::getColumnId));
        GenTable table = genTableMapper.selectById(id);
        table.setColumns(columns);
        return table;
    }

    @Override
    public List<GenColumn> selectColumnsByTableName(String tableName) {
        return genTableMapper.selectColumnByTableName(tableName);
    }

    @Override
    public List<GenTable> selectBatchByIds(Long[] ids) {
        List<GenTable> tables = new ArrayList<>(ids.length);
        for (Long id : ids) {
            GenTable table = genTableMapper.selectTableAndColumns(id);
            //将树表参数从下划线转为驼峰
            table.setTreeLabelName(CharacterEscapeUtils.underscoreToCamel(table.getTreeLabelName()));
            table.setTreeId(CharacterEscapeUtils.underscoreToCamel(table.getTreeId()));
            tables.add(table);
        }
        return tables;
    }

    @Override
    public void insertTable(String[] tables) {
        for (String tableName : tables) {
            GenTable genTable = new GenTable();
            genTable.setTableName(tableName);
            //将数据库命名转为java类名 帕斯卡
            genTable.setClassName(CharacterEscapeUtils.underscoreToPascal(genTable.getTableName()));
            //将数据库命名转为变量名 驼峰
            genTable.setVariableName(CharacterEscapeUtils.underscoreToCamel(genTable.getTableName()));
            //table模板 1为单表CRUD
            genTable.setTableTemplate(genProperties.getDefaultTemplate());
            //设置前端类型 默认vue3
            genTable.setFrontendType(genProperties.getDefaultFrontendType());
            //设置数据库类型 默认postgresql
            genTable.setSqlType(genProperties.getDefaultSqlType());
            //设置作者名称
            genTable.setAuthorName(genProperties.getAuthorName());
            //设置包名
            genTable.setPackageName(genProperties.getPackageName());
            //设置默认生成接口
            genTable.setGenAddInterface(SystemConstants.STATUS_1);
            genTable.setGenEditInterface(SystemConstants.STATUS_1);
            genTable.setGenDeleteInterface(SystemConstants.STATUS_1);
            genTable.setGenImportInterface(SystemConstants.STATUS_0);
            genTable.setGenExportInterface(SystemConstants.STATUS_0);

            genTableMapper.insert(genTable);
            List<GenColumn> columns = genTableMapper.selectColumnByTableName(tableName);
            columns.forEach(i -> {
                i.setTableId(genTable.getTableId());
                //获取字段名 驼峰
                String javaField = CharacterEscapeUtils.underscoreToCamel(i.getColumnName());
                i.setJavaField(javaField);
                //推断java类型 可以自行实现修改
                i.setJavaType(getJavaType(javaField));
                //设置后端默认查询方式
                i.setQueryType(checkQueryType(javaField));
                //设置前端是否查询字段
                i.setIsQueryField(checkIsQueryField(javaField));
                //设置前端表单展示类型
                i.setFormType(checkFormType(javaField));
                //设置默认值
                i.setIsPrimaryKey(SystemConstants.STATUS_0);
                i.setIsIncrement(SystemConstants.STATUS_0);
                i.setIsRequired(SystemConstants.STATUS_0);
                i.setCheckUnique(SystemConstants.STATUS_0);

            });
            genColumnMapper.insertBatch(columns);
        }
    }

    @Override
    public void updateTable(GenTable genTable) {
        int pk = 0;
        List<GenColumn> columns = genTable.getColumns();
        for (GenColumn column : columns) {
            if (column.getIsPrimaryKey().equals(SystemConstants.STATUS_1)) {
                pk++;
            }
            if (pk > 1) {
                ResType resType = ResType.GEN_MULTI_PRIMARY_KEY;
                throw new GenGeneratorException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
        }
        genTableMapper.updateById(genTable);
        for (GenColumn column : columns) {
            genColumnMapper.updateById(column);
        }
    }

    @Override
    public void deleteTable(Long[] ids) {
        List<Long> list = Arrays.asList(ids);
        genTableMapper.deleteBatchIds(list);
        genColumnMapper.delete(new LambdaQueryWrapper<GenColumn>()
                .in(GenColumn::getTableId, list));
    }

    @Override
    public void genCode(List<GenTable> list, HttpServletResponse response) {
        try {
            byte[] bytes = generateCode(list);
            setResponse(response, bytes.length, genProperties.getGenPackageName() + ".zip");
            IOUtils.write(bytes, response.getOutputStream());
        } catch (IOException | TemplateException e) {
            throw new GenGeneratorException(e.getMessage());
        }
    }

    @Override
    public void sync(Long id) {
        //load table

        //load from db

        //if empty return

        //try sync db
    }

    private byte[] generateCode(List<GenTable> list) throws IOException, TemplateException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (GenTable table : list) {
            deserializeTableTemplate(table, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    private void deserializeTableTemplate(GenTable table, ZipOutputStream zip) throws IOException, TemplateException {
        List<TemplateType> list = TemplateType.getTemplateList(table.getFrontendType(), table.getSqlType(),
                ObjectUtils.isEmpty(table.getTreeLabelName()) ? SystemConstants.STATUS_1 : SystemConstants.STATUS_0,
                table.getGenExportInterface(), table.getGenImportInterface());
        for (TemplateType templateType : list) {
            Template template = TemplateConfig.generator().getTemplate(templateType.getTemplate());
            StringWriter writer = new StringWriter();
            template.process(table, writer);
            String name;
            if ("1".equals(templateType.getSupportType())) {
                name = templateType.getPrefix() + table.getClassName() + templateType.getSuffix();
            } else if ("2".equals(templateType.getSupportType())) {
                name = templateType.getPrefix() + table.getModuleName() + "/" + templateType.getSuffix();
            } else {
                name = templateType.getPrefix() + templateType.getSuffix();
            }
            zip.putNextEntry(new ZipEntry(name));
            IOUtils.write(writer.toString(), zip, "UTF-8");
            IOUtils.closeQuietly(writer);
            zip.flush();
            zip.closeEntry();
        }
    }

    private void setResponse(HttpServletResponse response, long length, String fileName) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Length", "" + length);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/octet-stream; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private LambdaQueryWrapper<GenTable> wrapperBuilder(GenTable table) {
        return new LambdaQueryWrapper<>();
    }

    /**
     * 推断java类型
     */
    private String getJavaType(String javaField) {
        String field = javaField.toLowerCase();
        if (field.contains("time")) {
            return "LocalDateTime";
        } else if (field.contains("id")) {
            return "Long";
        } else {
            return "String";
        }
    }

    /**
     * 推断后端查询类型，可自行添加实现
     */
    private String checkQueryType(String javaField) {
        String field = javaField.toLowerCase();
        if (field.contains("name")) {
            //如果包含名称则模糊匹配
            return WrapperType.LIKE.getType();
        }
        return WrapperType.EQ.getType();
    }

    /**
     * 推断前端是否查询该字段，可自行添加实现
     */
    private String checkIsQueryField(String javaField) {
        String field = javaField.toLowerCase();
        if (field.contains("status") || field.contains("name")) {
            //如果包含状态或名称将自动生成查询状态
            return SystemConstants.STATUS_1;
        }
        return SystemConstants.STATUS_0;
    }

    /**
     * 推断前端表单类型
     */
    private String checkFormType(String javaField) {
        String field = javaField.toLowerCase();
        if (field.contains("time")) {
            //如果包含时间则启用日期控件
            return FormType.DATETIME.getType();
        } else if (field.contains("status")) {
            //如果包含状态则启用选项控件
            return FormType.CHECKBOX.getType();
        } else {
            return FormType.INPUT.getType();
        }
    }
}
