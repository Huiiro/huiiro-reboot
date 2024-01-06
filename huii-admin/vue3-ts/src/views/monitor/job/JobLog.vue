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
    <el-form-item label="任务状态" class="global-input-item">
      <el-select v-model="query.jobStatus" placeholder="请选择任务状态"
                 :size="size">
        <el-option
            v-for="item in jobLogStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"/>
      </el-select>
    </el-form-item>
    <el-form-item label="执行时间" class="global-input-item">
      <el-date-picker
          type="datetimerange"
          v-model="time"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"/>
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
    <!--delete-->
    <el-form-item class="global-form-item-margin">
      <el-button :size="size" :icon="ArrowLeft" @click="handleBack"
                 :color="layoutStore.BtnBack" plain>返回
      </el-button>
    </el-form-item>
    <el-form-item class="global-form-item-margin" v-if="checkPermission('system:job:log:delete')">
      <el-button :size="size" :icon="Delete" @click="handleDelete"
                 :color="layoutStore.BtnDelete" plain :disabled="selectable">删除日志
      </el-button>
    </el-form-item>
    <el-form-item class="global-form-item-margin" v-if="checkPermission('system:job:log:delete')">
      <el-button :size="size" :icon="Delete" @click="handleDeleteAll"
                 :color="layoutStore.BtnDelete" plain>删除全部日志
      </el-button>
    </el-form-item>
    <!--export-->
    <el-form-item class="global-form-item-margin" v-if="checkPermission('system:job:log:export')">
      <el-button :size="size" :icon="Upload" @click="handleExport"
                 :color="layoutStore.BtnExport" plain>导出日志
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
    <el-table-column prop="logId" label="日志ID" align="center" min-width="120"/>
    <el-table-column prop="jobName" label="任务名称" align="center" min-width="100"/>
    <el-table-column prop="groupName" label="分组名称" align="center" min-width="100"/>
    <el-table-column prop="target" label="调用目标" align="center" min-width="180"/>
    <el-table-column prop="jobMessage" label="任务信息" align="center" min-width="100"/>
    <el-table-column prop="errorInfo" label="错误信息" align="center" min-width="100"/>
    <el-table-column prop="cost" label="任务耗时" align="center" min-width="100">
      <template #default="scope">
        {{ scope.row.cost }}ms
      </template>
    </el-table-column>
    <el-table-column v-if="showTimeColumn" prop="beginTime" label="执行时间" align="center" sortable width="170"/>
    <el-table-column v-if="showTimeColumn" prop="endTime" label="结束时间" align="center" sortable width="170"/>
    <el-table-column prop="jobStatus" label="任务状态" align="center" min-width="100">
      <template #default="scope">
        <el-tag v-for="tag in jobLogStatusOptions"
                v-show="tag.value === scope.row.jobStatus"
                :size="size"
                :key="tag.value"
                :type="tag.type"> {{ tag.label }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="日志操作" align="center" width="100" fixed="right"
                     v-if="checkPermissions(['system:job:log:delete'])">
      <template #default="scope">
        <div class="display">
          <div class="display" v-if="checkPermission('system:job:log:delete')">
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
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {ElMessage, ElMessageBox} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {deleteSysJobLog, deleteSysJobLogAll, exportSysJobLog, getSysJobLogList} from "../../../api/monitor/jogLog";
import {ArrowLeft, Delete, Refresh, Search, Timer, Upload} from "@element-plus/icons-vue";
import {checkPermission, checkPermissions} from "@/utils/permission.ts";
import {downloadExport} from "@/utils/download.ts";
import router from "@/router";
import {jobGroupOptions, jobLogStatusOptions} from "@/views/monitor/job/dictionary.ts";
import {useRoute} from "vue-router";

//store
const layoutStore = useLayoutStore();
//layout
const size = layoutStore.tableSize;
const pageLayoutSize = computed(() => {
  return size === 'small';
});
//mounted
const route = useRoute();
onMounted(() => {
  if (route.params.name !== 'log') {
    //携带参数
    query.value.jobName = <string>route.params.name;
  }
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
});
const time = ref();

/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  getSysJobLogList(paramBuilder(query.value, queryPage.value, time.value, null)).then(res => {
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
  time.value = '';
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
 * 删除
 */
//@ts-ignore
const handleDelete = (index, row) => {
  let ids: any = [];
  let names: any = [];
  if (row == null) {
    multiSelectData.value.forEach(i => {
      ids.push(i.logId);
      names.push(i.logId);
    })
  } else {
    ids.push(row.logId);
    names.push(row.logId);
  }
  ElMessageBox.confirm(
      '将删除ID为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteSysJobLog(ids).then((res) => {
      if (res.code === 0) {
        getData();
      }
    });
  }).catch();
};

const handleDeleteAll = () => {
  ElMessageBox.confirm(
      '将删除全部数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteSysJobLogAll().then(res => {
      if (res.code === 0) {
        getData();
      }
    });
  }).catch();
}

const handleExport = () => {
  exportSysJobLog(null).then(res => {
    downloadExport(res);
  });
}

const handleBack = () => {
  router.push({name: '任务监控'})
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