package ua.algorithms.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@Getter
@AllArgsConstructor
public class IndexBlock {

    private int size;
    private int index;
    private Map<Long, IndexRecord> records;
    public static final int INDEX_OFFSET = 0;
    private static final int INDEX_BYTES = Integer.BYTES;
    public static final int SIZE_OFFSET = INDEX_OFFSET + INDEX_BYTES;
    public static final int SIZE_BYTES = Integer.BYTES;
    public static final int RECORDS_OFFSET = SIZE_OFFSET + SIZE_BYTES;
    public static final int RECORDS_BYTES = 1024;
    public static final int BYTES = INDEX_BYTES + SIZE_BYTES + RECORDS_BYTES; // size of block in bytes

    public boolean addRecord(IndexRecord indexRecord) {
        records.put(indexRecord.pk(), indexRecord);
        return ++size > RECORDS_BYTES / IndexRecord.BYTES;
    }

    public Optional<IndexRecord> findById(long id) {
        return Optional.ofNullable(records.get(id));
    }

    public int delete(long id) {
        IndexRecord i = records.remove(id);

        if (Objects.nonNull(i)) {
            size--;
            return 1;
        }

        return 0;
    }

    public int calculateIndicator(long id) {
        List<IndexRecord> records = getRecords();
        IndexRecord first = records.get(0);
        if (records.size() == 1) {
            return Long.compare(id, first.pk());
        }

        IndexRecord last = records.get(records.size() - 1);
        if (id >= first.pk() && id <= last.pk())
            return 0;

        if (id < first.pk())
            return -1;

        return 1;
    }

    public IndexBlock separate() {
        TreeMap<Long, IndexRecord> partOfRecords = new TreeMap<>();
        List<IndexRecord> values = valueOf(records.values());

        int length = values.size() / 2;

        records.clear();

        for (int i = 0; i < length; i++) {
            IndexRecord indexRecord = values.get(i);
            records.put(indexRecord.pk(), indexRecord);
        }

        for (int i = length; i < values.size(); i++) {
            IndexRecord indexRecord = values.get(i);
            partOfRecords.put(indexRecord.pk(), indexRecord);
        }

        size -= partOfRecords.size();

        return new IndexBlock(partOfRecords.size(), index + 1, partOfRecords);
    }

    private List<IndexRecord> valueOf(Collection<IndexRecord> values) {
        return new ArrayList<>(values);
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<IndexRecord> getRecords() {
        return valueOf(records.values());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndexBlock{");
        sb.append("size=").append(size);
        sb.append(", index=").append(index);
        List<IndexRecord> recordList = new ArrayList<>(records.values());
        sb.append(", fromTo=")
                .append("[").append(recordList.get(0).pk())
                .append("; ")
                .append(recordList.get(recordList.size() - 1).pk())
                .append("]");
        sb.append('}');
        return sb.toString();
    }
}
