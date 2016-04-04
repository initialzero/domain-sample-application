package com.jaspersoft.jasperserver.dsa.initialization;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class ManualInitializationStrategy implements InitializationStrategy {
    private static final Logger appLogger = Logger.getLogger(ManualInitializationStrategy.class);
    private static final Logger consoleLogger = Logger.getLogger("consoleLogger");

    public AppConfiguration initConfiguration() {
        Properties properties = new Properties();
        String[] initParams = {"url", "username", "password", "baseFolder"};

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            for (String initParam : initParams) {
                consoleLogger.info("Enter " + initParam + ":");
                String readLine = reader.readLine();
                appLogger.info("Application init parameters are:");
                appLogger.info(initParam + " : " + readLine);
                properties.setProperty(initParam, readLine);
            }
        } catch (IOException e) {
            appLogger.error("Error reading of parameter from console", e);
        } finally {
            consoleLogger.error("Error reading of parameter from console");
        }
        return new AppConfiguration(properties);
    }
}
