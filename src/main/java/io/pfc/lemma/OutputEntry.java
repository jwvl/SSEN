package io.pfc.lemma;

import io.pfc.MetaData;
import io.pfc.liaison.LiaisonOpportunity;

/**
 * Created by janwillem on 19/04/2017.
 */
public class OutputEntry {
    private final MetaData metaData;
    private final String sentence;
    private final LiaisonOpportunity liaison;
    private final String phoneticSequence;
    private final LexiqueEntry lexiqueInfo;

    public OutputEntry(MetaData metaData, String sentence, LiaisonOpportunity liaison, String phoneticSequence, LexiqueEntry lexiqueInfo) {
        this.metaData = metaData;
        this.sentence = sentence;
        this.liaison = liaison;
        this.phoneticSequence = phoneticSequence;
        this.lexiqueInfo = lexiqueInfo;
    }
}
