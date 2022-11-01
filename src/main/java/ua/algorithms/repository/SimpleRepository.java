package ua.algorithms.repository;

import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.exception.RecordAlreadyExistsException;
import ua.algorithms.indicator.Indicator;
import ua.algorithms.indicator.InsertIndicator;
import ua.algorithms.indicator.SearchIndicator;
import ua.algorithms.structure.DatumRecord;
import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

import java.math.RoundingMode;
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
        Optional<IndexBlock> indexRecordOptional = searchIndexBlock(id, new SearchIndicator());

        if (indexRecordOptional.isPresent()) {
            IndexBlock indexBlock = indexRecordOptional.get();
            Optional<IndexRecord> optionalIndexRecord = indexBlock.findById(id);

            if (optionalIndexRecord.isPresent())
                return Optional.of(globalArea.read(optionalIndexRecord.get().getPtr()));
        }

        return Optional.empty();
    }

    public int insert(DatumRecord datumRecord) throws RecordAlreadyExistsException {
        if (globalArea.isEmpty()) {
            addFirst(datumRecord);
        } else {
            IndexRecord indexRecord = writeNewDatumRecord(datumRecord);
            int numberOfBlocks = indexArea.countNumberOfBlocks();

            IndexBlock indexBlock = searchIndexBlock((int) datumRecord.getId(), new InsertIndicator())
                    .orElseGet(() -> indexArea.readBlock(numberOfBlocks - 1));

            if (indexBlock.findById((int) datumRecord.getId()).isPresent())
                throw new RecordAlreadyExistsException("Record with id [%d] already exists".formatted(datumRecord.getId()));

            boolean isOvercrowded = indexBlock.addRecord(indexRecord);

            if (isOvercrowded)
                reconstructIndexArea(indexBlock, numberOfBlocks);
            else
                indexArea.write(indexBlock, indexBlock.getNumber());

        }

        return 1;
    }

    public int update(int id, String value) {
        Optional<IndexBlock> optionalIndexBlock = searchIndexBlock(id, new SearchIndicator());

        if (optionalIndexBlock.isPresent()) {
            IndexBlock indexBlock = optionalIndexBlock.get();
            Optional<IndexRecord> optionalIndexRecord = indexBlock.findById(id);
            if (optionalIndexRecord.isPresent()) {
                IndexRecord indexRecord = optionalIndexRecord.get();
                DatumRecord datumRecord = globalArea.read(indexRecord.getPtr());
                datumRecord.setValue(value);
                globalArea.update(datumRecord, indexRecord.getPtr());
                return 1;
            }
        }

        return 0;
    }

    public int delete(int id) {
        Optional<IndexBlock> optionalIndexBlock = searchIndexBlock(id, new SearchIndicator());

        if (optionalIndexBlock.isPresent()) {
            IndexBlock curr = optionalIndexBlock.get();
            int number = curr.delete(id);

            if (curr.isEmpty()) {
                int numberOfBlocks = indexArea.countNumberOfBlocks();

                IndexBlock next;
                while (true) {
                    if (curr.getNumber() == numberOfBlocks - 1) {
                        indexArea.setLength(indexArea.getSizeOfFile() - IndexBlock.BYTES);
                        return number;
                    }

                    next = indexArea.readBlock(curr.getNumber() + 1);
                    next.setNumber(curr.getNumber());
                    indexArea.write(next, curr.getNumber());
                    curr = next;
                }
            } else {
                if (number > 0) {
                    indexArea.write(curr, curr.getNumber());
                    return number;
                }
            }
        }

        return 0;
    }

    private void reconstructIndexArea(IndexBlock curr, int numberOfBlocks) {
        IndexBlock temp = curr.separate();

        if (curr.getNumber() == numberOfBlocks - 1) {
            indexArea.write(curr, curr.getNumber());
            indexArea.write(temp, temp.getNumber());
            return;
        }

        IndexBlock next = indexArea.readBlock(curr.getNumber() + 1);
        while (true) {
            if (next.getNumber() == numberOfBlocks - 1) {
                temp.setNumber(next.getNumber());
                indexArea.write(temp, next.getNumber());
                next.setNumber(next.getNumber() + 1);
                indexArea.write(next, next.getNumber());
                return;
            }

            temp.setNumber(next.getNumber());
            indexArea.write(temp, next.getNumber());
            temp = next;
            next = indexArea.readBlock(temp.getNumber() + 1);
        }
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

    public Optional<IndexBlock> searchIndexBlock(int id, Indicator indicator) {
        int length = indexArea.countNumberOfBlocks();

        if (length == 0)
            return Optional.empty();

        if (length == 1)
            return Optional.of(indexArea.readBlock(0));

        int k = log2(length, RoundingMode.DOWN), i = (int) pow(2, k) - 1;

        IndexBlock indexBlock = indexArea.readBlock(i);
        int ind = indicator.calculate(id, indexBlock);

        if (ind == 0)
            return Optional.of(indexBlock);

        if (ind < 0)
            return homogeneousBinarySearch(indicator, ind, length, i, k, id);

        int expression = length - (int) pow(2, k);
        if (expression != 0) {
            int l = log2(expression, RoundingMode.DOWN);
            i = length - (int) pow(2, l);

            indexBlock = indexArea.readBlock(i);
            ind = indicator.calculate(id, indexBlock);

            if (ind == 0)
                return Optional.of(indexBlock);

            return homogeneousBinarySearch(indicator, ind, length, i, l, id);
        }

        return Optional.empty();
    }

    private Optional<IndexBlock> homogeneousBinarySearch(Indicator indicator, int ind, int length, int i, int p, int id) {
        int j = 1;
        for (int n = countN(p, j++); n >= 0; n = countN(p, j++)) {
            if (i >= length)
                i = countI(-1, i, n);
            else
                i = countI(ind, i, n);

//            if (n == 0 && (i < 0 || i >= length))
//                return Optional.empty();

            IndexBlock indexBlock = indexArea.readBlock(i);
            ind = indicator.calculate(id, indexBlock);

            if (ind == 0)
                return Optional.of(indexBlock);

            if (n == 0)
                break;
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
