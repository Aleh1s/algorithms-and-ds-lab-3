package ua.algorithms.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DatumRecord {

    private long id;
    private String value;
    public static final int VALUE_BYTES = 255;
    public static final int DATUM_RECORD_BYTES = Long.BYTES + VALUE_BYTES;

    public void setValue(String value) {
        if (value.length() > 255)
            throw new IllegalArgumentException();

        this.value = value;
    }
}
