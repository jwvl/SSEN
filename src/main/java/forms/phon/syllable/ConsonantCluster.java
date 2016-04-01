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
    private final static int ONSET = 0, CODA = 1;
    private final static ConsonantCluster EMPTY = new ConsonantCluster(new Sonority[0], Side.NEITHER);

    /**
     * @param side
     */
    private ConsonantCluster(Sonority[] sonorities, Side side) {
        this.cluster = sonorities;
        this.side = side;
    }

    public static ConsonantCluster getInstance(Sonority[] sonorities, Side side) {
        if (sonorities.length < 1)
            return EMPTY;
        else return new ConsonantCluster(sonorities, side);
    }

    public static ConsonantCluster getOnset(SonorityProfile profile) {
        int peakLocation = profile.peakLocation();
        Sonority[] subSequence = new Sonority[peakLocation];
        for (int i = 0; i < peakLocation; i++) {
            subSequence[i] = profile.getSonorities()[i];
        }
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
        for (int i = 0; i < peakLocation; i++) {
            onsetSequence[i] = fullSonorities[i];
        }
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
        return this == EMPTY;
    }


}
