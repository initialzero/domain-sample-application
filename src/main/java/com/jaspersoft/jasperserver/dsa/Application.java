package com.jaspersoft.jasperserver.dsa;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dsa.common.ConsoleUtil;
import com.jaspersoft.jasperserver.dsa.initialization.InitializationStrategy;
import com.jaspersoft.jasperserver.dsa.initialization.InitializationStrategyFactory;
import com.jaspersoft.jasperserver.dsa.querexecution.FlatQueryExecutor;
import com.jaspersoft.jasperserver.dsa.querexecution.MultiAxesQueryExecutor;
import com.jaspersoft.jasperserver.dsa.querexecution.MultiLevelQueryExecutor;
import com.jaspersoft.jasperserver.dsa.querexecution.ProvidedQueryExecutor;
import com.jaspersoft.jasperserver.dsa.querexecution.QueryExecutor;
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
        AppConfiguration configuration = strategy.initConfiguration();
        // Initialization server session
        configuration.initSession();

        //Execute provided query and save result dataset to file
        QueryExecutor providedQueryExecutor = new ProvidedQueryExecutor(configuration);
        providedQueryExecutor.executeQuery().saveQueryExecutionResults("providedQuery.json");

        //Retrieve metadata, build and  execute flat query and save dataset to file
        QueryExecutor flatQueryExecutor = new FlatQueryExecutor(configuration);
        flatQueryExecutor.retrieveMetadata().buildQuery().executeQuery().saveQueryExecutionResults("flatQuery.json");

        //Retrieve metadata, build and  execute multi level query and save dataset to file
        QueryExecutor multiLevelQueryExecutor = new MultiLevelQueryExecutor(configuration);
        multiLevelQueryExecutor.retrieveMetadata().buildQuery().executeQuery().saveQueryExecutionResults("multiLevelQuery.json");

        //Retrieve metadata, build and  execute multi axes query and save dataset to file
        QueryExecutor multiAxesQueryExecutor = new MultiAxesQueryExecutor(configuration);
        multiAxesQueryExecutor.retrieveMetadata().buildQuery().executeQuery().saveQueryExecutionResults("multiAxesQuery.json");

        //Logout on the server and stoop the application
        configuration.stopApplication();
    }
}
