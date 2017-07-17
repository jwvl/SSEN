/**
 *
 */
package forms.morphosyntax;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import forms.ElementCollection;
import forms.primitives.Subform;
import forms.primitives.feature.AbstractMFeature2;
import util.string.CollectionPrinter;

import java.util.*;

/**
 * A Syntactic Word (S-word) represents a 'concept' plus a set of semantic and morphosyntactic
 * features (MFeatures). It must belong to a Syntactic Category and is either a
 * head or a dependent within a phrase.
 *
 * @author jwvl
 * @date Dec 9, 2014
 */
public class SyntacticWord implements Subform, ElementCollection<MElement> {

    private final SyntacticCategory category;
    private AgreementType type;
    private MElement concept;
    private Set<MElement> morphologicalElements;
    private String asString;

    /**
     * Private constructor.
     */
    private SyntacticWord(SyntacticCategory category) {
        this.category = category;
        morphologicalElements = new HashSet<MElement>();
    }

    /**
     * Adds both inherent and optional features of this S-word as CONTEXTUAL
     * features to a receiving Lexeme.
     *
     * @param receiver
     */
    public void imposeAllFeatures(SyntacticWord receiver) {
        for (MElement f : morphologicalElements) {
            MElement copiedElement = f.contextualCopy();
            receiver.morphologicalElements.add(copiedElement);
        }
    }

    /**
     * Creates a S-word from a String representation. See inside method for
     * details on the syntax of these representations. TODO Make more robust to
     * errors in string.
     *
     * @param s String to parse
     * @return A new Lexeme object parsed from this String.
     */
    public static SyntacticWord parseFromString(String s) {
        SyntacticWord result;
        List<String> firstSplit = Splitter.on("{").splitToList(s);
        // Category is everything before bracket
        String categoryString = firstSplit.get(0);
        result = new SyntacticWord(SyntacticCategory.getInstance(categoryString));
        String rest = firstSplit.get(1);
        // If closing bracket is followed by star, it's a head
        if (rest.endsWith("*"))
            result.type = AgreementType.HEAD;
        else
            result.type = AgreementType.DEPENDENT;
        // Now check contents
        String contents = Splitter.on("}").splitToList(rest).get(0);
        List<String> ingredients = Splitter.on(" ").splitToList(contents);
        // First element is concept plus fixed features separated by dots
        String firstElement = ingredients.get(0);
        Iterator<String> firstElementContents = Splitter.on(".").split(firstElement).iterator();
        String conceptString = firstElementContents.next();
        result.concept = MElement.createConcept(conceptString);
        while (firstElementContents.hasNext()) {
            AbstractMFeature2 parsed = parseAttributeAndValue(firstElementContents.next());
            result.addElement(MElement.getInstance(parsed, MFeatureType.FIXED));
        }

        // Rest are selected features
        for (int i = 1; i < ingredients.size(); i++) {
            String currentFeature = ingredients.get(i);
            AbstractMFeature2 parsed = parseAttributeAndValue(currentFeature);
            result.addElement(MElement.getInstance(parsed, MFeatureType.SELECTED));
        }

        result.printOverview();
        return result;

    }

    /**
     * Prints some details of this Lexeme.
     */
    private void printOverview() {
        System.out.println("*S-Word*");
        System.out.println("Agreement type: " + type);
        System.out.println("Category: " + category);
        System.out.println("Concept: " + concept);
        System.out.println("Features:");
        CollectionPrinter.print(morphologicalElements);
    }

    /**
     * Parses an attribute and value into an MFeature. TODO Maybe belongs in that
     * class...?
     *
     * @param s String to parse
     * @return The MFeature parsed from the string
     */
    private static AbstractMFeature2 parseAttributeAndValue(String s) {
        List<String> contents = Splitter.on("=").splitToList(s);
        if (contents.size() < 2) {
            System.err.println("Cannot parse " + s);
        }
        return AbstractMFeature2.getMorphologicalFeature(contents.get(0), contents.get(1));
    }

