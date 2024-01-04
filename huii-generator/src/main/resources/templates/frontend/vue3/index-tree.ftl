<template>
  <el-card>
    <!--formSearch-->
    <el-form :inline="true" :size="size" v-show="showSearch">
      <!--searchParam-->
      <#list columns as col>
      <#if col.isQueryField == "1">
      <el-form-item label="<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>" class="global-input-item">
        <#if col.formType == "input">
      <el-input v-model="query.${col.javaField}" placeholder="请输入<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>"
                class="global-input" :size="size"/>
        </#if>
        <#if col.formType == "select">
        <el-select v-model="query.${col.javaField}" placeholder="请选择<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>"
                 :size="size">
            <el-option
                  v-for="item in dictionary"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"/>
        </el-select>
        </#if>
        <#if col.formType == "datetime">
              <el-date-picker
                      type="datetimerange"
                      v-model="time" />
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"/>
        </#if>
      </el-form-item>
      </#if>
      </#list>
      <!--fixed-->
      <el-form-item>
        <el-button :size="size" :icon="Search" type="primary" plain @click="getData">查询</el-button>
        <el-button :size="size" :icon="Refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>
    <!--formButton-->
    <el-form :inline="true" :size="size">
      <!--left select-->
      <!--add-->
      <#if genAddInterface == "1">
      <el-form-item class="global-form-item-margin" v-if="checkPermission('${authPrefix}:add')">
        <el-button :size="size" :icon="Plus" @click="handleInsert"
                   :color="layoutStore.BtnInsert" plain>添加${moduleFunctionName}
        </el-button>
      </el-form-item>
      </#if>
      <!--right fixed-->
      <el-form-item class="global-form-item-right">
        <!--级联删除按钮 仅树表生效-->
        <el-button :size="size" circle @click="handleAllowCascadeDelete">
          <el-icon>
            <component :is="allowIcon"/>
          </el-icon>
        </el-button>
        <!--折叠树形按钮 仅树表生效-->
        <el-button :size="size" :icon="Sort" circle @click="handleExpandAll"/>
        <!--显示/隐藏时间列-->
        <el-button :size="size" :icon="Timer" circle @click="handleExpandTime"/>
        <!--隐藏搜索栏按钮-->
        <el-button :size="size" :icon="Search" circle @click="handleHideSearch"/>
        <!--刷新按钮-->
        <el-button :size="size" :icon="Refresh" circle @click="handleRefresh"/>
      </el-form-item>
    </el-form>
    <!--dataTable-->
    <el-table :data="tableData"
              v-loading="loading"
              :size="size"
              :row-key="rowKey"
              :tree-props="treeProps"
              :default-expand-all="expandTable"
              :expand-row-keys="expandRowKeys"
              :highlight-current-row="true"
              header-cell-class-name="global-table-header"
              class="global-table"
              stripe>
        <#list columns as col>
        <el-table-column prop="${col.javaField}" label="<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>" align="center" min-width="120"/>
        </#list>
        <el-table-column v-if="showTimeColumn" prop="createTime" label="创建日期" align="center" sortable width="170"/>
        <el-table-column v-if="showTimeColumn" prop="updateTime" label="更新日期" align="center" sortable width="170"/>
        <el-table-column label="${moduleFunctionName}操作" align="center" width="200" fixed="right"
                         v-if="checkPermissions(['${authPrefix}:add','${authPrefix}:edit','${authPrefix}:delete'])">
          <template #default="scope">
            <div class="display">
              <div class="display" v-if="checkPermission('${authPrefix}:add')">
                <el-button class="global-table-btn"
                           size="small" type="primary" link :icon="Plus"
                           @click="handleInsert(scope.$index, scope.row)">
                    添加
                </el-button>
                <el-divider direction="vertical"/>
              </div>
              <div class="display" v-if="checkPermission('${authPrefix}:edit')">
                <el-button class="global-table-btn"
                           size="small" type="primary" link :icon="Edit"
                           @click="handleEdit(scope.$index, scope.row)">
                    编辑
                </el-button>
                <el-divider direction="vertical"/>
              </div>
              <div class="display" v-if="checkPermission('${authPrefix}:delete')">
                <el-button class="global-table-btn red"
                           size="small" type="primary" link :icon="Delete"
                           @click="handleDelete(scope.$index, scope.row)">
                    删除
                </el-button>
              </div>
            </div>
          </template>
        </el-table-column>
    </el-table>
  </el-card>

  <el-dialog class="global-dialog-iu"
           title=${moduleFunctionName}管理" v-model="dialogVisible"
           :close-on-click-modal="false"
           @close="handleCloseForm">
    <el-form :model="form"
             :rules="formRules"
             ref="formRuleRef">
      <el-row>
      <#list columns as col>
        <el-form-item label="<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>" label-width="85" prop="${col.javaField}">
          <#if col.formType == "input">
            <el-input v-model="form.${col.javaField}" autocomplete="off"/>
          </#if>
          <#if col.formType == "input-number">
            <el-input-number v-model="form.${col.javaField}"
                             :min="1" :max="99"
                             controls-position="right"
                             autocomplete="off"
                             style="width: 100%"/>
          </#if>
          <#if col.formType == "textarea">
            <el-input v-model="form.${col.javaField}"
                      autocomplete="off"
                      type="textarea"
                      show-word-limit
                      maxlength="100"
                      :rows="1"/>
          </#if>
          <#if col.formType == "radio">
            <el-radio-group v-model="form.${col.javaField}">
                <el-radio v-for="option in dictionary" :key="option.value" :label="option.value">
                    {{ option.label }}
                </el-radio>
            </el-radio-group>
          </#if>
          <#if col.formType == "select">
            <el-select v-model="form.${col.javaField}" placeholder=""
                       :size="size">
                <el-option
                        v-for="item in dictionary"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"/>
            </el-select>
          </#if>
          <#if col.formType == "tree-select">
            <el-tree-select v-model="form.${col.javaField}"
                            placeholder=""
                            :props="{value: 'id', label: 'label', children: 'children'}"
                            :data="dictionary"
                            check-strictly
                            style="width: 100%;"/>
          </#if>
        </el-form-item>
      </#list>
      </el-row>
      <el-row v-show="isEdit">
        <el-col :span="11">
          <el-form-item label="创建时间" label-width="85" prop="createTime">
            <el-input v-model="form.createTime" autocomplete="off" readonly="readonly"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="更新时间" label-width="85" prop="updateTime">
            <el-input v-model="form.updateTime" autocomplete="off" readonly="readonly"/>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="handleCloseForm">取 消</el-button>
      <el-button type="primary" @click="handleSubmitForm(formRuleRef)">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {onMounted, ref, shallowRef} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {Check, Close} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {delete${className}, export${className}, import${className}, insert${className}, update${className}} from "@/api";

