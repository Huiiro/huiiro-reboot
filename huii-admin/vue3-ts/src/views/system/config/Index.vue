<template>
  <el-card>
    <!--formSearch-->
    <el-form :inline="true" :size="size" v-show="showSearch">
      <!--searchParam-->
      <el-form-item label="参数名称" class="global-input-item">
        <el-input v-model="query.configName" placeholder="请输入参数名称"
                  class="global-input" :size="size"/>
      </el-form-item>
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
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Plus" @click="handleInsert"
                   :color="layoutStore.BtnInsert" plain
                   v-if="checkPermission('system:config:add')">添加参数
        </el-button>
      </el-form-item>
      <!--edit-->
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Edit" @click="handleEdit"
                   :color="layoutStore.BtnUpdate" plain :disabled="!selectSingle"
                   v-if="checkPermission('system:config:edit')">修改参数
        </el-button>
      </el-form-item>
      <!--delete-->
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Delete" @click="handleDelete"
                   :color="layoutStore.BtnDelete" plain :disabled="selectable"
                   v-if="checkPermission('system:config:delete')">删除参数
        </el-button>
      </el-form-item>
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
      <el-table-column prop="configName" label="参数名称" align="left" min-width="150"/>
      <el-table-column prop="configKey" label="参数键(key)" align="center" width="120"/>
      <el-table-column prop="configValue" label="参数值(value)" align="center" width="220"/>
      <el-table-column v-if="showTimeColumn" prop="createTime" label="创建日期" align="center" sortable width="150"/>
      <el-table-column v-if="showTimeColumn" prop="updateTime" label="更新日期" align="center" sortable width="150"/>
      <el-table-column label="参数操作" align="center" width="200" fixed="right"
                       v-if="checkPermissions(['system:config:edit','system:config:delete'])">
        <template #default="scope">
          <div class="display">
            <div v-if="checkPermission('system:config:edit')" class="display">
              <el-button class="global-table-btn"
                         size="small" type="primary" link :icon="Edit"
                         @click="handleEdit(scope.$index, scope.row)">
                编辑
              </el-button>
              <el-divider direction="vertical"/>
            </div>
            <div v-if="checkPermission('system:config:delete')" class="display">
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
             title="参数管理" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form :model="form"
             :rules="formRules"
             ref="formRuleRef">
      <el-form-item label="参数名称" label-width="85" prop="configName">
        <el-input v-model="form.configName" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="参数键名" label-width="85" prop="configKey">
        <el-input v-model="form.configKey" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="参数值" label-width="85" prop="configValue">
        <el-input v-model="form.configValue"
                  autocomplete="off"
                  type="textarea"
                  show-word-limit
                  maxlength="100"
                  :rows="1"
        />
      </el-form-item>
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
      <el-form-item label="参数备注" label-width="85" prop="remark">
        <el-input v-model="form.remark"
                  autocomplete="off"
                  type="textarea"
                  show-word-limit
                  maxlength="100"
                  :rows="1"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleCloseForm">取 消</el-button>
      <el-button type="primary" @click="handleSubmitForm(formRuleRef)">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {Delete, Edit, Plus, Refresh, Search, Timer} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {deleteConfig, getConfigList, getConfigSingleton, insertConfig, updateConfig} from "@/api/system/config";
import {checkPermission, checkPermissions} from "@/utils/permission.ts";

//store
const layoutStore = useLayoutStore();
//布局
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
  configName: '',
});

/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  getConfigList(paramBuilder(query.value, queryPage.value, null, null)).then(res => {
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
  query.value.configName = '';
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
  //TODO
  configId: 0,
  configName: '',
  configKey: '',
  configValue: '',
  configRemark: '',
  createBy: '',
  createTime: '',
  updateBy: '',
  updateTime: ''
});
//表单数据校验规则
const formRules = ref({
  configName: [
    {required: true, message: '请输入参数名称', trigger: 'blur'},
    {min: 1, max: 20, message: '参数名称在 1 至 20 个字符之间', trigger: 'blur'}
  ],
  configKey: [
    {required: true, message: '请输入参数键名', trigger: 'blur'},
    {min: 1, max: 20, message: '参数键名在 1 至 20 个字符之间', trigger: 'blur'}
  ],
  configValue: [
    {required: true, message: '请输入参数值', trigger: 'blur'},
    {min: 1, max: 20, message: '参数值在 1 至 20 个字符之间', trigger: 'blur'}
  ]
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
  updateConfig(form.value).then((res) => {
    if (res.code === 0) {
      isEdit.value = false;
      dialogVisible.value = false;
      getData();
    }
  });
};
const doInsert = () => {
  insertConfig(form.value).then((res) => {
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
    configId: 0,
    configKey: "",
    configName: "",
    configRemark: "",
    configValue: "",
    createBy: "",
    createTime: "",
    updateBy: "",
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
    id = multiSelectData.value[0].configId;
  } else {
    id = row.configId;
  }
  getConfigSingleton(id).then(res => {
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
      ids.push(i.configId);
      names.push(i.configName);
    })
  } else {
    ids.push(row.configId);
    names.push(row.configName);
  }
  ElMessageBox.confirm(
      '将删除参数名称为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteConfig(ids).then((res) => {
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