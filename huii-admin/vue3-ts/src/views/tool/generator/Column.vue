<template>
  <el-tabs type="border-card" class="demo-tabs">
    <el-tab-pane label="列（字段）信息">
      <el-table :data="columns"
                v-loading="loading"
                :highlight-current-row="true"
                header-cell-class-name="global-table-header"
                class="global-table"
                stripe>
        <el-table-column prop="columnName" label="字段" align="left" min-width="140"/>
        <el-table-column prop="javaField" label="实体类字段类型" align="left" min-width="140">
          <template #default="scope">
            <el-input v-model="scope.row.javaField" placeholder="实体类字段类型"/>
          </template>
        </el-table-column>
        <el-table-column prop="columnComment" label="字段注释" align="left" min-width="140">
          <template #default="scope">
            <el-input v-model="scope.row.columnComment" placeholder="输入字段注释"/>
          </template>
        </el-table-column>
        <el-table-column prop="sqlType" label="数据库字段类型" align="left" min-width="170">
          <template #default="scope">
            <span>{{ scope.row.sqlType }}
              <span v-if="scope.row.sqlCharLength">
                ({{ scope.row.sqlCharLength }})
              </span>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="javaType" label="实体类字段类型" align="left" min-width="170">
          <template #default="scope">
            <el-select v-model="scope.row.javaType" placeholder="选择实体类字段类型" style="width: 100%">
              <el-option
                  v-for="item in javaTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"/>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="isPrimaryKey" label="是否主键" align="center" width="80">
          <template #default="scope">
            <el-checkbox v-model="scope.row.isPrimaryKey" :true-label="'1'" :false-label="'0'" label=""/>
          </template>
        </el-table-column>
        <el-table-column prop="isIncrement" label="是否自增" align="center" width="80">
          <template #default="scope">
            <el-checkbox v-model="scope.row.isIncrement" :true-label="'1'" :false-label="'0'" label=""/>
          </template>
        </el-table-column>
        <el-table-column prop="isRequired" label="是否校验非空" align="center" width="110">
          <template #default="scope">
            <el-checkbox v-model="scope.row.isRequired" :true-label="'1'" :false-label="'0'" label=""/>
          </template>
        </el-table-column>
        <el-table-column prop="isRequired" label="是否不可重复" align="center" width="110">
          <template #default="scope">
            <el-checkbox v-model="scope.row.checkUnique" :true-label="'1'" :false-label="'0'" label=""/>
          </template>
        </el-table-column>
        <el-table-column prop="isQueryField" label="前端是否查询" align="center" width="110">
          <template #default="scope">
            <el-checkbox v-model="scope.row.isQueryField" :true-label="'1'" :false-label="'0'" label=""/>
          </template>
        </el-table-column>
        <el-table-column prop="queryType" label="后端查询方式" align="left" min-width="150">
          <template #default="scope">
            <el-select clearable v-model="scope.row.queryType" placeholder="选择查询方式" style="width: 100%">
              <el-option
                  v-for="item in wrapperTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"/>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="formType" label="表单数据类型" align="left" min-width="150">
          <template #default="scope">
            <el-select clearable v-model="scope.row.formType" placeholder="选择数据类型" style="width: 100%">
              <el-option
                  v-for="item in formTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"/>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="dicType" label="字典类型" align="left" min-width="150">
          <template #default="scope">
            <el-select clearable v-model="scope.row.dicType" placeholder="选择字典类型" style="width: 100%">
              <el-option
                  v-for="item in dicData"
                  :key="item.id"
                  :label="item.label"
                  :value="item.id"/>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="80" fixed="right" v-if="false">
          <template #default="scope">
            <div class="display">
              <el-button class="global-table-btn"
                         size="small" type="primary" link :icon="Plus"
                         @click="handleMoreInfo(scope.$index, scope.row)">更多
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <el-tab-pane label="生成（表）信息">
      <el-form :model="form"
               :rules="formRules"
               ref="formRuleRef"
               label-width="120px"
               v-loading="loading2">
        <div>
          <span>表基础信息</span>
          <el-divider class="divider"/>
          <el-row>
            <el-col :span="11">
              <el-form-item label="表名称" prop="tableName" class="global-input-item">
                <el-input v-model="form.tableName" autocomplete="off" placeholder="请输入表名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"/>
            <el-col :span="11">
              <el-form-item label="表注释(描述)" prop="tableComment" class="global-input-item">
                <el-input v-model="form.tableComment" autocomplete="off" placeholder="请输入表注释"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="实体类名称" prop="className" class="global-input-item">
                <template v-slot:label>
                  <el-tooltip
                      class="box-item"
                      effect="dark"
                      content="实体类名称，例如SysUser"
                      placement="top-start"
                  ><span>实体类名称
              <el-icon><QuestionFilled/></el-icon>
            </span>
                  </el-tooltip>
                </template>
                <el-input v-model="form.className" autocomplete="off" placeholder="请输入实体类名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"/>
            <el-col :span="11">
              <el-form-item label="变量名称" prop="variableName" class="global-input-item">
                <template v-slot:label>
                  <el-tooltip
                      class="box-item"
                      effect="dark"
                      content="变量名称，通常为实体类首字母小写，例如SysUser可以写为sysUser"
                      placement="top-start"
                  ><span>变量名称
              <el-icon><QuestionFilled/></el-icon>
            </span>
                  </el-tooltip>
                </template>
                <el-input v-model="form.variableName" autocomplete="off" placeholder="请输入变量名称"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="权限字符串" prop="authPrefix" class="global-input-item">
                <template v-slot:label>
                  <el-tooltip
                      class="box-item"
                      effect="dark"
                      content="权限字符串前缀，仅匹配前缀，不匹配完整的权限字符串，例如system:user"
                      placement="top-start"
                  ><span>权限字符串
                      <el-icon><QuestionFilled/></el-icon>
                    </span>
                  </el-tooltip>
                </template>
                <el-input v-model="form.authPrefix" autocomplete="off"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"/>
            <el-col :span="11">
              <el-form-item label="请求路径" prop="requestUrl" class="global-input-item">
                <template v-slot:label>
                  <el-tooltip
                      class="box-item"
                      effect="dark"
                      content="控制层请求路径前缀，以'/'开头，例如/system/user"
                      placement="top-start"
                  ><span>请求路径
                      <el-icon><QuestionFilled/></el-icon>
                    </span>
                  </el-tooltip>
                </template>
                <el-input v-model="form.requestUrl" autocomplete="off"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="备注" class="global-input-item">
                <el-input v-model="form.remark" autocomplete="off"
                          type="textarea" :rows="1"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"/>
            <el-col :span="11"/>
          </el-row>
        </div>
        <div>
          <span>生成信息</span>
          <el-divider class="divider"/>
          <el-row>
            <el-col :span="11">
              <el-form-item label="生成模板" prop="tableTemplate" class="global-input-item">
                <el-select v-model="form.tableTemplate" placeholder="请选择生成模板类型" style="width: 100%">
                  <el-option
                      v-for="item in genTemplateOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="生成包路径" prop="packageName" class="global-input-item">
                <template v-slot:label>
                  <el-tooltip
                      class="box-item"
                      effect="dark"
                      content="生成包路径，例如com.huii.system"
                      placement="top-start"
                  ><span>生成包路径
              <el-icon><QuestionFilled/></el-icon>
            </span>
                  </el-tooltip>
                </template>
                <el-input v-model="form.packageName" placeholder="请输入生成包路径"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"/>
            <el-col :span="11">
              <el-form-item label="作者名称" prop="authorName" class="global-input-item">
                <el-input v-model="form.authorName" placeholder="请输入作者名称"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="前端版本" prop="frontendType" class="global-input-item">
                <template v-slot:label>
                  <el-tooltip
                      class="box-item"
                      effect="dark"
                      content="前端版本分为vue2和vue3版本，目前仅支持vue3"
                      placement="top-start"
                  ><span>前端版本
              <el-icon><QuestionFilled/></el-icon>
            </span>
                  </el-tooltip>
                </template>
                <el-select v-model="form.frontendType" placeholder="请选择前端版本" style="width: 100%">
                  <el-option
                      v-for="item in genFrontendTypeOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="2"/>
            <el-col :span="11">
              <el-form-item label="数据库类型" prop="sqlType" class="global-input-item">
                <template v-slot:label>
                  <el-tooltip
                      class="box-item"
                      effect="dark"
                      content="数据库类型，目前仅支持pgsql"
                      placement="top-start"
                  ><span>数据库类型
              <el-icon><QuestionFilled/></el-icon>
            </span>
                  </el-tooltip>
                </template>
                <el-select v-model="form.sqlType" placeholder="请选择数据库类型" style="width: 100%">
                  <el-option
                      v-for="item in genSqlTypeOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="模块名称" prop="moduleName" class="global-input-item">
                <template v-slot:label>
                  <el-tooltip
                      class="box-item"
                      effect="dark"
                      content="模块名称，该类所属的模块，仅支持一级分类。例如system模块"
                      placement="top-start"
                  ><span>模块名称
              <el-icon><QuestionFilled/></el-icon>
            </span>
                  </el-tooltip>
                </template>
                <el-input v-model="form.moduleName" placeholder="请输入模块名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"/>
            <el-col :span="11"/>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="类作用描述" prop="moduleFunctionDesc" class="global-input-item">
                <template v-slot:label>
                  <el-tooltip
                      class="box-item"
                      effect="dark"
                      content="描述该类的作用"
                      placement="top-start"
                  ><span>类作用描述
              <el-icon><QuestionFilled/></el-icon>
            </span>
                  </el-tooltip>
                </template>
                <el-input v-model="form.moduleFunctionDesc" autocomplete="off" placeholder="请输入类作用描述"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"/>
            <el-col :span="11">
              <el-form-item label="类作用描述" prop="moduleFunctionName" class="global-input-item">
                <template v-slot:label>
                  <el-tooltip
                      class="box-item"
                      effect="dark"
                      content="述该类的名称的英文"
                      placement="top-start"
                  ><span>类作用名称
              <el-icon><QuestionFilled/></el-icon>
            </span>
                  </el-tooltip>
                </template>
                <el-input v-model="form.moduleFunctionName" autocomplete="off" placeholder="请输入类作用描述"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="生成接口类型" class="global-input-item">
                <el-checkbox v-model="form.genAddInterface" :true-label="'1'" :false-label="'0'" label="新增接口"/>
                <el-checkbox v-model="form.genEditInterface" :true-label="'1'" :false-label="'0'" label="修改接口"/>
                <el-checkbox v-model="form.genDeleteInterface" :true-label="'1'" :false-label="'0'" label="删除接口"/>
                <el-checkbox v-model="form.genImportInterface" :true-label="'1'" :false-label="'0'" label="导入接口"/>
                <el-checkbox v-model="form.genExportInterface" :true-label="'1'" :false-label="'0'" label="导出接口"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"/>
            <el-col :span="11">
              <el-form-item label="上级菜单" prop="parentMenuId" class="global-input-item">
                <el-tree-select v-model="form.parentMenuId"
                                placeholder="请选择上级菜单"
                                :props="props"
                                :data="selectData"
                                check-strictly
                                style="width: 100%;"/>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div v-show="form.tableTemplate === '2'">
          <span>树表信息</span>
          <el-divider class="divider"/>
          <el-row>
            <el-col :span="11">
              <el-form-item label="树ID字段" prop="treeId" class="global-input-item">
                <el-select v-model="form.treeId" placeholder="请选择树ID字段" style="width: 100%">
                  <el-option
                      v-for="item in columns"
                      :key="item.columnId"
                      :label="item.columnName"
                      :value="item.columnName"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="2"/>
            <el-col :span="11">
              <el-form-item label="树名称字段" prop="treeLabelName" class="global-input-item">
                <el-select v-model="form.treeLabelName" placeholder="请选择树名称字段" style="width: 100%">
                  <el-option
                      v-for="item in columns"
                      :key="item.columnId"
                      :label="item.columnName"
                      :value="item.columnName"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div v-show="form.tableTemplate === '3'">
          <span>关联子表信息</span>
          <el-divider class="divider"/>
          <el-row>
            <el-col :span="11">
              <el-form-item label="子表名称" prop="subTableName" class="global-input-item">
                <el-select v-model="form.subTableName" placeholder="请选择树名称字段" style="width: 100%"
                           @change="selectChange">
                  <el-option
                      v-for="item in subTables"
                      :key="item.tableId"
                      :label="item.tableName"
                      :value="item.tableName"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="2"/>
            <el-col :span="11">
              <el-form-item label="子表外键" prop="subTableForeignKey" class="global-input-item">
                <el-select v-model="form.subTableForeignKey" placeholder="请选择树名称字段" style="width: 100%">
                  <el-option
                      v-for="item in subTableColumns"
                      :key="item.columnId"
                      :label="item.columnName"
                      :value="item.columnName"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div style="display: flex;justify-content: center;">
          <el-button style="width: 240px;height:36px;margin:20px 0" type="primary"
                     @click="handleSubmitForm(formRuleRef)">确 定
          </el-button>
        </div>
      </el-form>
    </el-tab-pane>
  </el-tabs>

  <el-dialog class="global-dialog-iu"
             title="更多信息" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form>
      <el-form-item label="校验长度" label-width="100" prop="checkSize">
        <el-input v-model="currentColumn.checkSize"/>
      </el-form-item>
      <el-form-item label="校验重复" label-width="100" prop="checkUnique">
        <el-input v-model="currentColumn.checkUnique"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleSubmitMoreForm">确 定</el-button>
      <el-button @click="handleCloseForm">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {onMounted, ref, watch} from "vue";