//store
const layoutStore = useLayoutStore();
//layout
const size = layoutStore.tableSize;
//mounted
onMounted(() => {
    getData();
});

/**
 * 查询参数
 */
//加载标志
const loading = ref(false);
//表格数据
const tableData = ref();
//查询参数
const query = ref({
    <#list columns as col>
    <#if col.isQueryField == "1">
    ${col.javaField}: <#if col.javaType == "Long" || col.javaType == "Integer" || col.javaType == "Double">0<#else>''</#if>,
    </#if>
    </#list>
});
<#list columns as col>
<#if col.formType == "datetime">
const time = ref();
</#if>
</#list>

/**
 * 查询数据
 */
const getData = () => {
    loading.value = true;
    get${className}List(query.value).then(res => {
        tableData.value = res.data;
        loading.value = false;
    });
};

/**
 * 重置查询参数按钮
 */
const handleReset = () => {
    <#list columns as col>
    <#if col.isQueryField == "1">
    query.value.${col.javaField} = <#if col.javaType == "Long" || col.javaType == "Integer" || col.javaType == "Double">0<#else>''</#if>;
    </#if>
    </#list>
    <#list columns as col>
    <#if col.formType == "datetime">
    time.value = '';
    </#if>
    </#list>
    getData();
};

/**
 * 级联删除配置
 */
//级联删除标志
const allow = ref(false);
//级联删除图标
const allowIcon = shallowRef(Close);
const handleAllowCascadeDelete = () => {
    if (allow.value === false) {
        allow.value = true;
        allowIcon.value = Check;
        ElMessage.info('已允许级联删除');
    } else {
        allow.value = false;
        allowIcon.value = Close;
        ElMessage.warning('已关闭级联删除');
    }
};

