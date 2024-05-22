package com.example.miniprogrammanagement.Tools;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.miniprogrammanagement.Bean.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarOutputStream;

public class TokenGenerator {
    //用户的用户名

    //用于签名加密的密钥，为一个字符串（需严格保密）
    private static final String KEY = "12345678";

    public static String generateToken(String userId, String password, String userName, String userFigure) {
        //获取jwt生成器
        JWTCreator.Builder jwtBuilder = JWT.create();

        //由于该生成器设置Header的参数为一个<String, Object>的Map,
        //所以我们提前准备好
        Map<String, Object> headers = new HashMap<>();

        headers.put("typ", "jwt");   //设置token的type为jwt
        headers.put("alg", "hs256");  //表明加密的算法为HS256

        //开始生成token
        //我们将之前准备好的header设置进去
        String token = jwtBuilder.withHeader(headers)

                //接下来为设置PayLoad,Claim中的键值对可自定义
                // 设置密码
                .withClaim("password", password)

                // 设置用户id
                .withClaim("userId", userId)

                // 设置用户名
                .withClaim("userName", userName)

                // 设置用户头像
                // 设置用户名
                .withClaim("userFigure", userFigure)

                //token失效时间，这里为一天后失效
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                //设置该jwt的发行时间，一般为当前系统时间
                .withIssuedAt(new Date(System.currentTimeMillis()))

                //token的发行者（可自定义）
                .withIssuer("issuer")

                //进行签名，选择加密算法，以一个字符串密钥为参数
                .sign(Algorithm.HMAC256(KEY));

        //token生成完毕，可以发送给客户端了，前端可以使用
        //localStorage.setItem("your_token", token)进行存储，在
        //下次请求时携带发送给服务器端进行验证
        return token;
    }

    public static User verifyToken(String token) {

        /*获取jwt的验证器对象，传入的算法参数以及密钥字符串（KEY）必须
        和加密时的相同*/
        var require = JWT.require(Algorithm.HMAC256(KEY)).build();

        DecodedJWT decode = null;
        try {

            /*开始进行验证，该函数会验证此token是否遭到修改，
                以及是否过期，验证成功会生成一个解码对象
                ，如果token遭到修改或已过期就会
                抛出异常，我们用try-catch抓一下*/
            decode = require.verify(token);

        } catch (Exception e) {

            //抛出异常，验证失败
            System.out.println("token遭到修改或已过期");
        }

        //若验证成功，就可获取其携带的信息进行其他操作

        //也可以根据自定义参数的键值来获取
        System.out.println(decode.getClaim("userId").asString());
        System.out.println(decode.getClaim("password").asString());
        System.out.println(decode.getClaim("userName").asString());
        System.out.println(decode.getClaim("userFigure").asString());

        User user = new User();
        user.setUserId(decode.getClaim("userId").asString());
        user.setPassword(decode.getClaim("password").asString());
        user.setUserName(decode.getClaim("userName").asString());
        user.setUserFigure(decode.getClaim("userFigure").asString());

        //获取发送者，没有设置则为空
        System.out.println(decode.getIssuer());

        //获取过期时间
        System.out.println(decode.getExpiresAt());

        return user;
    }

    public static void main(String[] args) {
        String token = generateToken("202121130299", "88888888", "没脾气的打火叽_", "https://tdesign.gtimg.com/mobile/demos/avatar1.png");
        System.out.println(token);
        User user = verifyToken(token);
        System.out.println(user);
    }
}