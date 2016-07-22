/**
 *
 */
package forms.morphosyntax;

import util.collections.Couple;

/**
 * An Agreement object stores a link between two M-features and can be
 * questioned w.r.t whether they correspond. values correspond decides whether
 * the two features are in agreement.
 *
 * @author jwvl
 * @date Dec 14, 2014
 */
public class Agreement {
    private final Couple<String> featureValues;
    private final AffixType affixType;

    private Agreement(Couple<MElement> features, Couple<SyntacticWord> lexemes) {
        SyntacticCategory dependentCat;
        String attributeString = features.getLeft().getFeature().getAttribute();

        String leftString, rightString;
        if (lexemes.getLeft().isHead()) {
            dependentCat = lexemes.getRight().getSyntacticCategory();
            leftString = features.getLeft().getFeature().getValue();
            rightString = features.getRight().getFeature().getValue();
        } else {
            dependentCat = lexemes.getLeft().getSyntacticCategory();
            rightString = features.getLeft().getFeature().getValue();
            leftString = features.getRight().getFeature().getValue();
        }
        featureValues = Couple.of(leftString, rightString);
        affixType = AffixType.createInstance(dependentCat, attributeString);

    }

    public static Agreement between(MElement a, MElement b, SyntacticWord l, SyntacticWord r) {
        return new Agreement(Couple.of(a, b), Couple.of(l, r));
    }

    public static Agreement createInstance(Couple<MElement> couple,
                                           Couple<SyntacticWord> lexemes) {
        return new Agreement(couple, lexemes);
    }

    public boolean valuesAgree() {
        return featureValues.getLeft().equals(featureValues.getRight());
    }


    @Override
    public String toString() {
        return "Agreement [dependentCat=" + affixType.getSyntacticCategory() + ", featureValues="
                + featureValues + ", attributeString=" + affixType.getAttribute() + "]";
    }


    public AffixType getAffixType() {
        return affixType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agreement agreement = (Agreement) o;

        if (featureValues != null ? !featureValues.equals(agreement.featureValues) : agreement.featureValues != null)
            return false;
        return affixType != null ? affixType.equals(agreement.affixType) : agreement.affixType == null;

    }

    @Override
    public int hashCode() {
        int result = featureValues != null ? featureValues.hashCode() : 0;
        result = 31 * result + (affixType != null ? affixType.hashCode() : 0);
        return result;
    }
}
