package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dsa.domain.DomainMetadataUtil;
import com.jaspersoft.jasperserver.dto.adhoc.datasource.ClientDataSourceField;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientMultiLevelQuery;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientQueryBuilder;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.ClientVariable;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.literal.ClientString;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.operator.comparison.ClientEquals;
import com.jaspersoft.jasperserver.dto.adhoc.query.field.ClientQueryField;
import com.jaspersoft.jasperserver.dto.executions.ClientMultiLevelQueryExecution;
import com.jaspersoft.jasperserver.dto.executions.ClientMultiLevelQueryResultData;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryParams;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhoc.queryexecution.QueryExecutionAdapter;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

import static com.jaspersoft.jasperserver.dto.adhoc.query.MultiLevelQueryBuilder.select;

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
        PresentationGroupElement expJoin = findGroupElement(dataIslandsContainer, "exp_join");
        PresentationGroupElement table = (PresentationGroupElement) expJoin.
                getElements().
                get(0);
        List<PresentationElement> fields = table.getElements();
        PresentationGroupElement groupElement = (PresentationGroupElement) fields.get(0);

        List<PresentationElement> elements = groupElement.getElements();
        List<ClientQueryField> queryFields = new LinkedList<ClientQueryField>();
        for (PresentationElement element : elements) {
            queryFields.add(new ClientQueryField().
                    setDataSourceField(new ClientDataSourceField().
                            setName((((PresentationSingleElement) element).getHierarchicalName()))));
        }

        ClientEquals clientEquals = new ClientEquals();
        clientEquals.
                getOperands().add(new ClientVariable(findSingleElement(groupElement, "account__account_type").getName()));
        clientEquals.
                getOperands().add(new ClientString("Expense"));

        ClientQueryBuilder qb =
                select(queryFields).where(clientEquals);

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
