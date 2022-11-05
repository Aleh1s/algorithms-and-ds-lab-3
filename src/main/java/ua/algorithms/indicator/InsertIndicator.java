package ua.algorithms.indicator;

import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

import java.util.List;

public class InsertIndicator implements Indicator {
    @Override
    public int calculate(int id, IndexBlock block) {
        List<IndexRecord> records = block.getRecords();
        IndexRecord first = records.get(0);
        if (records.size() == 1) {
            if (id == first.getPk())
                return 0;

            if (id < first.getPk())
                return -1;

            return 1;
        }

        IndexRecord last = records.get(records.size() - 1);
        if (id >= first.getPk() && id <= last.getPk())
            return 0;

        if (id < first.getPk())
            return -1;

        return 1;
    }
}
