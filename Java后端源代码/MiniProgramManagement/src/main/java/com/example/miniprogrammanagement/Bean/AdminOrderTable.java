package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class AdminOrderTable {
    Integer orderId; // 订单编号
    String userName; // 用户名
    String userId; // 用户ID
    String dishName; // 菜品名称
    String canteenName; // 食堂名称
    String restaurantName; // 商家名称
    String status; // 订单状态
}
