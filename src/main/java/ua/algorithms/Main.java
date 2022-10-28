package ua.algorithms;

import com.google.common.math.LongMath;
import ua.algorithms.accessor.FileAccessor;
import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.repository.SimpleRepository;
import ua.algorithms.structure.DatumRecord;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.common.math.LongMath.*;
import static java.lang.Math.pow;


public class Main {
    public static void main(String[] args) {
//        IndexFileAccessor indexFileAccessor =
//                (IndexFileAccessor) FileAccessor.of("src/main/resources/index.bin", "INDEX");
//        GlobalFileAccessor globalFileAccessor =
//                (GlobalFileAccessor) FileAccessor.of("src/main/resources/global.bin", "GLOBAL");
//
//        SimpleRepository simpleRepository = new SimpleRepository(
//                indexFileAccessor,
//                globalFileAccessor
//        );
//
//        DatumRecord datumRecord = simpleRepository.find(100);
//        System.out.println(datumRecord);

//        int[] arr1 = {2, 5, 8, 9, 12, 16, 19, 20, 23, 25, 27, 35};
//        Arrays.stream(arr1)
//                .forEach(i -> System.out.println(i + " - " + search(arr1, i)));
//        IntStream.of(-1, 0, 1, 36, 100, 200)
//                .forEach(i -> System.out.println(i + " - " + search(arr1, i)));

        for (int i = 0; i < 1000; i++) {
            int[] ints = IntStream.range(0, i).toArray();
            Arrays.stream(ints)
                    .forEach(j -> {
                        int search = search(ints, j);
                        if (j != search)
                            System.err.println("size - " + ints.length + ", search - " + search + ", j - " + j);
                    });
        }

    }

    public static int search(int[] arr, int search) {
        int k = log2(arr.length, RoundingMode.DOWN);
        int i = (int) pow(2, k) - 1;

        if (search == arr[i])
            return i;

        if (search < arr[i]) {
            return homogeneousBinarySearch(i, search, arr, k);
        } else {
            int l = log2((long) (arr.length - pow(2, k) + 1), RoundingMode.DOWN);
            i = arr.length + 1 - (int) pow(2, l);

            if (search == arr[i])
                return i;

            return homogeneousBinarySearch(i, search, arr, l);
        }
    }

    public static int homogeneousBinarySearch(int i, int search, int[] arr, int p) {
        int j = 1, n = countN(p, j++);
        boolean end = false;
        do {
            if (n == 0)
                end = true;

            i = countI(search, arr[i], i, n);
            n = countN(p, j++);

            if (i < arr.length && search == arr[i])
                return i;

        } while (!end);
        return -1;
    }

    public static int countI(int search, int K, int i, int n) {
        return search < K ? i - ((n / 2) + 1) : i + ((n / 2) + 1);
    }

    public static int countN(int p, int j) {
        return (int) pow(2, p - j);
    }


}
