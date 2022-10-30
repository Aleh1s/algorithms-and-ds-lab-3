package ua.algorithms;

import ua.algorithms.accessor.FileAccessor;
import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.repository.SimpleRepository;
import ua.algorithms.structure.DatumRecord;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.stream.IntStream;

import static com.google.common.math.LongMath.*;
import static java.lang.Math.pow;


public class Main {
    public static void main(String[] args) {
        IndexFileAccessor indexFileAccessor =
                (IndexFileAccessor) FileAccessor.of("src/main/resources/index.bin", "INDEX");
        GlobalFileAccessor globalFileAccessor =
                (GlobalFileAccessor) FileAccessor.of("src/main/resources/global.bin", "GLOBAL");

        SimpleRepository simpleRepository = new SimpleRepository(
                indexFileAccessor,
                globalFileAccessor
        );

        DatumRecord datumRecord = simpleRepository.find(100);
        System.out.println(datumRecord);

//        int[] arr1 = {2, 5, 8, 9, 12, 16, 19, 20, 23, 25, 27, 35};
//        Arrays.stream(arr1)
//                .forEach(i -> System.out.println(i + " - " + search(arr1, i)));
//        IntStream.of(-1, 0, 1, 36, 100, 200)
//                .forEach(i -> System.out.println(i + " - " + search(arr1, i)));
//
//        for (int i = 0; i < 10_000; i++) {
//            int[] ints = IntStream.range(0, i).toArray();
//            Arrays.stream(ints)
//                    .forEach(j -> {
//                        int search = search(ints, j);
//
//                        if (j == search) {
//                            System.out.println(true);
//                        } else
//                            System.err.println("size - " + ints.length + ", search - " + search + ", j - " + j);
//
//                    });
//        }
    }

    public static int search(int[] arr, int search) {
        int k = log2(arr.length, RoundingMode.DOWN), i = (int) pow(2, k) - 1;

        if (search == arr[i])
            return i;

        if (search < arr[i])
            return homogeneousBinarySearch(arr, i, search, k);

        int l = log2(arr.length - (int) pow(2, k), RoundingMode.DOWN);
        i = arr.length - (int) pow(2, l);

        if (search == arr[i])
            return i;

        return homogeneousBinarySearch(arr, i, search, l);
    }

    public static int homogeneousBinarySearch(int[] arr, int i, int search, int p) {
        int j = 1;
        for (int n = countN(p, j++); n >= 0; n = countN(p, j++)) {
            if (i >= arr.length)
                i = countI(search, search + 1, i, n);
            else
                i = countI(search, arr[i], i, n);

            if (i < arr.length && search == arr[i])
                return i;
        }

        return -1;
    }

    public static int countI(int search, int curr, int i, int n) {
        return search < curr ? i - ((n / 2) + 1) : i + ((n / 2) + 1);
    }

    public static int countN(int p, int j) {
        return (int) pow(2, p - j);
    }
    
}
