import {createRouter, createWebHistory, NavigationGuardNext, RouteLocationNormalized, RouteRecordRaw} from "vue-router";
import {checkInWhiteList, constRoutes} from "@/router/routes.ts";
import {getAccessToken, removeAccessToken, setAccessToken} from "@/utils/token.ts";
import {useUserStore} from '@/store/modules/user.ts';
import {getRoutes} from "@/api/system/menu";
import settings from "@/settings.ts";
//@ts-ignore
import nprocess from 'nprogress';
import "nprogress/nprogress.css";
import {getInfo} from "@/api/auth/login";

interface MenuItem {
    path: string;
    name: string;
    meta: any;
    component: string | null;
    children?: MenuItem[];
}

const router = createRouter({
    history: createWebHistory(settings.baseUrl),
    routes: [...constRoutes],
    scrollBehavior() {
        return {
            left: 0,
            top: 0
        }
    }
})

const staticRoute =
    {
        path: '/:pathMatch(.*)*',
        redirect: '/404'
    }

const modules = import.meta.glob('../views/**/**.vue')

function generateRoutes(menuData: MenuItem[]): RouteRecordRaw[] {
    const routes: RouteRecordRaw[] = [];

    for (const item of menuData) {
        const route: any = {
            path: item.path,
            name: item.name,
            meta: item.meta || null,
            component: null,
        };
        if (item.component && item.component !== '') {
            route.component = modules[`../views/${item.component}.vue`]
        }
        if (item.children && item.children.length) {
            route.children = generateRoutes(item.children);
        }
        routes.push(route);
    }
    return routes;
}

//@ts-ignore
router.beforeEach(async (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
    nprocess.start();
    //放行白名单
    if (checkInWhiteList(to.path)) {
        next();
    }
    let token: any = getAccessToken();
    let queryToken: any = to.query.token;
    if (queryToken && !token) {
        setAccessToken(queryToken);
        token = queryToken;
    }
    if (!token && to.path !== '/login') {
        next({path: "/login"});
    }
    if (token && to.path === '/login') {
        next({path: "/index"});
    }

    const userStore = useUserStore();
    if (to.meta.breadcrumb) {
        userStore.addTab({
            name: to.name,
            title: to.meta.title,
            icon: to.meta.icon,
            params: to.params
        })
    }

    const isLogin = userStore.isLogin;
    if (token && !isLogin) {
        const response: any = await getRoutes();
        let code = response.code;
        if (code === 0) {
            let menus = response.data;
            const generatedRoutes: any = generateRoutes(menus);
            const indexRoute: any = constRoutes.find(item => item.name === 'root');
            for (let i = 0; i < generatedRoutes.length; i++) {
                indexRoute.children.push(generatedRoutes[i]);
            }
            userStore.setLoginStatus(true);
            userStore.setMenuList(menus);
            const info: any = await getInfo();
            userStore.setPermissions(info.data.permissions)
            userStore.setRoles(info.data.roles)
            router.addRoute(indexRoute);
            router.addRoute(staticRoute);
            next({path: to.path});
        } else if (code == 401 || code == 409) {
            removeAccessToken();
            next({path: "/login"});
        }
    } else {
        next();
    }
});

//@ts-ignore
router.afterEach((to: RouteLocationNormalized, from: RouteLocationNormalized) => {
    nprocess.done();
    if (typeof to.meta.title === 'string') {
        document.title = settings.headerTitle + ' | ' + to.meta.title;
    } else {
        document.title = settings.headerTitle;
    }
});

export default router;