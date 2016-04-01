package learn.update;

/**
 * Created by janwillem on 26/03/16.
 */
public class UpdateAlgorithms {
    public static UpdateAlgorithm ALL_UP_HIGH_DOWN = new AllUpHighDown();
    public static UpdateAlgorithm WEIGHTED_ALL = new WeightedAll();
    public static UpdateAlgorithm WEIGHTED_UNCANCELLED = new WeightedUncancelled();
    public static UpdateAlgorithm SYMMETRIC_ALL = new SymmetricAll();

    public static UpdateAlgorithm createFromString(String input) {
        String inputNoWhitespace = input.replace("\\s", "");
        if (inputNoWhitespace.equalsIgnoreCase("SymmetricAll")) {
            return SYMMETRIC_ALL;
        } else if (inputNoWhitespace.equalsIgnoreCase("WeightedUncancelled")) {
            return WEIGHTED_UNCANCELLED;
        } else if (inputNoWhitespace.equalsIgnoreCase("AllUpHighDown")) {
            return ALL_UP_HIGH_DOWN;
        } else if (inputNoWhitespace.equalsIgnoreCase("WeightedAll")) {
            return WEIGHTED_ALL;
        }
        return WEIGHTED_UNCANCELLED;
    }
}
