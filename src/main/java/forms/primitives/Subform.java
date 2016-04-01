package forms.primitives;


/**
 * Abstract class for 'subforms'. Maybe define more common methods
 * in the future? For now just serves to unite things that can serve as
 * primitives of forms, and to make implementations of
 * hashCode() and equals() obligatory in those classes.
 *
 * @author Jan-Willem van Leussen, Nov 12, 2014
 */
public abstract class Subform {

    public static Subform getNull() {
        return NullSubform.getInstance();
    }

    public abstract boolean equals(Object o);

    public abstract int hashCode();

    public abstract boolean isNull();

    public abstract int size();

    @Override
    public abstract String toString();


}
