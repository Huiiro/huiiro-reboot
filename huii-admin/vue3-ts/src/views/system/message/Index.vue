<template>
  <!--formSearch-->
  <el-form :inline="true" :size="size" v-show="showSearch">
    <!--searchParam-->
    <el-form-item label="消息内容" class="global-input-item">
      <el-input v-model="query.message" placeholder="请输入消息内容"
                class="global-input" :size="size"/>
    </el-form-item>
    <el-form-item label="消息类型" class="global-input-item">
      <el-select v-model="query.messageType" placeholder="请选择消息类型"
                 :size="size">
        <el-option
            v-for="item in messageTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"/>
      </el-select>
    </el-form-item>
    <el-form-item label="消息状态" class="global-input-item">
      <el-select v-model="query.messageStatus" placeholder="请选择消息状态"
                 :size="size">
        <el-option
            v-for="item in messageStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"/>
      </el-select>
    </el-form-item>
    <el-form-item label="消息是否已读" class="global-input-item">
      <el-select v-model="query.messageRead" placeholder="请选择消息是否已读"
                 :size="size">
        <el-option
            v-for="item in messageReadStatusOptions"
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
    <el-form-item class="global-form-item-margin" v-if="checkPermission('system:notice:add')">
      <el-button :size="size" :icon="Plus" @click="handleInsert"
                 :color="layoutStore.BtnInsert" plain>发送消息
      </el-button>
    </el-form-item>
    <!--delete-->
    <el-form-item class="global-form-item-margin" v-if="checkPermission('system:message:delete')">
      <el-button :size="size" :icon="Delete" @click="handleDelete"
                 :color="layoutStore.BtnDelete" plain :disabled="selectable">删除消息
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
    <el-table-column prop="messageId" label="消息ID" align="left" min-width="120"/>
    <el-table-column prop="sendId" label="发送者" align="center" width="120"/>
    <el-table-column prop="receiveId" label="接收者" align="center" width="120"/>
    <el-table-column prop="message" label="消息" align="center" min-width="300"/>
    <el-table-column prop="messageType" label="消息类型" align="center" width="120">
      <template #default="scope">
        <el-tag v-for="tag in messageTypeOptions"
                v-show="tag.value === scope.row.messageStatus"
                :size="size"
                :key="tag.value"
                :type="tag.type"> {{ tag.label }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="messageStatus" label="消息状态" align="center" width="120">
      <template #default="scope">
        <el-tag v-for="tag in messageStatusOptions"
                v-show="tag.value === scope.row.messageStatus"
                :size="size"
                :key="tag.value"
                :type="tag.type"> {{ tag.label }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="messageRead" label="消息已读" align="center" width="120">
      <template #default="scope">
        <el-tag v-for="tag in messageReadStatusOptions"
                v-show="tag.value === scope.row.messageRead"
                :size="size"
                :key="tag.value"
                :type="tag.type"> {{ tag.label }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column v-if="showTimeColumn" prop="createTime" label="创建日期" align="center" sortable width="170"/>
    <el-table-column v-if="showTimeColumn" prop="updateTime" label="更新日期" align="center" sortable width="170"/>
    <el-table-column label="消息操作" align="center" width="200" fixed="right"
                     v-if="checkPermissions(['system:message:delete'])">
      <template #default="scope">
        <div class="display">
          <div class="display" v-if="checkPermission('system:message:delete')">
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

  <el-dialog class="global-dialog-iu"
             title="发送消息" v-model="dialogVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form :model="form"
             :rules="formRules"
             ref="formRuleRef">
      <el-form-item label="消息内容" label-width="85" prop="message">
        <el-input v-model="form.message"
                  autocomplete="off"
                  type="textarea"
                  show-word-limit
                  maxlength="255"
                  :rows="4"
        />
      </el-form-item>
      <el-form-item label="消息类型" label-width="85" prop="messageType">
        <el-select
            v-model="form.messageType"
            placeholder="请选择通知类型"
            style="width: 240px"
        >
          <el-option
              v-for="item in messageTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="接收对象" label-width="85" prop="receiveId">
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
      </el-form-item>
    </el-form>
    <div>

    </div>
    <template #footer>
      <div style="clear: both"/>
      <el-button @click="handleCloseForm">取 消</el-button>
      <el-button type="primary" @click="handleSubmitForm(formRuleRef)">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {Delete, Plus, Refresh, Search, Timer} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {checkPermission, checkPermissions} from "@/utils/permission.ts";
import {deleteMessage, getMessageList, insertMessage} from "@/api/system/message";
import {messageReadStatusOptions, messageStatusOptions, messageTypeOptions} from "@/views/system/message/dictionary.ts";
import {getUserList} from "@/api/system/user";

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
const loading2 = ref(false);
//表格数据
const tableData = ref();
//表格数据2 用户信息
const tableData2 = ref();
//查询参数
const query = ref({
  message: '',
  messageType: '',
  messageStatus: '',
  messageRead: ''
});
const query2 = ref({
  userName: ''
});

/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  getMessageList(paramBuilder(query.value, queryPage.value, null, null)).then(res => {
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
  getUserList(paramBuilder(query2.value, queryPage2.value, null, null)).then(res => {
    const response = res.data;
    tableData2.value = response.data;
    pageCurrent2.value = response.current;
    pageTotal2.value = response.total;
    pageSize2.value = response.size;
    loading2.value = false;
  })
};

/**
 * 重置查询参数按钮
 */
const handleReset = () => {
  query.value.message = '';
  query.value.messageType = '';
  query.value.messageStatus = '';
  query.value.messageRead = '';
  getData();
};

const handleReset2 = () => {
  query2.value.userName = '';
  getData2();
}

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

//分页2 用户信息
const queryPage2 = ref({
  current: 1,
  size: 10,
  total: 0
});
const pageLayout2 = ref("total, sizes, prev, pager, next");
const pageSizes2 = ref([10, 20]);
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
//多选数据2 未授权用户
const multiSelectData2 = ref();
const selectionChange2 = (value: any) => {
  multiSelectData2.value = value
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

const form = ref({
  messageId: 0,
  sendId: '',
  receiveId: '',
  message: '',
  messageType: '1',
  messageStatus: '1',
  createTime: '',
  updateTime: ''
});
const formRules = ref({
  message: [
    {required: true, message: '请输入消息内容', trigger: 'blur'},
    {min: 1, max: 60, message: '消息内容不超过255个字', trigger: 'blur'}
  ],
});
const formRuleRef = ref<FormInstance>();
//是否编辑
const isEdit = ref(false);
//对话框
const dialogVisible = ref(false);
const handleInsert = () => {
  form.value = {
    messageId: 0,
    sendId: '',
    receiveId: '',
    message: '',
    messageType: '1',
    messageStatus: '1',
    createTime: '',
    updateTime: ''
  };
  isEdit.value = false;
  dialogVisible.value = true;
  getData2();
}

const handleCloseForm = () => {
  isEdit.value = false;
  dialogVisible.value = false;
};

//@ts-ignore
const handleSubmitForm = (fr: FormInstance | undefined) => {
  let ids: any = [];
  multiSelectData2.value.forEach(i => {
    ids.push(i.userId);
  })
  form.value.receiveId = ids.toString();
  insertMessage(form.value).then(res => {
    if (res.code === 0) {
      getData();
      isEdit.value = false;
      dialogVisible.value = false;
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
      ids.push(i.messageId);
      names.push(i.messageId);
    })
  } else {
    ids.push(row.messageId);
    names.push(row.messageId);
  }
  ElMessageBox.confirm(
      '将删除ID为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteMessage(ids).then((res) => {
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