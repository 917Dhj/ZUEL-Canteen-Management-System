package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class Comment {
    Integer commentId;
    String userId;
    Integer postId;
    String content;
    Timestamp commentTime;
    String timeDifference;
    String userName;
    String userFigure;
    Integer likesCount;
    String icon_like;
}
