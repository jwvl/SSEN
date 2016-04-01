/**
 *
 */
package gen.alignment;

/**
 * @author jwvl
 * @date Aug 29, 2015
 */
public interface IAlignmentIndex {
    int getNumBottom();

    int getNumTop();

    NonCrossingAlignmentIndex toNonCrossingAlignmentIndex();
}
