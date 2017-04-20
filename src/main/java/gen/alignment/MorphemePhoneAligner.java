package gen.alignment;

import com.google.common.collect.Lists;
import forms.morphosyntax.MForm;
import forms.morphosyntax.Morpheme;
import forms.primitives.segment.PhoneSubForm;
import util.string.StringCounter;

import java.util.Arrays;
import java.util.List;

/**
 * This is where MForms get aligned to Underlying Forms.
 *
 * @author jwvl
 * @date May 9, 2015
 */
public class MorphemePhoneAligner {

    private final MForm mForm;
    private final PhoneSubForm phoneSubForm;
    private final String phoneString;
    private List<MorphemePhoneAlignment> allAlignments;
    private AlignmentIndexFactory myFactory;
    private final String[] longestSubstrings;
    private int[] preAligned;

    public MorphemePhoneAligner(MForm myMF, PhoneSubForm myPhon,
                                SubstringDatabank lss) {
        mForm = myMF;
        phoneSubForm = myPhon;
        phoneString = myPhon.concatElementsToString("");
        myFactory = AlignmentIndexFactory.createInstance();
        allAlignments = Lists.newArrayList();
        longestSubstrings = createLCSarray(lss);
    }

    private String[] createLCSarray(SubstringDatabank lss) {
        String[] result = new String[mForm.recursiveSize()];
        List<Morpheme> mFormList = mForm.getMorphemes();
        for (int i = 0; i < mFormList.size(); i++) {
            Morpheme m = mFormList.get(i);
            String longestSubstring = "";
            if (m != null) {
                if (lss.getLongestSubstrings(m).size() == 1) {
                    longestSubstring = lss.getLongestSubstrings(m).get(0);
                }
            }
            // TODO Think of solution for > 1 string
            result[i] = longestSubstring;
        }
        return result;
    }

    public List<AlignmentIndex> createAllIndices() {
        System.out.println("Longest substrings: " +Arrays.toString(longestSubstrings));
        List<AlignmentIndex> result = Lists.newArrayList();
        preAligned = new int[phoneSubForm.size()];
        Arrays.fill(preAligned, -1);
        // Fill pre-alignment array
        int startSearchAt = 0;
        for (int iGroup = 0; iGroup < mForm.recursiveSize(); iGroup++) {
            String s = longestSubstrings[iGroup];
            if (s.length() > 0) {
                System.out.println("Looking for substring " + s + " in "
                        + phoneString);
                startSearchAt = fillPrealignedPatterns(iGroup, startSearchAt, s);
            }
        }
        //printAlignmentArray();
        result = myFactory.createAll(preAligned, mForm.recursiveSize());
        return result;
    }

    private int fillPrealignedPatterns(int groupIndex, int from, String pattern) {
        int patternLength = pattern.length();
        int index = StringCounter.firstIndexOfSubstring(phoneString, pattern,
                from);
        if (index > -1) {
            for (int i = index; i < index + patternLength; i++) {
                preAligned[i] = groupIndex;

            }
            return from + patternLength;
        }
        return from;

    }

    public List<MorphemePhoneAlignment> getAlignments() {
        allAlignments = Lists.newArrayList();
        List<AlignmentIndex> allIndices = createAllIndices();
        for (AlignmentIndex ai : allIndices) {
            allAlignments.add(MorphemePhoneAlignment.create(mForm,
                    phoneSubForm, ai));
        }
        return allAlignments;
    }

    public void printAlignmentArray() {
        System.out.println("Alignment for " + mForm);
        for (int i : preAligned) {
            System.out.print(i + 1);
        }
        System.out.println();
        for (char c : phoneString.toCharArray()) {
            System.out.print(c);
        }
        System.out.println();

    }

}