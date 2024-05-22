package com.example.miniprogrammanagement.Controller;

import com.example.miniprogrammanagement.Bean.Feedback;
import com.example.miniprogrammanagement.Dao.FeedbackDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class FeedbackController {
    private FeedbackDao feedbackDao = new FeedbackDao();

    @PostMapping("/feedback/insert")
    public ResponseEntity<String> insertFeedback(@RequestBody Map<String, String> requestBody) {
        String issue = requestBody.get("issue");

        String sql = "insert into Feedback (issue) values(?)";
        int insert = feedbackDao.update(sql, issue);

        if (insert == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("反馈上传成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("反馈上传失败");
        }
    }
}
