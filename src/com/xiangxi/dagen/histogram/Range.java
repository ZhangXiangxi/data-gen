package com.xiangxi.dagen.histogram;

public class Range {
    public int attributeId;
    public double ratio;

    public Range(int attributeId, double ratio) {
        this.attributeId = attributeId;
        this.ratio = ratio;
    }

    public Range() {
    }

    @Override
    public String toString() {
        return "Range{" +
                "attributeId=" + attributeId +
                ", ratio=" + ratio +
                '}';
    }
}
