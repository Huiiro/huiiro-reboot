<template>
  <div class="header">
    <div class="header-left">
      <el-icon :size="18" class="icon" @click="handleClickExpandIcon">
        <Expand v-if="isFold"></Expand>
        <Fold v-else></Fold>
      </el-icon>
      <breadcrumb v-if="!layoutStore.isMobile"/>
    </div>
    <div class="header-right">
      <timer v-if="!layoutStore.isMobile"/>
      <p style="user-select: none">欢迎, <span style="user-select: none; cursor: pointer">{{ username }}</span></p>
      <el-tooltip content="刷新">
        <el-icon class="item" :size="18" @click="handleClickRefresh">
          <Refresh/>
        </el-icon>
      </el-tooltip>
      <el-tooltip content="消息">
        <el-badge :value="unread" :max="99" :hidden="unread == 0">
          <el-icon class="item" :size="18" @click="handleClickMessage">
            <Message/>
          </el-icon>
        </el-badge>
      </el-tooltip>
      <el-tooltip content="设置">
        <el-icon class="item" :size="18" @click="handleClickSetting">
          <Setting/>
        </el-icon>
      </el-tooltip>
      <el-tooltip content="全屏">
        <el-icon class="item" :size="18" @click="handleClickFullScreen">
          <FullScreen/>
        </el-icon>
      </el-tooltip>
      <el-dropdown>
        <span class="item">
          更多
          <el-icon :size="12">
            <arrow-down/>
          </el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item :icon="User" @click="handleClickUserCenter">个人中心</el-dropdown-item>
            <el-dropdown-item :icon="ChromeFilled" @click="handleClickHelpDoc">帮助文档</el-dropdown-item>
            <el-dropdown-item :icon="SwitchButton" divided @click="handleLogout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-avatar shape="square" class="header-avatar"
                 :src="avatar"
                 @click="handleClickAvatar">
        user
      </el-avatar>
    </div>
  </div>

</template>

<script setup lang="ts">
import breadcrumb from "../breadcrumb/Index.vue";
import {
  ArrowDown,
  ChromeFilled,
  Expand,
  Fold,
  FullScreen,
  Message,
  Refresh,
  Setting,
  SwitchButton,
  User
} from "@element-plus/icons-vue";
import {onMounted, ref} from "vue";
import {useLayoutStore} from "@/store/modules/layout.ts";
import timer from '../../timer/Index.vue';
import {ElMessageBox} from "element-plus";
import {useUserStore} from "@/store/modules/user.ts";
import {logout} from "@/api/auth/logout";
import {useRouter} from "vue-router";
import {getUserInfo} from "@/utils/token.ts";
import {defaultAvatar} from "@/utils/constants.ts";

let isFold = ref(false);
let router = useRouter();
const avatar = ref();
const userId = ref()
const username = ref();
const unread = ref();
const layoutStore = useLayoutStore();
const userStore = useUserStore();

onMounted(() => {
  let info: any = getUserInfo();
  username.value = info.username;
  userId.value = info.userId;
  avatar.value = info.userAvatar || defaultAvatar;
  unread.value = userStore.unread || 0;
});

const handleClickExpandIcon = () => {
  layoutStore.setIsFold();
  isFold.value = layoutStore.isFold;
};

const handleClickRefresh = () => {
  layoutStore.setIsRefresh();
};

const handleClickFullScreen = () => {
  let isFull = document.fullscreenElement;
  if (!isFull) {
    document.documentElement.requestFullscreen();
  } else {
    document.exitFullscreen();
  }
};

const handleClickSetting = () => {

};

const handleClickAvatar = () => {

};

const handleClickMessage = () => {
  userStore.addTab({
    name: '我的消息',
    title: '我的消息',
    path: '/message',
    icon: 'Message'
  });
  router.push({name: '我的消息'});
};

const handleClickUserCenter = () => {
  userStore.addTab({
    name: '个人中心',
    title: '个人中心',
    path: '/profile',
    icon: 'User'
  });
  router.push({name: '个人中心'});
};

const handleClickHelpDoc = () => {
  window.open('https://gitee.com/hu-yi0990/huii-reboot3', '_blank');
};

const handleLogout = () => {
  ElMessageBox.confirm(
      '确定要退出登录吗', '提示',
      {confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    logout().then(response => {
      if (response.code === 0) {
        userStore.clearLoginInfo();
        router.push({path: "/login"})
      }
    });
  });
};
</script>

<style scoped lang="scss">
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: $c-header-height;
  background-color: $header-top-color;
  font-size: 12px;
  color: $header-font-color;

  .header-left {
    display: flex;
    align-items: center;
  }

  .header-right {
    display: flex;
    align-items: center;
    margin-right: 16px;
    gap: 16px;
  }

  .header-avatar {
    height: calc($c-header-height - 4px);
    width: calc($c-header-height - 4px);
    max-height: 42px;
    max-width: 42px;
    margin-top: 3px;
    margin-bottom: 3px;
  }

  .item {
    cursor: pointer;
    user-select: none;
  }
}

.icon {
  margin-left: 10px;
}
</style>