package com.huii.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.huii.common.convert.ExcelBigNumberConvert;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

public class ExcelUtils<T> {

    public static <T> void exportExcel(String sheetName, List<T> list, Class<T> clazz, HttpServletResponse response) {

    }

    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, OutputStream os) {
        ExcelWriterSheetBuilder builder = EasyExcel.write(os, clazz)
                .autoCloseStream(false)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerConverter(new ExcelBigNumberConvert())
                .sheet(sheetName);
        builder.doWrite(list);
    }
}
