<template>
  <div
      style="width: 400px; height: 200px; display: flex; justify-content: center; align-content: center; user-select: none">
    <el-form :model='form' :rules="formRules" ref="formRef">
      <el-form-item label="邮箱" label-width="70" prop="identify">
        <div style="display: flex; text-align: center; justify-content: center; gap: 20px">
          <el-input v-model="identity" :prefix-icon="Message"
                    placeholder="输入邮箱"
                    style="width: 220px" size="large"/>
          <el-button size="large" @click="getCode" :disabled="codeCanSend">{{ codeContent }}</el-button>
        </div>
      </el-form-item>
      <el-form-item label="验证码" label-width="70" prop="code">
        <el-input v-model="form.code" :prefix-icon="Iphone"
                  placeholder="输入验证码"
                  size="large"/>
      </el-form-item>
      <el-form-item label="新密码" label-width="70" prop="pwd">
        <el-input v-model="form.pwd" :prefix-icon="Lock"
                  placeholder="新密码至少为6位，且包含数字和字母"
                  size="large"
                  type="password" show-password autocomplete="new-password"/>
      </el-form-item>
      <el-form-item style="display: flex;">
        <el-button style="margin-left: 180px" size="default" @click="tryResetPwd">提交</el-button>
      </el-form-item>
    </el-form>
  </div>

</template>

<script setup lang="ts">
import {forgetUserPwd, getForgetUserPwdCheckCode} from "@/api/system/userProfile";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import {Iphone, Lock, Message} from "@element-plus/icons-vue";
import {encryptFiled} from "@/utils/encrypt.ts";

/**
 * 获取验证码
 */
const type = ref('email'); // only email and phone
const identity = ref('');
const getCode = () => {
  if (identity.value.length <= 0) {
    ElMessage.error('请填写邮箱');
    return;
  }
  getForgetUserPwdCheckCode(type.value, identity.value).then(res => {
    if (res.code === 0) {
      codeCanSend.value = true;
      codeContent.value = codeSendTime.value + 's';
      let timer = setInterval(() => {
        codeSendTime.value--;
        codeContent.value = codeSendTime.value + 's';
        if (codeSendTime.value < 0) {
          clearInterval(timer);
          codeContent.value = '获取验证码';
          codeSendTime.value = 60;
          codeCanSend.value = false;
        }
      }, 1000)
    }
  })
}

const codeContent = ref('获取验证码')
const codeSendTime = ref(60)
const codeCanSend = ref(false)

/**
 * 表单
 */
const form = ref({
  identify: '',
  code: '',
  type: '',
  pwd: ''
});
const formRef = ref();
const formRules = {
  identify: [
    {required: false, message: '请输入邮箱', trigger: 'blur'},
  ],
  code: [
    {required: true, message: '请输入验证码', trigger: 'blur'},
    {min: 4, max: 8, message: '验证码长度有误', trigger: 'blur'},
  ],
  pwd: [
    {required: true, message: '请输入新密码', trigger: 'blur'},
    {min: 6, max: 20, message: '新密码长度应在6至20位', trigger: 'blur'},
  ]
};
const tryResetPwd = () => {
  form.value.type = type.value;
  form.value.identify = identity.value;
  formRef.value.validate().then(() => {
    let tp = form.value.pwd;
    encryptFiled(form.value.pwd).then(encryptPwd => {
      form.value.pwd = encryptPwd;
      forgetUserPwd(form.value).then((res) => {
        if (res.code === 0) {
          location.reload();
        }
      })
      form.value.pwd = tp;
    })
  })

}
</script>

<style scoped lang="scss">

</style>