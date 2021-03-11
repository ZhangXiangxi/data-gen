package com.xiangxi.dagen.maria;

public class TestMaria {

    public void test() {

        String[] sqls = {"SELECT * FROM products_tbl; ", "SELECT product_name FROM products_tbl;"};
        SqlExecutor sqlExecutor = new SqlExecutor();
        if (!sqlExecutor.init()) {
            System.out.println("init failed");
            return;
        }
        for (int i = 0; i < sqls.length; i++) {
            long time = sqlExecutor.executeForTime(sqls[i]);
            System.out.println("execute time for sql " + i + " is: " + time);
        }
        sqlExecutor.close();
    }
}
