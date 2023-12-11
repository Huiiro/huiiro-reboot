import {defineStore} from "pinia";
import {removeAccessToken, removeRefreshToken, removeUserInfo} from "@/utils/token.ts";

export const useUserStore = defineStore('user', {
    state: () => {
        return {
            isLogin: false,
            menuList: [],
            tabList: [{name: '首页', title: '首页', icon: ''}],
            tabValue: '首页',
        }
    },
    actions: {
        clearLoginInfo() {
            this.isLogin = false;
            this.menuList = [];
            this.tabList = [{name: '首页', title: '首页', icon: ''}];
            this.tabValue = '首页';
            removeAccessToken();
            removeRefreshToken();
            removeUserInfo();
        },
        setLoginStatus(isLogin: boolean) {
            this.isLogin = isLogin
        },
        setMenuList(menuList: any) {
            this.menuList = menuList
        },
        addTab(tab: any) {
            if (tab.name !== null && tab.title !== null && tab.name !== undefined) {
                let index = this.tabList.findIndex(i => i.name === tab.name)
                if (index === -1) {
                    this.tabList.push({
                        name: tab.name!,
                        title: tab.title || tab.name,
                        icon: tab.icon,
                        params: tab.params
                    })
                } else {
                    this.tabList[index].params = tab.params;
                }
                this.tabValue = tab.name
            }
        }
    },
    getters: {}
})
