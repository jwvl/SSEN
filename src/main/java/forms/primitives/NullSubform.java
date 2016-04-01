package forms.primitives;

public class NullSubform extends Subform {
    private static NullSubform instance = new NullSubform();

    public static Subform getInstance() {
        return instance;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public String toString() {
        return "∅";
    }

}
