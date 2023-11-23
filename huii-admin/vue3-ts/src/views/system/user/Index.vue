<template>
  <div>
   user
{{routes}}
  </div>
</template>

<script lang="ts" setup>
import {getRoutes} from "@/api/system/menu";
import {onMounted, ref} from "vue";

const routes = ref([]);
onMounted(async () => {
  const response: any = await getRoutes();
  routes.value = response.data
})

</script>

<style scoped>

</style>
