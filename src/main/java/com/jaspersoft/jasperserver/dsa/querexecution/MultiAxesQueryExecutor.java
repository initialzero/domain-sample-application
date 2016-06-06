package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dsa.domain.DomainMetadataUtil;
import com.jaspersoft.jasperserver.dto.adhoc.datasource.ClientDataSourceField;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientMultiAxisQuery;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientQuery;
import com.jaspersoft.jasperserver.dto.adhoc.query.MultiAxisQueryBuilder;
import com.jaspersoft.jasperserver.dto.adhoc.query.field.ClientQueryLevel;
import com.jaspersoft.jasperserver.dto.executions.ClientMultiAxesQueryExecution;
import com.jaspersoft.jasperserver.dto.executions.ClientMultiAxesQueryResultData;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryParams;
import com.jaspersoft.jasperserver.dto.resources.domain.DataIslandsContainer;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhoc.queryexecution.QueryExecutionAdapter;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import java.io.InputStream;
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
public class MultiAxesQueryExecutor {

    private static final Logger appLogger = Logger.getLogger(MultiAxesQueryExecutor.class);
    private String domainUri;
    private AppConfiguration configuration;


    public MultiAxesQueryExecutor(AppConfiguration configuration) {
        this.configuration = configuration;
    }

    public DataIslandsContainer retrieveMetadata(String domainUri) {
        this.domainUri = domainUri;
        DomainMetadataUtil domainMetadataUtil = new DomainMetadataUtil(configuration);
        DataIslandsContainer dataIslandsContainer = domainMetadataUtil.fetchMetadata(domainUri);
        return dataIslandsContainer;
    }

    public ClientQuery buildQuery(DataIslandsContainer metadata) {
        appLogger.info("Build multi axes query for domain " + domainUri);

        // find elements for query in retrieved metadata
        List<PresentationSingleElement> presentationSingleElements = QueryBuilderUtil.
                extractPresentationSingleElements(metadata, "java.lang.Double", 2);

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

        ClientQuery query = qb.build().setLimit(100);
        return query;
    }

    public InputStream executeQuery(ClientQuery query) {
        appLogger.info("Start to execute multi axes query");
        ClientMultiAxesQueryExecution queryExecution = new ClientMultiAxesQueryExecution().
                setDataSourceUri(domainUri).
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
        OperationResult<ClientMultiAxesQueryResultData> operationResult = queryExecutionAdapter.
                execute(queryExecution);
        if (operationResult.getResponseStatus() == 200) {
            appLogger.info("Multi axes query was executed successfully");
        } else {
            appLogger.warn("Executing of multi axes query failed with response status " + operationResult.getResponseStatus());
            return null;
        }
        return  operationResult.getResponse().readEntity(InputStream.class);
    }
}
