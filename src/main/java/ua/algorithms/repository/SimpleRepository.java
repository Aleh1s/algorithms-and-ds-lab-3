package ua.algorithms.repository;

import ua.algorithms.accessor.FileAccessor;
import ua.algorithms.structure.DatumRecord;

public class SimpleRepository {

    private final FileAccessor indexArea;
    private final FileAccessor globalArea;

    public SimpleRepository(FileAccessor indexArea, FileAccessor globalArea) {
        this.indexArea = indexArea;
        this.globalArea = globalArea;
    }

    public void addDatumRecord(DatumRecord datumRecord) {

    }
}
