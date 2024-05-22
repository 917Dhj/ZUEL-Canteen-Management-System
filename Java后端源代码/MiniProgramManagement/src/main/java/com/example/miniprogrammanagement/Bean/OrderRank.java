package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class OrderRank {
    // 网页端-订单数量排行榜数据类
    Integer orderCount; // 订单数量
    String restaurantName; // 档口名称
    float percentage; // 订单数量占总订单量的百分比
    String color; // 排行榜上的颜色
}
