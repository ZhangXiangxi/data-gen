package com.xiangxi.dagen.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QueryProxy {
    public static final String[] tables = {"CUSTOMER", "NATION", "REGION", "PART", "SUPPLIER", "PARTSUPP", "ORDERS", "LINEITEM"};
    public static final String[] operators = {"<", "<=", ">", ">=", "=", "<>"};
    public static final String[] attributes = {
            "C_CUSTKEY", "C_NAME", "C_ADDRESS", "C_NATIONKEY", "C_PHONE", "C_ACCTBAL", "C_MKTSEGMENT", "C_COMMENT",
            "N_NATIONKEY", "N_NAME", "N_REGIONKEY", "N_COMMENT",
            "R_REGIONKEY", "R_NAME", "R_COMMENT",
            "P_PARTKEY", "P_NAME", "P_MFGR", "P_BRAND", "P_TYPE", "P_SIZE", "P_CONTAINER", "P_RETAILPRICE", "P_COMMENT",
            "S_SUPPKEY", "S_NAME", "S_ADDRESS", "S_NATIONKEY", "S_PHONE", "S_ACCTBAL", "S_COMMENT",
            "PS_PARTKEY", "PS_SUPPKEY", "PS_AVAILQTY", "PS_SUPPLYCOST", "PS_COMMENT",
            "O_ORDERKEY", "O_CUSTKEY", "O_ORDERSTATUS", "O_TOTALPRICE", "O_ORDERDATE", "O_ORDERPRIORITY", "O_CLERK", "O_SHIPPRIORITY", "O_COMMENT",
            "L_ORDERKEY", "L_PARTKEY", "L_SUPPKEY", "L_LINENUMBER", "L_QUANTITY", "L_EXTENDEDPRICE", "L_DISCOUNT", "L_TAX", "L_RETURNFLAG",
            "L_LINESTATUS", "L_SHIPDATE", "L_COMMITDATE", "L_RECEIPTDATE", "L_SHIPINSTRUCT", "L_SHIPMODE", "L_COMMENT"
    };
    public static Query jsonToQuery(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        Query query = mapper.readValue(json, Query.class);
        return query;
    }
    public static String queryToJson(Query q) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(q);
    }
    public static String queryToSql(Query q) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        if (q.projects.size() == 0) {
            sb.append("* ");
        } else {
            for (int i = 0; i < q.projects.size() - 1; i++) {
                String attribute = "" + attributes[q.projects.get(i).id] + ", ";
                sb.append(attribute);
            }
            String attribute = "" + attributes[q.projects.get(q.projects.size() - 1).id] + " ";
            sb.append(attribute);
        }
        sb.append("FROM ");
        String table = "" + tables[q.table] + " ";
        sb.append(table);
        if (q.predicates.size() > 0) {
            sb.append("WHERE ");
            for (int i = 0; i < q.predicates.size() - 1; i++) {
                Predicate predicate = q.predicates.get(i);
                String attribute = "" + attributes[predicate.attri] + " ";
                String operator = "" + operators[predicate.operator] + " ";
                String oprand = "" + predicate.oprand + " ";
                sb.append(attribute).append(operator).append(oprand).append(" AND ");
            }
            Predicate predicate = q.predicates.get(q.predicates.size() - 1);
            String attribute = "" + attributes[predicate.attri] + " ";
            String operator = "" + operators[predicate.operator] + " ";
            String oprand = "" + predicate.oprand + " ";
            sb.append(attribute).append(operator).append(oprand);
        }
        sb.append(";");
        return sb.toString();
    }
    public static void test() {
        Query generatedQuery = new Query();
        generatedQuery.type = 1;
        generatedQuery.table = 3;
        generatedQuery.projects.add(new Project(3));
        generatedQuery.predicates.add(new Predicate(3,2, 4));
        try {
            String result = queryToJson(generatedQuery);
            System.out.println("Object to json result is: " + result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String json = "{\"table\":7,\"type\":3, \"projects\":[{\"id\":3}],\"predicates\":[{\"attri\":3,\"operator\":2,\"oprand\":4}]}";
        try {
            Query query = jsonToQuery(json);
            System.out.println("query has table of " + query.table);
            System.out.println("query predicates size is: " + query.predicates.size());

            String sql = queryToSql(query);
            System.out.println("generated SQL is: " + sql);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
