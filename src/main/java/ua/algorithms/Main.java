package ua.algorithms;

import ua.algorithms.accessor.FileAccessor;
import ua.algorithms.structure.DatumRecord;

public class Main {
    public static void main(String[] args) {
//        IndexRecord i1 = new IndexRecord(1,2);
//        IndexRecord i2 = new IndexRecord(2,2);
//        IndexRecord i3 = new IndexRecord(3,2);
//
//        List<IndexRecord> indexes = List.of(i1, i2, i3);
//
//        Block block = new Block(3, indexes);
//
//        FileAccessor fileAccessor = FileAccessor.of("test.bin");
//        fileAccessor.write(block, 0);
//        Block read = fileAccessor.read(0);
//
//        System.out.println(read);

        DatumRecord d1 = new DatumRecord(1, "Hello");
        DatumRecord d2 = new DatumRecord(2, "How are you?");

        FileAccessor fileAccessor = FileAccessor.of("test.bin");
        fileAccessor.clearFile();
        long w1 = fileAccessor.write(d1);
        System.out.println(w1);
        long w2 = fileAccessor.write(d2);
        System.out.println(w2);

        DatumRecord datumRecord = fileAccessor.readDatum(w1 * DatumRecord.DATUM_RECORD_BYTES);
        System.out.println(datumRecord);
        DatumRecord datumRecord1 = fileAccessor.readDatum(w2 * DatumRecord.DATUM_RECORD_BYTES);
        System.out.println(datumRecord1);
    }
}
