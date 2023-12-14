package com.huii.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.core.model.PageParam;
import com.huii.common.utils.PageParamUtils;
import com.huii.generator.entity.GenColumn;
import com.huii.generator.entity.GenTable;
import com.huii.generator.mapper.GenColumnMapper;
import com.huii.generator.mapper.GenTableMapper;
import com.huii.generator.service.GenTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements GenTableService {

    private final GenTableMapper genTableMapper;
    private final GenColumnMapper genColumnMapper;

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
    public void updateTable(GenTable genTable) {
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
}
