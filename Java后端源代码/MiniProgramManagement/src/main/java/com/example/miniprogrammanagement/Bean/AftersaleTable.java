package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class AftersaleTable {
    String userId; // 用户ID
    Integer orderId; // 售后的订单ID
    String dishName; // 菜品信息
    float refundPrice; // 退款金额
    String reason; // 售后原因
    String audit; // 售后审核状态
}
