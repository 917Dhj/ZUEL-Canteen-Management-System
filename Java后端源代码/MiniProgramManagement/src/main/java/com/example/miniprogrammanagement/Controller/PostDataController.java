package com.example.miniprogrammanagement.Controller;

import com.example.miniprogrammanagement.Bean.Comment;
import com.example.miniprogrammanagement.Bean.Post;
import com.example.miniprogrammanagement.Dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PostDataController {
    private PostDao postDao = new PostDao();
    private UserLikesDao userLikesDao = new UserLikesDao();
    private UserStarsDao userStarsDao = new UserStarsDao();
    private CommentDao commentDao = new CommentDao();
    private CommentLikesDao commentLikesDao = new CommentLikesDao();

    // 获取小程序首页推荐帖子数据
    @PostMapping("/post/recommend")
    public ResponseEntity<List<Post>> getRecommendPost(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String sql = "SELECT \n" +
                "    Post.*, `User`.userFigure as posterFigure,\n" +
                "    IFNULL(likesCount.likes, 0) AS likesCount,\n" +
                "    IFNULL(starsCount.stars, 0) AS starsCount,\n" +
                "\t\tIFNULL(commentsCount.comments, 0) AS commentsCount,\n" +
                "    IF(\n" +
                "        EXISTS (\n" +
                "            SELECT 1 \n" +
                "            FROM User_Likes \n" +
                "            WHERE User_Likes.postId = Post.postId \n" +
                "              AND User_Likes.userId = ? \n" +
                "        ), \n" +
                "        'like', \n" +
                "        'like-o'\n" +
                "    ) AS icon_like,\n" +
                "    IF(\n" +
                "        EXISTS (\n" +
                "            SELECT 1 \n" +
                "            FROM User_Stars \n" +
                "            WHERE User_Stars.postId = Post.postId \n" +
                "              AND User_Stars.userId = ? \n" +
                "        ), \n" +
                "        'star', \n" +
                "        'star-o'\n" +
                "    ) AS icon_star\n" +
                "FROM Post\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS likes\n" +
                "    FROM User_Likes\n" +
                "    GROUP BY postId\n" +
                ") AS likesCount ON Post.postId = likesCount.postId\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS stars\n" +
                "    FROM User_Stars\n" +
                "    GROUP BY postId\n" +
                ") AS starsCount ON Post.postId = starsCount.postId\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS comments\n" +
                "    FROM `Comment`\n" +
                "    GROUP BY postId\n" +
                ") AS commentsCount ON Post.postId = commentsCount.postId\n" +
                "LEFT JOIN `User` ON Post.posterId = `User`.userId\n" +
                "WHERE Post.postType = 1\n" +
                "AND Post.`delete` = 0\n" +
                "ORDER BY likesCount DESC";

        List<Post> postList = postDao.queryMulti(sql, Post.class, userId, userId);
        // 返回数据给客户端
        return ResponseEntity.ok(postList);
    }

    @PostMapping("/post/stared")
    public ResponseEntity<List<Post>> getStaredPost(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String sql = "SELECT \n" +
                "    Post.*, `User`.userFigure as posterFigure,\n" +
                "    IFNULL(likesCount.likes, 0) AS likesCount,\n" +
                "    IFNULL(starsCount.stars, 0) AS starsCount,\n" +
                "\t\tIFNULL(commentsCount.comments, 0) AS commentsCount,\n" +
                "    IF(\n" +
                "        EXISTS (\n" +
                "            SELECT 1 \n" +
                "            FROM User_Likes \n" +
                "            WHERE User_Likes.postId = Post.postId \n" +
                "              AND User_Likes.userId = ? \n" +
                "        ), \n" +
                "        'like', \n" +
                "        'like-o'\n" +
                "    ) AS icon_like,\n" +
                "    IF(\n" +
                "        EXISTS (\n" +
                "            SELECT 1 \n" +
                "            FROM User_Stars \n" +
                "            WHERE User_Stars.postId = Post.postId \n" +
                "              AND User_Stars.userId = ? \n" +
                "        ), \n" +
                "        'star', \n" +
                "        'star-o'\n" +
                "    ) AS icon_star\n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\tPost.* \n" +
                "\tFROM\n" +
                "\t\tPost\n" +
                "\t\tLEFT JOIN User_Stars ON Post.postId = User_Stars.postId \n" +
                "\tWHERE\n" +
                "\t\tuserId = ? \n" +
                "\t) AS Post\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS likes\n" +
                "    FROM User_Likes\n" +
                "    GROUP BY postId\n" +
                ") AS likesCount ON Post.postId = likesCount.postId\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS stars\n" +
                "    FROM User_Stars\n" +
                "    GROUP BY postId\n" +
                ") AS starsCount ON Post.postId = starsCount.postId\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS comments\n" +
                "    FROM `Comment`\n" +
                "    GROUP BY postId\n" +
                ") AS commentsCount ON Post.postId = commentsCount.postId\n" +
                "LEFT JOIN `User` ON Post.posterId = `User`.userId\n" +
                "WHERE Post.`delete` = 0\n" +
                "ORDER BY Post.postId DESC;";

        List<Post> postList = postDao.queryMulti(sql, Post.class, userId, userId, userId);
        return ResponseEntity.ok(postList);
    }

    @PostMapping("/post/new")
    public ResponseEntity<List<Post>> getNewestPost(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String sql = "SELECT \n" +
                "    Post.*, `User`.userFigure as posterFigure,\n" +
                "    IFNULL(likesCount.likes, 0) AS likesCount,\n" +
                "    IFNULL(starsCount.stars, 0) AS starsCount,\n" +
                "\t\tIFNULL(commentsCount.comments, 0) AS commentsCount,\n" +
                "    IF(\n" +
                "        EXISTS (\n" +
                "            SELECT 1 \n" +
                "            FROM User_Likes \n" +
                "            WHERE User_Likes.postId = Post.postId \n" +
                "              AND User_Likes.userId = ? \n" +
                "        ), \n" +
                "        'like', \n" +
                "        'like-o'\n" +
                "    ) AS icon_like,\n" +
                "    IF(\n" +
                "        EXISTS (\n" +
                "            SELECT 1 \n" +
                "            FROM User_Stars \n" +
                "            WHERE User_Stars.postId = Post.postId \n" +
                "              AND User_Stars.userId = ? \n" +
                "        ), \n" +
                "        'star', \n" +
                "        'star-o'\n" +
                "    ) AS icon_star\n" +
                "FROM Post\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS likes\n" +
                "    FROM User_Likes\n" +
                "    GROUP BY postId\n" +
                ") AS likesCount ON Post.postId = likesCount.postId\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS stars\n" +
                "    FROM User_Stars\n" +
                "    GROUP BY postId\n" +
                ") AS starsCount ON Post.postId = starsCount.postId\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS comments\n" +
                "    FROM `Comment`\n" +
                "    GROUP BY postId\n" +
                ") AS commentsCount ON Post.postId = commentsCount.postId\n" +
                "LEFT JOIN `User` ON Post.posterId = `User`.userId\n" +
                "WHERE Post.postType = 1\n" +
                "AND Post.`delete` = 0\n" +
                "ORDER BY Post.postId DESC";
        List<Post> postList = postDao.queryMulti(sql, Post.class, userId, userId);
        return ResponseEntity.ok(postList);
    }

    @PostMapping("/post/find")
    public ResponseEntity<List<Post>> getFindLostPost(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String sql = "SELECT \n" +
                "    Post.*, `User`.userFigure as posterFigure,\n" +
                "    IFNULL(likesCount.likes, 0) AS likesCount,\n" +
                "    IFNULL(starsCount.stars, 0) AS starsCount,\n" +
                "\t\tIFNULL(commentsCount.comments, 0) AS commentsCount,\n" +
                "    IF(\n" +
                "        EXISTS (\n" +
                "            SELECT 1 \n" +
                "            FROM User_Likes \n" +
                "            WHERE User_Likes.postId = Post.postId \n" +
                "              AND User_Likes.userId = ? \n" +
                "        ), \n" +
                "        'like', \n" +
                "        'like-o'\n" +
                "    ) AS icon_like,\n" +
                "    IF(\n" +
                "        EXISTS (\n" +
                "            SELECT 1 \n" +
                "            FROM User_Stars \n" +
                "            WHERE User_Stars.postId = Post.postId \n" +
                "              AND User_Stars.userId = ? \n" +
                "        ), \n" +
                "        'star', \n" +
                "        'star-o'\n" +
                "    ) AS icon_star\n" +
                "FROM Post\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS likes\n" +
                "    FROM User_Likes\n" +
                "    GROUP BY postId\n" +
                ") AS likesCount ON Post.postId = likesCount.postId\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS stars\n" +
                "    FROM User_Stars\n" +
                "    GROUP BY postId\n" +
                ") AS starsCount ON Post.postId = starsCount.postId\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS comments\n" +
                "    FROM `Comment`\n" +
                "    GROUP BY postId\n" +
                ") AS commentsCount ON Post.postId = commentsCount.postId\n" +
                "LEFT JOIN `User` ON Post.posterId = `User`.userId\n" +
                "WHERE Post.postType = 2\n" +
                "AND Post.`delete` = 0\n" +
                "ORDER BY Post.postId DESC";
        List<Post> postList = postDao.queryMulti(sql, Post.class, userId, userId);
        return ResponseEntity.ok(postList);
    }

    @PostMapping("/post/detail")
    public ResponseEntity<Post> getPostDetail(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        int postId = Integer.parseInt(requestBody.get("postId"));
        String sql = "SELECT \n" +
                "    Post.*, `User`.userFigure as posterFigure,\n" +
                "    IFNULL(likesCount.likes, 0) AS likesCount,\n" +
                "    IFNULL(starsCount.stars, 0) AS starsCount,\n" +
                "\t\tIFNULL(commentsCount.comments, 0) AS commentsCount,\n" +
                "\t\tIFNULL(restaurantRate.restaurantRate, 0) AS restaurantRate,\n" +
                "    IF(\n" +
                "        EXISTS (\n" +
                "            SELECT 1 \n" +
                "            FROM User_Likes \n" +
                "            WHERE User_Likes.postId = Post.postId \n" +
                "              AND User_Likes.userId = ? \n" +
                "        ), \n" +
                "        'like', \n" +
                "        'like-o'\n" +
                "    ) AS icon_like,\n" +
                "    IF(\n" +
                "        EXISTS (\n" +
                "            SELECT 1 \n" +
                "            FROM User_Stars \n" +
                "            WHERE User_Stars.postId = Post.postId \n" +
                "              AND User_Stars.userId = ? \n" +
                "        ), \n" +
                "        'star', \n" +
                "        'star-o'\n" +
                "    ) AS icon_star\n" +
                "FROM Post\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS likes\n" +
                "    FROM User_Likes\n" +
                "    GROUP BY postId\n" +
                ") AS likesCount ON Post.postId = likesCount.postId\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS stars\n" +
                "    FROM User_Stars\n" +
                "    GROUP BY postId\n" +
                ") AS starsCount ON Post.postId = starsCount.postId\n" +
                "LEFT JOIN (\n" +
                "    SELECT postId, COUNT(*) AS comments\n" +
                "    FROM `Comment`\n" +
                "    GROUP BY postId\n" +
                ") AS commentsCount ON Post.postId = commentsCount.postId\n" +
                "LEFT JOIN (\n" +
                "\tSELECT Restaurant.restaurantName, ROUND(IFNULL(AVG(Post.rate), 0), 1) AS restaurantRate\n" +
                "\tFROM Restaurant\n" +
                "\tLEFT JOIN Post ON Restaurant.restaurantName = Post.shopName\n" +
                "\tGROUP BY Restaurant.restaurantName\n" +
                ") AS restaurantRate ON Post.shopName = restaurantRate.restaurantName\n" +
                "LEFT JOIN `User` ON Post.posterId = `User`.userId\n" +
                "WHERE Post.postId = ?";
        Post post = postDao.querySingle(sql, Post.class, userId, userId, postId);
        return ResponseEntity.ok(post);
    }

    // 发布美食点评帖子
    @PostMapping("/post/submit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> receiveSubmitPost(@RequestBody Post post) {
        System.out.println(post);
        String sql = "insert into Post(postId, posterId, posterName, postDate, postTitle, postContext, postType, rate, canteenName, shopName) values(?,?,?,?,?,?,?,?,?,?);";
        int insert = postDao.update(sql, post.getPostId(), post.getPosterId(), post.getPosterName(), post.getPostDate(), post.getPostTitle(), post.getPostContext(), post.getPostType(), post.getRate(), post.getCanteenName(), post.getShopName());
        System.out.println(insert);
        if (insert == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("数据接收成功！");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("数据存储失败！");
        }
    }

    // 发布失物招领帖子
    @PostMapping("/post/submit/find")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> insertFindPost(@RequestBody Post post) {
        System.out.println(post);
        String sql = "insert into Post(postId, posterId, posterName, postDate, postTitle, postContext, postType, rate, position) values(?,?,?,?,?,?,?,?,?);";
        int insert = postDao.update(sql, post.getPostId(), post.getPosterId(), post.getPosterName(), post.getPostDate(), post.getPostTitle(), post.getPostContext(), post.getPostType(), post.getRate(), post.getPosition());
        System.out.println(insert);
        if (insert == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("数据接收成功！");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("数据存储失败！");
        }
    }

    @PostMapping("/post/like")
    public ResponseEntity<String> insertUserLikes(@RequestBody Map<String, String> requestBody) {
        int postId = Integer.parseInt(requestBody.get("postId"));
        String userId = requestBody.get("userId");

        String sql = "insert into User_Likes(userId, postId) values(?,?);";
        int insert = userLikesDao.update(sql, userId, postId);
        System.out.println(insert);
        if (insert == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("点赞数据添加成功！");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("点赞数据添加失败！");
        }
    }

    @PostMapping("/post/dislike")
    public ResponseEntity<String> deleteUserLikes(@RequestBody Map<String, String> requestBody) {
        int postId = Integer.parseInt(requestBody.get("postId"));
        String userId = requestBody.get("userId");

        String sql = "delete from User_Likes where userId = ? and postId = ?;";
        int delete = userLikesDao.update(sql, userId, postId);
        System.out.println(delete);
        if (delete == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("点赞数据删除成功！");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("点赞数据删除失败！");
        }
    }

    @PostMapping("/post/star")
    public ResponseEntity<String> insertUserStars(@RequestBody Map<String, String> requestBody) {
        int postId = Integer.parseInt(requestBody.get("postId"));
        String userId = requestBody.get("userId");

        String sql = "insert into User_Stars(userId, postId) values(?,?);";
        int insert = userStarsDao.update(sql, userId, postId);
        System.out.println(insert);
        if (insert == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("收藏记录添加成功！");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("收藏记录添加失败！");
        }
    }

    @PostMapping("/post/cancelstar")
    public ResponseEntity<String> deleteUserStars(@RequestBody Map<String, String> requestBody) {
        int postId = Integer.parseInt(requestBody.get("postId"));
        String userId = requestBody.get("userId");

        String sql = "delete from User_Stars where userId = ? and postId = ?;";
        int delete = userStarsDao.update(sql, userId, postId);
        System.out.println(delete);
        if (delete == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("收藏记录删除成功！");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("收藏记录删除失败！");
        }
    }

    @PostMapping("/post/comments")
    public ResponseEntity<List<Comment>> getComments(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        int postId = Integer.parseInt(requestBody.get("postId"));
        String sql = "SELECT\n" +
                "\t`Comment`.*,\n" +
                "    CASE\n" +
                "        WHEN TIMESTAMPDIFF(MINUTE, commentTime, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, commentTime, NOW()), '分钟')\n" +
                "        WHEN TIMESTAMPDIFF(HOUR, commentTime, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, commentTime, NOW()), '小时')\n" +
                "        ELSE CONCAT(TIMESTAMPDIFF(DAY, commentTime, NOW()), '天')\n" +
                "    END AS timeDifference,\n" +
                "\t`User`.userName,\n" +
                "\t`User`.userFigure,\n" +
                "\tIFNULL(likesCount.likes, 0) AS likesCount,\n" +
                "    IF(\n" +
                "        EXISTS (\n" +
                "            SELECT 1 \n" +
                "            FROM Comment_Likes \n" +
                "            WHERE Comment_Likes.commentId = `Comment`.commentId \n" +
                "              AND Comment_Likes.userId = ? \n" +
                "        ), \n" +
                "        'good-job', \n" +
                "        'good-job-o'\n" +
                "    ) AS icon_like \n" +
                "FROM\n" +
                "\t`Comment`\n" +
                "\tLEFT JOIN `User` ON `Comment`.userId = `User`.userId\n" +
                "\tLEFT JOIN (\n" +
                "    SELECT commentId, COUNT(*) AS likes\n" +
                "    FROM Comment_Likes\n" +
                "    GROUP BY commentId\n" +
                ") AS likesCount ON `Comment`.commentId = likesCount.commentId \n" +
                "WHERE\n" +
                "\t`Comment`.postId = ?\n" +
                "\tORDER BY likesCount DESC";
        List<Comment> commentList = commentDao.queryMulti(sql, Comment.class, userId, postId);
        return ResponseEntity.ok(commentList);
    }

    @PostMapping("/post/comments/insert")
    public ResponseEntity<String> insertComment(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        int postId = Integer.parseInt(requestBody.get("postId"));
        String content = requestBody.get("content");

        String sql = "insert into Comment(userId, postId, content) values(?,?,?);";
        int insert = commentDao.update(sql, userId, postId, content);

        if (insert == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("添加评论成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("添加评论失败");
        }
    }

    @PostMapping("/post/comments/like")
    public ResponseEntity<String> commentLike(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        int commentId = Integer.parseInt(requestBody.get("commentId"));

        String sql = "insert into Comment_Likes(userId, commentId) values(?,?);";
        int insert = commentLikesDao.update(sql, userId, commentId);
        if (insert == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("点赞记录添加成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("点赞记录添加失败");
        }
    }

    @PostMapping("/post/comments/dislike")
    public ResponseEntity<String> commentDislike(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        int commentId = Integer.parseInt(requestBody.get("commentId"));

        String sql = "delete from Comment_Likes where userId = ? and commentId = ?;";
        int delete = commentLikesDao.update(sql, userId, commentId);

        if (delete == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("点赞记录删除成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("点赞记录删除失败");
        }
    }
}
