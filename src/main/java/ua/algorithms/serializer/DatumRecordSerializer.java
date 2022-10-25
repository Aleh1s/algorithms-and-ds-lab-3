package ua.algorithms.serializer;

import ua.algorithms.structure.DatumRecord;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DatumRecordSerializer {
    public static byte[] serialize(DatumRecord datumRecord) {
        return ByteBuffer.allocate(DatumRecord.BYTES)
                .putLong(datumRecord.getId())
                .put(datumRecord.getValue().getBytes())
                .array();
    }

    public static DatumRecord deserialize(byte[] bytes) {
        ByteBuffer wrap = ByteBuffer.wrap(bytes);
        long id = wrap.getLong(DatumRecord.ID_OFFSET);
        byte[] valueBytes = new byte[DatumRecord.VALUE_BYTES];
        wrap.get(DatumRecord.VALUE_OFFSET, valueBytes);
        String value = new String(valueBytes, StandardCharsets.UTF_8);
        return new DatumRecord(id, value.trim());
    }
}
