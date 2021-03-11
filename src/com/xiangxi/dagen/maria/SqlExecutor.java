package com.xiangxi.dagen.maria;

import java.sql.*;

public class SqlExecutor {
    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String DB_URL = "jdbc:mariadb://localhost/tpch";

    private static final String USER = "zhangxiangxi";
    private static final String PASS = "zhangxiangxi";

    protected Connection conn = null;
    protected Statement stmt = null;

    public SqlExecutor() {
    }

    public boolean init() {
        boolean succeed = true;
        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("connecting to database");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("connected database succesfully!");

            System.out.println("Getting result");
            stmt = conn.createStatement();


        } catch (Exception e) {
            e.printStackTrace();
            succeed = false;
        }
        return succeed;
    }

    public void close() {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        System.out.println("goodbye!");
    }

    public long executeForTime(String sql) {
        long start = System.nanoTime();
        long end = 0;
        try {
            ResultSet resultSet = stmt.executeQuery(sql);
            end = System.nanoTime();
        } catch (SQLException se) {
            se.printStackTrace();
            end = start;
        }
        return end - start;
    }

    public int executeForCount(String sql) {
        int count = 0;
        try {
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                count++;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return count;
    }

    public ResultSet executeForResult(String sql) {
        ResultSet result = null;
        try {
            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Label executeForLabel(String sql) {
        int count = 0;
        long start = System.nanoTime();
        long end = 0;
        try {
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                count++;
            }
            end = System.nanoTime();
        } catch (SQLException se) {
            se.printStackTrace();
            end = start;
        }
        return new Label(end-start, count);
    }

    public static class Label {
        public long time;
        public long count;

        public Label(long time, long count) {
            this.time = time;
            this.count = count;
        }
    }
}
