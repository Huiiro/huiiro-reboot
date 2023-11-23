<template>
  <div class="breadcrumb">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/index' }" v-show="routes.length <= 0">首页</el-breadcrumb-item>
      <el-breadcrumb-item
          v-for="(item, index) in routes"
          :key="index"
          :to="item.to"
      >
        {{ item.label }}
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script setup lang="ts">
import {useRoute} from 'vue-router';
import {computed} from 'vue';

const routes = computed(() => {
  const matchedRoutes = useRoute().matched;
  const breadcrumbs: any = [];

  matchedRoutes.forEach((route) => {
    if (route.meta.breadcrumb) {
      breadcrumbs.push({
        label: route.name,
        to: route.path,
      });
    }
  });
  return breadcrumbs;
});
</script>

<style scoped lang="scss">
.breadcrumb {
  margin-left: 24px;
  user-select: none;
  cursor: pointer;
}
</style>
