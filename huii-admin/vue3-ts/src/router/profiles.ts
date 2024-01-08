export const profileConstRoutes = [
    {
        name: "个人资料",
        path: "/profile/my",
        component: () => import('@/views/profile/profile/Index.vue'),
        meta: {
            keepAlive: false,
            breadcrumb: false,
            icon: "UserFilled",
            title: "个人资料"
        }
    },
    {
        name: "账号管理",
        path: "/profile/account",
        component: () => import('@/views/profile/account/Index.vue'),
        meta: {
            keepAlive: false,
            breadcrumb: false,
            icon: "UserFilled",
            title: "账号管理"
        }
    }
];