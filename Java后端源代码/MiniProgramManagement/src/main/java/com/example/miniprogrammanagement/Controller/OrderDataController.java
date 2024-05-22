package com.example.miniprogrammanagement.Controller;

import com.example.miniprogrammanagement.Bean.Course;
import com.example.miniprogrammanagement.Bean.Dish;
import com.example.miniprogrammanagement.Bean.Order;
import com.example.miniprogrammanagement.Dao.CourseDao;
import com.example.miniprogrammanagement.Dao.DishDao;
import com.example.miniprogrammanagement.Dao.OrderDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrderDataController {
    private OrderDao orderDao = new OrderDao();
    private CourseDao courseDao = new CourseDao();
    private DishDao dishDao = new DishDao();

    // 用户所有订单列表
    @PostMapping("/order/all")
    public ResponseEntity<List<Order>> getAllOrders(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String sql = "SELECT\n" +
                "\tOrderTable.*,\n" +
                "\tCanteen.canteenId AS canteenID,\n" +
                "\tCanteen.canteenName,\n" +
                "\tCourses.restaurantID,\n" +
                "\tCourses.dishName,\n" +
                "\tCourses.price,\n" +
                "\tCourses.dishImage AS orderImage \n" +
                "FROM\n" +
                "\tOrderTable\n" +
                "\tLEFT JOIN Courses ON OrderTable.dishID = Courses.dishID\n" +
                "\tLEFT JOIN Restaurant ON Courses.restaurantID = Restaurant.restaurantId\n" +
                "\tLEFT JOIN Canteen ON Restaurant.canteenId = Canteen.canteenId \n" +
                "WHERE\n" +
                "\tOrderTable.userID = ? \n" +
                "ORDER BY\n" +
                "\torderTime DESC";
        List<Order> orderList = orderDao.queryMulti(sql, Order.class, userId);
        return ResponseEntity.ok(orderList);
    }

    // 用户待评价订单列表
    @PostMapping("/order/evaluations")
    public ResponseEntity<List<Order>> getAfterSale(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String sql = "SELECT\n" +
                "\tOrderTable.*,\n" +
                "\tCanteen.canteenId AS canteenID,\n" +
                "\tCanteen.canteenName,\n" +
                "\tCourses.restaurantID,\n" +
                "\tCourses.dishName,\n" +
                "\tCourses.price,\n" +
                "\tCourses.dishImage AS orderImage \n" +
                "FROM\n" +
                "\tOrderTable\n" +
                "\tLEFT JOIN Courses ON OrderTable.dishID = Courses.dishID\n" +
                "\tLEFT JOIN Restaurant ON Courses.restaurantID = Restaurant.restaurantId\n" +
                "\tLEFT JOIN Canteen ON Restaurant.canteenId = Canteen.canteenId \n" +
                "WHERE\n" +
                "\tOrderTable.evaluationStatus = 0 \n" +
                "\tAND OrderTable.`status` = '已完成'\n" +
                "\tAND OrderTable.userID = ? \n" +
                "ORDER BY\n" +
                "\torderTime DESC";
        List<Order> orderList = orderDao.queryMulti(sql, Order.class, userId);
        return ResponseEntity.ok(orderList);
    }

    // 用户售后订单列表
    @PostMapping("/order/afterSale")
    public ResponseEntity<List<Order>> getEvaluations(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String sql = "SELECT\n" +
                "\tOrderTable.*,\n" +
                "\tCanteen.canteenId AS canteenID,\n" +
                "\tCanteen.canteenName,\n" +
                "\tCourses.restaurantID,\n" +
                "\tCourses.dishName,\n" +
                "\tCourses.price,\n" +
                "\tCourses.dishImage AS orderImage \n" +
                "FROM\n" +
                "\tOrderTable\n" +
                "\tLEFT JOIN Courses ON OrderTable.dishID = Courses.dishID\n" +
                "\tLEFT JOIN Restaurant ON Courses.restaurantID = Restaurant.restaurantId\n" +
                "\tLEFT JOIN Canteen ON Restaurant.canteenId = Canteen.canteenId \n" +
                "WHERE\n" +
                "\tOrderTable.aftersaleStatus = 1 \n" +
                "\tAND OrderTable.userID = ? \n" +
                "ORDER BY\n" +
                "\torderTime DESC";
        List<Order> orderList = orderDao.queryMulti(sql, Order.class, userId);
        return ResponseEntity.ok(orderList);
    }

    // 根据菜品ID查询订单号
    @PostMapping("/order/dishId")
    public ResponseEntity<Integer> getDishID(@RequestBody Map<String, String> requestBody) {
        String dishName = requestBody.get("dishName");
        int restaurantId = Integer.parseInt(requestBody.get("restaurantId"));
        System.out.println(dishName+restaurantId);
        String newName = dishName.replaceAll("\\s", "");
        System.out.println(newName);
        String sql = "select dishID from Courses where dishName = ? AND restaurantID = ? ";
        Dish dish = dishDao.querySingle(sql, Dish.class, newName, restaurantId);
        int dishId = dish.getDishId();
        System.out.println(dishId);
        return ResponseEntity.ok(dishId);
    }

    @PostMapping("/order/submit")
    public ResponseEntity<String> insertOrder(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        int dishId = Integer.parseInt(requestBody.get("dishId"));
        String pungencyDegree = requestBody.get("pungencyDegree");
        String seasoning = requestBody.get("seasoning");
        String notes = requestBody.get("notes");
        System.out.println(userId+pungencyDegree+seasoning+notes);
        String status = "制作中";
        int evaluationStatus = 0;
        int aftersaleStatus = 0;

        String sql = "insert into OrderTable(userId, dishId, pungencyDegree, seasoning, notes, status, evaluationStatus, aftersaleStatus) values(?,?,?,?,?,?,?,?);";
        int insert = orderDao.update(sql, userId, dishId, pungencyDegree, seasoning, notes, status, evaluationStatus, aftersaleStatus);
        System.out.println(insert);
        if (insert > 0) {
            return ResponseEntity.status(HttpStatus.OK).body("添加订单成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("添加订单失败");
        }
    }

    @PostMapping("/order/update/status")
    public ResponseEntity<String> updateOrderStatus(@RequestBody Map<String, String> requestBody) {
        int orderId = Integer.parseInt(requestBody.get("orderId"));
        String newStatus = requestBody.get("newStatus");

        String sql = "UPDATE OrderTable \n" +
                "SET STATUS = ? \n" +
                "WHERE\n" +
                "\torderId = ?;";

        int update = orderDao.update(sql, newStatus, orderId);
        if (update > 0) {
            return ResponseEntity.ok("更改订单状态成功！");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("更改订单状态失败");
        }
    }

    @PostMapping("/aftersale/add")
    public ResponseEntity<String> addAftersale(@RequestBody Map<String, String> requestBody) {
        int orderId = Integer.parseInt(requestBody.get("orderId"));
        String refundPrice = requestBody.get("refundPrice");
        String reason = requestBody.get("reason");
        String audit = "待审核";

        String sql = "insert into Aftersale(orderId, refundPrice, reason, audit) values(?,?,?,?);";
        int insert = orderDao.update(sql, orderId, refundPrice, reason, audit);
        String sql1 = "UPDATE OrderTable \n" +
                "SET aftersaleStatus = 1 \n" +
                "WHERE\n" +
                "\torderId = ?;";
        int update = orderDao.update(sql1, orderId);
        if (update > 0 && insert > 0) {
            return ResponseEntity.ok("提交售后申请成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("提交售后申请失败");
        }
    }

    // 更新订单的评价状态
    @PostMapping("/order/update/evaluationStatus")
    public ResponseEntity<String> updatePostEvaluationStatus(@RequestBody Map<String, String> requestBody) {
        int orderId = Integer.parseInt(requestBody.get("orderId"));

        String sql = "UPDATE OrderTable \n" +
                "SET evaluationStatus = 1 \n" +
                "WHERE\n" +
                "\torderId = ?;";
        int update = orderDao.update(sql, orderId);
        if (update > 0) {
            return ResponseEntity.ok("更新订单评价状态成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("更新订单评价状态失败");
        }
    }
}
