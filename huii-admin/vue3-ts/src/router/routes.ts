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
            breadcrumb: false
        },
        component: () => import('@/views/login/Login.vue')
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