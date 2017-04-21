package forms.morphosyntax;

/**
 * Created by janwillem on 21/04/2017.
 */
public enum MFeatureAttribute {

    CONCEPT("CONCEPT"), NUM("num"),GEN("g");
    private final String string;

    MFeatureAttribute(String string) {
        this.string = string;
    }


}
