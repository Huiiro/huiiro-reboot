<template>
  <el-card>
    <div style="display: flex">
      <transition name="left-fade">
        <div class="left" :class="{fold: !showLeft}">
          <el-input :prefix-icon="Search" :size=size v-model="filterText" v-show="showLeft" placeholder="输入部门名称"/>
          <el-tree node-key="id"
                   ref="deptTreeRef"
                   v-show="showLeft"
                   :props="props"
                   :data="selectDeptData"
                   :highlight-current="true"
                   :default-expand-all="true"
                   :expand-on-click-node="false"
                   :filter-node-method="filterNode"
                   @node-click="handleDeptNodeClick"
                   style="padding-top: 20px"/>
        </div>
      </transition>
      <div class="right" :class="{fold: !showLeft}">
        <!--formSearch-->
        <el-form :inline="true" :size="size" v-show="showSearch">
          <!--add for left control-->
          <el-form-item style="margin-right: 8px">
            <el-icon @click="handleShowLeft">
              <Switch/>
            </el-icon>
          </el-form-item>
          <!--searchParam-->
          <el-form-item label="用户名称" class="global-input-item">
            <el-input v-model="query.userName" placeholder="请输入用户名称"
                      class="global-input" :size="size"/>
          </el-form-item>
          <el-form-item label="用户状态" class="global-input-item">
            <el-select v-model="query.userStatus" placeholder="请选择用户状态"
                       :size="size">
              <el-option
                  v-for="item in userStatusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"/>
            </el-select>
          </el-form-item>
          <el-form-item label="用户手机" class="global-input-item">
            <el-input v-model="query.phone" placeholder="请输入用户手机"
                      class="global-input" :size="size"/>
          </el-form-item>
          <el-form-item label="用户邮箱" class="global-input-item">
            <el-input v-model="query.email" placeholder="请输入用户邮箱"
                      class="global-input" :size="size"/>
          </el-form-item>
          <el-form-item label="创建时间" class="global-input-item">
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
          <!--add-->
          <el-form-item class="global-form-item-margin">
            <el-button :size="size" :icon="Plus" @click="handleInsert"
                       :color="layoutStore.BtnInsert" plain
                       v-if="checkPermission('system:user:add')">添加用户
            </el-button>
          </el-form-item>
          <!--edit-->
          <el-form-item class="global-form-item-margin">
            <el-button :size="size" :icon="Edit" @click="handleEdit"
                       :color="layoutStore.BtnUpdate" plain :disabled="!selectSingle"
                       v-if="checkPermission('system:user:edit')">修改用户
            </el-button>
          </el-form-item>
          <!--delete-->
          <el-form-item class="global-form-item-margin">
            <el-button :size="size" :icon="Delete" @click="handleDelete"
                       :color="layoutStore.BtnDelete" plain :disabled="selectable"
                       v-if="checkPermission('system:user:delete')">删除用户
            </el-button>
          </el-form-item>
          <!--import-->
          <el-form-item class="global-form-item-margin">
            <el-button :size="size" :icon="Download" @click="handleImport"
                       :color="layoutStore.BtnImport" plain
                       v-if="checkPermission('system:user:import')">导入用户
            </el-button>
          </el-form-item>
          <!--export-->
          <el-form-item class="global-form-item-margin">
            <el-button :size="size" :icon="Upload" @click="handleExport"
                       :color="layoutStore.BtnExport" plain
                       v-if="checkPermission('system:user:export')">导出用户
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
          <el-table-column prop="userId" label="用户ID" align="left" min-width="150"/>
          <el-table-column prop="userName" label="用户名称" align="left" width="150"/>
          <el-table-column prop="nickName" label="用户昵称" align="center" width="150"/>
          <el-table-column prop="sexual" label="用户性别" align="center" width="120">
            <template #default="scope">
              <p v-for="tag in userSexualStatusOptions"
                 v-show="tag.value === scope.row.sexual"> {{ tag.label }}
              </p>
            </template>
          </el-table-column>
          <el-table-column prop="phone" label="用户手机" align="center" width="160"/>
          <el-table-column prop="email" label="用户邮箱" align="center" width="200"/>
          <el-table-column prop="userStatus" label="用户状态" align="center" width="120">
            <template #default="scope">
              <el-tag v-for="tag in userStatusOptions"
                      v-show="tag.value === scope.row.userStatus"
                      :size="size"
                      :key="tag.value"
                      :type="tag.type"> {{ tag.label }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column v-if="showTimeColumn" prop="createTime" label="创建日期" align="center" sortable
                           width="170"/>
          <el-table-column v-if="showTimeColumn" prop="updateTime" label="更新日期" align="center" sortable
                           width="170"/>
          <el-table-column label="用户操作" align="center" width="200" fixed="right"
                           v-if="checkPermissions(['system:user:edit','system:user:delete'])">
            <template #default="scope">
              <div class="display">
                <div v-if="checkPermission('system:user:edit')" class="display">
                  <el-button class="global-table-btn"
                             size="small" type="primary" link :icon="Edit"
                             @click="handleEdit(scope.$index, scope.row)">
                    编辑
                  </el-button>
                  <el-divider direction="vertical"/>
                </div>
                <div v-if="checkPermission('system:user:delete')" class="display">
                  <el-button class="global-table-btn red"
                             size="small" type="primary" link :icon="Delete"
                             @click="handleDelete(scope.$index, scope.row)">
                    删除
                  </el-button>
                  <el-divider direction="vertical"/>
                </div>
                <!--selectable more actions-->
                <el-dropdown class="global-table-dropdown" size="small"
                             v-if="checkPermission('system:user:edit')">
              <span class="display">
                <el-icon><DArrowRight/></el-icon>
                更多
              </span>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item :icon="Lock" @click="handleResetPwd(scope.$index, scope.row)">重置密码
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
      </div>
    </div>
  </el-card>

  <el-dialog class="global-dialog-iu"
             title="用户管理" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form :model="form"
             :rules="formRules"
             ref="formRuleRef">
      <el-row>
        <el-col :span="11">
          <el-form-item label="用户名称" label-width="85" prop="userName">
            <el-input v-model="form.userName" autocomplete="off"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="用户昵称" label-width="85" prop="nickName">
            <el-input v-model="form.nickName" autocomplete="off"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="用户手机" label-width="85" prop="phone">
            <el-input v-model="form.phone" autocomplete="off"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="用户邮箱" label-width="85" prop="email">
            <el-input v-model="form.email" autocomplete="off"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="角色分配" label-width="85" prop="roleIds">
            <el-select
                v-model="form.roleIds"
                multiple
                placeholder="请选择角色进行分配"
                style="width: 240px"
            >
              <el-option
                  v-for="item in roles"
                  :key="item.id"
                  :label="item.label"
                  :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="岗位分配" label-width="85" prop="postIds">
            <el-select
                v-model="form.postIds"
                multiple
                placeholder="请选择岗位进行分配"
                style="width: 240px"
            >
              <el-option
                  v-for="item in posts"
                  :key="item.id"
                  :label="item.label"
                  :value="item.id"
              />
            </el-select>
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
      <el-row v-show="isEdit">
        <el-col :span="11">
          <el-form-item label="登录IP" label-width="85" prop="loginIp">
            <el-input v-model="form.loginIp" autocomplete="off" readonly="readonly"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="登录时间" label-width="85" prop="loginTime">
            <el-input v-model="form.loginTime" autocomplete="off" readonly="readonly"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="用户性别" label-width="85" prop="sexual">
            <el-radio-group v-model="form.sexual">
              <el-radio v-for="option in userSexualStatusOptions" :key="option.value" :label="option.value">
                {{ option.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="用户状态" label-width="85" prop="userStatus">
            <el-radio-group v-model="form.userStatus">
              <el-radio v-for="option in userStatusOptions" :key="option.value" :label="option.value">
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

  <el-dialog class="global-dialog-iu"
             title="重置密码" v-model="resetPwdDialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form label-position="top"
             :model="pwdForm"
             :rules="pwdFormRules"
             ref="pwdFormRuleRef">
      <el-form-item label="为用户重新设置密码（密码长度至少为6位）。" prop="password">
        <el-input v-model="pwdForm.password" placeholder="请输入新密码"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleCloseForm">取 消</el-button>
      <el-button type="primary" @click="handleConfirmResetPwd(pwdFormRuleRef)">确 定</el-button>
    </template>
  </el-dialog>

  <el-dialog class="global-dialog-iu"
             title="批量导入" v-model="importVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <upload :url="importUrl"
            :show-list="false"
            :size=10
            type="xls/xlsx">
      <p class="temp-text">下载批量导入模板：
        <a class="hover"
           @click="handleDownloadTemplate"
        >用户导入模板.xlsx</a>
      </p>
    </upload>
  </el-dialog>
</template>

<script setup lang="ts">
import {computed, onMounted, ref, watch} from "vue";
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
  Switch,
  Timer,
  Upload,
} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox, ElTree, FormInstance} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {
  deleteUser,
  exportUser,
  getExportUserTemplate,
  getUserList,
  getUserSingleton,
  importUser,
  insertUser,
  resetPwd,
  updateUser,
} from "@/api/system/user";
import {userSexualStatusOptions, userStatusOptions} from "@/views/system/user/dictionary.ts";
import {getDeptSelect} from "@/api/system/dept";
import {Tree} from "element-plus/es/components/tree-v2/src/types";
import {encryptFiled} from "@/utils/encrypt.ts";
import {download} from "@/utils/download.ts";
import upload from '@/components/upload/Index.vue';
import {checkPermission, checkPermissions} from "@/utils/permission.ts";

//store
const layoutStore = useLayoutStore();
//布局
const size = layoutStore.tableSize;
const pageLayoutSize = computed(() => {
  return size === 'small';
});
//
const showLeft = ref(true);
const handleShowLeft = () => {
  showLeft.value = !showLeft.value
}
//mounted
onMounted(() => {
  getData();
  getDeptData();
});

/**
 * 父级菜单下拉树 props
 */
const props = {
  value: 'id',
  label: 'label',
  children: 'children',
};

/**
 * 获取下拉框数据
 */
//下拉框数据
const selectDeptData = ref();
const getDeptData = () => {
  getDeptSelect({}).then(res => {
    selectDeptData.value = res.data[0].children;
  });
};
const selectDeptId = ref(1);
const handleDeptNodeClick = (data: Tree) => {
  selectDeptId.value = data.id;
  query.value.deptId = data.id;
  getData();
};

/**
 * 部门树过滤器
 */
const deptTreeRef = ref<InstanceType<typeof ElTree>>();
const filterText = ref();

watch(filterText, (val) => {
  deptTreeRef.value!.filter(val);
});

const filterNode = (value: string, data: Tree) => {
  if (!value) return true
  return data.label.includes(value);
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
  userName: '',
  userStatus: '',
  phone: '',
  email: '',
  deptId: null
});
const time = ref();
/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  getUserList(paramBuilder(query.value, queryPage.value, time.value, null)).then(res => {
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
  query.value.userName = '';
  query.value.userStatus = '';
  query.value.phone = '';
  query.value.email = '';
  query.value.deptId = null;
  filterText.value = '';
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
  userId: 0,
  deptId: 0,
  userName: '',
  nickName: '',
  password: '',
  phone: '',
  email: '',
  sexual: '',
  avatar: '',
  loginIp: '',
  loginTime: '',
  deleteFlag: '',
  userStatus: '',
  createTime: '',
  updateTime: '',
  roleIds: '',
  postIds: ''
});
//表单数据校验规则
const formRules = ref({
  userName: [
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 1, max: 20, message: '用户名长度不超过20个字符', trigger: 'blur'}
  ],
  phone: [
    {required: true, message: '请输入手机', trigger: 'blur'},
    {min: 1, max: 20, message: '手机长度不超过20个字符之间', trigger: 'blur'}
  ],
  email: [
    {required: true, message: '请输入邮箱', trigger: 'blur'},
    {min: 1, max: 40, message: '邮箱长度不超过40个字符之间', trigger: 'blur'}
  ],
  avatar: [
    {required: true, message: '请选择用户性别', trigger: 'blur'},
  ],
  userStatus: [
    {required: true, message: '请选择用户状态', trigger: 'blur'},
  ]
});
//表单校验规则ref
const formRuleRef = ref<FormInstance>();

