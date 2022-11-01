package ua.algorithms.serializer;

import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class IndexBlockSerializer {
    public static byte[] serialize(IndexBlock indexBlock) {
        ByteBuffer buffer = ByteBuffer.allocate(IndexBlock.BYTES)
                .putInt(indexBlock.getNumber())
                .putInt(indexBlock.getSize());
        for (IndexRecord record : indexBlock.getRecords())
            buffer.put(IndexRecordSerializer.serialize(record));
        return buffer.array();
    }

    public static IndexBlock deserialize(byte[] bytes) {
        ByteBuffer wrap = ByteBuffer.wrap(bytes);
        int number = wrap.getInt(IndexBlock.NUMBER_OFFSET);
        int size = wrap.getInt(IndexBlock.SIZE_OFFSET);
        List<IndexRecord> records = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            byte[] indexRecordBytes = new byte[IndexRecord.BYTES];
            wrap.get(IndexBlock.RECORDS_OFFSET + i * IndexRecord.BYTES, indexRecordBytes);
            records.add(IndexRecordSerializer.deserialize(indexRecordBytes));
        }
        return new IndexBlock(number, size, records);
    }
}