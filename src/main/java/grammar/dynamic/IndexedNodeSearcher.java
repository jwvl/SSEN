package grammar.dynamic;

import com.typesafe.config.ConfigFactory;
import forms.Form;
import gen.mapping.FormMapping;
import gen.mapping.PairMapping;
import grammar.dynamic.node.IndexedCostNode;
import ranking.IndexedSampledHierarchy;
import ranking.constraints.helper.ConstraintArrayList;
import ranking.violations.vectors.ShortViolationVector;
import ranking.violations.vectors.ViolationVector;

import java.util.PriorityQueue;

/**
 * Created by janwillem on 02/04/16.
 */
public class IndexedNodeSearcher extends AbstractNodeSearcher<IndexedCostNode> {
    private final IndexedSampledHierarchy hierarchy;
    private final int maxConstraintViolations = ConfigFactory.load().getInt("implementation.expectedMaxViolations");
    private final int expectedNumConstraints = ConfigFactory.load().getInt("implementation.expectedNumConstraints");

    public IndexedNodeSearcher(IndexedSampledHierarchy hierarchy) {
        super(new PriorityQueue<IndexedCostNode>());
        this.hierarchy = hierarchy;
    }

    @Override
    public IndexedCostNode getInitial(Form initialForm) {
        FormMapping newMapping = PairMapping.createInstance(null, initialForm);
     //   ViolationVector emptyVector = MinMaxViolationVector.create(maxConstraintViolations,expectedNumConstraints);
        ViolationVector emptyVector = ShortViolationVector.create();
        return new IndexedCostNode(null, newMapping,emptyVector);

    }

    @Override
    public IndexedCostNode getSuccessor(IndexedCostNode parent, FormMapping formMapping, ConstraintArrayList constraintList) {
        ViolationVector copiedVector = parent.getCost().copy();
        copiedVector.addConstraints(constraintList,hierarchy);
        return new IndexedCostNode(parent, formMapping, copiedVector);
    }
}
