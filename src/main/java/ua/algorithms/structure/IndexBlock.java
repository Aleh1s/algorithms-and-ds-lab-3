package ua.algorithms.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class IndexBlock {

    public static class Result {
        private IndexRecord value;
        private int indicator;

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

    private int size; // curr size of data in block
    private List<IndexRecord> records;
    public static final int SIZE_OFFSET = 0;
    public static final int SIZE_BYTES = Integer.BYTES;
    public static final int RECORDS_OFFSET = SIZE_OFFSET + SIZE_BYTES;
    public static final int RECORDS_BYTES = 1024;
    public static final int BYTES = SIZE_BYTES + RECORDS_BYTES; // size of block in bytes

    public boolean addRecord(IndexRecord indexRecord) {
        if (records.isEmpty()) {
            records.add(indexRecord);
        } else {
            boolean added = false;
            for (int i = 0; i < records.size(); i++) {
                if (indexRecord.getPk() < records.get(i).getPk()) {
                    records.add(i, indexRecord);
                    added = true;
                    break;
                }
            }
            if (!added) records.add(indexRecord);
        }
        return ++size > RECORDS_BYTES / IndexRecord.BYTES;
    }

    public IndexRecord retrieveAndRemoveLast() {
        IndexRecord temp = records.get(records.size() - 1);
        records.remove(temp);
        size--;
        return temp;
    }

    public Result find(long id) {
        if (records.isEmpty())
            return Result.of(null, 0);

        IndexRecord first = records.get(0);
        if (records.size() == 1) {
            if (id == first.getPk())
                return Result.of(first, 0);

            if (id < first.getPk())
                return Result.of(null, -1);

            return Result.of(null, 1);
        }

        Optional<IndexRecord> find = records.stream()
                .filter(indexRecord -> indexRecord.getPk() == id)
                .findFirst();

        if (find.isPresent())
            return Result.of(find.get(), 0);

        if (id < first.getPk())
            return Result.of(null, -1);

        return Result.of(null, 1);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Block{");
        sb.append("size=").append(size);
        sb.append(", records=").append(records);
        sb.append('}');
        return sb.toString();
    }
}
