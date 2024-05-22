package com.example.miniprogrammanagement.Controller;

import com.example.miniprogrammanagement.Dao.AftersaleTableDao;
import com.example.miniprogrammanagement.Dao.OrderDao;
import com.example.miniprogrammanagement.Dao.PostDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DeleteDataController {
    private OrderDao orderDao = new OrderDao();
    private PostDao postDao = new PostDao();

    // 删除订单
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/order/delete")
    public ResponseEntity<String> deleteOrder(@RequestBody Map<String, String> requestBody) {
        int orderId = Integer.parseInt(requestBody.get("orderId"));

        String sql = "delete from OrderTable where orderId = ?";
        int delete = orderDao.update(sql, orderId);

        if (delete > 0) {
            return ResponseEntity.ok("删除订单" + orderId + "成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("删除订单失败！");
        }
    }

    // 删除帖子接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/post/delete")
    public ResponseEntity<String> adminDeletePost(@RequestBody Map<String, String> requestBody) {
        int postId = Integer.parseInt(requestBody.get("postId"));
        System.out.println(postId);

        String sql = "UPDATE Post \n" +
                "SET `delete` = 1 \n" +
                "WHERE\n" +
                "\tpostId = ?";

        int delete = postDao.update(sql, postId);
        if (delete > 0) {
            return ResponseEntity.ok("删除帖子成功！");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("删除帖子失败");
        }
    }

    // 下架菜品接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/course/delete")
    public ResponseEntity<String> adminDeleteCourse(@RequestBody Map<String, String> requestBody) {
        int dishId = Integer.parseInt(requestBody.get("dishId"));

        String sql = "UPDATE Courses \n" +
                "SET restaurantStatus = '下架' \n" +
                "WHERE\n" +
                "\tdishID = ?";

        int delete = postDao.update(sql, dishId);
        if (delete > 0) {
            return ResponseEntity.ok("下架菜品成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("下架菜品失败！");
        }
    }
}
