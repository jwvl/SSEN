package util.bitEncode;

import cern.colt.bitvector.BitVector;
import util.debug.Stopwatch;

import java.util.Random;

/**
 * Created by janwillem on 28/03/16.
 */
public class SmallIntList {
    private final int bitsNeeded;
    private final BitVector bitset;
    private final int length;

    public SmallIntList(int numElements, int maxValue) {
        length = numElements;
        bitsNeeded = findBitsNeeded(maxValue);
        bitset = new BitVector(numElements*bitsNeeded);
    }

    public int get(int index) {
        int from = index * bitsNeeded;
        int result = 0;
        for (int i = from; i < from + bitsNeeded; i++) {
            result = (result << 1) + (bitset.get(i) ? 1 : 0);
        }
        return result;
    }

    public void set(int index, int value) {
        int firstBitIndex = index * bitsNeeded;
        for (int i = 0; i < bitsNeeded; i++) {
            int setAt = firstBitIndex + (bitsNeeded - 1 - i);
            if (((1 << i & value) != 0)) {
                bitset.set(setAt);
            }
        }
    }

    public int length() {
        return length;
    }

    private static int findBitsNeeded(int maxValue) {
        int count = 0;
        while (maxValue > 0) {
            count++;
            maxValue = maxValue >> 1;
        }
        return count;
    }

    public String printBits() {
        StringBuilder result = new StringBuilder("[");
        int arrayLength = length * bitsNeeded;
        for (int i = 0; i < arrayLength; i++) {
            result.append(bitset.get(i) ? "1" : 0);
            if (i % bitsNeeded == bitsNeeded - 1 && i < arrayLength - 1) {
                result.append(",");
            }
        }
        return result.append("]").toString();
    }

    public static void main(String[] args) {
        int maxValue = 20;
        int arrayLength = 5000000;
        int[] intArray = new int[arrayLength];
        SmallIntList intList = new SmallIntList(arrayLength, maxValue);
        Random random = new Random();
        Stopwatch.start();
        for (int i = 0; i < arrayLength; i++) {
            int nextRand = random.nextInt(maxValue + 1);
            intArray[i] = nextRand;

        }
        Stopwatch.reportElapsedTime("Filling regular array took ", true);

        for (int i = 0; i < arrayLength; i++) {
            int next = intArray[i];
        }
        Stopwatch.reportElapsedTime("Reading regular array took ", true);


        for (int i = 0; i < arrayLength; i++) {
            int nextRand = random.nextInt(maxValue + 1);
            intList.set(i, nextRand);
        }
        Stopwatch.reportElapsedTime("Filling SmallInt array took ", true);

        for (int i = 0; i < arrayLength; i++) {
            int next = intList.get(i);
        }
        Stopwatch.reportElapsedTime("Reading SmallIntarray took ", true);


    }


}
