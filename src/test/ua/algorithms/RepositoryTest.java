package ua.algorithms;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.algorithms.accessor.FileAccessor;
import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.exception.RecordAlreadyExistsException;
import ua.algorithms.mvc.model.SimpleRepository;
import ua.algorithms.structure.DatumRecord;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {
    private final IndexFileAccessor ifa
            = (IndexFileAccessor) FileAccessor.of("src/test/resources/index.bin", "INDEX");
    private final GlobalFileAccessor gfa
            = (GlobalFileAccessor) FileAccessor.of("src/test/resources/global.bin", "GLOBAL");
    private final SimpleRepository repository
            = new SimpleRepository(ifa, gfa);


    @BeforeEach
    void clearAreas() {
        ifa.clearFile();
        gfa.clearFile();
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideData")
    void insertTest(DatumRecord input) {
        repository.insert(input);
        assertEquals(input, repository.findById(input.getId()).get());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideDataForInsertWithEqualIds")
    void insertWithEqualIds(DatumRecord input1, DatumRecord input2) {
        repository.insert(input1);
        assertThrows(RecordAlreadyExistsException.class, () -> repository.insert(input2));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideDataForUpdateEmail")
    void updateEmailTest(DatumRecord record, String newEmail, String expected) {
        repository.insert(record);
        record.setEmail(newEmail);
        repository.update(record);
        assertEquals(expected, repository.findById(record.getId()).get().getEmail());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideDataForUpdateNames")
    void updateFirstNameTest(DatumRecord record, String newFirstName, String expected) {
        repository.insert(record);
        record.setFirstName(newFirstName);
        repository.update(record);
        assertEquals(expected, repository.findById(record.getId()).get().getFirstName());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideDataForUpdateNames")
    void updateLastNameTest(DatumRecord record, String newLastName, String expected) {
        repository.insert(record);
        record.setLastName(newLastName);
        repository.update(record);
        assertEquals(expected, repository.findById(record.getId()).get().getLastName());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideData")
    void deleteTest(DatumRecord input) {
        repository.insert(input);
        long inputId = input.getId();
        repository.delete(inputId);
        assertFalse(repository.findById(inputId).isPresent());
    }

    @Test
    @SneakyThrows
    void deleteAll() {
        final int ITERATIONS = 1000;
        for (int i = 1; i <= ITERATIONS; i++)
            repository.insert(new DatumRecord(i, "test" + i, "test" + i, "test" + i + "@gmail.com"));
        for (int i = 1; i <= ITERATIONS; i++)
            repository.delete(i);
        assertEquals(0L, ifa.getSizeOfFile());
    }

    private static Stream<Arguments> provideData() {
        return Stream.of(
                Arguments.of(new DatumRecord(1, "test1", "test1", "test1@gmail.com")),
                Arguments.of(new DatumRecord(2, "test2", "test2", "test2@gmail.com")),
                Arguments.of(new DatumRecord(3, "test3", "test3", "test3@gmail.com")),
                Arguments.of(new DatumRecord(4, "test4", "test4", "test4@gmail.com")),
                Arguments.of(new DatumRecord(5, "test5", "test5", "test5@gmail.com")),
                Arguments.of(new DatumRecord(6, "test6", "test6", "test6@gmail.com")),
                Arguments.of(new DatumRecord(7, "test7", "test7", "test7@gmail.com")),
                Arguments.of(new DatumRecord(8, "test8", "test8", "test8@gmail.com")),
                Arguments.of(new DatumRecord(9, "test9", "test9", "test9@gmail.com")),
                Arguments.of(new DatumRecord(10, "test10", "test10", "test10@gmail.com"))
        );
    }

    private static Stream<Arguments> provideDataForInsertWithEqualIds() {
        return Stream.of(
                Arguments.of(new DatumRecord(1, "test1", "test1", "test1@gmail.com"),
                        new DatumRecord(1, "test2", "test2", "test2@gmail.com"))
        );
    }

    private static Stream<Arguments> provideDataForUpdateEmail() {
        return Stream.of(
                Arguments.of(new DatumRecord(1, "test1", "test1", "test1@gmail.com"), "update1@gmail.com", "update1@gmail.com"),
                Arguments.of(new DatumRecord(2, "test2", "test2", "test2@gmail.com"), "update2@gmail.com", "update2@gmail.com"),
                Arguments.of(new DatumRecord(3, "test3", "test3", "test3@gmail.com"), "update3@gmail.com", "update3@gmail.com")
        );
    }

    private static Stream<Arguments> provideDataForUpdateNames() {
        return Stream.of(
                Arguments.of(new DatumRecord(1, "test1", "test1", "test1@gmail.com"), "update1", "update1"),
                Arguments.of(new DatumRecord(2, "test2", "test2", "test2@gmail.com"), "update2", "update2"),
                Arguments.of(new DatumRecord(3, "test3", "test3", "test3@gmail.com"), "update3", "update3")
        );
    }
}
