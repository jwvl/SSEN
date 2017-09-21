/**
 *
 */
package grammar.dynamic;

import candidates.FormInput;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import constraints.helper.ConstraintArrayList;
import constraints.hierarchy.reimpl.Hierarchy;
import eval.Evaluation;
import eval.harmony.CostFactory;
import eval.harmony.CostType;
import eval.sample.AbstractSampler;
import eval.sample.XoroShiroSampler;
import forms.Form;
import forms.FormPair;
import forms.GraphForm;
import gen.mapping.FormMapping;
import gen.mapping.SubCandidateSet;
import gen.subgen.SubGen;
import grammar.dynamic.node.AbstractCostNode;
import grammar.dynamic.node.SearchMode;
import grammar.dynamic.node.hg.HarmonicNodeSearcher;
import grammar.levels.Level;
import learn.ViolatedCandidate;
import learn.ViolatedCandidateBuilder;

/**
 * @author jwvl
 * @date Aug 9, 2015
 */
public class DynamicNetworkEvaluation implements Evaluation {
    private static final Config config = ConfigFactory.load();
    private DynamicNetworkGrammar grammar;
    private long id;
    private static long idCounter = 0;
    private static boolean sampleLazy = config.getBoolean("implementation.lazySampling");
    private FormPair formPair;
    private ViolatedCandidate winner;
    private Hierarchy sampledHierarchy;
    private boolean endFormIsSink = false;
    private boolean useCandidateSpaces = config.getBoolean("grammar.useCandidateSpaces");
    private final CostFactory costFactory;
    private final AbstractNodeSearcher nodeSearcher;

    public DynamicNetworkEvaluation(DynamicNetworkGrammar dynamicNetworkGrammar, double evaluationNoise, FormPair input) {
        this(dynamicNetworkGrammar,
                sampleHierarchyForInput(dynamicNetworkGrammar,input, evaluationNoise));
    }

    public DynamicNetworkEvaluation(DynamicNetworkGrammar dynamicNetworkGrammar,
                                    Hierarchy sampledHierarchy) {
        this.id = idCounter++;
        this.grammar = dynamicNetworkGrammar;
        this.sampledHierarchy = sampledHierarchy;
        costFactory = new CostFactory(sampledHierarchy, CostType.SIMPLE_DOUBLES_OT);

        String searchModeString = config.getString("implementation.nodeSearch");
        SearchMode searchMode = SearchMode.valueOf(searchModeString);

        switch (searchMode) {
            case DYNAMIC:
                nodeSearcher = new DynamicNodeSearcher(dynamicNetworkGrammar, costFactory);
                break;
            case SIMPLE:
                nodeSearcher = new SimpleNodeSearcher(costFactory);
                break;
            case LINKED_NODE:
                nodeSearcher = new LinkedNodeSearcher(sampledHierarchy);
                break;
            case HARMONIC_GRAMMAR:
                nodeSearcher = new HarmonicNodeSearcher(sampledHierarchy);
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
            System.err.println("No winner found for "+ formPair.left() +" -- "+ formPair.right());
            System.exit(0);
        }

        ViolatedCandidate backtrackedCandidate = backTrackCandidate(winnerNode);
        if (backtrackedCandidate.getCandidate().getForms().length < grammar.getLevelSpace().getSize()) {
            System.out.println(backtrackedCandidate);
            System.err.println("This should not happen!");
            System.exit(0);
        }
       // System.out.printf("%d nodes in queue \n",nodeSearcher.getQueueSize());
        winner = backtrackedCandidate;
//        if (nodeSearcher instanceof  LinkedNodeSearcher) {
//            System.out.println("Total number of nodes opened: " +((LinkedNodeSearcher) nodeSearcher).getSize());
//        }
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
    public Hierarchy getSampledHierarchy() {
        return sampledHierarchy;
    }

    private static Hierarchy sampleHierarchyForInput(DynamicNetworkGrammar grammar, FormPair formPair, double evaluationNoise) {
        Hierarchy hierarchy = grammar.getHierarchy();
        AbstractSampler sampler = new XoroShiroSampler(evaluationNoise);
        if (sampleLazy) {
            return hierarchy.sampleLazy2(grammar.getViolatingConstraintCache(), sampler, formPair);
        } else {
            return hierarchy.sample(sampler);
        }
    }

}
