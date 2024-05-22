package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class Feedback {
    // 反馈ID
    Integer feedbackId;
    // 反馈的问题内容
    String issue;
}
