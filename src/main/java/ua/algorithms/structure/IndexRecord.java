package ua.algorithms.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class IndexRecord implements Comparable<IndexRecord> {

    private long pk; // primary key of datum record (id)
    private Pointer ptr; // pointer to global area
    public static final int PRIMARY_KEY_OFFSET = 0;
    public static final int PRIMARY_KEY_BYTES = Long.BYTES;
    public static final int POINTER_OFFSET = PRIMARY_KEY_OFFSET + PRIMARY_KEY_BYTES;
    public static final int POINTER_BYTES = Pointer.POINTER_BYTES;
    public static final int INDEX_RECORD_BYTES = PRIMARY_KEY_BYTES + POINTER_BYTES;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Pointer {
        private long blockIdx;
        private int recordIdx;
        public static final int BLOCK_INDEX_OFFSET = 0;
        public static final int BLOCK_INDEX_BYTES = Long.BYTES;
        public static final int RECORD_INDEX_OFFSET = BLOCK_INDEX_OFFSET + BLOCK_INDEX_BYTES;
        public static final int RECORD_INDEX_BYTES = Integer.BYTES;
        public static final int POINTER_BYTES = BLOCK_INDEX_BYTES + RECORD_INDEX_BYTES;

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Pointer{");
            sb.append("blockIdx=").append(blockIdx);
            sb.append(", recordIdx=").append(recordIdx);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public int compareTo(IndexRecord o) {
        return Long.compare(this.pk, o.pk);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndexRecord{");
        sb.append("pk=").append(pk);
        sb.append(", ptr=").append(ptr);
        sb.append('}');
        return sb.toString();
    }
}
