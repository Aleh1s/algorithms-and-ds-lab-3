package ua.algorithms.indicator;

import ua.algorithms.structure.IndexBlock;
import ua.algorithms.structure.IndexRecord;

public interface Indicator {

    int calculate(long id, IndexBlock block);

}
