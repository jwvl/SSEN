package simulate.analysis.statistics;

import candidates.Candidate;
import forms.Form;

/**
 * Created by janwillem on 23/09/16.
 */
public class CandidateDistance {

    public static double getDistance(Candidate a, Candidate b) {
        int length = Math.max(a.length(), b.length());
        double distance = 0;
        Form[] aForms = a.getForms();
        Form[] bForms = b.getForms();
        for (int i=0; i < length; i++) {
            if (!aForms[i].equals(bForms[i])) {
                distance += 1;
            }
        }
        return distance / length;
    }
}
