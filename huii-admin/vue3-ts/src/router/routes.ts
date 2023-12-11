/**
 * 静态路由
 */
export const constRoutes = [
    {
        name: '404',
        path: '/404',
        meta: {},
        component: () => import('@/views/errorPage/404.vue')
    },
    {
        name: 'test',
        path: '/test',
        meta: {},
        component: () => import('@/views/Test.vue')
    },
    {
        name: 'login',
        path: '/login',
        meta: {
            keepAlive: false,
            breadcrumb: false
        },
        component: () => import('@/views/login/Login.vue')
    },
    {
        name: 'register',
        path: '/register',
        meta: {
            keepAlive: false,
            breadcrumb: false
        },
        component: () => import('@/views/register/Register.vue')
    },
    {
        path: '/',
        name: 'root',
        redirect: '/index',
        component: () => import('@/components/layout/Index.vue'),
        children: [
            {
                name: '首页',
                path: '/index',
                meta: {},
                component: () => import('@/views/index/Index.vue'),
            }
        ]
    },
]

/**
 * 放行白名单
 * 不需要认证 token
 */
export const whiteList = ["/register"]

/**
 * 校验是否在白名单内
 * @param next
 */
export const checkInWhiteList = (next: string) => {
    return whiteList.includes(next);
}