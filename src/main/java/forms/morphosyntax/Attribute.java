package forms.morphosyntax;

/**
 * Created by janwillem on 21/04/2017.
 */
public enum Attribute {

    CONCEPT("CONCEPT"), NUM("num"),G("g");
    private final String string;

    Attribute(String string) {
        this.string = string;
    }

    public String toString() {
        return string;
    }


}
