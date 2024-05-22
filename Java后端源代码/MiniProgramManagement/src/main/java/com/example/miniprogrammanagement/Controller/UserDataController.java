package com.example.miniprogrammanagement.Controller;

import com.example.miniprogrammanagement.Bean.LoginResponse;
import com.example.miniprogrammanagement.Bean.User;
import com.example.miniprogrammanagement.Dao.UserDao;
import com.example.miniprogrammanagement.Tools.TokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserDataController {
    private UserDao userDao = new UserDao();
    private String USER_ID;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LoginResponse> receiveLoginData(@RequestBody Map<String, String> requestBody) {

        String userId = requestBody.get("userId");
        String password = requestBody.get("password");

        System.out.println(userId+" "+password);

        String sql = "select * from User where userId = ?;";

        List<User> userList = userDao.queryMulti(sql, User.class, userId);
        String userName = userList.getFirst().getUserName();
        String userFigure = userList.getFirst().getUserFigure();
        System.out.println(userList);

        LoginResponse loginResponse = new LoginResponse();

        if (!userList.isEmpty()) { // 如果查询到了userId，说明账号在数据库里
            System.out.println("账号正确" + userList);
            if (userList.getFirst().getPassword().equals(password)) { // 验证密码是否正确
                System.out.println("密码正确");
                USER_ID = userId;
                // 生成校验码
                String token = TokenGenerator.generateToken(userId, password, userName, userFigure);
                System.out.println("校验码：" + token);
                // 打包LoginResponse
                loginResponse.setToken(token);
                loginResponse.setCode(HttpStatus.OK.value());
                loginResponse.setMsg("账号密码正确");
                return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
            } else {
                System.out.println("密码错误");
                loginResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); // 状态码500
                loginResponse.setMsg("密码错误");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponse);
            }
        } else {
            System.out.println("账号不存在");
            loginResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            loginResponse.setMsg("账号不存在");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponse);
        }
    }

    @GetMapping("/getInfo")
    public ResponseEntity<User> getUserInfo() {
        String sql = "select * from User where userId = ?;";

        List<User> userList = userDao.queryMulti(sql, User.class, USER_ID);
        return ResponseEntity.status(HttpStatus.OK).body(userList.getFirst());
    }

    @PostMapping("/verify")
    public ResponseEntity<User> verifyToken(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        // 判断token是否存在，若不存在，验证失败，并进行验证失败的逻辑操作（例如跳转到登录界面，或拒绝访问等等）
        if (token == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } else {
            // 解析token，获取其中的用户数据并返回给客户端
            User userInfo = TokenGenerator.verifyToken(token);
            return ResponseEntity.status(HttpStatus.OK).body(userInfo);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String password = requestBody.get("password");
        System.out.println("userId:"+userId + " password:"+password);
        String userFigure = "https://tdesign.gtimg.com/mobile/demos/avatar1.png";

        String sql = "select * from User where userId = ?;";
        List<User> userList = userDao.queryMulti(sql, User.class, userId);
        System.out.println("userList:"+userList);

        if (userList.isEmpty()) { // 数据库中没有这个用户
            // 注册用户
            String insertSql = "insert into User(userId, userName, password, userFigure) values(?,?,?,?);";
            int insert = userDao.update(insertSql, userId, "用户"+userId, password, userFigure);
            System.out.println(insert);
            if (insert > 0) {
                // 注册成功
                return ResponseEntity.status(HttpStatus.OK).body("注册成功");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("注册用户失败");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("已存在该用户");
        }
    }

    @PostMapping("/modify/name")
    public ResponseEntity<String> modifyUserName(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String userName = requestBody.get("userName");
//        System.out.println(userId+userName);

        String sql = "update User\n" +
                "set userName  = ?\n" +
                "where userId = ?;";
        int update = userDao.update(sql, userName, userId);


        if (update > 0) {
            return ResponseEntity.status(HttpStatus.OK).body("修改用户昵称为：" + userName);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("修改用户昵称失败");
        }
    }

    @PostMapping("/modify/password")
    public ResponseEntity<String> modifyPassword(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String password = requestBody.get("password");

        String sql = "update User\n" +
                "set password  = ?\n" +
                "where userId = ?;";
        int update = userDao.update(sql, password, userId);


        if (update > 0) {
            return ResponseEntity.status(HttpStatus.OK).body("修改用户" + userId + "密码为：" + password);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("修改密码失败");
        }
    }
}
