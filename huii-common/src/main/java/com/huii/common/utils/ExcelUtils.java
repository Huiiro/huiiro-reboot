package com.huii.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.huii.common.convert.ExcelBigNumberConvert;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ExcelUtils {

    public static <T> List<T> importExcel(InputStream is, Class<T> clazz) {
        return EasyExcel.read(is).head(clazz).autoCloseStream(false).sheet().doReadSync();
    }

    public static <T> void exportExcel(String sheetName, List<T> list, Class<T> clazz, HttpServletResponse response) {
        try {
            setResponse(response, sheetName);
            ServletOutputStream os = response.getOutputStream();
            exportExcel(sheetName, list, clazz, os);
        } catch (IOException e) {
            throw new ServiceException(MessageUtils.message(ResType.COMMON_EXPORT_ERROR.getI18n()));
        }
    }


    public static <T> void exportExcel(String sheetName, List<T> list, Class<T> clazz, OutputStream os) {
        ExcelWriterSheetBuilder builder = EasyExcel.write(os, clazz)
                .autoCloseStream(false)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerConverter(new ExcelBigNumberConvert())
                .sheet(sheetName);
        builder.doWrite(list);
    }

    private static void setResponse(HttpServletResponse response, String sheetName) {
        response.setContentType("application/octet-stream");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("content-disposition", "attachment;filename=" + sheetName);
    }
}
