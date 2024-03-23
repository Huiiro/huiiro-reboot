/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.24.77_pgsql
 Source Server Type    : PostgreSQL
 Source Server Version : 140001
 Source Host           : 192.168.24.77:5432
 Source Catalog        : huii-pro
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 140001
 File Encoding         : 65001

 Date: 23/03/2024 17:09:13
*/

-- ----------------------------
-- Sequence structure for gen_column_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."gen_column_id_seq";
CREATE SEQUENCE "public"."gen_column_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for gen_table_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."gen_table_id_seq";
CREATE SEQUENCE "public"."gen_table_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for msg_mail_template_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."msg_mail_template_id_seq";
CREATE SEQUENCE "public"."msg_mail_template_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for msg_send_log_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."msg_send_log_id_seq";
CREATE SEQUENCE "public"."msg_send_log_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for msg_send_template_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."msg_send_template_id_seq";
CREATE SEQUENCE "public"."msg_send_template_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for msg_subscribe_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."msg_subscribe_id_seq";
CREATE SEQUENCE "public"."msg_subscribe_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_config_id_seq";
CREATE SEQUENCE "public"."sys_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_dept_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dept_id_seq";
CREATE SEQUENCE "public"."sys_dept_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_dic_data_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dic_data_id_seq";
CREATE SEQUENCE "public"."sys_dic_data_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_dic_type_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dic_type_id_seq";
CREATE SEQUENCE "public"."sys_dic_type_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_file_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_file_id_seq";
CREATE SEQUENCE "public"."sys_file_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_job_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_job_id_seq";
CREATE SEQUENCE "public"."sys_job_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_job_log_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_job_log_id_seq";
CREATE SEQUENCE "public"."sys_job_log_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_log_login_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_log_login_id_seq";
CREATE SEQUENCE "public"."sys_log_login_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_log_op_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_log_op_id_seq";
CREATE SEQUENCE "public"."sys_log_op_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_menu_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_menu_id_seq";
CREATE SEQUENCE "public"."sys_menu_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_message_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_message_id_seq";
CREATE SEQUENCE "public"."sys_message_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_notice_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_notice_id_seq";
CREATE SEQUENCE "public"."sys_notice_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_post_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_post_id_seq";
CREATE SEQUENCE "public"."sys_post_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_role_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_role_id_seq";
CREATE SEQUENCE "public"."sys_role_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_user_id_seq";
CREATE SEQUENCE "public"."sys_user_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Table structure for gen_column
-- ----------------------------
DROP TABLE IF EXISTS "public"."gen_column";
CREATE TABLE "public"."gen_column" (
  "column_id" int8 NOT NULL DEFAULT nextval('gen_column_id_seq'::regclass),
  "table_id" int8,
  "column_name" varchar(255) COLLATE "pg_catalog"."default",
  "column_comment" varchar(255) COLLATE "pg_catalog"."default",
  "sql_type" varchar(255) COLLATE "pg_catalog"."default",
  "sql_field" varchar(255) COLLATE "pg_catalog"."default",
  "sql_char_length" varchar(255) COLLATE "pg_catalog"."default",
  "sql_order" varchar(255) COLLATE "pg_catalog"."default",
  "java_type" varchar(255) COLLATE "pg_catalog"."default",
  "java_field" varchar(255) COLLATE "pg_catalog"."default",
  "is_primary_key" char(1) COLLATE "pg_catalog"."default",
  "is_increment" char(1) COLLATE "pg_catalog"."default",
  "is_required" char(1) COLLATE "pg_catalog"."default",
  "check_size" varchar(255) COLLATE "pg_catalog"."default",
  "check_unique" char(1) COLLATE "pg_catalog"."default",
  "query_type" varchar(255) COLLATE "pg_catalog"."default",
  "is_query_field" char(1) COLLATE "pg_catalog"."default",
  "form_type" varchar(255) COLLATE "pg_catalog"."default",
  "dic_type" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of gen_column
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS "public"."gen_table";
CREATE TABLE "public"."gen_table" (
  "table_id" int8 NOT NULL DEFAULT nextval('gen_table_id_seq'::regclass),
  "table_name" varchar(255) COLLATE "pg_catalog"."default",
  "table_comment" varchar(255) COLLATE "pg_catalog"."default",
  "table_template" varchar(255) COLLATE "pg_catalog"."default",
  "class_name" varchar(255) COLLATE "pg_catalog"."default",
  "variable_name" varchar(255) COLLATE "pg_catalog"."default",
  "frontend_type" varchar(255) COLLATE "pg_catalog"."default",
  "sql_type" varchar(255) COLLATE "pg_catalog"."default",
  "author_name" varchar(255) COLLATE "pg_catalog"."default",
  "package_name" varchar(255) COLLATE "pg_catalog"."default",
  "module_name" varchar(255) COLLATE "pg_catalog"."default",
  "module_function_desc" varchar(255) COLLATE "pg_catalog"."default",
  "module_function_name" varchar(255) COLLATE "pg_catalog"."default",
  "auth_prefix" varchar(255) COLLATE "pg_catalog"."default",
  "request_url" varchar(255) COLLATE "pg_catalog"."default",
  "gen_add_interface" char(1) COLLATE "pg_catalog"."default",
  "gen_edit_interface" char(1) COLLATE "pg_catalog"."default",
  "gen_delete_interface" char(1) COLLATE "pg_catalog"."default",
  "gen_import_interface" char(1) COLLATE "pg_catalog"."default",
  "gen_export_interface" char(1) COLLATE "pg_catalog"."default",
  "sub_table_name" varchar(255) COLLATE "pg_catalog"."default",
  "sub_table_foreign_key" varchar(255) COLLATE "pg_catalog"."default",
  "tree_id" varchar(255) COLLATE "pg_catalog"."default",
  "tree_label_name" varchar(255) COLLATE "pg_catalog"."default",
  "parent_menu_id" int8,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for msg_mail_template
-- ----------------------------
DROP TABLE IF EXISTS "public"."msg_mail_template";
CREATE TABLE "public"."msg_mail_template" (
  "mail_temp_id" int8 NOT NULL DEFAULT nextval('msg_mail_template_id_seq'::regclass),
  "mail_type" varchar(255) COLLATE "pg_catalog"."default",
  "mail_subject" varchar(255) COLLATE "pg_catalog"."default",
  "mail_content" text COLLATE "pg_catalog"."default",
  "mail_attach_file" varchar(255) COLLATE "pg_catalog"."default",
  "temp_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of msg_mail_template
-- ----------------------------

-- ----------------------------
-- Table structure for msg_send_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."msg_send_log";
CREATE TABLE "public"."msg_send_log" (
  "log_id" int8 NOT NULL DEFAULT nextval('msg_send_log_id_seq'::regclass),
  "temp_name" varchar(255) COLLATE "pg_catalog"."default",
  "send_type" varchar(255) COLLATE "pg_catalog"."default",
  "send_target" varchar(1000) COLLATE "pg_catalog"."default",
  "send_time" timestamp(0),
  "send_status" char(1) COLLATE "pg_catalog"."default",
  "err_info" varchar(1000) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of msg_send_log
-- ----------------------------

-- ----------------------------
-- Table structure for msg_send_template
-- ----------------------------
DROP TABLE IF EXISTS "public"."msg_send_template";
CREATE TABLE "public"."msg_send_template" (
  "temp_id" int8 NOT NULL DEFAULT nextval('msg_send_template_id_seq'::regclass),
  "temp_name" varchar(255) COLLATE "pg_catalog"."default",
  "send_temp_params" varchar(255) COLLATE "pg_catalog"."default",
  "send_temp_name" varchar(255) COLLATE "pg_catalog"."default",
  "send_type" char(1) COLLATE "pg_catalog"."default",
  "send_targets" text COLLATE "pg_catalog"."default",
  "sub_id" int8,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of msg_send_template
-- ----------------------------

-- ----------------------------
-- Table structure for msg_subscribe
-- ----------------------------
DROP TABLE IF EXISTS "public"."msg_subscribe";
CREATE TABLE "public"."msg_subscribe" (
  "sub_id" int8 NOT NULL DEFAULT nextval('msg_subscribe_id_seq'::regclass),
  "sub_name" varchar(255) COLLATE "pg_catalog"."default",
  "sub_desc" varchar(255) COLLATE "pg_catalog"."default",
  "sub_status" char(1) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of msg_subscribe
-- ----------------------------

-- ----------------------------
-- Table structure for msg_subscribe_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."msg_subscribe_user";
CREATE TABLE "public"."msg_subscribe_user" (
  "sub_id" int8 NOT NULL,
  "user_id" int8 NOT NULL
)
;

-- ----------------------------
-- Records of msg_subscribe_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_config";
CREATE TABLE "public"."sys_config" (
  "config_id" int8 NOT NULL DEFAULT nextval('sys_config_id_seq'::regclass),
  "config_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "config_key" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "config_value" varchar(2000) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of sys_config
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dept";
CREATE TABLE "public"."sys_dept" (
  "dept_id" int8 NOT NULL DEFAULT nextval('sys_dept_id_seq'::regclass),
  "parent_id" int8 NOT NULL,
  "dept_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "dept_leader" varchar(255) COLLATE "pg_catalog"."default",
  "dept_seq" int2,
  "dept_status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO "public"."sys_dept" VALUES (1, 0, '总公司', 'huiiro', 1, '1', '', 'admin', '2024-01-24 14:31:44', 'admin', '2024-01-24 14:31:44');
INSERT INTO "public"."sys_dept" VALUES (2, 1, '分公司', 'huii', 1, '1', '', 'admin', '2024-01-25 22:29:11', 'admin', '2024-01-25 22:29:26');

-- ----------------------------
-- Table structure for sys_dic_data
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dic_data";
CREATE TABLE "public"."sys_dic_data" (
  "data_id" int8 NOT NULL DEFAULT nextval('sys_dic_data_id_seq'::regclass),
  "data_type" varchar(50) COLLATE "pg_catalog"."default",
  "data_name" varchar(50) COLLATE "pg_catalog"."default",
  "data_key" varchar(50) COLLATE "pg_catalog"."default",
  "data_value" varchar(255) COLLATE "pg_catalog"."default",
  "data_label" varchar(50) COLLATE "pg_catalog"."default",
  "data_seq" int2,
  "data_type_info" varchar(255) COLLATE "pg_catalog"."default",
  "data_status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of sys_dic_data
-- ----------------------------
INSERT INTO "public"."sys_dic_data" VALUES (1, 'dic_type', 'test', '测试数据值', '1', '1', 1, 'default', '1', '', 'admin', '2024-01-24 14:37:59', 'admin', '2024-01-24 14:37:59');

-- ----------------------------
-- Table structure for sys_dic_type
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dic_type";
CREATE TABLE "public"."sys_dic_type" (
  "type_id" int8 NOT NULL DEFAULT nextval('sys_dic_type_id_seq'::regclass),
  "type_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "dic_type" varchar(50) COLLATE "pg_catalog"."default",
  "type_status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of sys_dic_type
-- ----------------------------
INSERT INTO "public"."sys_dic_type" VALUES (1, '测试数据', 'dic_type', '1', '123', 'admin', '2024-01-24 14:37:59', 'admin', '2024-01-24 14:37:59');

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_file";
CREATE TABLE "public"."sys_file" (
  "file_id" int8 NOT NULL DEFAULT nextval('sys_file_id_seq'::regclass),
  "file_name" varchar(255) COLLATE "pg_catalog"."default",
  "origin_name" varchar(255) COLLATE "pg_catalog"."default",
  "file_size" varchar(255) COLLATE "pg_catalog"."default",
  "file_suffix" varchar(255) COLLATE "pg_catalog"."default",
  "file_acl" varchar(255) COLLATE "pg_catalog"."default",
  "file_md5" varchar(255) COLLATE "pg_catalog"."default",
  "file_price" numeric,
  "access_url" varchar(255) COLLATE "pg_catalog"."default",
  "file_server" varchar(255) COLLATE "pg_catalog"."default",
  "file_status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of sys_file
-- ----------------------------

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job";
CREATE TABLE "public"."sys_job" (
  "job_id" int8 NOT NULL DEFAULT nextval('sys_job_id_seq'::regclass),
  "job_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "group_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "cron" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "target" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "job_status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "concurrent_status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "misfire_policy" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO "public"."sys_job" VALUES (1, '测试任务', 'DEFAULT', '*/10 * * * * ?', 'com.huii.job.task.DemoTask.demoTaskWithoutParams', '0', '0', '3', '', 'admin', '2024-01-06 22:18:05', 'admin', '2024-01-06 23:37:06');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job_log";
CREATE TABLE "public"."sys_job_log" (
  "log_id" int8 NOT NULL DEFAULT nextval('sys_job_log_id_seq'::regclass),
  "job_name" varchar(255) COLLATE "pg_catalog"."default",
  "group_name" varchar(255) COLLATE "pg_catalog"."default",
  "target" varchar(255) COLLATE "pg_catalog"."default",
  "job_status" char(1) COLLATE "pg_catalog"."default",
  "job_message" varchar(255) COLLATE "pg_catalog"."default",
  "error_info" varchar(255) COLLATE "pg_catalog"."default",
  "begin_time" timestamp(0),
  "end_time" timestamp(0),
  "cost" int4
)
;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log_login
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_log_login";
CREATE TABLE "public"."sys_log_login" (
  "login_id" int8 NOT NULL DEFAULT nextval('sys_log_login_id_seq'::regclass),
  "login_user_name" varchar(20) COLLATE "pg_catalog"."default",
  "login_ip" varchar(255) COLLATE "pg_catalog"."default",
  "login_address" varchar(255) COLLATE "pg_catalog"."default",
  "login_time" timestamp(0),
  "login_browser" varchar(255) COLLATE "pg_catalog"."default",
  "login_os" varchar(255) COLLATE "pg_catalog"."default",
  "login_type" int2,
  "login_status" char(1) COLLATE "pg_catalog"."default",
  "login_message" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of sys_log_login
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log_op
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_log_op";
CREATE TABLE "public"."sys_log_op" (
  "op_id" int8 NOT NULL DEFAULT nextval('sys_log_op_id_seq'::regclass),
  "op_user_name" varchar(255) COLLATE "pg_catalog"."default",
  "op_method_name" varchar(255) COLLATE "pg_catalog"."default",
  "op_type" int2,
  "op_time" timestamp(0),
  "op_cost_time" int8,
  "op_ip" varchar(255) COLLATE "pg_catalog"."default",
  "op_address" varchar(255) COLLATE "pg_catalog"."default",
  "op_request" varchar(255) COLLATE "pg_catalog"."default",
  "op_url" varchar(255) COLLATE "pg_catalog"."default",
  "op_req_param" varchar(2000) COLLATE "pg_catalog"."default",
  "op_res_param" varchar(2000) COLLATE "pg_catalog"."default",
  "op_status" char(1) COLLATE "pg_catalog"."default",
  "op_mark_flag" char(1) COLLATE "pg_catalog"."default",
  "op_message" varchar(2000) COLLATE "pg_catalog"."default",
  "op_desc" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of sys_log_op
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_menu";
CREATE TABLE "public"."sys_menu" (
  "menu_id" int8 NOT NULL DEFAULT nextval('sys_menu_id_seq'::regclass),
  "parent_id" int8 NOT NULL,
  "menu_type" int2,
  "menu_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_auth" varchar(255) COLLATE "pg_catalog"."default",
  "menu_icon" varchar(255) COLLATE "pg_catalog"."default",
  "menu_path" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_component" varchar(255) COLLATE "pg_catalog"."default",
  "menu_seq" int2,
  "menu_visible" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "query_param" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "public"."sys_menu" VALUES (34, 6, 3, '添加岗位', 'system:post:add', '', '/system/post/add', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (21, 4, 3, '查询用户', 'system:user:query', '', '/system/user/query', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (22, 4, 3, '添加用户', 'system:user:add', '', '/system/user/add', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (23, 4, 3, '修改用户', 'system:user:edit', '', '/system/user/edit', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (24, 4, 3, '删除用户', 'system:user:delete', '', '/system/user/delete', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (25, 4, 3, '导入用户', 'system:user:import', '', '/system/user/import', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (26, 4, 3, '导出用户', 'system:user:export', '', '/system/user/export', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (14, 2, 1, '服务监控', 'system:monitor:server', 'Cpu', '/monitor/server', 'monitor/server/Index', 4, '1', '1', NULL, NULL, 'admin', '2023-12-12 08:35:10', 'admin', '2024-01-07 14:24:05');
INSERT INTO "public"."sys_menu" VALUES (79, 18, 3, '添加表', 'tool:gen:add', '', '/tool/gen/add', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (19, 98, 1, '字典详情', 'system:dictionary:Data', 'Memo', '/system/dic/data/:typeId', 'system/dictionary/Data', 99, '0', '1', NULL, NULL, 'admin', '2023-12-11 20:07:22', 'admin', '2024-01-06 23:02:57');
INSERT INTO "public"."sys_menu" VALUES (28, 5, 3, '添加角色', 'system:role:add', '', '/system/role/add', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (29, 5, 3, '修改角色', 'system:role:edit', '', '/system/role/edit', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (30, 5, 3, '删除角色', 'system:role:delete', '', '/system/role/delete', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (31, 5, 3, '导入角色', 'system:role:import', '', '/system/role/import', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (32, 5, 3, '导出角色', 'system:role:export', '', '/system/role/export', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (35, 6, 3, '修改岗位', 'system:post:edit', '', '/system/post/edit', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (2, 0, 1, '系统监控', 'system:monitor', 'Monitor', '/monitor', '', 2, '1', '1', NULL, NULL, 'admin', '2023-12-12 08:23:00', 'admin', '2023-12-12 08:23:56');
INSERT INTO "public"."sys_menu" VALUES (17, 2, 1, '任务监控', 'system:monitor:job', 'AlarmClock', '/monitor/job', 'monitor/job/Index', 3, '1', '1', NULL, NULL, 'admin', '2023-12-12 08:37:21', 'admin', '2024-01-07 14:23:55');
INSERT INTO "public"."sys_menu" VALUES (36, 6, 3, '删除岗位', 'system:post:delete', '', '/system/post/delete', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (27, 5, 3, '查询角色', 'system:role:query', '', '/system/role/query', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (16, 2, 1, '数据监控', 'system:monitor:data', 'Histogram', '/monitor/data', 'monitor/data/Index', 5, '0', '0', NULL, NULL, 'admin', '2023-12-12 08:36:38', 'admin', '2024-01-10 20:20:09');
INSERT INTO "public"."sys_menu" VALUES (18, 3, 1, '代码生成', 'system:tool:generate', 'DocumentAdd', '/tool/generator', 'tool/generator/Index', 9, '1', '1', NULL, NULL, 'admin', '2023-12-13 19:03:58', 'admin', '2024-01-07 19:29:17');
INSERT INTO "public"."sys_menu" VALUES (33, 6, 3, '查询岗位', 'system:post:query', '', '/system/post/query', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (37, 6, 3, '导入岗位', 'system:post:import', '', '/system/post/import', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (38, 6, 3, '导出岗位', 'system:post:export', '', '/system/post/export', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (13, 2, 1, '登录日志', 'system:monitor:log:login', 'Cellphone', '/monitor/log/login', 'monitor/logLogin/Index', 2, '1', '1', NULL, NULL, 'admin', '2023-12-12 08:28:04', 'admin', '2023-12-12 17:06:10');
INSERT INTO "public"."sys_menu" VALUES (39, 7, 3, '查询部门', 'system:dept:query', '', '/system/dept/query', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (40, 7, 3, '添加部门', 'system:dept:add', '', '/system/dept/add', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (41, 7, 3, '修改部门', 'system:dept:edit', '', '/system/dept/edit', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (42, 7, 3, '删除部门', 'system:dept:delete', '', '/system/dept/delete', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (43, 8, 3, '查询菜单', 'system:menu:query', '', '/system/menu/query', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (44, 8, 3, '添加菜单', 'system:menu:add', '', '/system/menu/add', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (45, 8, 3, '修改菜单', 'system:menu:edit', '', '/system/menu/edit', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (46, 8, 3, '删除菜单', 'system:menu:delete', '', '/system/menu/delete', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (47, 9, 3, '查询字典', 'system:dic:query', '', '/system/dic/query', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (48, 9, 3, '添加字典', 'system:dic:add', '', '/system/dic/add', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (49, 9, 3, '修改字典', 'system:dic:edit', '', '/system/dic/edit', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (50, 9, 3, '删除字典', 'system:dic:delete', '', '/system/dic/delete', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (15, 2, 1, '缓存监控', 'system:monitor:cache', 'Coin', '/monitor/cache', 'monitor/cache/Index', 6, '1', '1', NULL, NULL, 'admin', '2023-12-12 08:30:27', 'admin', '2024-01-07 14:24:13');
INSERT INTO "public"."sys_menu" VALUES (11, 1, 1, '通知管理', ' system:notice', 'ChatLineRound', '/system/notice', 'system/notice/Index', 10, '1', '1', NULL, NULL, 'admin', '2023-12-12 18:06:54', 'admin', '2024-02-20 16:01:26');
INSERT INTO "public"."sys_menu" VALUES (10, 1, 1, '参数管理', 'system:config', 'SetUp', '/system/config', 'system/config/Index', 8, '1', '1', NULL, NULL, 'admin', '2023-12-11 21:20:29', 'admin', '2024-02-20 16:01:49');
INSERT INTO "public"."sys_menu" VALUES (51, 9, 3, '导入字典', 'system:dic:import', '', '/system/dic/import', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (52, 9, 3, '导出字典', 'system:dic:export', '', '/system/dic/export', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (12, 2, 1, '接口日志', 'system:monitor:log:op', 'Memo', '/monitor/log/op', 'monitor/logOp/Index', 1, '1', '1', NULL, NULL, 'admin', '2023-12-12 08:27:06', 'admin', '2023-12-12 17:06:02');
INSERT INTO "public"."sys_menu" VALUES (3, 0, 1, '系统工具', 'system:tool', 'Operation', '/tool', '', 3, '1', '1', NULL, NULL, 'admin', '2023-12-13 18:59:10', 'admin', '2023-12-13 18:59:19');
INSERT INTO "public"."sys_menu" VALUES (59, 11, 3, '查询通知', 'system:notice:query', '', '/system/notice/query', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (60, 11, 3, '添加通知', 'system:notice:add', '', '/system/notice/add', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (61, 11, 3, '修改通知', 'system:notice:edit', '', '/system/notice/edit', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (6, 1, 1, '岗位管理', 'sytem:post', 'Management', '/system/post', 'system/post/Index', 3, '1', '1', NULL, NULL, 'admin', '2023-12-13 18:59:10', 'admin', '2023-12-06 20:06:58');
INSERT INTO "public"."sys_menu" VALUES (4, 1, 1, '用户管理', 'system:user', 'UserFilled', '/system/user', 'system/user/Index', 1, '1', '1', NULL, NULL, 'admin', '2023-12-13 18:59:10', 'admin', '2023-12-13 18:59:10');
INSERT INTO "public"."sys_menu" VALUES (5, 1, 1, '角色管理', 'system:role', 'Avatar', '/system/role', 'system/role/Index', 2, '1', '1', NULL, NULL, 'admin', '2023-12-13 18:59:10', 'admin', '2023-12-13 18:59:10');
INSERT INTO "public"."sys_menu" VALUES (8, 1, 1, '菜单管理', 'system:menu', 'Menu', '/system/menu', 'system/menu/Index', 5, '1', '1', NULL, NULL, 'admin', '2023-12-13 18:59:10', 'admin', '2023-12-13 18:59:10');
INSERT INTO "public"."sys_menu" VALUES (62, 11, 3, '删除通知', 'system:notice:delete', '', '/system/notice/delete', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (64, 11, 3, '导出通知', 'system:notice:export', '', '/system/notice/export', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (53, 10, 3, '查询参数', 'system:config:query', '', '/system/config/query', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (54, 10, 3, '添加参数', 'system:config:add', '', '/system/config/add', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (55, 10, 3, '修改参数', 'system:config:edit', '', '/system/config/edit', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (56, 10, 3, '删除参数', 'system:config:delete', '', '/system/config/delete', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (57, 10, 3, '导入参数', 'system:config:import', '', '/system/config/import', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (58, 10, 3, '导出参数', 'system:config:export', '', '/system/config/export', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (63, 11, 3, '导入通知', 'system:notice:import', '', '/system/notice/import', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (65, 12, 3, '查询接口日志', 'system:log:op:query', '', '/system/logOp/query', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (66, 12, 3, '修改接口日志', 'system:log:op:edit', '', '/system/logOp/edit', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (67, 12, 3, '删除接口日志', 'system:log:op:delete', '', '/system/logOp/delete', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (68, 12, 3, '删除全部接口日志', 'system:log:op:delete:all', '', '/system/logOp/deleteAll', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (69, 12, 3, '导入接口日志', 'system:log:op:import', '', '/system/logOp/import', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (70, 12, 3, '导出接口日志', 'system:log:op:export', '', '/system/logOp/export', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (71, 13, 3, '查询登录日志', 'system:log:login:query', '', '/system/logLogin/query', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (72, 13, 3, '修改登录日志', 'system:log:login:edit', '', '/system/logLogin/edit', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (73, 13, 3, '删除登录日志', 'system:log:login:delete', '', '/system/logLogin/delete', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (74, 13, 3, '删除全部登录日志', 'system:log:login:delete:all', '', '/system/logLogin/deleteAll', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (75, 13, 3, '导入登录日志', 'system:log:login:import', '', '/system/logLogin/import', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (76, 13, 3, '导出登录日志', 'system:log:login:export', '', '/system/logLogin/export', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (78, 18, 3, '查询表', 'tool:gen:query', '', '/tool/gen/query', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (80, 18, 3, '修改表', 'tool:gen:edit', '', '/tool/gen/edit', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (81, 18, 3, '删除表', 'tool:gen:delete', '', '/tool/gen/delete', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (82, 18, 3, '导出代码', 'tool:gen:export', '', '/tool/gen/export', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (83, 18, 3, '同步代码', 'tool:gen:sync', '', '/tool/gen/sync', '', 1, '0', '1', NULL, NULL, 'admin', '2023-12-13 22:28:42', 'admin', '2023-12-13 22:28:42');
INSERT INTO "public"."sys_menu" VALUES (106, 105, 3, '查询邮件模板', 'tool:msg:mail:query', '', '/msg/mail/query', '', 1, '1', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-01-07 15:31:20');
INSERT INTO "public"."sys_menu" VALUES (107, 105, 3, '新增邮件模板', 'tool:msg:mail:add', '', '/msg/mail/add', '', 1, '1', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-01-07 15:31:20');
INSERT INTO "public"."sys_menu" VALUES (108, 105, 3, '修改邮件模板', 'tool:msg:mail:edit', '', '/msg/mail/edit', '', 1, '1', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-01-07 15:31:20');
INSERT INTO "public"."sys_menu" VALUES (109, 105, 3, '删除邮件模板', 'tool:msg:mail:delete', '', '/msg/mail/delete', '', 1, '1', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-01-07 15:31:20');
INSERT INTO "public"."sys_menu" VALUES (105, 3, 1, '邮件模板', 'tool:msg:mail', 'Management', '/msg/mail', 'tool/msgMailTemplate/Index', 2, '1', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-02-21 02:52:51');
INSERT INTO "public"."sys_menu" VALUES (116, 98, 1, '推送日志', 'msg:send:log', 'DocumentRemove', '/msg/send/log/:name', 'tool/msgSendTemplate/SendLog', 20, '0', '1', NULL, '', 'admin', '2024-02-21 03:55:06', 'admin', '2024-02-21 15:08:27');
INSERT INTO "public"."sys_menu" VALUES (114, 110, 3, '删除消息', 'system:message:delete', '', '/system/message/delete', '', 1, '0', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-02-21 04:10:57');
INSERT INTO "public"."sys_menu" VALUES (111, 110, 3, '查询消息', 'system:message:query', '', '/system/message/query', '', 1, '0', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-02-21 04:11:06');
INSERT INTO "public"."sys_menu" VALUES (113, 110, 3, '修改消息', 'system:message:edit', '', '/system/message/edit', '', 1, '0', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-02-21 04:11:10');
INSERT INTO "public"."sys_menu" VALUES (112, 110, 3, '新增消息', 'system:message:add', '', '/system/message/add', '', 1, '0', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-02-21 04:11:16');
INSERT INTO "public"."sys_menu" VALUES (115, 3, 1, '订阅管理', 'tool:msg:sub', 'Bell', '/msg/sub', 'tool/msgSubscribe/Index', 1, '1', '1', NULL, '', 'admin', '2024-02-21 02:31:20', 'admin', '2024-02-21 02:31:37');
INSERT INTO "public"."sys_menu" VALUES (100, 3, 1, '消息模板', 'tool:msg:send', 'SetUp', '/msg/send', 'tool/msgSendTemplate/Index', 3, '1', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-02-21 02:53:05');
INSERT INTO "public"."sys_menu" VALUES (91, 1, 1, '文件管理', 'system:file', 'Folder', '/system/file', 'system/file/Index', 7, '1', '1', '', '', 'admin', '2024-01-03 16:32:39', 'admin', '2024-01-03 17:04:41');
INSERT INTO "public"."sys_menu" VALUES (92, 91, 3, '查询文件', 'system:file:query', '', '/system/file/query', '', 1, '0', '1', '', '', 'admin', '2024-01-03 16:32:39', 'admin', '2024-01-03 16:32:39');
INSERT INTO "public"."sys_menu" VALUES (93, 91, 3, '新增文件', 'system:file:add', '', '/system/file/add', '', 1, '0', '1', '', '', 'admin', '2024-01-03 16:32:39', 'admin', '2024-01-03 16:32:39');
INSERT INTO "public"."sys_menu" VALUES (94, 91, 3, '修改文件', 'system:file:edit', '', '/system/file/edit', '', 1, '0', '1', '', '', 'admin', '2024-01-03 16:32:39', 'admin', '2024-01-03 16:32:39');
INSERT INTO "public"."sys_menu" VALUES (95, 91, 3, '删除文件', 'system:file:delete', '', '/system/file/delete', '', 1, '0', '1', '', '', 'admin', '2024-01-03 16:32:39', 'admin', '2024-01-03 16:32:39');
INSERT INTO "public"."sys_menu" VALUES (96, 91, 3, '导入文件', 'system:file:import', '', '/system/file/import', '', 1, '0', '1', '', '', 'admin', '2024-01-03 16:32:39', 'admin', '2024-01-03 16:32:39');
INSERT INTO "public"."sys_menu" VALUES (97, 91, 3, '导出文件', 'system:file:export', '', '/system/file/export', '', 1, '0', '1', '', '', 'admin', '2024-01-03 16:32:39', 'admin', '2024-01-03 16:32:39');
INSERT INTO "public"."sys_menu" VALUES (98, 0, 2, '二级菜单', 'sub:all', '', '/sub', '', 99, '0', '1', NULL, '', 'admin', '2024-01-06 23:02:11', 'admin', '2024-01-06 23:02:11');
INSERT INTO "public"."sys_menu" VALUES (77, 98, 1, '字段详情', 'gen:column', 'Document', '/gen/column/:tableId', 'tool/generator/Column', 97, '0', '1', NULL, NULL, 'admin', '2023-12-15 17:16:21', 'admin', '2024-02-21 15:07:34');
INSERT INTO "public"."sys_menu" VALUES (101, 100, 3, '查询发送模板', 'tool:msg:send:query', '', '/msg/send/query', '', 1, '1', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-01-07 15:31:20');
INSERT INTO "public"."sys_menu" VALUES (102, 100, 3, '新增发送模板', 'tool:msg:send:add', '', '/msg/send/add', '', 1, '1', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-01-07 15:31:20');
INSERT INTO "public"."sys_menu" VALUES (103, 100, 3, '修改发送模板', 'tool:msg:send:edit', '', '/msg/send/edit', '', 1, '1', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-01-07 15:31:20');
INSERT INTO "public"."sys_menu" VALUES (104, 100, 3, '删除发送模板', 'tool:msg:send:delete', '', '/msg/send/delete', '', 1, '1', '1', '', '', 'admin', '2024-01-07 15:31:20', 'admin', '2024-01-07 15:31:20');
INSERT INTO "public"."sys_menu" VALUES (117, 98, 1, '订阅用户', 'tool:msg:sub:user', 'Plus', '/msg/sub/user/:sid', 'tool/msgSubscribe/SubUser', 10, '0', '1', NULL, '', 'admin', '2024-02-21 15:05:17', 'admin', '2024-02-21 15:08:14');
INSERT INTO "public"."sys_menu" VALUES (20, 98, 1, '分配角色', 'system:role:auth:user', 'Plus', '/system/role/auth/user/:roleId', 'system/role/AuthUser', 11, '0', '1', NULL, NULL, 'admin', '2023-11-29 15:08:49', 'admin', '2024-02-21 15:08:19');
INSERT INTO "public"."sys_menu" VALUES (99, 98, 1, '任务日志', 'system:job:log', 'DocumentRemove', '/system/job/log/:name', 'monitor/job/JobLog', 21, '0', '1', NULL, '', 'admin', '2024-01-06 23:04:45', 'admin', '2024-02-21 15:08:31');
INSERT INTO "public"."sys_menu" VALUES (110, 1, 1, '消息管理', 'system:message', 'ChatLineSquare', '/system/message', 'system/message/Index', 9, '1', '1', NULL, '', 'admin', '2024-02-20 16:00:09', 'admin', '2024-02-20 16:02:23');
INSERT INTO "public"."sys_menu" VALUES (1, 0, 1, '系统管理', 'system:system', 'Promotion', '/system', '', 1, '1', '1', NULL, NULL, 'admin', '2023-11-22 21:59:22', 'admin', '2023-11-22 22:28:39');
INSERT INTO "public"."sys_menu" VALUES (7, 1, 1, '部门管理', 'sytem:dept', 'DataBoard', '/system/dept', 'system/dept/Index', 4, '1', '1', NULL, NULL, 'admin', '2023-12-13 18:59:10', 'admin', '2023-11-28 17:19:20');
INSERT INTO "public"."sys_menu" VALUES (9, 1, 1, '字典管理', 'system:dic', 'Reading', '/system/dic', 'system/dictionary/Index', 6, '1', '1', NULL, NULL, 'admin', '2023-12-13 18:59:10', 'admin', '2023-12-13 18:59:10');

-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_message";
CREATE TABLE "public"."sys_message" (
  "message_id" int8 NOT NULL DEFAULT nextval('sys_message_id_seq'::regclass),
  "send_id" int8,
  "receive_id" int8,
  "message" text COLLATE "pg_catalog"."default",
  "message_status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "message_type" char(1) COLLATE "pg_catalog"."default",
  "message_read" char(1) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of sys_message
-- ----------------------------

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_notice";
CREATE TABLE "public"."sys_notice" (
  "notice_id" int8 NOT NULL DEFAULT nextval('sys_notice_id_seq'::regclass),
  "notice_title" varchar(255) COLLATE "pg_catalog"."default",
  "notice_content" text COLLATE "pg_catalog"."default",
  "notice_type" int2,
  "notice_status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_post";
CREATE TABLE "public"."sys_post" (
  "post_id" int8 NOT NULL DEFAULT nextval('sys_post_id_seq'::regclass),
  "post_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "post_key" varchar(50) COLLATE "pg_catalog"."default",
  "post_duty" varchar(255) COLLATE "pg_catalog"."default",
  "post_seq" int2,
  "post_status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO "public"."sys_post" VALUES (1, 'CEO', 'CEO', '', 1, '1', NULL, 'admin', '2023-11-30 16:23:18', 'admin', '2023-11-30 16:23:18');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role" (
  "role_id" int8 NOT NULL DEFAULT nextval('sys_role_id_seq'::regclass),
  "role_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "role_key" varchar(50) COLLATE "pg_catalog"."default",
  "role_scope" char(1) COLLATE "pg_catalog"."default",
  "role_seq" int2,
  "role_status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "public"."sys_role" VALUES (1, '管理员', 'ADMIN', '1', 1, '1', NULL, 'admin', '2023-11-19 17:09:16', 'admin', '2023-11-19 17:09:16');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_dept";
CREATE TABLE "public"."sys_role_dept" (
  "role_id" int8 NOT NULL,
  "dept_id" int8 NOT NULL
)
;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO "public"."sys_role_dept" VALUES (1, 1);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_menu";
CREATE TABLE "public"."sys_role_menu" (
  "role_id" int8 NOT NULL,
  "menu_id" int8 NOT NULL
)
;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user" (
  "user_id" int8 NOT NULL DEFAULT nextval('sys_user_id_seq'::regclass),
  "dept_id" int8,
  "user_name" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "nick_name" varchar(20) COLLATE "pg_catalog"."default",
  "password" varchar(255) COLLATE "pg_catalog"."default",
  "phone" varchar(20) COLLATE "pg_catalog"."default",
  "email" varchar(40) COLLATE "pg_catalog"."default",
  "sexual" char(1) COLLATE "pg_catalog"."default",
  "avatar" varchar(255) COLLATE "pg_catalog"."default",
  "login_ip" varchar(255) COLLATE "pg_catalog"."default",
  "login_time" timestamp(0),
  "delete_flag" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "user_status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0)
)
;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user" VALUES (1, 1, 'admin', 'huii', '$2a$10$Tt68.t/JevKsB7rKG/12bOudxvDz9LHOHlEII/.2uPzsdSig9YEOe', '19923352758', '1659009445@qq.com', '1', 'http://127.0.0.1:8080/oss/local?module=avatar&fileName=202401241441e7cfdc292522.jpg', '0:0:0:0:0:0:0:1', '2024-03-15 23:19:43', '0', '1', NULL, NULL, '2023-11-29 16:09:15', NULL, '2024-03-15 23:19:43');

-- ----------------------------
-- Table structure for sys_user_oauth
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_oauth";
CREATE TABLE "public"."sys_user_oauth" (
  "user_id" int8 NOT NULL,
  "oauth_provider" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "oauth_identify" varchar(64) COLLATE "pg_catalog"."default",
  "oauth_user_name" varchar(255) COLLATE "pg_catalog"."default",
  "oauth_user_avatar" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0)
)
;

-- ----------------------------
-- Records of sys_user_oauth
-- ----------------------------
INSERT INTO "public"."sys_user_oauth" VALUES (1, 'gitee', '8520863', 'HuYi', 'https://gitee.com/assets/no_portrait.png', '2024-01-08 19:39:05');
INSERT INTO "public"."sys_user_oauth" VALUES (1, 'github', '71134144', '1659009445', 'https://avatars.githubusercontent.com/u/71134144?v=4', '2024-01-08 19:44:08');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_post";
CREATE TABLE "public"."sys_user_post" (
  "user_id" int8 NOT NULL,
  "post_id" int8 NOT NULL
)
;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_role";
CREATE TABLE "public"."sys_user_role" (
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL
)
;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO "public"."sys_user_role" VALUES (1, 1);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."gen_column_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."gen_table_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."msg_mail_template_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."msg_send_log_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."msg_send_template_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."msg_subscribe_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_config_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_dept_id_seq"', 3, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_dic_data_id_seq"', 2, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_dic_type_id_seq"', 2, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_file_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_job_id_seq"', 2, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_job_log_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_log_login_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_log_op_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_menu_id_seq"', 1000, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_message_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_notice_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_post_id_seq"', 2, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_role_id_seq"', 2, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."sys_user_id_seq"', 2, true);

-- ----------------------------
-- Primary Key structure for table gen_column
-- ----------------------------
ALTER TABLE "public"."gen_column" ADD CONSTRAINT "gen_column_pkey" PRIMARY KEY ("column_id");

-- ----------------------------
-- Primary Key structure for table gen_table
-- ----------------------------
ALTER TABLE "public"."gen_table" ADD CONSTRAINT "gen_table_pkey" PRIMARY KEY ("table_id");

-- ----------------------------
-- Uniques structure for table msg_mail_template
-- ----------------------------
ALTER TABLE "public"."msg_mail_template" ADD CONSTRAINT "uk_temp_name" UNIQUE ("temp_name");

-- ----------------------------
-- Primary Key structure for table msg_mail_template
-- ----------------------------
ALTER TABLE "public"."msg_mail_template" ADD CONSTRAINT "msg_mail_template_pkey" PRIMARY KEY ("mail_temp_id");

-- ----------------------------
-- Primary Key structure for table msg_send_log
-- ----------------------------
ALTER TABLE "public"."msg_send_log" ADD CONSTRAINT "msg_send_log_pkey" PRIMARY KEY ("log_id");

-- ----------------------------
-- Primary Key structure for table msg_send_template
-- ----------------------------
ALTER TABLE "public"."msg_send_template" ADD CONSTRAINT "sys_msg_template_pkey" PRIMARY KEY ("temp_id");

-- ----------------------------
-- Uniques structure for table msg_subscribe
-- ----------------------------
ALTER TABLE "public"."msg_subscribe" ADD CONSTRAINT "uk_sub_name" UNIQUE ("sub_name");

-- ----------------------------
-- Primary Key structure for table msg_subscribe
-- ----------------------------
ALTER TABLE "public"."msg_subscribe" ADD CONSTRAINT "msg_subscribe_pkey" PRIMARY KEY ("sub_id");

-- ----------------------------
-- Primary Key structure for table msg_subscribe_user
-- ----------------------------
ALTER TABLE "public"."msg_subscribe_user" ADD CONSTRAINT "msg_subscribe_user_pkey" PRIMARY KEY ("sub_id", "user_id");

-- ----------------------------
-- Uniques structure for table sys_config
-- ----------------------------
ALTER TABLE "public"."sys_config" ADD CONSTRAINT "uk_config_key" UNIQUE ("config_key");

-- ----------------------------
-- Primary Key structure for table sys_config
-- ----------------------------
ALTER TABLE "public"."sys_config" ADD CONSTRAINT "sys_config_pkey" PRIMARY KEY ("config_id");

-- ----------------------------
-- Uniques structure for table sys_dept
-- ----------------------------
ALTER TABLE "public"."sys_dept" ADD CONSTRAINT "uk_dept_name" UNIQUE ("dept_name");

-- ----------------------------
-- Primary Key structure for table sys_dept
-- ----------------------------
ALTER TABLE "public"."sys_dept" ADD CONSTRAINT "sys_dept_pkey" PRIMARY KEY ("dept_id");

-- ----------------------------
-- Primary Key structure for table sys_dic_data
-- ----------------------------
ALTER TABLE "public"."sys_dic_data" ADD CONSTRAINT "sys_dic_data_pkey" PRIMARY KEY ("data_id");

-- ----------------------------
-- Uniques structure for table sys_dic_type
-- ----------------------------
ALTER TABLE "public"."sys_dic_type" ADD CONSTRAINT "uk_type_name" UNIQUE ("type_name");

-- ----------------------------
-- Primary Key structure for table sys_dic_type
-- ----------------------------
ALTER TABLE "public"."sys_dic_type" ADD CONSTRAINT "dic_type_pkey" PRIMARY KEY ("type_id");

-- ----------------------------
-- Indexes structure for table sys_file
-- ----------------------------
CREATE INDEX "idx_file_name" ON "public"."sys_file" USING btree (
  "file_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_file
-- ----------------------------
ALTER TABLE "public"."sys_file" ADD CONSTRAINT "sys_file_pkey" PRIMARY KEY ("file_id");

-- ----------------------------
-- Primary Key structure for table sys_job
-- ----------------------------
ALTER TABLE "public"."sys_job" ADD CONSTRAINT "sys_job_pkey" PRIMARY KEY ("job_id");

-- ----------------------------
-- Primary Key structure for table sys_job_log
-- ----------------------------
ALTER TABLE "public"."sys_job_log" ADD CONSTRAINT "sys_job_log_pkey" PRIMARY KEY ("log_id");

-- ----------------------------
-- Primary Key structure for table sys_log_login
-- ----------------------------
ALTER TABLE "public"."sys_log_login" ADD CONSTRAINT "sys_log_login_pkey" PRIMARY KEY ("login_id");

-- ----------------------------
-- Primary Key structure for table sys_log_op
-- ----------------------------
ALTER TABLE "public"."sys_log_op" ADD CONSTRAINT "sys_log_op_pkey" PRIMARY KEY ("op_id");

-- ----------------------------
-- Uniques structure for table sys_menu
-- ----------------------------
ALTER TABLE "public"."sys_menu" ADD CONSTRAINT "uk_menu_name" UNIQUE ("menu_name");

-- ----------------------------
-- Primary Key structure for table sys_menu
-- ----------------------------
ALTER TABLE "public"."sys_menu" ADD CONSTRAINT "sys_menu_pkey" PRIMARY KEY ("menu_id");

-- ----------------------------
-- Primary Key structure for table sys_message
-- ----------------------------
ALTER TABLE "public"."sys_message" ADD CONSTRAINT "sys_message_pkey" PRIMARY KEY ("message_id");

-- ----------------------------
-- Primary Key structure for table sys_notice
-- ----------------------------
ALTER TABLE "public"."sys_notice" ADD CONSTRAINT "sys_notice_pkey" PRIMARY KEY ("notice_id");

-- ----------------------------
-- Primary Key structure for table sys_post
-- ----------------------------
ALTER TABLE "public"."sys_post" ADD CONSTRAINT "sys_post_pkey" PRIMARY KEY ("post_id");

-- ----------------------------
-- Uniques structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role" ADD CONSTRAINT "uk_role_name" UNIQUE ("role_name");

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role" ADD CONSTRAINT "sys_role_pkey" PRIMARY KEY ("role_id");

-- ----------------------------
-- Primary Key structure for table sys_role_dept
-- ----------------------------
ALTER TABLE "public"."sys_role_dept" ADD CONSTRAINT "sys_role_dept_pkey" PRIMARY KEY ("role_id", "dept_id");

-- ----------------------------
-- Primary Key structure for table sys_role_menu
-- ----------------------------
ALTER TABLE "public"."sys_role_menu" ADD CONSTRAINT "sys_role_menu_pkey" PRIMARY KEY ("role_id", "menu_id");

-- ----------------------------
-- Uniques structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "uk_user_name" UNIQUE ("user_name");

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "sys_user_pkey" PRIMARY KEY ("user_id");

-- ----------------------------
-- Primary Key structure for table sys_user_oauth
-- ----------------------------
ALTER TABLE "public"."sys_user_oauth" ADD CONSTRAINT "sys_user_social_pkey" PRIMARY KEY ("user_id", "oauth_provider");

-- ----------------------------
-- Primary Key structure for table sys_user_post
-- ----------------------------
ALTER TABLE "public"."sys_user_post" ADD CONSTRAINT "sys_user_post_pkey" PRIMARY KEY ("user_id", "post_id");

-- ----------------------------
-- Primary Key structure for table sys_user_role
-- ----------------------------
ALTER TABLE "public"."sys_user_role" ADD CONSTRAINT "sys_user_role_pkey" PRIMARY KEY ("user_id", "role_id");
