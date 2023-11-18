<template>
  <div class="slide-verify no-transition" :style="{width: canvasWidth + 'px'}" onselectstart="return false;">
    <div :class="{'img-loading': isLoading}" :style="{height: canvasHeight + 'px'}" v-if="isLoading"/>
    <div class="success-hint" :style="{height: canvasHeight + 'px'}" v-if="verifySuccess">{{ successHint }}</div>
    <div class="refresh-icon" @click="refresh"/>
    <template v-if="isFrontCheck">
      <canvas ref="canvas" class="slide-canvas" :width="canvasWidth" :height="canvasHeight"/>
      <canvas ref="block" class="slide-block" :width="canvasWidth" :height="canvasHeight"/>
    </template>
    <template v-else>
      <img ref="canvas" class="slide-canvas" :width="canvasWidth" :height="canvasHeight"/>
      <img ref="block" :class="['slide-block', {'verify-fail': verifyFail}]"/>
    </template>
    <div class="slider"
         :class="{'verify-active': verifyActive, 'verify-success': verifySuccess, 'verify-fail': verifyFail}">
      <div class="slider-box" :style="{width: sliderBoxWidth}">
        <div class="slider-button" id="slider-button" :style="{left: sliderButtonLeft}">
          <div class="slider-button-icon"/>
        </div>
      </div>
      <span class="slider-hint">{{ sliderHint }}</span>
    </div>
  </div>
</template>

<script>
import {genSlideCaptcha} from "@/api/auth/captcha/index.ts";

function sum(x, y) {
  return x + y;
}

function square(x) {
  return x * x;
}

