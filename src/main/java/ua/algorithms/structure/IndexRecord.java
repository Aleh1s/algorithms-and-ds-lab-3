package ua.algorithms.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class IndexRecord implements Comparable<IndexRecord> {

    private final long pk; // primary key of datum record (id)
    private final long ptr; // pointer to global area
    public static final int PRIMARY_KEY_OFFSET = 0;
    public static final int PRIMARY_KEY_BYTES = Long.BYTES;
    public static final int POINTER_OFFSET = PRIMARY_KEY_OFFSET + PRIMARY_KEY_BYTES;
    public static final int POINTER_BYTES = Long.BYTES;
    public static final int BYTES = PRIMARY_KEY_BYTES + POINTER_BYTES;

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
