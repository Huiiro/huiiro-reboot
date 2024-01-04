<template>
  <el-card>
    <!--formSearch-->
    <el-form :inline="true" :size="size" v-show="showSearch">
      <!--searchParam-->
      <el-form-item label="文件名称" class="global-input-item">
        <el-input v-model="query.fileName" placeholder="请输入文件名称"
                  class="global-input" :size="size"/>
      </el-form-item>
      <el-form-item label="文件位置" class="global-input-item">
        <el-select v-model="query.fileServer" placeholder="请选择文件位置"
                   :size="size">
          <el-option
              v-for="item in fileServer"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="文件状态" class="global-input-item">
        <el-select v-model="query.fileStatus" placeholder="请选择文件状态"
                   :size="size">
          <el-option
              v-for="item in fileStatus"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="文件权限" class="global-input-item">
        <el-select v-model="query.fileAcl" placeholder="请选择文件权限"
                   :size="size">
          <el-option
              v-for="item in fileAcl"
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
      <!--delete-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('system:file:delete')">
        <el-button :size="size" :icon="Delete" @click="handleDelete"
                   :color="layoutStore.BtnDelete" plain :disabled="selectable">删除文件
        </el-button>
      </el-form-item>
      <!--upload-->
      <el-form-item class="global-form-item-margin" v-if="checkPermission('system:file:upload')">
        <el-button :size="size" :icon="UploadFilled" @click="handleUpload"
                   :color="layoutStore.BtnUpload" plain>上传文件
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
      <el-table-column prop="fileView" label="文件阅览" align="center" width="120">
        <template #default="scope">
          <el-image :src="scope.row.accessUrl" alt="">
            <template #error>
              <div class="image-slot">
                暂不支持查看
              </div>
            </template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="fileName" label="文件名称" align="center" min-width="120"/>
      <el-table-column prop="originName" label="文件原名" align="center" min-width="120"/>
      <el-table-column prop="accessUrl" label="文件直链" align="center" min-width="120"/>
      <el-table-column prop="fileSize" label="文件大小" align="center" min-width="80"/>
      <el-table-column prop="fileServer" label="文件位置" align="center" min-width="80"/>
      <el-table-column prop="fileAcl" label="文件权限" align="center" min-width="80">
        <template #default="scope">
          <el-switch v-model="scope.row.fileAcl"
                     active-value="pub" inactive-value="pri"
                     @click="handleUpdateFileAcl(scope.$index, scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="fileStatus" label="文件状态" align="center" min-width="80">
        <template #default="scope">
          <el-switch v-model="scope.row.fileStatus"
                     active-value="1" inactive-value="0"
                     @click="handleUpdateFileStatus(scope.$index, scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column v-if="showTimeColumn" prop="createTime" label="创建日期" align="center" sortable width="170"/>
      <el-table-column v-if="showTimeColumn" prop="updateTime" label="更新日期" align="center" sortable width="170"/>
      <el-table-column label="文件操作" align="center" width="160" fixed="right"
                       v-if="checkPermissions(['system:file:edit','system:file:delete'])">
        <template #default="scope">
          <div class="display">
            <div class="display" v-if="checkPermission('system:file:edit')">
              <el-button class="global-table-btn"
                         size="small" type="primary" link :icon="Edit"
                         @click="handleDownload(scope.$index, scope.row)">
                下载
              </el-button>
              <el-divider direction="vertical"/>
            </div>
            <div class="display" v-if="checkPermission('system:file:delete')">
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
             title="上传文件" v-model="uploadVisible"
             :close-on-click-modal="false"
             @close="handleCloseForm">
    <el-form>
      <el-form-item label="上传文件至">
        <el-select v-model="uploadType" placeholder="Select">
          <el-option
              v-for="item in [{value: 'local', label: '本地(local)'}, {value: 'oss', label: '云(oss)'}]"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <div v-if="uploadType === 'local'">
      <upload :url="localUrl"
              :show-list="false"
              :size=10
              type=""/>
    </div>
    <div v-if="uploadType === 'oss'">
      <upload :url="ossUrl"
              :show-list="false"
              :size=10
              type=""/>
    </div>
  </el-dialog>

</template>

<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {ElMessage, ElMessageBox} from "element-plus";
import {paramBuilder} from "@/utils/common.ts";
import {deleteSysFile, getSysFileList, updateSysFileAcl, updateSysFileStatus, uploadSysFile} from "@/api/system/file";
import {checkPermission, checkPermissions} from "@/utils/permission.ts";
import {Delete, Edit, Refresh, Search, Timer, Upload, UploadFilled} from "@element-plus/icons-vue";
import upload from '@/components/upload/Index.vue';
import {download} from "@/utils/download.ts";
import {fileAcl, fileServer, fileStatus} from "@/views/system/file/dictionary.ts";

//store
const layoutStore = useLayoutStore();
//layout
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
  fileName: '',
  fileServer: '',
  fileAcl: '',
  fileStatus: '',
});

/**
 * 查询数据
 */
const getData = () => {
  loading.value = true;
  getSysFileList(paramBuilder(query.value, queryPage.value, null, null)).then(res => {
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
  query.value.fileName = '';
  query.value.fileServer = '';
  query.value.fileAcl = '';
  query.value.fileStatus = '';
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
 * 关闭表单
 */
const handleCloseForm = () => {
  uploadVisible.value = false;
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
      ids.push(i.fileId);
      names.push(i.fileName);
    })
  } else {
    ids.push(row.fileId);
    names.push(row.fileName);
  }
  ElMessageBox.confirm(
      '将删除ID为  \'' + names.toString().substring(0, 40) + '\'  的' + names.length + '项数据，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    deleteSysFile(ids).then((res) => {
      if (res.code === 0) {
        getData();
      }
    });
  }).catch();
};

/**
 * 上传文件
 */
const uploadVisible = ref(false);
const uploadType = ref('local');

const localUrl = ref(import.meta.env.VITE_BASE_URL + uploadSysFile() + '/local');
const ossUrl = ref(import.meta.env.VITE_BASE_URL + uploadSysFile() + '/oss');
const handleUpload = () => {
  uploadVisible.value = true;
}

/**
 * 更新文件权限
 * @param index
 * @param row
 */
//@ts-ignore
const handleUpdateFileAcl = (index, row) => {
  updateSysFileAcl(row).then(res => {
    if (res.code === 0) {
      getData();
    }
  })
}

/**
 * 更新文件状态
 * @param index
 * @param row
 */
//@ts-ignore
const handleUpdateFileStatus = (index, row) => {
  updateSysFileStatus(row).then(res => {
    if (res.code === 0) {
      getData();
    }
  })
}

/**
 * 下载文件
 * @param index
 * @param row
 */
//@ts-ignore
const handleDownload = (index, row) => {
    download(row.accessUrl, row.originName);
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

.image-slot {
  color: #868686;
}
</style>