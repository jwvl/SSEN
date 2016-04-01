package ranking;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A Label is used to distinguish different types of constraints.
 * The class keeps track of all labels generated in a static Set.
 *
 * @author Jan-Willem van Leussen, Jan 15, 2015
 */
public class FamilyLabel {
    String label;


    public static Set<FamilyLabel> allLabels = new LinkedHashSet<FamilyLabel>();

    public static FamilyLabel createInstance(String label) {
        FamilyLabel result = new FamilyLabel(label);
        allLabels.add(result);
        return result;
    }

    public static void destroyLabel(FamilyLabel f) {
        allLabels.remove(f);
        // Null it for good measure
        f = null;
    }

    private FamilyLabel(String label) {
        super();
        this.label = label;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FamilyLabel other = (FamilyLabel) obj;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        return true;
    }


}
