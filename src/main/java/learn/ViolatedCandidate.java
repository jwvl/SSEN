/**
 *
 */
package learn;

import candidates.Candidate;
import com.google.common.collect.Multiset;
import constraints.Constraint;

/**
 * @author jwvl
 * @date Aug 9, 2015
 */
public class ViolatedCandidate {
    private final Multiset<Constraint> violated;
    private final Candidate candidate;

    public ViolatedCandidate(Multiset<Constraint> violated, Candidate candidate) {
        this.violated = violated;
        this.candidate = candidate;
    }

    @Override
    public String toString() {
        return "ViolatedCandidate [candidate=" + candidate + ", violated="
                + violated + "]";
    }

    /**
     * @return the Multiset of Constraints
     */
    public Multiset<Constraint> getConstraints() {
        return violated;
    }

    /**
     * @return
     */
    public Candidate getCandidate() {
        return candidate;
    }


    public String toPrettyString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(candidate).append("\n");
        for (Constraint c: violated.elementSet()) {
            stringBuilder.append("  ");
            stringBuilder.append(c).append(" x ").append(violated.count(c)).append("\n");
        }
        return stringBuilder.toString();
    }
}
