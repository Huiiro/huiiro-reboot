<template>
  <div class="forgetPwd-container">
    <div class="forgetPwd-form" :class="{mobile: layoutStore.isMobile}">
      <el-form :model="forgetPwdForm"
               :rules="forgetPwdRules"
               :label-position="labelPosition"
               label-width="auto"
               status-icon ref="forgetPwdFormRef">
        <el-form-item>
          <div>
            <p class="title">{{ settings.headerTitle }} {{ acc_lang_zhCN.header }}</p>
          </div>
        </el-form-item>
        <el-form-item prop="username" class="forgetPwd-form-item" :label="acc_lang_zhCN.usernameLabel">
          <el-input v-model=forgetPwdForm.username
                    :placeholder="acc_lang_zhCN.usernamePlaceholder"
                    :prefix-icon="User"
                    size="large"/>
        </el-form-item>
        <el-form-item prop="verifyType" class="forgetPwd-form-item" :label="acc_lang_zhCN.verifyTypeLabel">
          <el-select
              v-model="forgetPwdForm.verifyType"
              :placeholder="acc_lang_zhCN.verifyTypePlaceholder"
              size="large"
              style="width: 100%;"
          >
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="verifyCode" class="forgetPwd-form-item" :label="acc_lang_zhCN.verifyCodeLabel">
          <el-input
              v-model=forgetPwdForm.verifyCode
              :placeholder="acc_lang_zhCN.verifyCodePlaceholder"
              :prefix-icon="Message"
              size="large"
              style="width: 230px"/>
          <el-button size="large"
                     style="margin-left: 20px;width: 110px"
                     @click="getForgetPwdVerifyCode"
                     :disabled="codeCanSend">{{ codeContent }}
          </el-button>
        </el-form-item>
        <el-form-item prop="password" class="forgetPwd-form-item" :label="acc_lang_zhCN.passwordLabel">
          <el-input v-model=forgetPwdForm.password
                    :placeholder="acc_lang_zhCN.passwordPlaceholder"
                    :prefix-icon="Lock"
                    size="large"
                    type="password"
                    autocomplete="off"/>
        </el-form-item>
        <el-form-item prop="confirmPassword" class="forgetPwd-form-item" :label="acc_lang_zhCN.confirmPasswordLabel">
          <el-input v-model=forgetPwdForm.confirmPassword
                    :placeholder="acc_lang_zhCN.passwordPlaceholder"
                    :prefix-icon="Lock"
                    size="large"
                    type="password"
                    autocomplete="off"/>
        </el-form-item>
        <el-form-item class="forgetPwd-form-item-button">
          <el-button type="primary" class="forgetPwd-button"
                     :loading="loadingWait" :disabled="loadingWait"
                     @click="handleConfirm">{{ acc_lang_zhCN.submitButton }}
          </el-button>
        </el-form-item>
        <el-form-item>
          <div class="forgetPwd-form-item-spec-item">
            <p @click="handleBackHuiiroHelp">{{ acc_lang_zhCN.problemText }}</p>
          </div>
        </el-form-item>
        <el-dialog :title="acc_lang_zhCN.verifyDialogTitle" width="360px" v-model="showVerifyForCode"
                   :close-on-click-modal="false" @closed="refreshForCode" append-to-body>
          <clickTextCaptcha ref="verifyRefCode" @success="onSuccessForCode"/>
        </el-dialog>
        <el-dialog :title="acc_lang_zhCN.verifyDialogTitle" width="360px" v-model="showVerify"
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
import {Lock, Message, User} from "@element-plus/icons-vue";
import {nextTick, reactive, ref} from "vue";
import {checkClickTextCaptcha, checkSlideCaptcha} from "@/api/auth/captcha";
import {encryptFiled} from "@/utils/encrypt.ts";
import {useLayoutStore} from "@/store/modules/layout.ts";
import settings from "../../settings.ts";
import SliderVerify from "@/components/captcha/SlideCaptcha.vue";
import {ElMessage, FormProps} from "element-plus";
import {getForgetPwdCode, sendForgetPwdRequest} from "@/api/auth/forgetPwd";

const acc_lang_zhCN = {
  header: '找回账号',
  usernameLabel: '需要找回的账号',
  usernamePlaceholder: '输入需要找回的账号',
  verifyTypeLabel: '选择验证方式',
  verifyTypePlaceholder: '选择验证方式',
  verifyCodeLabel: '验证码',
  verifyCodePlaceholder: '输入验证码',
  passwordLabel: '设置新密码',
  passwordPlaceholder: '输入新密码',
  confirmPasswordLabel: '确认新密码',
  confirmPasswordPlaceholder: '确认新密码',
  submitButton: '确 认',
  problemText: '遇到问题?联系我们',
  verifyDialogTitle: '请按要求完成验证'
}

const acc_lang_enUS = {
  header: 'Retrieve account',
  usernameLabel: 'The Account needs to be retrieved',
  usernamePlaceholder: 'Input your account to find back',
  verifyTypeLabel: 'Select verification method',
  verifyTypePlaceholder: 'Select your verification type',
  verifyCodeLabel: 'Verification code',
  verifyCodePlaceholder: 'Input verification code',
  passwordLabel: 'Set new password',
  passwordPlaceholder: 'Set new password',
  confirmPasswordLabel: 'Confirm your password',
  confirmPasswordPlaceholder: 'Confirm your password',
  submitButton: 'Confirm',
  problemText: 'Have a problem? Contact us',
  verifyDialogTitle: 'Please complete verification as required'
}

const layoutStore = useLayoutStore();
let forgetPwdForm = reactive(
    {
      username: '',
      verifyType: 'email',
      verifyCode: '',
      password: '',
      confirmPassword: ''
    }
);
let forgetPwdFormRef = ref();
let captchaForm = reactive({nonceStr: '', value: 0});

