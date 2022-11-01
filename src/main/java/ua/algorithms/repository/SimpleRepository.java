package ua.algorithms.repository;

import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.exception.RecordAlreadyExistsException;
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

    public Optional<DatumRecord> findDatumRecordById(int id) {
        Optional<IndexBlock> indexRecordOptional = searchIndexBlock(id);

        if (indexRecordOptional.isPresent()) {
            IndexBlock indexBlock = indexRecordOptional.get();
            Optional<IndexRecord> optionalIndexRecord = indexBlock.findById(id);

            if (optionalIndexRecord.isPresent())
                return Optional.of(globalArea.read(optionalIndexRecord.get().getPtr()));
        }

        return Optional.empty();
    }

    public void addDatumRecord(DatumRecord datumRecord) throws RecordAlreadyExistsException {
        if (globalArea.isEmpty()) {
            addFirst(datumRecord);
        } else {
            IndexRecord indexRecord = writeNewDatumRecord(datumRecord);
            int numberOfBlocks = indexArea.countNumberOfBlocks();

            IndexBlock indexBlock = searchIndexBlock((int) datumRecord.getId())
                    .orElseGet(() -> indexArea.readBlock(numberOfBlocks - 1));

            if (indexBlock.findById((int) datumRecord.getId()).isPresent())
                throw new RecordAlreadyExistsException("Record with id [%d] already exists".formatted(datumRecord.getId()));

            boolean isOvercrowded = indexBlock.addRecord(indexRecord);

            if (isOvercrowded)
                indexBlock = reconstructIndexArea(indexBlock, numberOfBlocks);

            indexArea.write(indexBlock, indexBlock.getNumber());
        }
    }

    private IndexBlock reconstructIndexArea(IndexBlock curr, int numberOfBlocks) {
        int currNumber = curr.getNumber();
        IndexBlock next;
        boolean isOvercrowded = true;
        while (isOvercrowded) {
            IndexRecord last = curr.retrieveAndRemoveLast();

            if (currNumber == numberOfBlocks - 1)
                next = new IndexBlock(numberOfBlocks, 0, new ArrayList<>());
            else
                next = indexArea.readBlock(currNumber + 1);

            isOvercrowded = next.addRecord(last);
            indexArea.write(curr, currNumber++);
            curr = next;
        }

        return curr;
    }

    private void addFirst(DatumRecord datumRecord) {
        IndexRecord indexRecord = writeNewDatumRecord(datumRecord);
        IndexBlock indexBlock = new IndexBlock(0, 1, List.of(indexRecord));
        indexArea.write(indexBlock, 0);
    }

    private IndexRecord writeNewDatumRecord(DatumRecord datumRecord) {
        long ptr = globalArea.write(datumRecord);
        return new IndexRecord(datumRecord.getId(), ptr);
    }

    public Optional<IndexBlock> searchIndexBlock(int id) {
        int length = indexArea.countNumberOfBlocks();

        if (length == 1)
            return Optional.of(indexArea.readBlock(0));

        int k = log2(length, RoundingMode.DOWN), i = (int) pow(2, k) - 1;

        IndexBlock indexBlock = indexArea.readBlock(i);
        int indicator = indexBlock.calculateIndicator(id);

        if (indicator == 0)
            return Optional.of(indexBlock);

        if (indicator < 0)
            return homogeneousBinarySearch(indicator, length, i, k, id);

        int expression = length - (int) pow(2, k);
        if (expression != 0) {
            int l = log2(expression, RoundingMode.DOWN);
            i = length - (int) pow(2, l);

            indexBlock = indexArea.readBlock(i);
            indicator = indexBlock.calculateIndicator(id);

            if (indicator == 0)
                return Optional.of(indexBlock);

            return homogeneousBinarySearch(indicator, length, i, l, id);
        }

        return Optional.empty();
    }

    private Optional<IndexBlock> homogeneousBinarySearch(int indicator, int length, int i, int p, int id) {
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

    private static int countI(int indicator, int i, int n) {
        return indicator < 0 ? i - ((n / 2) + 1) : i + ((n / 2) + 1);
    }

    private static int countN(int p, int j) {
        return (int) pow(2, p - j);
    }
}
