import request from '../utils/request';

// 校园网
// const baseUrl = "http://10.169.41.241:8080/MiniProgramManagement_war_exploded/api";
// 宿舍网
// const baseUrl = "http://10.174.158.124:8080/MiniProgramManagement_war_exploded/api";
// 内网穿透
const baseUrl = "http://zuelfanquan.v5.idcfengye.com/MiniProgramManagement_war_exploded/api";
interface userInfo {
    restaurantId: string;
}

interface updateUserInfo {
    restaurantId: string,
    restaurantName: string,
    password: string,
}

interface restaurantName {
    restaurantName: string;
}

interface userId {
    userId: string;
}

interface orderInfo {
    orderId: number;
}

interface orderStatus {
    status: string,
    orderId: number;
}

interface postInfo {
    postId: number;
}

interface posterInfo {
    posterId: string;
}

interface dishInfo {
    dishId: number
}

interface addDish {
    restaurantId: number,
    dishName: string,
    price: string
}

interface dishName {
    dishName: string;
}

interface messageInfo {
    id: string,
    content: string
}

interface restaurantImage {
    image: string,
    restaurantId: string,
}

interface addRestaurant {
    canteenId: string,
    restaurantName: string,
}

export const fetchData = () => {
    return request({
        url: './mock/table.json',
        method: 'get'
    });
};

// 管理员页面商家管理表单数据接口
export const fetchUserData = () => {
    return request({
        url: baseUrl + '/table/admin/user',
        method: 'get'
    });
};

// 管理员页面商家管理表单--筛选
export const fetchUserDataFiltered = (data:restaurantName) => {
    return request({
        url: baseUrl + '/table/admin/user/filter',
        method: 'post',
        data:data
    });
};

// 管理员页面用户管理表单数据接口
export const fetchRoleData = () => {
    return request({
        url: baseUrl + '/table/admin/role',
        method: 'get'
    });
};

// 管理员页面用户管理表单--筛选
export const fetchRoleDataFiltered = (data:userId) => {
    return request({
        url: baseUrl + '/table/admin/role/filter',
        method: 'post',
        data:data
    });
};

// 管理员页面帖子表单数据接口
export const fetchEvaluateData = () => {
    return request({
        url: baseUrl + '/table/admin/post',
        method: 'get'
    });
};

// 管理员页面帖子--筛选指定用户的帖子
export const fetchEvaluateDataFiltered = (data:posterInfo) => {
    return request({
        url: baseUrl + '/table/admin/post/filter',
        method: 'post',
        data:data,
    });
};

// 管理员删除帖子接口
export const deleteEvaluateData = (data:postInfo) => {
    return request({
        url: baseUrl + '/post/delete',
        method: 'post',
        data:data
    });
};

// 获取商家的订单表
export const fetchOrderTableData = (data:userInfo) => {
    return request({
        url: baseUrl + '/table/order',
        method: 'post',
        data:data
    });
};

// 商家改变订单状态
export const updateOrderStatus = (data:orderStatus) => {
    return request({
        url: baseUrl + '/update/order/status',
        method: 'post',
        data:data
    })
}

//获取商家的评价表
export const fetchUserEvaluateData = (data:userInfo) => {
    return request({
        url: baseUrl + '/table/post',
        method: 'post',
        data:data
    });
};

// 管理员页面菜单管理表单数据
export const fetchMenusData = () => {
    return request({
        url: baseUrl + '/table/admin/menu',
        method: 'get'
    });
};

// 管理员页面下架菜品接口
export const deleteMenusData = (data:dishInfo) => {
    return request({
        url: baseUrl + '/course/delete',
        method: 'post',
        data:data
    });
};

// 上架菜品接口
export const onsaleMenusData = (data:dishInfo) => {
    return request({
        url: baseUrl + '/course/onsale',
        method: 'post',
        data:data
    });
}

// 管理员菜品管理页面--筛选菜品
export const fetchMenusDataFiltered = (data:dishName) => {
    return request({
        url: baseUrl + '/table/admin/menu/filter',
        method: 'post',
        data:data
    });
};

// 管理员订单管理页面表单数据接口
export const fetchAdorderData = () => {
    return request({
        url: baseUrl + '/table/admin/order',
        method: 'get'
    });
};

