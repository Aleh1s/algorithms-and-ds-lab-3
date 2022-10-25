package ua.algorithms.accessor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class FileAccessor {
    protected final String fileName;
    protected final RandomAccessFile access;

    public FileAccessor(RandomAccessFile access, String fileName) {
        this.access = access;
        this.fileName = fileName;
    }

    public static FileAccessor of(String fileName, String accessor) {
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(fileName, "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File [%s] does not exist".formatted(fileName), e);
        }

        if (accessor.equals("GLOBAL"))
            return new GlobalFileAccessor(raf, fileName);
        else if (accessor.equals("INDEX"))
            return new IndexFileAccessor(raf, fileName);
        else
            throw new IllegalArgumentException("Bad file accessor");
    }

//    public long write(DatumRecord datumRecord) { // return position of new record
//        long sizeOfFile = getSizeOfFile();
//        movePtr(sizeOfFile);
//        write(DatumRecordSerializer.serialize(datumRecord));
//        return sizeOfFile / DATUM_RECORD_BYTES;
//    }
//
//    public DatumRecord readDatum(long offset) {
//        movePtr(offset);
//        return DatumRecordSerializer.deserialize(read(DATUM_RECORD_BYTES));
//    }

    protected byte[] read(int length) {
        byte[] bytes = new byte[length];
        try {
            access.read(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed while reading from file [%s]".formatted(fileName), e);
        }
        return bytes;
    }

    protected void write(byte[] bytes) {
        try {
            access.write(bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new RuntimeException("Failed while writing to file [%s]".formatted(fileName), e);
        }
    }

    protected void movePtr(long offset) {
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

    public void clearFile() {
        try {
            access.setLength(0);
        } catch (IOException e) {
            throw new RuntimeException("Can not set length of file [%s]".formatted(fileName), e);
        }
    }

    public boolean isEmpty() {
        return getSizeOfFile() == 0;
    }
}
