<template>
  <el-tabs type="border-card">
    <el-tab-pane label="缓存监控">
      <div class="main-info cache">
        <el-card>
          <el-divider/>
          <el-row>
            <el-col :span="6">
              Redis版本： {{ cacheProperties.redis_version }}
            </el-col>
            <el-col :span="6">
              运行时间(秒)： {{ cacheProperties.uptime_in_seconds }}
            </el-col>
            <el-col :span="6">
              运行时间(天)： {{ cacheProperties.uptime_in_days }}
            </el-col>
            <el-col :span="6">
              运行模式： {{ cacheProperties.redis_mode === 'standalone' ? '单机部署' : '集群部署' }}
            </el-col>
          </el-row>
          <el-divider/>
          <el-row>
            <el-col :span="6">
              连接客户端数量： {{ cacheProperties.connected_clients }}
            </el-col>
            <el-col :span="6">
              阻塞客户端数量：{{ cacheProperties.blocked_clients }}
            </el-col>
            <el-col :span="6">
              是否启用集群：{{ cacheProperties.cluster_enabled === '0' ? '否' : '是' }}
            </el-col>
            <el-col :span="6">
              集群节点数量：<span v-if="cacheProperties.cluster_size">{{ cacheProperties.cluster_size }}</span><span
                v-else>N/A</span>
            </el-col>
          </el-row>
          <el-divider/>
          <el-row>
            <el-col :span="6">
              使用内存： {{ cacheProperties.used_memory_human }}
            </el-col>
            <el-col :span="6">
              使用CPU(%)： {{ parseFloat(cacheProperties.used_cpu_user_children).toFixed(3) }}
            </el-col>
            <el-col :span="6">
              网络出口：{{ cacheProperties.instantaneous_output_kbps }}kps
            </el-col>
            <el-col :span="6">
              网络入口： {{ cacheProperties.instantaneous_input_kbps }}kps
            </el-col>
          </el-row>
          <el-divider/>
          <el-row>
            <el-col :span="6">
              是否开启AOF： {{ cacheProperties.aof_enabled === '0' ? '否' : '是' }}
            </el-col>
            <el-col :span="6">
              是否开启RDB： {{ cacheProperties.rdb_last_bgsave_status === 'ok' ? '是' : '否' }}
            </el-col>
            <el-col :span="6">
              key数量： {{ cacheSize }}
            </el-col>
            <el-col :span="6">
            </el-col>
          </el-row>
          <el-divider/>
        </el-card>
      </div>
      <div class="db-info cache" v-if="false">
        <el-card>
          <el-row>
            <el-col :span="12">
              DB0: {{ cacheProperties.db0 }}
            </el-col>
          </el-row>
          <el-divider/>
        </el-card>
      </div>
      <div class="ec-info">
        <el-card class="chart-card">
          <template #header>
            <el-icon>
              <Odometer/>
            </el-icon>
            命令信息
          </template>
          <div ref="command" class="chart"/>
        </el-card>
        <el-card class="chart-card">
          <template #header>
            <el-icon>
              <Odometer/>
            </el-icon>
            内存信息
          </template>
          <div ref="memory" class="chart"/>
        </el-card>
      </div>
    </el-tab-pane>
    <el-tab-pane label="缓存数据">
      <el-card>
        <el-form :inline="true" :size="size">
          <el-form-item class="global-form-item-margin" v-if="checkPermission('system:monitor:cache')">
            <el-button :size="size" :icon="Refresh" @click="handleCleanAll"
                       :color="layoutStore.BtnClean" plain>清空缓存
            </el-button>
          </el-form-item>
          <el-form-item class="global-form-item-margin" v-if="checkPermission('system:monitor:cache')">
            <el-button :size="size" :icon="Refresh" @click="handleRefresh"
                       :color="layoutStore.BtnClean" plain>刷新缓存
            </el-button>
          </el-form-item>
        </el-form>
        <el-row>
          <el-col :span="11">
            <el-table :data="caches"
                      size="small"
                      @row-click="handleRowClickName">
              <el-table-column prop="cacheName" label="缓存名称" align="left" min-width="120"/>
              <el-table-column prop="cacheRemark" label="缓存备注" align="left" min-width="120"/>
              <el-table-column label="操作" align="center" width="120">
                <template #default="scope">
                  <el-button class="global-table-btn red"
                             size="small" type="primary" link :icon="Delete"
                             @click.stop="handleDeleteKey(scope.$index, scope.row)">
                    清空
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
          <el-col :span="1"/>
          <el-col :span="11">
            <el-table :data="cacheKeys"
                      v-loading="loadingKey"
                      @row-click="handleRowClickKey">
              <el-table-column label="序号" width="120">
                <template #default="scope">
                  {{ scope.$index + 1 }}
                </template>
              </el-table-column>
              <el-table-column label="缓存键名">
                <template #default="scope">
                  {{ scope.row }}
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="120">
                <template #default="scope">
                  <el-button class="global-table-btn red"
                             size="small" type="primary" link :icon="Delete"
                             @click.stop="handleDeleteValue(scope.$index, scope.row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </el-card>
    </el-tab-pane>
  </el-tabs>

  <el-dialog v-model="dialogVisible"
             @close="handleCloseForm">
    <el-form>
      <el-form-item label="缓存键" label-position="top">
        {{ cacheData.cacheKey }}
      </el-form-item>
      <el-form-item label="缓存值" label-position="top">
        <el-input v-model="cacheData.cacheValue" type="textarea" rows="20"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleCloseForm">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {
  deleteCacheKey,
  deleteCaches,
  deleteCacheValue,
  getCacheInfo,
  getCacheKey,
  getCacheKeyValue,
  getCacheList
} from "@/api/monitor/cache";
import {Delete, Odometer, Refresh} from "@element-plus/icons-vue";
import echarts from "@/utils/echarts.ts";
import {checkPermission} from "@/utils/permission.ts";
import {useLayoutStore} from "@/store/modules/layout.ts";
import {ElMessage, ElMessageBox} from "element-plus";

