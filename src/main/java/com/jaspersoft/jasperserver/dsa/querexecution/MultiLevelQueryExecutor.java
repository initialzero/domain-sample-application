package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dsa.domain.DomainMetadataUtil;
import com.jaspersoft.jasperserver.dto.adhoc.datasource.ClientDataSourceField;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientMultiLevelQuery;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientQueryBuilder;
import com.jaspersoft.jasperserver.dto.adhoc.query.field.ClientQueryField;
import com.jaspersoft.jasperserver.dto.executions.ClientMultiLevelQueryExecution;
import com.jaspersoft.jasperserver.dto.executions.ClientMultiLevelQueryResultData;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryParams;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhoc.queryexecution.QueryExecutionAdapter;
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
public class MultiLevelQueryExecutor extends QueryExecutor {

    private static final Logger appLogger = Logger.getLogger(MultiLevelQueryExecutor.class);


    public MultiLevelQueryExecutor(AppConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public QueryExecutor retrieveMetadata() {
        DomainMetadataUtil domainMetadataUtil = new DomainMetadataUtil(configuration);
        this.dataIslandsContainer = domainMetadataUtil.fetchMetadata(supermartDpmainUri);
        return this;
    }

    @Override
    public QueryExecutor buildQuery() {
        appLogger.info("Build multi level query for domain " + supermartDpmainUri);
        PresentationSingleElement singleElement = extractPresentationSingleElement(dataIslandsContainer, "java.lang.Double");
        ClientQueryField queryField = new ClientQueryField().
                //setId!!!
                        setId("Sum1").
                setDataSourceField(new ClientDataSourceField().
                        setName(singleElement.getHierarchicalName()).
                        setType(singleElement.getType()));
        ClientQueryBuilder qb =
                select(singletonList(queryField), singletonList(countAll(queryField)));

        this.query = qb.build().setLimit(100);

        return this;
    }

    @Override
    public QueryExecutor executeQuery() {
        appLogger.info("Start to execute multi level query");
        ClientMultiLevelQueryExecution queryExecution = new ClientMultiLevelQueryExecution().
                setDataSourceUri(supermartDpmainUri).
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
        operationResult = queryExecutionAdapter.
                execute(queryExecution);
        if (operationResult.getResponseStatus() == 200) {
            appLogger.info("Multi level query was executed successfully");
        } else {
            appLogger.warn("Executing of multi level query failed with response status " + operationResult.getResponseStatus());
        }
        return this;
    }
}
