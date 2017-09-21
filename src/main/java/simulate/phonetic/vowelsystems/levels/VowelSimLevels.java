package simulate.phonetic.vowelsystems.levels;

import com.google.common.collect.Lists;
import grammar.levels.Level;
import grammar.levels.LevelInfo;
import grammar.levels.LevelSpace;

import java.util.List;

public class VowelSimLevels {
    private static LevelSpace levelSpace = getLevelSpace();

    public static LevelSpace getLevelSpace() {
        LevelSpace result = LevelSpace.createFromInfoList(getInfoList());
        return result;
    }

    private static List<LevelInfo> getInfoList() {
        List<LevelInfo> result = Lists.newArrayList();
        result.add(LevelInfo.createPhoneticForm());
        result.add(LevelInfo.createSurfaceForm());
        result.add(LevelInfo.createUnderlyingForm());
        return result;
    }

    /**
     * @return
     */
    public static Level getUnderlyingFormLevel() {
        return levelSpace.getByIndex(2);
    }

    /**
     * @return
     */
    public static Level getSurfaceFormLevel() {
        return levelSpace.getByIndex(1);
    }

    /**
     * @return
     */
    public static Level getPhoneticLevel() {
        return levelSpace.getByIndex(0);
    }
}
