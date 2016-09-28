package io.pfc;

import java.util.Objects;

/**
 * Created by janwillem on 26/09/16.
 */
public class MetaData {
    private final String region;
    private final String speaker;
    private final TextType textType;
    private final static MetaData NULL = new MetaData("XX","XXXX",null);

    public MetaData(String region, String speaker, TextType textType) {
        this.region = region;
        this.speaker = speaker;
        this.textType = textType;
    }

    public static MetaData parseFromString(String input) {
        if (input.length() != 8) {
            return NULL;
        }
        String regionString = input.substring(0,2);
        String speakerString = input.substring(2,6);
        String textTypeString = input.substring(6,8).toUpperCase();
        TextType textType = TextType.valueOf(textTypeString);
        return new MetaData(regionString,speakerString,textType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaData metaData = (MetaData) o;
        return Objects.equals(region, metaData.region) &&
                Objects.equals(speaker, metaData.speaker) &&
                textType == metaData.textType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, speaker, textType);
    }
}
