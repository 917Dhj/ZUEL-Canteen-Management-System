package com.example.miniprogrammanagement.Controller;

import com.example.miniprogrammanagement.Bean.*;
import com.example.miniprogrammanagement.Dao.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AnalyzeDataController {
    private OrderRankDao orderRankDao = new OrderRankDao();
    private TodayOrderCountDao todayOrderCountDao = new TodayOrderCountDao();
    private TodayTurnoverDao todayTurnoverDao = new TodayTurnoverDao();
    private RestaurantRateDao restaurantRateDao = new RestaurantRateDao();
    private TodayPostCountDao todayPostCountDao = new TodayPostCountDao();
    private UnfinishedOrderCountDao unfinishedOrderCountDao = new UnfinishedOrderCountDao();
    private FinishedOrderCountDao finishedOrderCountDao = new FinishedOrderCountDao();
    private OrderDao orderDao = new OrderDao();
    private TotalDataIntDao totalDataIntDao = new TotalDataIntDao();
    private AdminNewPostResponseDao adminNewPostResponseDao = new AdminNewPostResponseDao();
    private AdminDash2DataDao adminDash2DataDao = new AdminDash2DataDao();

    // 网页端首页的商家订单量排行榜数据接口
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @GetMapping("/analysis/orderRank")
    public ResponseEntity<List<OrderRank>> getOrderCount(){
        String sql = "SELECT\n" +
                "\torderCount,\n" +
                "\trestaurantName,\n" +
                "\t( orderCount / totalOrders ) * 100 AS percentage \n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\tCOUNT(*) AS orderCount,\n" +
                "\t\tRestaurant.restaurantId,\n" +
                "\t\tRestaurant.restaurantName,\n" +
                "\t\tSUM(\n" +
                "\t\tCOUNT(*)) OVER () AS totalOrders \n" +
                "\tFROM\n" +
                "\t\tOrderTable\n" +
                "\t\tLEFT JOIN Courses ON OrderTable.dishID = Courses.dishID\n" +
                "\t\tLEFT JOIN Restaurant ON Courses.restaurantID = Restaurant.restaurantId \n" +
                "\tGROUP BY\n" +
                "\t\tRestaurant.restaurantId,\n" +
                "\t\tRestaurant.restaurantName \n" +
                "\t) AS subquery \n" +
                "ORDER BY\n" +
                "\torderCount DESC\n" +
                "\tLIMIT 5;";

        List<OrderRank> orderRankList = orderRankDao.queryMulti(sql, OrderRank.class);
        orderRankList.get(0).setColor("#f25e43");
        orderRankList.get(1).setColor("#00bcd4");
        orderRankList.get(2).setColor("#64d572");
        orderRankList.get(3).setColor("#e9a745");
        orderRankList.get(4).setColor("#009688");
        return ResponseEntity.ok(orderRankList);
    }

    // 商家数据中心的统计数据
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @PostMapping("/analysis/todayData")
    public ResponseEntity<DataCenterResponse> getDataCenter(@RequestBody Map<String, String> requestBody) {
        int restaurantId = Integer.parseInt(requestBody.get("restaurantId"));

        String sql_countTodayOrder = "SELECT COUNT(*) AS todayOrderCount\n" +
                "FROM OrderTable\n" +
                "LEFT JOIN Courses ON OrderTable.dishID = Courses.dishID\n" +
                "WHERE DATE(orderTime) = CURDATE() AND Courses.restaurantID = ?";

        String sql_getTodayTurnover = "SELECT SUM(price) AS turnover\n" +
                "FROM OrderTable \n" +
                "LEFT JOIN Courses ON OrderTable.dishID = Courses.dishID\n" +
                "WHERE DATE(orderTime) = CURDATE() AND Courses.restaurantID = ?";

        String sql_getTodayOrderUser = "SELECT COUNT(DISTINCT userID) AS total\n" +
                "FROM OrderTable\n" +
                "LEFT JOIN Courses ON OrderTable.dishID = Courses.dishID\n" +
                "WHERE DATE(orderTime) = CURDATE() AND Courses.restaurantID = ?";

        String sql_getRestaurantRate = "SELECT\n" +
                "\tROUND(\n" +
                "\t\tIFNULL( AVG( Post.rate ), 0 ),\n" +
                "\t\t1 \n" +
                "\t) AS restaurantRate \n" +
                "FROM\n" +
                "\tRestaurant\n" +
                "\tLEFT JOIN Canteen ON Restaurant.canteenId = Canteen.canteenId\n" +
                "\tLEFT JOIN Post ON Restaurant.restaurantName = Post.shopName \n" +
                "\tAND Canteen.canteenName = Post.canteenName \n" +
                "WHERE\n" +
                "\trestaurantId = ? \n" +
                "GROUP BY\n" +
                "\tRestaurant.restaurantName";

        String sql_getTodayPostCount = "SELECT COUNT(*) AS todayPostCount\n" +
                "FROM Post\n" +
                "LEFT JOIN Restaurant ON Post.shopName = Restaurant.restaurantName\n" +
                "WHERE DATE(postTime) = CURDATE() AND Restaurant.restaurantId = ?";

        String sql_getUnfinishedOrderCount = "SELECT COUNT(*) AS unfinishedOrderCount\n" +
                "FROM OrderTable\n" +
                "LEFT JOIN Courses ON OrderTable.dishID = Courses.dishID\n" +
                "WHERE `status` = '制作中' AND Courses.restaurantID = ?";

        String sql_getFinishedOrderCount = "SELECT COUNT(*) AS finishedOrderCount\n" +
                "FROM OrderTable\n" +
                "LEFT JOIN Courses ON OrderTable.dishID = Courses.dishID\n" +
                "WHERE `status` = '已完成' AND Courses.restaurantID = ?";

        TodayOrderCount todayOrderCount = todayOrderCountDao.querySingle(sql_countTodayOrder, TodayOrderCount.class, restaurantId);
        int todayOrder = todayOrderCount.getTodayOrderCount(); // 今日订单数量

        TotalDataInt totalDataInt = totalDataIntDao.querySingle(sql_getTodayOrderUser, TotalDataInt.class, restaurantId);
        int todayOrderUser = totalDataInt.getTotal();

        TodayTurnover todayTurnover = todayTurnoverDao.querySingle(sql_getTodayTurnover, TodayTurnover.class, restaurantId);
        float turnover = todayTurnover.getTurnover(); // 今日营业总金额

        RestaurantRate restaurantRate = restaurantRateDao.querySingle(sql_getRestaurantRate, RestaurantRate.class, restaurantId);
        float rate = restaurantRate.getRestaurantRate(); // 当前商家评分

        TodayPostCount todayPostCount = todayPostCountDao.querySingle(sql_getTodayPostCount, TodayPostCount.class, restaurantId);
        int postCount = todayPostCount.getTodayPostCount(); // 今日发帖数量

        UnfinishedOrderCount unfinishedOrderCount = unfinishedOrderCountDao.querySingle(sql_getUnfinishedOrderCount, UnfinishedOrderCount.class, restaurantId);
        int unfinishedOrder = unfinishedOrderCount.getUnfinishedOrderCount(); // 未完成订单数量

        FinishedOrderCount finishedOrderCount = finishedOrderCountDao.querySingle(sql_getFinishedOrderCount, FinishedOrderCount.class, restaurantId);
        int finishedOrder = finishedOrderCount.getFinishedOrderCount(); // 已完成订单数量

        // 将数据封装到一个dataCenterResponse对象中
        DataCenterResponse dataCenterResponse = new DataCenterResponse();
        dataCenterResponse.setTodayOrderCount(todayOrder);
        dataCenterResponse.setTodayTurnover(turnover);
        dataCenterResponse.setTodayOrderUser(todayOrderUser);
        dataCenterResponse.setRestaurantRate(rate);
        dataCenterResponse.setTodayPostCount(postCount);
        dataCenterResponse.setUnfinishedOrderCount(unfinishedOrder);
        dataCenterResponse.setFinishedOrderCount(finishedOrder);

        return ResponseEntity.ok(dataCenterResponse);
    }

    // 管理员首页数据统计数据
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @GetMapping("/analysis/total")
    public ResponseEntity<AdminDataCenterResponse> getAdminDataCenter() {
        String sql_getTotalOrderCount = "SELECT COUNT(*) AS total\n" +
                "FROM OrderTable";
        String sql_getTotalOrderUser = "SELECT COUNT(DISTINCT userID) AS total\n" +
                "FROM OrderTable";
        String sql_getTotalCourseCount = "SELECT COUNT(*) AS total\n" +
                "FROM Courses";
        String sql_getTotalTurnover = "SELECT SUM(price) AS turnover\n" +
                "FROM OrderTable \n" +
                "LEFT JOIN Courses ON OrderTable.dishID = Courses.dishID";

        TotalDataInt totalOrderCount = totalDataIntDao.querySingle(sql_getTotalOrderCount, TotalDataInt.class);
        int orderCount = totalOrderCount.getTotal(); // 订单总数量

        TotalDataInt totalOrderUser = totalDataIntDao.querySingle(sql_getTotalOrderUser, TotalDataInt.class);
        int orderUser = totalOrderUser.getTotal(); // 下单总人数

        TotalDataInt totalCourseCount = totalDataIntDao.querySingle(sql_getTotalCourseCount, TotalDataInt.class);
        int courseCount = totalCourseCount.getTotal(); // 菜品总数量

        TodayTurnover totalTurnover = todayTurnoverDao.querySingle(sql_getTotalTurnover, TodayTurnover.class);
        float turnover = totalTurnover.getTurnover(); // 总营业额

        // 封装数据
        AdminDataCenterResponse adminDataCenterResponse = new AdminDataCenterResponse();
        adminDataCenterResponse.setTotalOrderCount(orderCount);
        adminDataCenterResponse.setTotalOrderUser(orderUser);
        adminDataCenterResponse.setTotalCourseCount(courseCount);
        adminDataCenterResponse.setTotalTurnover(turnover);

        return ResponseEntity.ok(adminDataCenterResponse);
    }

    // 管理员首页最新评价列表
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @GetMapping("/analysis/post")
    public ResponseEntity<List<AdminNewPostResponse>> getAdminNewPost() {
        String sql = "SELECT\n" +
                "\tposterName,\n" +
                "\tpostContext,\n" +
                "\tCASE\n" +
                "        WHEN TIMESTAMPDIFF(MINUTE, postTime, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, postTime, NOW()), '分钟')\n" +
                "        WHEN TIMESTAMPDIFF(HOUR, postTime, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, postTime, NOW()), '小时')\n" +
                "        ELSE CONCAT(TIMESTAMPDIFF(DAY, postTime, NOW()), '天')\n" +
                "\t\tEND AS timeDifference\n" +
                "FROM\n" +
                "\tPost \n" +
                "WHERE Post.`delete` = 0\n" +
                "ORDER BY\n" +
                "\tpostTime DESC \n" +
                "\tLIMIT 5";

        List<AdminNewPostResponse> adminNewPostResponseList = adminNewPostResponseDao.queryMulti(sql, AdminNewPostResponse.class);
        adminNewPostResponseList.get(0).setColor("#00bcd4");
        adminNewPostResponseList.get(1).setColor("#1ABC9C");
        adminNewPostResponseList.get(2).setColor("#3f51b5");
        adminNewPostResponseList.get(3).setColor("#f44336");
        adminNewPostResponseList.get(4).setColor("#009688");
        return ResponseEntity.ok(adminNewPostResponseList);
    }

    // 管理员首页环形图数据
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @GetMapping("/analysis/dash2")
    public ResponseEntity<List<AdminDash2Data>> getAdminDash2() {
        String sql = "SELECT\n" +
                "\tCOUNT(*) AS `value`,\n" +
                "\tCanteen.canteenName AS `name`\n" +
                "FROM\n" +
                "\tOrderTable\n" +
                "\tLEFT JOIN Courses ON OrderTable.dishID = Courses.dishID\n" +
                "\tLEFT JOIN Restaurant ON Courses.restaurantID = Restaurant.restaurantId\n" +
                "\tLEFT JOIN Canteen ON Restaurant.canteenId = Canteen.canteenId\n" +
                "GROUP BY\n" +
                "\tCanteen.canteenName\n" +
                "ORDER BY\n" +
                "\t`value` DESC";

        List<AdminDash2Data> adminDash2DataList = adminDash2DataDao.queryMulti(sql, AdminDash2Data.class);
        return ResponseEntity.ok(adminDash2DataList);
    }

    // 管理员首页柱状图数据
    @CrossOrigin(origins = "*") // 添加 @CrossOrigin 注解
    @GetMapping("/analysis/dash1")
    public ResponseEntity<float[]> getAdminDash1() {
        String sql = "SELECT COALESCE\n" +
                "\t( SUM( price ), 0 ) AS turnover \n" +
                "FROM\n" +
                "\tOrderTable\n" +
                "\tLEFT JOIN Courses ON OrderTable.dishID = Courses.dishID \n" +
                "WHERE\n" +
                "\tDATE ( orderTime ) = DATE_SUB(\n" +
                "\t\tCURDATE(),\n" +
                "\tINTERVAL ? DAY)";

        TodayTurnover day1 = todayTurnoverDao.querySingle(sql, TodayTurnover.class, 6);
        TodayTurnover day2 = todayTurnoverDao.querySingle(sql, TodayTurnover.class, 5);
        TodayTurnover day3 = todayTurnoverDao.querySingle(sql, TodayTurnover.class, 4);
        TodayTurnover day4 = todayTurnoverDao.querySingle(sql, TodayTurnover.class, 3);
        TodayTurnover day5 = todayTurnoverDao.querySingle(sql, TodayTurnover.class, 2);
        TodayTurnover day6 = todayTurnoverDao.querySingle(sql, TodayTurnover.class, 1);
        TodayTurnover day7 = todayTurnoverDao.querySingle(sql, TodayTurnover.class, 0);
        System.out.println(day1.getTurnover());
        float[] data = {day1.getTurnover(), day2.getTurnover(), day3.getTurnover(), day4.getTurnover(), day5.getTurnover(), day6.getTurnover(), day7.getTurnover()};
        return ResponseEntity.ok(data);
    }
}
