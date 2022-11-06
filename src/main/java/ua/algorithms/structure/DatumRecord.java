package ua.algorithms.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DatumRecord implements Comparable<DatumRecord> {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    public static final int ID_OFFSET = 0;
    public static final int ID_BYTES = Long.BYTES;
    public static final int FIRST_NAME_OFFSET = ID_OFFSET + ID_BYTES;
    public static final int FIRST_NAME_LENGTH = 60;
    public static final int FIRST_NAME_BYTES = FIRST_NAME_LENGTH * 2;

    public static final int LAST_NAME_OFFSET = FIRST_NAME_OFFSET + FIRST_NAME_BYTES;
    public static final int LAST_NAME_LENGTH = 60;
    public static final int LAST_NAME_BYTES = LAST_NAME_LENGTH * 2;

    public static final int EMAIL_OFFSET = LAST_NAME_OFFSET + LAST_NAME_BYTES;
    public static final int EMAIL_LENGTH = 60;
    public static final int EMAIL_BYTES = EMAIL_LENGTH * 2;
    public static final int BYTES = ID_BYTES + FIRST_NAME_BYTES + LAST_NAME_BYTES + EMAIL_BYTES;

    public DatumRecord(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        if (firstName.length() > FIRST_NAME_LENGTH)
            throw new IllegalArgumentException();

        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName.length() > LAST_NAME_LENGTH)
            throw new IllegalArgumentException();

        this.lastName = lastName;
    }

    public void setEmail(String email) {
        if (email.length() > EMAIL_LENGTH)
            throw new IllegalArgumentException();

        this.email = email;
    }

    @Override
    public String toString() {
        return """
                {
                    id: %d,
                    firstName: %s,
                    lastName: %s,
                    email: %s
                }
                """.formatted(id, firstName, lastName, email);
    }

    @Override
    public int compareTo(DatumRecord o) {
        return Long.compare(this.id, o.id);
    }
}
