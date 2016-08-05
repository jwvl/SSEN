package grammar.dynamic.persistent;

import forms.Form;
import forms.FormChain;
import gen.mapping.FormMapping;
import grammar.dynamic.node.AbstractCostNode;
import constraints.SparseViolationProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by janwillem on 05/08/16.
 */
public class LinkedNode extends AbstractCostNode<LinkedNode> {
    private final SparseViolationProfile sparseViolationProfile;

    public LinkedNode(LinkedNode parent, FormMapping formMapping, SparseViolationProfile sparseViolationProfile) {
        super(parent, formMapping);
        this.sparseViolationProfile = sparseViolationProfile;
    }

    public int getDepth() {
        Form rightForm = getFormMapping().right();
        if (rightForm == null) {
            System.err.println("This is not right!");
        }
        return getFormMapping().right().getLevel().myIndex();
    }

    public short[] getLeftmostDifference(LinkedNode other) {
        return sparseViolationProfile.getLeftmostDifference(other.sparseViolationProfile);
    }

    @Override
    public String toString() {
        return printFull()+"\n  "+sparseViolationProfile.toString();
        //return "LN: " + getFormMapping().right() + " " + sparseViolationProfile;
    }

    public String printFull() {
        return toFormChain().toString();
    }

    public FormChain toFormChain() {
        List<FormMapping> result = addUntilRoot(new ArrayList<FormMapping>());
        Collections.reverse(result);
        return FormChain.createFromMappings(result);
    }

    private List<FormMapping> addUntilRoot(List<FormMapping> list) {
        if (getParent() == null) {
            return list;
        } else {
            list.add(getFormMapping());
            return getParent().addUntilRoot(list);
        }
    }


}
