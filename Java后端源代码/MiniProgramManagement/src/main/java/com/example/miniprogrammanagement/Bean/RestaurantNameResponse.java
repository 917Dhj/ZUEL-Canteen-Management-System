package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class RestaurantNameResponse {
    List<RestaurantName> canteen1;
    List<RestaurantName> canteen2;
    List<RestaurantName> canteen3;
    List<RestaurantName> canteen4;
    List<RestaurantName> canteen5;
}
