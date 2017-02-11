package io.riverplot;

/**
 * Created by janwillem on 29/09/16.
 */
public class RFriendly {
    public static String makeRFriendly(String original) {
        String result = original.replace("#"," ");
        result = result.replace("⟨","<");
        result = result.replace("⟩",">");
        result = result.replace("∅","Ø");
        return simplifyFeatures(result);
    }

    public static String simplifyFeatures(String original) {
        String result = original.replace("num[SG]","SG");
        result = result.replace("num[PL]","PL");
        result = result.replace("num[Ø]","Ø");
        result = result.replace("g[F]","F");
        result = result.replace("g[M]","M");
        result = result.replace("g[Ø]","Ø");
        result = result.replace("DET","");
        result = result.replace("NUM","");
        result = result.replace("N","");
        result = result.replace("ADJ","");
        result = result.replace("_","");
        return result;

    }
}
