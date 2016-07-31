/**
 *
 */
package forms;

import gen.mapping.FormMapping;
import gen.mapping.PairMapping;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author jwvl
 * @date 18/10/2014 Basically a wrapper for an array of Forms. Often a FormTuple
 * will form the contents of a Candidate.
 */
public class FormChain {
    protected final Form[] contents;
    protected final FormMapping[] pairs;


    public static FormChain createFromMappings(List<FormMapping> mappings) {
        return new FormChain(mappings.toArray(new FormMapping[mappings.size()]));

    }

    public static FormChain fromFormstoSimpleMappings(Collection<Form> forms) {
        FormMapping[] asMappings = new FormMapping[forms.size() - 1];
        Iterator<Form> iterator = forms.iterator();
        Form a = iterator.next();
        int count = 0;
        while (iterator.hasNext()) {
            Form b = iterator.next();
            FormMapping toAdd = PairMapping.createInstance(a, b);
            a = b;
            asMappings[count++] = toAdd;
        }
        return new FormChain(asMappings);
    }

    protected FormChain(FormMapping[] pairs) {
        this.pairs = pairs;
        this.contents = calculateContents(pairs);
    }


    public String contentsToBracketedString() {
        return contentsToBracketedString(" ");
    }

    public String contentsToBracketedString(String separator) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            result.append(contents[i].toBracketedString());
            if (i < size() - 1)
                result.append(separator);
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof FormChain))
            return false;
        FormChain other = (FormChain) obj;
        if (!Arrays.deepEquals(contents, other.contents))
            return false;
        return Arrays.deepEquals(pairs, other.pairs);
    }

    /**
     * @return the contents
     */
    public Form[] getContents() {
        return contents;
    }

    /**
     * @return
     */
    public FormMapping[] getMappings() {
        return pairs;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(contents);
        return result;
    }

    public int size() {
        return contents.length;
    }

    private Form[] calculateContents(FormMapping[] mappings) {
        Form[] result = new Form[mappings.length + 1];
        result[0] = mappings[0].left();
        for (int i = 0; i < mappings.length; i++) {
            result[i + 1] = mappings[i].right();
        }
        return result;

    }

    @Override
    public String toString() {
        return this.contentsToBracketedString(" → ");
    }


}
