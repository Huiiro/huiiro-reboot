<template>
  <el-card>
    <!--formSearch-->
    <el-form :inline="true" :size="size" v-show="showSearch">
      <!--searchParam-->
      <el-form-item label="通知标题" class="global-input-item">
        <el-input v-model="query.noticeTitle" placeholder="请输入通知标题"
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
      <el-form-item class="global-form-item-margin" v-if="checkPermission('system:notice:add')">
        <el-button :size="size" :icon="Plus" @click="handleInsert"
                   :color="layoutStore.BtnInsert" plain>添加通知
        </el-button>
      </el-form-item>
      <!--edit-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('system:notice:edit')">
        <el-button :size="size" :icon="Edit" @click="handleEdit"
                   :color="layoutStore.BtnUpdate" plain :disabled="!selectSingle">修改通知
        </el-button>
      </el-form-item>
      <!--delete-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('system:notice:delete')">
        <el-button :size="size" :icon="Delete" @click="handleDelete"
                   :color="layoutStore.BtnDelete" plain :disabled="selectable">删除通知
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
      <el-table-column prop="noticeId" label="通知ID" align="center" min-width="120"/>
      <el-table-column prop="noticeTitle" label="通知标题" align="center" min-width="180"/>
      <el-table-column prop="noticeContent" label="通知内容" align="center" min-width="180">
        <template #default="scope">
          <p style="overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;"
          >{{ scope.row.noticeContent }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="通知备注" align="center" min-width="150" />
      <el-table-column prop="noticeType" label="通知类型" align="center" width="120">
        <template #default="scope">
          <el-tag v-for="tag in noticeTypeOptions"
                  v-show="tag.value === scope.row.noticeType"
                  :size="size"
                  :key="tag.value"
                  :type="tag.type"> {{ tag.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="noticeStatus" label="通知状态" align="center" width="120">
        <template #default="scope">
          <el-tag v-for="tag in noticeStatusOptions"
                  v-show="tag.value === scope.row.noticeStatus"
                  :size="size"
                  :key="tag.value"
                  :type="tag.type"> {{ tag.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="showTimeColumn" prop="createTime" label="创建日期" align="center" sortable width="170"/>
      <el-table-column v-if="showTimeColumn" prop="updateTime" label="更新日期" align="center" sortable width="170"/>
      <el-table-column label="通知操作" align="center" width="200" fixed="right"
                       v-if="checkPermissions(['system:notice:edit','system:notice:delete'])">
        <template #default="scope">
          <div class="display">
            <div class="display" v-if="checkPermission('system:notice:edit')">
              <el-button class="global-table-btn"
                         size="small" type="primary" link :icon="Edit"
                         @click="handleEdit(scope.$index, scope.row)">
                编辑
              </el-button>
              <el-divider direction="vertical"/>
            </div>
            <div class="display" v-if="checkPermission('system:notice:delete')">
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
             title="通知管理" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form :model="form"
             :rules="formRules"
             ref="formRuleRef">
      <el-form-item label="通知标题" label-width="85" prop="noticeTitle">
        <el-input v-model="form.noticeTitle" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="通知内容" label-width="85" prop="noticeContent">
        <el-input v-model="form.noticeContent"
                  autocomplete="off"
                  type="textarea"
                  show-word-limit
                  maxlength="999"
                  :rows="8"
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
      <el-row>
        <el-col :span="11">
          <el-form-item label="通知类型" label-width="85" prop="postIds">
            <el-select
                v-model="form.noticeType"
                placeholder="请选择通知类型"
                style="width: 240px"
            >
              <el-option
                  v-for="item in noticeTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="通知状态" label-width="85" prop="noticeStatus">
            <el-radio-group v-model="form.noticeStatus">
              <el-radio v-for="option in noticeStatusOptions" :key="option.value" :label="option.value">
                {{ option.label }}
              </el-radio>
            </el-radio-group>
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
import {computed, onMounted, ref} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {Delete, Edit, Plus, Refresh, Search, Timer,} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {noticeStatusOptions, noticeTypeOptions} from "@/views/system/notice/dictionary.ts";
import {deleteNotice, getNoticeList, getNoticeSingleton, insertNotice, updateNotice} from "@/api/system/notice";
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
  noticeTitle: '',
  noticeStatus: '',
  noticeType: null
});

/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  getNoticeList(paramBuilder(query.value, queryPage.value, null, null)).then(res => {
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
  query.value.noticeTitle = '';
  query.value.noticeStatus = '';
  query.value.noticeType = null;
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
  noticeId: 0,
  noticeTitle: '',
  noticeContent: '',
  noticeType: 1,
  noticeStatus: '',
  remark: '',
  createBy: '',
  createTime: '',
  updateBy: '',
  updateTime: ''
});
//表单数据校验规则
const formRules = ref({
  noticeTitle: [
    {required: true, message: '请输入通知标题', trigger: 'blur'},
    {min: 1, max: 60, message: '通知标题在 1 至 60 个字符之间', trigger: 'blur'}
  ],
  noticeContent: [
    {required: true, message: '请输入通知内容', trigger: 'blur'},
    {min: 1, max: 999, message: '通知内容在 1 至 999 个字符之间', trigger: 'blur'}
  ],
  noticeType: [
    {required: true, message: '请选择通知类型', trigger: 'blur'},
  ],
  noticeStatus: [
    {required: true, message: '请选择通知状态', trigger: 'blur'},
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
  updateNotice(form.value).then((res) => {
    if (res.code === 0) {
      isEdit.value = false;
      dialogVisible.value = false;
      getData();
    }
  });
};
const doInsert = () => {
  insertNotice(form.value).then((res) => {
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
    noticeId: 0,
    noticeContent: "",
    noticeStatus: "",
    noticeTitle: "",
    noticeType: 1,
    remark: "",
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
    id = multiSelectData.value[0].noticeId;
  } else {
    id = row.noticeId;
  }
  getNoticeSingleton(id).then(res => {
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
      ids.push(i.noticeId);
      names.push(i.noticeTitle);
    })
  } else {
    ids.push(row.noticeId);
    names.push(row.noticeTitle);
  }
  ElMessageBox.confirm(
      '将删除通知标题为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteNotice(ids).then((res) => {
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