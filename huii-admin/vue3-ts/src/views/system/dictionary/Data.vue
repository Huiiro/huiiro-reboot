<template>
  <el-card>
    <!--formSearch-->
    <el-form :inline="true" :size="size" v-show="showSearch">
      <!--searchParam-->
      <el-form-item label="字典项名称" class="global-input-item">
        <el-input v-model="query.dataName" placeholder="请输入字典项名称"
                  class="global-input" :size="size"/>
      </el-form-item>
      <el-form-item label="字典状态" class="global-input-item">
        <el-select v-model="query.dataStatus" placeholder="请选择字典状态"
                   :size="size">
          <el-option
              v-for="item in dicStatusOptions"
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
        <el-button :size="size" :icon="Back" @click="handleBack"
                   :color="layoutStore.BtnBack" plain>返回
        </el-button>
      </el-form-item>
      <el-form-item class="global-form-item-margin" v-if="checkPermission('system:dic:add')">
        <el-button :size="size" :icon="Plus" @click="handleInsert"
                   :color="layoutStore.BtnInsert" plain>添加字典项
        </el-button>
      </el-form-item>
      <!--edit-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('system:dic:edit')">
        <el-button :size="size" :icon="Edit" @click="handleEdit"
                   :color="layoutStore.BtnUpdate" plain :disabled="!selectSingle">修改字典项
        </el-button>
      </el-form-item>
      <!--delete-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('system:dic:delete')">
        <el-button :size="size" :icon="Delete" @click="handleDelete"
                   :color="layoutStore.BtnDelete" plain :disabled="selectable">删除字典项
        </el-button>
      </el-form-item>
      <!--export-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('system:dic:export')">
        <el-button :size="size" :icon="Upload" @click="handleExport"
                   :color="layoutStore.BtnExport" plain>导出字典项
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
      <el-table-column prop="dataId" label="字典项ID" align="center" min-width="120"/>
      <el-table-column prop="dataName" label="字典项名称" align="center" min-width="140"/>
      <el-table-column prop="dataType" label="字典项类型" align="center" min-width="160">
        <template #default="scope">
          <el-tag style="cursor: pointer" type="info">
            {{ scope.row.dataType }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="dataKey" label="字典键(key)" align="center" width="140"/>
      <el-table-column prop="dataValue" label="字典值(value)" align="center" min-width="140"/>
      <el-table-column prop="dataLabel" label="字典标签(label)" align="center" width="140"/>
      <el-table-column prop="dataSeq" label="字典展示顺序" sortable align="center" width="160"/>
      <el-table-column prop="dataTypeInfo" label="字典标签类型" align="center" width="160">
        <template #default="scope">
          <el-tag v-for="tag in dicLabelInfoOptions"
                  v-show="tag.value === scope.row.dataTypeInfo"
                  :size="size"
                  :key="tag.value"
                  :type="tag.type"> {{ tag.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="dataStatus" label="字典状态" align="center" width="120">
        <template #default="scope">
          <el-tag v-for="tag in dicStatusOptions"
                  v-show="tag.value === scope.row.dataStatus"
                  :size="size"
                  :key="tag.value"
                  :type="tag.type"> {{ tag.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="showTimeColumn" prop="createTime" label="创建日期" align="center" sortable width="170"/>
      <el-table-column v-if="showTimeColumn" prop="updateTime" label="更新日期" align="center" sortable width="170"/>
      <el-table-column label="字典项操作" align="center" width="200" fixed="right"
                       v-if="checkPermissions(['system:dic:edit','system:dic:delete'])">
        <template #default="scope">
          <div class="display">
            <div class="display" v-if="checkPermission('system:dic:edit')">
              <el-button class="global-table-btn"
                         size="small" type="primary" link :icon="Edit"
                         @click="handleEdit(scope.$index, scope.row)">
                编辑
              </el-button>
              <el-divider direction="vertical"/>
            </div>
            <div class="display" v-if="checkPermission('system:dic:delete')">
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
  </el-card>

  <el-dialog class="global-dialog-iu"
             title="字典项管理" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form :model="form"
             :rules="formRules"
             ref="formRuleRef">
      <el-row>
        <el-col :span="11">
          <el-form-item label="字典项名称" label-width="110" prop="dataName">
            <el-input v-model="form.dataName" autocomplete="off"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="字典项类型" label-width="110" prop="dataType">
            <el-input v-model="form.dataType" autocomplete="off" disabled/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="字典键" label-width="110" prop="dataKey">
            <template v-slot:label>
              <el-tooltip
                  class="box-item"
                  effect="dark"
                  content="字典数据项的键(key)"
                  placement="top-start"
              >       <span>字典键
              <el-icon><QuestionFilled/></el-icon>
            </span>
              </el-tooltip>
            </template>
            <el-input v-model="form.dataKey" autocomplete="off"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="字典标签" label-width="110" prop="dataLabel">
            <template v-slot:label>
              <el-tooltip
                  class="box-item"
                  effect="dark"
                  content="字典数据项的标签(label)"
                  placement="top-start"
              >       <span>字典标签
              <el-icon><QuestionFilled/></el-icon>
            </span>
              </el-tooltip>
            </template>
            <el-input v-model="form.dataLabel" autocomplete="off"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="字典值" label-width="110" prop="dataLabel">
        <template v-slot:label>
          <el-tooltip
              class="box-item"
              effect="dark"
              content="字典数据项的值(value)"
              placement="top-start"
          >       <span>字典值
              <el-icon><QuestionFilled/></el-icon>
            </span>
          </el-tooltip>
        </template>
        <el-input v-model="form.dataValue" autocomplete="off"
                  type="textarea"
                  show-word-limit
                  maxlength="499"
                  :rows="2"
        />
      </el-form-item>
      <el-form-item label="字典备注" label-width="110" prop="remark">
        <el-input v-model="form.remark"
                  autocomplete="off"
                  type="textarea"
                  show-word-limit
                  maxlength="100"
                  :rows="1"
        />
      </el-form-item>
      <el-row v-show="isEdit">
        <el-col :span="11">
          <el-form-item label="创建时间" label-width="110" prop="createTime">
            <el-input v-model="form.createTime" autocomplete="off" readonly="readonly"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="更新时间" label-width="110" prop="updateTime">
            <el-input v-model="form.updateTime" autocomplete="off" readonly="readonly"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="字典值顺序" label-width="110" prop="roleSeq">
            <el-input-number v-model="form.dataSeq"
                             :min="1" :max="99"
                             controls-position="right"
                             autocomplete="off"
                             style="width: 100%"/>
          </el-form-item>
        </el-col>
        <el-col :span="2"/>
        <el-col :span="11">
          <el-form-item label="字典标签类型" label-width="110" prop="dataTypeInfo">
            <el-select v-model="form.dataTypeInfo" placeholder="请选择字典标签类型" size="default">
              <el-option
                  v-for="item in dicLabelInfoOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="字典状态" label-width="110" prop="dataStatus">
            <el-radio-group v-model="form.dataStatus">
              <el-radio v-for="option in dicStatusOptions" :key="option.value" :label="option.value">
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
import {computed, onMounted, ref} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {Back, Delete, Edit, Plus, QuestionFilled, Refresh, Search, Timer, Upload,} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {dicLabelInfoOptions, dicStatusOptions} from "@/views/system/dictionary/dictionary.ts";
import {
  deleteDicData,
  exportDicData,
  getDicDataList,
  getDicDataSingleton,
  insertDicData,
  updateDicData
} from "@/api/system/dic/data";
import {useRoute} from "vue-router";
import {checkPermission, checkPermissions} from "@/utils/permission.ts";
import router from "@/router";
import {downloadExport} from "@/utils/download.ts";

//store
const layoutStore = useLayoutStore();
//布局
const size = layoutStore.tableSize;
const pageLayoutSize = computed(() => {
  return size === 'small';
});

const dicTypeId = ref();
const route = useRoute();
//mounted
onMounted(() => {
  dicTypeId.value = route.params.typeId;
  query.value.dataType = dicTypeId;
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
  dataType: '',
  dataName: '',
  dataKey: '',
  dataStatus: ''
});

/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  getDicDataList(paramBuilder(query.value, queryPage.value, null, null)).then(res => {
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
  query.value.dataType = '';
  query.value.dataName = '';
  query.value.dataKey = '';
  query.value.dataStatus = '';
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
  dataId: 0,
  dataType: '',
  dataName: '',
  dataKey: '',
  dataValue: '',
  dataLabel: '',
  dataTypeInfo: '',
  dataStatus: '',
  dataSeq: '',
  remark: '',
  createTime: '',
  updateTime: ''
});
//表单数据校验规则
const formRules = ref({
  dataName: [
    {required: true, message: '请输入数据项名称', trigger: 'blur'},
    {min: 1, max: 20, message: '数据项名称长度在 1 至 20 个字符之间', trigger: 'blur'}
  ],
  dataKey: [
    {required: true, message: '请输入字典键(key)', trigger: 'blur'},
    {min: 1, max: 20, message: '字典键(key)长度在 1 至 20 个字符之间', trigger: 'blur'}
  ],
  dataValue: [
    {required: true, message: '请输入字典值(value)', trigger: 'blur'},
    {min: 1, max: 20, message: '字典值(value)长度在 1 至 20 个字符之间', trigger: 'blur'}
  ],
  dataLabel: [
    {required: true, message: '请输入字典标签(label)', trigger: 'blur'},
    {min: 1, max: 20, message: '字典标签(label)长度在 1 至 20 个字符之间', trigger: 'blur'}
  ],
  dataTypeInfo: [
    {required: true, message: '请输入字典标签类型', trigger: 'blur'},
    {min: 1, max: 20, message: '字典标签类型长度在 1 至 20 个字符之间', trigger: 'blur'}
  ],
  dataSeq: [
    {required: true, message: '请选择字典顺序', trigger: 'blur'},
  ],
  dataStatus: [
    {required: true, message: '请选择字典状态', trigger: 'blur'},
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
  updateDicData(form.value).then((res) => {
    if (res.code === 0) {
      isEdit.value = false;
      dialogVisible.value = false;
      getData();
    }
  });
};
const doInsert = () => {
  insertDicData(form.value).then((res) => {
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
    dataId: 0,
    dataKey: "",
    dataLabel: "",
    dataName: "",
    dataSeq: "",
    dataStatus: "",
    dataType: "",
    dataTypeInfo: "",
    dataValue: "",
    remark: "",
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
  let id;
  if (row == null) {
    id = multiSelectData.value[0].dataId;
  } else {
    id = row.dataId;
  }
  getDicDataSingleton(id).then(res => {
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
      ids.push(i.dataId);
      names.push(i.dataName);
    })
  } else {
    ids.push(row.dataId);
    names.push(row.dataName);
  }
  ElMessageBox.confirm(
      '将删除数据字典项名称为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteDicData(ids).then((res) => {
      if (res.code === 0) {
        getData();
      }
    });
  }).catch();
};

/**
 * 导出数据
 */
const handleExport = () => {
  exportDicData(null).then(res => {
    downloadExport(res);
  });
};


const handleBack = () => {
  router.back()
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