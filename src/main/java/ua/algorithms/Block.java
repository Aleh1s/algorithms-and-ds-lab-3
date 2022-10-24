package ua.algorithms;

import java.util.List;

public class Block {

    private List<IndexRecord> records;
    private static final int BLOCK_BYTES = 1024; // size of block in bytes
    private static final int RECORD_BYTES = 2 * Long.BYTES; // size of record (key + ptr)

}
