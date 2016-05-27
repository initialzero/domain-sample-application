package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dto.executions.ClientProvidedQueryExecution;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryParams;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryResultData;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhoc.queryexecution.QueryExecutionAdapter;
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
        super(configuration);
    }

    // retrieving of matadata is not necessary for provided query
    @Override
    public QueryExecutor retrieveMetadata() {
        return this;
    }

    // there is not query for provided query
    @Override
    public QueryExecutor buildQuery() {
        return this;
    }

    // execution query and getting result dataset
    // provided query are supported only for Ad Hoc views
    @Override
    public QueryExecutor executeQuery() {
        appLogger.info("Execute provided query for " + adhocViewUri);
        ClientProvidedQueryExecution queryExecution = new ClientProvidedQueryExecution().
                setDataSourceUri(adhocViewUri).
                setParams(new ClientQueryParams().setOffset(new int[]{0}).setPageSize(new int[]{100}));

        QueryExecutionAdapter<ClientQueryResultData> queryExecutionAdapter = configuration.getSession().
                queryExecutionService().
                providedQuery();

        if (configuration.getResponseFormat() != null) {
            if (configuration.getResponseFormat().toLowerCase().equals("xml")) {
                queryExecutionAdapter = queryExecutionAdapter.asXml();
            }
            if (configuration.getResponseFormat().toLowerCase().equals("json")) {
                queryExecutionAdapter = queryExecutionAdapter.asJson();
            }
        }

        // sending request to server and getting result dataset
        this.operationResult = queryExecutionAdapter.
                execute(queryExecution);
        if (operationResult.getResponseStatus() == 200) {
            appLogger.info("Provided query was executed successfully");
        } else {
            appLogger.warn("Executing of provided query failed with response status " + operationResult.getResponseStatus());
        }
        return this;
    }

}
