package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dto.executions.ClientProvidedQueryExecution;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryParams;
import org.apache.log4j.Logger;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class ProvidedQueryExecutor extends QueryExecutor {

    private static final Logger appLogger = Logger.getLogger(ProvidedQueryExecutor.class);

    public ProvidedQueryExecutor(AppConfiguration configuration) {
    this.configuration = configuration;
    }

    @Override
    public QueryExecutor retrieveMetadata() {
        return this;
    }

    @Override
    public QueryExecutor buildQuery() {
        return this;
    }

    @Override
    public QueryExecutor executeQuery() {
        appLogger.info("Execute provided query for " + adhocViewUri);
        ClientProvidedQueryExecution queryExecution = new ClientProvidedQueryExecution().
                setDataSourceUri(adhocViewUri).
                setParams(new ClientQueryParams().setOffset(new int[]{0}).setPageSize(new int[]{100}));

        this.operationResult = configuration.getSession().
                queryExecutionService().
                providedQuery().
                execute(queryExecution);
        if (operationResult.getResponseStatus() == 200) {
            appLogger.info("Provided query was executed successfully");
        } else {
            appLogger.warn("Executing of provided query failed with response status " + operationResult.getResponseStatus());
        }
        return this;
    }

    @Override
    public void saveQueryExecutionResults() {

    }
}
