<template>
  <el-card>
    <!--formSearch-->
    <el-form :inline="true" :size="size" v-show="showSearch">
      <!--searchParam-->
      <el-form-item label="角色名称" class="global-input-item">
        <el-input v-model="query.roleName" placeholder="请输入角色名称"
                  class="global-input" :size="size"/>
      </el-form-item>
      <el-form-item label="角色状态" class="global-input-item">
        <el-select v-model="query.roleStatus" placeholder="请选择角色状态"
                   :size="size">
          <el-option
              v-for="item in roleStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="角色数据权限" class="global-input-item">
        <el-select v-model="query.roleScope" placeholder="请选择角色数据权限"
                   :size="size">
          <el-option
              v-for="item in roleDataScopeOptions"
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
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Plus" @click="handleInsert"
                   :color="layoutStore.BtnInsert" plain>添加角色
        </el-button>
      </el-form-item>
      <!--edit-->
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Edit" @click="handleEdit"
                   :color="layoutStore.BtnUpdate" plain :disabled="!selectSingle">修改角色
        </el-button>
      </el-form-item>
      <!--delete-->
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Delete" @click="handleDelete"
                   :color="layoutStore.BtnDelete" plain :disabled="selectable">删除角色
        </el-button>
      </el-form-item>
      <!--import-->
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Download" @click="handleImport"
                   :color="layoutStore.BtnImport" plain>导入角色
        </el-button>
      </el-form-item>
      <!--export-->
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Upload" @click="handleExport"
                   :color="layoutStore.BtnExport" plain>导出角色
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
      <el-table-column prop="roleName" label="角色名称" align="left" min-width="150"/>
      <el-table-column prop="roleKey" label="角色权限字符" align="center" min-width="150"/>
      <el-table-column prop="roleSeq" label="角色展示顺序" align="center" sortable width="140"/>
      <el-table-column prop="roleScope" label="角色数据权限" align="center" width="160">
        <template #default="scope">
          <el-tag :size="size"
          >{{ getDataScope(scope.row.roleScope) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="roleStatus" label="角色状态" align="center" width="120">
        <template #default="scope">
          <el-tag v-for="tag in roleStatusOptions"
                  v-show="tag.value === scope.row.roleStatus"
                  :size="size"
                  :key="tag.value"
                  :type="tag.type"> {{ tag.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="showTimeColumn" prop="createTime" label="创建日期" align="center" sortable width="180"/>
      <el-table-column v-if="showTimeColumn" prop="updateTime" label="更新日期" align="center" sortable width="180"/>
      <el-table-column label="角色操作" align="center" width="220" fixed="right">
        <template #default="scope">
          <div class="display">
            <el-button class="global-table-btn"
                       size="small" type="primary" link :icon="Edit"
                       @click="handleEdit(scope.$index, scope.row)">
              编辑
            </el-button>
            <el-divider direction="vertical"/>
            <el-button class="global-table-btn red"
                       size="small" type="primary" link :icon="Delete"
                       @click="handleDelete(scope.$index, scope.row)">
              删除
            </el-button>
            <el-divider direction="vertical"/>
            <el-dropdown class="global-table-dropdown" size="small">
              <span class="display">
                <el-icon><DArrowRight/></el-icon>
                更多
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :icon="Lock" @click="handleMenuPermission(scope.$index, scope.row)">分配菜单权限
                  </el-dropdown-item>
                  <el-dropdown-item :icon="Lock" @click="handleDataScopePermission(scope.$index, scope.row)">分配数据权限
                  </el-dropdown-item>
                  <el-dropdown-item :icon="User" @click="handleAuthUserRole(scope.$index, scope.row)">分配用户
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
             title="角色管理" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form :model="form"
             :rules="formRules"
             ref="formRuleRef">
      <el-row>
        <el-col :span="11">
          <el-form-item label="角色名称" label-width="85" prop="roleName">
            <el-input v-model="form.roleName" autocomplete="off"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="角色编码" label-width="85" prop="roleKey">
            <el-input v-model="form.roleKey" autocomplete="off"/>
          </el-form-item>
        </el-col>
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
      <el-row>
        <el-col :span="11">
          <el-form-item label="角色顺序" label-width="85" prop="roleSeq">
            <el-input-number v-model="form.roleSeq"
                             :min="1" :max="99"
                             controls-position="right"
                             autocomplete="off"
                             style="width: 100%"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="角色状态" label-width="85" prop="roleStatus">
            <el-radio-group v-model="form.roleStatus">
              <el-radio v-for="option in roleStatusOptions" :key="option.value" :label="option.value">
                {{ option.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="角色备注" label-width="85" prop="remark">
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

  <el-drawer title="菜单权限管理"
             class="global-drawer-iu"
             v-model="menuDrawer"
             :before-close="beforeCloseMenuDrawer">
    <template #default>
      <p class="p-header">
        当前分配角色： <span class="p-i-header">{{ menuRoleName }}</span>
      </p>
      <p class="check-box">
        <el-checkbox v-model="menuBtnCheckStrictly">父子联动</el-checkbox>
        <el-divider class="d"/>
      </p>
      <el-tree ref="menuTreeRef"
               show-checkbox
               node-key="id"
               :default-expand-all="true"
               :data="menuData"
               :default-checked-keys="menuKeys"
               :check-strictly="!menuBtnCheckStrictly"
      />
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="handleCloseMenuDrawer">返回</el-button>
        <el-button type="primary" @click="confirmMenuDrawer">确认</el-button>
      </div>
    </template>
  </el-drawer>

  <el-drawer title="数据权限管理"
             class="global-drawer-iu"
             v-model="dataScopeDrawer"
             :before-close="beforeCloseDataScopeDrawer">
    <template #default>
      <div>
        <p class="p-header">
          当前分配角色： <span class="p-i-header">{{ dsRoleName }}</span>
        </p>
        <el-select v-model="selectScope" placeholder="请选择分配权限">
          <el-option
              v-for="item in roleDataScopeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </div>
      <div v-show="selectScope == '2'">
        <p class="check-box">
          <el-checkbox v-model="dsBtnCheckStrictly">父子联动</el-checkbox>
          <el-divider class="d"/>
        </p>
        <el-tree ref="dsTreeRef"
                 show-checkbox
                 node-key="id"
                 :default-expand-all="true"
                 :data="dsData"
                 :default-checked-keys="dsKeys"
                 :check-strictly="!dsBtnCheckStrictly"
        />
      </div>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="handleCloseDataScopeDrawer">返回</el-button>
        <el-button type="primary" @click="confirmDataScopeDrawer">确认</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {
  DArrowRight,
  Delete,
  Download,
  Edit,
  Lock,
  Plus,
  Refresh,
  Search,
  Timer,
  Upload,
  User
} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox, ElTree, FormInstance} from "element-plus";
import {
  deleteRole,
  getRoleList,
  getRoleSingleton,
  insertRole,
  updateRole,
  updateRoleAuth,
  updateRoleScope
} from "@/api/system/role";
import {roleDataScopeOptions, roleStatusOptions} from "./dictionary.ts";
import {paramBuilder} from "@/utils/common.ts";
import {getMenuSelectRole} from "@/api/system/menu";
import {getDeptSelectRole} from "@/api/system/dept";
import router from "@/router";

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
 * 获取 dataScope tag数据
 */
const getDataScope = (scope: string | undefined) => {
  let res = roleDataScopeOptions.find(i => i.value === scope);
  if (res)
    return res.label;
};

/**
 * 查询参数
 */
//加载标志
const loading = ref(false);
//表格数据
const tableData = ref();
//查询参数
const query = ref({
  roleName: '',
  roleStatus: '',
  roleScope: ''
});

/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  getRoleList(paramBuilder(query.value, queryPage.value, null, null)).then(res => {
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
  query.value.roleName = '';
  query.value.roleStatus = '';
  query.value.roleScope = '';
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
  roleId: 0,
  roleName: '',
  roleKey: '',
  roleScope: 1,
  roleSeq: 1,
  roleStatus: '',
  remark: '',
  createTime: '',
  updateTime: ''
});
//表单数据校验规则
const formRules = ref({
  roleName: [
    {required: true, message: '请输入角色名称', trigger: 'blur'},
    {min: 2, max: 20, message: '菜单名称在 2 至 20 个字符之间', trigger: 'blur'}
  ],
  roleKey: [
    {required: true, message: '请输入角色权限字符', trigger: 'blur'},
    {min: 2, max: 50, message: '角色权限字符在 2 至 50 个字符之间', trigger: 'blur'}
  ],
  roleSeq: [
    {required: true, message: '请输入角色顺序', trigger: 'blur'},
  ],
  roleStatus: [
    {required: true, message: '请选择角色状态', trigger: 'blur'},
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
  updateRole(form.value).then((res) => {
    if (res.code === 0) {
      isEdit.value = false;
      dialogVisible.value = false;
      getData();
    }
  });
};
const doInsert = () => {
  insertRole(form.value).then((res) => {
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
    roleId: 0,
    roleName: '',
    roleKey: '',
    roleScope: 1,
    roleSeq: 1,
    roleStatus: '',
    remark: '',
    createTime: '',
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
    id = multiSelectData.value[0].roleId;
  } else {
    id = row.roleId;
  }
  getRoleSingleton(id).then(res => {
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
      ids.push(i.roleId);
      names.push(i.roleName);
    })
  } else {
    ids.push(row.roleId);
    names.push(row.roleName);
  }
  ElMessageBox.confirm(
      '将删除角色名称为  \'' + names.toString().substring(0, 100) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteRole(ids).then((res) => {
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

}
/**
 * 导出数据
 */
const handleExport = () => {

}

/**
 * more 菜单权限
 */
const menuDrawer = ref(false);
const menuData = ref();
const menuKeys = ref();
const menuTempRowData = ref();
const menuRoleName = ref();
const menuTreeRef = ref<InstanceType<typeof ElTree>>();
const menuBtnCheckStrictly = ref(false);
//@ts-ignore
const handleMenuPermission = (index, row) => {
  menuDrawer.value = true;
  getMenuSelectRole(row.roleId).then(res => {
    menuData.value = res.data.tree;
    menuKeys.value = res.data.keys;
    menuTempRowData.value = row;
    menuRoleName.value = menuTempRowData.value.roleName;
  });
};
const beforeCloseMenuDrawer = () => {
  ElMessageBox.confirm('确定要离开吗，离开该页面后数据不会保存！',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'})
      .then(() => {
        handleCloseMenuDrawer();
      }).catch();
};
const handleCloseMenuDrawer = () => {
  menuDrawer.value = false;
};
const confirmMenuDrawer = () => {
  menuTempRowData.value.menuIdList = menuTreeRef.value!.getCheckedKeys(false);
  updateRoleAuth(menuTempRowData.value).then(res => {
    if (res.code === 0) {
      menuTempRowData.value = null;
      handleCloseMenuDrawer();
    }
  });
};

/**
 * more 数据权限
 */
const dataScopeDrawer = ref(false);
const dsData = ref();
const dsKeys = ref();
const dsTempRowData = ref();
const dsRoleName = ref();
const dsTreeRef = ref<InstanceType<typeof ElTree>>();
const dsBtnCheckStrictly = ref(false);
const selectScope = ref('2');
//@ts-ignore
const handleDataScopePermission = (index, row) => {
  dataScopeDrawer.value = true;
  selectScope.value = row.roleScope;
  getDeptSelectRole(row.roleId).then(res => {
    dsData.value = res.data.tree;
    dsKeys.value = res.data.keys;
    dsTempRowData.value = row;
    dsRoleName.value = dsTempRowData.value.roleName;
  });
};
const beforeCloseDataScopeDrawer = () => {
  ElMessageBox.confirm('确定要离开吗，离开该页面后数据不会保存！',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'})
      .then(() => {
        handleCloseDataScopeDrawer();
      }).catch();
};
const handleCloseDataScopeDrawer = () => {
  dataScopeDrawer.value = false;
};
const confirmDataScopeDrawer = () => {
  dsTempRowData.value.deptIdList = dsTreeRef.value!.getCheckedKeys(false);
  dsTempRowData.value.roleScope = selectScope.value;
  updateRoleScope(dsTempRowData.value).then(res => {
    if (res.code === 0) {
      dsTempRowData.value = null;
      handleCloseDataScopeDrawer();
    }
  });
};

/**
 * 分配角色至用户
 */
//@ts-ignore
const handleAuthUserRole = (index, row) => {
  router.push({name: '分配角色', params: {roleId: row.roleId}});
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

.p-header {
  margin: -12px 0 20px;
  color: #606266;
  font-size: 16px;

  .p-i-header {
    color: #3d3d3d;
  }
}

.check-box {
  margin: 10px 0 18px;
}

.d {
  padding: 0;
  margin: 10px 0 0;
}
</style>