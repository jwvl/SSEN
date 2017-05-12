package simulate.french.sixlevel.constraints.factories;

import com.google.common.collect.Lists;
import constraints.Constraint;
import constraints.factories.FormConstraintFactory;
import constraints.helper.ConstraintArrayList;
import forms.phon.flat.PhoneticForm;
import forms.primitives.segment.Phone;
import gen.mapping.FormMapping;
import simulate.french.sixlevel.constraints.PhoneCombinationConstraint;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by janwillem on 09/05/2017.
 */
public class PhoneCombinationConstraintFactory extends FormConstraintFactory<PhoneticForm> {

    private final PhoneCombinationConstraint[] constraints;
    private final int[] bases;
    private final int windowLength;

    public PhoneCombinationConstraintFactory(int length) {
        this.windowLength = length;
        bases = new int[length];
        int multiplier = Phone.getInventory().size();
        for (int i=1; i < length; i++) {
            bases[i] = (int) Math.pow(multiplier,i);

        }
        this.constraints = new PhoneCombinationConstraint[(int)Math.pow(multiplier,windowLength)];
    }

    @Override
    public void addFormMapping(FormMapping fm) {
        addForm((PhoneticForm) fm.right());
    }

    @Override
    public Collection<Constraint> getAll() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void addForm(PhoneticForm phones) {
        getConstraintsForForm(phones);
    }

    @Override
    public List<Constraint> getConstraintsForForm(PhoneticForm right) {
        byte[] asBytes = right.getByteArray();
        int[] offenders = getOffendingConstraints(asBytes);
        List<Constraint> list = Lists.newArrayListWithExpectedSize(offenders.length);
        for (int index: offenders) {
            list.add(constraints[index]);
        }
        return list;
    }

    @Override
    protected void addViolatorsForForm(PhoneticForm right, ConstraintArrayList list) {
        byte[] asBytes = right.getByteArray();
        int[] offenders = getOffendingConstraints(asBytes);
        for (int index: offenders) {
            list.add(constraints[index]);
        }

    }

    private int[] getOffendingConstraints(byte[] sequence) {
        int stopat = (1+sequence.length) - windowLength;
        int[] result = new int[stopat+2];
        for (int i = -1; i <= stopat; i++) {
            int encoded = bytesToIntAt(i,windowLength,sequence);
            if (constraints[encoded] == null) {
                byte[] asBytes = getByteArrayAt(i, windowLength, sequence);
                PhoneCombinationConstraint toAdd = new PhoneCombinationConstraint(asBytes);
                constraints[encoded] = toAdd;
            }
            result[i+1] = encoded;
        }
        return result;
    }


    private byte[] getByteArrayAt(int index, int length, byte[] sequence) {
        byte[] result = new byte[length];
        for (int i=0; i < length; i++) {
            result[i] = getByteAt(index+i,sequence);
        }
        return result;
    }

    private byte getByteAt(int index, byte[] sequence) {
        if (index < 0 || index >= sequence.length) {
            return Phone.NULL.getId();
        } else {
            return sequence[index];
        }
    }

    private int bytesToIntAt(int index, int length, byte[] sequence) {
        int sum = getByteAt(index, sequence);
        for (int i=1; i < length; i++) {
            sum+=(getByteAt(index+i,sequence)*bases[i]);
        }
        return sum;
    }
}
