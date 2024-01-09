import {createApp} from 'vue'
//elementUI
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//@ts-ignore
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

//svgIcon
import "virtual:svg-icons-register"

//vueCropper
//https://github.com/xyxiao001/vue-cropper?tab=readme-ov-file
import VueCropper from 'vue-cropper';
import 'vue-cropper/dist/index.css'

//store
import pinia from "@/store";
//router
import router from "@/router";
//global component
import globalComponent from '@/components/index.ts';
//global style
import '@/styles/index.scss'
//vmEditor
import vmEditor from './plugins/vmEditor';

import App from '@/App.vue';

const app = createApp(App)
app.use(ElementPlus, {
    locale: zhCn
})
app.use(pinia);
app.use(router);
app.use(VueCropper);
app.use(globalComponent);
app.use(vmEditor.VMdEditor);
app.use(vmEditor.VMdPreview);
app.mount('#app')