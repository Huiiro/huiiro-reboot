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
      <#if genAddInterface == "1">
      <!--add-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('${authPrefix}:add')">
        <el-button :size="size" :icon="Plus" @click="handleInsert"
                   :color="layoutStore.BtnInsert" plain>添加${moduleFunctionName}
        </el-button>
      </el-form-item>
      </#if>
      <#if genEditInterface == "1">
      <!--edit-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('${authPrefix}:edit')">
        <el-button :size="size" :icon="Edit" @click="handleEdit"
                   :color="layoutStore.BtnUpdate" plain :disabled="!selectSingle">修改${moduleFunctionName}
        </el-button>
      </el-form-item>
      </#if>
      <#if genDeleteInterface == "1">
      <!--delete-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('${authPrefix}:delete')">
        <el-button :size="size" :icon="Delete" @click="handleDelete"
                   :color="layoutStore.BtnDelete" plain :disabled="selectable">删除${moduleFunctionName}
        </el-button>
      </el-form-item>
      </#if>
      <#if genImportInterface == "1">
      <!--import-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('${authPrefix}:import')">
        <el-button :size="size" :icon="Download" @click="handleImport"
                   :color="layoutStore.BtnImport" plain>导入${moduleFunctionName}
        </el-button>
      </el-form-item>
      </#if>
      <#if genExportInterface == "1">
      <!--export-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('${authPrefix}:export')">
        <el-button :size="size" :icon="Upload" @click="handleExport"
                   :color="layoutStore.BtnExport" plain>导出${moduleFunctionName}
        </el-button>
      </el-form-item>
      </#if>
      <!--right fixed-->
      <el-form-item class="global-form-item-right">
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
              :highlight-current-row="true"
              header-cell-class-name="global-table-header"
              class="global-table"
              stripe
              @selection-change="selectionChange">
      <el-table-column type="selection" width="55"/>
      <#list columns as col>
      <#if col.javaField !="updateBy" && col.javaField !="createBy" && col.javaField != "createTime" && col.javaField != "updateTime">
      <el-table-column prop="${col.javaField}" label="<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>" align="center" min-width="120"/>
      </#if>
      </#list>
      <el-table-column v-if="showTimeColumn" prop="createTime" label="创建日期" align="center" sortable width="170"/>
      <el-table-column v-if="showTimeColumn" prop="updateTime" label="更新日期" align="center" sortable width="170"/>
      <el-table-column label="${moduleFunctionName}操作" align="center" width="200" fixed="right"
                     v-if="checkPermissions(['${authPrefix}:edit','${authPrefix}:delete'])">
        <template #default="scope">
          <div class="display">
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
              <el-divider direction="vertical"/>
            </div>
            <!--selectable more actions-->
            <el-dropdown class="global-table-dropdown" size="small"
                       v-if="checkPermission('${authPrefix}:query')">
              <span class="display">
              <el-icon><DArrowRight/></el-icon>
              更多
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :icon="Plus">更多功能</el-dropdown-item>
                </el-dropdown-menu>
              </template>
          </el-dropdown>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <!--pagination-->
    <el-pagination
            class="global-pagination"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :small="pageLayoutSize"
            :layout="pageLayout"
            :page-sizes="pageSizes"
            :current-page="pageCurrent"
            :page-size="pageSize"
            :total="pageTotal"/>
  </el-card>

  <el-dialog class="global-dialog-iu"
           title="${moduleFunctionName}管理" v-model="dialogVisible"
           :close-on-click-modal="false"
           @close="handleCloseForm">
    <el-form :model="form"
             :rules="formRules"
             ref="formRuleRef">
      <el-row>
      <#list columns as col>
        <#if col.javaField !="updateBy" && col.javaField !="createBy" && col.javaField != "createTime" && col.javaField != "updateTime">
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
        </#if>
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
  <#if genImportInterface == "1">

  <el-dialog class="global-dialog-iu"
           title="批量导入" v-model="importVisible"
           :close-on-click-modal="false"
           @close="handleCloseForm">
    <upload :url="importUrl"
            :show-list="false"
            :size=10
            type="xls/xlsx">
      <p class="temp-text">下载批量导入模板：
        <a class="hover"
           @click="handleDownloadTemplate"
        >${moduleFunctionName}导入模板.xlsx</a>
      </p>
    </upload>
  </el-dialog>
  </#if>
</template>

<script setup lang="ts">
  import {computed, onMounted, ref} from "vue";
  import {useLayoutStore} from "@/store/modules/layout.ts";
  import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
  import {paramBuilder} from "@/utils/common.ts";
  import {delete$

  {
    className
  }
  </#if>
  <#if genExportInterface == "1" >,
  export${
    className
  }
          </#if>
          <#if genImportInterface == "1" >, import${
    className
  }
          ,
          insert$
  {
    className
  }
  </#if>
  <#if genEditInterface == "1" >,
  update${
    className
  }
  </#if>
  <#if genDeleteInterface == "1" >
  }
  from
  "@/api/";
  import {download} from "@/utils/download.ts";

  //store