import {useRoute} from "vue-router";
import {
  getGenTableDbList,
  getGenTableSingleton,
  getGenTableSingletonByName,
  updateGenTable
} from "@/api/tool/generator";
import {Plus, QuestionFilled} from "@element-plus/icons-vue";
import {
  formTypes,
  genFrontendTypeOptions,
  genSqlTypeOptions,
  genTemplateOptions,
  javaTypes,
  wrapperTypes
} from "@/views/tool/generator/dictionary.ts";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {getMenuSelect} from "@/api/system/menu";
import {FormInstance} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {getDicTypeLabel} from "@/api/system/dic/type";
//props
const props = {
  value: 'id',
  label: 'label',
  children: 'children',
}
//获取下拉框数据
//下拉框数据
const selectData = ref();
const getSelect = () => {
  getMenuSelect().then(res => {
    selectData.value = res.data
  });
};
const dicData = ref();
const getDicData = () => {
  getDicTypeLabel().then(res => {
    dicData.value = res.data
  })
}

//store
const layoutStore = useLayoutStore();
//布局
const size = layoutStore.tableSize;

const route = useRoute();
const tableId = ref();
onMounted(() => {
  tableId.value = route.params.tableId;
  getData();
  getSelect();
  getDicData();
})


const loading = ref(false);
const loading2 = ref(false);
const data = ref();
const columns = ref();

