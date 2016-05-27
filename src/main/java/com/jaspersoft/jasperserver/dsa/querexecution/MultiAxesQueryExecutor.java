package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dsa.domain.DomainMetadataUtil;
import com.jaspersoft.jasperserver.dto.adhoc.datasource.ClientDataSourceField;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientMultiAxisQuery;
import com.jaspersoft.jasperserver.dto.adhoc.query.MultiAxisQueryBuilder;
import com.jaspersoft.jasperserver.dto.adhoc.query.field.ClientQueryLevel;
import com.jaspersoft.jasperserver.dto.executions.ClientMultiAxesQueryExecution;
import com.jaspersoft.jasperserver.dto.executions.ClientMultiAxesQueryResultData;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryParams;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhoc.queryexecution.QueryExecutionAdapter;
import java.util.List;
import org.apache.log4j.Logger;

import static com.jaspersoft.jasperserver.dto.adhoc.query.ClientAggregates.sum;
import static com.jaspersoft.jasperserver.dto.adhoc.query.ClientQueryBuilder.ascByMember;
import static com.jaspersoft.jasperserver.dto.adhoc.query.MultiAxisQueryBuilder.aggRef;
import static java.util.Collections.singletonList;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class MultiAxesQueryExecutor extends QueryExecutor {

    private static final Logger appLogger = Logger.getLogger(MultiAxesQueryExecutor.class);


    public MultiAxesQueryExecutor(AppConfiguration configuration) {
        super(configuration);
    }

    @Override
    public QueryExecutor retrieveMetadata() {
        DomainMetadataUtil domainMetadataUtil = new DomainMetadataUtil(configuration);
        this.dataIslandsContainer = domainMetadataUtil.fetchMetadata(supermartDpmainUri);
        return this;
    }

    @Override
    public QueryExecutor buildQuery() {
        appLogger.info("Build multi axes query for domain " + supermartDpmainUri);

        // find elements for query in retrieved metadata
        List<PresentationSingleElement> presentationSingleElements = extractPresentationSingleElements(dataIslandsContainer, "java.lang.Double", 2);

        // use found elements for building multi level query with sum by found element, group by rows and order by field
        ClientDataSourceField dataSourceField = new ClientDataSourceField().
                setName(presentationSingleElements.get(0).getHierarchicalName()).
                setType(presentationSingleElements.get(0).getType());

        ClientQueryLevel queryLevel = (ClientQueryLevel) new ClientQueryLevel().setId("Id1").
                setDataSourceField(new ClientDataSourceField().
                        setName(presentationSingleElements.get(1).getHierarchicalName()).
                        setType(presentationSingleElements.get(1).getType()));

        MultiAxisQueryBuilder qb = MultiAxisQueryBuilder.select(sum(dataSourceField).setId("Sums1"))
                .groupByColumns(aggRef())
                .groupByRows(queryLevel)
                .orderBy(ascByMember(singletonList(presentationSingleElements.get(1).getHierarchicalName())));

        this.query = qb.build().setLimit(100);
        return this;
    }

    @Override
    public QueryExecutor executeQuery() {
        appLogger.info("Start to execute multi axes query");
        ClientMultiAxesQueryExecution queryExecution = new ClientMultiAxesQueryExecution().
                setDataSourceUri(supermartDpmainUri).
                setQuery((ClientMultiAxisQuery) query).
                setParams(new ClientQueryParams().setOffset(new int[]{0}).setPageSize(new int[]{100}));

        QueryExecutionAdapter<ClientMultiAxesQueryResultData> queryExecutionAdapter = configuration.getSession().
                queryExecutionService().
                multiAxesQuery();
        if (configuration.getResponseFormat() != null) {
            if (configuration.getResponseFormat().toLowerCase().equals("xml")) {
                queryExecutionAdapter = queryExecutionAdapter.asXml();
            }
            if (configuration.getResponseFormat().toLowerCase().equals("json")) {
                queryExecutionAdapter = queryExecutionAdapter.asJson();
            }
        }

        // send request to server ade get result dataset
        operationResult = queryExecutionAdapter.
                execute(queryExecution);
        if (operationResult.getResponseStatus() == 200) {
            appLogger.info("Multi axes query was executed successfully");
        } else {
            appLogger.warn("Executing of multi axes query failed with response status " + operationResult.getResponseStatus());
        }
        return this;
    }
}
