package com.jaspersoft.jasperserver.dsa.initialization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class FileInitializationStrategy implements InitializationStrategy {
    public Properties initConfiguration() {
        Properties properties = new Properties();
        System.out.println("Enter path to configuration file:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String readLine = reader.readLine();
            properties.load(this.getClass().getClassLoader().getResourceAsStream(readLine));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }
}
