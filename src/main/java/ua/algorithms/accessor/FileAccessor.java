package ua.algorithms.accessor;

import ua.algorithms.serializer.BlockSerializer;
import ua.algorithms.structure.Block;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static ua.algorithms.structure.Block.BLOCK_BYTES;

public class FileAccessor {
    private String fileName;
    private final RandomAccessFile access;

    public FileAccessor(RandomAccessFile access, String fileName) {
        this.access = access;
        this.fileName = fileName;
    }

    public static FileAccessor of(String fileName) {
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(fileName, "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File [%s] does not exist".formatted(fileName), e);
        }
        return new FileAccessor(raf, fileName);
    }

    public void writeBlock(Block block, long offset) {
        movePtr(offset);
        write(BlockSerializer.serialize(block));
    }

    public Block readBlock(long offset) {
        movePtr(offset);
        return BlockSerializer.deserialize(read(BLOCK_BYTES));
    }

    private byte[] read(int length) {
        byte[] bytes = new byte[length];
        try {
            access.read(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed while reading from file [%s]".formatted(fileName), e);
        }
        return bytes;
    }

    private void write(byte[] bytes) {
        try {
            access.write(bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new RuntimeException("Failed while writing to file [%s]".formatted(fileName), e);
        }
    }

    private void movePtr(long offset) {
        try {
            access.seek(offset);
        } catch (IOException e) {
            throw new RuntimeException("Failed while moving pointer in file [%s]".formatted(fileName), e);
        }
    }

    public long getSizeOfFile() {
        try {
            return access.length();
        } catch (IOException e) {
            throw new RuntimeException("Can not get length of file %s".formatted(fileName), e);
        }
    }

}
