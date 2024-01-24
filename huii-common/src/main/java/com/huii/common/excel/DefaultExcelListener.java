package com.huii.common.excel;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 默认excel导入结果监听器
 *
 * @param <T> clazz
 * @author huii
 */
@Slf4j
public class DefaultExcelListener<T> extends AnalysisEventListener<T> implements ExcelListener<T> {

    private ExcelResult<T> excelResult;

    private Map<Integer, String> headMap;

    public DefaultExcelListener() {
        this.excelResult = new DefaultExcelResult<>();
    }

    @Override
    public ExcelResult<T> getExcelResult() {
        return excelResult;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        if (exception instanceof ExcelDataConvertException excelDataConvertException) {
            Integer rowIndex = excelDataConvertException.getRowIndex();
            Integer columnIndex = excelDataConvertException.getColumnIndex();
            String errMsg = StrUtil.format("第{}行-第{}列-表头{}: 解析异常<br/>",
                    rowIndex + 1, columnIndex + 1, headMap.get(columnIndex));
            if (log.isDebugEnabled()) {
                log.error(errMsg);
            }
            excelResult.getErrorList().add(errMsg);
            throw new ExcelAnalysisException(errMsg);
        }
        super.onException(exception, context);
    }

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        excelResult.getList().add(t);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
        log.debug("解析表头完成！{}", headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.debug("所有数据解析完成！");
    }
}
