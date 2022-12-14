package ua.algorithms.mvc.model;

import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.exception.RecordAlreadyExistsException;
import ua.algorithms.mvc.Model;
import ua.algorithms.structure.DatumRecord;
import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import static com.google.common.math.LongMath.log2;
import static java.lang.Math.pow;

public class SimpleRepository implements Model {
    private final IndexFileAccessor indexArea;
    private final GlobalFileAccessor globalArea;
    private int counter;

    public SimpleRepository(IndexFileAccessor indexArea, GlobalFileAccessor globalArea) {
        this.indexArea = indexArea;
        this.globalArea = globalArea;
    }

    public Optional<DatumRecord> findById(long id) {
        Optional<IndexBlock> indexBlockOptional = searchIndexBlock(id);

        if (indexBlockOptional.isPresent()) {
            IndexBlock indexBlock = indexBlockOptional.get();
            Optional<IndexRecord> indexRecordOptional = indexBlock.findById(id);

            if (indexRecordOptional.isPresent())
                return Optional.of(globalArea.read(indexRecordOptional.get().ptr()));
        }

        return Optional.empty();
    }

    public int insert(DatumRecord datumRecord) throws RecordAlreadyExistsException {
        if (globalArea.isEmpty()) {
            addFirst(datumRecord);
        } else {
            IndexRecord indexRecord = writeNewDatumRecord(datumRecord);
            int numberOfIndexBlocks = indexArea.countNumberOfBlocks();

            int blockIdx = 0;
            IndexBlock indexBlock = null;
            for (; blockIdx < numberOfIndexBlocks; blockIdx++) {
                IndexBlock temp = indexArea.readBlock(blockIdx);
                List<IndexRecord> records = temp.getRecords();
                if (datumRecord.getId() < records.get(records.size() - 1).pk() || blockIdx == numberOfIndexBlocks - 1) {
                    indexBlock = temp;
                    break;
                }
            }

            assert indexBlock != null;
            if (indexBlock.findById(datumRecord.getId()).isPresent())
                throw new RecordAlreadyExistsException("Record with id [%d] already exists".formatted(datumRecord.getId()));

            boolean isOvercrowded = indexBlock.addRecord(indexRecord);

            if (isOvercrowded)
                reconstructIndexArea(indexBlock, numberOfIndexBlocks);
            else
                indexArea.write(indexBlock, indexBlock.getIndex());

        }

        return 1;
    }

    public int update(DatumRecord updatedDatumRecord) {
        long id = updatedDatumRecord.getId();
        Optional<IndexBlock> optionalIndexBlock = searchIndexBlock(id);

        if (optionalIndexBlock.isPresent()) {
            IndexBlock indexBlock = optionalIndexBlock.get();
            Optional<IndexRecord> optionalIndexRecord = indexBlock.findById(id);
            if (optionalIndexRecord.isPresent()) {
                IndexRecord indexRecord = optionalIndexRecord.get();
                DatumRecord datumRecord = globalArea.read(indexRecord.ptr());

                if (!updatedDatumRecord.getFirstName().isBlank())
                    datumRecord.setFirstName(updatedDatumRecord.getFirstName());

                if (!updatedDatumRecord.getLastName().isBlank())
                    datumRecord.setLastName(updatedDatumRecord.getLastName());

                if (!updatedDatumRecord.getEmail().isBlank())
                    datumRecord.setEmail(updatedDatumRecord.getEmail());

                globalArea.update(datumRecord, indexRecord.ptr());
                return 1;
            }
        }

        return 0;
    }

    public int delete(long id) {
        Optional<IndexBlock> optionalIndexBlock = searchIndexBlock(id);

        if (optionalIndexBlock.isPresent()) {
            IndexBlock curr = optionalIndexBlock.get();
            int number = curr.delete(id);

            if (curr.isEmpty()) {
                int numberOfBlocks = indexArea.countNumberOfBlocks();

                int temp = curr.getIndex();
                IndexBlock next;
                while (true) {
                    if (temp == numberOfBlocks - 1) {
                        indexArea.setLength(indexArea.getSizeOfFile() - IndexBlock.BYTES);
                        return number;
                    }

                    next = indexArea.readBlock(temp + 1);
                    temp = next.getIndex();
                    next.setIndex(next.getIndex() - 1);
                    indexArea.write(next, next.getIndex());
                }
            } else {
                if (number > 0) {
                    indexArea.write(curr, curr.getIndex());
                    return number;
                }
            }
        }

        return 0;
    }

    private void reconstructIndexArea(IndexBlock curr, int numberOfBlocks) {
        IndexBlock temp = curr.separate();

        if (curr.getIndex() == numberOfBlocks - 1) {
            indexArea.write(curr, curr.getIndex());
            indexArea.write(temp, temp.getIndex());
        } else {
            IndexBlock next = indexArea.readBlock(curr.getIndex() + 1);
            while (true) {
                if (next.getIndex() == numberOfBlocks - 1) {
                    temp.setIndex(next.getIndex());
                    indexArea.write(temp, next.getIndex());
                    next.setIndex(next.getIndex() + 1);
                    indexArea.write(next, next.getIndex());
                    return;
                }

                temp.setIndex(next.getIndex());
                indexArea.write(temp, next.getIndex());
                temp = next;
                next = indexArea.readBlock(temp.getIndex() + 1);
            }
        }
    }

    private void addFirst(DatumRecord datumRecord) {
        IndexRecord indexRecord = writeNewDatumRecord(datumRecord);
        IndexBlock indexBlock = new IndexBlock(0, 0, new TreeMap<>());
        indexBlock.addRecord(indexRecord);
        indexArea.write(indexBlock, indexBlock.getIndex());
    }

    private IndexRecord writeNewDatumRecord(DatumRecord datumRecord) {
        long ptr = globalArea.write(datumRecord);
        return new IndexRecord(datumRecord.getId(), ptr);
    }

    public Optional<IndexBlock> searchIndexBlock(long key) {
        counter = 0;
        int length = indexArea.countNumberOfBlocks();

        if (length == 0)
            return Optional.empty();

        int k = log2(length, RoundingMode.DOWN), i = (int) pow(2, k) - 1;

        IndexBlock indexBlock = indexArea.readBlock(i);
        int itr = indexBlock.calculateIndicator(key);
        counter++;

        if (itr == 0)
            return Optional.of(indexBlock);

        if (itr < 0)
            return homogeneousBinarySearch(
                    key, i - ((int) pow(2, k) / 2), (int) pow(2, k) / 2);

        if (length > (int) pow(2, k)) {
            int l = log2(length - (int) pow(2, k) + 1, RoundingMode.DOWN);
            return homogeneousBinarySearch(key, length - (int) pow(2, l), (int) pow(2, l));
        }

        return Optional.empty();
    }

    private Optional<IndexBlock> homogeneousBinarySearch(long key, int i, int step) {
        do {
            IndexBlock indexBlock = indexArea.readBlock(i);
            int itr = indexBlock.calculateIndicator(key);
            counter++;

            if (itr == 0)
                return Optional.of(indexBlock);

            step /= 2;

            i += itr < 0 ? -step : step;

        } while (step > 0);

        return Optional.empty();
    }
}
