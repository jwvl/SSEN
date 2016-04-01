package graph.visual;

/**
 * Quick and dirty double-pair class to make working with coordinates a little
 * easier
 *
 * @author Jan-Willem van Leussen, Nov 24, 2014
 */
public class DoubleCoordinate {
    private double[] coords;

    public static DoubleCoordinate fromIntAndDimension(int x, int y,
                                                       int xTotal, int yTotal) {
        double xFrac = (x * 1D) / xTotal;
        double yFrac = (y * 1D) / yTotal;
        return new DoubleCoordinate(xFrac, yFrac);
    }

    public DoubleCoordinate(double x, double y) {
        this.coords = new double[2];
        this.coords[0] = x;
        this.coords[1] = y;
    }

    public double x() {
        return coords[0];
    }

    public double y() {
        return coords[1];
    }

}
