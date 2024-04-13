<template>
  <el-upload
      :action="url"
      :drag="drag"
      :limit="limit"
      :multiple="multiple"
      :headers="uploadHeaders"
      :auto-upload="autoUpload"
      :show-file-list="showList"
      :before-upload="beforeUploadInternal"
      :on-success="onSuccessInternal"
      :on-error="onErrorInternal"
  >
    <el-icon class="el-icon--upload">
      <upload-filled/>
    </el-icon>
    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
    <template #tip>
      <div class="el-upload__tip">{{ text }}</div>
      <slot></slot>
      <div class="el-upload__tip" v-html="msg"></div>
    </template>
  </el-upload>
</template>

<script setup lang="ts">
import {UploadFilled} from "@element-plus/icons-vue";
import {ref} from "vue";
import {getAccessToken} from "@/utils/token.ts";
import {ElMessage} from "element-plus";

const msg = ref();
const props = defineProps({
  url: {
    type: String,
    required: true
  },
  drag: {
    type: Boolean,
    default: true,
  },
  limit: {
    type: Number,
    default: 10,
  },
  multiple: {
    type: Boolean,
    default: true,
  },
  autoUpload: {
    type: Boolean,
    default: true,
  },
  showList: {
    type: Boolean,
    default: true,
  },
  text: {
    //上传类型提示文字
    type: String,
    default: '只能上传image/jpeg文件，且不超过2mb',
  },
  type: {
    //文件校验类型，'/' 分割
    type: String,
    default: 'image/jpeg'
  },
  size: {
    //文件校验大小， 单位mb
    type: Number,
    default: 2
  },
  beforeUpload: {
    //自定义校验实现
    type: Function
  },
  onSuccess: {
    //自定义成功回调
    type: Function
  },
  onFail: {
    //自定义失败回调
    type: Function
  }
});
const uploadHeaders = ref({
  "Authorization": `Bearer ${getAccessToken()}`
});

/**
 * before upload
 */
const beforeUploadInternal = (rawFile: any) => {
  //自定义校验逻辑
  if (props.beforeUpload) {
    props.beforeUpload();
  }
  if (props.type === "") {
    return true;
  }
  if (!checkFileSuffix(rawFile.name, props.type)) {
    ElMessage.error('文件格式只能是' + props.type + '格式！');
    return false;
  } else if (rawFile.size / 1024 / 1024 > props.size) {
    ElMessage.error('文件大小不超过' + props.size + 'MB！');
    return false;
  }
  return true;
};

const getFileExtension = (fileName: any) => {
  return fileName.slice((Math.max(0, fileName.lastIndexOf(".")) || Infinity) + 1);
}

const checkFileSuffix = (filename: any, suffix: any) => {
  return suffix.toString().split('/').includes(getFileExtension(filename));
}

const onSuccessInternal = (response: any) => {
  const onSuccessCallback = props.onSuccess || success;
  onSuccessCallback(response);
};

const onErrorInternal = (response: any) => {
  const onFailCallback = props.onFail || error;
  onFailCallback(response);
};

/**
 * 默认成功回调
 * @param response
 */
const success = (response: any) => {
  if (response.code === 0) {
    ElMessage({type: 'success', message: response.message == 'ok' ? '文件已成功上传' : response.message});
    if (response.data && response.data.analysis) {
      msg.value = response.data.analysis;
    }
  } else {
    ElMessage({type: 'error', message: response.message || '文件上传失败，请重试'});
  }
};

/**
 * 默认失败回调
 * @param response
 */
const error = (response: any) => {
  ElMessage({type: 'error', message: response.message || '文件上传失败，请重试'});
};
</script>

<style scoped lang="scss">

</style>