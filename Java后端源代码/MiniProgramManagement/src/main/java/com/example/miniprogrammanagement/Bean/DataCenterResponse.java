package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class DataCenterResponse {
    // 当前商家评分
    float restaurantRate;
    // 今日订单数量
    Integer todayOrderCount;
    // 今日下单人数
    Integer todayOrderUser;
    // 今日收入总额
    float todayTurnover;
    // 今日评论数量
    Integer todayPostCount;
    // 未完成订单数量
    Integer unfinishedOrderCount;
    // 已完成订单数量
    Integer finishedOrderCount;
}
