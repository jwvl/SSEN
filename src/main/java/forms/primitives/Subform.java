package forms.primitives;


/**
 * Abstract class for 'subforms'. Maybe define more common methods
 * in the future? For now just serves to unite things that can serve as
 * primitives of forms, and to make implementations of
 * hashCode() and equals() obligatory in those classes.
 *
 * @author Jan-Willem van Leussen, Nov 12, 2014
 */
public interface Subform {
    
     boolean equals(Object o);

     int hashCode();

     boolean isNull();

     int size();

    @Override
     String toString();


}
