package com.jaspersoft.jasperserver.dsa.initialization;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    public AppConfiguration initConfiguration() {
        Properties properties = new Properties();
        consoleLogger.info("Enter path to configuration file: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String readLine = reader.readLine();
                properties.load(new FileInputStream(readLine));
                appLogger.info("The application is configured from " + readLine + " file");
                break;
            } catch (FileNotFoundException e) {
                consoleLogger.warn("File not found, please enter correct path to property file: ");
            } catch (IOException e) {
                appLogger.error("Error reading of file " + e.getCause());
                System.exit(1);
            }
        }

        return new AppConfiguration(properties);
    }
}
