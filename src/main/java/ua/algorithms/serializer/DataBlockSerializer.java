package ua.algorithms.serializer;

import ua.algorithms.structure.DataBlock;
import ua.algorithms.structure.DatumRecord;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static ua.algorithms.structure.DataBlock.RECORDS_OFFSET;
import static ua.algorithms.structure.DatumRecord.DATUM_RECORD_BYTES;

public class DataBlockSerializer {

    public static byte[] serialize(DataBlock dataBlock) {
        ByteBuffer buffer = ByteBuffer.allocate(DataBlock.BLOCK_BYTES)
                .putInt(dataBlock.getSize());
        for (DatumRecord datumRecord : dataBlock.getRecords())
            buffer.put(DatumRecordSerializer.serialize(datumRecord));
        return buffer.array();
    }

    public static DataBlock deserialize(byte[] bytes) {
        int size = ByteBuffer.wrap(bytes).getInt();
        List<DatumRecord> records = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            byte[] datumRecordBytes = new byte[DATUM_RECORD_BYTES];
            ByteBuffer.wrap(bytes).get(RECORDS_OFFSET + i * DATUM_RECORD_BYTES, datumRecordBytes);
            records.add(DatumRecordSerializer.deserialize(datumRecordBytes));
        }
        return new DataBlock(size, records);
    }

}
