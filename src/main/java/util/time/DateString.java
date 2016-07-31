package util.time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by janwillem on 30/07/16.
 */
public class DateString {
    private final static SimpleDateFormat shortDate = new SimpleDateFormat("yyyyMMdd-HHmmss");

    public static String getShortDateString() {
        Date currentTime = new Date();
        return shortDate.format(currentTime);
    }
}
