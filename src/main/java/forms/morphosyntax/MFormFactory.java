/**
 *
 */
package forms.morphosyntax;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import util.collections.Superset;
import util.combinatorics.PartitionGenerator;
import util.string.CollectionPrinter;

import java.util.*;

/**
 * Factory to create different possible M-Forms from an M-Structure.
 *
 * @author jwvl
 * @date Dec 17, 2014
 */
public class MFormFactory {


    private MFormFactory() {
    }

    public static MFormFactory createInstance() {
        return new MFormFactory();
    }


    /**
     * Initializes factory.
     */
    private List<MForm> init(MStructure input) {
        List<MForm> result = Lists.newArrayList();
        ArrayList<Set<MBuilder>> orderingsPerLexeme = new ArrayList<Set<MBuilder>>(
                input.size());
        for (Lexeme l : input.getLexemes()) {
            List<MElement> lElements = l.getNonNullFeatures();
            Set<MElement> prePartitioned = new HashSet<MElement>(
                    lElements.size());
            // Pre-partition concept and fixed features
            for (int i = lElements.size() - 1; i >= 0; i--) {
                MElement curr = lElements.get(i);
                if (curr.getType() == MFeatureType.CONCEPT
                        || curr.getType() == MFeatureType.FIXED) {
                    lElements.remove(curr);
                    prePartitioned.add(curr);
                }
            }

            PartitionGenerator<MElement> pGen = new PartitionGenerator<MElement>(
                    lElements, prePartitioned);
            List<Superset<MElement>> unorderedPartitions = pGen.getPartitions();
            Set<MBuilder> orderings = makeMorphemePermutations(
                    unorderedPartitions, l);
            orderingsPerLexeme.add(orderings);
        }

        Set<List<MBuilder>> builderLists = Sets
                .cartesianProduct(orderingsPerLexeme);
        for (List<MBuilder> bl : builderLists) {

            MForm created = new MForm(joinToMorphologicalWords(bl));
            result.add(created);
        }
        return result;
    }


    public List<MForm> generateAllForms(MStructure input) {
        return ImmutableList.copyOf(init(input));
    }

    /**
     * @param supersets
     * @param l
     * @return
     */
    private Set<MBuilder> makeMorphemePermutations(
            List<Superset<MElement>> supersets, Lexeme l) {
        Set<MBuilder> result = Sets.newHashSet();
        for (Superset<MElement> superset : supersets) {
            Collection<List<Set<MElement>>> allPermutations = superset
                    .getPermutations();
            for (List<Set<MElement>> permutation : allPermutations) {
                MBuilder mList = new MBuilder(l.getSyntacticCategory());
                for (Set<MElement> elementSet : permutation) {
                    Morpheme toAdd = Morpheme.createInstance(elementSet, l.getSyntacticCategory());
                    mList.addMorpheme(toAdd);
                }
                result.add(mList);
            }

        }

        return result;
    }

    private static MorphologicalWord[] joinToMorphologicalWords(List<MBuilder> builders) {
        MorphologicalWord[] result = new MorphologicalWord[builders.size()];
        for (int i = 0; i < builders.size(); i++) {
            MBuilder mb = builders.get(i);
            result[i] = new MorphologicalWord(mb.syntacticCategory, mb.morphemes);
        }
        return result;
    }

    private class MBuilder {
        private List<Morpheme> morphemes;
        private SyntacticCategory syntacticCategory;

        public MBuilder(SyntacticCategory syntacticCategory) {
            this.morphemes = Lists.newArrayList();
            this.syntacticCategory = syntacticCategory;
        }

        public void addMorpheme(Morpheme m) {
            morphemes.add(m);
        }

        public String toString() {
            return CollectionPrinter.collectionToString(morphemes, " ");
        }
    }

}