//edit value
//
const roles = ref();
const posts = ref();

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
  resetPwdDialogVisible.value = false;
  importVisible.value = false;
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
  updateUser(form.value).then((res) => {
    if (res.code === 0) {
      isEdit.value = false;
      dialogVisible.value = false;
      getData();
    }
  });
};
const doInsert = () => {
  insertUser(form.value).then((res) => {
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
    avatar: "",
    deleteFlag: "",
    deptId: null,
    email: "",
    loginIp: "",
    loginTime: "",
    nickName: "",
    password: "",
    phone: "",
    sexual: "",
    userId: 0,
    userStatus: "",
    userName: "",
    createTime: "",
    updateTime: "",
    postIds: "",
    roleIds: ""
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
    id = multiSelectData.value[0].userId;
  } else {
    id = row.userId;
  }
  getUserSingleton(id).then(res => {
    form.value = res.data.user;
    roles.value = res.data.roles;
    posts.value = res.data.posts;
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
      ids.push(i.userId);
      names.push(i.userName);
    })
  } else {
    ids.push(row.userId);
    names.push(row.userName);
  }
  ElMessageBox.confirm(
      '将删除用户名称为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteUser(ids).then((res) => {
      if (res.code === 0) {
        getData();
      }
    });
  }).catch();
};

