package ua.algorithms.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DataBlock {

    private int size;
    private List<DatumRecord> records;
    public static final int SIZE_OFFSET = 0;
    public static final int SIZE_BYTES = Integer.BYTES;
    public static final int RECORDS_BYTES = 1024;
    public static final int RECORDS_OFFSET = SIZE_OFFSET + SIZE_BYTES;
    public static final int BYTES = SIZE_BYTES + RECORDS_BYTES;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DataBlock{");
        sb.append("size=").append(size);
        sb.append(", records=").append(records);
        sb.append('}');
        return sb.toString();
    }
}
