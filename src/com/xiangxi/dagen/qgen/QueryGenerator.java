package com.xiangxi.dagen.qgen;

import com.xiangxi.dagen.query.Predicate;
import com.xiangxi.dagen.query.Project;
import com.xiangxi.dagen.query.Query;
import com.xiangxi.dagen.query.QueryProxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QueryGenerator {
    protected QueryTemplate template;
    protected Random random;

    public QueryGenerator(QueryTemplate template) {
        this.template = template;
        random = new Random(System.nanoTime());
    }

    public Query next() {
        Query result = new Query();
        result.table = template.from;

        result.projects = new ArrayList<>();
        int project_size = random.nextInt(template.projectSet.length);
        ArrayList<Integer> projects = new ArrayList<>();
        for (int i : template.projectSet) {
            projects.add(i);
        }
        java.util.Collections.shuffle(projects);
        List<Integer> head = projects.subList(0, project_size);
        head.sort(Integer::compareTo);
        for (int i : head) {
            result.projects.add(new Project(i));
        }

        result.predicates = new ArrayList<>();
        int predicate_size = random.nextInt(template.predicateSet.size());
        List<QueryTemplate.PredicateTemplate> predicates = new ArrayList<>(List.copyOf(template.predicateSet));
        java.util.Collections.shuffle(predicates);
        for (int i = 0; i < predicate_size; i++) {
            QueryTemplate.PredicateTemplate predicate = predicates.get(i);
            int attributeId = predicate.attribute;
            int oprand = predicate.lowest + random.nextInt(predicate.highest - predicate.lowest - 1) + 1;
            int operator = random.nextInt(6);
            if (predicate.equal) {
                operator = 4;
                oprand = predicate.lowest + random.nextInt(predicate.highest - predicate.lowest + 1);
            }
            result.predicates.add(new Predicate(attributeId, operator, oprand));
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");
        QueryTemplateCollection.prepare();
        QueryGenerator generator = new QueryGenerator(QueryTemplateCollection.templates.get(0));
        for (int i = 0; i < 30; i++) {
            System.out.println(QueryProxy.queryToSql(generator.next()));
        }
    }

}
