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
public class ManualInitializationStrategy implements InitializationStrategy {
    public Properties initConfiguration() {
        Properties properties = new Properties();
        String[] initParams = {"uri", "username", "password", "baseFolder"};

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            for (String initParam : initParams) {
                System.out.println("Enter " + initParam + ":");
                String readLine = reader.readLine();
                properties.setProperty(initParam, readLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
