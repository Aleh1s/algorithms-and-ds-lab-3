package ua.algorithms.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DatumRecord implements Comparable<DatumRecord> {

    private long id;
    private String value;
    public static final int ID_OFFSET = 0;
    public static final int ID_BYTES = Long.BYTES;
    public static final int VALUE_OFFSET = ID_OFFSET + ID_BYTES;
    public static final int VALUE_LENGTH = 60;
    public static final int VALUE_BYTES = VALUE_LENGTH * 2;
    public static final int BYTES = ID_BYTES + VALUE_BYTES;

    public void setValue(String value) {
        if (value.length() > VALUE_LENGTH)
            throw new IllegalArgumentException();

        this.value = value;
    }

    @Override
    public String toString() {

        return """
                {
                    id: %d,
                    value: %s
                }
                """.formatted(id, value);
    }

    @Override
    public int compareTo(DatumRecord o) {
        return Long.compare(this.id, o.id);
    }
}
