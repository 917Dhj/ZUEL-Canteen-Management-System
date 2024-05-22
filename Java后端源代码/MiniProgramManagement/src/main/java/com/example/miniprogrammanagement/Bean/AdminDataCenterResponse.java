package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class AdminDataCenterResponse {
    Integer totalOrderUser; // 下单总人数
    float totalTurnover; // 总营业额
    Integer totalOrderCount; // 订单总数
    Integer totalCourseCount; // 菜品总数量
}
