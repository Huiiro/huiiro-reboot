<template>
  <div v-if="showTimer" style="user-select: none">
    {{ dateYear }} {{ dateDay }} {{ dateWeek }}
  </div>
</template>

<script setup lang="ts">
import {computed, onBeforeUnmount, onMounted, ref} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import dayjs from "dayjs";

const dateDay = ref<string | null>(null);
const dateYear = ref<string | null>(null);
const dateWeek = ref<string | null>(null);
const timer = ref<number | null>(null);
const weekday = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];

const layoutStore = useLayoutStore();
const showTimer = computed(() => {
  return layoutStore.isTimer;
});
const updateDate = () => {
  const date = dayjs(new Date());
  dateDay.value = date.format("HH:mm:ss");
  dateYear.value = date.format("YYYY-MM-DD");
  dateWeek.value = date.format(weekday[date.day()]);
};

onMounted(() => {
  timer.value = setInterval(updateDate, 1000);
});

onBeforeUnmount(() => {
  if (timer.value) {
    clearInterval(timer.value);
  }
});
</script>

<style scoped>

</style>