export default {
  name: 'sliderVerify',
  props: {
    blockLength: {
      type: Number,
      default: 42,
    },
    blockRadius: {
      type: Number,
      default: 10,
    },
    canvasWidth: {
      type: Number,
      default: 320,
    },
    canvasHeight: {
      type: Number,
      default: 155,
    },
    sliderHint: {
      type: String,
      default: '向右滑动',
    },
    accuracy: {
      type: Number,
      default: 3,
    },
    imageList: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      // 前端校验
      isFrontCheck: false,
      // 校验进行状态
      verifyActive: false,
      // 校验成功状态
      verifySuccess: false,
      // 校验失败状态
      verifyFail: false,
      // 阻塞块对象
      blockObj: null,
      // 图片画布对象
      canvasCtx: null,
      // 阻塞块画布对象
      blockCtx: null,
      // 阻塞块宽度
      blockWidth: this.blockLength * 2,
      // 阻塞块的横轴坐标
      blockX: undefined,
      // 阻塞块的纵轴坐标
      blockY: undefined,
      // 图片对象
      image: undefined,
      // 移动的X轴坐标
      originX: undefined,
      // 移动的Y轴做坐标
      originY: undefined,
      // 拖动距离数组
      dragDistanceList: [],
      // 滑块箱拖动宽度
      sliderBoxWidth: 0,
      // 滑块按钮距离左侧起点位置
      sliderButtonLeft: 0,
      // 鼠标按下状态
      isMouseDown: false,
      // 图片加载提示，防止图片没加载完就开始验证
      isLoading: true,
      // 时间戳，计算滑动时长
      timestamp: null,
      // 成功提示
      successHint: '',
      // 随机字符串
      nonceStr: undefined,
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.initDom();
      this.bindEvents();
    },
    initDom() {
      this.blockObj = this.$refs.block;
      if (this.isFrontCheck) {
        this.canvasCtx = this.$refs.canvas.getContext('2d');
        this.blockCtx = this.blockObj.getContext('2d');
        this.initImage();
      } else {
        this.getCaptcha();
      }
    },
    getCaptcha() {
      let self = this;
      const data = {};
      genSlideCaptcha(data).then((response) => {
        console.log('slide component 162 getSlideCaptcha')
        console.log(response)
        const data = response.data;
        self.nonceStr = data.nonceStr;
        self.$refs.block.src = data.blockSrc;
        self.$refs.block.style.top = data.blockY + 'px';
        self.$refs.canvas.src = data.canvasSrc;
      }).finally(() => {
        self.isLoading = false;
      });
    },
    initImage() {
      const image = this.createImage(() => {
        this.drawBlock();
        let {canvasWidth, canvasHeight, blockX, blockY, blockRadius, blockWidth} = this;
        this.canvasCtx.drawImage(image, 0, 0, canvasWidth, canvasHeight);
        this.blockCtx.drawImage(image, 0, 0, canvasWidth, canvasHeight);
        let yAxle = blockY - blockRadius * 2;
        let ImageData = this.blockCtx.getImageData(blockX, yAxle, blockWidth, blockWidth);
        this.blockObj.width = blockWidth;
        this.blockCtx.putImageData(ImageData, 0, yAxle);
        this.isLoading = false;
        this.nonceStr = 'loyer';
      });
      this.image = image;
    },
    createImage(onload) {
      const image = document.createElement('img');
      image.crossOrigin = 'Anonymous';
      image.onload = onload;
      image.onerror = () => {
        image.src = require('@/assets/imgs/captcha/bgImg.jpg');
      };
      image.src = this.getImageSrc();
      return image;
    },
    getImageSrc() {
      const len = this.imageList.length;
      return len > 0 ? this.imageList[this.getNonceByRange(0, len)] : `https://loyer.wang/view/ftp/wallpaper/${this.getNonceByRange(1, 1000)}.jpg`;
    },
    getNonceByRange(start, end) {
      return Math.round(Math.random() * (end - start) + start);
    },
    drawBlock() {
      this.blockX = this.getNonceByRange(this.blockWidth + 10, this.canvasWidth - (this.blockWidth + 10));
      this.blockY = this.getNonceByRange(10 + this.blockRadius * 2, this.canvasHeight - (this.blockWidth + 10));
      this.draw(this.canvasCtx, 'fill');
      this.draw(this.blockCtx, 'clip');
    },
    draw(ctx, operation) {
      const PI = Math.PI;
      let {blockX: x, blockY: y, blockLength: l, blockRadius: r} = this;
      ctx.beginPath();
      ctx.moveTo(x, y);
      ctx.arc(x + l / 2, y - r + 2, r, 0.72 * PI, 2.26 * PI);
      ctx.lineTo(x + l, y);
      ctx.arc(x + l + r - 2, y + l / 2, r, 1.21 * PI, 2.78 * PI);
      ctx.lineTo(x + l, y + l);
      ctx.lineTo(x, y + l);
      ctx.arc(x + r - 2, y + l / 2, r + 0.4, 2.76 * PI, 1.24 * PI, true);
      ctx.lineTo(x, y);
      ctx.lineWidth = 2;
      ctx.fillStyle = 'rgba(255, 255, 255, 0.9)';
      ctx.strokeStyle = 'rgba(255, 255, 255, 0.9)';
      ctx.stroke();
      ctx[operation]();
      ctx.globalCompositeOperation = 'destination-over';
    },
    bindEvents() {
      document.getElementById('slider-button').addEventListener('mousedown', (event) => {
        this.startEvent(event.clientX, event.clientY);
      });
      document.addEventListener('mousemove', (event) => {
        this.moveEvent(event.clientX, event.clientY);
      });
      document.addEventListener('mouseup', (event) => {
        this.endEvent(event.clientX);
      });
      document.getElementById('slider-button').addEventListener('touchstart', (event) => {
        this.startEvent(event.changedTouches[0].pageX, event.changedTouches[0].pageY);
      });
      document.addEventListener('touchmove', (event) => {
        this.moveEvent(event.changedTouches[0].pageX, event.changedTouches[0].pageY);
      });
      document.addEventListener('touchend', (event) => {
        this.endEvent(event.changedTouches[0].pageX);
      });
    },
    checkImgSrc() {
      if (this.isFrontCheck) {
        return true;
      }
      return !!this.$refs.canvas.src;
    },
    startEvent(originX, originY) {
      if (!this.checkImgSrc() || this.isLoading || this.verifySuccess) {
        return;
      }
      this.originX = originX;
      this.originY = originY;
      this.isMouseDown = true;
      this.timestamp = +new Date();
    },
    moveEvent(originX, originY) {
      if (!this.isMouseDown) {
        return false;
      }
      const moveX = originX - this.originX;
      const moveY = originY - this.originY;
      if (moveX < 0 || moveX + 40 >= this.canvasWidth) {
        return false;
      }
      this.sliderButtonLeft = moveX + 'px';
      let blockLeft = (this.canvasWidth - 40 - 20) / (this.canvasWidth - 40) * moveX;
      this.blockObj.style.left = blockLeft + 'px';
      this.verifyActive = true;
      this.sliderBoxWidth = moveX + 'px';
      this.dragDistanceList.push(moveY);
    },
    endEvent(originX) {
      if (!this.isMouseDown) {
        return false;
      }
      this.isMouseDown = false;
      if (originX === this.originX) {
        return false;
      }
      this.isLoading = true;
      this.verifyActive = false;
      this.timestamp = +new Date() - this.timestamp;
      const moveLength = parseInt(this.blockObj.style.left);
      if (this.timestamp > 10000) {
        this.verifyFailEvent();
      } else
      if (!this.turingTest()) {
        this.verifyFail = true;
        this.$emit('again');
      } else
      if (this.isFrontCheck) {
        const accuracy = this.accuracy <= 1 ? 1 : this.accuracy > 10 ? 10 : this.accuracy; // 容错精度值
        const spliced = Math.abs(moveLength - this.blockX) <= accuracy; // 判断是否重合
        if (!spliced) {
          this.verifyFailEvent();
        } else {
          this.$emit('success', {nonceStr: this.nonceStr, value: moveLength});
        }
      } else {
        this.$emit('success', {nonceStr: this.nonceStr, value: moveLength});
      }
    },
    turingTest() {
      const arr = this.dragDistanceList;
      const average = arr.reduce(sum) / arr.length;
      const deviations = arr.map((x) => x - average);
      const stdDev = Math.sqrt(deviations.map(square).reduce(sum) / arr.length);
      return average !== stdDev;
    },
    verifySuccessEvent() {
      this.isLoading = false;
      this.verifySuccess = true;
      const elapsedTime = (this.timestamp / 1000).toFixed(1);
      if (elapsedTime < 1) {
        this.successHint = `仅仅${elapsedTime}S，你的速度快如闪电`;
      } else if (elapsedTime < 2) {
        this.successHint = `只用了${elapsedTime}S，这速度简直完美`;
      } else {
        this.successHint = `耗时${elapsedTime}S，争取下次再快一点`;
      }
    },
    verifyFailEvent(msg) {
      this.verifyFail = true;
      this.$emit('fail', msg);
      this.refresh();
    },
    refresh() {
      setTimeout(() => {
        this.verifyFail = false;
      }, 500);
      this.isLoading = true;
      this.verifyActive = false;
      this.verifySuccess = false;
      this.blockObj.style.left = 0;
      this.sliderBoxWidth = 0;
      this.sliderButtonLeft = 0;
      if (this.isFrontCheck) {
        let {canvasWidth, canvasHeight} = this;
        this.canvasCtx.clearRect(0, 0, canvasWidth, canvasHeight);
        this.blockCtx.clearRect(0, 0, canvasWidth, canvasHeight);
        this.blockObj.width = canvasWidth;
        this.image.src = this.getImageSrc();
      } else {
        this.getCaptcha();
      }
    },
  },
};
</script>

