package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class AdminNewPostResponse {
    // 管理员首页--最新评论广场数据类
    String posterName;
    String postContext;
    String timeDifference;
    String color;
}
