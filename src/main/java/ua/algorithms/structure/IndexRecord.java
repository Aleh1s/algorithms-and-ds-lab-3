package ua.algorithms.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IndexRecord implements Comparable<IndexRecord> {

    private final long pk; // primary key of datum record (id)
    private final long ptr; // pointer to global area
    public static final int INDEX_RECORD_BYTES = 2 * Long.BYTES;

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
