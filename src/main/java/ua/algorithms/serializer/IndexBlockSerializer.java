package ua.algorithms.serializer;

import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

import java.nio.ByteBuffer;
import java.util.TreeMap;

public class IndexBlockSerializer {
    public static byte[] serialize(IndexBlock indexBlock) {
        ByteBuffer buffer = ByteBuffer.allocate(IndexBlock.BYTES)
                .putInt(indexBlock.getIndex())
                .putInt(indexBlock.getSize());
        for (IndexRecord record : indexBlock.getRecords())
            buffer.put(IndexRecordSerializer.serialize(record));
        return buffer.array();
    }

    public static IndexBlock deserialize(byte[] bytes) {
        ByteBuffer wrap = ByteBuffer.wrap(bytes);
        int index = wrap.getInt(IndexBlock.INDEX_OFFSET);
        int size = wrap.getInt(IndexBlock.SIZE_OFFSET);
        TreeMap<Long, IndexRecord> records = new TreeMap<>();
        for (int i = 0; i < size; i++) {
            byte[] indexRecordBytes = new byte[IndexRecord.BYTES];
            wrap.get(IndexBlock.RECORDS_OFFSET + i * IndexRecord.BYTES, indexRecordBytes);
            IndexRecord indexRecord = IndexRecordSerializer.deserialize(indexRecordBytes);
            records.put(indexRecord.pk(), indexRecord);
        }
        return new IndexBlock(size, index, records);
    }
}