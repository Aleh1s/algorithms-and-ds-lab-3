package ua.algorithms;

import ua.algorithms.accessor.FileAccessor;
import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.repository.SimpleRepository;
import ua.algorithms.structure.DatumRecord;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        IndexFileAccessor indexFileAccessor =
                (IndexFileAccessor) FileAccessor.of("src/main/resources/index.bin", "INDEX");
        GlobalFileAccessor globalFileAccessor =
                (GlobalFileAccessor) FileAccessor.of("src/main/resources/global.bin", "GLOBAL");

        SimpleRepository simpleRepository = new SimpleRepository(
                indexFileAccessor,
                globalFileAccessor
        );

        DatumRecord datumRecord = simpleRepository.find(100);
        System.out.println(datumRecord);
    }
}
