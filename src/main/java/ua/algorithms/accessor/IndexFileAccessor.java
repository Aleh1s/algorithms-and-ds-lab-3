package ua.algorithms.accessor;

import ua.algorithms.serializer.IndexBlockSerializer;
import ua.algorithms.structure.IndexBlock;

import java.io.RandomAccessFile;

import static ua.algorithms.structure.IndexBlock.BYTES;

public class IndexFileAccessor extends FileAccessor {
    public IndexFileAccessor(RandomAccessFile raf, String fileName) {
        super(raf, fileName);
    }

    public void write(IndexBlock indexBlock, long offset) {
        movePtr(offset);
        write(IndexBlockSerializer.serialize(indexBlock));
    }

    public IndexBlock readBlock(long offset) {
        movePtr(offset);
        return IndexBlockSerializer.deserialize(read(BYTES));
    }

    public long countNumberOfBlocks() {
        return getSizeOfFile() / BYTES;
    }
}
