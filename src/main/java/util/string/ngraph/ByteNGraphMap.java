package util.string.ngraph;

import forms.phon.flat.PhoneSequence;
import forms.primitives.segment.Phone;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by janwillem on 20/08/16.
 */
public class ByteNGraphMap {
    private final Set<ByteNGraph> map;
    private final int n;

    public ByteNGraphMap(int n) {
        this.map = new HashSet<ByteNGraph>();
        this.n = n;
    }

    public boolean isLegal(byte[] sequenceAsBytes, int maxUnfoundNgraphs) {
        ByteNGraph[] nGraphs = ByteNGraph.fromByteArray(sequenceAsBytes,n);
        int errorCounter = 0;
        for (ByteNGraph byteNGraph: nGraphs) {
            if (!map.contains(byteNGraph)) {
                errorCounter++;
                if (errorCounter >= maxUnfoundNgraphs) {
                    return false;
                }
            } else {
                errorCounter = 0;
            }
        }
        return true;
    }

    public boolean isLegal(PhoneSequence phones, int maxUnfoundNgraphs) {
        return isLegal(phones.getByteArray(),maxUnfoundNgraphs);
    }

    public void addFromString(String s) {
        byte[] asBytes = Phone.encode(s);
        ByteNGraph[] nGraphs = ByteNGraph.fromByteArray(asBytes,n);
        for (ByteNGraph nGraph: nGraphs) {
            map.add(nGraph);
        }
    }
}
