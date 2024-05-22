package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class User {
    // 用户账号
    String userId;
    // 用户名
    String userName;
    // 用户密码
    String password;
    // 用户头像
    String userFigure;
}
