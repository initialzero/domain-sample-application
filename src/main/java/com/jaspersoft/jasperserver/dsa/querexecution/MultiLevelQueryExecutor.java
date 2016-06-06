package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dsa.domain.DomainMetadataUtil;
import com.jaspersoft.jasperserver.dto.adhoc.datasource.ClientDataSourceField;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientMultiLevelQuery;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientQuery;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientQueryBuilder;
import com.jaspersoft.jasperserver.dto.adhoc.query.field.ClientQueryField;
import com.jaspersoft.jasperserver.dto.executions.ClientMultiLevelQueryExecution;
import com.jaspersoft.jasperserver.dto.executions.ClientMultiLevelQueryResultData;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryParams;
import com.jaspersoft.jasperserver.dto.resources.domain.DataIslandsContainer;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhoc.queryexecution.QueryExecutionAdapter;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import java.io.InputStream;
import org.apache.log4j.Logger;

import static com.jaspersoft.jasperserver.dto.adhoc.query.ClientAggregates.countAll;
import static com.jaspersoft.jasperserver.dto.adhoc.query.MultiLevelQueryBuilder.select;
import static java.util.Collections.singletonList;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class MultiLevelQueryExecutor {

    private static final Logger appLogger = Logger.getLogger(MultiLevelQueryExecutor.class);
    private AppConfiguration configuration;
    private String domainUri;


    public MultiLevelQueryExecutor(AppConfiguration configuration) {
        this.configuration = configuration;
    }

    public DataIslandsContainer retrieveMetadata(String domainUri) {
        this.domainUri = domainUri;
        DomainMetadataUtil domainMetadataUtil = new DomainMetadataUtil(configuration);
        DataIslandsContainer dataIslandsContainer = domainMetadataUtil.fetchMetadata(domainUri);
        return dataIslandsContainer;
    }

    public ClientQuery buildQuery(DataIslandsContainer metadata) {
        appLogger.info("Build multi level query for domain " + domainUri);
        // find element for query in retrieved metadata
        PresentationSingleElement singleElement = QueryBuilderUtil.extractPresentationSingleElement(metadata, "java.lang.Double");

        // use found element for building multi level query with sum by found element
        ClientQueryField queryField = new ClientQueryField().
                setId("Sum1").
                setDataSourceField(new ClientDataSourceField().
                        setName(singleElement.getHierarchicalName()).
                        setType(singleElement.getType()));
        ClientQueryBuilder qb =
                select(singletonList(queryField), singletonList(countAll(queryField)));

        ClientQuery query = qb.build().setLimit(100);

        return query;
    }

    public InputStream executeQuery(ClientQuery query) {
        appLogger.info("Start to execute multi level query");
        ClientMultiLevelQueryExecution queryExecution = new ClientMultiLevelQueryExecution().
                setDataSourceUri(domainUri).
                setQuery((ClientMultiLevelQuery) query).
                setParams(new ClientQueryParams().setOffset(new int[]{0}).setPageSize(new int[]{100}));

        QueryExecutionAdapter<ClientMultiLevelQueryResultData> queryExecutionAdapter = configuration.getSession().
                queryExecutionService().
                multiLevelQuery();
        if (configuration.getResponseFormat() != null) {
            if (configuration.getResponseFormat().toLowerCase().equals("xml")) {
                queryExecutionAdapter = queryExecutionAdapter.asXml();
            }
            if (configuration.getResponseFormat().toLowerCase().equals("json")) {
                queryExecutionAdapter = queryExecutionAdapter.asJson();
            }
        }

        // send request to server ade get result dataset
        OperationResult<ClientMultiLevelQueryResultData> operationResult = queryExecutionAdapter.
                execute(queryExecution);
        if (operationResult.getResponseStatus() == 200) {
            appLogger.info("Multi level query was executed successfully");
        } else {
            appLogger.warn("Executing of multi level query failed with response status " + operationResult.getResponseStatus());
            return  null;
        }
        return operationResult.getResponse().readEntity(InputStream.class);
    }
}
