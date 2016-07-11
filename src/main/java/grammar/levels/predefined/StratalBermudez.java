package grammar.levels.predefined;

import com.google.common.collect.Lists;
import grammar.levels.LevelInfo;
import grammar.levels.LevelSpace;

import java.util.List;

/**
 * Created by janwillem on 05/04/16.
 */
public class StratalBermudez {
    private static LevelSpace levelSpace = getLevelSpace();

    public static LevelSpace getLevelSpace() {
        LevelSpace result = LevelSpace.createFromInfoList(getInfoList());
        return result;
    }

    private static List<LevelInfo> getInfoList() {
        List<LevelInfo> result = Lists.newArrayList();
        result.add(LevelInfo.createInstance("Lexical Level","LL","<",">"));
        result.add(LevelInfo.createInstance("Stem Level","SL","|","|"));
        result.add(LevelInfo.createInstance("Word Level","WL","//","//"));
        result.add(LevelInfo.createInstance("Phrase Level","PL","/","/"));
        return result;
    }

}
