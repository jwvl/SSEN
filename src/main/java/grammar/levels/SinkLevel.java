/**
 *
 */
package grammar.levels;

/**
 * Singleton object representing the graph 'layer' on which
 * the SINK node is located.
 *
 * @author jwvl
 * @date Dec 5, 2014
 */
public class SinkLevel extends Level {
    private static SinkLevel INSTANCE = new SinkLevel(LevelInfo.sinkLevelInfo);


    public static Level getInstance() {
        return INSTANCE;
    }

    /**
     * @param li
     */
    private SinkLevel(LevelInfo li) {
        super(li);
    }

    @Override
    public int myIndex() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SinkLevel;
    }


}