let loadingWait = ref(false);
let showVerify: any = ref(false);
let verifyRef = ref();
let showVerifyForCode: any = ref(false);
let verifyRefCode = ref();

const labelPosition = ref<FormProps['labelPosition']>('top');
const options = [{
  value: 'email',
  label: '邮箱验证'
}, {
  value: 'phone',
  label: '手机验证',
}];

const codeContent = ref('获取验证码');
const codeSendTime = ref(60);
const codeCanSend = ref(false);

//获取验证码触发验证回调
const onSuccessForCode = (captcha: any) => {
  Object.assign(captchaForm, captcha);
  getForgetPwdVerifyI();
};
const refreshForCode = () => {
  verifyRefCode.value.refresh();
};

/**
 * 获取验证码验证
 */
const getForgetPwdVerifyCode = () => {
  if ('' == forgetPwdForm.username) {
    ElMessage.error('请输入待找回的账号')
    return;
  }
  nextTick(() => {
    showVerifyForCode.value = true;
  });
}

/**
 * 获取验证码接口
 */
const getForgetPwdVerifyI = () => {
  checkClickTextCaptcha(captchaForm.nonceStr, captchaForm.value).then((res: any) => {
    if (res.code === 0) {
      showVerifyForCode.value = false;
      getForgetPwdCode(forgetPwdForm.verifyType, forgetPwdForm.username).then((res: any) => {
        if (res.code === 0) {
          codeCanSend.value = true;
          codeContent.value = codeSendTime.value + 's';
          let timer = setInterval(async () => {
            codeSendTime.value--;
            codeContent.value = codeSendTime.value + 's';
            if (codeSendTime.value < 0) {
              clearInterval(timer);
              codeContent.value = '获取验证码';
              codeSendTime.value = 60;
              codeCanSend.value = false;
            }
          }, 1000);
        } else {
          codeCanSend.value = true;
          codeSendTime.value = 10;
          codeContent.value = codeSendTime.value + 's';
          let timer = setInterval(async () => {
            codeSendTime.value--;
            codeContent.value = codeSendTime.value + 's';
            if (codeSendTime.value < 0) {
              clearInterval(timer);
              codeContent.value = '获取验证码';
              codeSendTime.value = 60;
              codeCanSend.value = false;
            }
          }, 1000);
        }
      })
    } else {
      verifyRefCode.value.refresh();
    }
  })
}

//提交验证码回调
const onSuccess = (captcha: any) => {
  Object.assign(captchaForm, captcha);
  handleCheck();
};

const refresh = () => {
  verifyRef.value.refresh();
};

const handleConfirm = () => {
  forgetPwdFormRef.value.validate().then(() => {
    nextTick(() => {
      showVerify.value = true;
    });
  })
};

const handleCheck = () => {
  checkSlideCaptcha(captchaForm.nonceStr, captchaForm.value).then((res: any) => {
    if (res.code === 0) {
      showVerify.value = false;
      loadingWait.value = true;
      let pwd = forgetPwdForm.password;
      forgetPwdForm.confirmPassword = '';
      encryptFiled(forgetPwdForm.password).then(encryptPassword => {
        forgetPwdForm.password = encryptPassword;
        let obj = {identify: '', code: '', type: '', pwd: ''};
        obj.identify = forgetPwdForm.username;
        obj.code = forgetPwdForm.verifyCode;
        obj.type = forgetPwdForm.verifyType;
        obj.pwd = forgetPwdForm.password;
        sendForgetPwdRequest(obj).then((res: any) => {
          if (res.code === 0) {
            ElMessage.success('您的密码已修改，请重新登录')
            setTimeout(() => {
              handleBackHuiiro();
            }, 2600);
          }
        });
        forgetPwdForm.password = pwd;
        loadingWait.value = false;
      });
    } else {
      verifyRef.value.refresh();
    }
  })
};

/**
 * 参数校验
 */
//@ts-ignore
const validateUsername = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  }
  callback()
}

//@ts-ignore
const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (forgetPwdForm.confirmPassword !== '') {
      if (!forgetPwdFormRef.value) return
      forgetPwdFormRef.value.validateField('confirmPassword', () => null)
    }
    callback()
  }
}

//@ts-ignore
const validatePassConfirm = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请确认密码'))
  } else if (value !== forgetPwdForm.password) {
    callback(new Error("两次密码不一致"))
  } else {
    callback()
  }
}
const forgetPwdRules = {
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
  ],
  verifyCode: [
    {required: true, message: '请确认验证码', trigger: 'blur'},
    {min: 4, max: 8, message: '验证码长度有误', trigger: 'blur'},
  ]
};

const handleBackHuiiro = () => {
  window.location.href = 'https:www.huiiro.com';
}

const handleBackHuiiroHelp = () => {
  ElMessage.info('contact us with \'1659009445@qq.com\'');
}
</script>

<style scoped lang="scss">
:deep(.el-input__validateIcon) {
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

.forgetPwd-container {
  width: 100%;
  height: 100vh;
  background: url('@/assets/imgs/background/login-background.png') no-repeat center center;
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
}

.forgetPwd-form {
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

.forgetPwd-form-item {
  align-items: center;
  margin-bottom: 18px;
  cursor: pointer;
  user-select: none;
  width: 360px;
}

.forgetPwd-form-item-rem {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .el-form-item__content {
    display: none;
  }
}


.forgetPwd-form-item-spec-item {
  margin-left: 3px;
  color: #9499a0;
  float: right;
}

.forgetPwd-form-item-spec-item:hover {
  cursor: pointer;
  color: $global-hover-color;
}

.forgetPwd-button {
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