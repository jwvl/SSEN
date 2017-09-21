package simulate.phonetic.vowelsystems.subgens;

import com.google.common.collect.Lists;
import simulate.phonetic.vowelsystems.levels.UnderlyingVowelForm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnderlyingFormFactory {
    private static Map<String, UnderlyingVowelForm> cached = new HashMap<>();

    public static List<UnderlyingVowelForm> getUnderlyingForms(List<String> vowels) {
        List<UnderlyingVowelForm> result = Lists.newArrayList();
        for (String vowel: vowels) {
            UnderlyingVowelForm underlyingVowelForm = getUnderlyingForm(vowel);
            result.add(underlyingVowelForm);
        }
        return result;
    }

    public static UnderlyingVowelForm getUnderlyingForm(String vowel) {
        UnderlyingVowelForm result = cached.get(vowel);
        if (result == null) {
            result = new UnderlyingVowelForm(vowel);
            cached.put(vowel,result);
        }
        return result;
    }

    public static List<UnderlyingVowelForm> getUnderlyingForms() {
        return Lists.newArrayList(cached.values());
    }
}