//store
const layoutStore = useLayoutStore();
//布局
const size = layoutStore.tableSize;
onMounted(() => {
  init();
  getCaches();
})

const cacheSize = ref({});
const cacheProperties = ref({
  redis_version: '',
  uptime_in_seconds: '',
  uptime_in_days: '',
  redis_mode: '',
  connected_clients: '',
  blocked_clients: '',
  cluster_enabled: '',
  cluster_size: '',
  used_memory_human: '',
  used_cpu_user_children: '',
  instantaneous_output_kbps: '',
  instantaneous_input_kbps: '',
  aof_enabled: '',
  rdb_last_bgsave_status: '',
  db0: '',
});
const cacheCommands = ref({});
const memory = ref();
const command = ref();

const init = () => {
  getCacheInfo().then(res => {
    cacheSize.value = res.data.size;
    cacheProperties.value = res.data.properties;
    cacheCommands.value = res.data.commands;

    let memoryChat = echarts.init(memory.value);
    let usedMemory = cacheProperties.value.used_memory_human;
    let maxMemory = cacheProperties.value.maxmemory;
    if (maxMemory === '0') {
      const reg = /[-+]?[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)?/;
      const match = usedMemory.match(reg);
      const number = parseInt(match[0]);
      const thresholds = [10, 20, 40, 50, 60, 80, 100, 150, 200, 250, 500, 750, 1000, 1500, 2000, 4000, 8000];
      maxMemory = 16000;
      for (const threshold of thresholds) {
        if (number <= threshold) {
          maxMemory = threshold;
          break;
        }
      }
    }
    memoryChat.setOption({
      tooltip: {
        trigger: "item",
        formatter: "{b} <br/>{a} : " + usedMemory,
      },
      series: [{
        name: "峰值",
        type: "gauge",
        min: 0,
        max: maxMemory,
        center: ['30%', '50%'],
        detail: {
          formatter: usedMemory,
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: [
              [0.25, "#7FFFD4"],
              [0.50, "#00FFFF"],
              [0.75, "#FFAC1C"],
              [1, "#ff0000"]
            ],
          }
        },
        data: [{
          value: parseFloat(usedMemory),
          name: "内存消耗",
        }],
        animation: true,
        animationDuration: 1500,
        animationEasing: 'cubicInOut'
      }]
    });


    let commandChat = echarts.init(command.value);
    commandChat.setOption({
      tooltip: {
        trigger: "item",
        formatter: "{a} <br/>{b} : {c} ({d}%)",
      },
      legend: {
        orient: 'vertical',
        x: '65%',
        data: cacheCommands.value.key
      },
      series: [{
        name: "命令",
        type: "pie",
        roseType: "radius",
        radius: [0, "55%"],
        center: ["30%", "50%"],
        data: cacheCommands.value,
        animationEasing: "cubicInOut",
        animationDuration: 1500,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }]
    });
  });
}
/**
 * 获取缓存集合
 */
const loadingKey = ref(false);
const caches = ref();
const getCaches = () => {
  getCacheList().then(res => {
    caches.value = res.data;
  });
}

/**
 * 获取缓存key
 */
const cacheKeys = ref();
const selectCacheKey = ref();
const handleRowClickName = (row) => {
  getCacheKeys(row.cacheName);
  selectCacheKey.value = row.cacheName;
}
const getCacheKeys = (key) => {
  loadingKey.value = true;
  getCacheKey(key).then(res => {
    cacheKeys.value = res.data;
    loadingKey.value = false;
  });
}

/**
 * 删除缓存key
 */
const handleDeleteKey = (index, row) => {
  ElMessageBox.confirm('将删除缓存，是否确认？', '提示',
      {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}).then(() => {
    deleteCacheKey(row.cacheName).then(res => {
      if (res.code === 0) {
        getCaches();
      }
    });
  }).catch();
}

/**
 * 获取缓存值
 */
const dialogVisible = ref(false);
const cacheData = ref();
const handleRowClickKey = (row) => {
  getCacheValue(row);
}
const getCacheValue = (key) => {
  getCacheKeyValue(key).then(res => {
    if (res.code === 0) {
      cacheData.value = res.data;
      dialogVisible.value = true;
    }
  });
}
const handleCloseForm = () => {
  dialogVisible.value = false;
}

/**
 * 删除缓存value
 */
const handleDeleteValue = (index, row) => {
  ElMessageBox.confirm('将删除缓存，是否确认？', '提示',
      {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}).then(() => {
    deleteCacheValue(row).then(res => {
      if (res.code === 0) {
        getCacheKeys(selectCacheKey.value);
      }
    });
  }).catch();
}

/**
 * 删除全部缓存
 */
const handleCleanAll = () => {
  ElMessageBox.confirm('将删除缓存，是否确认？', '提示',
      {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}).then(() => {
    deleteCaches();
  }).catch();
}

const handleRefresh = () => {
  getCaches();
  cacheKeys.value = [];
  ElMessage.info('已刷新');
}

</script>

<style scoped lang="scss">
.cache {
  font-size: 12px
}

.el-divider {
  margin: 12px 0;
  padding: 0;
}

.ec-info {
  margin-top: 30px;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.db-info {
  margin-top: 30px;
}

.chart-card {
  flex: 1;

}

@media (max-width: 1300px) {
  .chart-card {
    flex: 0 0 100%;
  }
}

.chart {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 580px;
  height: 400px;
}

.red {
  color: red;
}
</style>