package ua.algorithms;

import ua.algorithms.accessor.FileAccessor;
import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.structure.DataBlock;
import ua.algorithms.structure.DatumRecord;
import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

import java.util.List;

public class Main {
    public static void main(String[] args) {
//        IndexFileAccessor indexFileAccessor = (IndexFileAccessor) FileAccessor.of("test.bin", "INDEX");
//        indexFileAccessor.clearFile();
//
//        IndexRecord i1 = new IndexRecord(1, new IndexRecord.Pointer(1, 1));
//        IndexRecord i2 = new IndexRecord(2, new IndexRecord.Pointer(2, 1));
//        IndexRecord i3 = new IndexRecord(3, new IndexRecord.Pointer(3, 1));
//        IndexRecord i4 = new IndexRecord(4, new IndexRecord.Pointer(4, 1));
//        IndexRecord i5 = new IndexRecord(5, new IndexRecord.Pointer(5, 1));
//
//        List<IndexRecord> i11 = List.of(i1, i2, i3, i4, i5);
//        IndexBlock indexBlock = new IndexBlock(i11.size(), i11);
//
//        indexFileAccessor.write(indexBlock, 0);
//        System.out.println(indexFileAccessor.readBlock(0));

//        GlobalFileAccessor globalFileAccessor = (GlobalFileAccessor) FileAccessor.of("test.bin", "GLOBAL");
//        globalFileAccessor.clearFile();
//
//        DatumRecord d1 = new DatumRecord(1, "Value1");
//        DatumRecord d2 = new DatumRecord(2, "Value2");
//        DatumRecord d3 = new DatumRecord(3, "Value3");
//        DatumRecord d4 = new DatumRecord(4, "Value4");
//        DatumRecord d5 = new DatumRecord(5, "Value5");
//
//        List<DatumRecord> d11 = List.of(d1, d2, d3, d4, d5);
//        DataBlock dataBlock = new DataBlock(d11.size(), d11);
//
//        globalFileAccessor.write(dataBlock, 0);
//        System.out.println(globalFileAccessor.readBlock(0));
    }
}
