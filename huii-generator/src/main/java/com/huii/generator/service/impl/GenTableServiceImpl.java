package com.huii.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.model.PageParam;
import com.huii.common.utils.PageParamUtils;
import com.huii.generator.config.properties.GenProperties;
import com.huii.generator.entity.GenColumn;
import com.huii.generator.entity.GenTable;
import com.huii.generator.enums.FormType;
import com.huii.generator.enums.WrapperType;
import com.huii.generator.mapper.GenColumnMapper;
import com.huii.generator.mapper.GenTableMapper;
import com.huii.generator.service.GenTableService;
import com.huii.generator.utils.CharacterEscapeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
                .eq(GenColumn::getTableId, id));
        GenTable table = genTableMapper.selectById(id);
        table.setColumns(columns);
        return table;
    }

    @Override
    public List<GenColumn> selectColumnsByTableName(String tableName) {
        return genTableMapper.selectColumnByTableName(tableName);
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
            genTable.setGenImportInterface(SystemConstants.STATUS_1);
            genTable.setGenExportInterface(SystemConstants.STATUS_1);

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
        //TODO 提交表格更新 此处要对数据进行校验
        //校验内容包括 主键唯一
        //字段属性是否对应 ... ?? 等等
        genTableMapper.updateById(genTable);
        List<GenColumn> columns = genTable.getColumns();
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
        if(field.contains("name")) {
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
     if(field.contains("status") || field.contains("name")) {
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
        if(field.contains("time") ) {
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
