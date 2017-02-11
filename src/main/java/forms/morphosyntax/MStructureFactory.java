/**
 *
 */
package forms.morphosyntax;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.typesafe.config.ConfigFactory;

import java.util.*;

/**
 * A factory object for morphemic structures. Takes as input a head and
 * (underspecified) dep and outputs all possible realizations.
 *
 * @author jwvl
 * @date Dec 15, 2014
 */
public class MStructureFactory {
    private final ImmutableList<SyntacticWord> syntacticWordTemplates;
    private final boolean addNullValues = ConfigFactory.load().getBoolean("gen.nullValuesMSF");
    private final boolean addDisagreement = ConfigFactory.load().getBoolean("gen.disagreementMSF");


    private MStructureFactory(Collection<SyntacticWord> input) {
        syntacticWordTemplates = ImmutableList.copyOf(input);
        setHead();
    }

    public void setHead() {
        // Find head
        SyntacticWord result = null;
        for (SyntacticWord l : syntacticWordTemplates) {
            if (l.isHead()) {
                result = l;
            }
        }

        if (result == null) {
            System.out.println("Couldn't find head in " + syntacticWordTemplates);
        }

        // Impose head features unto dependent templates
        for (SyntacticWord l : syntacticWordTemplates) {
            if (!l.isHead())
                result.imposeAllFeatures(l);
        }
    }

    public static MStructureFactory createInstance(SemSynForm input) {
        List<SyntacticWord> copiedSyntacticWords = new ArrayList<SyntacticWord>(input.size());
        for (SyntacticWord l : input) {
            copiedSyntacticWords.add(l.copy());
        }
        MStructureFactory result = new MStructureFactory(copiedSyntacticWords);
        return result;
    }

    public static List<MStructure> lexemesToStructure(List<SyntacticWord> inputSyntacticWords) {
        MStructureFactory factoryObject = new MStructureFactory(inputSyntacticWords);
        return factoryObject.generateAllStructures();
    }

    public List<MStructure> generateAllStructures() {
        List<Set<SyntacticWord>> lexemeSet = Lists.newArrayList();
        List<MStructure> result = Lists.newArrayList();

        // Create possible realisations for all templates using cartesian
        // product
        for (SyntacticWord l : syntacticWordTemplates) {
            Set<SyntacticWord> cartesianSyntacticWordSet = new HashSet<SyntacticWord>(
                    syntacticWordTemplates.size());
            List<Set<MElement>> featureSets = getPossibleRealisations(l);
            Set<List<MElement>> cartesianFeatureSet = Sets
                    .cartesianProduct(featureSets);
            for (List<MElement> ls : cartesianFeatureSet) {
                SyntacticWord current = l.featurelessCopy();
                for (MElement mf : ls) {
                    current.addElement(mf);
                }
                cartesianSyntacticWordSet.add(current);
            }
            lexemeSet.add(cartesianSyntacticWordSet);
        }
        // Create possible combinations of templates using cartesian product

        Set<List<SyntacticWord>> combinations = Sets.cartesianProduct(lexemeSet);
        for (List<SyntacticWord> ll : combinations) {
            MStructure toAdd = MStructure.createInstance(ll);
            result.add(toAdd);
        }

        return result;
    }

    /**
     * @param l Lexeme (template) for which to extract feature values
     * @return
     */
    private List<Set<MElement>> getPossibleRealisations(SyntacticWord l) {
        ArrayList<Set<MElement>> result = Lists.newArrayList();
        for (MElement mf : l.getAllNonConceptFeatures()) {
            if (mf.getType() != MFeatureType.CONTEXTUAL) {
                result.add(Collections.singleton(mf));
            } else {
                Set<MElement> resultingSet = Sets.newHashSet();
                for (MElement mElement: mf.getRealizationSet()) {
                    if (!mElement.expressesValue()) {
                        if (addNullValues) {
                            resultingSet.add(mElement);
                        }
                    } else if (mElement.getFeature().valueEquals(mf.getFeature()) || addDisagreement) {
                        resultingSet.add(mElement);
                    }
                }
                result.add(resultingSet);
            }
        }
        result.trimToSize();
        return result;
    }
}
