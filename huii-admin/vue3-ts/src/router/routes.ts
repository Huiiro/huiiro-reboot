import {profileConstRoutes} from "@/router/profiles.ts";

/**
 * 静态路由
 */
export const constRoutes = [
    {
        //404
        name: '404',
        path: '/404',
        meta: {},
        component: () => import('@/views/errorPage/404.vue')
    },
    {
        //重定向页面
        path: '/oauth/redirect',
        component: () => import('../views/redirect/OauthRedirect.vue')
    },
    {
        //登陆页面
        name: 'login',
        path: '/login',
        meta: {
            keepAlive: false,
            breadcrumb: false
        },
        component: () => import('@/views/login/Login.vue')
    },
    {
        //注册页面
        name: 'register',
        path: '/register',
        meta: {
            keepAlive: false,
            breadcrumb: false
        },
        component: () => import('@/views/register/Register.vue')
    },
    {
        //找回密码页面
        name: 'forgetPwd',
        path: '/forgetPwd',
        meta: {
            keepAlive: false,
            breadcrumb: false
        },
        component: () => import('@/views/forgetPwd/ForgetPwd.vue')
    },
    {
        path: '/',
        name: 'root',
        redirect: '/index',
        component: () => import('@/components/layout/Index.vue'),
        children: [
            {
                //首页
                name: '首页',
                path: '/index',
                meta: {},
                component: () => import('@/views/index/Index.vue'),
            },
            {
                //个人资料页面
                name: '个人中心',
                path: '/profile',
                redirect: '/profile/my',
                meta: {
                    keepAlive: false,
                    breadcrumb: true,
                    icon: "User",
                    title: "个人中心"
                },
                component: () => import('@/views/profile/Index.vue'),
                children: profileConstRoutes
            },
            {
                //消息页
                name: '我的消息',
                path: '/message',
                meta: {
                    keepAlive: false,
                    breadcrumb: true,
                    icon: "Message",
                    title: "我的消息"
                },
                component: () => import('@/views/message/Index.vue'),
            }
        ]
    },
]

/**
 * 放行白名单
 * 不需要认证 token
 */
export const whiteList = ["/register","/forgetPwd"]

/**
 * 校验是否在白名单内
 * @param next
 */
export const checkInWhiteList = (next: string) => {
    return whiteList.includes(next);
}