package util.string;

import java.util.List;

public class StringPrinter {

    public static String linearizeList(List<? extends Object> toPrint, String separator) {
        int nItems = toPrint.size();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < nItems; i++) {
            result.append(toPrint.get(i));
            if (i < nItems - 1)
                result.append(separator);
        }
        return result.toString();
    }

}
