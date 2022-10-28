package ua.algorithms;

import com.google.common.math.LongMath;
import ua.algorithms.accessor.FileAccessor;
import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.repository.SimpleRepository;
import ua.algorithms.structure.DatumRecord;

import java.math.RoundingMode;

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

        int[] arr = {2, 5, 8, 9, 12, 16, 19, 20, 23, 25, 27, 35, 67};
        int search = search(arr, 2);
        System.out.println(search);
        System.out.println(arr[search]);
    }

    public static int search(int[] arr, int search) {
        int k = log2(arr.length, RoundingMode.DOWN);
        int i = (int) pow(2, k) - 1;

        if (search == arr[i])
            return i;

        if (search < arr[i]) {
            int j = 1;
            int n;
            do {
                n = (int) pow(2, k - j++);
                i = search < arr[i] ? i - ((n / 2) + 1) : i + ((n / 2) + 1);

                if (search == arr[i])
                    return i;

            } while (n != 0);
        } else {
            int j = 1;
            int l = log2((long) (arr.length - pow(2, k) + 1), RoundingMode.DOWN);
            i = arr.length + 1 - (int) pow(2, l);
            int n = (int) pow(2, l - j++);

            if (search == arr[i])
                return i;

            do {
                i = search < arr[i] ? i - ((n / 2) + 1) : i + ((n / 2) + 1);
                n = (int) pow(2, l - j++);

                if (search == arr[i])
                    return i;

            } while (n != 0);
        }

        return -1;
    }
}
