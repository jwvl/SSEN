/**
 *
 */
package forms.phon.syllable;

import forms.phon.Sonority;
import gen.rule.string.Side;

import java.util.Arrays;

/**
 * @author jwvl
 * @date 25/03/2016
 */
public class ConsonantCluster {
    private final Side side;
    private final Sonority[] cluster;
    public final static ConsonantCluster EMPTY_ONSET = new ConsonantCluster(new Sonority[0],Side.LEFT);
    public final static ConsonantCluster EMPTY_CODA = new ConsonantCluster(new Sonority[0],Side.RIGHT);


    /**
     * @param side
     */
    private ConsonantCluster(Sonority[] sonorities, Side side) {
        this.cluster = sonorities;
        this.side = side;
    }

    public static ConsonantCluster getInstance(Sonority[] sonorities, Side side) {
        if (sonorities.length == 0) {
            switch (side) {
                case LEFT:
                    return EMPTY_ONSET;
                case RIGHT:
                    return EMPTY_CODA;
            }
        }
        return new ConsonantCluster(sonorities, side);
    }

    public static ConsonantCluster getOnset(SonorityProfile profile) {
        int peakLocation = profile.peakLocation();
        Sonority[] subSequence = new Sonority[peakLocation];
        System.arraycopy(profile.getSonorities(), 0, subSequence, 0, peakLocation);
        return getInstance(subSequence, Side.LEFT);
    }

    public static ConsonantCluster getCoda(SonorityProfile profile) {
        int peakLocation = profile.peakLocation();
        Sonority[] subSequence = new Sonority[profile.size() - (peakLocation + 1)];
        for (int i = 0; i < subSequence.length; i++) {
            subSequence[i] = profile.getSonorities()[peakLocation + i + 1];
        }
        return getInstance(subSequence, Side.RIGHT);
    }

    public static ConsonantCluster[] getOnsetAndCoda(SonorityProfile profile) {
        ConsonantCluster[] result = new ConsonantCluster[2];
        int peakLocation = profile.peakLocation();
        Sonority[] fullSonorities = profile.getSonorities();
        Sonority[] onsetSequence = new Sonority[peakLocation];
        System.arraycopy(fullSonorities, 0, onsetSequence, 0, peakLocation);
        result[0] = getInstance(onsetSequence, Side.LEFT);
        Sonority[] codaSequence = new Sonority[profile.size() - (peakLocation + 1)];
        for (int i = 0; i < codaSequence.length; i++) {
            codaSequence[i] = fullSonorities[peakLocation + i + 1];
        }
        result[1] = getInstance(codaSequence, Side.RIGHT);
        return result;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (side == Side.LEFT) {
            builder.append("[");
        }
        for (Sonority sonority : cluster) {
            builder.append(sonority);
        }
        if (side == Side.RIGHT)
            builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cluster == null) ? 0 : Arrays.hashCode(cluster));
        result = prime * result + ((side == null) ? 0 : side.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ConsonantCluster))
            return false;
        ConsonantCluster other = (ConsonantCluster) obj;
        if (side != other.side)
            return false;
        return Arrays.equals(cluster, other.cluster);
    }

    /**
     * @return
     */
    public Side getSide() {
        return side;
    }

    /**
     * @return
     */
    public boolean isEmpty() {
        return cluster.length == 0;
    }


}
