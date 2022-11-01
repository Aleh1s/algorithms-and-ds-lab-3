package ua.algorithms.indicator;

import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

import java.util.List;

public class InsertIndicator implements Indicator {
    @Override
    public int calculate(int id, IndexBlock block) {
        List<IndexRecord> records = block.getRecords();
        IndexRecord first;
        try {
            first = records.get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException();
        }
        long pk = first.getPk();
        boolean fits = id > pk && id <= pk + (IndexBlock.RECORDS_BYTES / IndexRecord.BYTES - 1);
        if (records.size() == 1) {

            if (fits)
                return 0;

            if (id < first.getPk())
                return -1;

            return 1;
        }

        IndexRecord last = records.get(records.size() - 1);
        if ((id >= first.getPk() && id <= last.getPk()) || fits)
            return 0;

        if (id < first.getPk())
            return -1;

        return 1;
    }
}
