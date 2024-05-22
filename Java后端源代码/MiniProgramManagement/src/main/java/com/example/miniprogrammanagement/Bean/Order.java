package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class Order {
    String OrderID;
    String UserID;
    String CanteenName;
    Integer RestaurantID;
    String DishName;
    float Price;
    String PungencyDegree;
    String Seasoning;
    String Notes;
    String Status;
    Integer EvaluationStatus;
    Integer AftersaleStatus;
    String orderImage;
}
