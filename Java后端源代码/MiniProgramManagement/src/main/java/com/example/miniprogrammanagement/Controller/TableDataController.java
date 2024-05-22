package com.example.miniprogrammanagement.Controller;

import com.example.miniprogrammanagement.Bean.*;
import com.example.miniprogrammanagement.Dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TableDataController {
    private OrderTableDao orderTableDao = new OrderTableDao();
    private OrderDao orderDao = new OrderDao();
    private PostDao postDao = new PostDao();
    private AftersaleTableDao aftersaleTableDao = new AftersaleTableDao();
    private AdminOrderTableDao adminOrderTableDao = new AdminOrderTableDao();
    private UserDao userDao = new UserDao();
    private AdminRestaurantTableDao adminRestaurantTableDao = new AdminRestaurantTableDao();
    private AdminPostTableDao adminPostTableDao = new AdminPostTableDao();
    private AdminMenuTableDao adminMenuTableDao = new AdminMenuTableDao();
    private PostTableDao postTableDao = new PostTableDao();
    private DishTableDao dishTableDao = new DishTableDao();

    // 商家订单表
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/order")
    public ResponseEntity<List<OrderTable>> getRestaurantOrderTable(@RequestBody Map<String, String> requestBody) {
        int restaurantId = Integer.parseInt(requestBody.get("restaurantId"));

        String sql = "SELECT\n" +
                "\torderId,\n" +
                "\tCourses.dishName,\n" +
                "\tpungencyDegree,\n" +
                "\tseasoning,\n" +
                "\tCourses.price,\n" +
                "\tuserID,\n" +
                "\tDATE_FORMAT( orderTime, '%Y-%m-%d %H:%i:%s' ) AS orderTime,\n" +
                "\t`status` \n" +
                "FROM\n" +
                "\tOrderTable\n" +
                "\tLEFT JOIN Courses ON OrderTable.dishID = Courses.dishID \n" +
                "WHERE\n" +
                "\tCourses.restaurantID = ? \n" +
                "ORDER BY\n" +
                "\tOrderTable.orderTime DESC";

        List<OrderTable> orderTableList = orderTableDao.queryMulti(sql, OrderTable.class, restaurantId);
        return ResponseEntity.ok(orderTableList);
    }

    // 商家售后表
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/aftersale")
    public ResponseEntity<List<AftersaleTable>> getAftersaleTable(@RequestBody Map<String, String> requestBody) {
        int restaurantId = Integer.parseInt(requestBody.get("restaurantId"));

        String sql = "SELECT\n" +
                "\tOrderTable.userID AS userId,\n" +
                "\tAftersale.orderId,\n" +
                "\tCourses.dishName,\n" +
                "\tAftersale.reason,\n" +
                "\tAftersale.refundPrice,\n" +
                "\tAftersale.audit \n" +
                "FROM\n" +
                "\tAftersale\n" +
                "\tLEFT JOIN OrderTable ON Aftersale.orderId = OrderTable.orderID\n" +
                "\tLEFT JOIN Courses ON OrderTable.dishID = Courses.dishID \n" +
                "WHERE\n" +
                "\tCourses.restaurantID = ? \n" +
                "ORDER BY\n" +
                "\taftersaleTime DESC";

        List<AftersaleTable> aftersaleTableList = aftersaleTableDao.queryMulti(sql, AftersaleTable.class, restaurantId);
        return ResponseEntity.ok(aftersaleTableList);
    }

    // 管理员订单管理页面表单数据接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @GetMapping("/table/admin/order")
    public ResponseEntity<List<AdminOrderTable>> getAdminOrderTable() {
        String sql = "SELECT\n" +
                "\torderId,\n" +
                "\t`User`.userName,\n" +
                "\tOrderTable.userID AS userId,\n" +
                "\tCourses.dishName,\n" +
                "\tCanteen.canteenName,\n" +
                "\tRestaurant.restaurantName,\n" +
                "\tOrderTable.`status` \n" +
                "FROM\n" +
                "\tOrderTable\n" +
                "\tLEFT JOIN Courses ON OrderTable.dishId = Courses.dishId\n" +
                "\tLEFT JOIN Restaurant ON Courses.restaurantID = Restaurant.restaurantId\n" +
                "\tLEFT JOIN Canteen ON Restaurant.canteenId = Canteen.canteenId\n" +
                "\tLEFT JOIN `User` ON `User`.userId = OrderTable.userId \n" +
                "ORDER BY\n" +
                "\torderTime DESC";

        List<AdminOrderTable> adminOrderTableList = adminOrderTableDao.queryMulti(sql, AdminOrderTable.class);
        return ResponseEntity.ok(adminOrderTableList);
    }

    // 管理员订单管理页面表单--筛选
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/admin/order/filter")
    public ResponseEntity<List<AdminOrderTable>> getAdminOrderTableFiltered(@RequestBody Map<String, String> requestBody) {
        int orderId = Integer.parseInt(requestBody.get("orderId"));

        String sql = "SELECT\n" +
                "\torderId,\n" +
                "\t`User`.userName,\n" +
                "\tOrderTable.userID AS userId,\n" +
                "\tCourses.dishName,\n" +
                "\tCanteen.canteenName,\n" +
                "\tRestaurant.restaurantName,\n" +
                "\tOrderTable.`status` \n" +
                "FROM\n" +
                "\tOrderTable\n" +
                "\tLEFT JOIN Courses ON OrderTable.dishId = Courses.dishId\n" +
                "\tLEFT JOIN Restaurant ON Courses.restaurantID = Restaurant.restaurantId\n" +
                "\tLEFT JOIN Canteen ON Restaurant.canteenId = Canteen.canteenId\n" +
                "\tLEFT JOIN `User` ON `User`.userId = OrderTable.userId \n" +
                "WHERE\n" +
                "\torderID = ?";

        List<AdminOrderTable> adminOrderTableList = adminOrderTableDao.queryMulti(sql, AdminOrderTable.class, orderId);
        return ResponseEntity.ok(adminOrderTableList);
    }

    // 管理员用户管理页面表单数据接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @GetMapping("/table/admin/role")
    public ResponseEntity<List<User>> getRoleTable() {
        String sql = "SELECT\n" +
                "\tuserName,\n" +
                "\tuserId ,\n" +
                "\tuserFigure, \n" +
                "\tpassword\n" +
                "FROM\n" +
                "\t`User`";

        List<User> userList = userDao.queryMulti(sql, User.class);
        return ResponseEntity.ok(userList);
    }

    // 管理员用户管理页面表单--筛选
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/admin/role/filter")
    public ResponseEntity<List<User>> getRoleTableFiltered(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String sql = "SELECT\n" +
                "\tuserName,\n" +
                "\tuserId,\n" +
                "\tuserFigure \n" +
                "FROM\n" +
                "\t`User` \n" +
                "WHERE\n" +
                "\tuserId = ?";

        List<User> userList = userDao.queryMulti(sql, User.class, userId);
        return ResponseEntity.ok(userList);
    }

    // 管理员商家管理页面表单数据接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @GetMapping("/table/admin/user")
    public ResponseEntity<List<AdminRestaurantTable>> getAdminRestaurantTable() {
        String sql = "SELECT\n" +
                "\tCanteen.canteenId,\n" +
                "\tCanteen.canteenName,\n" +
                "\tRestaurant.restaurantName,\n" +
                "\tRestaurant.restaurantId,\n" +
                "\tRestaurant.`password`,\n" +
                "\tCOUNT(*) AS dishCount \n" +
                "FROM\n" +
                "\tRestaurant\n" +
                "\tLEFT JOIN Canteen ON Restaurant.canteenId = Canteen.canteenId\n" +
                "\tLEFT JOIN Courses ON Restaurant.restaurantId = Courses.restaurantId \n" +
                "WHERE\n" +
                "\tCourses.dishName IS NOT NULL \n" +
                "GROUP BY\n" +
                "\tCanteen.canteenId,\n" +
                "\tCanteen.canteenName,\n" +
                "\tRestaurant.restaurantName,\n" +
                "\tRestaurant.restaurantId,\n" +
                "\tRestaurant.`password`";

        List<AdminRestaurantTable> adminRestaurantTableList = adminRestaurantTableDao.queryMulti(sql, AdminRestaurantTable.class);
        return ResponseEntity.ok(adminRestaurantTableList);
    }

    // 管理员商家管理页面表单--筛选
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/admin/user/filter")
    public ResponseEntity<List<AdminRestaurantTable>> getAdminRestaurantTableFiltered(@RequestBody Map<String, String> requestBody) {
        String restaurantName = requestBody.get("restaurantName");

        String sql = "SELECT\n" +
                "\tCanteen.canteenId,\n" +
                "\tCanteen.canteenName,\n" +
                "\tRestaurant.restaurantName,\n" +
                "\tRestaurant.restaurantId,\n" +
                "\tRestaurant.`password`,\n" +
                "\tCOUNT(*) AS dishCount \n" +
                "FROM\n" +
                "\tRestaurant\n" +
                "\tLEFT JOIN Canteen ON Restaurant.canteenId = Canteen.canteenId\n" +
                "\tLEFT JOIN Courses ON Restaurant.restaurantId = Courses.restaurantId \n" +
                "WHERE\n" +
                "\tCourses.dishName IS NOT NULL \n" +
                "\tAND Restaurant.restaurantName = ? \n" +
                "GROUP BY\n" +
                "\tCanteen.canteenId,\n" +
                "\tCanteen.canteenName,\n" +
                "\tRestaurant.restaurantName,\n" +
                "\tRestaurant.restaurantId,\n" +
                "\tRestaurant.`password`";

        List<AdminRestaurantTable> adminRestaurantTableList = adminRestaurantTableDao.queryMulti(sql, AdminRestaurantTable.class, restaurantName);
        return ResponseEntity.ok(adminRestaurantTableList);
    }

    // 管理员页面评价管理表单数据接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @GetMapping("/table/admin/post")
    public ResponseEntity<List<AdminPostTable>> getAdminPostTable() {
        String sql = "SELECT\n" +
                "\tpostId,\n" +
                "\tposterName,\n" +
                "\tposterId,\n" +
                "\tpostContext,\n" +
                "\tDATE_FORMAT( postTime, '%Y-%m-%d %H:%i:%s' ) AS postTime,\n" +
                "\tshopName \n" +
                "FROM\n" +
                "\tPost \n" +
                "WHERE\n" +
                "\tPost.`delete` = 0 \n" +
                "ORDER BY\n" +
                "\tpostTime DESC";

        List<AdminPostTable> adminPostTableList = adminPostTableDao.queryMulti(sql, AdminPostTable.class);
        return ResponseEntity.ok(adminPostTableList);
    }

    // 管理员页面评价管理表单--筛选指定用户--数据接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/admin/post/filter")
    public ResponseEntity<List<AdminPostTable>> getAdminPostTableFiltered(@RequestBody Map<String, String> requestBody) {
        String posterId = requestBody.get("posterId");

        String sql = "SELECT\n" +
                "\tpostId,\n" +
                "\tposterName,\n" +
                "\tposterId,\n" +
                "\tpostContext,\n" +
                "\tDATE_FORMAT( postTime, '%Y-%m-%d %H:%i:%s' ) AS postTime,\n" +
                "\tshopName \n" +
                "FROM\n" +
                "\tPost \n" +
                "WHERE\n" +
                "\tPost.`delete` = 0 \n" +
                "\tAND posterId = ? \n" +
                "ORDER BY\n" +
                "\tpostTime DESC";

        List<AdminPostTable> adminPostTableList = adminPostTableDao.queryMulti(sql, AdminPostTable.class, posterId);
        return ResponseEntity.ok(adminPostTableList);
    }

    // 管理员页面菜单表
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @GetMapping("/table/admin/menu")
    public ResponseEntity<List<AdminMenuTable>> getAdminMenuTable() {
        String sql = "SELECT\n" +
                "\tCanteen.canteenName,\n" +
                "\tRestaurant.restaurantName,\n" +
                "\tCourses.dishName,\n" +
                "\tCourses.dishId,\n" +
                "\tCourses.restaurantStatus AS dishStatus \n" +
                "FROM\n" +
                "\tCourses\n" +
                "\tLEFT JOIN Restaurant ON Courses.restaurantId = Restaurant.restaurantId\n" +
                "\tLEFT JOIN Canteen ON Restaurant.canteenId = Canteen.canteenId";

        List<AdminMenuTable> adminMenuTableList = adminMenuTableDao.queryMulti(sql, AdminMenuTable.class);
        return ResponseEntity.ok(adminMenuTableList);
    }

    // 管理员页面菜单表--筛选
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/admin/menu/filter")
    public ResponseEntity<List<AdminMenuTable>> getAdminMenuTableFiltered(@RequestBody Map<String, String> requestBody) {
        String dishName = requestBody.get("dishName");
        String sql = "SELECT\n" +
                "\tCanteen.canteenName,\n" +
                "\tRestaurant.restaurantName,\n" +
                "\tCourses.dishName,\n" +
                "\tCourses.dishId,\n" +
                "\tCourses.restaurantStatus AS dishStatus \n" +
                "FROM\n" +
                "\tCourses\n" +
                "\tLEFT JOIN Restaurant ON Courses.restaurantId = Restaurant.restaurantId\n" +
                "\tLEFT JOIN Canteen ON Restaurant.canteenId = Canteen.canteenId \n" +
                "WHERE\n" +
                "\tdishName = ?";

        List<AdminMenuTable> adminMenuTableList = adminMenuTableDao.queryMulti(sql, AdminMenuTable.class, dishName);
        return ResponseEntity.ok(adminMenuTableList);
    }

    // 商家评价管理表数据
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/post")
    public ResponseEntity<List<PostTable>> getPostTable(@RequestBody Map<String, String> requestBody) {
        int restaurantId = Integer.parseInt(requestBody.get("restaurantId"));

        String sql = "SELECT\n" +
                "\tposterName,\n" +
                "\tposterId,\n" +
                "\tpostContext,\n" +
                "\trate \n" +
                "FROM\n" +
                "\tPost\n" +
                "\tLEFT JOIN Restaurant ON Post.shopName = Restaurant.restaurantName \n" +
                "WHERE\n" +
                "\tRestaurant.restaurantId = ? \n" +
                "ORDER BY\n" +
                "\tpostTime DESC";

        List<PostTable> postTableList = postTableDao.queryMulti(sql, PostTable.class, restaurantId);
        return ResponseEntity.ok(postTableList);
    }

    // 商家菜品管理表数据
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/dish")
    public ResponseEntity<List<DishTable>> getDishTable(@RequestBody Map<String, String> requestBody) {
        int restaurantId = Integer.parseInt(requestBody.get("restaurantId"));

        String sql = "SELECT\n" +
                "\tdishID,\n" +
                "\tdishName,\n" +
                "\tprice, \n" +
                "\trestaurantStatus AS dishStatus \n" +
                "FROM\n" +
                "\tCourses\n" +
                "\tLEFT JOIN Restaurant ON Courses.restaurantID = Restaurant.restaurantId \n" +
                "WHERE\n" +
                "\tRestaurant.restaurantId = ?";

        List<DishTable> dishTableList = dishTableDao.queryMulti(sql, DishTable.class, restaurantId);
        return ResponseEntity.ok(dishTableList);
    }
}
