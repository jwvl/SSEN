package util.collections;

/**
 * Simple helper class to represent a 2-tuple of objects of identical type.
 * Needed for e.g. alignment of forms in this project
 *
 * @param <T>
 * @author jwvl, Nov 12, 2014
 */
public class Couple<T> {
    private final T left;
    private final T right;


    public static <T> Couple<T> of(T l, T r) {
        return new Couple<T>(l, r);
    }

    protected Couple(T l, T r) {
        left = l;
        right = r;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Couple<?> couple = (Couple<?>) o;

        if (left != null ? !left.equals(couple.left) : couple.left != null) return false;
        return right != null ? right.equals(couple.right) : couple.right == null;

    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }

    /**
     * @return the left element of the pair
     */
    public T getLeft() {
        return left;
    }

    public T getRight() {
        return right;
    }


    public boolean isReverse(Couple<T> c) {
        return c.left.equals(right) && c.right.equals(left);
    }


    public Couple<T> makeReverse() {
        return new Couple<T>(right, left);
    }

    public boolean contains(T t) {
        return left.equals(t) || right.equals(t);
    }

    @Override
    public String toString() {
        return String.format("%s -- %s", left.toString(), right.toString());
    }


}
