<template>
  <!--formSearch-->
  <el-form :inline="true" :size="size" v-show="showSearch">
    <!--searchParam-->
    <el-form-item label="用户名称" class="global-input-item">
      <el-input v-model="query.loginUserName" placeholder="请输入用户名称"
                class="global-input" :size="size"/>
    </el-form-item>
    <el-form-item label="请求IP" class="global-input-item">
      <el-input v-model="query.loginIp" placeholder="请输入登录IP"
                class="global-input" :size="size"/>
    </el-form-item>
    <el-form-item label="登录方式" class="global-input-item">
      <el-select v-model="query.loginType" placeholder="请选择登录方式"
                 :size="size">
        <el-option
            v-for="item in logLoginTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"/>
      </el-select>
    </el-form-item>
    <el-form-item label="登录结果" class="global-input-item">
      <el-select v-model="query.loginStatus" placeholder="请选择登录结果"
                 :size="size">
        <el-option
            v-for="item in logLoginStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"/>
      </el-select>
    </el-form-item>
    <el-form-item label="登录时间" class="global-input-item">
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
    <el-form-item class="global-form-item-margin" v-if="checkPermission('system:logLogin:delete:all')">
      <el-button :size="size" :icon="Delete" @click="handleDeleteAll"
                 :color="layoutStore.BtnDelete" plain>删除全部
      </el-button>
    </el-form-item>
    <el-form-item class="global-form-item-margin" v-if="checkPermission('system:logLogin:delete')">
      <el-button :size="size" :icon="Delete" @click="handleDelete"
                 :color="layoutStore.BtnDelete" plain :disabled="selectable">删除日志
      </el-button>
    </el-form-item>
    <!--export-->
    <el-form-item class="global-form-item-margin" v-if="checkPermission('system:logLogin:export')">
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
    <el-table-column prop="loginId" label="日志ID" align="center" min-width="120"/>
    <el-table-column prop="loginUserName" label="用户名称" align="center" min-width="150"/>
    <el-table-column prop="loginIp" label="Ip地址" align="center" min-width="150"/>
    <el-table-column prop="loginAddress" label="真实地址" align="center" min-width="120"/>
    <el-table-column prop="loginTime" label="登录时间" align="center" sortable min-width="170"/>
    <el-table-column prop="loginBrowser" label="登录浏览器" align="center" min-width="150"/>
    <el-table-column prop="loginOs" label="登录系统" align="center" min-width="150"/>
    <el-table-column prop="loginType" label="登录类型" align="center" width="120">
      <template #default="scope">
        <el-tag v-for="tag in logLoginTypeOptions"
                v-show="tag.value === scope.row.loginType"
                :size="size"
                :key="tag.value"
                :type="tag.type"> {{ tag.label }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="loginStatus" label="登录结果" align="center" width="120">
      <template #default="scope">
        <el-tag v-for="tag in logLoginStatusOptions"
                v-show="tag.value === scope.row.loginStatus"
                :size="size"
                :key="tag.value"
                :type="tag.type"> {{ tag.label }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="loginMessage" label="错误信息" align="center" width="180">
      <template #default="scope">
        <p style="overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;"
        >{{ scope.row.loginMessage }}</p>
      </template>
    </el-table-column>
    <el-table-column label="日志操作" align="center" width="200" fixed="right" v-if="false">
      <template #default="scope">
        <div class="display" v-if="checkPermission('system:logLogin:delete')">
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

  <el-dialog class="global-dialog-iu"
             title="日志详情" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form :model="form"
             :label-position="'top'"
             :rules="formRules"
             ref="formRuleRef">
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
import {Delete, Refresh, Search, Timer, Upload,} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {downloadExport} from "@/utils/download.ts";
import {logLoginStatusOptions, logLoginTypeOptions} from "@/views/monitor/logLogin/dictionary.ts";
import {deleteLogLogin, deleteLogLoginAll, exportLogLogin, getLogLoginList} from "@/api/monitor/logLogin";
import {checkPermission} from "@/utils/permission.ts";

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
  loginUserName: '',
  loginIp: '',
  loginStatus: '',
  loginType: null,
});
const time = ref();
/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  getLogLoginList(paramBuilder(query.value, queryPage.value, time.value, null)).then(res => {
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
  query.value.loginUserName = '';
  query.value.loginIp = '';
  query.value.loginStatus = '';
  query.value.loginType = null;
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
  loginId: 0,
  loginUserName: '',
  loginIp: '',
  loginAddress: '',
  loginTime: '',
  loginBrowser: '',
  loginOs: '',
  loginType: 0,
  loginStatus: '',
  loginMessage: ''
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
      ids.push(i.loginId);
      names.push(i.loginId);
    })
  } else {
    ids.push(row.loginId);
    names.push(row.loginId);
  }
  ElMessageBox.confirm(
      '将删除登录ID为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteLogLogin(ids).then((res) => {
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
    deleteLogLoginAll().then((res) => {
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
  exportLogLogin(null).then(res => {
    downloadExport(res)
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