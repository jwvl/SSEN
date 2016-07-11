package grammar.dynamic;

import com.typesafe.config.ConfigFactory;
import forms.Form;
import gen.mapping.FormMapping;
import gen.mapping.PairMapping;
import grammar.dynamic.node.LinkedIndexNode;
import ranking.IndexedSampledHierarchy;
import ranking.constraints.helper.ConstraintArrayList;
import ranking.violations.vectors.BitViolationVector;

import java.util.PriorityQueue;

/**
 * Created by janwillem on 02/04/16.
 */
public class LinkedIndexNodeSearcher extends AbstractNodeSearcher<LinkedIndexNode>{
    private final IndexedSampledHierarchy hierarchy;
    private final int maxConstraintViolations = ConfigFactory.load().getInt("implementation.expectedMaxViolations");
    private final int expectedNumConstraints = ConfigFactory.load().getInt("implementation.expectedNumConstraints");

    public LinkedIndexNodeSearcher(IndexedSampledHierarchy hierarchy) {
        super(new PriorityQueue<LinkedIndexNode>());
        this.hierarchy = hierarchy;
    }

    @Override
    public LinkedIndexNode getInitial(Form initialForm) {
        FormMapping newMapping = PairMapping.createInstance(null, initialForm);
        BitViolationVector emptyVector = new BitViolationVector(expectedNumConstraints,maxConstraintViolations);
        return new LinkedIndexNode(null, newMapping,emptyVector);

    }

    @Override
    public LinkedIndexNode getSuccessor(LinkedIndexNode parent, FormMapping formMapping, ConstraintArrayList constraintList) {
        BitViolationVector emptyVector = new BitViolationVector(expectedNumConstraints,maxConstraintViolations);
        emptyVector.addConstraints(constraintList,hierarchy);
        return new LinkedIndexNode(parent, formMapping, emptyVector);
    }
}
