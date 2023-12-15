<template>
  <el-card>
    <!--formSearch-->
    <el-form :inline="true" :size="size" v-show="showSearch">
      <!--searchParam-->
      <el-form-item label="用户名称" class="global-input-item">
        <el-input v-model="query.userName" placeholder="请输入用户名称"
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
      <el-form-item class="global-form-item-margin" v-if="checkPermission('sys:user:query')">
        <el-button :size="size" :icon="Plus" @click="handleInsert"
                   :color="layoutStore.BtnInsert" plain>添加用户
        </el-button>
      </el-form-item>
      <!--delete-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('system:role:auth:user')">
        <el-button :size="size" :icon="Delete" @click="handleDelete"
                   :color="layoutStore.BtnDelete" plain :disabled="selectable">取消授权
        </el-button>
      </el-form-item>
      <!--right fixed-->
      <el-form-item class="global-form-item-right">
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
      <el-table-column prop="userName" label="用户名称" align="left"/>
      <el-table-column prop="nickName" label="用户昵称" align="center"/>
      <el-table-column prop="phone" label="用户手机" align="center"/>
      <el-table-column prop="email" label="用户邮箱" align="center"/>
      <el-table-column label="授权操作" align="center" width="120" fixed="right"
                       v-if="checkPermissions(['system:role:auth:user'])">
        <template #default="scope">
          <div class="display">
            <div class="display" v-if="checkPermission('system:role:auth:user')">
              <el-button class="global-table-btn red"
                         size="small" type="primary" link :icon="Delete"
                         @click="handleDelete(scope.$index, scope.row)">
                取消授权
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
             title="授权管理" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <template #default>
      <el-form :inline="true" :size="size">
        <el-form-item label="用户名称" class="global-input-item">
          <el-input v-model="query2.userName" placeholder="请输入用户名称"
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
        <el-table-column prop="userName" label="用户名称" align="left"/>
        <el-table-column prop="nickName" label="用户昵称" align="center"/>
        <el-table-column prop="phone" label="用户手机" align="center"/>
        <el-table-column prop="email" label="用户邮箱" align="center"/>
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
    </template>
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
import {Delete, Plus, Refresh, Search,} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {checkPermission, checkPermissions} from "@/utils/permission.ts";
import {useRoute} from "vue-router";
import {authUser, queryAuthUser, queryNonAuthUser, unauthUser} from "@/api/system/role";

//提升作用域 便于监听
//对话框
const dialogVisible = ref(false);

//store
const layoutStore = useLayoutStore();
//布局
const size = layoutStore.tableSize;
const pageLayoutSize = computed(() => {
  return size === 'small';
});
//获取请求参数
const route = useRoute();
const roleId = ref();
//mounted
onMounted(() => {
  roleId.value = route.params.roleId;
  getData();
});
//
watch(() => dialogVisible, () => {
  if (dialogVisible.value === true) {
    getData2()
  }
}, {deep: true})

/**
 * 查询参数
 */
//加载标志
const loading = ref(false);
const loading2 = ref(false);
//表格数据 授权用户
const tableData = ref();
//表格数据2 未授权用户
const tableData2 = ref();
//查询参数
const query = ref({
  roleId: roleId,
  userName: ''
});

const query2 = ref({
  roleId: roleId,
  userName: ''
});

/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  queryAuthUser(paramBuilder(query.value, queryPage.value, null, null)).then(res => {
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
  queryNonAuthUser(paramBuilder(query2.value, queryPage2.value, null, null)).then(res => {
    const response = res.data;
    tableData2.value = response.data;
    pageCurrent2.value = response.current;
    pageTotal2.value = response.total;
    pageSize2.value = response.size;
    loading2.value = false;
  });
}

/**
 * 重置查询参数按钮
 */
const handleReset = () => {
  query.value.userName = '';
  getData();
};
const handleReset2 = () => {
  query2.value.userName = '';
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

//分页2 未授权用户
const queryPage2 = ref({
  current: 1,
  size: 10,
  total: 0
});
const pageLayout2 = ref("total, sizes, prev, pager, next");
const pageSizes2 = ref([10, 20, 50, 100]);
const pageTotal2 = ref(0);
const pageCurrent2 = ref(1);
const pageSize2 = ref(pageSizes2.value[0] | 10);
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
//多选数据2 未授权用户
const multiSelectData2 = ref();
const selectionChange2 = (value: any) => {
  multiSelectData2.value = value
};
/**
 * 隐藏时间列
 */
const showTimeColumn = ref(false);

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
const form = ref({});
//表单数据校验规则
const formRules = ref({});
//表单校验规则ref
const formRuleRef = ref<FormInstance>();

//是否编辑
const isEdit = ref(false);


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
const handleSubmitForm = (fr: FormInstance | undefined) => {
  let ids: any = [];
  multiSelectData2.value.forEach(i => {
    ids.push(i.userId);
  })
  authUser(ids, roleId.value).then(res => {
    if (res.code === 0) {
      getData();
      isEdit.value = false;
      dialogVisible.value = false;
    }
  })
};
/**
 * 点击添加
 */
//@ts-ignore
const handleInsert = (index, row) => {
  isEdit.value = false;
  dialogVisible.value = true;
};
/**
 * 点击编辑
 */
//@ts-ignore
const handleEdit = (index, row) => {
  isEdit.value = true;
  dialogVisible.value = true;
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
      ids.push(i.userId);
      names.push(i.userName);
    })
  } else {
    ids.push(row.userId);
    names.push(row.userName);
  }
  ElMessageBox.confirm(
      '将取消授权用户名称为  \'' + names.toString().substring(0, 40) + '\'  的角色，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    unauthUser(ids, roleId.value).then((res) => {
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