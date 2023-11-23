<template>
  <el-card>
    <!--formSearch-->
    <el-form :inline="true" :size="size" v-show="showSearch">
      <!--searchParam-->
      <el-form-item label="菜单名称" class="global-input-item">
        <el-input v-model="query.menuName" placeholder="请输入菜单名称"
                  class="global-input" :size="size"/>
      </el-form-item>
      <el-form-item label="菜单状态" class="global-input-item">
        <el-select v-model="query.menuStatus" placeholder="请选择菜单状态"
                   :size="size">
          <el-option
              v-for="item in menuStatusOptions"
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
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Plus" @click="handleInsert"
                   :color="layoutStore.BtnInsert" plain>添加菜单
        </el-button>
      </el-form-item>
      <!--right fixed-->
      <el-form-item class="global-form-item-right">
        <!--级联删除按钮 仅树表生效-->
        <el-button :size="size" circle @click="handleAllowCascadeDelete">
          <el-icon>
            <component :is="allowIcon"/>
          </el-icon>
        </el-button>
        <!--折叠树形按钮 仅树表生效-->
        <el-button :size="size" :icon="Sort" circle @click="handleExpandAll"/>
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
              :row-key="rowKey"
              :tree-props="treeProps"
              :default-expand-all="expandTable"
              :expand-row-keys="expandRowKeys"
              :highlight-current-row="true"
              header-cell-class-name="global-table-header"
              class="global-table"
              stripe>
      <el-table-column prop="menuName" label="菜单名称" align="left" min-width="150"/>
      <el-table-column prop="menuIcon" label="菜单图标" align="center" width="120">
        <template #default="scope">
          <el-icon>
            <component :is="scope.row.menuIcon"/>
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="menuAuth" label="菜单权限字段" align="center" width="240"/>
      <el-table-column prop="menuPath" label="菜单路由地址" align="center" width="240"/>
      <el-table-column prop="menuComponent" label="菜单路由组件" align="center" width="240"/>
      <el-table-column prop="menuSeq" label="菜单展示顺序" align="center" sortable width="120"/>
      <el-table-column prop="menuType" label="菜单类型" align="center" width="100">
        <template #default="scope">
          <el-tag :size="size"
          >{{ getMenuType(scope.row.menuType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="menuVisible" label="是否展示" align="center" width="100">
        <template #default="scope">
          <el-tag v-for="tag in menuVisibleOptions"
                  v-show="tag.value === scope.row.menuStatus"
                  :size="size"
                  :key="tag.value"
                  :type="tag.type"> {{ tag.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="menuStatus" label="菜单状态" align="center" width="100">
        <template #default="scope">
          <el-tag v-for="tag in menuStatusOptions"
                  v-show="tag.value === scope.row.menuStatus"
                  :size="size"
                  :key="tag.value"
                  :type="tag.type"> {{ tag.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建日期" align="center" sortable width="150"/>
      <el-table-column prop="updateTime" label="更新日期" align="center" sortable width="150"/>
      <el-table-column label="菜单操作" align="center" width="200" fixed="right">
        <template #default="scope">
          <el-button class="global-table-btn"
                     size="small" type="primary" link :icon="Plus"
                     @click="handleInsert(scope.$index, scope.row)">
            添加
          </el-button>
          <el-divider direction="vertical"/>
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
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog class="global-dialog-iu"
             title="菜单管理" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form :model="form"
             :rules="formRules"
             ref="formRuleRef">
      <el-form-item label="父级菜单" prop="parentId">
        <el-tree-select v-model="form.parentId"
                        placeholder="请选择父级菜单"
                        :props="props"
                        :data="selectData"
                        check-strictly
                        style="width: 100%;"/>
      </el-form-item>
      <el-row>
        <el-col :span="11">
          <el-form-item label="菜单名称" prop="menuName">
            <el-input v-model="form.menuName" autocomplete="off"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="菜单顺序" prop="menuSeq">
            <el-input-number v-model="form.menuSeq"
                             :min="1" :max="99"
                             controls-position="right"
                             autocomplete="off"
                             style="width: 100%"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="权限字段" prop="menuAuth">
            <el-input v-model="form.menuAuth" autocomplete="off"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="菜单路由" prop="menuPath">
            <el-input v-model="form.menuPath" autocomplete="off"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="&nbsp;&nbsp;菜单图标" prop="menuIcon">
            <el-input v-model="form.menuIcon" autocomplete="off"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="&nbsp;&nbsp;菜单组件" prop="menuComponent">
            <el-input v-model="form.menuComponent" autocomplete="off"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row v-show="isEdit">
        <el-col :span="11">
          <el-form-item label="&nbsp;&nbsp;创建时间" prop="createTime">
            <el-input v-model="form.createTime" autocomplete="off" readonly="readonly"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="&nbsp;&nbsp;更新时间" prop="updateTime">
            <el-input v-model="form.updateTime" autocomplete="off" readonly="readonly"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="菜单类型" prop="menuType">
        <el-radio-group v-model="form.menuType">
          <el-radio v-for="option in menuTypeOptions" :key="option.value" :label="option.value">
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-row>
        <el-col :span="11">
          <el-form-item label="是否可见" prop="menuVisible">
            <el-radio-group v-model="form.menuVisible">
              <el-radio v-for="option in menuVisibleOptions" :key="option.value" :label="option.value">
                {{ option.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="菜单状态" prop="menuStatus">
            <el-radio-group v-model="form.menuStatus">
              <el-radio v-for="option in menuStatusOptions" :key="option.value" :label="option.value">
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
import {deleteMenu, getMenuSelect, getMenuSingleton, getMenuTree, insertMenu, updateMenu} from "@/api/system/menu";
import {onMounted, ref, shallowRef} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {Check, Close, Delete, Edit, Plus, Refresh, Search, Sort} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {menuStatusOptions, menuTypeOptions, menuVisibleOptions} from "./dictionary.ts";

//store
const layoutStore = useLayoutStore();
//布局
const size = layoutStore.tableSize;
//mounted
onMounted(() => {
  getData();
  getSelect();
});

/**
 * 父级菜单下拉树 props
 */
const props = {
  value: 'id',
  label: 'label',
  children: 'children',
}

/**
 * 获取下拉框数据
 */
//下拉框数据
const selectData = ref();
const getSelect = () => {
  getMenuSelect().then(res => {
    selectData.value = res.data
  });
};

/**
 * 获取menuType tag数据
 */
const getMenuType = (menuType: number | undefined) => {
  let type = menuTypeOptions.find(i => i.value === menuType);
  return type.label
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
  menuName: '',
  menuStatus: ''
});

/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  getMenuTree(query.value).then(res => {
    tableData.value = res.data;
    loading.value = false;
  });
};

/**
 * 重置查询参数按钮
 */
const handleReset = () => {
  query.value.menuName = '';
  query.value.menuStatus = '';
  getData();
};

/**
 * 级联删除配置
 */
//级联删除标志
const allow = ref(false);
//级联删除图标
const allowIcon = shallowRef(Close);
const handleAllowCascadeDelete = () => {
  if (allow.value === false) {
    allow.value = true;
    allowIcon.value = Check;
    ElMessage.info('已允许级联删除');
  } else {
    allow.value = false;
    allowIcon.value = Close;
    ElMessage.warning('已关闭级联删除');
  }
};

/**
 * 树表参数配置
 */
//rowKey 树表id
const rowKey = ref('menuId');
//treeProps 树形结构
const treeProps = ref({children: 'children', childrenFlag: 'childrenFlag'});
//展开/收起表格
const expandTable = ref(false);
//展开/收起表格数据
const expandRowKeys = ref([1]);
const handleExpandAll = () => {
  expandRowKeys.value = [1];
  expandTable.value = !expandTable.value;
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
  menuId: 0,
  parentId: 0,
  menuName: '',
  menuSeq: 1,
  menuAuth: '',
  menuPath: '',
  menuIcon: '',
  menuComponent: '',
  createTime: '',
  updateTime: '',
  menuType: '',
  menuVisible: '',
  menuStatus: '',
});
//表单数据校验规则
const formRules = ref({
  parentId: [
    {required: true, message: '请选择上级菜单', trigger: 'blur'},
  ],
  menuName: [
    {required: true, message: '请输入菜单名称', trigger: 'blur'},
    {min: 1, max: 50, message: '菜单名称在 1 至 50 个字符之间', trigger: 'blur'},
  ],
  menuSeq: [
    {required: true, message: '请输入菜单顺序', trigger: 'blur'},
  ],
  menuAuth: [
    {required: true, message: '请输入权限字段', trigger: 'blur'},
  ],
  menuPath: [
    {required: true, message: '请输入菜单路由', trigger: 'blur'},
  ],
  menuType: [
    {required: true, message: '请选择菜单类型', trigger: 'blur'},
  ],
  menuVisible: [
    {required: true, message: '请选择菜单是否可见', trigger: 'blur'},
  ],
  menuStatus: [
    {required: true, message: '请选择菜单状态', trigger: 'blur'},
  ]
});
//表单校验ref
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
  updateMenu(form.value).then((res) => {
    if (res.code === 0) {
      isEdit.value = false;
      dialogVisible.value = false;
    }
  });
};
const doInsert = () => {
  insertMenu(form.value).then((res) => {
    if (res.code === 0) {
      dialogVisible.value = false;
    }
  });
};
/**
 * 点击添加
 */
//@ts-ignore
const handleInsert = (index, row) => {
  form.value = {
    menuAuth: "",
    menuComponent: "",
    menuIcon: "",
    menuId: 0,
    menuName: "",
    menuPath: "",
    menuSeq: 0,
    menuStatus: "",
    menuType: "",
    menuVisible: "",
    parentId: 0,
    createTime: "",
    updateTime: ""
  };
  form.value.parentId = row.menuId;
  isEdit.value = false;
  dialogVisible.value = true;
};
/**
 * 点击编辑
 */
//@ts-ignore
const handleEdit = (index, row) => {
  getMenuSingleton(row.menuId).then(res => {
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
  ElMessageBox.confirm(
      '将删除菜单名称为  \'' + row.menuName + '\'  的数据项，是否确认？', '提示',
      {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteMenu(allow.value === true ? '1' : '0', row.menuId).then((res) => {
      if (res.code === 0) {
        getData();
      }
    });
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
</style>