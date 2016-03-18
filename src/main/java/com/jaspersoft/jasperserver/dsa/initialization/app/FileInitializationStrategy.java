package com.jaspersoft.jasperserver.dsa.initialization.app;

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
public class FileInitializationStrategy implements InitializationStrategy {
    private static final Logger appLogger = Logger.getLogger(FileInitializationStrategy.class);
    private static final Logger consoleLogger = Logger.getLogger("consoleLogger");

    public Properties initConfiguration() {
        Properties properties = new Properties();
        consoleLogger.info("Enter path to configuration file:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String readLine = reader.readLine();
            appLogger.info("The application is configured from " + readLine + " file");
            properties.load(this.getClass().getClassLoader().getResourceAsStream(readLine));
        } catch (IOException e) {
            appLogger.error("Error reading of file name from console", e);
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
