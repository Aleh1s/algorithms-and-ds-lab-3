package ua.algorithms.mvc;

import ua.algorithms.exception.RecordAlreadyExistsException;
import ua.algorithms.structure.DatumRecord;

import java.util.Optional;

public interface Model {

    Optional<DatumRecord> findById(long id);
    int insert(DatumRecord datumRecord) throws RecordAlreadyExistsException;
    int update(long id, String newValue);
    int delete(long id);

}