    /**
     * Adds an MFeature to this Lexeme.
     *
     * @param feature MFeature to add.
     */
    protected void addElement(MElement feature) {
        morphologicalElements.add(feature);
        asString = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.SubForm#isNullElement()
     */
    @Override
    public boolean isNull() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.SubForm#getNumSteps()
     */
    @Override
    public int size() {
        return 1 + morphologicalElements.size();
    }


    public String toString() {
        if (asString == null) {
            asString = generateString();
        }
        return asString;
    }

    public String generateString() {
        StringBuffer result = new StringBuffer("");
        result.append(category);
        result.append('{');
        result.append(concept);
        List<MElement> sorted = Lists.newArrayList(morphologicalElements);
        Collections.sort(sorted);
        for (MElement mf : sorted) {
            String appendString = "";
            if (mf.getType() == MFeatureType.FIXED) {
                appendString = ".";
            } else if (mf.getType() == MFeatureType.SELECTED)
                appendString = " ";
            result.append(appendString);
            result.append(mf.toString());
        }
        result.append('}');
        if (this.type == AgreementType.HEAD)
            result.append('*');
        return result.toString();
    }

    public SyntacticWord featurelessCopy() {
        SyntacticWord result = new SyntacticWord(this.category);
        result.concept = this.concept;
        result.type = this.type;
        return result;
    }

    public List<MElement> getNonNullFeatures() {
        List<MElement> result = Lists.newArrayList();
        result.add(concept);
        result.addAll(getAllExpressedFeatures());
        return result;
    }

    protected Set<MElement> getAllNonConceptFeatures() {
        return morphologicalElements;
    }

    protected Set<MElement> getAllExpressedFeatures() {
        Set<MElement> result = new HashSet<MElement>(morphologicalElements.size());
        for (MElement m : morphologicalElements) {
            if (!m.getFeature().isNull()) {
                result.add(m);
            }
        }
        return result;
    }

    protected boolean isHead() {
        return type == AgreementType.HEAD;
    }

    /**
     * @return The SyntacticCategory of this Lexeme.
     */
    public SyntacticCategory getSyntacticCategory() {
        return category;
    }

    /**
     * @return The Concept feature of this Lexeme.
     */
    protected MElement getConcept() {
        return concept;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SyntacticWord mElements = (SyntacticWord) o;
        return category == mElements.category &&
                type == mElements.type &&
                Objects.equals(concept, mElements.concept) &&
                Objects.deepEquals(morphologicalElements, mElements.morphologicalElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, type, concept, morphologicalElements);
    }

    /*
         * (non-Javadoc)
         *
         * @see java.lang.Iterable#iterator()
         */
    @Override
    public Iterator<MElement> iterator() {
        return morphologicalElements.iterator();
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsList()
     */
    @Override
    public List<MElement> elementsAsList() {
        // TODO Auto-generated method stub
        return new ArrayList<MElement>(morphologicalElements);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsSet()
     */
    @Override
    public Set<MElement> elementsAsSet() {

        return ImmutableSet.copyOf(morphologicalElements);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#recursiveSize()
     */
    @Override
    public int recursiveSize() {
        return size();
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#getIndexOf(forms.primitives.Subform)
     */
    @Override
    public int getIndexOf(MElement element) {
        return getIndexOf(element, 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#concatElementsToString(java.lang.String)
     */
    @Override
    public String concatElementsToString(String separator) {
        return CollectionPrinter.collectionToString(morphologicalElements, separator);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#getIndexOf(forms.primitives.Subform, int)
     */
    @Override
    public int getIndexOf(MElement subform, int startAt) {
        List<MElement> listToSearch = elementsAsList();
        for (int i = startAt; i < listToSearch.size(); i++) {
            if (listToSearch.get(i).equals(subform)) {
                return i;
            }
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#elementsAsArray()
     */
    @Override
    public MElement[] elementsAsArray() {
        return morphologicalElements.toArray(new MElement[size()]);
    }

    /**
     * @return A Copy of this Lexeme.
     */
    public SyntacticWord copy() {
        SyntacticWord result = new SyntacticWord(this.category);
        result.concept = this.concept;
        result.type = this.type;
        result.morphologicalElements = Sets.newHashSet(this.morphologicalElements);
        return result;
    }

}
