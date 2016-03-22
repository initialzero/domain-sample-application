package com.jaspersoft.jasperserver.dsa;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dsa.common.ConsoleUtil;
import com.jaspersoft.jasperserver.dsa.domain.DomainUtil;
import com.jaspersoft.jasperserver.dsa.initialization.app.InitAppHelper;
import com.jaspersoft.jasperserver.dsa.initialization.app.InitializationStrategy;
import com.jaspersoft.jasperserver.dsa.initialization.app.InitializationStrategyFactory;
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

        // init application
        consoleLogger.info("Choose way of configuration (file or manual) [f/m]:");
        InitializationStrategy strategy = InitializationStrategyFactory.resolveStrategy(ConsoleUtil.readChar());

        // config application
        AppConfiguration configuration = InitAppHelper.initConfiguration(strategy.initConfiguration());
        configuration.initClient();
        configuration.initSession();

        // Create test resources
        DomainUtil domainUtil = new DomainUtil(configuration);
        domainUtil.createBaseFolder();
        //create domain with single data island
        domainUtil.createDomain();
        domainUtil.addCalculatedFields();
        domainUtil.addFilters();
        domainUtil.addDerivedTable();

        consoleLogger.info("Delete demonstration resources?[y/n]:");
        if (ConsoleUtil.readChar() == 'y') {
            domainUtil.deleteBaseFolder();
        }

        configuration.closeSession();;

    }

}