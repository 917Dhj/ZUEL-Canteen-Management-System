package com.example.miniprogrammanagement.Controller;

import com.example.miniprogrammanagement.Bean.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<String> getTestData() {
        String sql = "select * from Post;";

        // 返回数据给客户端
        return ResponseEntity.ok(sql);
    }
}
