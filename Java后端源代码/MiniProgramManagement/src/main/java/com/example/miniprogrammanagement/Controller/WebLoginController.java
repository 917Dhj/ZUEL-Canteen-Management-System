package com.example.miniprogrammanagement.Controller;

import com.example.miniprogrammanagement.Bean.AdminLogin;
import com.example.miniprogrammanagement.Bean.DataCenterResponse;
import com.example.miniprogrammanagement.Bean.RestaurantLogin;
import com.example.miniprogrammanagement.Dao.AdminLoginDao;
import com.example.miniprogrammanagement.Dao.RestaurantLoginDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WebLoginController {
    private AdminLoginDao adminLoginDao = new AdminLoginDao();
    private RestaurantLoginDao restaurantLoginDao = new RestaurantLoginDao();

    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/admin/login")
    public ResponseEntity<String> adminLogin(@RequestBody Map<String, String> requestBody) {
        int restaurantId = Integer.parseInt(requestBody.get("username"));
        String password = requestBody.get("password");

        // 查询数据库里有没有用户输入的用户名和密码
        String sql = "select * from Restaurant where restaurantId = ? and password = ? ";
        List<AdminLogin> adminLoginList = adminLoginDao.queryMulti(sql, AdminLogin.class, restaurantId, password);

        if (!adminLoginList.isEmpty()) {
            System.out.println("账号密码正确: " + adminLoginList);
            return ResponseEntity.status(HttpStatus.OK).body("登录成功！");
        } else {
            System.out.println("账号或者密码错误!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("登录失败！账号或密码错误"); // 400
        }
    }

    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/restaurant/login/info")
    public ResponseEntity<RestaurantLogin> getRestaurantInfo(@RequestBody Map<String, String> requestBody) {
        int restaurantId = Integer.parseInt(requestBody.get("restaurantId"));

        String sql = "SELECT\n" +
                "\trestaurantName,\n" +
                "\tcanteenId,\n" +
                "\trestaurantImage \n" +
                "FROM\n" +
                "\tRestaurant \n" +
                "WHERE\n" +
                "\trestaurantId = ?";

        RestaurantLogin restaurantLogin = restaurantLoginDao.querySingle(sql, RestaurantLogin.class, restaurantId);
        return ResponseEntity.ok(restaurantLogin);
    }
}

