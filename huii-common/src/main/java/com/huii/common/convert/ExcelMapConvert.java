package com.huii.common.convert;

import cn.hutool.core.convert.Convert;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.util.Map;

/**
 * map转换
 *
 * @author huii
 */
@SuppressWarnings("rawtypes")
public class ExcelMapConvert implements Converter<Map> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Map.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Map convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return Convert.toMap(String.class, Object.class, cellData);
    }

    @Override
    public WriteCellData<?> convertToExcelData(Map value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String str = Convert.toStr(value);
        return new WriteCellData<>(str);
    }
}
