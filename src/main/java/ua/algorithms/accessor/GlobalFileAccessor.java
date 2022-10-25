package ua.algorithms.accessor;

import ua.algorithms.serializer.DatumRecordSerializer;
import ua.algorithms.structure.DatumRecord;

import java.io.RandomAccessFile;

public class GlobalFileAccessor extends FileAccessor {
    public GlobalFileAccessor(RandomAccessFile access, String fileName) {
        super(access, fileName);
    }

    public long write(DatumRecord datumRecord) {
        long offset = getSizeOfFile();
        movePtr(offset);
        write(DatumRecordSerializer.serialize(datumRecord));
        return offset;
    }

    public void update(DatumRecord datumRecord, long offset) {
        movePtr(offset);
        write(DatumRecordSerializer.serialize(datumRecord));
    }
    public DatumRecord read(long offset) {
        movePtr(offset);
        return DatumRecordSerializer.deserialize(read(DatumRecord.BYTES));
    }

    public boolean isEmpty() {
        return getSizeOfFile() == 0;
    }
}
