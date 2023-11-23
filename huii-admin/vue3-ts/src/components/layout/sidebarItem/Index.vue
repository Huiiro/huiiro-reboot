<template>
  <template v-for="(item) in dataList">
    <el-sub-menu :index="item.name" v-if="item.children.length && item.visible !== false" :key="item.name">
      <template v-slot:title>
        <el-icon>
          <component :is="item.icon"/>
        </el-icon>
        <span>{{ item.name }}</span>
      </template>
      <sidebar-item :dataList="item.children"/>
    </el-sub-menu>
    <template v-else-if="item.visible !== false">
      <el-menu-item :index="item.name" :key="item.name" @click="handleClickMenu(item)">
        <el-icon>
          <component :is="item.icon"/>
        </el-icon>
        <template #title> {{ item.name }}</template>
      </el-menu-item>
    </template>
  </template>
</template>

<script setup lang="ts">
import SidebarItem from './index.vue';
import {useUserStore} from "@/store/modules/user.ts";
import router from "@/router";

const {dataList} = defineProps(['dataList']);
const userStore = useUserStore();
const handleClickMenu = (menu: any) => {
  if (router.currentRoute.name !== menu.name) {
    userStore.addTab(menu)
    router.push({name: menu.name})
  }
}
</script>

<style scoped>

</style>