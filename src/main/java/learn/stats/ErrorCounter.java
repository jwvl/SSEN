package learn.stats;

/**
 * Created by janwillem on 26/03/16.
 */
public class ErrorCounter {
    private int errors = 0;
    private int total = 0;


    public void increaseCount(boolean increaseError) {
        total++;
        if (increaseError) {
            errors++;
        }
    }

    public int getErrors() {
        return errors;
    }

    public int getTotal() {
        return total;
    }

    public double getErrorRate() {
        return errors / (double) total;
    }

    public String getErrorAsPercentage() {
        double inPercent = 100 * getErrorRate();
        return String.format("%.02f", inPercent);
    }

    public String getErrorAsRatio() {
        return errors + "/" + total;
    }

    public void reset() {
        errors = 0;
        total = 0;
    }
}
