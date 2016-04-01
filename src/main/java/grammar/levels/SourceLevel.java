/**
 *
 */
package grammar.levels;

/**
 * Singleton object representing the graph 'layer' on which
 * the SOURCE node is located.
 *
 * @author jwvl
 * @date Dec 5, 2014
 */
public class SourceLevel extends Level {
    private final static SourceLevel instance = new SourceLevel(LevelInfo.sourceLevelInfo);


    public static Level getInstance() {
        return instance;
    }

    /**
     * @param li
     */
    private SourceLevel(LevelInfo li) {
        super(li);
    }

    @Override
    public int myIndex() {
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SourceLevel;
    }

}
