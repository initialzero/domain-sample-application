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
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhoc.queryexecution.QueryExecutionAdapter;
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
        appLogger.info("Build multi axes query for domain " + supermartDpmainUri);
        PresentationGroupElement salesJoinGroupElement = findGroupElement(dataIslandsContainer, "sales_join");
        PresentationGroupElement salesFactAllGroupElement = findGroupElement(salesJoinGroupElement, "sales_fact_ALL");
        PresentationSingleElement salesFactAllStoreSales2013Element = findSingleElement(salesFactAllGroupElement, "sales_fact_ALL__store_sales_2013");

        ClientDataSourceField salesDSField = new ClientDataSourceField().
                setName(salesFactAllStoreSales2013Element.getHierarchicalName()).
                setType(salesFactAllStoreSales2013Element.getType());

        PresentationGroupElement salesStoreGroupElement = findGroupElement(salesFactAllGroupElement, "sales__store");
        PresentationGroupElement salesStoreRegionGroupElement = findGroupElement(salesStoreGroupElement, "sales__store__region");
        PresentationSingleElement salesStoreRegionSalesCityElement = findSingleElement(salesStoreRegionGroupElement, "sales__store__region__sales_city");

        ClientQueryLevel cityLevel = (ClientQueryLevel) new ClientQueryLevel().setId("city1").
                setDataSourceField(new ClientDataSourceField().
                        setName(salesStoreRegionSalesCityElement.getHierarchicalName()).
                        setType(salesStoreRegionSalesCityElement.getType()));

        MultiAxisQueryBuilder qb = MultiAxisQueryBuilder.select(sum(salesDSField).setId("Sumsales1"))
                .groupByColumns(aggRef())
                .groupByRows(cityLevel)
                .orderBy(ascByMember(singletonList(salesFactAllStoreSales2013Element.getHierarchicalName())));

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
