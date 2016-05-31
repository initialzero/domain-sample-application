package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientQuery;
import com.jaspersoft.jasperserver.dto.adhoc.query.group.ClientGroupBy;
import com.jaspersoft.jasperserver.dto.adhoc.query.order.ClientOrder;
import com.jaspersoft.jasperserver.dto.executions.ClientProvidedQueryExecution;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryParams;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryResultData;
import com.jaspersoft.jasperserver.dto.resources.domain.DataIslandsContainer;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhoc.queryexecution.QueryExecutionAdapter;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import java.util.List;
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
    public DataIslandsContainer retrieveMetadata(String domainUri) {
        this.domainUri = domainUri;
        return new DataIslandsContainer();
    }

    // there is not query for provided query
    @Override
    public ClientQuery buildQuery(DataIslandsContainer metadata) {
        return new ClientQuery() {
            @Override
            public <T extends ClientGroupBy> T getGroupBy() {
                return null;
            }

            @Override
            public List<? extends ClientOrder> getOrderBy() {
                return null;
            }
        };
    }

    // execution query and getting result dataset
    // provided query are supported only for Ad Hoc views
    @Override
    public String executeQuery(ClientQuery query) {
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
        }
        return operationResult.getSerializedContent();
    }

}
