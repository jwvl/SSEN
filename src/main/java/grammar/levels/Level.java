/**
 *
 */
package grammar.levels;

import com.google.common.collect.Ordering;


/**
 * @author jwvl
 * @date Nov 25, 2014
 */
public class Level implements Comparable<Level> {
    public boolean inLevelSpace;
    private LevelSpace containingSpace;
    private LevelInfo info;
    public static final Level NULL_LEVEL = createNullLevel();
    private int index = Integer.MIN_VALUE;
    private int cachedHashCode = 0;

    public static Ordering<Level> LevelComparator = new Ordering<Level>() {
        @Override
        public int compare(Level l1, Level l2) {
            return l1.compareTo(l2);
        }
    };


    /**
     * @param li
     * @return
     */
    public static Level createFromInfo(LevelInfo li) {
        System.out.println("Created level " + li);
        return new Level(li);
    }

    /**
     * @return
     */
    private static Level createNullLevel() {
        return new Level(LevelInfo.nullLevelInfo);
    }


    public Level(LevelInfo li) {
        this.info = li;
        inLevelSpace = false;
    }


    public LevelSpace getContainingSpace() {
        return containingSpace;
    }

    /**
     * @return
     */
    public LevelInfo getInfo() {
        return info;
    }

    public int myIndex() {
        if (!this.inLevelSpace) {
            System.out.printf("Level %s is not in level space!\n", this.toString());
            return Integer.MIN_VALUE;
        } else {
            return index;
        }
    }

    protected void setContainingSpace(LevelSpace ls) {
        containingSpace = ls;
        inLevelSpace = true;
        index = ls.getLevelIndex(this);
    }

    @Override
    public String toString() {
        return info.getFullName();
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (cachedHashCode == 0) {
            cachedHashCode = computeHashCode();
        }
        return cachedHashCode;
    }

    private int computeHashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((info == null) ? 0 : info.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Level other = (Level) obj;
        if (info == null) {
            if (other.info != null)
                return false;
        } else if (!info.equals(other.info))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Level arg0) {
        return this.myIndex() - arg0.myIndex();
    }

    /**
     * @return
     */
    public Level getPredecessor() {
        return containingSpace.getByIndex(index - 1);
    }

    public boolean precedes(Level level) {
        return index == (level.index - 1);
    }

    public Level getSuccessor() {
        return containingSpace.getByIndex(index + 1);
    }

    public boolean succeeds(Level level) {
        return index == (level.index + 1);
    }


    public boolean isRightmost() {
        return getSuccessor() == SinkLevel.getInstance();
    }
}
