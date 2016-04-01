package graph.visual;

/**
 * Quick and dirty int-pair class to make working with coordinates
 * a little easier
 *
 * @author Jan-Willem van Leussen, Nov 24, 2014
 */
class IntCoordinate {
    private int[] coords;

    static IntCoordinate of(int x, int y) {
        return new IntCoordinate(x, y);
    }

    IntCoordinate(int x, int y) {
        this.coords = new int[2];
        this.coords[0] = x;
        this.coords[1] = y;
    }

    DoubleCoordinate toFractions(int xNumerator, int yNumerator) {
        double xFrac = (coords[0] * 1D) / xNumerator;
        double yFrac = (coords[1] * 1D) / yNumerator;
        return new DoubleCoordinate(xFrac, yFrac);
    }

    int x() {
        return coords[0];
    }

    int y() {
        return coords[1];
    }

}
