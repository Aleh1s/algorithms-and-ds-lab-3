package ua.algorithms.mvc.controller;

import ua.algorithms.exception.RecordAlreadyExistsException;
import ua.algorithms.mvc.Controller;
import ua.algorithms.mvc.Model;
import ua.algorithms.structure.DatumRecord;

import java.util.Objects;
import java.util.Optional;

public class SimpleController implements Controller {

    private final Model model;

    public SimpleController(Model model) {
        this.model = model;
    }

    private static final String INVALID_INPUT = "Invalid input";
    private static final String VIOLATION_OF_LENGTH_CONSTRAINT = "Violation of constraint. Value [%s] length must be <= [%d] but actually [%d]";
    private static final String VIOLATION_OF_EMPTY_VALUE_CONSTANT = "Violation of constraint. Value [%s] length must be present";
    private static final String RECORDS_AFFECTED = "%d records was affected";
    private static final String FIRST_NAME_FIELD = "first name";
    private static final String LAST_NAME_FIELD = "last name";
    private static final String EMAIL = "email";

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
    public String insert(String pk, String firstName, String lastName, String email) {
        long id;
        try {
            id = Long.parseLong(pk);
        } catch (NumberFormatException e) {
            return INVALID_INPUT;
        }

        if (firstName.length() == 0)
            return VIOLATION_OF_EMPTY_VALUE_CONSTANT.formatted(FIRST_NAME_FIELD);

        if (lastName.length() == 0)
            return VIOLATION_OF_EMPTY_VALUE_CONSTANT.formatted(LAST_NAME_FIELD);

        if (email.length() == 0)
            return VIOLATION_OF_EMPTY_VALUE_CONSTANT.formatted(EMAIL);

        String check = checkLengthConstraint(firstName, lastName, email);

        if (Objects.nonNull(check))
            return check;

        int insert;
        try {
            insert = model.insert(new DatumRecord(id, firstName, lastName, email));
        } catch (RecordAlreadyExistsException e) {
            return e.getMessage();
        }

        return RECORDS_AFFECTED.formatted(insert);
    }

    @Override
    public String update(String pk, String firstName, String lastName, String email) {
        long id;
        try {
            id = Long.parseLong(pk);
        } catch (NumberFormatException e) {
            return INVALID_INPUT;
        }

        String check = checkLengthConstraint(firstName, lastName, email);

        if (Objects.nonNull(check))
            return check;

        int update = model.update(new DatumRecord(id, firstName, lastName, email));

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

    private String checkLengthConstraint(String firstName, String lastName, String email) {
        if (firstName.length() > DatumRecord.FIRST_NAME_LENGTH)
            return VIOLATION_OF_LENGTH_CONSTRAINT.formatted(
                    FIRST_NAME_FIELD, DatumRecord.FIRST_NAME_LENGTH, firstName.length());

        if (lastName.length() > DatumRecord.LAST_NAME_LENGTH)
            return VIOLATION_OF_LENGTH_CONSTRAINT.formatted(
                    LAST_NAME_FIELD, DatumRecord.LAST_NAME_LENGTH, lastName.length());

        if (email.length() > DatumRecord.EMAIL_LENGTH)
            return VIOLATION_OF_LENGTH_CONSTRAINT.formatted(EMAIL,
                    DatumRecord.EMAIL_LENGTH, email.length());

        return null;
    }

}
