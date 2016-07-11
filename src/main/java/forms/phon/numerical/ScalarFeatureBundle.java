package forms.phon.numerical;

import forms.ElementCollection;
import forms.primitives.Subform;
import forms.primitives.feature.ScaleFeature;
import util.string.CollectionPrinter;

import java.util.*;

/**
 * Created by janwillem on 07/07/16.
 */
public class ScalarFeatureBundle implements Subform, ElementCollection<PhoneticElement> {
    private final ScalarFeatureSpace space;
    private final int identifier;
    private ScaleFeature[] contents;

    public ScalarFeatureBundle(ScalarFeatureSpace space, int identifier) {
        this.space = space;
        this.identifier = identifier;
    }


    @Override
    public PhoneticElement[] elementsAsArray() {
        if (contents == null) {
            contents = space.decode(identifier);
        }
        return contents;
    }

    @Override
    public List<PhoneticElement> elementsAsList() {
        return Arrays.asList(elementsAsArray());
    }

    @Override
    public Set<PhoneticElement> elementsAsSet() {
        return new HashSet<>(elementsAsList());
    }

    @Override
    public int recursiveSize() {
        return space.getDimension();
    }

    @Override
    public int getIndexOf(PhoneticElement subform) {
        return getIndexOf(subform,0);
    }

    @Override
    public int getIndexOf(PhoneticElement subform, int startAt) {
        PhoneticElement[] contents = elementsAsArray();
        for (int i=startAt; i < recursiveSize(); i++) {
            if (contents[i] == subform)
                return i;
        }
        return -1;
    }

    @Override
    public String concatElementsToString(String separator) {
        return CollectionPrinter.collectionToString(elementsAsList(),separator);
    }

    @Override
    public boolean isNull() {
        return contents.length < 1;
    }

    @Override
    public int size() {
        return contents.length;
    }

    @Override
    public Iterator<PhoneticElement> iterator() {
        return elementsAsList().iterator();
    }
}
