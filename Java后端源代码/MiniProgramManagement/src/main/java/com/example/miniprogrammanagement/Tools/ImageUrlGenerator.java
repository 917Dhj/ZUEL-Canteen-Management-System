package com.example.miniprogrammanagement.Tools;

import com.example.miniprogrammanagement.Bean.ImageResponse;
import com.google.gson.Gson;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

public class ImageUrlGenerator {
    public static void main(String[] args) {
        String url = "https://api.imgbb.com/1/upload";
        File imageFile = new File("/Users/dinghongjing/Desktop/WechatIMG1328.jpg");
        System.out.println(imageFile.length());
        String result = postImage(url, imageFile);

        //构建一个Gson对象
        Gson gson = new Gson();
        // 将json解析为相应的类
        ImageResponse imageResponse = gson.fromJson(result, ImageResponse.class);
        ImageResponse.DataDTO dataDTO = imageResponse.getData();
        String imageUrl = dataDTO.getUrl();
        System.out.println(imageUrl);
        System.out.println(result);
    }

    // 上传图片文件，生成图片链接
    public static String postImage(String url, File imageFile) {
        String result = null;
        Request request = Request.post(url);

        // post请求参数
        // 将图片文件转换为 base64 编码的字符串
        String base64Image = encodeImageToBase64(imageFile);

        request.bodyForm(
                new BasicNameValuePair("key", "178e0d49fe942e805157f4f81babb324"),
                new BasicNameValuePair("image", base64Image)
        );
        try {
            result = request.execute().returnContent().asString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // 根据base64格式的图片生成链接
    public static String postImage64(String url, String base64Image) {
        String result = null;
        Request request = Request.post(url);

        request.bodyForm(
                new BasicNameValuePair("key", "178e0d49fe942e805157f4f81babb324"),
                new BasicNameValuePair("image", base64Image)
        );
        try {
            result = request.execute().returnContent().asString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String encodeImageToBase64(File imageFile) {
        try {
            byte[] fileContent = new byte[(int) imageFile.length()];
            FileInputStream inputStream = new FileInputStream(imageFile);
            inputStream.read(fileContent);
            inputStream.close();
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file: " + imageFile.getAbsolutePath(), e);
        }

    }
}
