package ua.algorithms;

import lombok.Getter;

import java.util.List;

@Getter
public class Block {

    private int size; // curr size of data in block
    private List<IndexRecord> records;
    public static final int BLOCK_BYTES = 1024; // size of block in bytes

}
