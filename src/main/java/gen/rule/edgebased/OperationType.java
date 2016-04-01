/**
 *
 */
package gen.rule.edgebased;

/**
 * @author jwvl
 * @date 19/02/2016
 */
public enum OperationType {
    INSERT, DELETE, CHANGE;

    OperationType getInverse() {
        if (this == INSERT)
            return DELETE;
        else if (this == DELETE) {
            return INSERT;
        } else return CHANGE;
    }


}
