package ua.algorithms.mvc;

import ua.algorithms.exception.RecordAlreadyExistsException;
import ua.algorithms.structure.DatumRecord;

import java.util.Optional;

public class SimpleController implements Controller {

    private Model model;

    public SimpleController(Model model) {
        this.model = model;
    }

    private static final String INVALID_INPUT = "Invalid input";
    private static final String VIOLATION_OF_CONSTRAINT = "Violation of constraint. Value length must be <= 60 but actually [%d]";
    private static final String RECORDS_AFFECTED = "%d records was affected";
    @Override
    public String select(String pk) {
        long id;
        try {
            id = Long.parseLong(pk);
        } catch (NumberFormatException e) {
            return INVALID_INPUT;
        }

        Optional<DatumRecord> datumRecordOptional = model.findById(id);

        if (datumRecordOptional.isPresent())
            return datumRecordOptional.get().toString();

        return "Record with pk [%d] does not exist".formatted(id);
    }

    @Override
    public String insert(String pk, String value) {
        long id;
        try {
            id = Long.parseLong(pk);
        } catch (NumberFormatException e) {
            return INVALID_INPUT;
        }

        if (value.length() > 60)
            return VIOLATION_OF_CONSTRAINT.formatted(value.length());

        int insert;
        try {
            insert = model.insert(new DatumRecord(id, value));
        } catch (RecordAlreadyExistsException e) {
            return e.getMessage();
        }

        return RECORDS_AFFECTED.formatted(insert);
    }

    @Override
    public String update(String pk, String newValue) {
        long id;
        try {
            id = Long.parseLong(pk);
        } catch (NumberFormatException e) {
            return INVALID_INPUT;
        }

        if (newValue.length() > 60)
            return VIOLATION_OF_CONSTRAINT.formatted(newValue.length());

        int update = model.update(id, newValue);

        return RECORDS_AFFECTED.formatted(update);
    }

    @Override
    public String delete(String pk) {
        long id;
        try {
            id = Long.parseLong(pk);
        } catch (NumberFormatException e) {
            return INVALID_INPUT;
        }

        int update = model.delete(id);

        return RECORDS_AFFECTED.formatted(update);
    }


}
