/**
 *
 */
package forms;

import com.google.common.collect.Ordering;
import forms.primitives.Subform;
import grammar.levels.Level;

/**
 * Quoting from my thesis: For the purposes of \textit{computation} or
 * \textit{evaluation}, Forms are the primitives of the framework: both
 * constraints and candidates will be defined [...] over forms or combinations
 * of forms. In terms of _representation_ or _generation_, forms are not
 * primitives: rather, they are usually composed of smaller numerical or
 * symbolic elements. The interlevel and intralevel constraints seen so far
 * often referred to these smaller elements and their relations, but only
 * implicitly. In fact, no explicit formalization of the possible contents of a
 * form will be attempted within this thesis. MLCG forms are an _abstract type_,
 * to borrow an object-oriented programming term: their concrete instantiation
 * depends on the representational preferences of a given theory, as well as the
 * specific linguistic phenomenon being modeled. These specifics are
 * _encapsulated_ from the point of view of _Eval_ and the learning and parsing
 * algorithms it informs.
 *
 * @author jwvl
 * @date 18/10/2014
 */
public interface Form {


    Ordering<Form> SizeComparator = new Ordering<Form>() {
        @Override
        public int compare(Form o1, Form o2) {
            int fieldsCompare = o1.getNumSubForms() - o2.getNumSubForms();
            if (fieldsCompare == 0)
                return o1.toString().compareTo(o2.toString());
            return fieldsCompare;
        }
    };
    Ordering<Form> StringComparator = new Ordering<Form>() {

        @Override
        public int compare(Form o1, Form o2) {
            return o1.toString().compareTo(o2.toString());
        }

    };

    int hashCode();

    boolean equals(Object obj);

    /**
     * @return the Level
     */
    Level getLevel();

    int getLevelIndex();

    int getNumSubForms();

    String toBracketedString();

    String toString();

    int countSubform(Subform sf);


}
