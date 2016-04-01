package graph.explorer;

import forms.Form;

/**
 * Created by janwillem on 29/03/16.
 */
public class VisitNode {
    private final Form form;
    private int nowAt;

    public int getNowAt() {
        return nowAt;
    }

    public void increaseNowAt() {
        this.nowAt += 1;
    }

    public Form getForm() {
        return form;
    }


    public VisitNode(Form form) {
        this.form = form;
        nowAt = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VisitNode visitNode = (VisitNode) o;

        return form != null ? form.equals(visitNode.form) : visitNode.form == null;

    }

    @Override
    public int hashCode() {
        return form != null ? form.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("VisitNode{");
        sb.append("form=").append(form);
        sb.append(", nowAt=").append(nowAt);
        sb.append('}');
        return sb.toString();
    }
}
