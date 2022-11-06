package ua.algorithms.structure;

public record IndexRecord(long pk, long ptr) implements Comparable<IndexRecord> {

    public static final int PRIMARY_KEY_OFFSET = 0;
    public static final int PRIMARY_KEY_BYTES = Long.BYTES;
    public static final int POINTER_OFFSET = PRIMARY_KEY_OFFSET + PRIMARY_KEY_BYTES;
    public static final int POINTER_BYTES = Long.BYTES;
    public static final int BYTES = PRIMARY_KEY_BYTES + POINTER_BYTES;

    @Override
    public int compareTo(IndexRecord o) {
        return Long.compare(this.pk, o.pk);
    }
}
