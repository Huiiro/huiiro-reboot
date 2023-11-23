<template>
  <div class="layout-container">
    <div class="layout-sidebar" :class="{fold: layoutStore.isFold || layoutStore.isMobile}">
      <logo></logo>
      <sidebar></sidebar>
    </div>
    <div class="layout-header" :class="{fold: layoutStore.isFold || layoutStore.isMobile}">
      <Header></Header>
      <tab></tab>
    </div>
    <div class="layout-main" :class="{fold: layoutStore.isFold || layoutStore.isMobile}">
      <router-view :key="$route.fullPath"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import Logo from "./logo/Index.vue";
import Sidebar from "./sidebar/Index.vue";
import Header from './header/Index.vue'
import tab from "./tab/Index.vue";
import {useLayoutStore} from "@/store/modules/layout.ts";

const layoutStore = useLayoutStore();
</script>

<style scoped lang="scss">

.layout-container {
  width: 100%;
  height: 100vh;

  .layout-sidebar {
    width: $sidebar-width;
    height: 100vh;
    background-color: $sidebar-color;
    transition: all 0.32s;

    &.fold {
      width: $sidebar-min-width;
    }
  }

  .layout-header {
    position: fixed;
    top: 0;
    left: $sidebar-width;
    width: calc(100% - $sidebar-width);
    height: $header-height;
    background-color: $header-color;
    transition: all 0.32s;

    &.fold {
      width: calc(100vw - $sidebar-min-width);
      left: $sidebar-min-width;
    }
  }

  .layout-main {
    position: absolute;
    top: $header-height;
    left: $sidebar-width;
    width: calc(100% - $sidebar-width);
    height: calc(100vh - $header-height);
    background-color: $background-color;
    padding: 16px;
    overflow: auto;
    transition: all 0.32s;

    &.fold {
      width: calc(100vw - $sidebar-min-width);
      left: $sidebar-min-width;
    }
  }
}
</style>