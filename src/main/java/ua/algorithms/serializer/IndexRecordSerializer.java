package ua.algorithms.serializer;

import ua.algorithms.structure.IndexRecord;

import java.nio.ByteBuffer;

import static ua.algorithms.structure.IndexRecord.INDEX_RECORD_BYTES;

public class IndexRecordSerializer {
    public static byte[] serialize(IndexRecord record) {
        return ByteBuffer
                .allocate(INDEX_RECORD_BYTES)
                .putLong(record.getPk())
                .put(serializePointer(record.getPtr()))
                .array();
    }

    private static byte[] serializePointer(IndexRecord.Pointer pointer) {
        return ByteBuffer.allocate(IndexRecord.Pointer.POINTER_BYTES)
                .putLong(pointer.getBlockIdx())
                .putInt(pointer.getRecordIdx())
                .array();
    }

    private static IndexRecord.Pointer deserializePointer(byte[] bytes) {
        ByteBuffer wrap = ByteBuffer.wrap(bytes);
        long blockIdx = wrap.getLong(IndexRecord.Pointer.BLOCK_INDEX_OFFSET);
        int recordIdx = wrap.getInt(IndexRecord.Pointer.RECORD_INDEX_OFFSET);
        return new IndexRecord.Pointer(blockIdx, recordIdx);
    }

    public static IndexRecord deserialize(byte[] bytes) {
        long pk = ByteBuffer.wrap(bytes)
                .getLong(IndexRecord.PRIMARY_KEY_OFFSET);
        byte[] pointerBytes = new byte[IndexRecord.Pointer.POINTER_BYTES];
        ByteBuffer.wrap(bytes)
                .get(IndexRecord.POINTER_OFFSET, pointerBytes);
        return new IndexRecord(pk, deserializePointer(pointerBytes));
    }
}
