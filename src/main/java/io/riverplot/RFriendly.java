package io.riverplot;

/**
 * Created by janwillem on 29/09/16.
 */
public class RFriendly {
    public static String makeRFriendly(String original) {
        String result = original.replace("#","♯");
        result = result.replace("⟨","<");
        result = result.replace("⟩",">");
        result = result.replace("∅","Ø");
        return result;
    }
}
