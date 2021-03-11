package com.xiangxi.dagen.maria;

import com.xiangxi.dagen.histogram.QueryToRange;
import com.xiangxi.dagen.histogram.Range;

import java.util.List;

public class Example {
    public SqlExecutor.Label label;
    public String sql;
    public List<Range> ranges;

    @Override
    public String toString() {
        return "Example{" +
                "label=" + label +
                ", sql='" + sql + '\'' +
                ", ranges=" + ranges +
                '}';
    }

    public Example(SqlExecutor.Label label, String sql, List<Range> ranges) {
        this.label = label;
        this.sql = sql;
        this.ranges = ranges;
    }

    public Example() {
    }
}
