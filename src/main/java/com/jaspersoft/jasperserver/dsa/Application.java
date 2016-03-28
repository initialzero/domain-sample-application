package com.jaspersoft.jasperserver.dsa;

import com.jaspersoft.jasperserver.dsa.common.ConsoleUtil;
import com.jaspersoft.jasperserver.dsa.domain.DomainSamplesUtil;
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

        DomainSamplesUtil domainSamplesUtil = new DomainSamplesUtil(strategy.initConfiguration());
        domainSamplesUtil.initSession();

        // Create demonstration resources
        domainSamplesUtil.createBaseFolder();
        //create domain with single data island
        domainSamplesUtil.createBaseDomain();
        // Add to domain calculated fields and add them to presentation
        domainSamplesUtil.addCalculatedFields();
        // Add filters to calculated and normal fields
        domainSamplesUtil.addFilters();
        // Add to domain derived tables
        domainSamplesUtil.addDerivedTable();
        // Add to domain derived tables
        domainSamplesUtil.copyTable("public_customer");

        domainSamplesUtil.addCrossTableCalculatedField();

        domainSamplesUtil.addConstantCalculatedField(300);

        // Finish executing application
        consoleLogger.info("Delete demonstration resources?[y/n]:");
        if (ConsoleUtil.readChar() == 'y') {
            domainSamplesUtil.deleteBaseFolder();
        }
        domainSamplesUtil.stopApplication();

    }

}