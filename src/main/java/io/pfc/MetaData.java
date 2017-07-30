package io.pfc;

import java.util.Objects;

/**
 * Created by janwillem on 26/09/16.
 */
public class MetaData {
    private final String region;
    private final String speakerCode;
    private final String speaker;
    private final TextType textType;
    private final static MetaData NULL = new MetaData("XX","XXXX", "XX", null);

    public MetaData(String region, String speakerCode, String speaker, TextType textType) {
        this.region = region;
        this.speakerCode = speakerCode;
        this.speaker = speaker;
        this.textType = textType;
    }

    public static MetaData parseFromString(String input) {
        if (input.length() < 8 || input.length() > 15) {
            return NULL;
        }
        String regionString = input.substring(0, 2);
        String speakerString = input.substring(2, 6);
        String textTypeString = input.substring(6, 8).toUpperCase();
        String speaker = getSpeaker(input);
        TextType textType = TextType.valueOf(textTypeString);
        return new MetaData(regionString, speakerString, speaker, textType);
    }

    private static String getSpeaker(String input) {
        int indexOfComma = input.indexOf(",");
        String speakerString = input.substring(indexOfComma+1);
        return speakerString.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaData metaData = (MetaData) o;
        return Objects.equals(region, metaData.region) &&
                Objects.equals(speakerCode, metaData.speakerCode) &&
                textType == metaData.textType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, speakerCode, textType);
    }

    public String toString() {
        return region+ speakerCode +textType;
    }

    public String getSpeakerCode() {
        return speakerCode;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getFullCode() {
        String textTypeString = textType == null ? "xx" : textType.toString().toLowerCase();
        return region+speakerCode+textTypeString+","+speaker;
    }
}
