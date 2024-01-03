<template>
  <el-card>
    <!--formSearch-->
    <el-form :inline="true" :size="size" v-show="showSearch">
      <!--searchParam-->
      <el-form-item label="表单名称" class="global-input-item">
        <el-input v-model="query.tableName" placeholder="请输入表单名称"
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
      <!--edit-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('tool:gen:edit')">
        <el-button :size="size" :icon="Edit" @click="handleEdit"
                   :color="layoutStore.BtnUpdate" plain :disabled="!selectSingle">修改表格
        </el-button>
      </el-form-item>
      <!--delete-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('tool:gen:delete')">
        <el-button :size="size" :icon="Delete" @click="handleDelete"
                   :color="layoutStore.BtnDelete" plain :disabled="selectable">删除表格
        </el-button>
      </el-form-item>
      <!--import-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('tool:gen:add')">
        <el-button :size="size" :icon="Download" @click="handleImport"
                   :color="layoutStore.BtnImport" plain>从数据库导入
        </el-button>
      </el-form-item>
      <!--export-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('tool:gen:export')">
        <el-button :size="size" :icon="Upload" @click="handleExport"
                   :color="layoutStore.BtnExport" plain :disabled="selectable">导出代码
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
      <el-table-column prop="tableName" label="表名称" align="left" min-width="150"/>
      <el-table-column prop="tableComment" label="表注释" align="center" min-width="150"/>
      <el-table-column v-if="showTimeColumn" prop="createTime" label="创建日期" align="center" sortable width="170"/>
      <el-table-column v-if="showTimeColumn" prop="updateTime" label="更新日期" align="center" sortable width="170"/>
      <el-table-column label="表操作" align="center" width="200" fixed="right"
                       v-if="checkPermissions(['tool:gen:export','tool:gen:edit','tool:gen:delete','tool:gen:sync'])">
        <template #default="scope">
          <div class="display">
            <div class="display" v-if="checkPermission('tool:gen:edit')">
              <el-button class="global-table-btn"
                         size="small" type="primary" link :icon="Edit"
                         @click="handleEdit(scope.$index, scope.row)">
                编辑
              </el-button>
              <el-divider direction="vertical"/>
            </div>
            <div class="display" v-if="checkPermission('tool:gen:delete')">
              <el-button class="global-table-btn red"
                         size="small" type="primary" link :icon="Delete"
                         @click="handleDelete(scope.$index, scope.row)">
                删除
              </el-button>
              <el-divider direction="vertical"/>
            </div>

            <el-dropdown class="global-table-dropdown" size="small"
                         v-if="checkPermissions(['tool:gen:export','tool:gen:sync'])">
              <span class="display">
                <el-icon><DArrowRight/></el-icon>
                更多
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :icon="Upload" @click="handleExport(scope.$index, scope.row)"
                                    v-if="checkPermission('tool:gen:export')">导出代码
                  </el-dropdown-item>
                  <el-dropdown-item :icon="Refresh" @click="handleSync(scope.$index, scope.row)"
                                    v-if="checkPermission('tool:gen:sync')">同步表结构
                  </el-dropdown-item>
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
             title="选择导入表" v-model="dialogVisible2"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form :inline="true" :size="size" v-show="showSearch">
      <!--searchParam-->
      <el-form-item label="表单名称" class="global-input-item">
        <el-input v-model="query2.tableName" placeholder="请输入表单名称"
                  class="global-input" :size="size"/>
      </el-form-item>
      <el-form-item>
        <el-button :size="size" :icon="Search" type="primary" plain @click="getData2">查询</el-button>
        <el-button :size="size" :icon="Refresh" @click="handleReset2">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="tableData2"
              v-loading="loading2"
              :size="size"
              :highlight-current-row="true"
              header-cell-class-name="global-table-header"
              class="global-table"
              stripe
              @selection-change="selectionChange2">
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="tableName" label="表名称" align="left" min-width="150"/>
      <el-table-column prop="tableComment" label="表注释" align="left" min-width="150"/>
    </el-table>
    <el-pagination
        class="global-pagination"
        @size-change="handleSizeChange2"
        @current-change="handleCurrentChange2"
        :small="pageLayoutSize"
        :layout="pageLayout2"
        :page-sizes="pageSizes2"
        :current-page="pageCurrent2"
        :page-size="pageSize2"
        :total="pageTotal2"/>
    <template #footer>
      <div style="clear: both"/>
      <el-button @click="handleCloseForm">取 消</el-button>
      <el-button type="primary" @click="handleSubmitForm(formRuleRef)">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {computed, onMounted, ref, watch} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {DArrowRight, Delete, Download, Edit, Refresh, Search, Timer, Upload,} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {checkPermission, checkPermissions} from "@/utils/permission.ts";
