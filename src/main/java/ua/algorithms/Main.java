package ua.algorithms;

import ua.algorithms.accessor.FileAccessor;
import ua.algorithms.structure.Block;
import ua.algorithms.structure.IndexRecord;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        IndexRecord i1 = new IndexRecord(1,2);
        IndexRecord i2 = new IndexRecord(2,2);
        IndexRecord i3 = new IndexRecord(3,2);

        List<IndexRecord> indexes = List.of(i1, i2, i3);

        Block block = new Block(3, indexes);

        FileAccessor fileAccessor = FileAccessor.of("test.bin");
        fileAccessor.writeBlock(block, 0);
        Block read = fileAccessor.readBlock(0);

        System.out.println(read);
    }
}
