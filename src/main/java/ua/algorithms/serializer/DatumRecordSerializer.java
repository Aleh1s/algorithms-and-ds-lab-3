package ua.algorithms.serializer;

import ua.algorithms.structure.DatumRecord;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DatumRecordSerializer {
    public static byte[] serialize(DatumRecord datumRecord) {
        return ByteBuffer.allocate(DatumRecord.BYTES)
                .putLong(DatumRecord.ID_OFFSET, datumRecord.getId())
                .put(DatumRecord.FIRST_NAME_OFFSET, datumRecord.getFirstName().getBytes())
                .put(DatumRecord.LAST_NAME_OFFSET, datumRecord.getLastName().getBytes())
                .put(DatumRecord.EMAIL_OFFSET, datumRecord.getEmail().getBytes())
                .array();
    }

    public static DatumRecord deserialize(byte[] bytes) {
        ByteBuffer wrap = ByteBuffer.wrap(bytes);
        long id = wrap.getLong(DatumRecord.ID_OFFSET);

        byte[] firstNameBytes = new byte[DatumRecord.FIRST_NAME_BYTES];
        wrap.get(DatumRecord.FIRST_NAME_OFFSET, firstNameBytes);
        String firstName = new String(firstNameBytes, StandardCharsets.UTF_8);

        byte[] lastNameBytes = new byte[DatumRecord.LAST_NAME_BYTES];
        wrap.get(DatumRecord.LAST_NAME_OFFSET, lastNameBytes);
        String lastName = new String(lastNameBytes, StandardCharsets.UTF_8);

        byte[] emailBytes = new byte[DatumRecord.EMAIL_BYTES];
        wrap.get(DatumRecord.EMAIL_OFFSET, emailBytes);
        String email = new String(emailBytes, StandardCharsets.UTF_8);

        return new DatumRecord(id, firstName.trim(), lastName.trim(), email.trim());
    }
}
