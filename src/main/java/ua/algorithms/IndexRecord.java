package ua.algorithms;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IndexRecord {

    private final long pk; // primary key of datum record (id)
    private final long ptr; // pointer to global area
    public static final int INDEX_RECORD_BYTES = 2 * Long.BYTES;
}
