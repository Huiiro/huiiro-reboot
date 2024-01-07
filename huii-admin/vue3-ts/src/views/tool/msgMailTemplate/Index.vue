<template>
  <!--formSearch-->
  <el-form :inline="true" :size="size" v-show="showSearch">
    <!--searchParam-->
    <el-form-item label="模板名称" class="global-input-item">
      <el-input v-model="query.tempName" placeholder="请输入模板名称"
                class="global-input" :size="size"/>
    </el-form-item>
    <el-form-item label="邮件类型" class="global-input-item">
      <el-select v-model="query.mailType" placeholder="请选择邮件类型"
                 :size="size">
        <el-option
            v-for="item in mailTypeStatus"
            :key="item.value"
            :label="item.label"
            :value="item.value"/>
      </el-select>
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
    <el-form-item class="global-form-item-margin" v-if="checkPermission('tool:msg:mail:add')">
      <el-button :size="size" :icon="Plus" @click="handleInsert"
                 :color="layoutStore.BtnInsert" plain>添加模板
      </el-button>
    </el-form-item>
    <!--edit-->
    <el-form-item class="global-form-item-margin" v-if="checkPermission('tool:msg:mail:edit')">
      <el-button :size="size" :icon="Edit" @click="handleEdit"
                 :color="layoutStore.BtnUpdate" plain :disabled="!selectSingle">修改模板
      </el-button>
    </el-form-item>
    <!--delete-->
    <el-form-item class="global-form-item-margin" v-if="checkPermission('tool:msg:mail:delete')">
      <el-button :size="size" :icon="Delete" @click="handleDelete"
                 :color="layoutStore.BtnDelete" plain :disabled="selectable">删除模板
      </el-button>
    </el-form-item>
    <!--import-->
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
    <el-table-column prop="mailTempId" label="ID" align="left" min-width="120"/>
    <el-table-column prop="tempName" label="模板名称" align="left" min-width="120"/>
    <el-table-column prop="mailType" label="邮件类型" align="left" min-width="120"/>
    <el-table-column prop="mailSubject" label="邮件主题" align="left" min-width="180"/>
    <el-table-column prop="remark" label="模板备注" align="left" min-width="120"/>
    <el-table-column v-if="showTimeColumn" prop="createTime" label="创建日期" align="center" sortable width="170"/>
    <el-table-column v-if="showTimeColumn" prop="updateTime" label="更新日期" align="center" sortable width="170"/>
    <el-table-column label="模板操作" align="center" width="200" fixed="right"
                     v-if="checkPermissions(['tool:msg:mail:edit','tool:msg:mail:delete'])">
      <template #default="scope">
        <div class="display">
          <div class="display" v-if="checkPermission('tool:msg:mail:edit')">
            <el-button class="global-table-btn"
                       size="small" type="primary" link :icon="Edit"
                       @click="handleEdit(scope.$index, scope.row)">
              编辑
            </el-button>
            <el-divider direction="vertical"/>
          </div>
          <div class="display" v-if="checkPermission('tool:msg:mail:delete')">
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

  <el-dialog class="global-dialog-iu"
             title="邮件模板管理" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form :model="form"
             :rules="formRules"
             ref="formRuleRef">
      <el-row>
        <el-col :span="12">
          <el-form-item label="模板名称" label-width="85" prop="tempName">
            <el-input v-model="form.tempName" autocomplete="off"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模板类型" label-width="85" prop="mailType">
            <el-select v-model="form.mailType" placeholder="请选择邮件类型" style="width: 100%">
              <el-option
                  v-for="item in mailTypeStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"/>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="模板主题" label-width="85" prop="mailSubject">
        <el-input v-model="form.mailSubject" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="模板内容" label-width="85" prop="mailContent">
        <v-md-editor v-model="form.mailContent" height="320px"/>
      </el-form-item>
      <el-row v-show="isEdit">
        <el-col :span="12">
          <el-form-item label="创建时间" label-width="85" prop="createTime">
            <el-input v-model="form.createTime" autocomplete="off" readonly="readonly"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="更新时间" label-width="85" prop="updateTime">
            <el-input v-model="form.updateTime" autocomplete="off" readonly="readonly"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="模板备注" label-width="85" prop="remark">
        <el-input v-model="form.remark" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="模板附件" label-width="85" prop="mailAttachFile" v-show="form.mailType == '3'">
        <el-input v-model="form.mailAttachFile" autocomplete="off"/>
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
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {
  deleteMsgMailTemplate,
  getMsgMailTemplateList,
  getMsgMailTemplateSingleton,
  insertMsgMailTemplate,
  updateMsgMailTemplate
} from "@/api/tool/msgMailTemplate";
import {Delete, Edit, Plus, Refresh, Search, Timer} from "@element-plus/icons-vue";
import {checkPermission, checkPermissions} from "@/utils/permission.ts";
import {mailTypeStatus} from "@/views/tool/msgMailTemplate/dictionary.ts";

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
  mailType: '',
  tempName: ''
});
const time = ref();

/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  getMsgMailTemplateList(paramBuilder(query.value, queryPage.value, null, {})).then(res => {
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
  query.value.mailType = '';
  query.value.tempName = '';
  time.value = '';
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
  mailTempId: 0,
  mailType: '',
  mailSubject: '',
  mailContent: '',
  mailAttachFile: '',
  tempName: '',
  remark: '',
  createBy: '',
  createTime: '',
  updateBy: '',
  updateTime: ''
});
//表单数据校验规则
const formRules = ref({
  tempName: [
    {required: true, message: '请输入模板名称', trigger: 'blur'},
  ],
  mailType: [
    {required: true, message: '请选择类型', trigger: 'blur'},
  ],
  mailSubject: [
    {required: true, message: '请输入模板主题', trigger: 'blur'},
  ],
  mailContent: [
    {required: true, message: '请输入邮件内容', trigger: 'blur'},
  ],
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
  updateMsgMailTemplate(form.value).then((res) => {
    if (res.code === 0) {
      isEdit.value = false;
      dialogVisible.value = false;
      getData();
    }
  });
};
const doInsert = () => {
  insertMsgMailTemplate(form.value).then((res) => {
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
    mailTempId: 0,
    mailType: '',
    mailSubject: '',
    mailContent: '',
    mailAttachFile: '',
    tempName: '',
    remark: '',
    createBy: '',
    createTime: '',
    updateBy: '',
    updateTime: ''
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
    id = multiSelectData.value[0].mailTempId;
  } else {
    id = row.mailTempId;
  }
  getMsgMailTemplateSingleton(id).then(res => {
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
      ids.push(i.mailTempId);
      names.push(i.mailTempId);
    })
  } else {
    ids.push(row.mailTempId);
    names.push(row.mailTempId);
  }
  ElMessageBox.confirm(
      '将删除ID为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteMsgMailTemplate(ids).then((res) => {
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