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
    public static final int RECORDS_BYTES = 1020;
    public static final int RECORDS_OFFSET = SIZE_OFFSET + SIZE_BYTES;
    public static final int BLOCK_BYTES = SIZE_BYTES + RECORDS_BYTES; // size of block in bytes

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Block{");
        sb.append("size=").append(size);
        sb.append(", records=").append(records);
        sb.append('}');
        return sb.toString();
    }
}
