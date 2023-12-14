import {defineStore} from "pinia";

export const useLayoutStore = defineStore('layout', {
    state: () => {
        return {
            //移动端ui适配
            isMobile: false,
            //折叠左侧导航菜单
            isFold: false,
            //顶部刷新按钮
            isRefresh: false,
            //顶部时间显示
            isTimer: true,
            //布局
            tableSize: 'small',
            //top按钮样式
            BtnBack: '#cbc303',
            BtnInsert: '#1fa0e7',
            BtnUpdate: '#08bb1f',
            BtnDelete: '#ee1010',
            BtnImport: '#db69f8',
            BtnExport: '#9b39f3',
            BtnUpload: '#e7a80c',
            BtnClean: '#201e65'
        }
    },
    actions: {
        setIsMobile(b: boolean) {
            this.isMobile = b
        },
        setIsFold() {
            this.isFold = !this.isFold
        },
        setIsRefresh() {
            this.isRefresh = !this.isRefresh
        },
        setIsTimer() {
            this.isTimer = !this.isTimer
        },
        setTableSize(size: 'small' | 'default' | 'large') {
            this.tableSize = size;
        }
    }
})