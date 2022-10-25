package ua.algorithms.accessor;

import ua.algorithms.serializer.DataBlockSerializer;
import ua.algorithms.structure.DataBlock;

import java.io.RandomAccessFile;

import static ua.algorithms.structure.DataBlock.BYTES;


public class GlobalFileAccessor extends FileAccessor {
    public GlobalFileAccessor(RandomAccessFile access, String fileName) {
        super(access, fileName);
    }

    public void write(DataBlock dataBlock, long offset) {
        movePtr(offset);
        write(DataBlockSerializer.serialize(dataBlock));
    }

    public DataBlock readBlock(long offset) {
        movePtr(offset);
        return DataBlockSerializer.deserialize(read(BYTES));
    }

    public boolean isEmpty() {
        return getSizeOfFile() == 0;
    }
}
