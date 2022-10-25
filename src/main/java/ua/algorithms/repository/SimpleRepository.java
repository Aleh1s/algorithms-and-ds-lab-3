package ua.algorithms.repository;

import ua.algorithms.accessor.FileAccessor;
import ua.algorithms.structure.Block;
import ua.algorithms.structure.DatumRecord;
import ua.algorithms.structure.IndexRecord;

public class SimpleRepository {

    private final FileAccessor indexArea;
    private final FileAccessor globalArea;

    public SimpleRepository(FileAccessor indexArea, FileAccessor globalArea) {
        this.indexArea = indexArea;
        this.globalArea = globalArea;
    }

    public void addDatumRecord(DatumRecord datumRecord) {
        long pk = datumRecord.getId();
        long ptr = globalArea.getSizeOfFile() / DatumRecord.DATUM_RECORD_BYTES;

        IndexRecord indexRecord = new IndexRecord(pk, ptr);
        long numOfBlocks = indexArea.getSizeOfFile() / Block.BLOCK_BYTES;
        long indexOfBlock = (long) Math.ceil(indexRecord.getPk() / (double) (Block.RECORDS_BYTES / IndexRecord.INDEX_RECORD_BYTES));
    }

}
