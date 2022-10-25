package ua.algorithms.serializer;

import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static ua.algorithms.structure.IndexBlock.BLOCK_BYTES;
import static ua.algorithms.structure.IndexBlock.RECORDS_OFFSET;
import static ua.algorithms.structure.IndexRecord.INDEX_RECORD_BYTES;

public class IndexBlockSerializer {
    public static byte[] serialize(IndexBlock indexBlock) {
        ByteBuffer buffer = ByteBuffer.allocate(BLOCK_BYTES)
                .putInt(indexBlock.getSize());
        for (IndexRecord record : indexBlock.getRecords())
            buffer.put(IndexRecordSerializer.serialize(record));
        return buffer.array();
    }

    public static IndexBlock deserialize(byte[] bytes) {
        int size = ByteBuffer.wrap(bytes).getInt();
        List<IndexRecord> records = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            byte[] indexRecordBytes = new byte[INDEX_RECORD_BYTES];
            ByteBuffer.wrap(bytes).get(RECORDS_OFFSET + i * INDEX_RECORD_BYTES, indexRecordBytes);
            records.add(IndexRecordSerializer.deserialize(indexRecordBytes));
        }
        return new IndexBlock(size, records);
    }
}