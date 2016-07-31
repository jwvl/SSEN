package learn.batch.combination;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import learn.batch.LearningProperties;
import learn.batch.LearningPropertiesBuilder;
import util.collections.Couple;

import java.util.*;


/**
 * Created by janwillem on 31/07/16.
 */
public class LearningPropertyCombinations implements Iterable<LearningProperties> {
    private final Set<LearningProperties> combinations;

    public LearningPropertyCombinations(Set<LearningProperties> combinations) {
        this.combinations = combinations;
    }

    public static LearningPropertyCombinations fromMultimap(Multimap<String,String> map, LearningProperties initial) {
        List<Set<Couple<String>>> asStringCouples = multimapToListSet(map);
        Set<List<Couple<String>>> cartesian = Sets.cartesianProduct(asStringCouples);
        Set<LearningProperties> result = new HashSet<>();
        for (List<Couple<String>> couplesList: cartesian) {
            LearningPropertiesBuilder builder = initial.toBuilder();
            for (Couple<String> couple: couplesList) {
                builder.setFromStrings(couple.getLeft(),couple.getRight());
            }
            result.add(builder.build());
        }
        return new LearningPropertyCombinations(result);
    }

    private static List<Set<Couple<String>>> multimapToListSet(Multimap<String,String> map) {
        List<Set<Couple<String>>> list = new ArrayList<>();
        for (String parameter: map.keySet()) {
            Set<Couple<String>> stringCouples = new HashSet<>();
            Collection<String> values = map.get(parameter);
            for (String value: values) {
                stringCouples.add(Couple.of(parameter,value));
            }
            list.add(stringCouples);
        }
        return list;
    }

    @Override
    public Iterator<LearningProperties> iterator() {
        return combinations.iterator();
    }
}
