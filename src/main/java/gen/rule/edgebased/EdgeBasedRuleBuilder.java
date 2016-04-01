/**
 *
 */
package gen.rule.edgebased;

import forms.phon.Sonority;
import forms.primitives.boundary.Edge;
import forms.primitives.segment.Phone;
import util.arrays.ByteArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jwvl
 * @date 18/03/2016
 */
public class EdgeBasedRuleBuilder {
    private static List<EdgeBasedRule> results = new ArrayList<EdgeBasedRule>();

    public static List<EdgeBasedRule> fromString(String inputString, Edge edge) {
        results.clear();
        buildResults(inputString, edge);
        return results;
    }

    public static void buildResults(String inputString, Edge edge) {
        int firstIndexOfPipe = inputString.indexOf("|");
        if (firstIndexOfPipe >= 0) {
            char charFound = inputString.charAt(firstIndexOfPipe + 1);
            Sonority sonority = Sonority.valueOf(charFound);
            for (Phone phone : sonority.getPhones()) {
                String toReplace = "\\|" + charFound;
                buildResults(inputString.replaceFirst(toReplace, phone.toString()), edge);
            }
        } else {
            results.add(fromStringSingle(inputString, edge));
        }
    }

    public static EdgeBasedRule fromStringSingle(String inputString, Edge edge) {
        String[] splitBySlash = inputString.split("/");
        String operationPart = splitBySlash[0].trim();
        EdgeBasedOperation edgeBasedOperation = EdgeBasedOperation.fromString(operationPart);
        String[] contexts = splitBySlash[1].trim().split("__");
        byte[] left;
        byte[] right;
        if (contexts.length > 0) {
            left = ByteArrayUtils.fromString(contexts[0].trim());
        } else {
            left = new byte[0];
        }
        if (contexts.length > 1) {
            right = ByteArrayUtils.fromString(contexts[1].trim());
        } else {
            right = new byte[0];
        }
        return new EdgeBasedRule(edge, left, right, edgeBasedOperation);
    }
}
