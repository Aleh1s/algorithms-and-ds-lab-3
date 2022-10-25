package ua.algorithms.repository;

import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.structure.DataBlock;
import ua.algorithms.structure.DatumRecord;
import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

import java.util.ArrayList;
import java.util.List;

public class SimpleRepository {

    private final IndexFileAccessor indexArea;
    private final GlobalFileAccessor globalArea;

    public SimpleRepository(IndexFileAccessor indexArea, GlobalFileAccessor globalArea) {
        this.indexArea = indexArea;
        this.globalArea = globalArea;
    }

//    public void addDatumRecord(DatumRecord datumRecord) {
//        if (globalArea.isEmpty()) {
//            DataBlock dataBlock = new DataBlock(1, new ArrayList<>(List.of(datumRecord)));
//            IndexRecord indexRecord = new IndexRecord(datumRecord.getId(), 0);
//            IndexBlock indexBlock = new IndexBlock(1, new ArrayList<>(List.of()));
//        } else {
//
//        }
//    }
}
