package util.string.ngraph;

import forms.primitives.segment.Phone;

import java.util.Arrays;

/**
 * Created by janwillem on 20/08/16.
 */
public class ByteNGraph {
    private final byte[] contents;

    public ByteNGraph(byte[] contents) {
        this.contents = contents;
    }

    public int size() {
        return contents.length;
    }

    public static ByteNGraph[] fromByteArray(byte[] bytes, int n) {
        int resultSize = bytes.length-(n-1);
        ByteNGraph[] result = new ByteNGraph[resultSize];
        for (int i=0; i < resultSize; i++) {
            byte[] subBytes = new byte[n];
            for (int j=0; j < n; j++) {
                subBytes[j] = bytes[i+j];
            }
            result[i] = new ByteNGraph(subBytes);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteNGraph that = (ByteNGraph) o;
        return Arrays.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(contents);
    }

    @Override
    public String toString() {
        return Phone.decode(contents);
    }
}
