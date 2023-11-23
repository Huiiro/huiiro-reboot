<template>
  <div>
    <router-view v-slot="{ Component }">
      <transition name="fade">
          <component :is="Component"  :key="$route.name" v-if="refreshFlag"/>
<!--        <keep-alive>-->
<!--          <component :is="Component"  :key="$route.name" v-if="refreshFlag"/>-->
<!--        </keep-alive>-->
      </transition>
    </router-view>
  </div>
</template>

<script setup lang="ts">
import {useLayoutStore} from "@/store/modules/layout.ts";
import {h, nextTick, ref, watch, watchEffect} from "vue";
import {ElNotification} from "element-plus";

const layoutStore = useLayoutStore();
const screenWidth = ref(window.innerWidth);
let refreshFlag = ref(true);
watch(() => layoutStore.isRefresh, () => {
  refreshFlag.value = false;
  nextTick(() => {
    refreshFlag.value = true;
    ElNotification({
      title: '提示',
      message: h('i', {style: 'color: teal'}, '刷新成功'),
      type: 'success',
      duration: 1200
    })
  })
})
const handleResize = () => {
  screenWidth.value = window.innerWidth;
};
watch(screenWidth, () => {
  if (screenWidth.value <= 768) {
    layoutStore.setIsMobile(true)
  } else {
    layoutStore.setIsMobile(false)
  }
}, {immediate: true, deep: true})
watchEffect(() => {
  window.addEventListener('resize', handleResize);
  return () => {
    window.removeEventListener('resize', handleResize);
  };
});
</script>

<style lang="scss">

</style>
