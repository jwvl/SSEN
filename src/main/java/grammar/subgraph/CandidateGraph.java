package grammar.subgraph;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import forms.Form;
import forms.FormPair;
import gen.mapping.FormMapping;

import java.util.*;

/**
 * Created by janwillem on 29/03/16.
 */
public class CandidateGraph {
    private final FormPair formPair;
    private Map<Form, List<Form>> myMap;

    public CandidateGraph(FormPair formPair, ListMultimap<Form, Form> mappings) {
        this.formPair = formPair;
        myMap = new HashMap<Form, List<Form>>();
        int count = 0;
        for (Form f : mappings.keySet()) {
            List<Form> list = ImmutableList.copyOf(mappings.get(f));
            myMap.put(f, list);
            count += list.size();
        }
        myMap = Collections.synchronizedMap(myMap);
        //System.out.println("Built candidate space for" + formPair);
        //System.out.println("Added items: " + count);
    }

    public List<FormMapping> filter(Collection<FormMapping> toFilter) {
        List<FormMapping> filtered = new ArrayList<>();
        if (toFilter.isEmpty()) {
            return ImmutableList.copyOf(toFilter);
        }
        Form leftForm = toFilter.iterator().next().left();
        List<Form> asList = myMap.get(leftForm);
        if (asList == null) {
            System.out.println("Should not happen?");
            System.out.println("Looking for " + leftForm);
            System.out.println(myMap.keySet());
            return ImmutableList.copyOf(toFilter);
        }
        for (FormMapping formMapping : toFilter) {
            Form right = formMapping.right();

            if (asList.contains(right)) {
                filtered.add(formMapping);
            }
        }
        return filtered;
    }


    public FormPair getFormPair() {
        return formPair;
    }
    public Map<Form,List<Form>> getMap() {
        return myMap;
    }
}
