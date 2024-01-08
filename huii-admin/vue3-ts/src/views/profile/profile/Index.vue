<template>
  <el-form :model="profile"
           :rules="profileRules"
           ref="profileRef">
    <el-form-item label="用户名称" label-width="85" prop="userName">
      <el-input v-model="profile.userName" autocomplete="off"/>
    </el-form-item>
    <el-form-item label="姓名" label-width="85" prop="nickName">
      <el-input v-model="profile.nickName" autocomplete="off"/>
    </el-form-item>
    <el-form-item label="用户性别" label-width="85" prop="sexual">
      <el-radio-group v-model="profile.sexual">
        <el-radio v-for="option in userSexualStatusOptions" :key="option.value" :label="option.value">
          {{ option.label }}
        </el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="自我介绍" label-width="85" prop="nickName">
      <el-input v-model="profile.remark" autocomplete="off"
                placeholder="介绍一下自己吧..."
                type="textarea" rows="6"/>
    </el-form-item>
  </el-form>
  <el-button style="float: right" @click="handleUpdateProfile(profileRef)">更新资料</el-button>
</template>

<script setup lang="ts">
import {getUserProfile, updateUserProfile} from "@/api/system/userProfile";
import {onMounted, ref} from "vue";
import {FormInstance} from "element-plus";
import {userSexualStatusOptions} from "@/views/system/user/dictionary.ts";

onMounted(() => {
  queryUserProfile();
});

/**
 * 获取用户资料
 */
const profile = ref({
  userName: '',
  nickName: '',
  sexual: '',
  remark: ''
});
const profileRules = ref({
  userName: [
    {required: true, message: '请填写用户名', trigger: 'blur'},
    {min: 1, max: 20, message: '用户名长度不超过20个字符', trigger: 'blur'}
  ],
  nickName: [
    {required: false, message: '请填写名称', trigger: 'blur'},
    {min: 1, max: 20, message: '名称长度不超过20个字符', trigger: 'blur'}
  ],
  sexual: [
    {required: true, message: '请选择用户性别', trigger: 'blur'},
  ],
});
const profileRef = ref<FormInstance>();
const queryUserProfile = () => {
  getUserProfile().then(res => {
    profile.value = res.data;
  })
}

/**
 * 更新用户资料
 */
const handleUpdateProfile = async (fr: FormInstance | undefined) => {
  await fr?.validate((valid) => {
    if (valid) {
      updateUserProfile(profile.value).then(res => {
        if (res.code === 0) {
          location.reload();
        }
      })
    }
  });
}
</script>

<style scoped lang="scss">

</style>