/**
 * 重置密码
 */
const resetPwdDialogVisible = ref();
const tempResetModel = ref();
const pwdForm = ref({
  password: ''
});
const pwdFormRules = ref({
  password: [
    {required: true, message: '请输入新密码', trigger: 'blur'},
    {min: 6, max: 20, message: '新密码长度在6-20个字符之间', trigger: 'blur'}
  ]
});
const pwdFormRuleRef = ref<FormInstance>();
//@ts-ignore
const handleResetPwd = (index, row) => {
  tempResetModel.value = row;
  resetPwdDialogVisible.value = true;
};
const handleConfirmResetPwd = async (fr: FormInstance | undefined) => {
  //@ts-ignore
  await fr.validate((valid) => {
    if (valid) {
      encryptFiled(pwdForm.value.password).then(newVal => {
        tempResetModel.value.encrypt = newVal;
        resetPwd(tempResetModel.value).then(res => {
          if (res.code === 0) {
            handleCloseForm();
            let msg = "已重置用户 '" + tempResetModel.value.userName + "' 的密码！";
            ElMessage({type: 'success', message: msg});
          }
        });
      });
    }
  });
};

/**
 * 导入数据
 */
const importVisible = ref(false);
const importUrl = ref(import.meta.env.VITE_BASE_URL + importUser());
const handleImport = () => {
  importVisible.value = true;
}
const handleDownloadTemplate = () => {
  getExportUserTemplate().then(res => {
    download(res)
  });
};
/**
 * 导出数据
 */
const handleExport = () => {
  exportUser({}).then(res => {
    download(res)
  });
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

.left {
  min-width: $u-left-width;
  padding-right: 20px;
  overflow: hidden;
  transition: all 0.3s ease-out;

  &.fold {
    min-width: 0;
    max-width: 0;
  }
}

.right {
  width: calc(100% - $u-left-width);
  transition: all 0.3s ease-out;

  &.fold {
    min-width: calc(100% - 20px);
    max-width: calc(100% - 20px);
  }
}

.hover {
  color: $global-hover-color;
  cursor: pointer;
}

.temp-text {
  font-size: 12px;
  margin-top: 6px;
}

.left-fade-enter-active, .left-fade-leave-active {
  transition: all 0.3s;
}

.left-fade-enter, .left-fade-leave-to {
  opacity: 0;
}
</style>