<template>
  <el-scrollbar class="scrollbar">
    <el-menu
        text-color="#fff"
        active-text-color="#ffd04b"
        background-color="#2b2d30"
        :default-active="tabValue"
        :collapse="layoutStore.isFold || layoutStore.isMobile">
      <el-menu-item :index="index.name"
                    :show-arrow="false"
                    @click="handleClickMenu(index)">
        <el-icon>
          <component :is="index.icon"/>
        </el-icon>
        <template #title> {{ index.name }}</template>
      </el-menu-item>
      <sidebar-item :dataList="menuList"/>
    </el-menu>
  </el-scrollbar>
</template>

<script setup lang="ts">
import {computed, ref} from "vue";
import {useUserStore} from "@/store/modules/user.ts";
import {useLayoutStore} from "@/store/modules/layout.ts";
import sidebarItem from "../sidebarItem/Index.vue"
import router from "@/router";

let index = ref({
  name: '首页',
  path: '/index',
  icon: 'HomeFilled'
});
let tabValue = computed(() => userStore.tabValue);
let menuList = computed(() => userStore.menuList);
const userStore = useUserStore();
const layoutStore = useLayoutStore();
const handleClickMenu = (menu: any) => {
  if (router.currentRoute.name !== menu.name) {
    userStore.addTab(index);
    router.push({name: menu.name});
  }
}

</script>

<style scoped lang="scss">
.scrollbar {
  height: calc(100vh - $c-logo-height);
  user-select: none;
}

.el-menu {
  border: none !important;
}
</style>