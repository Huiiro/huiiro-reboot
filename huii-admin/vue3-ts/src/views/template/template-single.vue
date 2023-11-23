<!--通用模板-->
<!--单表模板-->
<!--version:1.1-->
<!--author:huii-->
<template>
  <el-card>
    <!--formSearch-->
    <el-form :inline="true" :size="size" v-show="showSearch">
      <!--searchParam-->
      <!--TODO-->
      <el-form-item label="huii名称" class="global-input-item">
        <el-input v-model="query.huii" placeholder="请输入huii名称"
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
      <!--TODO-->
      <!--add-->
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Plus" @click="handleInsert"
                   :color="layoutStore.BtnInsert" plain>添加huii
        </el-button>
      </el-form-item>
      <!--edit-->
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Edit" @click="handleEdit"
                   :color="layoutStore.BtnUpdate" plain :disabled="!selectSingle">修改huii
        </el-button>
      </el-form-item>
      <!--delete-->
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Delete" @click="handleDelete"
                   :color="layoutStore.BtnDelete" plain :disabled="selectable">删除huii
        </el-button>
      </el-form-item>
      <!--import-->
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Download" @click="handleImport"
                   :color="layoutStore.BtnImport" plain>导入huii
        </el-button>
      </el-form-item>
      <!--export-->
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="Upload" @click="handleExport"
                   :color="layoutStore.BtnExport" plain>导出huii
        </el-button>
      </el-form-item>
      <!--upload-->
      <el-form-item class="global-form-item-margin">
        <el-button :size="size" :icon="UploadFilled" @click="handleUpload"
                   :color="layoutStore.BtnUpload" plain>上传huii
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
      <!--TODO-->
      <el-table-column prop="huii" label="huii" align="left" min-width="150"/>
      <el-table-column prop="huii" label="huii" align="center" width="120"/>
      <el-table-column prop="createTime" label="创建日期" align="center" sortable width="150"/>
      <el-table-column prop="updateTime" label="更新日期" align="center" sortable width="150"/>
      <!--TODO-->
      <el-table-column label="huii操作" align="center" width="200" fixed="right">
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
            <!--selectable more actions-->
            <!--TODO-->
            <el-dropdown class="global-table-dropdown" size="small">
              <span class="display">
                <el-icon><DArrowRight/></el-icon>
                更多
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :icon="Plus">TODO</el-dropdown-item>
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

  <!--TODO-->
  <el-dialog class="global-dialog-iu"
             title="huii管理" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form :model="form"
             :rules="formRules"
             ref="formRuleRef">
      <el-row>
        <el-col :span="11">
          <!--TODO-->
          <el-form-item label="huii" prop="huii">
            <el-input v-model="form.huii" autocomplete="off"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
        </el-col>
      </el-row>
    </el-form>
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
    <template #footer>
      <el-button @click="handleCloseForm">取 消</el-button>
      <el-button type="primary" @click="handleSubmitForm(formRuleRef)">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {
  DArrowRight,
  Delete,
  Download,
  Edit,
  Plus,
  Refresh,
  Search,
  Upload,
  UploadFilled
} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";

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
  //TODO
  huii: '',
});

/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  //TODO
  getHuiiList(paramBuilder(query.value, queryPage.value, null)).then(res => {
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
  //TODO
  query.value.huii = '';
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
  //TODO
  huii: '',
  createTime: '',
  updateTime: ''
});
//表单数据校验规则
const formRules = ref({
  //TODO
  huii: [
    {required: true, message: '请输入huii', trigger: 'blur'},
    {min: 1, max: 20, message: 'huii在 1 至 20 个字符之间', trigger: 'blur'}
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
  //TODO
  updateHuii(form.value).then((res) => {
    if (res.code === 0) {
      isEdit.value = false;
      dialogVisible.value = false;
      getData();
    }
  });
};
const doInsert = () => {
  //TODO
  insertHuii(form.value).then((res) => {
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
  //TODO
  form.value = {
    huii: "",
    createTime: "",
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
  //TODO
  let id;
  if (row == null) {
    id = multiSelectData.value[0].huii;
  } else {
    id = row.huii;
  }
  getHuiiSingleton(id).then(res => {
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
  //TODO
  let ids: any = [];
  let names: any = [];
  if (row == null) {
    multiSelectData.value.forEach(i => {
      ids.push(i.huiiId);
      names.push(i.huiiName);
    })
  } else {
    ids.push(row.huiiId);
    names.push(row.huiiName);
  }
  ElMessageBox.confirm(
      '将删除huii名称为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    //TODO
    deleteHuii(ids).then((res) => {
      if (res.code === 0) {
        getData();
      }
    });
  });
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
 * 上传数据
 */
const handleUpload = () => {
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