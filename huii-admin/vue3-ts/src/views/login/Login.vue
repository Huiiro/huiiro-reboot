<template>
  <div class="login-container">
    <div class="login-form" :class="{mobile: layoutStore.isMobile}">
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef">
        <el-form-item>
          <div>
            <p class="title">{{ settings.headerTitle }} Login</p>
          </div>
        </el-form-item>
        <el-form-item prop="username" class="login-form-item">
          <el-input v-model=loginForm.username
                    placeholder="username" :prefix-icon="User"
                    size="large"/>
        </el-form-item>
        <el-form-item prop="password" class="login-form-item">
          <el-input v-model=loginForm.password
                    placeholder="password" :prefix-icon="Lock"
                    size="large"
                    type="password" show-password/>
        </el-form-item>
        <el-form-item class="login-form-item-oauth">
          <div class="login-form-item-spec-form">
            <el-checkbox v-model="loginForm.rememberMe"
                         class="login-form-item-spec-item">记住密码
            </el-checkbox>
            <p class="login-form-item-spec-item"
               @click="handleForgetPassword">忘记密码?</p>
          </div>
        </el-form-item>
        <el-form-item class="login-form-item-oauth">
          <div class="login-form-item-spec-text">
            <p>第三方登录方式</p>
          </div>
          <div class="login-form-item-spec-form">
            <i @click="handleOauthLogin('wechat')">
              <svgIcon name="oauth-wechat" width="22px" height="22px" class="svg-icon"/>
              <span class="login-form-item-spec-item">微信登录</span>
            </i>
            <i @click="handleOauthLogin('github')">
              <svgIcon name="oauth-github" width="20px" height="20px" class="svg-icon"/>
              <span class="login-form-item-spec-item">github登录</span>
            </i>
            <i @click="handleOauthLogin('gitee')">
              <svgIcon name="oauth-gitee" width="20px" height="20px" class="svg-icon"/>
              <span class="login-form-item-spec-item">gitee登录</span>
            </i>
          </div>
        </el-form-item>
        <el-form-item class="login-form-item-button">
          <el-button type="primary" class="login-button"
                     :loading="loadingWait" :disabled="loadingWait"
                     @click="handleLogin">Login
          </el-button>
        </el-form-item>
        <el-dialog title="请按要求完成验证" width="360px" v-model="showVerify"
                   :close-on-click-modal="false" @closed="refresh" append-to-body>
          <clickTextCaptcha ref="verifyRef" @success="onSuccess"/>
        </el-dialog>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
//@ts-ignore
import clickTextCaptcha from "@/components/captcha/ClickTextCaptcha.vue";
import {Lock, User} from "@element-plus/icons-vue";
import {nextTick, reactive, ref} from "vue";
import {accountLogin} from "@/api/auth/login";
import {useRoute, useRouter} from "vue-router";
import {setAccessToken, setRefreshToken, setUserInfo} from "@/utils/token.ts";
import {checkClickTextCaptcha} from "@/api/auth/captcha";
import {encryptFiled} from "@/utils/encrypt.ts";
import {useLayoutStore} from "@/store/modules/layout.ts";
import settings from "../../settings.ts";
import {useUserStore} from "@/store/modules/user.ts";
import {giteeLogin, githubLogin} from "@/api/auth/oauth2";

const layoutStore = useLayoutStore();
const userStore = useUserStore();
let router = useRouter();
let route = useRoute();
let loginForm = reactive({username: '', password: '', rememberMe: false});
let captchaForm = reactive({nonceStr: '', value: 0});

let loadingWait = ref(false);
let showVerify: any = ref(false);
let loginFormRef = ref();
let verifyRef = ref();
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
const onSuccess = (captcha: any) => {
  Object.assign(captchaForm, captcha)
  handleRealLogin()
};
const refresh = () => {
  verifyRef.value.refresh();
};
const handleLogin = () => {
  loginFormRef.value.validate().then(() => {
    nextTick(() => {
      showVerify.value = true;
    });
  })
};
const handleRealLogin = () => {
  checkClickTextCaptcha(captchaForm.nonceStr, captchaForm.value).then((res: any) => {
    if (res.code === 0) {
      showVerify.value = false;
      loadingWait.value = true;
      let pwd = loginForm.password
      encryptFiled(loginForm.password).then(encryptPassword => {
        loginForm.password = encryptPassword;
        accountLogin(loginForm).then((res: any) => {
          if (res.code === 0) {
            const data = res.data;
            setUserInfo(data.userInfo);
            setAccessToken(data.accessToken);
            setRefreshToken(data.refreshToken);
            userStore.setPermissions(data.permissions)
            userStore.setRoles(data.roles)
            let redirect: any = route.query.redirect;
            router.push({path: redirect || "/index"});
          }
        });
        loginForm.password = pwd
        loadingWait.value = false;
      });
    } else {
      verifyRef.value.refresh();
    }
  })
};
const handleForgetPassword = () => {
  console.log('forgetPassword')
};
const handleOauthLogin = (type: String) => {
  if (type == 'gitee') {
    giteeLogin().then(res => {
      window.location.href = res.data.url;
    });
  } else if (type == 'github') {
    githubLogin().then(res => {
      window.location.href = res.data.url;
    });
  }
};
</script>

<style scoped lang="scss">
.el-form-item {
  margin-bottom: 0;
}

.title {
  text-align: center;
  font-size: 22px;
  font-weight: lighter;
  margin-bottom: 20px;
}

.login-container {
  width: 100%;
  height: 100vh;
  background: url('@/assets/imgs/background/login-background.jpg') no-repeat center center;
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-form {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  width: 400px;
  display: flex;

  &.mobile {
    width: 400px;
  }
}

.login-form-item {
  display: flex;
  align-items: center;
  margin-bottom: 18px;
  cursor: pointer;
  user-select: none;
  width: 360px;
}

.login-form-item-rem {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .el-form-item__content {
    display: none;
  }
}

.login-form-item-oauth {
  user-select: none;

  .el-form-item__content {
    display: none;
  }

  .login-form-item-spec-text {
    width: 100%;
    display: inline;
    text-align: center;
    color: #9499a0;
  }

  .login-form-item-spec-form {
    width: 100%;
    display: flex;
    justify-content: space-between;
  }

  .login-form-item-spec-item {
    margin-left: 3px;
    color: #9499a0;
  }

  .login-form-item-spec-item:hover {
    cursor: pointer;
    color: $global-hover-color;
  }
}

.login-button {
  width: 360px;
  height: 38px;
  font-size: 15px;
  font-weight: revert;
  margin-top: 12px;
}

.left-content {
  flex: 2;
  padding: 20px;
}


</style>