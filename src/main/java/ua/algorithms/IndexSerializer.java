package ua.algorithms;

import java.nio.ByteBuffer;

import static ua.algorithms.IndexRecord.INDEX_RECORD_BYTES;

public class IndexSerializer {
    public static byte[] serialize(IndexRecord record) {
        return ByteBuffer
                .allocate(INDEX_RECORD_BYTES)
                .putLong(record.getPk())
                .putLong(record.getPtr())
                .array();
    }
}
