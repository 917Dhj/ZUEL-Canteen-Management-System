package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class AdminLogin {
    Integer restaurantId;
    String restaurantName;
    Integer canteenId;
    String password;
    String restaurantImage;
}
