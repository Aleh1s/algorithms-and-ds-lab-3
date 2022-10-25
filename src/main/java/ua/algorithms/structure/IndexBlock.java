package ua.algorithms.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class IndexBlock {

    private int size; // curr size of data in block
    private List<IndexRecord> records;
    public static final int SIZE_OFFSET = 0;
    public static final int SIZE_BYTES = Integer.BYTES;
    public static final int RECORDS_BYTES = 1024;
    public static final int RECORDS_OFFSET = SIZE_OFFSET + SIZE_BYTES;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Block{");
        sb.append("size=").append(size);
        sb.append(", records=").append(records);
        sb.append('}');
        return sb.toString();
    }
}
