package cn.yyfullstack.chapter2.helper;

import cn.yyfullstack.chapter2.util.CollectionUtil;
import cn.yyfullstack.chapter2.util.PropsUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    private static final QueryRunner QUERY_RUNNER;

    private static final BasicDataSource DATA_SOURCE;

    static {
        CONNECTION_HOLDER = new ThreadLocal<Connection>();

        QUERY_RUNNER = new QueryRunner();

        Properties conf = PropsUtil.loadProps("config.properties");

        String driver = conf.getProperty("jdbc.driver");
        String url = conf.getProperty("jdbc.url");
        String username = conf.getProperty("jdbc.username");
        String password = conf.getProperty("jdbc.password");

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);
    }

    //e.printStackTrace( )是打印异常栈信息，
    // 而throw new RuntimeException(e)是把异常包在一个运行时异常中抛出
    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 查询语句
     *
     * @param sql
     * @param params
     * @return
     */
    public List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result;
        try {
            Connection conn = getConnection();
            LOGGER.info("executeQuery SQL: ", sql);
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);

        } catch (Exception e) {
            LOGGER.error("execute query failure", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 更新语句（包括：update、insert、delete）
     *
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;
        try {
            Connection conn = getConnection();
            LOGGER.info("executeUpdate SQL: ", sql);
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (Exception e) {
            LOGGER.error("execute update failure", e);
            throw new RuntimeException(e);
        }
        return rows;
    }

//    public static int[] executeBatch(String sql, Object[][] params) {
//        int[] rows = new int[0];
//        try {
//            Connection conn = getConnection();
//            rows = QUERY_RUNNER.batch(conn, sql, params);
//        } catch (Exception e) {
//            LOGGER.error("execute batch failure", e);
//            throw new RuntimeException(e);
//        }
//        return rows;
//    }

    public static long executeCount(String sql, Object... params) {
        Number num = 0;
        try {
            Connection conn = getConnection();
            num = (Number) QUERY_RUNNER.query(conn, sql, new ScalarHandler<>(), params);
        } catch (Exception e) {
            LOGGER.error("execute count failure", e);
            throw new RuntimeException(e);
        }
        return (num != null) ? num.longValue() : -1;
    }

    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (Exception e) {
            LOGGER.error("execute query list failure", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

//    public static <T> PageBean queryByPage(Class<T> entityClass, String sql, PageBean pageBean, Object... params) {
//        if (pageBean == null) {
//            pageBean = new PageBean();
//        }
//        try {
//            Connection conn = getConnection();
//            List<T> entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
//            pageBean.setT(entityList);
//        } catch (Exception e) {
//            LOGGER.error("execute query list failure", e);
//            throw new RuntimeException(e);
//        }
//        return pageBean;
//    }


    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (Exception e) {
            LOGGER.error("execute query list failure", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }
        String sql = "INSERT INTO " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += columns + " VALUES " + values;
        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql, params) == 1;
    }

    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not update entity: fieldMap is empty");
            return false;
        }
        String sql = "UPDATE " + getTableName(entityClass) + " SET ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(" = ?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id = ? ";
        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();
        return executeUpdate(sql, params) == 1;
    }

    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id = ?";
        return executeUpdate(sql, id) == 1;
    }

    public static <T> long countEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        String sql = "SELECT COUNT(*) as count FROM " + getTableName(entityClass);
        if (CollectionUtil.isEmpty(fieldMap)) {
            return executeCount(sql, null);
        } else {
            StringBuilder columns = new StringBuilder(" WHERE ");
            for (String fieldName : fieldMap.keySet()) {
                columns.append(fieldName).append(" = ?, ");
            }
            sql += columns.substring(0, columns.lastIndexOf(", "));
            List<Object> paramList = new ArrayList<Object>();
            paramList.addAll(fieldMap.values());
            Object[] params = paramList.toArray();
            return executeCount(sql, params);
        }
    }

    //开启事务
    public static void startTransaction() {
        Connection conn = getConnection();
        try {
            //开启事务
            conn.setAutoCommit(false);
        } catch (Exception e) {
            LOGGER.error("start transaction failure", e);
            throw new RuntimeException(e);
        }
    }

    //提交事务
    public static void commitTransaction() {
        Connection conn = getConnection();
        try {
            //提交事务
            conn.commit();
        } catch (Exception e) {
            LOGGER.error("commit transaction failure", e);
            throw new RuntimeException(e);
        }
    }

    //回滚事务
    public static void rollbackTransaction() {
        Connection conn = getConnection();
        try {
            //回滚事务
            conn.rollback();
        } catch (Exception e) {
            LOGGER.error("rollback transaction failure", e);
            throw new RuntimeException(e);
        }
    }

    //关闭连接
    public static void colseConnection() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                //关闭连接
                conn.close();
            } catch (Exception e) {
                LOGGER.error("colose connection failure", e);
                throw new RuntimeException(e);
            } finally {
                //从当前线程移除连接 切记
                CONNECTION_HOLDER.remove();
            }
        }
    }

    public static <T> String getTableName(Class<T> entityClass) {
        return entityClass.getSimpleName();
    }
}
