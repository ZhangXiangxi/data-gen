package com.xiangxi.dagen.qgen;

import java.util.Arrays;
import java.util.List;

public class QueryTemplate {
    public int from;
    public int[] projectSet;
    public List<PredicateTemplate> predicateSet;
    public static class PredicateTemplate {
        public int attribute;
        public int lowest;
        public int highest;
        public boolean equal;

        public PredicateTemplate(int attribute, int lowest, int highest) {
            this.attribute = attribute;
            this.lowest = lowest;
            this.highest = highest;
            this.equal = false;
        }

        public PredicateTemplate(int attribute, int lowest, int highest, boolean equal) {
            this.attribute = attribute;
            this.lowest = lowest;
            this.highest = highest;
            this.equal = equal;
        }

        @Override
        public String toString() {
            return "PredicateTemplate{" +
                    "attribute=" + attribute +
                    ", lowest=" + lowest +
                    ", highest=" + highest +
                    ", equal=" + equal +
                    '}';
        }
    }

    public QueryTemplate(int from, int[] projectSet, List<PredicateTemplate> predicateSet) {
        this.from = from;
        this.projectSet = projectSet;
        this.predicateSet = predicateSet;
    }

    @Override
    public String toString() {
        return "QueryTemplate{" +
                "from=" + from +
                ", projectSet=" + Arrays.toString(projectSet) +
                ", predicateSet=" + predicateSet +
                '}';
    }
}
