/**
 *
 */
package eval.harmony;

/**
 * @author jwvl
 * @date 20/02/2016
 */
public interface HarmonyCost<H extends HarmonyCost<H>> extends Comparable<H> {
    void mergeCost(H other);

}
