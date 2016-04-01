package util.debug;

import java.text.DecimalFormat;

/**
 * Timing class for debugging purposes.
 *
 * @author Jan-Willem van Leussen, Apr 23, 2014
 */
public class Timer {

    private final static DecimalFormat df = new DecimalFormat("###.#####");
    private long startTime = System.nanoTime();
    private long stopTime;


    public String report_ms() {
        stop();
        return df.format((double) getTimeElapsed() / 1000000) + " ms";
    }

    public void reportElapsedTime(String toPrint, boolean restart) {
        System.out.println(toPrint + ": " + report_ms());
        if (restart)
            start();
    }

    public void start() {
        startTime = System.nanoTime();
    }

    public long getTimeElapsed() {
        return stopTime - startTime;
    }

    protected void stop() {
        stopTime = System.nanoTime();
    }

    public boolean timePassed(int milliseconds) {
        if (milliseconds < 0) {
            return false;
        }
        int millisecondsPassed = (int) ((System.nanoTime() - startTime) / 1000000);
        if (milliseconds < millisecondsPassed) {
            start();
            return true;
        }
        return false;
    }


}
