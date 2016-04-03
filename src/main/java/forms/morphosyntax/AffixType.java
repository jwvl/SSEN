/**
 *
 */
package forms.morphosyntax;

import com.google.common.base.Objects;

/**
 * @author jwvl
 * @date Aug 1, 2015
 * TODO think of a better name.
 * Basically a Pair of SyntacticCategory and (MFeature) Attribute.
 * Since this pairing re-occurs often I conveniently put them into a single
 * struct-like object.
 */
public class AffixType {
    private final SyntacticCategory syntacticCategory;
    private final String attribute;

    /**
     * @param syntacticCategory
     * @param attribute
     */
    private AffixType(SyntacticCategory syntacticCategory, String attribute) {
        this.syntacticCategory = syntacticCategory;
        this.attribute = attribute;
    }

    public static AffixType createInstance(SyntacticCategory syntacticCategory, String attribute) {
        return new AffixType(syntacticCategory, attribute);
    }

    @Override
    public String toString() {

        return syntacticCategory.toString() + "-" + attribute;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((attribute == null) ? 0 : attribute.hashCode());
        result = prime
                * result
                + ((syntacticCategory == null) ? 0 : syntacticCategory
                .hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AffixType affixType = (AffixType) o;
        return syntacticCategory == affixType.syntacticCategory &&
                Objects.equal(attribute, affixType.attribute);
    }

    public SyntacticCategory getSyntacticCategory() {
        return syntacticCategory;
    }

    public String getAttribute() {
        return attribute;
    }


}
