package ua.algorithms.accessor;

import ua.algorithms.serializer.IndexBlockSerializer;
import ua.algorithms.structure.IndexBlock;

import java.io.IOException;
import java.io.RandomAccessFile;

import static ua.algorithms.structure.IndexBlock.BYTES;

public class IndexFileAccessor extends FileAccessor {
    public IndexFileAccessor(RandomAccessFile raf, String fileName) {
        super(raf, fileName);
    }

    public void write(IndexBlock indexBlock, int blockNumber) {
        movePtr((long) blockNumber * BYTES);
        write(IndexBlockSerializer.serialize(indexBlock));
    }

    public IndexBlock readBlock(int blockNumber) {
        movePtr((long) blockNumber * BYTES);
        return IndexBlockSerializer.deserialize(read(BYTES));
    }

    public int countNumberOfBlocks() {
        return (int) getSizeOfFile() / BYTES;
    }

    public void setLength(long bytes) {
        try {
            super.access.setLength(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Cannot set length [%s]".formatted(super.fileName));
        }
    }
}
