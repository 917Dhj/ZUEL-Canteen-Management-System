package com.example.miniprogrammanagement.test;

import com.example.miniprogrammanagement.Bean.*;
import com.example.miniprogrammanagement.Dao.*;
import com.example.miniprogrammanagement.Tools.IdGenerator;

import java.util.Arrays;
import java.util.List;

public class testPost {
    private PostDao postDao = new PostDao();
    private UserDao userDao = new UserDao();
    private OrderDao orderDao = new OrderDao();
    private CourseDao courseDao = new CourseDao();
    private CommentDao commentDao = new CommentDao();
    private DishDao dishDao = new DishDao();
    private RestaurantDao restaurantDao = new RestaurantDao();
    private RestaurantNameDao restaurantNameDao = new RestaurantNameDao();
    private OrderRankDao orderRankDao = new OrderRankDao();
    private TodayOrderCountDao todayOrderCountDao = new TodayOrderCountDao();
    private OrderTableDao orderTableDao = new OrderTableDao();
    private AdminPostTableDao adminPostTableDao = new AdminPostTableDao();
    private TotalDataIntDao totalDataIntDao = new TotalDataIntDao();
    private TodayTurnoverDao todayTurnoverDao = new TodayTurnoverDao();
    private NewDishDataDao newDishDataDao = new NewDishDataDao();

    public void testSelectMulti() {
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
        System.out.println(Arrays.toString(data));
    }

    public void testInsert() {
        int restaurantId = 11;
        String dishName = "小丸子";
        double price = 24.0;
        String dishImage = "https://tdesign.gtimg.com/mobile/demos/example2.png";
        String status = "上架";
        int canteenId = 1;
        String canteenName = "环湖园";
        int latestDishId = 115;
//        int newDishId = IdGenerator.generateDishId(latestDishId); // 生成新的dishID
        String sql = "insert into Courses(canteenID, canteenName, dishID, dishName, price, restaurantStatus, restaurantID, dishImage) values(?,?,?,?,?,?,?,?);";
//        int insert = newDishDataDao.update(sql, canteenId, canteenName, newDishId, dishName, price, status, restaurantId, dishImage);
//        System.out.println(insert);
    }

    public void testDelete() {
        String sql = "UPDATE Post \n" +
                "SET `delete` = 1 \n" +
                "WHERE\n" +
                "\tpostId = ?";

        int delete = postDao.update(sql, 30);
        System.out.println(delete);
    }

    public void testUpdate() {
        String orderId = "23";
        String newStatus = "已完成";
//        System.out.println(userId+userName);

        String sql = "UPDATE OrderTable \n" +
                "SET STATUS = ? \n" +
                "WHERE\n" +
                "\torderId = ?;";

        int update = orderDao.update(sql, newStatus, orderId);
        System.out.println(update);
    }

    public static void main(String[] args) {
        testPost test = new testPost(); // 创建 TestPost 类的对象
//        test.testSelectMulti(); // 调用实例方法
        test.testInsert();
//        test.testDelete();
//        test.testUpdate();
    }
}