import {deleteGenTable, genCode, getGenTableDbList, getGenTableList, insertGenTable} from "@/api/tool/generator";
import router from "@/router";
import {downloadExport} from "@/utils/download.ts";

//dialogVisible2
const dialogVisible2 = ref();
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
watch(() => dialogVisible2, () => {
  if (dialogVisible2.value === true) {
    getData2()
  }
}, {deep: true})
/**
 * 查询参数
 */
//加载标志
const loading = ref(false);
const loading2 = ref(false);
//表格数据
const tableData = ref();
//db表格数据
const tableData2 = ref();
//查询参数
const query = ref({
  tableName: '',
});
const query2 = ref({
  tableName: '',
});

/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  getGenTableList(paramBuilder(query.value, queryPage.value, null, null)).then(res => {
    const response = res.data;
    tableData.value = response.data;
    pageCurrent.value = response.current;
    pageTotal.value = response.total;
    pageSize.value = response.size;
    loading.value = false;
  });
};
const getData2 = () => {
  loading2.value = true;
  getGenTableDbList(paramBuilder(query2.value, queryPage2.value, null, null)).then(res => {
    const response = res.data;
    tableData2.value = response.data;
    pageCurrent2.value = response.current;
    pageTotal2.value = response.total;
    pageSize2.value = response.size;
    loading2.value = false;
  });
};

/**
 * 重置查询参数按钮
 */
const handleReset = () => {
  query.value.tableName = '';
  getData();
};

const handleReset2 = () => {
  query2.value.tableName = '';
  getData2();
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

const queryPage2 = ref({
  current: 1,
  size: 10,
  total: 0
});
const pageLayout2 = ref("total, sizes, prev, pager, next");
const pageSizes2 = ref([10, 20, 50, 100]);
const pageTotal2 = ref(0);
const pageCurrent2 = ref(1);
const pageSize2 = ref(pageSizes.value[0] | 10);
const handleSizeChange2 = (page: any) => {
  pageSize2.value = page
  queryPage2.value.size = page
  getData2();
};
const handleCurrentChange2 = (page: any) => {
  pageCurrent2.value = page
  queryPage2.value.current = page
  getData2();
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

const multiSelectData2 = ref();
const selectionChange2 = (value: any) => {
  multiSelectData2.value = value
}
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

//对话框
const dialogVisible = ref(false);
/**
 * 关闭表单
 */
const handleCloseForm = () => {
  dialogVisible.value = false;
  dialogVisible2.value = false;
};
/**
 * 提交表单
 */
const handleSubmitForm = async (fr: FormInstance | undefined) => {
  let ids: any = [];
  multiSelectData2.value.forEach(i => {
    ids.push(i.tableName);
  })
  insertGenTable(ids).then(res => {
    console.log(res)
    if (res.code === 0) {
      dialogVisible.value = false;
      dialogVisible2.value = false;
      getData();
    }
  })
};
/**
 * 点击添加
 */
//@ts-ignore
const handleInsert = (index, row) => {
  dialogVisible.value = true;
};
/**
 * 点击编辑
 */
//@ts-ignore
const handleEdit = (index, row) => {
  let id;
  if (row === undefined) {
    id = multiSelectData.value[0].tableId
  } else {
    id = row.tableId
  }
  router.push({name: '字段详情', params: {tableId: id}});
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
      ids.push(i.tableId);
      names.push(i.tableName);
    })
  } else {
    ids.push(row.tableId);
    names.push(row.tableName);
  }
  ElMessageBox.confirm(
      '将删除表名称为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteGenTable(ids).then((res) => {
      if (res.code === 0) {
        getData();
      }
    });
  }).catch();
};

/**
 * 导入数据
 */
const handleImport = () => {
  dialogVisible2.value = true;
}
/**
 * 导出数据
 */
//@ts-ignore
const handleExport = (index, row) => {
  let ids: any = [];
  if (row == null) {
    multiSelectData.value.forEach(i => {
      ids.push(i.tableId);
    })
  } else {
    ids.push(row.tableId);
  }
  genCode(ids).then(res => {
    downloadExport(res);
  })
}

/**
 * 同步表
 */
//@ts-ignore
const handleSync = (index, row) => {
  console.log(row)
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