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

        // Initialization and configuration of application
        consoleLogger.info("Choose way of configuration (file or manual) [f/m]:");
        InitializationStrategy strategy = InitializationStrategyFactory.resolveStrategy(ConsoleUtil.readChar());
        AppConfiguration configuration = InitAppHelper.initApplication(strategy.initConfiguration());

        // Create demonstration resources
        DomainUtil domainUtil = new DomainUtil(configuration);
        domainUtil.createBaseFolder();
        //create domain with single data island
        domainUtil.createDomain();
        // Add to domain calculated fields and add them to presentation
        domainUtil.addCalculatedFields();
        // Add filters to calculated and normal fields
        domainUtil.addFilters();
        // Add to domain derived tables
        domainUtil.addDerivedTable();
        // Add to domain derived tables
        domainUtil.copyTable("public_customer");

        // Finish executing application
        consoleLogger.info("Delete demonstration resources?[y/n]:");
        if (ConsoleUtil.readChar() == 'y') {
            domainUtil.deleteBaseFolder();
        }
        configuration.stopApplication();

    }

}