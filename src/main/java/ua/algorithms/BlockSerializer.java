package ua.algorithms;

import java.nio.ByteBuffer;

import static ua.algorithms.Block.BLOCK_BYTES;

public class BlockSerializer {

    public static byte[] serialize(Block block) {
        ByteBuffer buffer = ByteBuffer.allocate(BLOCK_BYTES);
        for (IndexRecord record : block.getRecords())
            buffer.put(IndexSerializer.serialize(record));
        return buffer.array();
    }
}
