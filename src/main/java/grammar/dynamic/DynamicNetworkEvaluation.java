/**
 *
 */
package grammar.dynamic;

import candidates.FormInput;
import com.typesafe.config.ConfigFactory;
import eval.Evaluation;
import eval.harmony.CostFactory;
import eval.harmony.CostType;
import eval.sample.GaussianXORSampler;
import forms.Form;
import forms.FormPair;
import forms.GraphForm;
import gen.mapping.FormMapping;
import gen.mapping.SubCandidateSet;
import gen.subgen.SubGen;
import grammar.dynamic.node.AbstractCostNode;
import grammar.dynamic.node.SearchMode;
import grammar.levels.Level;
import learn.ViolatedCandidate;
import learn.ViolatedCandidateBuilder;
import ranking.DynamicSampledHierarchy;
import ranking.IndexedSampledHierarchy;
import ranking.constraints.helper.ConstraintArrayList;

/**
 * @author jwvl
 * @date Aug 9, 2015
 */
public class DynamicNetworkEvaluation implements Evaluation {
    private DynamicNetworkGrammar grammar;
    private long id;
    private static long idCounter = 0;
    private FormPair formPair;
    private ViolatedCandidate winner;
    private DynamicSampledHierarchy sampledHierarchy;
    private boolean endFormIsSink = false;
    private boolean useCandidateSpaces = ConfigFactory.load().getBoolean("grammar.useCandidateSpaces");
    private final CostFactory costFactory;
    private final AbstractNodeSearcher nodeSearcher;

    public DynamicNetworkEvaluation(DynamicNetworkGrammar dynamicNetworkGrammar, double evaluationNoise) {
        this(dynamicNetworkGrammar,
                new DynamicSampledHierarchy(dynamicNetworkGrammar.getRankedCon(), GaussianXORSampler.createInstance(evaluationNoise)));
    }

    public DynamicNetworkEvaluation(DynamicNetworkGrammar dynamicNetworkGrammar,
                                    DynamicSampledHierarchy sampledHierarchy) {
        this.id = idCounter++;
        this.grammar = dynamicNetworkGrammar;
        this.sampledHierarchy = sampledHierarchy;
        costFactory = new CostFactory(sampledHierarchy, CostType.SIMPLE_DOUBLES_OT);

        String searchModeString = ConfigFactory.load().getString("implementation.nodeSearch");
        SearchMode searchMode = SearchMode.valueOf(searchModeString);

        switch (searchMode) {
            case DYNAMIC:
                nodeSearcher = new DynamicNodeSearcher(dynamicNetworkGrammar, costFactory);
                break;
            case SIMPLE:
                nodeSearcher = new SimpleNodeSearcher(costFactory);
                break;
            case INDEXED:
                IndexedSampledHierarchy indexed = sampledHierarchy.toIndexedSampledHierarchy();
                nodeSearcher = new IndexedNodeSearcher(indexed);
                break;
            case LINKED_INDEXED:
                indexed = sampledHierarchy.toIndexedSampledHierarchy();
                nodeSearcher = new LinkedIndexNodeSearcher(indexed);
                break;
            default:
                nodeSearcher = new SimpleNodeSearcher(costFactory);
        }

    }


    public long getId() {
        return id;
    }

    public void run() {

        nodeSearcher.init(formPair.left());

        AbstractCostNode winnerNode = null;
        boolean winnerFound = false;
        while (!winnerFound && nodeSearcher.canExpand()) {
            AbstractCostNode nodeToExpand = nodeSearcher.nextNode();
            Form formToExpand = nodeToExpand.getMappedForm();

            if (formToExpand.getLevel() == getEndForm().getLevel()) {

                if (formToExpand.equals(getEndForm())) {
                    winnerFound = true;
                    winnerNode = nodeToExpand;
                }
            } else if (formToExpand.getLevel().isRightmost() && endFormIsSink) {
                winnerFound = true;
                winnerNode = nodeToExpand;
            } else {
                SubCandidateSet subCandidateSet = getFormMappings(formToExpand);

                if (useCandidateSpaces) {
                    if (!formPair.isUnlabeled()) {
                        subCandidateSet = grammar.getCandidateSpaces().filterSet(formPair, subCandidateSet);
                    }
                }

                for (FormMapping formMapping : subCandidateSet) {
                    ConstraintArrayList constraints = getConstraintsForMapping(formMapping);
                    nodeSearcher.generateSuccessor(nodeToExpand, formMapping, constraints);
                }
            }
        }
        if (!winnerFound) {
            System.err.println("No winner found for " + formPair.right());
            System.exit(0);
        }

        ViolatedCandidate backtrackedCandidate = backTrackCandidate(winnerNode);
        if (backtrackedCandidate.getCandidate().getForms().length < 5) {
            System.out.println(backtrackedCandidate);
            System.err.println("This should not happen!");
            System.exit(0);
        }

        winner = backtrackedCandidate;
        nodeSearcher.clearQueue();

    }

    private SubCandidateSet getFormMappings(Form toExpand) {
        Level formLevel = toExpand.getLevel();
        SubGen<?, ?> subGen = grammar.getSubGenForLevel(formLevel);
        if (subGen == null) {
            return SubCandidateSet.EMPTY;
        } else {
            return subGen.generateRight(toExpand);
        }
    }

    private ViolatedCandidate backTrackCandidate(AbstractCostNode winnerNode) {
        ViolatedCandidateBuilder builder = new ViolatedCandidateBuilder();
        AbstractCostNode current = winnerNode;
        AbstractCostNode next = current.getParent();

        while (next != null) {
            builder.addForm(current.getMappedForm());
            ConstraintArrayList constraints = getConstraintsForMapping(current.getFormMapping());
            builder.addConstraintsFromList(constraints);
            current = next;
            next = current.getParent();
        }
        builder.addForm(current.getMappedForm());
        ConstraintArrayList constraints = getConstraintsForMapping(current.getFormMapping());
        builder.addConstraintsFromList(constraints);

        return builder.build(FormInput.createInstance(current.getMappedForm()), true);
    }

    private ConstraintArrayList getConstraintsForMapping(FormMapping formMapping) {
        if (formMapping.left() == null) {
            return ConstraintArrayList.EMPTY;
        }
        SubGen<?, ?> subGen = grammar.getSubGenForLevel(formMapping.left().getLevel());
        return subGen.getAssociatedConstraints(formMapping);
    }


    public void setStartAndEndForm(FormPair formPair) {
        this.formPair = formPair;
        endFormIsSink = (formPair.right() == GraphForm.getSinkInstance());
    }


    public Form getEndForm() {
        return formPair.right();
    }


    public ViolatedCandidate getWinner() {
        return winner;
    }

    /**
     * @return
     */
    public DynamicNetworkGrammar getGrammar() {
        return grammar;
    }

    /**
     * @return
     */
    public DynamicSampledHierarchy getSampledHierarchy() {
        return sampledHierarchy;
    }

}
