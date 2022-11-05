package ua.algorithms.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@Getter
@AllArgsConstructor
public class IndexBlock {

    private int index;
    private int size; // curr size of data in block
    private List<IndexRecord> records;
    public static final int NUMBER_OFFSET = 0;
    private static final int NUMBER_BYTES = Integer.BYTES;
    public static final int SIZE_OFFSET = NUMBER_OFFSET + NUMBER_BYTES;
    public static final int SIZE_BYTES = Integer.BYTES;
    public static final int RECORDS_OFFSET = SIZE_OFFSET + SIZE_BYTES;
    public static final int RECORDS_BYTES = 1024;
    public static final int BYTES = NUMBER_BYTES + SIZE_BYTES + RECORDS_BYTES; // size of block in bytes

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

    public Optional<IndexRecord> findById(int id) {
        int i = find(id);

        if (i >= 0)
            return Optional.of(records.get(i));

        return Optional.empty();
    }

    public int delete(int id) {
        int i = find(id);

        if (i >= 0) {
            records.remove(i);
            size--;
            return 1;
        }

        return 0;
    }

    private int find(int id) {
        return Collections.binarySearch(
                records, new IndexRecord(id, 0), Comparator.comparing(IndexRecord::getPk));
    }

//    public int calculateIndicator(int id) {
//        IndexRecord first = records.get(0);
//        if (records.size() == 1) {
//            if (id == first.getPk())
//                return 0;
//
//            if (id < first.getPk())
//                return -1;
//
//            return 1;
//        }
//
//        IndexRecord last = records.get(records.size() - 1);
//        if (id >= first.getPk() && id <= last.getPk())
//            return 0;
//
//        if (id < first.getPk())
//            return -1;
//
//        return 1;
//    }

    public IndexBlock separate() {
        List<IndexRecord> partOfRecords = new ArrayList<>();
        int i = records.size() / 2;
        for (int j = 0; j < i + 1; j++) {
            partOfRecords.add(records.get(i));
            records.remove(i);
            this.size--;
        }

        return new IndexBlock(index + 1, partOfRecords.size(), partOfRecords);
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }

    public void setIndex(int index) {
        this.index = index;
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
