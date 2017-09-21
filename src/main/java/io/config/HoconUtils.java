package io.config;

import com.typesafe.config.ConfigObject;

public class HoconUtils {


    public static String getStringFromObject(ConfigObject configObject, String key) {
        return configObject.get(key).unwrapped().toString();
    }

    public static double getDoubleFromObject(ConfigObject configObject, String key) {
        return Double.valueOf(configObject.get(key).unwrapped().toString());
    }
}
