<template>
  <div class="register-container">
    <div class="register-form" :class="{mobile: layoutStore.isMobile}">
      <el-form :model="registerForm" :rules="registerRules" status-icon ref="registerFormRef">
        <el-form-item>
          <div>
            <p class="title">{{ settings.headerTitle }} Register</p>
          </div>
        </el-form-item>
        <el-form-item prop="username" class="register-form-item">
          <el-input v-model=registerForm.username
                    placeholder="username" :prefix-icon="User"
                    size="large"/>
        </el-form-item>
        <el-form-item prop="password" class="register-form-item">
          <el-input v-model=registerForm.password
                    placeholder="password" :prefix-icon="Lock"
                    size="large"
                    type="password"/>
        </el-form-item>
        <el-form-item prop="confirmPassword" class="register-form-item">
          <el-input v-model=registerForm.confirmPassword
                    placeholder="confirm password" :prefix-icon="Lock"
                    size="large"
                    type="password"/>
        </el-form-item>
        <el-form-item class="register-form-item-button">
          <el-button type="primary" class="register-button"
                     :loading="loadingWait" :disabled="loadingWait"
                     @click="handleRegister">Register
          </el-button>
        </el-form-item>
        <el-form-item>
          <div class="register-form-item-spec-item">
            <p @click="handleBackLogin">已有账号?去登陆</p>
          </div>
        </el-form-item>
        <el-dialog title="请按要求完成验证" width="360px" v-model="showVerify"
                   :close-on-click-modal="false" @closed="refresh" append-to-body>
          <slider-verify ref="verifyRef" @success="onSuccess"/>
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
import {useRoute, useRouter} from "vue-router";
import {checkSlideCaptcha} from "@/api/auth/captcha";
import {encryptFiled} from "@/utils/encrypt.ts";
import {useLayoutStore} from "@/store/modules/layout.ts";
import settings from "../../settings.ts";
import SliderVerify from "@/components/captcha/SlideCaptcha.vue";
import {checkUsername, register} from "@/api/auth/register";

const layoutStore = useLayoutStore();
let router = useRouter();
let route = useRoute();
let registerForm = reactive({username: '', password: '', confirmPassword: ''});
let captchaForm = reactive({nonceStr: '', value: 0});

let loadingWait = ref(false);
let showVerify: any = ref(false);
let registerFormRef = ref();
let verifyRef = ref();
const validateUsername = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else {
    checkUsername(value).then(res => {
      if (res.data) {
        callback();
      } else {
        callback(new Error('该账号已被注册'))
      }
    })
  }
}
const checkUserNameUnique = async (username: any) => {
  const r = await checkUsername(username);
  console.log(r.data)
  return r.data;
}
const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (registerForm.confirmPassword !== '') {
      if (!registerFormRef.value) return
      registerFormRef.value.validateField('confirmPassword', () => null)
    }
    callback()
  }
}
const validatePassConfirm = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请确认密码'))
  } else if (value !== registerForm.password) {
    callback(new Error("两次密码不一致"))
  } else {
    callback()
  }
}
const registerRules = {
  username: [
    {validator: validateUsername, trigger: 'blur'},
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 1, max: 20, message: '用户名长度不超过20位', trigger: 'blur'},
  ],
  password: [
    {validator: validatePass, trigger: 'blur'},
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 1, max: 20, message: '密码长度不超过20位', trigger: 'blur'},
  ],
  confirmPassword: [
    {validator: validatePassConfirm, trigger: 'blur'},
    {required: true, message: '请确认密码', trigger: 'blur'},
    {min: 1, max: 20, message: '密码长度不超过20位', trigger: 'blur'},
  ]
};
const onSuccess = (captcha: any) => {
  Object.assign(captchaForm, captcha)
  handleRealRegister()
};
const refresh = () => {
  verifyRef.value.refresh();
};
const handleRegister = () => {
  registerFormRef.value.validate().then(() => {
    nextTick(() => {
      showVerify.value = true;
    });
  })
};
const handleRealRegister = () => {
  checkSlideCaptcha(captchaForm.nonceStr, captchaForm.value).then((res: any) => {
    if (res.code === 0) {
      showVerify.value = false;
      loadingWait.value = true;
      let pwd = registerForm.password;
      registerForm.confirmPassword = '';
      encryptFiled(registerForm.password).then(encryptPassword => {
        registerForm.password = encryptPassword;
        register(registerForm).then((res: any) => {
          if (res.code === 0) {
            setTimeout(() => {
              handleBackLogin();
            }, 2600);
          }
        });
        registerForm.password = pwd;
        loadingWait.value = false;
      });
    } else {
      verifyRef.value.refresh();
    }
  })
};
const handleBackLogin = () => {
  router.push('login')
}
</script>

<style scoped lang="scss">
:deep(.el-input__validateIcon) {
  /*成功icon样式设置*/
  /*.el-icon el-input__icon el-input__validateIcon*/
  /*失败icon样式设置*/
  /*.el-form-item.is-error .el-input__validateIcon*/
  color: $global-success-color;
}


.el-form-item {
  margin-bottom: 0;
}

.title {
  text-align: center;
  font-size: 22px;
  font-weight: lighter;
  margin-bottom: 20px;
}

.register-container {
  width: 100%;
  height: 100vh;
  background: url('@/assets/imgs/background/login-background.jpg') no-repeat center center;
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
}

.register-form {
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

.register-form-item {
  display: flex;
  align-items: center;
  margin-bottom: 18px;
  cursor: pointer;
  user-select: none;
  width: 360px;
}

.register-form-item-rem {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .el-form-item__content {
    display: none;
  }
}


.register-form-item-spec-item {
  margin-left: 3px;
  color: #9499a0;
  float: right;
}

.register-form-item-spec-item:hover {
  cursor: pointer;
  color: $global-hover-color;
}

.register-button {
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