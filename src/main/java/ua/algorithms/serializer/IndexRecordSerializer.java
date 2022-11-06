package ua.algorithms.serializer;

import ua.algorithms.structure.IndexRecord;

import java.nio.ByteBuffer;

public class IndexRecordSerializer {
    public static byte[] serialize(IndexRecord record) {
        return ByteBuffer
                .allocate(IndexRecord.BYTES)
                .putLong(record.pk())
                .putLong(record.ptr())
                .array();
    }

    public static IndexRecord deserialize(byte[] bytes) {
        ByteBuffer wrap = ByteBuffer.wrap(bytes);
        long pk = wrap.getLong(IndexRecord.PRIMARY_KEY_OFFSET);
        long ptr = wrap.getLong(IndexRecord.POINTER_OFFSET);
        return new IndexRecord(pk, ptr);
    }
}
