import {createApp} from 'vue'
//elementUI
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//@ts-ignore
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
//svgIcon
import "virtual:svg-icons-register"


//router
import router from "@/router";
//store
import pinia from "@/store";
//global component
import globalComponent from '@/components/index.ts'
//global style
import '@/styles/index.scss'


import App from '@/App.vue'

const app = createApp(App)
app.use(ElementPlus, {
    locale: zhCn
})
app.use(router)
app.use(pinia)
app.use(globalComponent)
app.mount('#app')