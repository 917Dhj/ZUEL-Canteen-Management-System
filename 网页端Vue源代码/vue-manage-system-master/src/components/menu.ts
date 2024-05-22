import { Menu } from '@/types/menu';

export const menuData: Menu[] = [
    {
        // 商家-首页
        id: '02',
        title: '数据中心',
        index: '/datacenter',
        icon: 'Odometer',
    },
    {
        //商家-个人中心页面
        id: '100',
        icon: 'HomeFilled',
        index: '/ucenter',
        title: '个人中心',
    },
    {
        //商家-订单管理页面
        id: '101',
        icon: 'Calendar',
        index: '/order',
        title: '订单管理',
    },
    {
        //商家-评价管理页面
        id: '102',
        icon: 'Comment',
        index: '/uevaluate',
        title: '评价管理',
    },
    {
        id: '104',
        title: '售后',
        index: '/after',
        icon: 'Service',
    },
    {
        //商家-菜品管理页面
        id: '105',
        icon: 'Goods',
        index: '/dish',
        title: '菜品管理',
    },
    {
        id: '01',
        title: '首页',
        index: '/dashboard',
        icon: 'Odometer',
    },
    {
        id: '1',
        title: '管理',
        index: '1',
        icon: 'HomeFilled',
        children: [
            {
                id: '11',
                pid: '1',
                index: '/system-user',
                title: '商家管理',
            },
            {
                id: '12',
                pid: '1',
                index: '/system-role',
                title: '用户管理',
            },
            {
                id: '13',
                pid: '1',
                index: '/system-menu',
                title: '菜单管理',
            },
            {
                id: '14',
                pid: '1',
                index: '/system-adorder',
                title: '订单管理',
            },
            {
                id: '15',
                pid: '1',
                index: '/system-evaluate',
                title: '评价管理',
            },
        ],
    },
];
