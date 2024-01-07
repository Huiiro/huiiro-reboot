<template>
  <div>
    认证成功，正在跳转...
  </div>
</template>

<script setup lang="ts">
import {onMounted} from "vue";
import router from "@/router";

onMounted(() => {
  router.push('/index');
})
</script>

<style scoped lang="scss">

</style>