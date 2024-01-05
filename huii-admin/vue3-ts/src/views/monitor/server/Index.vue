<template>
  <div v-loading="loading">
    <div class="server">
      <el-card class="cpu-card gap">
        <template #header>
          <el-icon>
            <Cpu/>
          </el-icon>
          CPU
        </template>
        <el-row>
          <el-col :span="11">
            核心数：{{ cpu.cpuCore }}
          </el-col>
          <el-col :span="11">
            型号：{{ cpu.cpuType }}
          </el-col>
        </el-row>
        <el-divider/>
        <el-row>
          <el-col :span="11">
            使用率： {{ cpu.totalUsed }}%
          </el-col>
          <el-col :span="11">
            空闲率： {{ cpu.free }}%
          </el-col>
        </el-row>
        <el-divider/>
        <el-row>
          <el-col :span="11">
            系统使用率： {{ cpu.sysUsed }}%
          </el-col>
          <el-col :span="11">
            用户使用率： {{ cpu.userUsed }}%
          </el-col>
        </el-row>
      </el-card>
      <el-card class="mem-card gap">
        <template #header>
          <el-icon>
            <Odometer/>
          </el-icon>
          Memory
        </template>
        <el-row>
          <el-col :span="10">
            总内存：{{ mem.total }}GB
          </el-col>
        </el-row>
        <el-divider/>
        <el-row>
          <el-col :span="10">
            已用内存：{{ mem.used }}GB
          </el-col>
        </el-row>
        <el-divider/>
        <el-row>
          <el-col :span="10">
            剩余内存：{{ mem.free }}GB
          </el-col>
        </el-row>
      </el-card>
      <el-card class="sys-card gap">
        <template #header>
          <el-icon>
            <Monitor/>
          </el-icon>
          System
        </template>
        <el-row>
          <el-col :span="11">
            计算机名称：{{ sys.computerName }}
          </el-col>
          <el-col :span="11">
            项目路径：{{ sys.userDir }}
          </el-col>
        </el-row>
        <el-divider/>
        <el-row>
          <el-col :span="11">
            操作系统：{{ sys.osName }}
          </el-col>
          <el-col :span="11">
            系统架构：{{ sys.osArch }}
          </el-col>
        </el-row>
      </el-card>
      <el-card class="jvm-card gap">
        <template #header>
          <el-icon>
            <Notification/>
          </el-icon>
          JVM
        </template>
        <el-row>
          <el-col :span="11">
            JDK版本：{{ jvm.version }}
          </el-col>
          <el-col :span="11">
            JDK路径：{{ jvm.home }}
          </el-col>
        </el-row>
        <el-divider/>
        <el-row>
          <el-col :span="11">
            JVM总内存：{{ jvm.total }}MB
          </el-col>
          <el-col :span="11">
            JVM可用内存：{{ jvm.free }}MB
          </el-col>
        </el-row>
      </el-card>
      <el-card class="file-card">
        <template #header>
          <el-icon>
            <FolderOpened/>
          </el-icon>
          File
        </template>
        <el-table :data="file" size="small">
          <el-table-column prop="dirName" label="盘符名称"/>
          <el-table-column prop="typeName" label="盘符类型"/>
          <el-table-column prop="sysTypeName" label="文件类型"/>
          <el-table-column prop="total" label="总大小"/>
          <el-table-column prop="used" label="已用"/>
          <el-table-column prop="free" label="空闲"/>
          <el-table-column prop="usage" label="使用率">
            <template #default="scope">
              {{ scope.row.usage }}%
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {getServerInfo} from "@/api/monitor/server";
import {FolderOpened, Monitor, Notification, Odometer} from "@element-plus/icons-vue";

const loading = ref(false);
const cpu = ref({
  cpuCore: '',
  cpuType: '',
  totalUsed: '',
  free: '',
  sysUsed: '',
  userUsed: ''
});
const mem = ref({
  total: '',
  used: '',
  free: ''
});
const sys = ref({
  computerName: '',
  userDir: '',
  osName: '',
  osArch: ''
});
const jvm = ref({
  version: '',
  home: '',
  total: '',
  free: ''
});
const file = ref();
onMounted(() => {
  loading.value = true;
  getServerInfo().then(res => {
    const data = res.data;
    cpu.value = data.cpu;
    mem.value = data.mem;
    sys.value = data.sys;
    jvm.value = data.jvm;
    file.value = data.files;
    loading.value = false;
  });
})
</script>

<style scoped lang="scss">
.el-divider {
  margin: 12px 0;
  padding: 0;
}

.server {
  font-size: 12px
}

.gap {
  margin-bottom: 20px;
}
</style>