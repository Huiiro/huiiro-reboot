<template>
  <div class="empty">
    <el-empty
        :image-size="520"
        :image="empty"
        description="您访问的页面不存在，即将自动返回首页"
        style="margin-top: 80px"
    >
      <p>如果没有自动返回首页，您也可以点击按钮手动返回</p>
      <el-button @click="goBack" >回到首页</el-button>
    </el-empty>

  </div>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import {onMounted} from "vue";
import empty from '@/assets/imgs/error/pageNotFound.jpg';

const router = useRouter();
const goBack = () => {
  router.push('/index');
}

const autoGoBack = () => {
  setTimeout(() => {
    goBack();
  }, 5000);
};

onMounted(() => {
  autoGoBack();
});
</script>

<style scoped lang="scss">
.empty {
  p {
    font: inherit;
    font-size: 14px;
    color: #8f9298;
    margin: 10px 0 20px;
    padding: 0;
    vertical-align: baseline;
    border: 0;
  }
}
</style>