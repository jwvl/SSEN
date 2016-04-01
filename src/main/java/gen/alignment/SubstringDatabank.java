/**
 *
 */
package gen.alignment;

import com.google.common.collect.*;
import forms.morphosyntax.MForm;
import forms.morphosyntax.Morpheme;
import forms.phon.flat.PhoneticForm;
import util.string.LongestSubstring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A helper class that first extracts Morphemes and Strings from all MForms with
 * the Phonetic Forms of the original datum, then finds the longest common
 * substring for each. Since an MForm may have *two* longest substrings, they
 * are stored in a Multimap as well.
 *
 * @author jwvl
 * @date May 9, 2015
 */
public class SubstringDatabank {
    private Map<MForm, PhoneticForm> mfToPf;
    private SetMultimap<Morpheme, PhoneticForm> pfsPerMorpheme;
    private ListMultimap<Morpheme, String> longestSubstrings;
    private boolean done = false;

    private SubstringDatabank() {
        mfToPf = Maps.newHashMap();
        pfsPerMorpheme = HashMultimap.create();
        longestSubstrings = ArrayListMultimap.create();
    }

    public static SubstringDatabank createInstance() {
        return new SubstringDatabank();
    }

    public void addPair(MForm mf, PhoneticForm pf) {
        mfToPf.put(mf, pf);
        List<Morpheme> asMorphemes = mf.getMorphemes();
        for (Morpheme m : asMorphemes) {
            pfsPerMorpheme.put(m, pf);
        }
    }

    public void init() {
        setAllSubstrings();
        done = true;
    }

    public List<String> getLongestSubstrings(Morpheme m) {
        if (done == false) {
            System.err
                    .println("Wait! Longest substrings have not been calculated yet.");
            return Lists.newArrayList();
        } else {
            return longestSubstrings.get(m);
        }
    }

    private void setAllSubstrings() {
        for (Morpheme m : pfsPerMorpheme.keySet()) {
            setSubstrings(m);
        }
    }

    private void setSubstrings(Morpheme m) {
        if (!pfsPerMorpheme.containsKey(m)) {
            System.err.println("Error! Morpheme not found.");
        }
        List<String> longest = calculateLongestSubstrings(m);
        longestSubstrings.putAll(m, longest);
    }

    private List<String> calculateLongestSubstrings(Morpheme m) {
        ArrayList<String> allStrings = new ArrayList<String>();
        for (PhoneticForm pf : pfsPerMorpheme.get(m)) {
            allStrings.add(pf.toString());
            System.out.println("Adding " + pf.toString());
        }
        allStrings.trimToSize();
        List<String> longest = LongestSubstring.find(allStrings);
        if (longest.get(0).length() < 1) {
            System.out.println("No longest substring found for " + m + ";");
            longest.clear();
            longest.add("");
        } else {
            System.out.println("Longest substring found for " + m + " : " + longest.get(0));
        }
        return longest;
    }

    /**
     * @return
     */
    public Collection<Morpheme> getMorphemes() {
        return pfsPerMorpheme.keySet();
    }

    public Map<MForm, PhoneticForm> getMfToPf() {
        return mfToPf;
    }

}
