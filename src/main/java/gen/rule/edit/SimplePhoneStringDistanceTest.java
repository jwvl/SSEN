/**
 *
 */
package gen.rule.edit;

import forms.phon.PhoneTransform;
import org.junit.Test;

import java.util.List;

/**
 * @author jwvl
 * @date Jul 19, 2015
 */
public class SimplePhoneStringDistanceTest {
    SimplePhoneStringDistance testSubject;
    String firstString = "aka";
    String leftChanged = "traka";
    String rightChanged = "akart";

    /**
     * Test method for {@link gen.rule.edit.SimplePhoneStringDistance#createInstance(forms.primitives.segment.PhoneSubForm, forms.primitives.segment.PhoneSubForm)}.
     */
    @Test
    public void testLeftTransform() {
        testSubject = SimplePhoneStringDistance.createInstance(firstString, leftChanged);
        List<PhoneTransform> result = testSubject.getEditScript();
        System.out.println(result);
    }

    /**
     * Test method for {@link gen.rule.edit.SimplePhoneStringDistance#createInstance(forms.primitives.segment.PhoneSubForm, forms.primitives.segment.PhoneSubForm)}.
     */
    @Test
    public void testRightTransform() {
        testSubject = SimplePhoneStringDistance.createInstance(firstString, rightChanged);
        List<PhoneTransform> result = testSubject.getEditScript();
        System.out.println(result);
    }

}
