/**
 *
 */
package forms.primitives;


/**
 * @author jwvl
 * @date Nov 16, 2014
 */
public class Submapping<S extends Subform, T extends Subform> implements ISubmapping<S, T> {
    private final S left;
    private final T right;

    public Submapping(S s, T t) {
        this.left = s;
        this.right = t;
    }


    public S left() {
        return left;
    }

    public T right() {
        return right;
    }

    @Override
    public String toString() {
        return left() + " --> " + right();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        result = prime * result + ((right == null) ? 0 : right.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Submapping))
            return false;
        Submapping other = (Submapping) obj;
        if (left == null) {
            if (other.left != null)
                return false;
        } else if (!left.equals(other.left))
            return false;
        if (right == null) {
            if (other.right != null)
                return false;
        } else if (!right.equals(other.right))
            return false;
        return true;
    }


}
