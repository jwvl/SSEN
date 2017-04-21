/**
 *
 */
package gen.rule.edgebased;

/**
 * @author jwvl
 * @date 25/03/2016
 */
public class AnyEdgeRule extends EdgeBasedRule {

    /**
     * @param leftContext
     * @param rightContext
     * @param operation
     */
    public AnyEdgeRule(byte[] leftContext, byte[] rightContext, EdgeBasedOperation operation) {
        super(null, leftContext, rightContext, operation);
    }


    @Override
    public EdgeBasedRule getReverse() {
        return new AnyEdgeRule(getLeftContext(), getRightContext(), getOperation().getReverse());
    }


}
