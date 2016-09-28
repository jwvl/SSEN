package io.pfc.ngrams;

import java.util.Objects;

/**
 * Created by janwillem on 26/09/16.
 */
public class Bigram {
    public final String left;
    public final String right;

    public Bigram(String left, String right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bigram bigram = (Bigram) o;
        return Objects.equals(left, bigram.left) &&
                Objects.equals(right, bigram.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
