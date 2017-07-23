package simulate.french.sixlevel.data;

import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import forms.FormPair;
import forms.morphosyntax.SemSynForm;
import forms.phon.flat.PhoneticForm;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * Created by janwillem on 21/07/2017.
 */
public class PfcPossibleForms {
    private Set<FormPair> isPossible;
    private Set<FormPair> liaising;
    private Set<FormPair> inData;

    public PfcPossibleForms(String resource) {
        isPossible = Sets.newHashSet();
        liaising = Sets.newHashSet();
        inData = Sets.newHashSet();

        readDataFromFile(resource);
        System.out.println("Done reading file " + resource);
    }

    private void readDataFromFile(String resource) {
        URL url = Resources.getResource(resource);
        try {
            List<String> asLines = Resources.readLines(url, Charsets.UTF_8);
            for (int i =1; i < asLines.size(); i++) {
                String line = asLines.get(i);
                String[] split = line.split("\t");
                SemSynForm semSynForm = SemSynForm.readFromString(split[0]);
                PhoneticForm phoneticForm = PhoneticForm.createFromString(split[1]);
                FormPair formPair = FormPair.of(semSynForm,phoneticForm);
                isPossible.add(formPair);
                if (split[2].equals("1")) {
                    liaising.add(formPair);
                }
                if (split[3].equals("1")) {
                    inData.add(formPair);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPossible(FormPair formPair) {
        return isPossible.contains(formPair);
    }

    public boolean isInData(FormPair formPair) {
        return inData.contains(formPair);
    }


    public boolean isLiaising(FormPair formPair) {
        return liaising.contains(formPair);
    }

}
