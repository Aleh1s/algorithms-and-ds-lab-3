package ua.algorithms;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ByteBuffer allocate = ByteBuffer.allocate(16);
        allocate.putLong(1);
        allocate.putLong(1);
        byte[] array = allocate.array();
        System.out.println(Arrays.toString(array));

    }
}
