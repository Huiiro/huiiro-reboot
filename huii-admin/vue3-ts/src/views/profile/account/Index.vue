<template>
  <div class="p-div">
    <p class="p-header">
      账号密码管理
    </p>
    <p class="info-text info-text-clickable" @click="showResetPasswordDialog = true">
      修改密码?</p>
  </div>
  <div class="p-div">
    <p class="p-header">
      第三方账号信息管理
    </p>
    <el-table :data="bindData" style="width: 100%" border>
      <el-table-column prop="" label="序号" width="120">
        <template #default="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="oauthProvider" label="绑定信息" min-width="200"/>
      <el-table-column prop="oauthUserAvatar" label="账户详情" min-width="200">
        <template #default="scope">
          <div class="item">
            <el-avatar :src="scope.row.oauthUserAvatar" :size="'small'"/>
            <p>{{ scope.row.oauthUserName }}</p>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="绑定时间" min-width="200"/>
      <el-table-column prop="createTime" label="操作" width="120">
        <template #default="scope">
          <el-button type="primary" link style="color: red" @click="cancelBindByProvider(scope.row.oauthProvider)">
            取消绑定
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <p class="info-text info-text-clickable" @click="showBindToAlreadyExistsAccountDialog = true">
      已有帐户，绑定至已有账户?</p>
    <p class="info-text">绑定其他第三方账号?</p>
    <p>
      <el-button @click="bindGitee" v-if="!oauthProviders.includes('gitee')">绑定gitee</el-button>
      <el-button @click="bindGithub" v-if="!oauthProviders.includes('github')">绑定github</el-button>
    </p>
  </div>

  <el-dialog title="登录已有帐号完成账号绑定"
             class="global-dialog-iu-min"
             v-model="showBindToAlreadyExistsAccountDialog"
             :close-on-click-modal="false"
             :before-close="handleClose">
    <template #default>
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef">
        <el-form-item prop="username" class="login-form-item">
          <el-input v-model=loginForm.username
                    placeholder="username" :prefix-icon="User"
                    size="large" autocomplete="new-password"/>
        </el-form-item>
        <el-form-item prop="password" class="login-form-item">
          <el-input v-model=loginForm.password
                    placeholder="password" :prefix-icon="Lock"
                    size="large"
                    type="password" show-password autocomplete="new-password"/>
        </el-form-item>
      </el-form>
    </template>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="handleConfirm">登 录</el-button>
      </span>
    </template>
  </el-dialog>

  <el-dialog title="修改密码"
             class="global-dialog-iu-min"
             v-model="showResetPasswordDialog"
             :close-on-click-modal="false"
             :before-close="handleClose">
    <template #default>
      todo
    </template>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="handleConfirmResetPassword">提 交</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {bindToHas, cancelBind, getBindList, giteeLogin, githubLogin} from "@/api/auth/oauth2";
import {onMounted, reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {Lock, User} from "@element-plus/icons-vue";
import {encryptFiled} from "@/utils/encrypt.ts";
import {setAccessToken, setRefreshToken, setUserInfo} from "@/utils/token.ts";
import {useUserStore} from "@/store/modules/user.ts";

onMounted(() => {
  getBindStatus();
})

/**
 * 获取第三方绑定信息
 */
const bindData = ref();
const oauthProviders = ref([]);
const getBindStatus = () => {
  getBindList().then(res => {
    bindData.value = res.data
    oauthProviders.value = res.data.map(item => item.oauthProvider);
  });
}

/**
 * 取消绑定
 * @param provider
 */
const cancelBindByProvider = (provider: string) => {
  ElMessageBox.confirm(
      '将取消绑定 ' + provider + ' 账户，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    cancelBind(provider).then(() => {
      getBindStatus();
    })
  }).catch();
}

/**
 * 绑定github
 */
const bindGithub = () => {
  githubLogin().then(res => {
    window.location.href = res.data.url;
  });
}

/**
 * 绑定gitee
 */
const bindGitee = () => {
  giteeLogin().then(res => {
    window.location.href = res.data.url;
  });
}

/**
 * 绑定至已有帐户
 */
const userStore = useUserStore();
const showBindToAlreadyExistsAccountDialog = ref(false);

let loginFormRef = ref();
let loginForm = reactive({username: '', password: ''});
const loginRules = {
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 1, max: 20, message: '用户名长度不超过20位', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 1, max: 20, message: '密码长度不超过20位', trigger: 'blur'},
  ]
};

const handleClose = () => {
  showBindToAlreadyExistsAccountDialog.value = false;
  showResetPasswordDialog.value = false;
}

const handleConfirm = () => {
  loginFormRef.value.validate().then(() => {
    let pwd = loginForm.password;
    encryptFiled(loginForm.password).then(encryptPassword => {
      loginForm.password = encryptPassword;
      bindToHas(loginForm).then(res => {
        if (res.code === 0) {
          const data = res.data;
          setUserInfo(data.userInfo);
          setAccessToken(data.accessToken);
          setRefreshToken(data.refreshToken);
          userStore.setPermissions(data.permissions);
          userStore.setRoles(data.roles);
          ElMessage.success('绑定成功');
          setInterval(() => {
            location.reload();
          }, 1200);
        }
      });
      loginForm.password = pwd;
    });
  })
}

/**
 * 重置密码
 */
const showResetPasswordDialog = ref(false);

const handleConfirmResetPassword = () => {

}
</script>

<style scoped lang="scss">
.item {
  display: flex;
  align-content: center;
  gap: 10px;
}

.p-div {
  margin-bottom: 50px;
}

.p-header {
  font-size: 20px;
  padding-bottom: 20px;
}

.info-text-clickable:hover {
  color: #5d5d5d;
}

.info-text {
  font-size: 14px;
  font-weight: 400;
  margin: 10px 0;
  cursor: pointer;
}
</style>