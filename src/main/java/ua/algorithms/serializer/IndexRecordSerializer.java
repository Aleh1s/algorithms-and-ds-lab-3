package ua.algorithms.serializer;

import ua.algorithms.structure.IndexRecord;

import java.nio.ByteBuffer;

import static ua.algorithms.structure.IndexRecord.INDEX_RECORD_BYTES;

public class IndexRecordSerializer {
    public static byte[] serialize(IndexRecord record) {
        return ByteBuffer
                .allocate(INDEX_RECORD_BYTES)
                .putLong(record.getPk())
                .putLong(record.getPtr())
                .array();
    }

    public static IndexRecord deserialize(byte[] bytes) {
        long pk = ByteBuffer.wrap(bytes).getLong(0);
        long ptr = ByteBuffer.wrap(bytes).getLong(8);
        return new IndexRecord(pk, ptr);
    }
}
