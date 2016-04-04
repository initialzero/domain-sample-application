package com.jaspersoft.jasperserver.dsa.initialization;

import org.apache.log4j.Logger;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class InitializationStrategyFactory {
    private static Logger appLogger = Logger.getLogger(InitializationStrategyFactory.class);

    public static InitializationStrategy resolveStrategy(Character option) {
        InitializationStrategy strategy = null;
        switch (option) {
            case 'f':
                appLogger.info("The application is configured from file");
                strategy = new FileInitializationStrategy();
                break;
            case 'm':
                appLogger.info("The application is configured manually");
                strategy = new ManualInitializationStrategy();
                break;
            default:
                break;
        }
        return strategy;
    }
}
