package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class AdminPostTable {
    Integer postId;
    String posterName;
    String posterId;
    String postContext;
    String postTime;
    String shopName;
}
