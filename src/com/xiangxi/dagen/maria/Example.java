package com.xiangxi.dagen.maria;

import com.xiangxi.dagen.histogram.QueryToRange;
import com.xiangxi.dagen.histogram.Range;

import java.util.List;

public class Example {
    public SqlExecutor.Label label;
    public String sql;
    public List<Range> ranges;
    public int from;

    @Override
    public String toString() {
        return "Example{" +
                "label=" + label +
                ", sql='" + sql + '\'' +
                ", ranges=" + ranges +
                ", from=" + from +
                '}';
    }

    public Example(SqlExecutor.Label label, String sql, List<Range> ranges, int from) {
        this.label = label;
        this.sql = sql;
        this.ranges = ranges;
        this.from = from;
    }

    public Example() {
    }
}
