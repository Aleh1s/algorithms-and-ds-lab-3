package ua.algorithms.repository;

import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.structure.DataBlock;
import ua.algorithms.structure.DatumRecord;
import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

import java.util.List;

public class SimpleRepository {
    private final IndexFileAccessor indexArea;
    private final GlobalFileAccessor globalArea;

    public SimpleRepository(IndexFileAccessor indexArea, GlobalFileAccessor globalArea) {
        this.indexArea = indexArea;
        this.globalArea = globalArea;
    }

    public void addDatumRecord(DatumRecord datumRecord) {
        if (globalArea.isEmpty()) {
            DataBlock dataBlock = new DataBlock(1, List.of(datumRecord));
            IndexRecord indexRecord = new IndexRecord(datumRecord.getId(), 0);
            IndexBlock indexBlock = new IndexBlock(1, List.of(indexRecord));
            globalArea.write(dataBlock, 0);
            indexArea.write(indexBlock, 0);
        } else {
            long numberOfBlocks = indexArea.countNumberOfBlock();

            long blockIdx = 0;
            IndexBlock indexBlock = null;
            for (; blockIdx < numberOfBlocks; blockIdx++) {
                IndexBlock temp = indexArea.readBlock(blockIdx * IndexBlock.BYTES);
                List<IndexRecord> records = temp.getRecords();
                if (datumRecord.getId() < records.get(records.size() - 1).getPk() || blockIdx == numberOfBlocks - 1) {
                    indexBlock = temp;
                    break;
                }
            }

            assert indexBlock != null;
            indexBlock.addRecord(null); // todo:

        }
    }
}
