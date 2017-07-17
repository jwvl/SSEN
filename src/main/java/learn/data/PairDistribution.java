/**
 *
 */
package learn.data;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import forms.Form;
import forms.FormPair;
import graph.Direction;
import util.collections.Couple;
import util.collections.Distribution;
import util.collections.FrequencyTable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author jwvl
 * @date Feb 13, 2015
 */
public class PairDistribution extends
        Distribution<FormPair> implements LearningData {


    /**
     * @param name
     */
    public PairDistribution(String name) {
        super(name);
    }

    public void add(Form f, Form g, int frequency) {
        FormPair toAdd = FormPair.of(f, g);
        System.out.println("Adding formpair " + toAdd);
        add(toAdd,frequency);
    }

    public void addOne(Form f, Form g) {
        FormPair toAdd = FormPair.of(f, g);
        addOne(toAdd);
    }

    public Set<Form> getLeftForms() {
        Set<Form> result = new HashSet<Form>();
        for (FormPair learningDatum : getKeySet()) {
            result.add(learningDatum.left());
        }
        return result;
    }

    public Set<Form> getRightForms() {
        Set<Form> result = new HashSet<Form>();
        for (FormPair learningDatum : getKeySet()) {
            result.add(learningDatum.right());
        }
        return result;
    }

    public FormPair drawFormPair() {
        return drawItem();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return true;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @Override
    public FormPair next() {
        return drawFormPair();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
        // Does nothing!
    }

    @Override
    public Set<FormPair> getForLearningDirection(Direction direction) {
        Set<FormPair> result = Sets.newHashSet();
        for (FormPair learningDatum : getKeySet()) {
            result.add(learningDatum.getUnlabeled(direction));
        }
        return result;
    }

    public Collection<FormPair> getKeys() {
        Set<FormPair> result = new HashSet<FormPair>();
        for (FormPair learning : getKeySet()) {
            result.add(learning);
        }
        return result;
    }

    public PairDistribution filter(Predicate<FormPair> predicate) {
        PairDistribution result = new PairDistribution(this.getName() + "-filtered");
        for (FormPair formPair : getKeys()) {
            if (predicate.apply(formPair)) {
                int frequency = getFrequency(formPair);
                result.add(formPair, frequency);
            } else {
                System.out.println("Removing formpair " + formPair);
            }
        }
        return result;
    }

    public PairDistribution squareRoot() {
        PairDistribution result = new PairDistribution(this.getName() + "-filtered");
        for (FormPair formPair : getKeys()) {
            int frequency = getFrequency(formPair);
            int newFrequency = (int) (1 + Math.sqrt(frequency));
            result.add(formPair, newFrequency);
        }
        return result;
    }

    public Couple<PairDistribution> splitToTestAndTraining(double testFraction) {
        if (testFraction <= 0.0) {
            return Couple.of(null, this);
        }
        Random random = new Random();
        PairDistribution test = new PairDistribution(this.getName() + "-test");
        PairDistribution train = new PairDistribution(this.getName() + "-train");
        for (FormPair formPair: getKeys()) {
            int frequency = getFrequency(formPair);
            for (int i=0; i < frequency; i++) {
                if (random.nextDouble() < testFraction) {
                    test.addOne(formPair);
                } else {
                    train.addOne(formPair);
                }
            }
        }
        return Couple.of(test,train);
    }

    public FrequencyTable<Form, Form> toFrequencyTable() {
        FrequencyTable<Form,Form> result = new FrequencyTable<>();
        for (FormPair formPair: getKeys()) {
            int frequency = getFrequency(formPair);
            result.add(formPair.left(), formPair.right(), frequency);
        }
        return result;
    }

    public double calculateExpectedError() {
        FrequencyTable<Form,Form> frequencyTable = toFrequencyTable();
        int totalCount = 0;
        int expectedCount = 0;
        for (Form leftForm: frequencyTable.getColumnSet()) {
            int leftTotal = frequencyTable.getColumnCount(leftForm);
            totalCount += leftTotal;
            int currMax = 0;
            for (Form rightForm: frequencyTable.getRowsForColumn(leftForm)) {
                int currCount = frequencyTable.getCount(leftForm,rightForm);
                if (currCount > currMax) {
                    currMax = currCount;
                }

            }
            expectedCount+= currMax;
        }
        return 1-(expectedCount / ((double) totalCount));
    }
}
