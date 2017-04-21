/**
 *
 */
package forms.morphosyntax;

import util.collections.Couple;

import java.util.Objects;

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
        Attribute attribute = features.getLeft().getFeature().attribute;

        String leftString, rightString;
        if (lexemes.getLeft().isHead()) {
            dependentCat = lexemes.getRight().getSyntacticCategory();
            leftString = features.getLeft().getFeatureValue();
            rightString = features.getRight().getFeatureValue();
        } else {
            dependentCat = lexemes.getLeft().getSyntacticCategory();
            rightString = features.getLeft().getFeatureValue();
            leftString = features.getRight().getFeatureValue();
        }
        featureValues = Couple.of(leftString, rightString);
        affixType = AffixType.createInstance(dependentCat, attribute);

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
        return Objects.equals(featureValues, agreement.featureValues) &&
                Objects.equals(affixType, agreement.affixType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureValues, affixType);
    }
}
