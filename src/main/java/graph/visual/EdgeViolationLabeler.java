/**
 *
 */
package graph.visual;

import gen.mapping.FormMapping;
import org.apache.commons.collections4.Transformer;
import ranking.violations.Violation;
import ranking.violations.ViolationMap;
import util.string.StringPrinter;

import java.util.List;

/**
 * @author jwvl
 * @date Nov 25, 2014
 */
public class EdgeViolationLabeler implements Transformer<FormMapping, String> {
    private ViolationMap violatorMap;

    public static EdgeViolationLabeler createInstance(ViolationMap vm) {
        return new EdgeViolationLabeler(vm);
    }

    protected EdgeViolationLabeler(ViolationMap vm) {
        this.violatorMap = vm;
    }

    public String transform(FormMapping fp) {
        String result = "";
        if (violatorMap.containsKey(fp)) {
            List<Violation> violations = violatorMap.get(fp);
            result += ("\n" + StringPrinter.linearizeList(violations, ", "));
        }
        return result;
    }

}
