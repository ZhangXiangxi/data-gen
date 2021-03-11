package com.xiangxi.dagen.qgen;

import java.util.ArrayList;
import java.util.List;

public class QueryTemplateBuilder {
    private int from;
    private int[] projectSet;
    private List<QueryTemplate.PredicateTemplate> predicateSet;

    public QueryTemplateBuilder setFrom(int from) {
        this.from = from;
        return this;
    }

    public QueryTemplateBuilder setProjectSet(int[] projectSet) {
        this.projectSet = projectSet;
        return this;
    }
    public QueryTemplateBuilder setProjectSet(int start, int end) {
        int[] projectSet = new int[end - start + 1];
        for (int i = start; i <= end; i++) {
            projectSet[i - start] = i;
        }
        return setProjectSet(projectSet);
    }

    public QueryTemplateBuilder setPredicateSet(List<QueryTemplate.PredicateTemplate> predicateSet) {
        this.predicateSet = predicateSet;
        return this;
    }

    public QueryTemplateBuilder addPredicate(QueryTemplate.PredicateTemplate template) {
        if (this.predicateSet == null) {
            this.predicateSet = new ArrayList<>();
        }
        this.predicateSet.add(template);
        return this;
    }

    public QueryTemplateBuilder addPredicate(int attribute, int lowest, int highest) {
        return addPredicate(new QueryTemplate.PredicateTemplate(attribute, lowest, highest));
    }

    public QueryTemplateBuilder addPredicate(int attribute, int lowest, int highest, boolean equal) {
        return addPredicate(new QueryTemplate.PredicateTemplate(attribute, lowest, highest, equal));
    }

    public QueryTemplate createQueryTemplate() {
        return new QueryTemplate(from, projectSet, predicateSet);
    }
}