<style scoped>
.slide-verify {
  position: relative;
}

.img-loading {
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
  bottom: 0;
  z-index: 999;
  animation: loading 1.5s infinite;
  background-image: url(@/assets/imgs/captcha/loading.svg);
  background-repeat: no-repeat;
  background-position: center center;
  background-size: 100px;
  background-color: #737c8e;
  border-radius: 5px;
}

@keyframes loading {
  0% {
    opacity: .7;
  }
  100% {
    opacity: 9;
  }
}

.success-hint {
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
  z-index: 999;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.8);
  color: #2CD000;
  font-size: large;
}

.refresh-icon {
  position: absolute;
  right: 0;
  top: 0;
  width: 35px;
  height: 35px;
  cursor: pointer;
  background: url("@/assets/imgs/captcha/light.png") 0 -432px;
  background-size: 35px 470px;
}

.slide-canvas {
  border-radius: 5px;
}

.slide-block {
  position: absolute;
  left: 0;
  top: 0;
}

.slide-block.verify-fail {
  transition: left 0.5s linear;
}

.slider {
  position: relative;
  text-align: center;
  width: 100%;
  height: 40px;
  line-height: 40px;
  margin-top: 15px;
  background: #f7f9fa;
  color: #45494c;
  border: 1px solid #e4e7eb;
  border-radius: 5px;
}

.slider-box {
  position: absolute;
  left: 0;
  top: 0;
  height: 40px;
  border: 0 solid #1991FA;
  background: #D1E9FE;
  border-radius: 5px;
}

.slider-button {
  position: absolute;
  top: 0;
  left: 0;
  width: 40px;
  height: 40px;
  background: #fff;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.3);
  cursor: pointer;
  transition: background .2s linear;
  border-radius: 5px;
}

.slider-button:hover {
  background: #1991FA
}

.slider-button:hover .slider-button-icon {
  background-position: 0 -13px
}

.slider-button-icon {
  position: absolute;
  top: 15px;
  left: 13px;
  width: 15px;
  height: 13px;
  background: url("@/assets/imgs/captcha/light.png") 0 -26px;
  background-size: 35px 470px
}

.verify-active .slider-button {
  height: 38px;
  top: -1px;
  border: 1px solid #1991FA;
}

.verify-active .slider-box {
  height: 38px;
  border-width: 1px;
}

.verify-success .slider-box {
  height: 38px;
  border: 1px solid #52CCBA;
  background-color: #D2F4EF;
}

.verify-success .slider-button {
  height: 38px;
  top: -1px;
  border: 1px solid #52CCBA;
  background-color: #52CCBA !important;
}

.verify-success .slider-button-icon {
  background-position: 0 0 !important;
}

.verify-fail .slider-box {
  height: 38px;
  border: 1px solid #f57a7a;
  background-color: #fce1e1;
  transition: width 0.5s linear;
}

.verify-fail .slider-button {
  height: 38px;
  top: -1px;
  border: 1px solid #f57a7a;
  background-color: #f57a7a !important;
  transition: left 0.5s linear;
}

.verify-fail .slider-button-icon {
  top: 14px;
  background-position: 0 -82px !important;
}

.verify-active .slider-hint,
.verify-success .slider-hint,
.verify-fail .slider-hint {
  display: none;
}
.no-transition {
  transition: none !important;
}
</style>