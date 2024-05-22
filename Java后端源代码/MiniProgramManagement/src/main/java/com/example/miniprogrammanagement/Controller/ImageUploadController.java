package com.example.miniprogrammanagement.Controller;

import com.example.miniprogrammanagement.Bean.ImageResponse;
import com.example.miniprogrammanagement.Dao.RestaurantLoginDao;
import com.example.miniprogrammanagement.Dao.UserDao;
import com.example.miniprogrammanagement.Tools.ImageUrlGenerator;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImageUploadController {
    private UserDao userDao = new UserDao();
    private RestaurantLoginDao restaurantLoginDao = new RestaurantLoginDao();

    @PostMapping("/upload/userFigure")
    public ResponseEntity<String> imageUpload(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) {
        if (file.isEmpty()) {
            // 报错statusCode=400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("上传的文件为空");
        }

        try {
            // 保存文件到服务器上
            String uploadDir = "/Users/dinghongjing/eclipse-workspace/MiniProgramManagement/src/main/resources/static/uploads"; // 设置上传目录
            String fileName = file.getOriginalFilename();
            String filePath = uploadDir + File.separator + fileName;
            File dest = new File(filePath);
            file.transferTo(dest);

            // 将图片上传到imgbb网站，生成链接
            String url = "https://api.imgbb.com/1/upload";
            System.out.println(dest.length());
            String result = ImageUrlGenerator.postImage(url, dest);

            //构建一个Gson对象
            Gson gson = new Gson();
            // 将json解析为相应的类
            ImageResponse imageResponse = gson.fromJson(result, ImageResponse.class);
            ImageResponse.DataDTO dataDTO = imageResponse.getData();
            String imageUrl = dataDTO.getUrl(); // 得到最终的图片url链接
            System.out.println("图片url为:" + imageUrl);

            String sql = "update User\n" +
                    "set userFigure  = ?\n" +
                    "where userId = ?;";
            System.out.println(userId);
            int update = userDao.update(sql, imageUrl, userId);

            if (update > 0) {
                return ResponseEntity.status(HttpStatus.OK).body("新头像保存到数据库成功，路径为：" + imageUrl);
            } else {
                // 返回文件在服务器上的路径
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("头像保存到数据库失败" + update);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败：" + e.getMessage());
        }
    }

    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/upload/restaurantImage")
    public ResponseEntity<String> restaurantImageUpload(@RequestBody Map<String, String> requestBody) {
        String image = requestBody.get("image");
        int restaurantId = Integer.parseInt(requestBody.get("restaurantId"));
        System.out.println(image+restaurantId);

        // 将图片上传到imgbb网站，生成链接
        String url = "https://api.imgbb.com/1/upload";
        String result = ImageUrlGenerator.postImage64(url, image);

        //构建一个Gson对象
        Gson gson = new Gson();
        // 将json解析为相应的类
        ImageResponse imageResponse = gson.fromJson(result, ImageResponse.class);
        ImageResponse.DataDTO dataDTO = imageResponse.getData();
        String imageUrl = dataDTO.getUrl(); // 得到最终的图片url链接
        System.out.println("图片url为:" + imageUrl);

        String sql = "update Restaurant\n" +
                "set restaurantImage  = ?\n" +
                "where restaurantId = ?;";

        int update = restaurantLoginDao.update(sql, imageUrl, restaurantId);

        if (update > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(imageUrl);
        } else {
            // 返回文件在服务器上的路径
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("新商家头像保存到数据库失败" + update);
        }
    }


}
