import {defineStore} from "pinia";
import {removeAccessToken, removeRefreshToken, removeUserInfo} from "@/utils/token.ts";

export const useUserStore = defineStore('user', {
    state: () => {
        return {
            isLogin: false,
            menuList: [],
            permissions: [],
            roles: [],
            tabList: [{name: '首页', title: '首页', icon: '', params: {}}],
            tabValue: '首页',
        }
    },
    actions: {
        clearLoginInfo() {
            this.isLogin = false;
            this.menuList = [];
            this.tabList = [{name: '首页', title: '首页', icon: '', params: {}}];
            this.tabValue = '首页';
            this.permissions = [];
            this.roles = [];
            removeAccessToken();
            removeRefreshToken();
            removeUserInfo();
        },
        setLoginStatus(isLogin: boolean) {
            this.isLogin = isLogin;
        },
        setMenuList(menuList: any) {
            this.menuList = menuList;
        },
        setPermissions(permissions: any) {
            this.permissions = permissions;
        },

        setRoles(roles: any) {
            this.roles = roles;
        },
        addTab(tab: any) {
            if (tab.name !== null && tab.title !== null && tab.name !== undefined) {
                let index = this.tabList.findIndex(i => i.name === tab.name)
                if (index === -1) {
                    this.tabList.push({
                        name: tab.name!,
                        title: tab.title || tab.name,
                        icon: tab.icon,
                        params: tab.params || null
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
