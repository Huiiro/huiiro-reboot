-- mysql

START TRANSACTION;

SET @parent_id = 0;

INSERT INTO sys_menu (parent_id, menu_type, menu_name, menu_auth, menu_icon, menu_path, menu_component, menu_seq, menu_visible, menu_status, query_param, remark, create_by, create_time, update_by, update_time)
VALUES (<#if parentMenuId?has_content>${parentMenuId}<#else>0</#if>, 1, '${moduleFunctionName}', '${authPrefix}', '', '${requestUrl}', '${requestUrl}/Index', 1, '1', '1', '', '', 'admin', '${createTime}', 'admin', '${createTime}');

SET @parent_id = LAST_INSERT_ID();

--query
INSERT INTO sys_menu (parent_id, menu_type, menu_name, menu_auth, menu_icon, menu_path, menu_component, menu_seq, menu_visible, menu_status, query_param, remark, create_by, create_time, update_by, update_time)
VALUES (@parent_id, 3, '查询${moduleFunctionName}', '${authPrefix}:query', '', '${requestUrl}/query', '', 1, '1', '1', '', '', 'admin', '${createTime}', 'admin', '${createTime}');
<#if genAddInterface == "1">
--add
INSERT INTO sys_menu (parent_id, menu_type, menu_name, menu_auth, menu_icon, menu_path, menu_component, menu_seq, menu_visible, menu_status, query_param, remark, create_by, create_time, update_by, update_time)
VALUES (@parent_id, 3, '新增${moduleFunctionName}', '${authPrefix}:add', '', '${requestUrl}/add', '', 1, '1', '1', '', '', 'admin', '${createTime}', 'admin', '${createTime}');
</#if>
<#if genEditInterface == "1">
--edit
INSERT INTO sys_menu (parent_id, menu_type, menu_name, menu_auth, menu_icon, menu_path, menu_component, menu_seq, menu_visible, menu_status, query_param, remark, create_by, create_time, update_by, update_time)
VALUES (@parent_id, 3, '修改${moduleFunctionName}', '${authPrefix}:edit', '', '${requestUrl}/edit', '', 1, '1', '1', '', '', 'admin', '${createTime}', 'admin', '${createTime}');
</#if>
<#if genDeleteInterface == "1">
--delete
INSERT INTO sys_menu (parent_id, menu_type, menu_name, menu_auth, menu_icon, menu_path, menu_component, menu_seq, menu_visible, menu_status, query_param, remark, create_by, create_time, update_by, update_time)
VALUES (@parent_id, 3, '删除${moduleFunctionName}', '${authPrefix}:delete', '', '${requestUrl}/delete', '', 1, '1', '1', '', '', 'admin', '${createTime}', 'admin', '${createTime}');
</#if>
<#if genImportInterface == "1">
--import
INSERT INTO sys_menu (parent_id, menu_type, menu_name, menu_auth, menu_icon, menu_path, menu_component, menu_seq, menu_visible, menu_status, query_param, remark, create_by, create_time, update_by, update_time)
VALUES (@parent_id, 3, '导入${moduleFunctionName}', '${authPrefix}:import', '', '${requestUrl}/import', '', 1, '1', '1', '', '', 'admin', '${createTime}', 'admin', '${createTime}');
</#if>
<#if genExportInterface == "1">
--export
INSERT INTO sys_menu (parent_id, menu_type, menu_name, menu_auth, menu_icon, menu_path, menu_component, menu_seq, menu_visible, menu_status, query_param, remark, create_by, create_time, update_by, update_time)
VALUES (@parent_id, 3, '导出${moduleFunctionName}', '${authPrefix}:export', '', '${requestUrl}/export', '', 1, '1', '1', '', '', 'admin', '${createTime}', 'admin', '${createTime}');
</#if>

COMMIT;