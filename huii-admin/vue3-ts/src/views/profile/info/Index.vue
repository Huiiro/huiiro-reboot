<template>
  <div class="p-div">
    <p class="p-header">
      信息管理
    </p>
    <el-form>
      <el-form-item>
        <p style="width: 320px">当前绑定手机： {{ phoneBind }} </p>
        <p style="cursor: pointer" @click="handleClickPhoneDialog">
          <span v-if="phoneBindStatus === '1'">换绑</span>
          <span v-if="phoneBindStatus === '0'">去绑定</span>
        </p>
      </el-form-item>
      <el-form-item>
        <p style="width: 320px">当前绑定邮箱： {{ emailBind }} </p>
        <p style="cursor: pointer" @click="handleClickEmailDialog">
          <span v-if="emailBindStatus === '1'">换绑</span>
          <span v-if="emailBindStatus === '0'">去绑定</span>
        </p>
      </el-form-item>
    </el-form>
  </div>

  <el-dialog v-model="bindEmailDialog"
             :close-on-click-modal="false"
             title="绑定邮箱"
             style="width: 480px;"
             @close="handleCloseEmail">
    <div v-if="bindEmailDialogPage1">
      <el-form>
        <el-form-item label="旧邮箱">
          <el-input v-model="email1"/>
        </el-form-item>
        <el-form-item label="验证码">
          <el-input style="width: 264px" v-model="code1"/>
          <el-button style="margin-left: 20px; width: 100px"
                     @click="getEmailCode1" :disabled="emailCode1CanSend">{{ emailCode1Content }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div v-if="bindEmailDialogPage2">
      <el-form>
        <el-form-item label="新邮箱">
          <el-input v-model="email2"/>
        </el-form-item>
        <el-form-item label="验证码">
          <el-input style="width: 264px" v-model="code2"/>
          <el-button style="margin-left: 20px; width: 100px"
                     @click="getEmailCode2" :disabled="emailCode2CanSend">{{ emailCode2Content }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div>
        <el-button @click="handleCloseEmail">取消</el-button>
        <el-button @click="emailFormUpload1" v-if="showEmailNext">下一步</el-button>
        <el-button @click="emailFormUpload2" v-if="showEmailSubmit">提交</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="bindPhoneDialog"></el-dialog>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {
  checkBindEmailCode,
  checkUnbindEmailCode,
  getBind,
  getBindEmailCode,
  getUnbindEmailCode
} from "@/api/system/userProfile";
import {ElMessage} from "element-plus";

const phoneBind = ref();
const emailBind = ref();
const phoneBindStatus = ref();
const emailBindStatus = ref();

onMounted(() => {
  getBind().then(res => {
    emailBind.value = res.data.email;
    phoneBind.value = res.data.phone;
    emailBindStatus.value = res.data.emailStatus;
    phoneBindStatus.value = res.data.phoneStatus;
  })
})

/**
 * 绑定邮箱对话框
 */
const bindEmailDialog = ref(false);
const bindEmailDialogPage1 = ref(true);
const bindEmailDialogPage2 = ref(false);

const showEmailNext = ref(true);
const showEmailSubmit = ref(false);

const handleClickEmailDialog = () => {
  if (emailBindStatus.value === '1') {
    bindEmailDialog.value = true;
  } else if (emailBindStatus.value === '0') {
    bindEmailDialog.value = true;
    bindEmailDialogPage1.value = false;
    bindEmailDialogPage2.value = true;
    showEmailNext.value = false;
    showEmailSubmit.value = true;
  }
}
/**
 * 关闭邮箱对话框
 */
const handleCloseEmail = () => {
  bindEmailDialog.value = false;
  bindEmailDialogPage1.value = true;
  bindEmailDialogPage2.value = false;
  showEmailNext.value = true;
  showEmailSubmit.value = false;
}

/**
 * page1 验证码
 */
const emailCode1Content = ref('获取验证码');
const emailCode1SendTime = ref(60);
const emailCode1CanSend = ref(false);

const email1 = ref('');
const code1 = ref('');
const getEmailCode1 = () => {
  if (email1.value.length <= 0) {
    ElMessage.error('请填写邮箱');
    return;
  }
  getUnbindEmailCode(email1.value).then(res => {
    if (res.code === 0) {
      emailCode1CanSend.value = true;
      emailCode1Content.value = emailCode1SendTime.value + 's';
      let timer = setInterval(() => {
        emailCode1SendTime.value--;
        emailCode1Content.value = emailCode1SendTime.value + 's';
        if (emailCode1SendTime.value < 0) {
          clearInterval(timer);
          emailCode1Content.value = '获取验证码';
          emailCode1SendTime.value = 60;
          emailCode1CanSend.value = false;
        }
      }, 1000)
    }
  })
}

const emailFormUpload1 = () => {
  if (code1.value.length <= 0) {
    ElMessage.error('请输入验证码');
    return;
  }
  let form = {
    email: email1.value,
    code: code1.value
  };
  checkUnbindEmailCode(form).then(res => {
    if (res.code === 0) {
      bindEmailDialogPage1.value = false;
      bindEmailDialogPage2.value = true;
      showEmailNext.value = false;
      showEmailSubmit.value = true;
    }
  })
}

/**
 * page2 验证码
 */
const emailCode2Content = ref('获取验证码');
const emailCode2SendTime = ref(60);
const emailCode2CanSend = ref(false);

const email2 = ref('');
const code2 = ref('');

const getEmailCode2 = () => {
  if (email2.value.length <= 0) {
    ElMessage.error('请填写邮箱');
    return;
  }
  getBindEmailCode(email2.value).then(res => {
    if (res.code === 0) {
      emailCode2CanSend.value = true;
      emailCode2Content.value = emailCode2SendTime.value + 's';
      let timer = setInterval(() => {
        emailCode2SendTime.value--;
        emailCode2Content.value = emailCode2SendTime.value + 's';
        if (emailCode2SendTime.value < 0) {
          clearInterval(timer);
          emailCode2Content.value = '获取验证码';
          emailCode2SendTime.value = 60;
          emailCode2CanSend.value = false;
        }
      }, 1000)
    }
  })
}

const emailFormUpload2 = () => {
  if (code2.value.length <= 0) {
    ElMessage.error('请输入验证码');
    return;
  }
  let form = {
    email: email2.value,
    code: code2.value
  };
  checkBindEmailCode(form).then(res => {
    if (res.code === 0) {
      handleCloseEmail();
    }
  })
}

/**
 * 绑定sms
 */

const bindPhoneDialog = ref(false);

const handleClickPhoneDialog = () => {
  ElMessage.error('暂不支持绑定手机');
}
</script>

<style scoped lang="scss">
.aligned-text {
  display: flex;
  align-items: center;
}

.aligned-text p {
  margin: 0;
}
</style>