package com.huii.generator.enums;

import com.huii.common.constants.SystemConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 模板类型
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum TemplateType {

    CONTROLLER("backend/controller.ftl", "controller/", "Controller.java", "1", "1"),
    DOMAIN("backend/domain.ftl", "domain/", ".java", "1", "1"),
    MAPPER("backend/mapper.ftl", "mapper/", "Mapper.java", "1", "1"),
    SERVICE("backend/service.ftl", "service/", "Service.java", "1", "1"),
    SERVICE_IMPL("backend/serviceImpl.ftl", "service/impl/", "ServiceImpl.java", "1", "1"),
    XML("backend/xml.ftl", "mapper/", "Mapper.xml", "1", "1"),
    EXPORT_VO("backend/exportVo.ftl", "domain/vo/", "ExcelExportVo.java", "1", "0"),
    IMPORT_VO("backend/importVo.ftl", "domain/vo/", "ExcelImportVo.java", "1", "0"),

    V2_API("frontend/vue2/api.ftl", "api/", "index.js", "2", "0"),
    V2_DICTIONARY("frontend/vue2/dictionary.ftl", "views/", "dictionary.js", "2", "0"),
    V2_INDEX("frontend/vue2/index.ftl", "views/", "Index.vue", "2", "0"),
    V2_INDEX_TREE("frontend/vue2/index-tree.ftl", "views/", "Index.vue", "2", "0"),

    V3_API("frontend/vue3/api.ftl", "api/", "index.ts", "2", "0"),
    V3_DICTIONARY("frontend/vue3/dictionary.ftl", "views/", "dictionary.ts", "2", "0"),
    V3_INDEX("frontend/vue3/index.ftl", "views/", "Index.vue", "2", "0"),
    V3_INDEX_TREE("frontend/vue3/index-tree.ftl", "views/", "Index.vue", "2", "0"),

    MYSQL("sql/mysql.ftl", "sql/", ".sql", "1", "0"),
    ORACLE("sql/oracle.ftl", "sql/", ".sql", "1", "0"),
    POSTGRESQL("sql/postgresql.ftl", "sql/", ".sql", "1", "0"),
    SQLSERVER("sql/sqlserver.ftl", "sql/", ".sql", "1", "0");

    /**
     * 模板文件
     */
    private final String template;

    /**
     * 包路径前缀
     */
    private final String prefix;

    /**
     * 文件名后缀
     */
    private final String suffix;

    /**
     * 支持类型 
     * 1-- 后端文件
     * 2--前端文件
     * 0--不做处理
     */
    private final String supportType;

    private final String isBasic;

    public static TemplateType getTemplateByTemplateName(String templateName) {
        if (StringUtils.isEmpty(templateName)) {
            return null;
        }
        for (TemplateType type : values()) {
            if (type.getTemplate().equals(templateName)) {
                return type;
            }
        }
        return null;
    }

    public static List<TemplateType> getTemplateList(String frontendType, String sqlType, String isTree,
                                                     String doExport, String doImport) {
        List<TemplateType> basics = Arrays.stream(TemplateType.values())
                .filter(i -> i.getIsBasic().equals(SystemConstants.STATUS_1)).toList();
        List<TemplateType> list = new ArrayList<>(basics);
        if (frontendType.equals("1")) {
            //vue2
            list.add(TemplateType.V2_API);
            list.add(TemplateType.V2_DICTIONARY);
            if (SystemConstants.STATUS_1.equals(isTree)) {
                list.add(TemplateType.V2_INDEX);
            } else {
                list.add(TemplateType.V2_INDEX_TREE);
            }
        } else if (frontendType.equals("2")) {
            //vue3
            list.add(TemplateType.V3_API);
            list.add(TemplateType.V3_DICTIONARY);
            if (SystemConstants.STATUS_1.equals(isTree)) {
                list.add(TemplateType.V3_INDEX);
            } else {
                list.add(TemplateType.V3_INDEX_TREE);
            }
        }
        //sql
        switch (sqlType) {
            case "1" -> list.add(TemplateType.MYSQL);
            case "2" -> list.add(TemplateType.POSTGRESQL);
            case "3" -> list.add(TemplateType.ORACLE);
            case "4" -> list.add(TemplateType.SQLSERVER);
        }
        if (SystemConstants.STATUS_1.equals(doExport)) {
            list.add(TemplateType.EXPORT_VO);
        }
        if (SystemConstants.STATUS_1.equals(doImport)) {
            list.add(TemplateType.IMPORT_VO);
        }
        return list;
    }
}
