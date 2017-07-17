package test;

import forms.FormPair;
import forms.morphosyntax.SemSynForm;
import forms.phon.flat.PhoneticForm;
import grammar.levels.predefined.BiPhonSix;

/**
 * @author jwvl
 * @date 08/11/2014
 */
public class Tester {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String test1 = "koleksjQ";
        String test2 = "koleksjQ";
        PhoneticForm pf1 = PhoneticForm.createFromString(test1);
        PhoneticForm pf2 = PhoneticForm.createFromString(test2);
        System.out.println(pf1.equals(pf2));
        System.out.println(pf1.hashCode());
        System.out.println(pf2.hashCode());

        SemSynForm form1 = SemSynForm.createFromString("ADJ{affreux}, N{personnage.g=M num=SG}*", BiPhonSix.getSemSynFormLevel());
        SemSynForm form2 = SemSynForm.createFromString("ADJ{affreux}, N{personnage.g=M num=SG}*", BiPhonSix.getSemSynFormLevel());
        System.out.println(form1.equals(form2));
        FormPair fp1 = FormPair.of(pf1,form1);
        FormPair fp2 = FormPair.of(pf2,form2);
        System.out.println(fp1.equals(fp2));
        System.out.println(fp1.hashCode());
        System.out.println(fp2.hashCode());
    }

}
