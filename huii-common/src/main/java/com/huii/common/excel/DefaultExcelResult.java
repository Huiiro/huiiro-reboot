package com.huii.common.excel;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认excel导入结果
 *
 * @param <T> clazz
 * @author huii
 */
@Setter
@AllArgsConstructor
public class DefaultExcelResult<T> implements ExcelResult<T> {

    private List<T> list;
    private List<String> errorList;

    public DefaultExcelResult() {
        this.list = new ArrayList<>();
        this.errorList = new ArrayList<>();
    }

    public DefaultExcelResult(List<T> list) {
        this.list = list;
    }

    public DefaultExcelResult(ExcelResult<T> excelResult) {
        this.list = excelResult.getList();
        this.errorList = excelResult.getErrorList();
    }

    @Override
    public List<T> getList() {
        return list;
    }

    @Override
    public List<String> getErrorList() {
        return errorList;
    }

    @Override
    public String getAnalysis() {
        int successCount = list.size();
        int errorCount = errorList.size();
        if (successCount == 0) {
            return "读取失败，未解析到数据，请检查导入文件";
        } else {
            if (errorCount == 0) {
                return StrUtil.format("共{}条数据读取成功，共{}条数据读取失败", successCount, errorCount);
            } else {
                return StrUtil.format("共{}条数据读取成功，共{}条数据读取失败，失败数据包括：{}", successCount, errorCount, errorList);
            }
        }
    }
}
