package util.collections;

/**
 * Simple helper class to represent a 2-tuple of objects of identical type.
 * Needed for e.g. alignment of forms in this project
 *
 * @param <L>
 * @author jwvl, Nov 12, 2014
 */
public class Pair<L, R> {
    private final L left;
    private final R right;

    public static <L, R> Pair<L, R> of(L l, R r) {
        return new Pair<L, R>(l, r);
    }

    private Pair(L l, R r) {
        left = l;
        right = r;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pair<L, R> other = (Pair<L, R>) obj;
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

    /**
     * @return the left element of the pair
     */
    public L getLeft() {
        return left;
    }

    /**
     * @return the left element of the pair
     */
    public R getRight() {
        return right;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        result = prime * result + ((right == null) ? 0 : right.hashCode());
        return result;
    }

    public boolean isReverse(Pair<R, L> c) {
        return c.left.equals(right) && c.right.equals(left);
    }


    public Pair<R, L> makeReverse() {
        return new Pair<R, L>(right, left);
    }

    @Override
    public String toString() {
        return "[l=" + left + ", r=" + right + "]";
    }


}
