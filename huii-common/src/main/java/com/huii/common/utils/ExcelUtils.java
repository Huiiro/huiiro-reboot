package com.huii.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.huii.common.convert.ExcelBigNumberConvert;
import com.huii.common.convert.ExcelMapConvert;
import com.huii.common.enums.ResType;
import com.huii.common.excel.DefaultExcelListener;
import com.huii.common.excel.ExcelListener;
import com.huii.common.excel.ExcelResult;
import com.huii.common.exception.ServiceException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * excel工具类
 *
 * @author huii
 */
public class ExcelUtils {

    /**
     * 同步导入
     */
    public static <T> List<T> importSyncExcel(InputStream is, Class<T> clazz) {
        return EasyExcel.read(is).head(clazz).sheet().doReadSync();
    }

    /**
     * 异步导入
     *
     * @param is       输入流
     * @param clazz    实体类
     * @param listener 监听器
     */
    public static <T> ExcelResult<T> importAsyncExcel(InputStream is, Class<T> clazz, ExcelListener<T> listener) {
        EasyExcel.read(is, listener).head(clazz).sheet().doRead();
        return listener.getExcelResult();
    }

    public static <T> ExcelResult<T> importAsyncExcel(InputStream is, Class<T> clazz) {
        DefaultExcelListener<T> listener = new DefaultExcelListener<>();
        return importAsyncExcel(is, clazz, listener);
    }

    /**
     * 导出数据
     *
     * @param sheetName 表明
     * @param list      数据
     * @param clazz     实体类
     * @param response  响应体
     */
    public static <T> void exportExcel(String sheetName, List<T> list, Class<T> clazz, HttpServletResponse response) {
        try {
            sheetName = (sheetName == null || sheetName.isEmpty()) ? genRandomName() : sheetName;
            setResponse(response, sheetName);
            ServletOutputStream os = response.getOutputStream();
            ExcelWriterSheetBuilder builder = EasyExcel
                    .write(os)
                    .head(clazz)
                    .sheet(sheetName)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .registerConverter(new ExcelBigNumberConvert())
                    .registerConverter(new ExcelMapConvert());
            builder.doWrite(list);
        } catch (Exception e) {
            throw new ServiceException(MessageUtils.message(ResType.COMMON_EXPORT_ERROR.getI18n()));
        }
    }


    private static <T> void exportExcel(Boolean merge, String sheetName, List<T> list, Class<T> clazz, OutputStream os) {
    }

    private static void setResponse(HttpServletResponse response, String sheetName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        String encodedSheetName = URLEncoder.encode(sheetName, StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedSheetName + ".xlsx");
    }

    private static String genRandomName() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }

}
