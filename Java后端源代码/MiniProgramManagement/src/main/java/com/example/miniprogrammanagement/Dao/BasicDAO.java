package com.example.miniprogrammanagement.Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import com.example.miniprogrammanagement.utils.JDBCUtilsByDruid;

public class BasicDAO<T> {
    private final QueryRunner queryRunner = new QueryRunner();

    /**
     * 开发通用的 dml 方法, 针对任意表(增删改操作)
     * @param sql
     * @param parameters
     * @return
     */
    public int update(String sql, Object... parameters) {
        Connection connection = null;

        try {
            connection = JDBCUtilsByDruid.getConnection();
            return queryRunner.update(connection, sql, parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }

    /**
     * 查询操作：返回结果为多个对象(即查询的结果是多行记录)的通用方法, 针对任意表
     * @param sql
     * @param clazz
     * @param parameters
     * @return
     */
    public List<T> queryMulti(String sql, Class<T> clazz, Object... parameters) {
        Connection connection = null;

        try {
            connection = JDBCUtilsByDruid.getConnection();
            return queryRunner.query(connection, sql, new BeanListHandler<>(clazz), parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }

    /**
     * 查询操作：返回结果为单行记录 的通用方法，针对任意表
     * @param sql
     * @param clazz
     * @param parameters
     * @return
     */
    public T querySingle(String sql, Class<T> clazz, Object... parameters) {

        Connection connection = null;

        try {
            connection = JDBCUtilsByDruid.getConnection();
            return  queryRunner.query(connection, sql, new BeanHandler<>(clazz), parameters);

        } catch (SQLException e) {
            throw  new RuntimeException(e);// 将编译异常->运行异常 ,抛出
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }

    /**
     * 查询操作：返回结果为单行单列，即返回单值(Object 对象)的通用方法，针对任意表
     * @param sql
     * @param parameters
     * @return
     */
    public Object queryScalar(String sql, Object... parameters) {

        Connection connection = null;

        try {
            connection = JDBCUtilsByDruid.getConnection();
            return queryRunner.query(connection, sql, new ScalarHandler(), parameters);

        } catch (SQLException e) {
            throw  new RuntimeException(e);// 将编译异常->运行异常 ,抛出
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }
}

