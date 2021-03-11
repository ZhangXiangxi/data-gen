package com.xiangxi.dagen.histogram;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiangxi.dagen.qgen.QueryGenerator;
import com.xiangxi.dagen.qgen.QueryTemplateCollection;
import com.xiangxi.dagen.query.Predicate;
import com.xiangxi.dagen.query.Query;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class QueryToRange {
    public Map<Integer, Integer> attriToCount;
    public Map<Integer, Map<Integer, Integer>> equalToCount;
    public Map<Integer, List<Histogram.RangeHistogram.RangeEntry>> rangeToCount;
    protected Histogram histogram;
    Set<Integer> trivialAttributes;
    Set<Integer> equalAttributes;
    Set<Integer> rangeAttributes;
    public QueryToRange(String path) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        try {
            histogram = mapper.readValue(file, Histogram.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        trivialAttributes = new HashSet<>();
        equalAttributes = new HashSet<>();
        rangeAttributes = new HashSet<>();
        attriToCount = new HashMap<>();
        equalToCount = new HashMap<>();
        rangeToCount = new HashMap<>();


        assert histogram != null;
        for (Histogram.TrivialHistogram trivialHistogram : histogram.trivialGrams) {
            trivialAttributes.add(trivialHistogram.attributeId);
            attriToCount.put(trivialHistogram.attributeId, trivialHistogram.total);
        }
        for (Histogram.EqualHistogram equalHistogram : histogram.equalGrams) {
            equalAttributes.add(equalHistogram.attributeId);
            attriToCount.put(equalHistogram.attributeId, equalHistogram.total);
            Map<Integer, Integer> equalMap = new HashMap<>();
            for (Histogram.EqualHistogram.EqualEntry entry : equalHistogram.entries) {
                equalMap.put(entry.key, entry.value);
            }
            equalToCount.put(equalHistogram.attributeId, equalMap);
        }
        for (Histogram.RangeHistogram rangeHistogram: histogram.rangeGrams) {
            rangeAttributes.add(rangeHistogram.attributeId);
            attriToCount.put(rangeHistogram.attributeId, rangeHistogram.total);
            List<Histogram.RangeHistogram.RangeEntry> ranges = new ArrayList<>(rangeHistogram.entries);
            rangeToCount.put(rangeHistogram.attributeId, ranges);
        }
    }

    public List<Range> transform(Query q) {
        List<Range> result = new ArrayList<>();
        List<Predicate> predicates = q.predicates;
        for (Predicate predicate : predicates) {
            int attributeId = predicate.attri;

            if (trivialAttributes.contains(attributeId)) { // for trivial
                double ratio = 1.0 / (double)attriToCount.get(attributeId);
                result.add(new Range(attributeId, ratio));
                continue;
            }

            if (equalAttributes.contains(attributeId)) { // for equal
                int count = equalToCount.get(attributeId).get(predicate.oprand);
                double ratio = (double) count / (double) attriToCount.get(attributeId);
                result.add(new Range(attributeId, ratio));
                continue;
            }

            if (rangeAttributes.contains(attributeId)) { // for range
                int operator = predicate.operator;
                List<Histogram.RangeHistogram.RangeEntry> ranges = rangeToCount.get(attributeId);
                int count = 0;
                int oprand = predicate.oprand;
                for (Histogram.RangeHistogram.RangeEntry range : ranges) {
                    // "<", "<=", ">", ">=", "=", "<>"
                    // 0      1     2    3    4     5
                    if (range.highest < oprand) { // below
                        if (operator == 0 || oprand == 1 || operator == 5) {
                            count += range.count;
                        }
                        continue;
                    }
                    if (range.lowest > oprand) { // above
                        if (operator == 2 || operator == 3 || operator == 5) {
                            count += range.count;
                        }
                        continue;
                    }
                    // within range
                    double partRatio = (double) (oprand - range.lowest) / (double) (range.highest - range.lowest);
                    if (operator == 0 || operator == 1) {
                        count += (int)(partRatio * range.count);
                    }
                    if (operator == 2 || operator == 3) {
                        count += (int)((1-partRatio) * range.count);
                    }
                    if (operator == 4 || operator == 5) {
                        count += range.count / 2; // Caution: this value is by-design
                    }
                }

                double ratio = (double) count / (double) attriToCount.get(attributeId);
                result.add(new Range(attributeId, ratio));
                continue;
            }
            throw new IllegalArgumentException("This attribute " + attributeId +" in predicate is not seen!");
        }
        return result;
    }



    public QueryToRange() {
        this("/home/zhangxiangxi/project/sqltovec/data-gen/src/com/xiangxi/dagen/histogram/histogram.json");
    }

    public void test() {
        System.out.println("hello");
        QueryToRange transformer = new QueryToRange();
        QueryTemplateCollection.prepare();
        QueryGenerator queryGenerator = new QueryGenerator(QueryTemplateCollection.templates.get(0));
        Query query = queryGenerator.next();
        List<Range> ranges = transformer.transform(query);
        System.out.println("length of ranges is: " + ranges.size());
        for (Range range : ranges) {
            System.out.printf("range for query is: %d, %f\n", range.attributeId, range.ratio);
        }
    }
    public static void main(String[] args) {
        new QueryToRange().test();
    }
}
