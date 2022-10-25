package ua.algorithms;

import ua.algorithms.accessor.FileAccessor;
import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.repository.SimpleRepository;
import ua.algorithms.structure.DatumRecord;
import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

import java.util.*;
import java.util.stream.IntStream;

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

//        DatumRecord d1 = new DatumRecord(1, "Hello");
//        DatumRecord d2 = new DatumRecord(2, "How are you?");
//
//        FileAccessor fileAccessor = FileAccessor.of("test.bin");
//        fileAccessor.clearFile();
//        long w1 = fileAccessor.write(d1);
//        System.out.println(w1);
//        long w2 = fileAccessor.write(d2);
//        System.out.println(w2);
//
//        DatumRecord datumRecord = fileAccessor.readDatum(w1 * DatumRecord.DATUM_RECORD_BYTES);
//        System.out.println(datumRecord);
//        DatumRecord datumRecord1 = fileAccessor.readDatum(w2 * DatumRecord.DATUM_RECORD_BYTES);
//        System.out.println(datumRecord1);

        IndexFileAccessor indexFileAccessor =
                (IndexFileAccessor) FileAccessor.of("D:\\KPI\\Algorithms\\Labs\\lab-3\\src\\main\\resources\\index.bin", "INDEX");
        GlobalFileAccessor globalFileAccessor =
                (GlobalFileAccessor) FileAccessor.of("D:\\KPI\\Algorithms\\Labs\\lab-3\\src\\main\\resources\\global.bin", "GLOBAL");
        SimpleRepository simpleRepository = new SimpleRepository(
                indexFileAccessor,
                globalFileAccessor
        );

        indexFileAccessor.clearFile();
        globalFileAccessor.clearFile();

        List<Integer> list = new LinkedList<>();
        IntStream.range(1, 100_001).forEach(list::add);
        Collections.shuffle(list);
        Queue<Integer> ids = new LinkedList<>(list);

        for (int i = 0; i < 100_000; i++) {
            Integer id = ids.poll();
            if (id != null) {
                simpleRepository.addDatumRecord(
                        new DatumRecord(id, "value%d".formatted(id)));
            }
        }

        long numberOfBlocks = indexFileAccessor.countNumberOfBlock();

        for (int i = 0; i < numberOfBlocks; i++) {
            IndexBlock indexBlock = indexFileAccessor.readBlock((long) i * IndexBlock.BYTES);
            System.out.printf("BLOCK%d%n", i);
            printRecords(indexBlock.getRecords());
        }
    }

    public static void printRecords(List<IndexRecord> list) {
        for (IndexRecord i : list) {
            System.out.println(i);
        }
    }
}
