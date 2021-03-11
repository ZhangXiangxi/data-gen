package com.xiangxi.dagen.query;

import java.util.ArrayList;
import java.util.List;

public class Query {
    public int table;
    public int type;
    public List<Project> projects;
    public List<Predicate> predicates;

    public Query() {
        projects = new ArrayList<>();
        predicates = new ArrayList<>();
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<Predicate> predicates) {
        this.predicates = predicates;
    }


}
