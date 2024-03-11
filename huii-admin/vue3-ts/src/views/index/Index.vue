<template>
  <div>
    <el-alert title="欢迎访问Huiiro！" type="success" style="margin-bottom: 20px"/>
    <el-divider/>
    <p class="header-text">系统导航：</p>
    <el-row class="header-row">
      <el-col class="header-col" :span="8">
        <el-empty
            class="guide-container"
            image=""
            :image-size="80"
            description=" "
        >
          <el-button
              class="guide-button"
              plain
              :icon="Promotion"
              @click="gotoHuiiro"
          >前往Huiiro
          </el-button>
        </el-empty>
      </el-col>
      <el-col class="header-col" :span="8">
        <el-empty
            class="guide-container"
            image=""
            :image-size="80"
            description=" "
        >
          <el-button
              class="guide-button"
              plain
              :icon="Promotion"
              @click="gotoPersonalCenter"
          >前往个人中心
          </el-button>
        </el-empty>
      </el-col>
      <el-col class="header-col" :span="8"/>
    </el-row>
    <el-divider/>
    <p class="header-text">系统消息：</p>
    <p v-for="item in news" class="item-text">
      {{ item }}
    </p>
    <el-divider/>
    <p class="header-text">更新日志：</p>
    <p v-for="item in updates" class="item-text">
      {{ item }}
    </p>
    <el-divider/>
    <p class="header-text">版本支持：</p>
    <p v-for="item in supports" class="item-text">
      {{ item }}
    </p>
  </div>
</template>

<script setup lang="ts">
import router from "@/router";
import {Promotion} from "@element-plus/icons-vue";
import {ref} from "vue";

const gotoPersonalCenter = () => {
  router.push({name: '个人中心'});
}
const gotoHuiiro = () => {
  window.location.href = 'https://www.huiiro.com/';
}

const news = ref([
    "暂无更多消息。"
]);
const updates = ref([
  "1. v1.2.2 新增消息模块",
  "2. v1.2.3 修复bug，新增消息推送模块",
  "3. v1.2.4 完善并修复代码生成模块的部分bug，优化体验和模板文件。后续会补充字段查询方式和更多方式的支持。"
]);
const supports = ref([
  "1. java17+",
  "2. springboot 3.0+",
  "3. postgresql 14+",
  "4. redis 6+",
  "5. minio",
  "6. nodejs 18+",
  "7. vue3",
]);
</script>

<style scoped>
.header-row {
  max-width: 1080px;
  display: flex;
  justify-content: center;
  align-content: center;
  text-align: left;
}

.header-col {
  min-width: 300px;
  margin-bottom: 20px;
}

.guide-container {
  border: 1px #b9b9b9 solid;
  border-radius: 10px;
  padding: 20px;
  width: 280px;
  background: linear-gradient(to right, #bbcafd, #d3adfd);
}

.guide-container:hover {
  border: 1px #ffffff solid;
}

.guide-button {
  width: 180px;
  height: 40px;
  color: #626262;
  background: linear-gradient(to right, #bbcafd, #d3adfd);
}

.guide-button:hover {
  background: linear-gradient(to right, #b1c1fc, #caa1fc);
}

.header-text {
  font-size: 20px;
  margin: 20px 0;
}

.item-text {
  margin: 12px 0;
  cursor: pointer;
}
</style>