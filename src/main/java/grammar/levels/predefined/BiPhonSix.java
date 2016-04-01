/**
 *
 */
package grammar.levels.predefined;

import com.google.common.collect.Lists;
import grammar.levels.Level;
import grammar.levels.LevelInfo;
import grammar.levels.LevelSpace;

import java.util.List;

/**
 * @author jwvl
 * @date Jul 18, 2015
 */
public class BiPhonSix {
    private static LevelSpace levelSpace = getLevelSpace();

    public static LevelSpace getLevelSpace() {
        LevelSpace result = LevelSpace.createFromInfoList(getInfoList());
        return result;
    }

    private static List<LevelInfo> getInfoList() {
        List<LevelInfo> result = Lists.newArrayList();
        result.add(LevelInfo.createSemanticForm());
        result.add(LevelInfo.createMStructureForm());
        result.add(LevelInfo.createMFormForm());
        result.add(LevelInfo.createUnderlyingForm());
        result.add(LevelInfo.createSurfaceForm());
        result.add(LevelInfo.createPhoneticForm());
        return result;
    }

    /**
     * @return
     */
    public static Level getSemSynFormLevel() {
        return levelSpace.getByIndex(0);
    }

    /**
     * @return
     */
    public static Level getMstructureLevel() {
        return levelSpace.getByIndex(1);
    }

    /**
     * @return
     */
    public static Level getMformLevel() {
        return levelSpace.getByIndex(2);
    }

    /**
     * @return
     */
    public static Level getUnderlyingFormLevel() {
        return levelSpace.getByIndex(3);
    }

    /**
     * @return
     */
    public static Level getSurfaceFormLevel() {
        return levelSpace.getByIndex(4);
    }

    /**
     * @return
     */
    public static Level getPhoneticLevel() {
        return levelSpace.getByIndex(5);
    }

}
