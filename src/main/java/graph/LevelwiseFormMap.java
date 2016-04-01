/**
 *
 */
package graph;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import forms.Form;
import grammar.levels.Level;

import java.util.Collection;

/**
 * @author jwvl
 * @date Dec 5, 2014
 */
public class LevelwiseFormMap {
    private Multimap<Level, Form> contents;
    private final static Ordering<Form> DEFAULT_FORM_ORDER = Form.StringComparator;

    /**
     * Private constructor.
     *
     * @param contents
     */
    private LevelwiseFormMap(Multimap<Level, Form> contents) {
        this.contents = contents;
    }

    /**
     *
     */
    public static LevelwiseFormMap create() {
        HashMultimap<Level, Form> initContents = HashMultimap.create();
        return new LevelwiseFormMap(initContents);
    }

    public void add(Form f) {
        contents.put(f.getLevel(), f);
    }

    public void add(Iterable<Form> forms) {
        for (Form f : forms) {
            add(f);
        }
    }

    /**
     * @param nullLevel
     * @return
     */
    public boolean hasLevel(Level l) {
        return contents.containsKey(l);
    }

    public Collection<Form> getFormsAtLevel(Level l) {
        return contents.get(l);
    }

    public int getSizeAtLevel(Level l) {
        return contents.get(l).size();
    }

    /**
     * @param f
     */
    public void removeForm(Form f) {
        contents.get(f.getLevel()).remove(f);
    }

}
