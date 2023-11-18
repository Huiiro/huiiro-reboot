<template>
  <p>->vue test ✔</p>
  <h5 class="sa">->sass test ✔</h5>

  <p>->svg test ✔</p>
  <svgIcon name="flower"></svgIcon>

  <p>->element ui test ✔</p>
  <el-button :icon="Edit"></el-button>
  <el-pagination layout="prev, pager, next" :total="50"/>

  <p>->password encrypt test ✔</p>
  <p>{{ password}}</p>
  <el-button @click="handleEncrypt">click to encrypt password</el-button>
  <p>->router test ✔</p>
</template>

<script setup lang="ts">
import {Edit} from "@element-plus/icons-vue";
import {encryptFiled} from "@/utils/encrypt.ts";
import {ref} from "vue";

let password = ref('123456');
const handleEncrypt = () => {
  encryptFiled('123456').then(res => {
    password.value = res
  })
}
</script>

<style scoped lang="scss">
h5 {
color:$global-color;
}
</style>