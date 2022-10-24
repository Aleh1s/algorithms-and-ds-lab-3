package ua.algorithms.serializer;

import ua.algorithms.structure.Block;
import ua.algorithms.structure.IndexRecord;

import java.nio.ByteBuffer;

import static ua.algorithms.structure.Block.BLOCK_BYTES;

public class BlockSerializer {

    public static byte[] serialize(Block block) {
        ByteBuffer buffer = ByteBuffer.allocate(BLOCK_BYTES);
        for (IndexRecord record : block.getRecords())
            buffer.put(IndexRecordSerializer.serialize(record));
        return buffer.array();
    }
}
