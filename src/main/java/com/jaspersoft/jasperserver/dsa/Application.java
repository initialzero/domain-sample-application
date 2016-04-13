package com.jaspersoft.jasperserver.dsa;

import com.jaspersoft.jasperserver.dsa.common.ConsoleUtil;
import com.jaspersoft.jasperserver.dsa.initialization.InitializationStrategy;
import com.jaspersoft.jasperserver.dsa.initialization.InitializationStrategyFactory;
import org.apache.log4j.Logger;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class Application {
    private static Logger appLogger = Logger.getLogger(Application.class);
    private static Logger consoleLogger = Logger.getLogger("consoleLogger");

    public static void main(String[] args) {
        appLogger.info("Initialization of application");

        // Initialization and configuration of application
        consoleLogger.info("Choose way of configuration (file or manual) [f/m]: ");
        InitializationStrategy strategy = InitializationStrategyFactory.resolveStrategy(ConsoleUtil.readChar(new Character[]{'f', 'm'}));

//        DomainSamplesUtil domainSamplesUtil = new DomainSamplesUtil(strategy.initConfiguration());
//        domainSamplesUtil.initSession();
        // domainSamplesUtil.stopApplication();
    }
}
