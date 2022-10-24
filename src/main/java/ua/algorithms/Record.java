package ua.algorithms;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Record {

    private long id;
    private String value;

    public void setValue(String value) {
        if (value.length() > 255)
            throw new IllegalArgumentException();

        this.value = value;
    }
}
