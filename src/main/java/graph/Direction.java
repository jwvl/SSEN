package graph;

public enum Direction {
    BIDIRECTIONAL, LEFT, RIGHT;

    public static String StringValueOf(Direction direction) {
        switch (direction) {
            case BIDIRECTIONAL:
                return "<->";
            case LEFT:
                return "<--";
            case RIGHT:
                return "-->";

        }
        return "";
    }

}
