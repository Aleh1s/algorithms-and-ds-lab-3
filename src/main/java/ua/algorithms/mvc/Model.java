package ua.algorithms.mvc;

import ua.algorithms.exception.RecordAlreadyExistsException;
import ua.algorithms.structure.DatumRecord;

import java.util.Optional;

public interface Model {

    Optional<DatumRecord> findById(long id);
    int insert(DatumRecord newDatumRecord) throws RecordAlreadyExistsException;
    int update(DatumRecord updatedDatumRecord);
    int delete(long id);

}
