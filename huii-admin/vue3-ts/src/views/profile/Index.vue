<template>
  <div class="main">
    <div>
      <el-card class="box-card" shadow="never">
        <template #header>
          <div class="card-header">
            <user-avatar :src="avatar"/>
            <p class="card-header-text">{{ username }}</p>
          </div>
        </template>
        <div>
          <p class="header">基本设置</p>
          <p
              class="item"
              v-for="menu in basicMenus"
              :key="menu.name"
              ref="menu.name"
              @click="handleClick(menu.name)"
              :class="{ 'active': isActive == menu.name }"
          >
            <el-icon v-if="menu.icon">
              <component :is="menu.icon"/>
            </el-icon>
            {{ menu.label }}
          </p>
        </div>
        <!--        <div>-->
        <!--          <el-divider/>-->
        <!--          <p class="header">基本设置</p>-->
        <!--          <p class="item" ref="个人资料" @click="handleClick('个人资料')">个人资料</p>-->
        <!--          <p class="item" ref="账号管理" @click="handleClick('账号管理')">账号管理</p>-->
        <!--        </div>-->
      </el-card>
    </div>
    <div class="right-div">
      <router-view></router-view>
    </div>
  </div>

</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {getUserInfo} from "@/utils/token.ts";
import {defaultAvatar} from "@/utils/constants.ts";
import router from "@/router";
import UserAvatar from "@/views/profile/UserAvatar.vue";

const isActive = ref();
const basicMenus = [
  {name: '个人资料', label: '个人资料', icon: 'Postcard', url: ''},
  {name: '账号管理', label: '账号管理', icon: 'UserFilled', url: ''},
  {name: '信息管理', label: '信息管理', icon: 'Management', url: ''},
];

const avatar = ref();
const username = ref();
onMounted(() => {
  //获取基本信息
  let info: any = getUserInfo();
  username.value = info.username;
  avatar.value = info.userAvatar || defaultAvatar;
  isActive.value = router.currentRoute.value.name;
});

/**
 * 点击跳转
 */
const handleClick = (item: any) => {
  router.push({name: item});
}
</script>

<style scoped lang="scss">
:deep(.el-card__header) {
  padding: 0;
  margin: 0;
  background-color: #ececec;
}

.el-divider {
  margin: 20px 0;
  padding: 0;
}

/**main div*/
.main {
  display: flex;
  justify-content: center;
  gap: 40px;
  padding: 0 60px;
  @media (max-width: 768px) {
    padding: 0;
    gap: 20px;
  }
}

/**right div show router view*/
.right-div {
  width: calc(100% - $global-profile-left-width);
  max-width: 1000px;
  /**background-color: #ffffff;*/
}

/**el-card body*/
.box-card {
  width: $global-profile-left-width;
  user-select: none;
}

.card-header {
  height: 68px;
  margin-left: 20px;
  display: flex;
  align-items: center;

}

.card-header-text {
  margin-left: 15px;
  font-size: 16px;
  font-weight: 400;
  overflow: hidden;
}

.header {
  color: #1c1c1c;
  cursor: none;
}

.item {
  font-size: 14px;
  color: #9f9f9f;
  margin: 20px 0;
  cursor: pointer;
}

.item:hover {
  color: #1c1c1c;
}

.active {
  color: #1c1c1c;
  font-weight: 500;
}
</style>