// 管理员订单管理页面表单--筛选
export const fetchAdorderDataFiltered = (data:orderInfo) => {
    return request({
        url: baseUrl + '/table/admin/order/filter',
        method: 'post',
        data:data
    });
};

// 确认登录信息接口
interface LoginInfo {
    username: string;
    password: string;
}
export const confirmLogin = (data:LoginInfo) => {
    return request({
        url: baseUrl + '/admin/login', // 这里是你要发送 POST 请求的 URL
        method: 'post',
        data: data // 这里是要发送的数据
    });
};

// 获取登录信息接口
export const getRestaurantInfo = (data: userInfo) => {
    return request({
        url: baseUrl + '/restaurant/login/info',
        method: 'post',
        data: data
    })
}

// 修改商家信息接口
export const updateLoginInfo = (data: updateUserInfo) => {
    return request({
        url: baseUrl + '/restaurant/info/update',
        method: 'post',
        data: data
    })
}

// 修改商家头像接口
export const updateRestaurantImage = (data: restaurantImage) => {
    return request({
        url: baseUrl + '/upload/restaurantImage',
        method: 'post',
        data: data
    })
}

// 商家订单量排行榜数据接口
export const fetchOrderRankData = () => {
    return request({
        url: baseUrl + '/analysis/orderRank',
        method: 'get'
    });
};

// 数据中心今日统计数据接口
export const fetchTodayData = (data:userInfo) => {
    return request({
        url: baseUrl + '/analysis/todayData',
        method: 'post',
        data: data
    });
};

// 管理员首页统计数据接口
export const fetchTotalData = () => {
    return request({
        url: baseUrl + '/analysis/total',
        method: 'get'
    });
};

// 管理员首页最新评价列表数据
export const fetchNewPostData = () => {
    return request({
        url: baseUrl + '/analysis/post',
        method: 'get'
    });
};

// 管理员首页柱状图数据
export const fetchDash1Data = () => {
    return request({
        url: baseUrl + '/analysis/dash1',
        method: 'get'
    })
}

// 管理员首页环形图数据
export const fetchDash2Data = () => {
    return request({
        url: baseUrl + '/analysis/dash2',
        method: 'get'
    });
};

// 删除订单的接口
export const deleteOrder = (data:orderInfo) => {
    return request({
        url: baseUrl + '/table/order/delete',
        method: 'post',
        data: data
    });
};

// 售后订单列表数据接口
export const fetchAfterData = (data:userInfo) => {
    return request({
        url: baseUrl +'/table/aftersale',
        method: 'post',
        data:data
    });
};

// 拒绝售后订单的接口
export const disagreeAftersale = (data:orderInfo) => {
    return request({
        url: baseUrl + '/table/aftersale/disagree',
        method: 'post',
        data: data
    });
};

// 同意售后订单的接口
export const agreeAftersale = (data:orderInfo) => {
    return request({
        url: baseUrl + '/table/aftersale/agree',
        method: 'post',
        data: data
    });
};

// 商家菜品
export const fetchDishData = (data:userInfo) => {
    return request({
        url: baseUrl+'/table/dish',
        method: 'post',
        data: data
    });
};

// 商家修改菜品
export const updateDishData = (data:dishInfo) => {
    return request({
        url: baseUrl + '/table/dish/update',
        method: 'post',
        data: data
    })
}

// 新增菜品
export const fetchAddData = (data:addDish) => {
    return request({
        url: baseUrl + '/table/dish/add',
        method: 'post',
        data: data
    });
};

// 管理员向商家发送消息
export const sendMessageToRestaurant = (data:messageInfo) => {
    return request({
        url: baseUrl + '/message/restaurant/send',
        method: 'post',
        data: data
    })
}

// 管理员向用户发送消息
export const sendMessageToUser = (data:messageInfo) => {
    return request({
        url: baseUrl + '/message/user/send',
        method: 'post',
        data: data
    })
}

// 获取商家消息列表接口
export const getMessageToRestaurant = (data:userInfo) => {
    return request({
        url: baseUrl + '/message/restaurant',
        method: 'post',
        data: data
    })
}

// 新增商家接口
export const addNewRestaurant = (data:addRestaurant) => {
    return request({
        url: baseUrl + '/restaurant/add',
        method: 'post',
        data: data
    })
}