const getData = () => {
  loading.value = true;
  getGenTableSingleton(tableId.value).then(res => {
    const response = res.data;
    //获取列信息至table
    data.value = response;
    form.value = data.value;
    //initForm();
    columns.value = response.columns;
    loading.value = false;
  })
}

const form = ref({
  tableId: tableId,
  tableName: '',
  tableComment: '',
  tableTemplate: '',
  authPrefix: '',
  requestUrl: '',
  className: '',
  variableName: '',
  packageName: '',
  authorName: '',
  frontendType: '',
  sqlType: '',
  moduleName: '',
  moduleFunctionDesc: '',
  moduleFunctionName: '',
  genAddInterface: '',
  genEditInterface: '',
  genDeleteInterface: '',
  genImportInterface: '',
  genExportInterface: '',
  parentMenuId: 0,
  subTableName: '',
  subTableForeignKey: '',
  treeId: 0,
  treeLabelName: '',
  remark: '',
  createBy: '',
  createTime: '',
  updateBy: '',
  updateTime: ''
});
const initForm = () => {
  form.value.frontendType = '2';
  form.value.sqlType = '2';
  form.value.tableTemplate = '1';
  form.value.authorName = 'huii';
  form.value.genAddInterface = '1';
  form.value.genEditInterface = '1';
  form.value.genDeleteInterface = '1';
  form.value.genImportInterface = '0';
  form.value.genExportInterface = '0';
}
const validateTreeId = (rule, value, callback) => {
  if (form.value.tableTemplate === '2') {
    if (!value) {
      callback(new Error('请选择树ID字段'));
    } else {
      callback();
    }
  } else {
    callback();
  }
};
const validateTreeLabelName = (rule, value, callback) => {
  if (form.value.tableTemplate === '2') {
    if (!value) {
      callback(new Error('请选择树名称字段'));
    } else {
      callback();
    }
  } else {
    callback();
  }
};
const validateSubTableName = (rule, value, callback) => {
  if (form.value.tableTemplate === '3') {
    if (!value) {
      callback(new Error('请选择关联子表名称'));
    } else {
      callback();
    }
  } else {
    callback();
  }
};
const validateSubTableForeignKey = (rule, value, callback) => {
  if (form.value.tableTemplate === '3') {
    if (!value) {
      callback(new Error('请选择关联子表外键'));
    } else {
      callback();
    }
  } else {
    callback();
  }
};
const formRules = ref({
  tableName: [
    {required: true, message: '表名称不为空', trigger: 'blur'},
  ],
  tableTemplate: [
    {required: true, message: '表生成模板不为空', trigger: 'blur'},
  ],
  className: [
    {required: true, message: '实体类名称不为空', trigger: 'blur'},
  ],
  variableName: [
    {required: true, message: '变量名称不为空', trigger: 'blur'},
  ],
  authPrefix: [
    {required: true, message: '权限字符串不为空', trigger: 'blur'},
  ],
  requestUrl: [
    {required: true, message: '请求路径不为空', trigger: 'blur'},
  ],
  authorName: [
    {required: true, message: '作者名称不为空', trigger: 'blur'},
  ],
  packageName: [
    {required: true, message: '包路径不为空', trigger: 'blur'},
  ],
  frontendType: [
    {required: true, message: '请选择前端版本', trigger: 'blur'},
  ],
  sqlType: [
    {required: true, message: '请选择数据库类型', trigger: 'blur'},
  ],
  moduleName: [
    {required: true, message: '请输入模块名称', trigger: 'blur'},
  ],
  treeId: [
    {validator: validateTreeId, trigger: 'blur'}
  ],
  treeLabelName: [
    {validator: validateTreeLabelName, trigger: 'blur'}
  ],
  subTableName: [
    {validator: validateSubTableName, trigger: 'blur'}
  ],
  subTableForeignKey: [
    {validator: validateSubTableForeignKey, trigger: 'blur'}
  ]
});

