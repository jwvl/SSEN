package io.utils;

/**
 * Created by janwillem on 23/09/16.
 */
public class PathUtils {
    public static String getFilenameFromPath(String path) {
        String[] split = path.split("/");
        return split[split.length-1];
    }
}
