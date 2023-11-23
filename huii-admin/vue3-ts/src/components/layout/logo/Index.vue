<template>
  <div class="logo">
    <img src="../../../assets/icons/logo/kun.svg" alt="">
    <p :class="{fold: layoutStore.isFold || layoutStore.isMobile}">{{settings.logoTitle}}</p>
  </div>
</template>

<script setup lang="ts">
import settings from "@/settings.ts";
import {useLayoutStore} from "@/store/modules/layout.ts";

const layoutStore = useLayoutStore();
</script>

<style scoped lang="scss">
.logo {
  display: flex;
  align-items: center;
  width: 100%;
  height: $c-logo-height;
  padding: 15px 10px;

  img {
    width: 36px;
    height: 36px;
  }

  p {
    color: #eaeaea;
    font-size: 22px;
    margin-left: 10px;
    opacity: 1;
    transform: scale(1);
    transition: opacity 0.6s, transform  0.6s;

    &.fold {
      opacity: 0;
      transform: scale(0);
      transition: opacity 0.12s, transform  0.12s;
    }
  }

}
</style>