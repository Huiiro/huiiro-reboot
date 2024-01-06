<template>
  <!--formSearch-->
  <el-form :inline="true" :size="size" v-show="showSearch">
    <!--searchParam-->
    <el-form-item label="任务名称" class="global-input-item">
      <el-input v-model="query.jobName" placeholder="请输入任务名称"
                class="global-input" :size="size"/>
    </el-form-item>
    <el-form-item label="任务分组" class="global-input-item">
      <el-select v-model="query.groupName" placeholder="请选择任务分组"
                 :size="size">
        <el-option
            v-for="item in jobGroupOptions"
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
    <el-form-item class="global-form-item-margin" v-if="checkPermission('system:job:add')">
      <el-button :size="size" :icon="Plus" @click="handleInsert"
                 :color="layoutStore.BtnInsert" plain>添加任务
      </el-button>
    </el-form-item>
    <!--edit-->
    <el-form-item class="global-form-item-margin" v-if="checkPermission('system:job:edit')">
      <el-button :size="size" :icon="Edit" @click="handleEdit"
                 :color="layoutStore.BtnUpdate" plain :disabled="!selectSingle">修改任务
      </el-button>
    </el-form-item>
    <!--delete-->
    <el-form-item class="global-form-item-margin" v-if="checkPermission('system:job:delete')">
      <el-button :size="size" :icon="Delete" @click="handleDelete"
                 :color="layoutStore.BtnDelete" plain :disabled="selectable">删除任务
      </el-button>
    </el-form-item>
    <!--log-->
    <el-form-item class="global-form-item-margin" v-if="checkPermission('system:job:log')">
      <el-button :size="size" :icon="DocumentRemove" @click="handleLog(null)"
                 :color="layoutStore.BtnUpload" plain>管理日志
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
    <el-table-column prop="jobName" label="任务名称" align="center" min-width="120"/>
    <el-table-column prop="groupName" label="分组名称" align="center" min-width="120"/>
    <el-table-column prop="cron" label="cron表达式" align="center" min-width="120"/>
    <el-table-column prop="target" label="调用目标" align="center" min-width="120"/>
    <el-table-column prop="jobStatus" label="任务状态" align="center" min-width="120">
      <template #default="scope">
        <el-switch v-model="scope.row.jobStatus"
                   active-value="1" inactive-value="0"
                   @click="handleUpdateJobStatus(scope.$index, scope.row)"
        />
      </template>
    </el-table-column>
    <el-table-column v-if="showTimeColumn" prop="createTime" label="创建日期" align="center" sortable width="170"/>
    <el-table-column v-if="showTimeColumn" prop="updateTime" label="更新日期" align="center" sortable width="170"/>
    <el-table-column label="任务操作" align="center" width="200" fixed="right"
                     v-if="checkPermissions(['system:job:edit','system:job:delete'])">
      <template #default="scope">
        <div class="display">
          <div class="display" v-if="checkPermission('system:job:edit')">
            <el-button class="global-table-btn"
                       size="small" type="primary" link :icon="Edit"
                       @click="handleEdit(scope.$index, scope.row)">
              编辑
            </el-button>
            <el-divider direction="vertical"/>
          </div>
          <div class="display" v-if="checkPermission('system:job:delete')">
            <el-button class="global-table-btn red"
                       size="small" type="primary" link :icon="Delete"
                       @click="handleDelete(scope.$index, scope.row)">
              删除
            </el-button>
            <el-divider direction="vertical"/>
          </div>
          <!--selectable more actions-->
          <el-dropdown class="global-table-dropdown" size="small"
                       v-if="checkPermission('system:job:query')">
              <span class="display">
              <el-icon><DArrowRight/></el-icon>
              更多
              </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item :icon="CaretRight" @click="handleRunOnce(scope.row)">立即执行</el-dropdown-item>
                <el-dropdown-item :icon="DocumentRemove" @click="handleLog(scope.row)">查看日志</el-dropdown-item>
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

  <el-dialog class="global-dialog-iu"
             title="任务管理" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form :model="form"
             :rules="formRules"
             ref="formRuleRef">
      <el-row>
        <el-col :span="12">
          <el-form-item label="任务名称" label-width="95" prop="jobName">
            <el-input v-model="form.jobName" autocomplete="off"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务分组" label-width="95" prop="groupName">
            <el-select
                v-model="form.groupName"
                placeholder="请选择任务分组"
                style="width: 100%"
            >
              <el-option
                  v-for="item in jobGroupOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="调用对象" label-width="95" prop="target">
        <el-input v-model="form.target" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="cron表达式" label-width="95" prop="cron">
        <el-input v-model="form.cron" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="任务备注" label-width="95" prop="remark">
        <el-input v-model="form.remark" autocomplete="off"/>
      </el-form-item>
      <el-row v-show="isEdit">
        <el-col :span="11">
          <el-form-item label="创建时间" label-width="95" prop="createTime">
            <el-input v-model="form.createTime" autocomplete="off" readonly="readonly"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="更新时间" label-width="95" prop="updateTime">
            <el-input v-model="form.updateTime" autocomplete="off" readonly="readonly"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="任务状态" label-width="95" prop="jobStatus">
            <el-radio-group v-model="form.jobStatus">
              <el-radio v-for="option in jobStatusOptions" :key="option.value" :label="option.value">
                {{ option.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="并发执行" label-width="95" prop="concurrentStatus">
            <el-radio-group v-model="form.concurrentStatus">
              <el-radio v-for="option in jobConcurrentStatusOptions" :key="option.value" :label="option.value">
                {{ option.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-form-item label="执行策略" label-width="95" prop="misfirePolicy">
          <el-radio-group v-model="form.misfirePolicy">
            <el-radio v-for="option in jobMisfirePolicyOptions" :key="option.value" :label="option.value">
              {{ option.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
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
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {
  deleteSysJob,
  getSysJobList,
  getSysJobSingleton,
  insertSysJob,
  runSysJob,
  updateSysJob,
  updateSysJobStatus
} from "@/api/monitor/job";
import {
  CaretRight,
  DArrowRight,
  Delete,
  DocumentRemove,
  Edit,
  Plus,
  Refresh,
  Search,
  Timer
} from "@element-plus/icons-vue";
import {checkPermission, checkPermissions} from "@/utils/permission.ts";
import {
  jobConcurrentStatusOptions,
  jobGroupOptions,
  jobMisfirePolicyOptions,
  jobStatusOptions
} from "@/views/monitor/job/dictionary.ts";
import router from "@/router";

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
  jobName: '',
  groupName: '',
  jobStatus: '',
  concurrentStatus: '',
});
const time = ref();

/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  getSysJobList(paramBuilder(query.value, queryPage.value, time.value, {})).then(res => {
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
  query.value.jobName = '';
  query.value.groupName = '';
  query.value.jobStatus = '';
  query.value.concurrentStatus = '';
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
  jobId: 0,
  jobName: '',
  groupName: '',
  cron: '',
  target: '',
  jobStatus: '',
  concurrentStatus: '',
  misfirePolicy: '',
  remark: '',
  createBy: '',
  createTime: '',
  updateBy: '',
  updateTime: '',
});
//表单数据校验规则
const formRules = ref({
  jobName: [
    {required: true, message: '请输入任务名称', trigger: 'blur'},
    {min: 1, max: 20, message: '任务名称长度不超过20个字符', trigger: 'blur'}
  ],
  groupName: [
    {required: true, message: '请选择任务分组', trigger: 'blur'},
  ],
  target: [
    {required: true, message: '请输入任务对象', trigger: 'blur'},
  ],
  cron: [
    {required: true, message: '请输入Cron表达式', trigger: 'blur'},
  ],
  jobStatus: [
    {required: true, message: '亲选择是否执行任务', trigger: 'blur'},
  ],
  concurrentStatus: [
    {required: true, message: '请选择是否允许并发执行', trigger: 'blur'},
  ],
  misfirePolicy: [
    {required: true, message: '请选择执行策略', trigger: 'blur'},
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
  updateSysJob(form.value).then((res) => {
    if (res.code === 0) {
      isEdit.value = false;
      dialogVisible.value = false;
      getData();
    }
  });
};
const doInsert = () => {
  insertSysJob(form.value).then((res) => {
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
    jobId: 0,
    jobName: '',
    groupName: '',
    cron: '',
    target: '',
    jobStatus: '',
    concurrentStatus: '',
    misfirePolicy: '',
    remark: '',
    createBy: '',
    createTime: '',
    updateBy: '',
    updateTime: '',
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
    id = multiSelectData.value[0].jobId;
  } else {
    id = row.jobId;
  }
  getSysJobSingleton(id).then(res => {
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
      ids.push(i.jobId);
      names.push(i.jobName);
    })
  } else {
    ids.push(row.jobId);
    names.push(row.jobName);
  }
  ElMessageBox.confirm(
      '将删除ID为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteSysJob(ids).then((res) => {
      if (res.code === 0) {
        getData();
      }
    });
  }).catch();
};

//@ts-ignore
const handleUpdateJobStatus = (index, row) => {
  updateSysJobStatus(row).then(res => {
    if (res.code === 0) {
      getData();
    }
  });
}

const handleRunOnce = (row) => {
  runSysJob(row).then(() => {
  })
}
const handleLog = (row: any) => {
  if (row === null) {
    router.push({name: '任务日志', params: {name: 'log'}});
  } else {
    router.push({name: '任务日志', params: {name: row.jobName}});
  }
}
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