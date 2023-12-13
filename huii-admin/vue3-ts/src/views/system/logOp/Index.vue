<template>
  <el-card>
    <!--formSearch-->
    <el-form :inline="true" :size="size" v-show="showSearch">
      <!--searchParam-->
      <el-form-item label="用户名称" class="global-input-item">
        <el-input v-model="query.opUserName" placeholder="请输入用户名称"
                  class="global-input" :size="size"/>
      </el-form-item>
      <el-form-item label="接口耗时" class="global-input-item">
        <el-input v-model="costTime" placeholder="请输入接口耗时"
                  class="global-input" :size="size"/>
      </el-form-item>
      <el-form-item label="请求IP" class="global-input-item">
        <el-input v-model="query.opIp" placeholder="请输入请求IP"
                  class="global-input" :size="size"/>
      </el-form-item>
      <el-form-item label="请求方式" class="global-input-item">
        <el-select v-model="query.opType" placeholder="请选择请求方式"
                   :size="size">
          <el-option
              v-for="item in logOpTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="请求结果" class="global-input-item">
        <el-select v-model="query.opStatus" placeholder="请选择请求结果"
                   :size="size">
          <el-option
              v-for="item in logOpStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="是否标记" class="global-input-item">
        <el-select v-model="query.opMarkFlag" placeholder="请选择是否标记"
                   :size="size">
          <el-option
              v-for="item in logOpMarkOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="请求时间" class="global-input-item">
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
        <el-button :size="size" :icon="Delete" @click="handleDeleteAll"
                   :color="layoutStore.BtnDelete" plain>删除全部
        </el-button>
      </el-form-item>
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Delete" @click="handleDelete"
                   :color="layoutStore.BtnDelete" plain :disabled="selectable">删除日志
        </el-button>
      </el-form-item>
      <!--export-->
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Upload" @click="handleExport"
                   :color="layoutStore.BtnExport" plain>导出日志
        </el-button>
      </el-form-item>
      <!--right fixed-->
      <el-form-item class="global-form-item-right">
        <!--显示/隐藏时间列-->
        <el-button :size="size" :icon="Timer" circle @click="handleExpandTime" v-if="false"/>
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
      <el-table-column prop="opId" label="日志ID" align="left" min-width="150"/>
      <el-table-column prop="opUserName" label="用户名称" align="center" width="150"/>
      <el-table-column prop="opMethodName" label="方法名称" align="center" width="150"/>
      <el-table-column prop="opTime" label="操作时间" align="center" sortable width="150"/>
      <el-table-column prop="opCostTime" label="操作花费时间" align="center" sortable width="120">
        <template #default="scope">
          <p> {{ scope.row.opCostTime }}ms</p>
        </template>
      </el-table-column>
      <el-table-column prop="opIp" label="Ip地址" align="center" width="120"/>
      <el-table-column prop="opAddress" label="真实地址" align="center" width="120"/>
      <el-table-column prop="opRequest" label="请求方式" align="center" width="120"/>
      <el-table-column prop="opStatus" label="请求结果" align="center" width="120">
        <template #default="scope">
          <el-tag v-for="tag in logOpStatusOptions"
                  v-show="tag.value === scope.row.opStatus"
                  :size="size"
                  :key="tag.value"
                  :type="tag.type"> {{ tag.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="opType" label="请求类型" align="center" width="120">
        <template #default="scope">
          <el-tag v-for="tag in logOpTypeOptions"
                  v-show="tag.value === scope.row.opType"
                  :size="size"
                  :key="tag.value"
                  :type="tag.type"> {{ tag.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="opMarkFlag" label="标记" align="center" width="120">
        <template #default="scope">
          <el-tag v-for="tag in logOpMarkOptions"
                  v-show="tag.value === scope.row.opMarkFlag"
                  :size="size"
                  :key="tag.value"
                  :type="tag.type"> {{ tag.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="opMessage" label="错误信息" align="center" width="180">
        <template #default="scope">
          <p style="overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;"
          >{{ scope.row.opMessage }}</p>
        </template>
      </el-table-column>
      <el-table-column label="日志操作" align="center" width="200" fixed="right">
        <template #default="scope">
          <div class="display">
            <el-button class="global-table-btn"
                       size="small" type="primary" link :icon="Edit"
                       @click="handleUpdateStatus(scope.$index, scope.row)">
              标记
            </el-button>
            <el-divider direction="vertical"/>
            <el-button class="global-table-btn"
                       size="small" type="primary" link :icon="Edit"
                       @click="handleEdit(scope.$index, scope.row)">
              详情
            </el-button>
            <el-divider direction="vertical"/>
            <el-button class="global-table-btn red"
                       size="small" type="primary" link :icon="Delete"
                       @click="handleDelete(scope.$index, scope.row)">
              删除
            </el-button>
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
             title="日志详情" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form :model="form"
             :label-position="'top'"
             :rules="formRules"
             ref="formRuleRef">
      <el-form-item label="请求路径" label-width="85" prop="opUrl">
        <p style="font-size: 12px">{{ form.opUrl }}</p>
      </el-form-item>
      <el-form-item label="请求参数" label-width="85" prop="opReqParam">
        <p style="font-size: 12px">{{ form.opReqParam }}</p>
      </el-form-item>
      <el-form-item label="响应参数" label-width="85" prop="opResParam">
        <p style="font-size: 12px">{{ form.opResParam }}</p>
      </el-form-item>
      <el-form-item label="错误信息" label-width="85" prop="opMessage">
        <p style="font-size: 12px">{{ form.opMessage }}</p>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleCloseForm">关 闭</el-button>
      <el-button type="primary" @click="handleSubmitForm(formRuleRef)" v-if="false">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {Delete, Edit, Refresh, Search, Timer, Upload,} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {deleteLogOp, deleteLogOpAll, exportLogOp, getLogOpList, updateLogOpFlagStatus} from "@/api/system/logOp";
import {logOpMarkOptions, logOpStatusOptions, logOpTypeOptions} from "@/views/system/logOp/dictionary.ts";
import {download} from "@/utils/download.ts";

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
  opUserName: '',
  opCostTime: '',
  opIp: '',
  opStatus: '',
  opType: null,
  opMarkFlag: ''
});
const time = ref();
const costTime = ref();
/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  let param = {
    costTime: costTime
  }
  getLogOpList(paramBuilder(query.value, queryPage.value, time.value, param)).then(res => {
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
  query.value.opUserName = '';
  query.value.opCostTime = '';
  query.value.opIp = '';
  query.value.opStatus = '';
  query.value.opType = null;
  query.value.opMarkFlag = '';
  time.value = '';
  costTime.value = '';
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
  opId: 0,
  opUserName: '',
  opMethodMame: '',
  opType: 0,
  opTime: '',
  opCostTime: '',
  opIp: '',
  opAddress: '',
  opRequest: '',
  opUrl: '',
  opReqParam: '',
  opResParam: '',
  opStatus: '',
  opMarkFlag: '',
  opMessage: ''
});
//表单数据校验规则
const formRules = ref({});
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

};
const doInsert = () => {

};
/**
 * 点击添加
 */
//@ts-ignore
const handleInsert = (index, row) => {

};
/**
 * 点击编辑
 */
//@ts-ignore
const handleEdit = (index, row) => {
  form.value = row;
  isEdit.value = true;
  dialogVisible.value = true;
};
/**
 * 编辑 flag
 */
//@ts-ignore
const handleUpdateStatus = (index, row) => {
  let obj = {
    opId: row.opId,
    opMarkFlag: row.opMarkFlag
  }
  updateLogOpFlagStatus(obj).then(res => {
    if (res.code === 0) {
      getData();
    }
  })
}
/**
 * 删除
 */
//@ts-ignore
const handleDelete = (index, row) => {
  let ids: any = [];
  let names: any = [];
  if (row == null) {
    multiSelectData.value.forEach(i => {
      ids.push(i.opId);
      names.push(i.opId);
    })
  } else {
    ids.push(row.opId);
    names.push(row.opId);
  }
  ElMessageBox.confirm(
      '将删除操作ID为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteLogOp(ids).then((res) => {
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
    deleteLogOpAll(ids).then((res) => {
      if (res.code === 0) {
        getData();
      }
    });
  }).catch();
}
/**
 * 导出数据
 */
const handleExport = () => {
  exportLogOp({}).then(res => {
    download(res)
  })
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