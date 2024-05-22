package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class AdminRestaurantTable {
    Integer canteenId;
    String canteenName;
    String restaurantName;
    Integer restaurantId;
    String password;
    Integer dishCount; // 商家的菜品数量
}
