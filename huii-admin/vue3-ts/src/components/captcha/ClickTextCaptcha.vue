<template>
  <div class="slide-verify no-transition" :style="{width: canvasWidth + 'px'}" onselectstart="return false;">
    <div :class="{'img-loading': isLoading}" :style="{height: canvasHeight + 'px'}" v-if="isLoading"/>
    <div class="success-hint" :style="{height: canvasHeight + 'px'}" v-if="verifySuccess">{{ successHint }}</div>
    <div class="refresh-icon" @click="refresh"/>
    <img ref="canvas" class="slide-canvas" :width="canvasWidth" :height="canvasHeight"
         @click="handleClick"/>
    <div class="btn-1" :style="{ left: iconPositions[1].x + 'px', top: iconPositions[1].y + 'px' }"
         v-show="iconPositions[1].x > 0" @click="handleIconClick(1)">
    </div>
    <div class="btn-2" :style="{ left: iconPositions[2].x + 'px', top: iconPositions[2].y + 'px' }"
         v-show="iconPositions[2].x > 0" @click="handleIconClick(2)">
    </div>
    <div class="btn-3" :style="{ left: iconPositions[3].x + 'px', top: iconPositions[3].y + 'px' }"
         v-show="iconPositions[3].x > 0" @click="handleIconClick(3)">
    </div>
    <div class="slider"
         :class="{'verify-active': verifyActive, 'verify-success': verifySuccess, 'verify-fail': verifyFail}">
      <span class="slider-hint">{{ clickText }}</span>
    </div>
  </div>
</template>

<script>
import {genClickTextCaptcha} from "@/api/auth/captcha/index.ts";

export default {
  name: 'sliderVerify',
  props: {
    canvasWidth: {
      type: Number,
      default: 320,
    },
    canvasHeight: {
      type: Number,
      default: 155,
    },
  },
  data() {
    return {
      verifyActive: false,
      verifySuccess: false,
      verifyFail: false,
      canvasCtx: null,
      image: undefined,
      isLoading: true,
      timestamp: null,
      successHint: '',
      nonceStr: undefined,
      clickText: '',
      currentIcon: 1,
      clickedPosition: {x: 0, y: 0},
      iconPositions: {
        1: {x: 0, y: 0},
        2: {x: 0, y: 0},
        3: {x: 0, y: 0}
      },
      clickedPoints: [],
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.initDom();
    },
    initDom() {
      this.getCaptcha();
    },
    getCaptcha() {
      let self = this;
      const data = {};
      genClickTextCaptcha(data).then((response) => {
        const data = response.data;
        self.nonceStr = data.nonceStr;
        self.clickText = data.clickText;
        self.$refs.canvas.src = data.clickSrc;
      }).finally(() => {
        self.isLoading = false;
      });
    },
    handleClick(event) {
      const rect = this.$refs.canvas.getBoundingClientRect();
      const x = event.clientX - rect.left - 14;
      const y = event.clientY - rect.top - 26;

      if (this.currentIcon <= 3) {
        this.clickedPoints.push({x, y});
        this.iconPositions[this.currentIcon.toString()] = {x, y};
        if (this.currentIcon === 3) {
          this.isLoading = true;
          this.verifyActive = false;
          if (this.timestamp > 20000) {
            this.verifyFailEvent();
          } else {
            //TODO data json
            let value = [this.iconPositions[1], this.iconPositions[2], this.iconPositions[3]]

            this.$emit('success', {
              nonceStr: this.nonceStr,
              value: encodeURIComponent(JSON.stringify(value))
            });
          }
        }
        this.currentIcon++;
      }
    },
    handleIconClick(iconNumber) {
      if (this.currentIcon - 1 === iconNumber) {
        this.currentIcon--;
        const x = 0;
        const y = 0;
        this.iconPositions[this.currentIcon.toString()] = {x, y};
        this.clickedPoints.pop()
      }
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
      this.currentIcon = 1;
      this.clickedPoints = [];
      this.iconPositions = {
        1: {x: 0, y: 0},
        2: {x: 0, y: 0},
        3: {x: 0, y: 0}
      };
      this.getCaptcha();
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

/**/
/*background: url("@/assets/images/light.png") 0 -362;*/
/*background: url("@/assets/images/light.png") 0 -219;*/
/*background: url("@/assets/images/light.png") 0 -324;*/
/*background: url("@/assets/images/light.png") 0 -289;*/
/*background: url("@/assets/images/light.png") 0 -254;*/
/**/

.btn-1 {
  cursor: pointer;
  position: absolute;
  width: 35px;
  height: 35px;
  background: url("@/assets/imgs/captcha/light.png") 0 -362px;
  background-size: 35px 470px;
  z-index: 9;
  transform: scale(0.7);
}

.btn-2 {
  cursor: pointer;
  position: absolute;
  width: 35px;
  height: 35px;
  background: url("@/assets/imgs/captcha/light.png") 0 -219px;
  background-size: 35px 470px;
  z-index: 9;
  transform: scale(0.7);
}

.btn-3 {
  cursor: pointer;
  position: absolute;
  width: 35px;
  height: 35px;
  background: url("@/assets/imgs/captcha/light.png") 0 -324px;
  background-size: 35px 470px;
  z-index: 9;
  transform: scale(0.7);
}

.slide-canvas {
  border-radius: 5px;
  pointer-events: auto;
  z-index: 1;
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

.slider-button:hover .slider-button-icon {
  background-position: 0 -13px
}

.verify-active .slider-button {
  height: 38px;
  top: -1px;
  border: 1px solid #1991FA;
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

