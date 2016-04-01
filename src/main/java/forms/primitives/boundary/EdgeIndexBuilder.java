package forms.primitives.boundary;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by janwillem on 28/03/16.
 */
public class EdgeIndexBuilder {

    private static IndexRepresentation representation = loadRepresentationFromConfig();


    public static EdgeIndex newBitsetIndex(int size) {
        return new BitsetEdgeIndex(size);
    }

    public static EdgeIndex newBooleanEdgeIndex(int size) {
        return new BooleanEdgeIndex(size);
    }

    public static EdgeIndex fromIntArray(Edge edgeType, int[] positions, int length) {
        EdgeIndex result = getEmpty(length);
        for (int i : positions) {
            result.set(edgeType, i);
        }
        return result;
    }

    private static IndexRepresentation loadRepresentationFromConfig() {
        Config config = ConfigFactory.load();
        String stringRead = config.getString("system.edgeIndexRepresentation");
        return IndexRepresentation.valueOf(stringRead);
    }

    public static EdgeIndex getEmpty(int length) {
        EdgeIndex result = null;
        switch (representation) {
            case BITSET:
                result = new BitsetEdgeIndex(length);
                break;
            case BOOLEAN:
                result = new BooleanEdgeIndex(length);
                break;
        }
        return result;
    }
}
