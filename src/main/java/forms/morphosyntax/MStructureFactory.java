/**
 *
 */
package forms.morphosyntax;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * A factory object for morphemic structures. Takes as input a head and
 * (underspecified) dep and outputs all possible realizations.
 *
 * @author jwvl
 * @date Dec 15, 2014
 */
public class MStructureFactory {
    private final ImmutableList<Lexeme> lexemeTemplates;

    private MStructureFactory(Collection<Lexeme> input) {
        lexemeTemplates = ImmutableList.copyOf(input);
        setHead();
    }

    public void setHead() {
        // Find head
        Lexeme result = null;
        for (Lexeme l : lexemeTemplates) {
            if (l.isHead())
                result = l;
        }

        // Impose head features unto dependent templates
        for (Lexeme l : lexemeTemplates) {
            if (!l.isHead())
                result.imposeAllFeatures(l);
        }
    }

    public static MStructureFactory createInstance(SemSynForm input) {
        List<Lexeme> copiedLexemes = new ArrayList<Lexeme>(input.size());
        for (Lexeme l : input.elementsAsList()) {
            copiedLexemes.add(l.copy());
        }
        MStructureFactory result = new MStructureFactory(copiedLexemes);
        return result;
    }

    public static List<MStructure> lexemesToStructure(List<Lexeme> inputLexemes) {
        MStructureFactory factoryObject = new MStructureFactory(inputLexemes);
        return factoryObject.generateAllStructures();
    }

    public List<MStructure> generateAllStructures() {
        List<Set<Lexeme>> lexemeSet = Lists.newArrayList();
        List<MStructure> result = Lists.newArrayList();

        // Create possible realisations for all templates using cartesian
        // product
        for (Lexeme l : lexemeTemplates) {
            Set<Lexeme> cartesianLexemeSet = new HashSet<Lexeme>(
                    lexemeTemplates.size());
            List<Set<MElement>> featureSets = getPossibleRealisations(l);
            Set<List<MElement>> cartesianFeatureSet = Sets
                    .cartesianProduct(featureSets);
            for (List<MElement> ls : cartesianFeatureSet) {
                Lexeme current = l.featurelessCopy();
                for (MElement mf : ls) {
                    current.addElement(mf);
                }
                cartesianLexemeSet.add(current);
            }
            lexemeSet.add(cartesianLexemeSet);
        }
        // Create possible combinations of templates using cartesian product

        Set<List<Lexeme>> combinations = Sets.cartesianProduct(lexemeSet);
        for (List<Lexeme> ll : combinations) {
            MStructure toAdd = MStructure.createInstance(ll);
            result.add(toAdd);
        }

        return result;
    }

    /**
     * @param l Lexeme (template) for which to extract feature values
     * @return
     */
    private List<Set<MElement>> getPossibleRealisations(Lexeme l) {
        ArrayList<Set<MElement>> result = Lists.newArrayList();
        for (MElement mf : l.getAllNonConceptFeatures()) {
            if (mf.getType() == MFeatureType.FIXED) {
                result.add(Collections.singleton(mf));
            } else {
                result.add(mf.getRealizationSet());
            }
        }
        result.trimToSize();
        return result;
    }
}
