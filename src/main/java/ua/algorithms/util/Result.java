package ua.algorithms.util;

import ua.algorithms.structure.IndexRecord;

import java.util.Objects;

public class Result {
    private final IndexRecord value;
    private final int indicator;

    private Result(IndexRecord value, int indicator) {
        this.value = value;
        this.indicator = indicator;
    }

    public static Result of(IndexRecord value, int indicator) {
        return new Result(value, indicator);
    }

    public boolean hasResult() {
        return Objects.nonNull(value);
    }

    public IndexRecord getValue() {
        return value;
    }

    public int getIndicator() {
        return indicator;
    }
}