/**
 * 树表参数配置
 */
//rowKey 树表id
const rowKey = ref('<#list columns as col><#if col.isPrimaryKey == "1">${col.javaField}</#if></#list>');
//treeProps 树形结构
const treeProps = ref({children: 'children', childrenFlag: 'childrenFlag'});
//展开/收起表格
const expandTable = ref(false);
//展开/收起表格数据
const expandRowKeys = ref([1]);
const handleExpandAll = () => {
    expandRowKeys.value = [1];
    expandTable.value = !expandTable.value;
};

/**
 * 隐藏时间列
 */
const showTimeColumn = ref(false);
const handleExpandTime = () => {
    showTimeColumn.value = !showTimeColumn.value;
};

/**
 * 隐藏搜索按钮
 */
//搜索按钮
const showSearch = ref(true);
const handleHideSearch = () => {
    showSearch.value = !showSearch.value;
};

/**
 * 刷新按钮
 */
const handleRefresh = () => {
    getData();
    ElMessage.info('已刷新');
};

/**
 * 添加修改对话框
 */
//表单数据
const form = ref({
    <#list columns as col>
    ${col.javaField}: <#if col.javaType == "Long" || col.javaType == "Integer" || col.javaType == "Double">0<#else>''</#if>,
    </#list>
    createTime: '',
    updateTime: ''
});
//表单数据校验规则
const formRules = ref({
    <#list columns as col>
    <#if col.checkUnique == "1" || col.isRequired == "1" || col.isPrimaryKey == "1">
    ${col.javaField}: [
        {required: true, message: '请输入<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>', trigger: 'blur'},
    ],
    </#if>
    </#list>
});
//表单校验规则ref
const formRuleRef = ref<FormInstance>();

//是否编辑
const isEdit = ref(false);
//对话框
const dialogVisible = ref(false);
/**
 * 关闭表单
 */
const handleCloseForm = () => {
    isEdit.value = false;
    dialogVisible.value = false;
};

/**
 * 提交表单
 */
const handleSubmitForm = async (fr: FormInstance | undefined) => {
    //@ts-ignore
    await fr.validate((valid) => {
        if (valid) {
            if (isEdit.value) {
                doUpdate();
            } else {
                doInsert();
            }
        }
    });
};
const doUpdate = () => {
    update${className}(form.value).then((res) => {
        if (res.code === 0) {
            isEdit.value = false;
            dialogVisible.value = false;
            getData();
        }
    });
};
const doInsert = () => {
    insert${className}(form.value).then((res) => {
        if (res.code === 0) {
            isEdit.value = false;
            dialogVisible.value = false;
            getData();
        }
    });
};

/**
 * 点击添加
 */
//@ts-ignore
const handleInsert = (index, row) => {
    form.value = {
        <#list columns as col>
        ${col.javaField}: <#if col.javaType == "Long" || col.javaType == "Integer" || col.javaType == "Double">0<#else>''</#if>,
        </#list>
        createTime: "",
        updateTime: ""
    };
    isEdit.value = false;
    dialogVisible.value = true;
};

/**
 * 点击编辑
 */
//@ts-ignore
const handleEdit = (index, row) => {
    get${className}Singleton(row.<#list columns as col><#if col.isPrimaryKey == "1">${col.javaField}</#if></#list>).then(res => {
        form.value = res.data;
        isEdit.value = true;
        dialogVisible.value = true;
    });
};
/**
 * 删除
 */
//@ts-ignore
const handleDelete = (index, row) => {
    ElMessageBox.confirm(
        '将删除ID为  \'' + row.<#list columns as col><#if col.isPrimaryKey == "1">${col.javaField}</#if></#list> + '\'  的数据项，是否确认？', '提示',
        {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
    ).then(() => {
        delete${className}(allow.value === true ? '1' : '0', row.<#list columns as col><#if col.isPrimaryKey == "1">${col.javaField}</#if></#list>).then((res) => {
            if (res.code === 0) {
                getData();
            }
        });
    }).catch();
};
</script>

<style scoped lang="scss">
/*按钮栏 左侧 item 样式*/
.global-form-item-margin {
    margin-right: 10px;
}

/*按钮栏 右侧 item 样式*/
.global-form-item-right {
    margin-right: 0;
    float: right;
}

.red {
    color: red;
}
</style>