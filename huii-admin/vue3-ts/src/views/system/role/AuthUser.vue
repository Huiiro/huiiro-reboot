<template>
  <div>{{ roleId }}</div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";

const route = useRoute();
const roleId = ref();
onMounted(() => {
  roleId.value = route.params.roleId;
})
</script>

<style scoped lang="scss">

</style>