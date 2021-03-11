package com.xiangxi.dagen.histogram;

import java.util.ArrayList;
import java.util.List;

public class Histogram {
    public List<EqualHistogram> equalGrams;
    public List<RangeHistogram> rangeGrams;
    public List<TrivialHistogram> trivialGrams;

    @Override
    public String toString() {
        return "Histogram{" +
                "equalGrams=" + equalGrams +
                ", rangeGrams=" + rangeGrams +
                ", trivialGrams=" + trivialGrams +
                '}';
    }

    public static class TrivialHistogram {
        public int attributeId;
        public int total;

        @Override
        public String toString() {
            return "TrivialHistogram{" +
                    "attributeId=" + attributeId +
                    ", total=" + total +
                    '}';
        }

        public TrivialHistogram(int attributeId, int total) {
            this.attributeId = attributeId;
            this.total = total;
        }

        public TrivialHistogram() {
        }
    }

    public static class RangeHistogram {
        public int attributeId;
        public int total;
        public List<RangeEntry> entries;

        @Override
        public String toString() {
            return "RangeHistogram{" +
                    "attributeId=" + attributeId +
                    ", total=" + total +
                    ", entries=" + entries +
                    '}';
        }

        public static class RangeEntry {
            public int lowest;
            public int highest;
            public int count;

            @Override
            public String toString() {
                return "RangeEntry{" +
                        "lowest=" + lowest +
                        ", highest=" + highest +
                        ", count=" + count +
                        '}';
            }

            public RangeEntry() {
            }

            public RangeEntry(int lowest, int highest, int count) {
                this.lowest = lowest;
                this.highest = highest;
                this.count = count;
            }
        }
    }
    public static class EqualHistogram {
        public int attributeId;
        public List<EqualEntry> entries;
        public int total;

        @Override
        public String toString() {
            return "EqualHistogram{" +
                    "attributeId=" + attributeId +
                    ", entries=" + entries +
                    ", total=" + total +
                    '}';
        }

        public static class EqualEntry {
            public int key;
            public int value;

            @Override
            public String toString() {
                return "EqualEntry{" +
                        "key=" + key +
                        ", value=" + value +
                        '}';
            }

            public EqualEntry() {
            }

            public EqualEntry(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }
    }
}
