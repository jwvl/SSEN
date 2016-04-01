package util.debug;

import java.text.DecimalFormat;

/**
 * Timing class for debugging purposes.
 *
 * @author Jan-Willem van Leussen, Apr 23, 2014
 */
public class Stopwatch {

    private final static Timer TIMER = new Timer();

    private final static DecimalFormat df = new DecimalFormat("###.#####");


    public static String report_ms() {
        return TIMER.report_ms();
    }

    public static void reportElapsedTime(String toPrint, boolean restart) {
        TIMER.reportElapsedTime(toPrint, restart);
    }

    public static void start() {
        TIMER.start();
    }


    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean timePassed(int milliseconds) {
        return TIMER.timePassed(milliseconds);
    }


}
