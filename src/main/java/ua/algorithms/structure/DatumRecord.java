package ua.algorithms.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DatumRecord {

    private long id;
    private String value;
    public static final int VALUE_LENGTH = 60;
    public static final int VALUE_BYTES = VALUE_LENGTH * 2;
    public static final int DATUM_RECORD_BYTES = Long.BYTES + VALUE_BYTES;

    public void setValue(String value) {
        if (value.length() > VALUE_LENGTH)
            throw new IllegalArgumentException();

        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DatumRecord{");
        sb.append("id=").append(id);
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
