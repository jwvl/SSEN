package grammar.dynamic.persistent;

import constraints.SparseViolationProfile;
import forms.FormChain;
import gen.mapping.FormMapping;
import grammar.dynamic.node.AbstractCostNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by janwillem on 05/08/16.
 */
public class LinkedNode extends AbstractCostNode<LinkedNode> {
    private final short[] sparseViolationProfile;
    private final short worstViolator;
    public final byte depth;

    public short getWorstViolator() {
        return worstViolator;
    }

    public short[] getViolationProfile() {
        return sparseViolationProfile;
    }

    public LinkedNode(LinkedNode parent, FormMapping formMapping, short[] sparseViolationProfile, short previousHighestViolator) {
        super(parent, formMapping);
        this.sparseViolationProfile = sparseViolationProfile;
        if (parent == null) {
            this.depth = 0;
        }
        else {
            this.depth = (byte) (parent.depth + 1);
        }
        short thisHighestViolator = sparseViolationProfile.length > 0 ? sparseViolationProfile[0] : Short.MAX_VALUE;
        this.worstViolator = previousHighestViolator < thisHighestViolator ? previousHighestViolator : thisHighestViolator;
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

    public String toStringRecursive() {
        short[] builder = new short[0];
        LinkedNode current = this;
        while (current.getParent() != null) {
            builder = SparseViolationProfile.mergeSorted(builder,current.sparseViolationProfile);
            current = current.getParent();
        }
        return printFull()+"\n  "+builder.toString();
    }




}
