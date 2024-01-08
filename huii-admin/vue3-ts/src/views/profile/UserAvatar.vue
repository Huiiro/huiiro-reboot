<template>
  <div style="margin: 0;padding: 0">
    <el-avatar :src="src" :size="size" class="avatar" @click="openDialog"/>
    <el-dialog v-model="dialogVisible" width="400px">
      <div class="avatar-upload">
        <div class="avatar-upload-cropper">
          <vueCropper
              ref="cropper"
              :img="options.img"
              :info="options.info"
              :fixed="options.fixed"
              :fixedBox="options.fixedBox"
              :fixedNumber="options.fixedNumber"
              :outputSize="options.outputSize"
              :outputType="options.outputType"
              :autoCrop="options.autoCrop"
              :autoCropWidth="options.autoCropWidth"
              :autoCropHeight="options.autoCropHeight"
              @realTime="realTime"
          ></vueCropper>
        </div>
        <div class="avatar-upload-preview" v-if="false">
          <img :src="previews.url" :style="previews.img" alt=""/>
        </div>
      </div>
      <div class="avatar-operation">
        <el-upload
            ref="upload"
            action="#"
            :limit="1"
            :show-file-list="false"
            :http-request="requestUpload"
            :before-upload="beforeUpload"
        >
          <el-button>上传</el-button>
        </el-upload>
        <el-button @click="rotateRight">
          <el-icon>
            <RefreshRight/>
          </el-icon>
        </el-button>
        <el-button @click="submitAvatar">保存</el-button>

      </div>
    </el-dialog>
  </div>
</template>

<script>
import {RefreshRight} from "@element-plus/icons-vue";
import {updateUserAvatar} from "@/api/system/userProfile/index.ts";
import {getUserInfo, setUserInfo} from "@/utils/token.ts";

export default {
  components: {RefreshRight},
  props: {
    src: {required: true},
    size: {default: 48}
  },
  data() {
    return {
      dialogVisible: false,
      options: {
        img: '',//裁剪图片的地址
        info: true,//裁剪框的大小信息
        fixed: true,//是否开启截图框宽高固定比例
        fixedBox: false,	//固定截图框大小
        fixedNumber: [1, 1],//	截图框的宽高比例, 开启fixed生效
        outputSize: 0.8,//裁剪生成图片的质量 0.1 ~ 1
        outputType: 'jpg',
        autoCrop: true,//是否默认生成截图框
        autoCropWidth: 160,
        autoCropHeight: 160
      },
      previews: {}
    }
  },
  methods: {
    openDialog() {
      this.dialogVisible = true;
      if (this.src) {
        this.options.img = this.$props.src
      }
    },
    beforeUpload(file) {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.options.img = reader.result;
      };
    },
    requestUpload() {

    },
    //提交头像
    submitAvatar() {
      this.$refs.cropper.getCropBlob(data => {
        const file = new File([data], 'avatar.jpg', {type: 'image/jpeg'});
        const formData = new FormData();
        formData.append('file', file);
        updateUserAvatar(formData).then(res => {
          let ui = getUserInfo();
          ui.userAvatar = res.data;
          setUserInfo(ui);
          this.dialogVisible = false;
        })
      })
    },
    realTime(data) {
      this.previews = data;
    },
    rotateRight() {
      this.$refs.cropper.rotateRight();
    },
  }
}
</script>

<style scoped lang="scss">
$a-width: 36px;
.avatar {
  cursor: pointer;
}

.avatar:hover::after {
  /*
  content: '+';
  margin-right: $a-width;
  margin-bottom: 2px;
  color: #eee;
  background: transparent;
  font-size: 24px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  line-height: 120px;
  border-radius: 50%;
  */
}

.avatar-upload {
  width: 360px;
  height: 300px;
  display: flex;
  align-content: center;
  justify-content: center;
  gap: 20px;
}

.avatar-upload-cropper {
  width: 250px;
  height: 250px;
}

.avatar-upload-preview {
  width: 80px;
  height: 80px;
  max-height: 200px;
  max-width: 200px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;
  overflow: hidden;
  background-color: transparent;
}

.avatar-operation {
  display: flex;
  align-content: center;
  justify-content: center;
  gap: 20px;
}
</style>