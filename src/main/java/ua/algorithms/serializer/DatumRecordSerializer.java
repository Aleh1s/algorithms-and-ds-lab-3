package ua.algorithms.serializer;

import ua.algorithms.structure.DatumRecord;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static ua.algorithms.structure.DatumRecord.DATUM_RECORD_BYTES;
import static ua.algorithms.structure.DatumRecord.VALUE_BYTES;

public class DatumRecordSerializer {
    public static byte[] serialize(DatumRecord datumRecord) {
        return ByteBuffer.allocate(DATUM_RECORD_BYTES)
                .putLong(datumRecord.getId())
                .put(datumRecord.getValue().getBytes())
                .array();
    }

    public static DatumRecord deserialize(byte[] bytes) {
        long id = ByteBuffer.wrap(bytes).getLong();
        byte[] valueBytes = new byte[VALUE_BYTES];
        ByteBuffer.wrap(bytes).get(Long.BYTES, valueBytes);
        String value = new String(valueBytes, StandardCharsets.UTF_8);
        return new DatumRecord(id, value.trim());
    }
}
