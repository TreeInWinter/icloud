/**
 * CFieldComparator.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 *
 * created by jay 2010-5-13
 */
package org.apache.lucene.search;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;

public abstract class CFieldComparator {

    public abstract void setNextReader(IndexReader reader) throws IOException;
    
    public abstract Comparable value(int slot);

    public static final class DoubleComparator extends CFieldComparator {
        private double[] currentReaderValues;

        private final String field;

        DoubleComparator(String field) {
            this.field = field;
        }

        @Override
        public void setNextReader(IndexReader reader) throws IOException {
            currentReaderValues = FieldCache.DEFAULT.getDoubles(reader, field,
                    FieldCache.DEFAULT_DOUBLE_PARSER);
        }

        @Override
        public Comparable value(int slot) {
            return currentReaderValues[slot];
        }
    }

    public static final class LongComparator extends CFieldComparator {
        private long[] currentReaderValues;

        private final String field;

        LongComparator(String field) {
            this.field = field;
        }

        @Override
        public void setNextReader(IndexReader reader) throws IOException {
            currentReaderValues = FieldCache.DEFAULT.getLongs(reader, field,
                    FieldCache.DEFAULT_LONG_PARSER);
        }
        
        @Override
        public Comparable value(int slot) {
            return currentReaderValues[slot];
        }
    }

    public static final class FloatComparator extends CFieldComparator {
        private float[] currentReaderValues;

        private final String field;

        FloatComparator(String field) {
            this.field = field;
        }

        @Override
        public void setNextReader(IndexReader reader) throws IOException {
            currentReaderValues = FieldCache.DEFAULT.getFloats(reader, field,
                    FieldCache.DEFAULT_FLOAT_PARSER);
        }
        
        @Override
        public Comparable value(int slot) {
            return currentReaderValues[slot];
        }
    }

    public static final class IntComparator extends CFieldComparator {
        private int[] currentReaderValues;

        private final String field;

        IntComparator(String field) {
            this.field = field;
        }

        @Override
        public void setNextReader(IndexReader reader) throws IOException {
            currentReaderValues = FieldCache.DEFAULT.getInts(reader, field,
                    FieldCache.DEFAULT_INT_PARSER);
        }
        
        @Override
        public Comparable value(int slot) {
            return currentReaderValues[slot];
        }
    }
    
    public static final class StringComparator extends CFieldComparator {
        private String[] currentReaderValues;

        private final String field;

        StringComparator(String field) {
            this.field = field;
        }

        @Override
        public void setNextReader(IndexReader reader) throws IOException {
            currentReaderValues = FieldCache.DEFAULT.getStrings(reader, field);
        }
        
        @Override
        public Comparable value(int slot) {
            return currentReaderValues[slot];
        }
    }
}
