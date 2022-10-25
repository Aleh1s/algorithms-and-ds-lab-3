package ua.algorithms.repository;

import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
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
    public void update(long id, String newValue) {
        long numberOfBlocks = indexArea.countNumberOfBlocks();
        for (int i = 0; i < numberOfBlocks; i++) {
            IndexBlock indexBlock = indexArea.readBlock((long) i * IndexBlock.BYTES);
            List<IndexRecord> records = indexBlock.getRecords();
            long offset = binarySearch(records, 0, records.size() - 1, id);
            if (offset != -1) {
                DatumRecord update = globalArea.read(offset);
                update.setValue(newValue);
                globalArea.update(update, offset);
            }
        }
    }

    public DatumRecord find(long id) {
        long numberOfBlocks = indexArea.countNumberOfBlocks();
        for (int i = 0; i < numberOfBlocks; i++) {
            IndexBlock indexBlock = indexArea.readBlock((long) i * IndexBlock.BYTES);
            List<IndexRecord> records = indexBlock.getRecords();
            long offset = binarySearch(records, 0, records.size() - 1, id);
            if (offset != -1)
                return globalArea.read(offset);
        }
        return null;
    }

    long binarySearch(List<IndexRecord> records, int l, int r, long pk)
    {
        if (r >= l) {
            int mid = l + (r - l) / 2;

            if (records.get(mid).getPk() == pk)
                return records.get(mid).getPtr();

            if (records.get(mid).getPk() > pk)
                return binarySearch(records, l, mid - 1, pk);

            return binarySearch(records, mid + 1, r, pk);
        }

        return -1;
    }

    public void addDatumRecord(DatumRecord datumRecord) {
        if (globalArea.isEmpty()) {
            long ptr = globalArea.write(datumRecord);
            IndexRecord indexRecord = new IndexRecord(datumRecord.getId(), ptr);
            IndexBlock indexBlock = new IndexBlock(1, List.of(indexRecord));
            indexArea.write(indexBlock, 0);
        } else {
            long ptr = globalArea.write(datumRecord);
            IndexRecord indexRecord = new IndexRecord(datumRecord.getId(), ptr);
            long numberOfBlocks = indexArea.countNumberOfBlocks();

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
            boolean isOvercrowded = indexBlock.addRecord(indexRecord);

            long currIdx = blockIdx;
            IndexBlock curr = indexBlock;
            while (isOvercrowded) {
                IndexRecord last = curr.retrieveAndRemoveLast();
                if (currIdx == numberOfBlocks - 1) {
                    IndexBlock newOne = new IndexBlock(0, new ArrayList<>());
                    isOvercrowded = newOne.addRecord(last);
                    indexArea.write(curr, currIdx * IndexBlock.BYTES);
                    curr = newOne;
                    currIdx++;
                } else {
                    IndexBlock next = indexArea.readBlock((currIdx + 1) * IndexBlock.BYTES);
                    isOvercrowded = next.addRecord(last);
                    indexArea.write(curr, currIdx * IndexBlock.BYTES);
                    curr = next;
                    currIdx++;
                }
            }
            indexArea.write(curr, currIdx * IndexBlock.BYTES);
        }
    }
}
