package constraints;

/**
 * Created by janwillem on 05/08/16.
 */
public class TestSparseViolationProfile {

    public static void main(String[] args) {
        short[] aList = {20,30,30,40};
        short[] bList = {21,30,30,40};
        short[] cList = {20,31,30,40};

        SparseViolationProfile aProfile = SparseViolationProfile.createFromShortArray(aList);
        SparseViolationProfile bProfile = SparseViolationProfile.createFromShortArray(bList);
        SparseViolationProfile cProfile = SparseViolationProfile.createFromShortArray(cList);

        System.out.println(aProfile.getLeftmostDifference(cProfile));

    }
}
