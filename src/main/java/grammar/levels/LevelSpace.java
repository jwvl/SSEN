/**
 *
 */
package grammar.levels;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author jwvl
 * @date Dec 5, 2014
 */
public class LevelSpace implements Iterable<Level> {
    private final ImmutableList<Level> contents;

    public static LevelSpace createFromInfoList(List<LevelInfo> toSet) {
        List<Level> toAdd = new ArrayList<Level>();

        for (LevelInfo li : toSet) {
            toAdd.add(Level.createFromInfo(li));
        }
        return new LevelSpace(toAdd);
    }

    public static LevelSpace createFromLevelList(List<Level> toSet) {
        return new LevelSpace(toSet);
    }

    /**
     * @param contents
     */
    private LevelSpace(List<Level> levelsToSet) {
        this.contents = ImmutableList.copyOf(levelsToSet);
        for (Level l : levelsToSet) {
            l.setContainingSpace(this);
        }
    }

    public long distance(Level arg0, Level arg1) {

        return contents.indexOf(arg1) - contents.indexOf(arg0);
    }

    public List<Level> getContentsCopy() {
        return Lists.newArrayList(contents);

    }

    /**
     * @param level
     * @return
     */
    public int getLevelIndex(Level level) {
        return contents.indexOf(level);
    }

    public Level getByIndex(int i) {
        if (i == -1) {
            return SourceLevel.getInstance();
        } else if (i < -1 || i > getSize()) {
            return Level.NULL_LEVEL;
        } else if (i == getSize()) {
            return SinkLevel.getInstance();
        } else {
            return contents.get(i);
        }
    }

    public int getSize() {
        return contents.size();
    }

    public LevelSpace getSubspace(Level start, Level end) {
        long distance = distance(start, end);
        if (distance <= 0) {
            System.err.println("Can't do that!");
            throw new IndexOutOfBoundsException(String.format("%s and %s are not L-R connected.", start, end));
        } else {
            List<Level> subset = new ArrayList<Level>((int) distance + 1);
            for (int i = getLevelIndex(start); i <= getLevelIndex(end); i++) {
                subset.add(contents.get(i));
            }
            return new LevelSpace(subset);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Level> iterator() {
        return contents.iterator();
    }

    public Level maxValue() {
        return contents.get(contents.size() - 1);
    }

    public Level minValue() {
        return contents.get(0);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.google.common.collect.DiscreteDomain#next(java.lang.Comparable)
     */
    public Level next(Level arg0) {
        int index = contents.indexOf(arg0);
        if (index < contents.size() - 1)
            return contents.get(index + 1);
        else if (index == contents.size() - 1)
            return Level.NULL_LEVEL;
        System.err.println("Error in method LevelSpace.next()! Level " + arg0 + " is not in this level space.");
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.google.common.collect.DiscreteDomain#previous(java.lang.Comparable)
     */
    public Level previous(Level arg0) {
        int index = contents.indexOf(arg0);
        if (index > 0)
            return contents.get(index - 1);
        else if (index == 0)
            return Level.NULL_LEVEL;
        System.err.println("Error in method LevelSpace.previous()! Level " + arg0
                + " is not in this level space.");
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contents == null) ? 0 : contents.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof LevelSpace))
            return false;
        LevelSpace other = (LevelSpace) obj;
        if (contents == null) {
            if (other.contents != null)
                return false;
        } else if (!contents.equals(other.contents))
            return false;
        return true;
    }

    /**
     * @param l
     * @return
     */
    public boolean containsLevel(Level l) {
        return contents.contains(l);
    }

    /**
     * @param level
     * @return
     */
    public boolean isEndLevel(Level level) {
        return getLevelIndex(level) == getSize() - 1;
    }

}
