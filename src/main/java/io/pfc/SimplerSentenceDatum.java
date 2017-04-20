package io.pfc;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by janwillem on 19/04/2017.
 */
public class SimplerSentenceDatum {
    private final String[] tokens;
    private final MetaData metaData;

    public SimplerSentenceDatum(String[] tokens, MetaData metaData) {
        this.tokens = tokens;
        this.metaData = metaData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplerSentenceDatum that = (SimplerSentenceDatum) o;
        return Arrays.equals(tokens, that.tokens) &&
                Objects.equals(metaData, that.metaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokens, metaData);
    }

    public String toString() {
        return metaData.toString()+"\t"+Arrays.toString(tokens);
    }

    public String[] getTokens() {
        return tokens;
    }

    public MetaData getMetaData() {
        return metaData;
    }
}
