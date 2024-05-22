package com.example.miniprogrammanagement.Controller;

import com.example.miniprogrammanagement.Bean.*;
import com.example.miniprogrammanagement.Dao.RestaurantNameDao;
import com.example.miniprogrammanagement.Dao.CourseDao;
import com.example.miniprogrammanagement.Dao.RestaurantDao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FoodDataController {
    private CourseDao courseDao = new CourseDao();
    private RestaurantDao restaurantDao = new RestaurantDao();
    private RestaurantNameDao restaurantNameDao = new RestaurantNameDao();

    @GetMapping("/course/all")
    public ResponseEntity<CourseResponse> getAllCourses() {
        CourseResponse courseResponse = new CourseResponse();
        String sql11 = "select dishName as label, dishImage as image from Courses where restaurantID = 11 and restaurantStatus = '上架'";
        String sql12 = "select dishName as label, dishImage as image from Courses where restaurantID = 12 and restaurantStatus = '上架'";
        String sql13 = "select dishName as label, dishImage as image from Courses where restaurantID = 13 and restaurantStatus = '上架'";
        String sql14 = "select dishName as label, dishImage as image from Courses where restaurantID = 14 and restaurantStatus = '上架'";
        String sql15 = "select dishName as label, dishImage as image from Courses where restaurantID = 15 and restaurantStatus = '上架'";

        String sql21 = "select dishName as label, dishImage as image from Courses where restaurantID = 21 and restaurantStatus = '上架'";
        String sql22 = "select dishName as label, dishImage as image from Courses where restaurantID = 22 and restaurantStatus = '上架'";
        String sql23 = "select dishName as label, dishImage as image from Courses where restaurantID = 23 and restaurantStatus = '上架'";
        String sql24 = "select dishName as label, dishImage as image from Courses where restaurantID = 24 and restaurantStatus = '上架'";
        String sql25 = "select dishName as label, dishImage as image from Courses where restaurantID = 25 and restaurantStatus = '上架'";

        String sql31 = "select dishName as label, dishImage as image from Courses where restaurantID = 31 and restaurantStatus = '上架'";
        String sql32 = "select dishName as label, dishImage as image from Courses where restaurantID = 32 and restaurantStatus = '上架'";
        String sql33 = "select dishName as label, dishImage as image from Courses where restaurantID = 33 and restaurantStatus = '上架'";
        String sql34 = "select dishName as label, dishImage as image from Courses where restaurantID = 34 and restaurantStatus = '上架'";
        String sql35 = "select dishName as label, dishImage as image from Courses where restaurantID = 35 and restaurantStatus = '上架'";

        String sql41 = "select dishName as label, dishImage as image from Courses where restaurantID = 41 and restaurantStatus = '上架'";
        String sql42 = "select dishName as label, dishImage as image from Courses where restaurantID = 42 and restaurantStatus = '上架'";
        String sql43 = "select dishName as label, dishImage as image from Courses where restaurantID = 43 and restaurantStatus = '上架'";
        String sql44 = "select dishName as label, dishImage as image from Courses where restaurantID = 44 and restaurantStatus = '上架'";
        String sql45 = "select dishName as label, dishImage as image from Courses where restaurantID = 45 and restaurantStatus = '上架'";

        String sql51 = "select dishName as label, dishImage as image from Courses where restaurantID = 51 and restaurantStatus = '上架'";
        String sql52 = "select dishName as label, dishImage as image from Courses where restaurantID = 52 and restaurantStatus = '上架'";
        String sql53 = "select dishName as label, dishImage as image from Courses where restaurantID = 53 and restaurantStatus = '上架'";
        String sql54 = "select dishName as label, dishImage as image from Courses where restaurantID = 54 and restaurantStatus = '上架'";
        String sql55 = "select dishName as label, dishImage as image from Courses where restaurantID = 55 and restaurantStatus = '上架'";

        List<Course> courseList1 = courseDao.queryMulti(sql11, Course.class);
        List<Course> courseList2 = courseDao.queryMulti(sql12, Course.class);
        List<Course> courseList3 = courseDao.queryMulti(sql13, Course.class);
        List<Course> courseList4 = courseDao.queryMulti(sql14, Course.class);
        List<Course> courseList5 = courseDao.queryMulti(sql15, Course.class);

        List<Course> courseList6 = courseDao.queryMulti(sql21, Course.class);
        List<Course> courseList7 = courseDao.queryMulti(sql22, Course.class);
        List<Course> courseList8 = courseDao.queryMulti(sql23, Course.class);
        List<Course> courseList9 = courseDao.queryMulti(sql24, Course.class);
        List<Course> courseList10 = courseDao.queryMulti(sql25, Course.class);

        List<Course> courseList11 = courseDao.queryMulti(sql31, Course.class);
        List<Course> courseList12 = courseDao.queryMulti(sql32, Course.class);
        List<Course> courseList13 = courseDao.queryMulti(sql33, Course.class);
        List<Course> courseList14 = courseDao.queryMulti(sql34, Course.class);
        List<Course> courseList15 = courseDao.queryMulti(sql35, Course.class);

        List<Course> courseList16 = courseDao.queryMulti(sql41, Course.class);
        List<Course> courseList17 = courseDao.queryMulti(sql42, Course.class);
        List<Course> courseList18 = courseDao.queryMulti(sql43, Course.class);
        List<Course> courseList19 = courseDao.queryMulti(sql44, Course.class);
        List<Course> courseList20 = courseDao.queryMulti(sql45, Course.class);

        List<Course> courseList21 = courseDao.queryMulti(sql51, Course.class);
        List<Course> courseList22 = courseDao.queryMulti(sql52, Course.class);
        List<Course> courseList23 = courseDao.queryMulti(sql53, Course.class);
        List<Course> courseList24 = courseDao.queryMulti(sql54, Course.class);
        List<Course> courseList25 = courseDao.queryMulti(sql55, Course.class);

        courseResponse.setCanteen1_restaurant1(courseList1);
        courseResponse.setCanteen1_restaurant2(courseList2);
        courseResponse.setCanteen1_restaurant3(courseList3);
        courseResponse.setCanteen1_restaurant4(courseList4);
        courseResponse.setCanteen1_restaurant5(courseList5);

        courseResponse.setCanteen2_restaurant1(courseList6);
        courseResponse.setCanteen2_restaurant2(courseList7);
        courseResponse.setCanteen2_restaurant3(courseList8);
        courseResponse.setCanteen2_restaurant4(courseList9);
        courseResponse.setCanteen2_restaurant5(courseList10);

        courseResponse.setCanteen3_restaurant1(courseList11);
        courseResponse.setCanteen3_restaurant2(courseList12);
        courseResponse.setCanteen3_restaurant3(courseList13);
        courseResponse.setCanteen3_restaurant4(courseList14);
        courseResponse.setCanteen3_restaurant5(courseList15);

        courseResponse.setCanteen4_restaurant1(courseList16);
        courseResponse.setCanteen4_restaurant2(courseList17);
        courseResponse.setCanteen4_restaurant3(courseList18);
        courseResponse.setCanteen4_restaurant4(courseList19);
        courseResponse.setCanteen4_restaurant5(courseList20);

        courseResponse.setCanteen5_restaurant1(courseList21);
        courseResponse.setCanteen5_restaurant2(courseList22);
        courseResponse.setCanteen5_restaurant3(courseList23);
        courseResponse.setCanteen5_restaurant4(courseList24);
        courseResponse.setCanteen5_restaurant5(courseList25);
        return ResponseEntity.ok(courseResponse);
    }

    @GetMapping("/restaurant/all")
    public ResponseEntity<RestaurantResponse> getRestaurantInfo() {
        String sql = "SELECT Restaurant.restaurantName, ROUND(IFNULL(AVG(Post.rate), 0), 1) AS restaurantRate\n" +
                "FROM Restaurant\n" +
                "LEFT JOIN Post ON Restaurant.restaurantName = Post.shopName\n" +
                "WHERE Restaurant.restaurantId = ?\n" +
                "GROUP BY Restaurant.restaurantName";

        Restaurant restaurant11 = restaurantDao.querySingle(sql, Restaurant.class, 11);
        Restaurant restaurant12 = restaurantDao.querySingle(sql, Restaurant.class, 12);
        Restaurant restaurant13 = restaurantDao.querySingle(sql, Restaurant.class, 13);
        Restaurant restaurant14 = restaurantDao.querySingle(sql, Restaurant.class, 14);
        Restaurant restaurant15 = restaurantDao.querySingle(sql, Restaurant.class, 15);

        Restaurant restaurant21 = restaurantDao.querySingle(sql, Restaurant.class, 21);
        Restaurant restaurant22 = restaurantDao.querySingle(sql, Restaurant.class, 22);
        Restaurant restaurant23 = restaurantDao.querySingle(sql, Restaurant.class, 23);
        Restaurant restaurant24 = restaurantDao.querySingle(sql, Restaurant.class, 24);
        Restaurant restaurant25 = restaurantDao.querySingle(sql, Restaurant.class, 25);

        Restaurant restaurant31 = restaurantDao.querySingle(sql, Restaurant.class, 31);
        Restaurant restaurant32 = restaurantDao.querySingle(sql, Restaurant.class, 32);
        Restaurant restaurant33 = restaurantDao.querySingle(sql, Restaurant.class, 33);
        Restaurant restaurant34 = restaurantDao.querySingle(sql, Restaurant.class, 34);
        Restaurant restaurant35 = restaurantDao.querySingle(sql, Restaurant.class, 35);

        Restaurant restaurant41 = restaurantDao.querySingle(sql, Restaurant.class, 41);
        Restaurant restaurant42 = restaurantDao.querySingle(sql, Restaurant.class, 42);
        Restaurant restaurant43 = restaurantDao.querySingle(sql, Restaurant.class, 43);
        Restaurant restaurant44 = restaurantDao.querySingle(sql, Restaurant.class, 44);
        Restaurant restaurant45 = restaurantDao.querySingle(sql, Restaurant.class, 45);

        Restaurant restaurant51 = restaurantDao.querySingle(sql, Restaurant.class, 51);
        Restaurant restaurant52 = restaurantDao.querySingle(sql, Restaurant.class, 52);
        Restaurant restaurant53 = restaurantDao.querySingle(sql, Restaurant.class, 53);
        Restaurant restaurant54 = restaurantDao.querySingle(sql, Restaurant.class, 54);
        Restaurant restaurant55 = restaurantDao.querySingle(sql, Restaurant.class, 55);

        RestaurantResponse restaurantResponse = new RestaurantResponse();
        restaurantResponse.setRestaurant11(restaurant11);
        restaurantResponse.setRestaurant12(restaurant12);
        restaurantResponse.setRestaurant13(restaurant13);
        restaurantResponse.setRestaurant14(restaurant14);
        restaurantResponse.setRestaurant15(restaurant15);
        restaurantResponse.setRestaurant21(restaurant21);
        restaurantResponse.setRestaurant22(restaurant22);
        restaurantResponse.setRestaurant23(restaurant23);
        restaurantResponse.setRestaurant24(restaurant24);
        restaurantResponse.setRestaurant25(restaurant25);
        restaurantResponse.setRestaurant31(restaurant31);
        restaurantResponse.setRestaurant32(restaurant32);
        restaurantResponse.setRestaurant33(restaurant33);
        restaurantResponse.setRestaurant34(restaurant34);
        restaurantResponse.setRestaurant35(restaurant35);
        restaurantResponse.setRestaurant41(restaurant41);
        restaurantResponse.setRestaurant42(restaurant42);
        restaurantResponse.setRestaurant43(restaurant43);
        restaurantResponse.setRestaurant44(restaurant44);
        restaurantResponse.setRestaurant45(restaurant45);
        restaurantResponse.setRestaurant51(restaurant51);
        restaurantResponse.setRestaurant52(restaurant52);
        restaurantResponse.setRestaurant53(restaurant53);
        restaurantResponse.setRestaurant54(restaurant54);
        restaurantResponse.setRestaurant55(restaurant55);

        return ResponseEntity.ok(restaurantResponse);
    }

    // 获取所有商家的名字
    @GetMapping("/restaurant/name")
    public ResponseEntity<RestaurantNameResponse> getCanteenList() {
        String sql = "SELECT\n" +
                "\tRestaurant.restaurantName\n" +
                "FROM\n" +
                "\tRestaurant\n" +
                "\tLEFT JOIN Canteen ON Restaurant.canteenId = Canteen.canteenId \n" +
                "WHERE\n" +
                "\tRestaurant.canteenId = ?;";
        List<RestaurantName> restaurantNameList1 = restaurantNameDao.queryMulti(sql, RestaurantName.class, 1);
        List<RestaurantName> restaurantNameList2 = restaurantNameDao.queryMulti(sql, RestaurantName.class, 2);
        List<RestaurantName> restaurantNameList3 = restaurantNameDao.queryMulti(sql, RestaurantName.class, 3);
        List<RestaurantName> restaurantNameList4 = restaurantNameDao.queryMulti(sql, RestaurantName.class, 4);
        List<RestaurantName> restaurantNameList5 = restaurantNameDao.queryMulti(sql, RestaurantName.class, 5);

        RestaurantNameResponse restaurantNameResponse = new RestaurantNameResponse();
        restaurantNameResponse.setCanteen1(restaurantNameList1);
        restaurantNameResponse.setCanteen2(restaurantNameList2);
        restaurantNameResponse.setCanteen3(restaurantNameList3);
        restaurantNameResponse.setCanteen4(restaurantNameList4);
        restaurantNameResponse.setCanteen5(restaurantNameList5);

        return ResponseEntity.ok(restaurantNameResponse);
    }
}
