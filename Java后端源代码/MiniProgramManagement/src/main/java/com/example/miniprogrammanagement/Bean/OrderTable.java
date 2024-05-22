package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class OrderTable {
    Integer orderId;
    String dishName;
    String pungencyDegree;
    String seasoning;
    float price;
    String userId;
    String status;
    String orderTime;
}
