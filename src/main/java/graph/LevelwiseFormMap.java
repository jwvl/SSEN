/**
 *
 */
package graph;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import forms.Form;
import grammar.levels.Level;

import java.util.Collection;
import java.util.Map;

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

    public static LevelwiseFormMap createFromMultimap(Map<Form,Collection<Form>> map) {
        Multimap<Level, Form> multimap = HashMultimap.create();

        for (Form in: map.keySet()) {
            multimap.put(in.getLevel(),in);
            for (Form out: map.get(in)) {
                multimap.put(out.getLevel(),out);
            }
        }
        return new LevelwiseFormMap(multimap);
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

    public int size() {
        return contents.keySet().size();
    }

    public ArrayListMultimap<Level,Form> toArrayMultimap() {
        ArrayListMultimap<Level, Form> result = ArrayListMultimap.create();
        for (Level level: contents.keySet()) {
            for (Form form: contents.get(level)) {
                result.put(level,form);
            }
        }
        return result;
    }
}
