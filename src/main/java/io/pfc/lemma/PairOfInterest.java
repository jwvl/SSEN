package io.pfc.lemma;

import io.pfc.liaison.LiaisonOpportunity;

/**
 * Created by janwillem on 19/04/2017.
 */
public class PairOfInterest {
    private final LiaisonOpportunity liaisonOpportunity;
    private final LexiqueEntry left;
    private final LexiqueEntry right;

    public PairOfInterest(LiaisonOpportunity liaisonOpportunity, LexiqueEntry left, LexiqueEntry right) {
        this.liaisonOpportunity = liaisonOpportunity;
        this.left = left;
        this.right = right;
    }
}
