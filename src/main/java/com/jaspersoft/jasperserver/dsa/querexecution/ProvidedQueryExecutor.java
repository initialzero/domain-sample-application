package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dto.executions.ClientProvidedQueryExecution;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryParams;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryResultData;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhoc.queryexecution.QueryExecutionAdapter;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import java.io.InputStream;
import org.apache.log4j.Logger;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class ProvidedQueryExecutor {

    private static final Logger appLogger = Logger.getLogger(ProvidedQueryExecutor.class);
    private AppConfiguration configuration;

    public ProvidedQueryExecutor(AppConfiguration configuration) {
        this.configuration = configuration;
    }

    // execution query and getting result dataset
    // provided query are supported only for Ad Hoc views
    public InputStream executeQuery(String domainUri) {
        appLogger.info("Execute provided query for " + domainUri);
        ClientProvidedQueryExecution queryExecution = new ClientProvidedQueryExecution().
                setDataSourceUri(domainUri).
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
        OperationResult<ClientQueryResultData> operationResult = queryExecutionAdapter.
                execute(queryExecution);
        if (operationResult.getResponseStatus() == 200) {
            appLogger.info("Provided query was executed successfully");
        } else {
            appLogger.warn("Executing of provided query failed with response status " + operationResult.getResponseStatus());
            return null;
        }
        return operationResult.getResponse().readEntity(InputStream.class);
    }

}
