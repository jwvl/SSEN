/**
 *
 */
package forms;

import forms.primitives.Subform;
import grammar.levels.Level;
import grammar.levels.SinkLevel;
import grammar.levels.SourceLevel;

/**
 * An implementation of a Form which exists only in a graph. They do not
 * represent some linguistic form/representation but are used implementationally
 * for path finding in the layered graph etc.
 *
 * @author jwvl
 * @date Nov 17, 2014
 */
public class GraphForm implements Form {
    private static GraphForm SINK_INSTANCE = new GraphForm("SINK", true);
    private static GraphForm SOURCE_INSTANCE = new GraphForm("SOURCE", false);
    final boolean isSink;
    final boolean isSource;
    final String name;

    public static GraphForm getSinkInstance() {
        return SINK_INSTANCE;
    }

    public static GraphForm getSourceInstance() {
        return SOURCE_INSTANCE;
    }

    /**
     * @param name
     */
    private GraphForm(String name, boolean isSink) {
        this.isSink = isSink;
        this.isSource = !isSink;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof GraphForm))
            return false;
        GraphForm other = (GraphForm) obj;
        return this.isSink() == other.isSink();
    }

    @Override
    public int getNumSubForms() {
        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public String toString() {
        if (this.isSink)
            return "(snk)";
        else
            return "(src)";
    }

    public boolean isSink() {
        return isSink;
    }

    public boolean isSource() {
        return isSource;
    }

    @Override
    public int countSubform(Subform sf) {
        return 0;
    }

    /* (non-Javadoc)
     * @see forms.Form#getLevel()
     */
    @Override
    public Level getLevel() {
        return isSink() ? SinkLevel.getInstance() : SourceLevel.getInstance();
    }

    @Override
    public int getLevelIndex() {
        return getLevel().myIndex();
    }


    /* (non-Javadoc)
     * @see forms.Form#toBracketedString()
     */
    @Override
    public String toBracketedString() {
        // TODO Auto-generated method stub
        return toString();
    }

}
