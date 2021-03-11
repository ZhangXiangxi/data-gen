package com.xiangxi.dagen.query;

public  class Predicate {
    public int attri;
    public int operator;
    public int oprand;

    public Predicate() {
    }

    public Predicate(int attri, int operator, int oprand) {
        this.attri = attri;
        this.operator = operator;
        this.oprand = oprand;
    }

    public int getAttri() {
        return attri;
    }

    public void setAttri(int attri) {
        this.attri = attri;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public int getOprand() {
        return oprand;
    }

    public void setOprand(int oprand) {
        this.oprand = oprand;
    }
}
