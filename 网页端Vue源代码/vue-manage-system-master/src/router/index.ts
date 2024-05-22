import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';
import { usePermissStore } from '../store/permiss';
import Home from '../views/home.vue';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';

const routes: RouteRecordRaw[] = [
    // {
    //     path: '/',
    //     redirect: '/login',
    // },
    {
        path: '/',
        name: 'Home',
        component: Home,
        children: [
            {
                path: '/system-evaluate',
                name: 'evaluate',
                meta: {
                    title: '评价管理',
                    permiss: '15',
                },
                component: () => import(/* webpackChunkName: "order" */ '../views/system/evaluate.vue'),
            },
            {
                path: '/system-adorder',
                name: 'adorder',
                meta: {
                    title: '订单管理',
                    permiss: '14',
                },
                component: () => import(/* webpackChunkName: "order" */ '../views/system/adorder.vue'),
            },
            {
                path: '/dashboard',
                name: 'dashboard',
                meta: {
                    title: '系统首页',
                    noAuth: true,
                    permiss: '01'
                },
                component: () => import(/* webpackChunkName: "dashboard" */ '../views/dashboard.vue'),
            },
            {
                path: '/system-user',
                name: 'system-user',
                meta: {
                    title: '商家管理',
                    permiss: '11',
                },
                component: () => import(/* webpackChunkName: "system-user" */ '../views/system/user.vue'),
            },
            {
                path: '/system-role',
                name: 'system-role',
                meta: {
                    title: '用户管理',
                    permiss: '12',
                },
                component: () => import(/* webpackChunkName: "system-role" */ '../views/system/role.vue'),
            },
            {
                path: '/system-menu',
                name: 'system-menu',
                meta: {
                    title: '菜单管理',
                    permiss: '13',
                },
                component: () => import(/* webpackChunkName: "system-menu" */ '../views/system/menu.vue'),
            },
            {
                //商家-个人中心
                path: '/ucenter',
                name: 'ucenter',
                meta: {
                    title: '个人中心',
                    permiss: '100',
                },
                component: () => import(/* webpackChunkName: "ucenter" */ '../views/ucenter.vue'),
            },
            {
                //商家-订单管理
                path: '/order',
                name: 'order',
                meta: {
                    title: '订单管理',
                    permiss: '101',
                },
                component: () => import(/* webpackChunkName: "order" */ '../views/ordermanage/order.vue'),
            },
            {
                //商家-评价管理
                path: '/uevaluate',
                name: 'uevaluate',
                meta: {
                    title: '评价管理',
                    permiss: '102',
                },
                component: () => import(/* webpackChunkName: "order" */ '../views/uevaluate.vue'),
            },
            {
                //商家-首页
                path: '/datacenter',
                name: 'datacenter',
                meta: {
                    title: '数据中心',
                    permiss: '02',
                },
                component: () => import(/* webpackChunkName: "dashboard" */ '../views/datacenter.vue'),
            },
            {
                path: '/after',
                name: 'after',
                meta: {
                    title: '售后',
                    permiss: '104',
                },
                component: () => import(/* webpackChunkName: "after" */ '../views/pages/after.vue'),
            },
            {
                path: '/dish',
                name: 'dish',
                meta: {
                    title: '菜品管理',
                    permiss: '105',
                },
                component: () => import(/* webpackChunkName: "dish" */ '../views/pages/dish.vue'),
            },
        ],
    },
    {
        path: '/login',
        meta: {
            title: '登录',
            noAuth: true,
        },
        component: () => import(/* webpackChunkName: "login" */ '../views/pages/login.vue'),
    },
    {
        path: '/register',
        meta: {
            title: '注册',
            noAuth: true,
        },
        component: () => import(/* webpackChunkName: "register" */ '../views/pages/register.vue'),
    },
    {
        path: '/reset-pwd',
        meta: {
            title: '重置密码',
            noAuth: true,
        },
        component: () => import(/* webpackChunkName: "reset-pwd" */ '../views/pages/reset-pwd.vue'),
    },
    {
        path: '/403',
        meta: {
            title: '没有权限',
            noAuth: true,
        },
        component: () => import(/* webpackChunkName: "403" */ '../views/pages/403.vue'),
    },
    {
        path: '/404',
        meta: {
            title: '找不到页面',
            noAuth: true,
        },
        component: () => import(/* webpackChunkName: "404" */ '../views/pages/404.vue'),
    },
    {
        path: '/theme',
        name: 'theme',
        meta: {
            title: '主题设置',
            permiss: '7',
        },
        component: () => import(/* webpackChunkName: "theme" */ '../views/pages/theme.vue'),
    },
    { path: '/:path(.*)', redirect: '/404' },
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

router.beforeEach((to, from, next) => {
    NProgress.start();
    const role = localStorage.getItem('vuems_name');
    const permiss = usePermissStore();

    if (!role && to.meta.noAuth !== true) {
        next('/login');
    } else if (typeof to.meta.permiss == 'string' && !permiss.key.includes(to.meta.permiss)) {
        // 如果没有权限，则进入403
        next('/403');
    } else {
        next();
    }
});

router.afterEach(() => {
    NProgress.done();
});

export default router;
