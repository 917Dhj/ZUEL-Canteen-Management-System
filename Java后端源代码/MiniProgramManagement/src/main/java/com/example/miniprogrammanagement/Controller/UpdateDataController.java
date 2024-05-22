package com.example.miniprogrammanagement.Controller;

import com.example.miniprogrammanagement.Bean.MessageToRestaurant;
import com.example.miniprogrammanagement.Bean.MessageToUser;
import com.example.miniprogrammanagement.Bean.NewDishData;
import com.example.miniprogrammanagement.Bean.TotalDataInt;
import com.example.miniprogrammanagement.Dao.*;
import com.example.miniprogrammanagement.Tools.IdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UpdateDataController {
    private OrderDao orderDao = new OrderDao();
    private AftersaleTableDao aftersaleTableDao = new AftersaleTableDao();
    private PostDao postDao = new PostDao();
    private NewDishDataDao newDishDataDao = new NewDishDataDao();
    private MessageToRestaurantDao messageToRestaurantDao = new MessageToRestaurantDao();
    private MessageToUserDao messageToUserDao = new MessageToUserDao();
    private RestaurantLoginDao restaurantLoginDao = new RestaurantLoginDao();
    private TotalDataIntDao totalDataIntDao = new TotalDataIntDao();

    // 商家修改订单状态
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/update/order/status")
    public ResponseEntity<String> updateOrderStatus(@RequestBody Map<String, String> requestBody) {
        String status = requestBody.get("status");
        int orderId = Integer.parseInt(requestBody.get("orderId"));

        String sql = "UPDATE OrderTable \n" +
                "SET `status` = ? \n" +
                "WHERE\n" +
                "\torderID = ?";

        int update = orderDao.update(sql, status, orderId);
        if (update == 1) {
            return ResponseEntity.ok("更新订单状态成功！");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("更新订单状态失败");
        }
    }

    // 商家拒绝售后订单
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/aftersale/disagree")
    public ResponseEntity<String> disagreeAftersale(@RequestBody Map<String, String> requestBody) {
        int orderId = Integer.parseInt(requestBody.get("orderId"));

        String sql = "UPDATE Aftersale \n" +
                "SET audit = '拒绝' \n" +
                "WHERE\n" +
                "\torderId = ?";
        int update = aftersaleTableDao.update(sql, orderId);
        String sql1 = "UPDATE OrderTable \n" +
                "SET aftersaleStatus = 0 \n" +
                "WHERE\n" +
                "\torderId = ?";
        int update1 = orderDao.update(sql1, orderId);

        if (update > 0 && update1 > 0) {
            return ResponseEntity.ok("拒绝售后订单" + orderId + "成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("拒绝售后请求失败！");
        }
    }

    // 商家同意售后订单
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/aftersale/agree")
    public ResponseEntity<String> agreeAftersale(@RequestBody Map<String, String> requestBody) {
        int orderId = Integer.parseInt(requestBody.get("orderId"));

        String sql = "UPDATE Aftersale \n" +
                "SET audit = '同意' \n" +
                "WHERE\n" +
                "\torderId = ?";
        int update = aftersaleTableDao.update(sql, orderId);

        String sql1 = "UPDATE OrderTable \n" +
                "SET aftersaleStatus = 1 \n" +
                "WHERE\n" +
                "\torderId = ?";
        int update1 = orderDao.update(sql1, orderId);

        if (update > 0 && update1 > 0) {
            return ResponseEntity.ok("同意售后订单" + orderId + "成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("同意售后失败！");
        }
    }

    // 下架菜品接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/course/onsale")
    public ResponseEntity<String> adminOnSaleCourse(@RequestBody Map<String, String> requestBody) {
        int dishId = Integer.parseInt(requestBody.get("dishId"));

        String sql = "UPDATE Courses \n" +
                "SET restaurantStatus = '上架' \n" +
                "WHERE\n" +
                "\tdishID = ?";

        int delete = postDao.update(sql, dishId);
        if (delete > 0) {
            return ResponseEntity.ok("上架菜品成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("上架菜品失败！");
        }
    }

    // 修改菜品接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/dish/update")
    public ResponseEntity<String> updateDishTable(@RequestBody Map<String, String> requestBody) {
        int dishId = Integer.parseInt(requestBody.get("dishId"));
        String dishName = requestBody.get("dishName");
        float price = Float.parseFloat(requestBody.get("price"));

        String sql = "UPDATE Courses \n" +
                "SET dishName = ?, price = ? \n" +
                "WHERE\n" +
                "\tdishID = ?";

        int delete = postDao.update(sql, dishName, price, dishId);
        if (delete > 0) {
            return ResponseEntity.ok("修改菜品成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("修改菜品失败！");
        }
    }

    // 添加新菜品接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/table/dish/add")
    public ResponseEntity<String> addDish(@RequestBody Map<String, String> requestBody) {
        int restaurantId = Integer.parseInt(requestBody.get("restaurantId"));
        String dishName = requestBody.get("dishName");
        float price = Float.parseFloat(requestBody.get("price"));
        System.out.println(restaurantId+dishName+price);
        String dishImage = "https://tdesign.gtimg.com/mobile/demos/example2.png";
        String status = "上架";
        String getData = "SELECT\n" +
                "\tCanteen.canteenName,\n" +
                "\tCanteen.canteenId,\n" +
                "\tCourses.dishID AS dishId\n" +
                "FROM\n" +
                "\tCourses\n" +
                "\tLEFT JOIN Restaurant ON Courses.restaurantID = Restaurant.restaurantId\n" +
                "\tLEFT JOIN Canteen ON Restaurant.canteenId = Canteen.canteenId \n" +
                "WHERE\n" +
                "\tRestaurant.restaurantId = ? \n" +
                "ORDER BY\n" +
                "\tCourses.dishID DESC \n" +
                "\tLIMIT 1";

        NewDishData newDishData = newDishDataDao.querySingle(getData, NewDishData.class, restaurantId);
        System.out.println("该商家的最后一个菜品ID是："+newDishData);
        int newDishId=0;
        if(newDishData == null){
            // 这个商家还没有任何菜品
            newDishId = Integer.parseInt(String.valueOf(restaurantId)+"1");
        } else {
            // 这个商家已经有菜品
            int latestDishId = newDishData.getDishId();
            newDishId = IdGenerator.generateNewId(latestDishId, String.valueOf(restaurantId).length()); // 生成新的dishID
        }

        String sql = "insert into Courses(dishID, dishName, price, restaurantStatus, restaurantID, dishImage) values(?,?,?,?,?,?);";
        int insert = newDishDataDao.update(sql, newDishId, dishName, price, status, restaurantId, dishImage);
        System.out.println(insert);
        if (insert > 0) {
            return ResponseEntity.ok("添加新菜品成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("添加新菜品失败");
        }
    }

    // 管理员向商家发送消息接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/message/restaurant/send")
    public ResponseEntity<String> sendMessageToRestaurant(@RequestBody Map<String, String> requestBody) {
        int id = Integer.parseInt(requestBody.get("id"));
        String content = requestBody.get("content");

        String sql = "insert into MessageToRestaurant(restaurantId, content) values(?,?);";
        int insert = messageToRestaurantDao.update(sql, id, content);
        if (insert > 0) {
            return ResponseEntity.ok("发送消息成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("发送消息失败");
        }
    }

    // 管理员向用户发送消息接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/message/user/send")
    public ResponseEntity<String> sendMessageToUser(@RequestBody Map<String, String> requestBody) {
        String id = requestBody.get("id");
        String content = requestBody.get("content");

        String sql = "insert into MessageToUser(userId, content) values(?,?);";
        int insert = messageToUserDao.update(sql, id, content);
        if (insert > 0) {
            return ResponseEntity.ok("发送消息成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("发送消息失败");
        }
    }

    // 商家修改商家账户信息
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/restaurant/info/update")
    public ResponseEntity<String> updateRestaurantInfo(@RequestBody Map<String, String> requestBody) {
        int restaurantId = Integer.parseInt(requestBody.get("restaurantId"));
        String restaurantName = requestBody.get("restaurantName");
        String password = requestBody.get("password");

        String sql = "update Restaurant set restaurantName = ?, password = ? where restaurantId = ?";
        int update = restaurantLoginDao.update(sql, restaurantName, password, restaurantId);
        if (update > 0) {
            return ResponseEntity.ok("修改商家信息成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("修改商家信息失败");
        }
    }

    // 获取用户的消息列表
    @PostMapping("/message/user")
    public ResponseEntity<List<MessageToUser>> getMessageToUser(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");

        String sql = "SELECT\n" +
                "\tcontent,\n" +
                "\tDATE_FORMAT( sendTime, '%Y年%m月%d日' ) AS timeDifference \n" +
                "FROM\n" +
                "\tMessageToUser \n" +
                "WHERE\n" +
                "\tuserId = ?";

        List<MessageToUser> messageToUserList = messageToUserDao.queryMulti(sql, MessageToUser.class, userId);
        return ResponseEntity.ok(messageToUserList);
    }

    // 获取商家的消息列表
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/message/restaurant")
    public ResponseEntity<List<MessageToRestaurant>> getMessageToRestaurant(@RequestBody Map<String, String> requestBody) {
        int restaurantId = Integer.parseInt(requestBody.get("restaurantId"));

        String sql = "SELECT\n" +
                "\tcontent AS title,\n" +
                "\tsendTime AS `date` \n" +
                "FROM\n" +
                "\tMessageToRestaurant \n" +
                "WHERE\n" +
                "\trestaurantId = ? \n" +
                "ORDER BY\n" +
                "\tsendTime DESC";

        List<MessageToRestaurant> messageToRestaurantList = messageToRestaurantDao.queryMulti(sql, MessageToRestaurant.class, restaurantId);
        return ResponseEntity.ok(messageToRestaurantList);
    }

    // 新增商家接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/restaurant/add")
    public ResponseEntity<String> addNewRestaurant(@RequestBody Map<String, String> requestBody) {
        int canteenId = Integer.parseInt(requestBody.get("canteenId")); // 食堂ID 1
        String restaurantName = requestBody.get("restaurantName");

        String sql_getLatestRestaurantId = "SELECT\n" +
                "\trestaurantId AS total\n" +
                "FROM\n" +
                "\tRestaurant \n" +
                "WHERE\n" +
                "\tcanteenId = ? \n" +
                "ORDER BY\n" +
                "\trestaurantId DESC \n" +
                "\tLIMIT 1";

        TotalDataInt totalDataInt = totalDataIntDao.querySingle(sql_getLatestRestaurantId, TotalDataInt.class, canteenId);
        int latestRestaurantId = totalDataInt.getTotal();
        int newRestaurantId = IdGenerator.generateNewId(latestRestaurantId, String.valueOf(canteenId).length()); // 生成新的id
        System.out.println("新的restaurantId: " + newRestaurantId);
        String password = "123";
        String image = "https://i.ibb.co/Rvs94bj/Wechat-IMG234563.jpg";

        String sql = "insert into Restaurant(restaurantId, restaurantName, canteenId, password, restaurantImage) values(?,?,?,?,?)";
        int insert = restaurantLoginDao.update(sql, newRestaurantId, restaurantName, canteenId, password, image);
        if (insert > 0) {
            return ResponseEntity.ok("新增商家成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("新增商家失败");
        }
    }
}
