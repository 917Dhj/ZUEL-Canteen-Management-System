package com.example.miniprogrammanagement.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data // 生成getter&setter
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor // 无参构造方法
public class Post {
    // 帖子ID
    String postId;
    // 发帖人ID
    String posterId;
    // 发帖人用户名
    String posterName;
    // 发帖人头像url
    String posterFigure;
    // 发帖日期
    String postDate;
    // 帖子标题
    String postTitle;
    // 帖子内容
    String postContext;
    // 帖子的类型：1为评价帖子；2为失物招领帖子
    Integer postType;
    // 对商家的评分
    float rate;
    // 评价的商家所在的食堂
    String canteenName;
    // 评价的对象商家的名称
    String shopName;
    // 失物招领的地点
    String position;
    // 点赞数量
    Integer likesCount;
    // 收藏数量
    Integer starsCount;
    //评论的数量
    Integer commentsCount;
    // 评价的档口的评分
    float restaurantRate;
    // 点赞的状态（图标）
    String icon_like;
    // 收藏的状态（图标）
    String icon_star;
}