const layoutStore = useLayoutStore();
//layout
const size = layoutStore.tableSize;
const pageLayoutSize = computed(() => {
    return size === 'small';
});
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
<#assign timeAssign = false>
<#list columns as col>
<#if col.formType == "datetime" && !timeAssign>
const time = ref();
<#assign timeAssign = true>
</#if>
</#list>

/**
 * 查询数据
 */
const getData = () => {
    loading.value = true;
    <#assign dataAssign = false>
    <#list columns as col>
    <#if col.formType == "datetime" && !dataAssign>
    <#assign getDataAssign = true>
    </#if>
    </#list>
    <#if dataAssign = true>
    get${className}List(paramBuilder(query.value, queryPage.value, time.value, null)).then(res => {
    <#else >
    get${className}List(paramBuilder(query.value, queryPage.value, null, null)).then(res => {
    </#if>
        const response = res.data;
        tableData.value = response.data;
        pageCurrent.value = response.current;
        pageTotal.value = response.total;
        pageSize.value = response.size;
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
 * 分页参数配置
 */
const queryPage = ref({
    current: 1,
    size: 10,
    total: 0
});
const pageLayout = ref("total, sizes, prev, pager, next");
const pageSizes = ref([10, 20, 50, 100]);
const pageTotal = ref(0);
const pageCurrent = ref(1);
const pageSize = ref(pageSizes.value[0] | 10);
const handleSizeChange = (page: any) => {
    pageSize.value = page
    queryPage.value.size = page
    getData();
};
const handleCurrentChange = (page: any) => {
    pageCurrent.value = page
    queryPage.value.current = page
    getData();
};

/**
 * 多选配置
 */
//是否可选
const selectable = ref(true);
//多选时只允许单选
const selectSingle = ref(false);
//多选数据
const multiSelectData = ref();
const selectionChange = (value: any) => {
    multiSelectData.value = value
    selectable.value = multiSelectData.value.length <= 0;
    selectSingle.value = multiSelectData.value.length == 1;
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
    <#if col.javaField !="updateBy" && col.javaField !="createBy" && col.javaField != "createTime" && col.javaField != "updateTime">
    ${col.javaField}: <#if col.javaType == "Long" || col.javaType == "Integer" || col.javaType == "Double">0<#else>''</#if>,
    </#if>
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
    <#if genImportInterface == "1">
    importVisible.value = false;
    </#if>
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
        <#if col.javaField !="updateBy" && col.javaField !="createBy" && col.javaField != "createTime" && col.javaField != "updateTime">
        ${col.javaField}: <#if col.javaType == "Long" || col.javaType == "Integer" || col.javaType == "Double">0<#else>''</#if>,
        </#if>
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
    let id;
    if (row == null) {
        id = multiSelectData.value[0].<#list columns as col><#if col.isPrimaryKey == "1">${col.javaField}</#if></#list>;
    } else {
        id = row.<#list columns as col><#if col.isPrimaryKey == "1">${col.javaField}</#if></#list>;
    }
    get${className}Singleton(id).then(res => {
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
    let ids: any = [];
    let names: any = [];
    if (row == null) {
        multiSelectData.value.forEach(i => {
            ids.push(i.<#list columns as col><#if col.isPrimaryKey == "1">${col.javaField}</#if></#list>);
            names.push(i.<#list columns as col><#if col.isPrimaryKey == "1">${col.javaField}</#if></#list>);
        })
    } else {
        ids.push(row.<#list columns as col><#if col.isPrimaryKey == "1">${col.javaField}</#if></#list>);
        names.push(row.<#list columns as col><#if col.isPrimaryKey == "1">${col.javaField}</#if></#list>);
    }
    ElMessageBox.confirm(
        '将删除ID为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
        '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
    ).then(() => {
        delete${className}(ids).then((res) => {
            if (res.code === 0) {
                getData();
            }
        });
    }).catch();
};
<#if genImportInterface == "1">

/**
 * 导入数据
 */
const importVisible = ref(false);
const importUrl = ref(import.meta.env.VITE_BASE_URL + import${className}());
const handleImport = () => {
    importVisible.value = true;
}
const handleDownloadTemplate = () => {
    getExport${className}Template().then(res => {
        download(res);
    });
};
</#if>
<#if genExportInterface == "1">

/**
 * 导出数据
 */
const handleExport = () => {
    export${className}(null).then(res => {
        download(res);
    });
}
</#if>
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