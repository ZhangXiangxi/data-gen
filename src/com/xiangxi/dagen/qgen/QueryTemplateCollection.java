package com.xiangxi.dagen.qgen;

import com.xiangxi.dagen.query.QueryProxy;

import java.util.ArrayList;
import java.util.List;

public class QueryTemplateCollection {
    public static List<QueryTemplate> templates;
    public static boolean prepared = false;
    public static void prepare() {
        if (prepared) {
            return;
        }
        templates = new ArrayList<>();
        QueryTemplateBuilder builder = new QueryTemplateBuilder();

        // q1
        builder.setFrom(0)
                .setProjectSet(0, 7)
                .addPredicate(0,1,150000)
                .addPredicate(3,0,24, true)
                .addPredicate(5, 0, 10000)
        ;

        templates.add(builder.createQueryTemplate());

        // q2
        builder = new QueryTemplateBuilder();
        builder.setFrom(1)
                .setProjectSet(8, 11)
                .addPredicate(8,0,24, true)
                .addPredicate(10, 0, 4, true);
        templates.add(builder.createQueryTemplate());

        // q3
        builder = new QueryTemplateBuilder();
        builder.setFrom(2)
                .setProjectSet(12, 14)
                .addPredicate(12,0,4, true);
        templates.add(builder.createQueryTemplate());

        // q4
        builder = new QueryTemplateBuilder();
        builder.setFrom(3)
                .setProjectSet(15, 23)
                .addPredicate(15,1,200000)
                .addPredicate(20,1,50)
                .addPredicate(22, 901, 2099);
        templates.add(builder.createQueryTemplate());

        // q5
        builder = new QueryTemplateBuilder();
        builder.setFrom(4)
                .setProjectSet(24, 30)
                .addPredicate(24,1,10000)
                .addPredicate(27,0,24, true)
                .addPredicate(29, 1,10000);
        templates.add(builder.createQueryTemplate());

        // q6
        builder = new QueryTemplateBuilder();
        builder.setFrom(5)
                .setProjectSet(31, 35)
                .addPredicate(31,1,200000)
                .addPredicate(32,1,10000, true)
                .addPredicate(33, 1, 9999)
                .addPredicate(34, 1, 1000);
        templates.add(builder.createQueryTemplate());

        // q7
        builder = new QueryTemplateBuilder();
        builder.setFrom(6)
                .setProjectSet(36, 44)
                .addPredicate(36,1,6000000)
                .addPredicate(37,1,149999, true)
                .addPredicate(39, 858, 555285);
        templates.add(builder.createQueryTemplate());

        // q8
        builder = new QueryTemplateBuilder();
        builder.setFrom(7)
                .setProjectSet(45, 60)
                .addPredicate(45,1, 6000000)
                .addPredicate(46,1,200000)
                .addPredicate(47, 1, 10000, true)
                .addPredicate(49, 1, 50)
                .addPredicate(50, 904, 104900);
        templates.add(builder.createQueryTemplate());

        prepared = true;
    }

    public static void test() {
        prepare();
        System.out.println("template number: " + templates.size());
        for (QueryTemplate template : templates) {
            System.out.println(template);
            int last_project = template.projectSet[template.projectSet.length - 1];
            System.out.println(QueryProxy.attributes[last_project]);
        }
    }
}
