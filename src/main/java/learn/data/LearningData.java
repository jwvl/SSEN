/**
 *
 */
package learn.data;

import forms.FormPair;
import graph.Direction;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author jwvl
 * @date 26/03/2016
 */
public interface LearningData extends Iterator<FormPair> {
    Set<FormPair> getForLearningDirection(Direction direction);

    Collection<FormPair> getKeys();
}
