package io.riverplot;

import forms.Form;

import java.util.Objects;

/**
 * Created by janwillem on 29/09/16.
 */
public class WeightedForm implements Comparable<WeightedForm> {
    private final Form form;
    private final int weight;

    public WeightedForm(Form form, int weight) {
        this.form = form;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeightedForm that = (WeightedForm) o;
        return weight == that.weight &&
                Objects.equals(form, that.form);
    }

    @Override
    public int hashCode() {
        return Objects.hash(form, weight);
    }


    @Override
    public int compareTo(WeightedForm o) {
        return Integer.compare(weight,o.weight);
    }

    public Form getForm() {
        return form;
    }
}
