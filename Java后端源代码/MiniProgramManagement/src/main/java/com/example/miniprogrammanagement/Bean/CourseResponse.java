package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class CourseResponse {
    // 食堂1档口1的菜单
    List<Course> canteen1_restaurant1;
    // 食堂1档口2的菜单
    List<Course> canteen1_restaurant2;
    // 食堂1档口3的菜单
    List<Course> canteen1_restaurant3;
    // 食堂1档口4的菜单
    List<Course> canteen1_restaurant4;
    // 食堂1档口5的菜单
    List<Course> canteen1_restaurant5;
    // 食堂2档口1-5
    List<Course> canteen2_restaurant1;
    List<Course> canteen2_restaurant2;
    List<Course> canteen2_restaurant3;
    List<Course> canteen2_restaurant4;
    List<Course> canteen2_restaurant5;
    // 食堂3档口1-5
    List<Course> canteen3_restaurant1;
    List<Course> canteen3_restaurant2;
    List<Course> canteen3_restaurant3;
    List<Course> canteen3_restaurant4;
    List<Course> canteen3_restaurant5;
    // 食堂4档口1-5
    List<Course> canteen4_restaurant1;
    List<Course> canteen4_restaurant2;
    List<Course> canteen4_restaurant3;
    List<Course> canteen4_restaurant4;
    List<Course> canteen4_restaurant5;
    // 食堂5档口1-5
    List<Course> canteen5_restaurant1;
    List<Course> canteen5_restaurant2;
    List<Course> canteen5_restaurant3;
    List<Course> canteen5_restaurant4;
    List<Course> canteen5_restaurant5;
}
