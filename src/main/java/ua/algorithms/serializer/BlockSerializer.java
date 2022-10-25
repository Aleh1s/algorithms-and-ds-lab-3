package ua.algorithms.serializer;

import ua.algorithms.structure.Block;
import ua.algorithms.structure.IndexRecord;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static ua.algorithms.structure.Block.BLOCK_BYTES;
import static ua.algorithms.structure.Block.RECORDS_OFFSET;
import static ua.algorithms.structure.IndexRecord.INDEX_RECORD_BYTES;

public class BlockSerializer {
    public static byte[] serialize(Block block) {
        ByteBuffer buffer = ByteBuffer.allocate(BLOCK_BYTES)
                .putInt(block.getSize());
        for (IndexRecord record : block.getRecords())
            buffer.put(IndexRecordSerializer.serialize(record));
        return buffer.array();
    }

    public static Block deserialize(byte[] bytes) {
        int size = ByteBuffer.wrap(bytes).getInt();
        List<IndexRecord> records = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            byte[] indexRecordBytes = new byte[INDEX_RECORD_BYTES];
            ByteBuffer.wrap(bytes).get(RECORDS_OFFSET + i * INDEX_RECORD_BYTES, indexRecordBytes);
            records.add(IndexRecordSerializer.deserialize(indexRecordBytes));
        }
        return new Block(size, records);
    }
}