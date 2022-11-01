package ua.algorithms.repository;

import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.structure.DatumRecord;
import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.math.LongMath.log2;
import static java.lang.Math.pow;

public class SimpleRepository {
    private final IndexFileAccessor indexArea;
    private final GlobalFileAccessor globalArea;
    public SimpleRepository(IndexFileAccessor indexArea, GlobalFileAccessor globalArea) {
        this.indexArea = indexArea;
        this.globalArea = globalArea;
    }

    public Optional<DatumRecord> findById(int id) {
        Optional<IndexBlock> indexRecordOptional = search(id);

        if (indexRecordOptional.isPresent()) {
            IndexBlock indexBlock = indexRecordOptional.get();
            Optional<IndexRecord> optionalIndexRecord = indexBlock.findById(id);

            if (optionalIndexRecord.isPresent())
                return Optional.of(globalArea.read(optionalIndexRecord.get().getPtr()));
        }

        return Optional.empty();
    }

    public Optional<IndexBlock> search(int id) {
        int length = indexArea.countNumberOfBlocks();
        int k = log2(length, RoundingMode.DOWN), i = (int) pow(2, k) - 1;

        IndexBlock indexBlock = indexArea.readBlock(i);
        int indicator = indexBlock.calculateIndicator(id);

        if (indicator == 0)
            return Optional.of(indexBlock);

        if (indicator < 0)
            return homogeneousBinarySearch(indicator, length, i, k, id);

        int l = log2(length - (int) pow(2, k), RoundingMode.DOWN);
        i = length - (int) pow(2, l);

        indexBlock = indexArea.readBlock(i);
        indicator = indexBlock.calculateIndicator(id);

        if (indicator == 0)
            return Optional.of(indexBlock);

        return homogeneousBinarySearch(indicator, length, i, l, id);
    }

    public Optional<IndexBlock> homogeneousBinarySearch(int indicator, int length, int i, int p, int id) {
        int j = 1;
        for (int n = countN(p, j++); n >= 0; n = countN(p, j++)) {
            if (i >= length)
                i = countI(-1, i, n);
            else
                i = countI(indicator, i, n);

            if (n == 0 && (i < 0 || i >= length))
                return Optional.empty();

            IndexBlock indexBlock = indexArea.readBlock(i);
            indicator = indexBlock.calculateIndicator(id);

            if (indicator == 0)
                return Optional.of(indexBlock);
        }

        return Optional.empty();
    }

    public static int countI(int indicator, int i, int n) {
        return indicator < 0 ? i - ((n / 2) + 1) : i + ((n / 2) + 1);
    }

    public static int countN(int p, int j) {
        return (int) pow(2, p - j);
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
            int numberOfBlocks = indexArea.countNumberOfBlocks();

            int blockIdx = 0;
            IndexBlock indexBlock = null;
            for (; blockIdx < numberOfBlocks; blockIdx++) {
                IndexBlock temp = indexArea.readBlock(blockIdx);
                List<IndexRecord> records = temp.getRecords();
                if (datumRecord.getId() < records.get(records.size() - 1).getPk() || blockIdx == numberOfBlocks - 1) {
                    indexBlock = temp;
                    break;
                }
            }

            assert indexBlock != null;
            boolean isOvercrowded = indexBlock.addRecord(indexRecord);

            int currIdx = blockIdx;
            IndexBlock curr = indexBlock;
            while (isOvercrowded) {
                IndexRecord last = curr.retrieveAndRemoveLast();
                if (currIdx == numberOfBlocks - 1) {
                    IndexBlock newOne = new IndexBlock(0, new ArrayList<>());
                    isOvercrowded = newOne.addRecord(last);
                    indexArea.write(curr, currIdx);
                    curr = newOne;
                    currIdx++;
                } else {
                    IndexBlock next = indexArea.readBlock(currIdx + 1);
                    isOvercrowded = next.addRecord(last);
                    indexArea.write(curr, currIdx);
                    curr = next;
                    currIdx++;
                }
            }
            indexArea.write(curr, currIdx);
        }
    }
}
