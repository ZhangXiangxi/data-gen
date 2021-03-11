package com.xiangxi.dagen.maria;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiangxi.dagen.histogram.Histogram;
import com.xiangxi.dagen.histogram.QueryToRange;
import com.xiangxi.dagen.qgen.QueryGenerator;
import com.xiangxi.dagen.qgen.QueryTemplate;
import com.xiangxi.dagen.qgen.QueryTemplateCollection;
import com.xiangxi.dagen.query.Query;
import com.xiangxi.dagen.query.QueryProxy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlRun {
    public static void main(String[] args) {
        System.out.println("Hello world");
        // labelTest();
        try {
            exampleGeneration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void histogramTest(int bucketNumber) {
        SqlExecutor executor = new SqlExecutor();
        executor.init();
        QueryTemplateCollection.prepare();
        ResultSet rs = null;
        Histogram histogram = new Histogram();
        histogram.equalGrams = new ArrayList<>();
        histogram.rangeGrams = new ArrayList<>();
        histogram.trivialGrams = new ArrayList<>();

        for (QueryTemplate template: QueryTemplateCollection.templates) {
            int tableId = template.from;
            String table = QueryProxy.tables[tableId];
            for (QueryTemplate.PredicateTemplate predicateTemplate : template.predicateSet) {
                boolean isEqual = predicateTemplate.equal;
                int lowest = predicateTemplate.lowest;
                int highest = predicateTemplate.highest;
                int attributeId = predicateTemplate.attribute;
                String attribute = QueryProxy.attributes[attributeId];

                if (isEqual) {
                    if (highest-lowest >= 1000) {
                        Histogram.TrivialHistogram trivialHistogram = new Histogram.TrivialHistogram();
                        trivialHistogram.attributeId = attributeId;
                        String sql = String.format("SELECT count(*) FROM %s", table);
                        rs = executor.executeForResult(sql);
                        try {
                            rs.next();
                            int count = rs.getInt(1);
                            System.out.printf("Attri %s has count of \t %d\n", attribute, count);
                            trivialHistogram.total = count;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        histogram.trivialGrams.add(trivialHistogram);
                        continue;
                    }
                    Histogram.EqualHistogram equalHistogram = new Histogram.EqualHistogram();
                    equalHistogram.attributeId = attributeId;
                    equalHistogram.entries = new ArrayList<>();

                    for (int i = lowest; i <= highest; i++) {
                        String sql = String.format("SELECT count(*) FROM %s WHERE %s = %d;", table, attribute, i);
                        rs = executor.executeForResult(sql);
                        try {
                            rs.next();
                            String count = rs.getString(1);
                            System.out.printf("Attri %s at %d is\t %s\n", attribute, i, count);
                            equalHistogram.entries.add(new Histogram.EqualHistogram.EqualEntry(i, Integer.parseInt(count)));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    String sql = String.format("SELECT count(*) FROM %s", table);
                    rs = executor.executeForResult(sql);
                    try {
                        rs.next();
                        int count = rs.getInt(1);
                        System.out.printf("Attri %s has count of \t %d\n", attribute, count);
                        equalHistogram.total = count;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    histogram.equalGrams.add(equalHistogram);
                } else {
                    Histogram.RangeHistogram rangeHistogram = new Histogram.RangeHistogram();
                    rangeHistogram.attributeId = attributeId;
                    rangeHistogram.entries = new ArrayList<>();
                    int interval = (highest - lowest) / bucketNumber;
                    while (interval <= 1) {
                        bucketNumber = bucketNumber / 2;
                        interval = (highest - lowest) / bucketNumber;
                    }
                    for (int i = 0; i < bucketNumber; i++) {
                        int bucketMin = lowest + i * interval;
                        int bucketMax = lowest + i * interval + interval - 1;
                        if (i == bucketNumber - 1) {
                            bucketMax = highest;
                        }
                        String sql = String.format("SELECT count(*) FROM %s WHERE %s >= %d AND %s <= %d", table, attribute, bucketMin, attribute, bucketMax);
                        rs = executor.executeForResult(sql);
                        try {
                            rs.next();
                            int count = rs.getInt(1);
                            System.out.printf("Attri %s at [%d, %d] is\t %d\n", attribute, bucketMin, bucketMax, count);
                            rangeHistogram.entries.add(new Histogram.RangeHistogram.RangeEntry(bucketMin,bucketMax, count));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    String sql = String.format("SELECT count(*) FROM %s", table);
                    rs = executor.executeForResult(sql);
                    try {
                        rs.next();
                        int count = rs.getInt(1);
                        System.out.printf("Attri %s has count of \t %d\n", attribute, count);
                        rangeHistogram.total = count;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    histogram.rangeGrams.add(rangeHistogram);
                }
            }
        }
        executor.close();
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(histogram));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void exampleGeneration() throws IOException {
        QueryTemplateCollection.prepare();
        SqlExecutor executor = new SqlExecutor();
        QueryToRange transformer = new QueryToRange();
        ObjectMapper mapper = new ObjectMapper();
        BufferedWriter writer = new BufferedWriter(new FileWriter("lable_10.txt"));
        executor.init();
        for (int i = 0; i <= 7; i++) {
            QueryGenerator generator = new QueryGenerator(QueryTemplateCollection.templates.get(i));
            for (int j = 0; j < 50000; j++) {
                Example example = new Example();
                Query query = generator.next();
                String sql = QueryProxy.queryToSql(query);
                example.sql = sql;
                example.ranges = transformer.transform(query);
                example.label = executor.executeForLabel(sql);
                try {
                    String jsonString = mapper.writeValueAsString(example);
                    System.out.println(jsonString);
                    writer.write(jsonString + "\n");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }


        }
        executor.close();
        writer.close();
    }

    // get the min and max of each column
    public static void minMaxTest() {
        SqlExecutor executor = new SqlExecutor();
        executor.init();
        ResultSet result = null;

        QueryTemplateCollection.prepare();

        for (QueryTemplate template : QueryTemplateCollection.templates) {
            String tableName = QueryProxy.tables[template.from];
            for (int attriID : template.projectSet) {
                String attributeName = QueryProxy.attributes[attriID];
                String sql = String.format("SELECT MIN(%s), MAX(%s) FROM %s;", attributeName, attributeName, tableName);
                result = executor.executeForResult(sql);
                try {
                    result.next();
                    String min = result.getString(1);
                    String max = result.getString(2);
                    System.out.printf("attribute: %s, \tmin: %s, max: %s\n", attributeName, min, max);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // get time from query
    public static void timeTest() {
        QueryTemplateCollection.prepare();
        QueryGenerator generator = new QueryGenerator(QueryTemplateCollection.templates.get(0));
        SqlExecutor executor = new SqlExecutor();
        executor.init();
        for (int i = 0; i < 150; i++) {
            String sql = QueryProxy.queryToSql(generator.next());
            long time = executor.executeForTime(sql);
            System.out.printf("time for query %d is %d\n", i, time);
        }
        executor.close();
    }

    // get count from query
    public static void countTest() {
        QueryTemplateCollection.prepare();
        QueryGenerator generator = new QueryGenerator(QueryTemplateCollection.templates.get(0));
        SqlExecutor executor = new SqlExecutor();
        executor.init();
        for (int i = 0; i < 150; i++) {
            String sql = QueryProxy.queryToSql(generator.next());
            int time = executor.executeForCount(sql);
            System.out.printf("count for query %d is %d, SQL: %s\n", i, time, sql);
        }
        executor.close();
    }

    // get count from label
    public static void labelTest() {
        QueryTemplateCollection.prepare();
        QueryGenerator generator = new QueryGenerator(QueryTemplateCollection.templates.get(7));
        SqlExecutor executor = new SqlExecutor();
        executor.init();
        for (int i = 0; i < 150; i++) {
            String sql = QueryProxy.queryToSql(generator.next());
            SqlExecutor.Label label = executor.executeForLabel(sql);
            System.out.printf("time and count for query %d is %dns, %d, SQL: %s\n", i, label.time, label.count, sql);
        }
        executor.close();
    }
}
