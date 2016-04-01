/**
 *
 */
package simulate.french.sixlevel;

import forms.morphosyntax.SemSynForm;
import forms.phon.flat.PhoneticForm;
import grammar.levels.Level;
import io.MyStringTable;
import learn.PairDistribution;
import util.collections.Couple;

import java.util.List;

/**
 * @author jwvl
 * @date Dec 18, 2014
 */
public class DataPairReader {
    private String dataPath;
    private Level leftLevel;
    private Level rightLevel;
    private MyStringTable table;
    private List<Couple<String>> stringCouples;
    private PairDistribution pairDistribution;

    /**
     * @param dataFile
     */
    private DataPairReader(String dataFile, Level leftLevel, Level rightLevel) {
        this.leftLevel = leftLevel;
        this.rightLevel = rightLevel;
        this.dataPath = dataFile;
        pairDistribution = new PairDistribution(dataFile);
    }

    /**
     * @param dataFile2
     * @return A new DataPairReader
     */
    public static DataPairReader createInstance(String dataFile2, Level leftLevel, Level rightLevel) {
        return new DataPairReader(dataFile2, leftLevel, rightLevel);
    }

    public void readDistribution() {
        table = MyStringTable.fromFile(dataPath, false, "\t");
        stringCouples = table.getStringPairs(0, 1);
        List<String> frequencies = table.getColumnContents(2);
        for (int i = 0; i < frequencies.size(); i++) {
            Couple<String> sp = stringCouples.get(i);
            String leftString = sp.getLeft();
            String rightString = sp.getRight();
            // The left forms will be "SemSyn forms", right forms phonetic forms.
            SemSynForm leftForm = SemSynForm.createFromString(leftString, leftLevel);
            PhoneticForm rightForm = PhoneticForm.createFromString(rightString);
            int frequency = Integer.parseInt(frequencies.get(i));
            pairDistribution.add(leftForm, rightForm, frequency);
        }
    }

    public void testReading() {
        System.out.println("Printing distribution as list:");
        pairDistribution.printAsList();
    }

    public PairDistribution getPairDistribution() {
        return pairDistribution;
    }

}
