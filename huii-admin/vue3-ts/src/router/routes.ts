export const constRoutes = [
    {
        path: '/:pathMatch(.*)*',
        redirect: '/404'
    },
    {
        name: 'test',
        path: '/test',
        meta: {},
        component: () => import('@/views/Test.vue')
    },
    {
        name: '404',
        path: '/404',
        meta: {},
        component: () => import('@/views/errorPage/404.vue')
    },
    {
        name: 'login',
        path: '/login',
        meta: {},
        component: () => import('@/views/login/Login.vue')
    },
    {
        path: '/',
        redirect: '/index'
    },
    {
        name: 'index',
        path: '/index',
        meta: {},
        component: () => import('@/views/index/Index.vue')
    }
]