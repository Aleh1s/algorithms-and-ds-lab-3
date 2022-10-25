package ua.algorithms.serializer;

import ua.algorithms.structure.DataBlock;
import ua.algorithms.structure.DatumRecord;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DataBlockSerializer {

    public static byte[] serialize(DataBlock dataBlock) {
        ByteBuffer buffer = ByteBuffer.allocate(DataBlock.BYTES)
                .putInt(dataBlock.getSize());
        for (DatumRecord datumRecord : dataBlock.getRecords())
            buffer.put(DatumRecordSerializer.serialize(datumRecord));
        return buffer.array();
    }

    public static DataBlock deserialize(byte[] bytes) {
        int size = ByteBuffer.wrap(bytes)
                .getInt(DataBlock.SIZE_OFFSET);
        List<DatumRecord> records = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            byte[] datumRecordBytes = new byte[DatumRecord.BYTES];
            ByteBuffer.wrap(bytes).get(DataBlock.RECORDS_OFFSET + i * DatumRecord.BYTES, datumRecordBytes);
            records.add(DatumRecordSerializer.deserialize(datumRecordBytes));
        }
        return new DataBlock(size, records);
    }

}
