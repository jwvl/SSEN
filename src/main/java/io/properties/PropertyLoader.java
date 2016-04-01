/**
 *
 */
package io.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author jwvl
 * @date 18/03/2016
 */
public class PropertyLoader {
    public static Properties getProperties(String path) {
        String filename = PropertyLoader.class.getClassLoader().getResource(path).getFile();
        Properties result = new Properties();
        FileInputStream in;
        try {
            in = new FileInputStream(filename);
            result.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