//表单校验ref
const formRuleRef = ref<FormInstance>();

/**
 * 提交表单
 * @param fr
 */
const handleSubmitForm = async (fr: FormInstance | undefined) => {
  //@ts-ignore
  await fr.validate((valid) => {
    if (valid) {
      updateGenTable(form.value).then(res => {
        console.log(res)
      })
    }
  });
}

/**
 * 获取树表和子表数据
 */
const subTables = ref();
const subTableColumns = ref();
const queryPage = ref({
  current: 1,
  size: 99,
  total: 0
});
watch(() => form.value.tableTemplate, () => {
  if (form.value.tableTemplate === '3' && (subTables.value === undefined)) {
    loading2.value = true;
    getGenTableDbList(paramBuilder({}, queryPage.value, null, null)).then(res => {
      subTables.value = res.data.data;
      loading2.value = false;
    });
  }
});
const selectChange = (value: any) => {
  getGenTableSingletonByName(value).then(res => {
    form.value.subTableForeignKey = '';
    subTableColumns.value = res.data
  })
}

/**
 * deprecated
 * drop checkSize
 */
const dialogVisible = ref(false);
const currentColumn = ref();
const model = ref({
  checkSize: '',
  sizeMin: '',
  sizeMax: '',
  sizeMessage: '',
  checkUnique: ''
});
const handleMoreInfo = (index, row) => {
  currentColumn.value = row;
  dialogVisible.value = true;
}
const handleCloseForm = () => {
  dialogVisible.value = false;
};
const handleSubmitMoreForm = () => {

}
</script>

<style scoped lang="scss">
.divider {
  margin-top: 10px;
}

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