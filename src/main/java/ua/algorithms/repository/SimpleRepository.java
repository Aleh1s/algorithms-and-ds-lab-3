package ua.algorithms.repository;

import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.structure.DatumRecord;
import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public DatumRecord find(int id) {
        IndexRecord indexRecord = search(id).orElseThrow(() -> new RuntimeException("Element doesn't exist"));
        return globalArea.read(indexRecord.getPtr());
    }

    public Optional<IndexRecord> search(int id) {
        long numberOfBlocks = indexArea.countNumberOfBlocks();
        int k = log2(numberOfBlocks, RoundingMode.DOWN), i = (int) pow(2, k) - 1;

        IndexBlock indexBlock = indexArea.readBlock(i);
        IndexBlock.Result result = indexBlock.find(id);

        if (result.hasResult())
            return Optional.of(result.getValue());

        if (result.getIndicator() < 0)
            return homogeneousBinarySearch((int) numberOfBlocks, i, result, k, id);

        int l = log2(numberOfBlocks - (int) pow(2, k), RoundingMode.DOWN);
        i = (int) (numberOfBlocks - (int) pow(2, l));

        IndexBlock indexBlock1 = indexArea.readBlock(i);
        IndexBlock.Result result1 = indexBlock1.find(id);

        if (result1.hasResult())
            return Optional.of(result1.getValue());

        return homogeneousBinarySearch((int) numberOfBlocks, i, result, l, id);
    }

    public Optional<IndexRecord> homogeneousBinarySearch(int length, int i, IndexBlock.Result result, int p, int id) {
        int j = 1;
        for (int n = countN(p, j++); n >= 0; n = countN(p, j++)) {
            if (i >= length)
                i = countI(-1, i, n);
            else
                i = countI(result.getIndicator(), i, n);

            IndexBlock indexBlock = indexArea.readBlock(i);
            IndexBlock.Result result1 = indexBlock.find(id);

            if (result1.hasResult())
                return Optional.of(result1.getValue());
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
            long numberOfBlocks = indexArea.countNumberOfBlocks();

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
                    indexArea.write(curr, (long) currIdx * IndexBlock.BYTES);
                    curr = newOne;
                    currIdx++;
                } else {
                    IndexBlock next = indexArea.readBlock(currIdx + 1);
                    isOvercrowded = next.addRecord(last);
                    indexArea.write(curr, (long) currIdx * IndexBlock.BYTES);
                    curr = next;
                    currIdx++;
                }
            }
            indexArea.write(curr, (long) currIdx * IndexBlock.BYTES);
        }
    }
}
