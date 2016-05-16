package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dsa.domain.DomainMetadataUtil;
import com.jaspersoft.jasperserver.dto.adhoc.datasource.ClientDataSourceField;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientMultiLevelQuery;
import com.jaspersoft.jasperserver.dto.adhoc.query.field.ClientQueryField;
import com.jaspersoft.jasperserver.dto.adhoc.query.select.ClientSelect;
import com.jaspersoft.jasperserver.dto.executions.ClientMultiLevelQueryExecution;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryParams;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import java.util.List;
import org.apache.log4j.Logger;

import static java.util.Collections.singletonList;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class FlatQueryExecutor extends QueryExecutor {

    private static final Logger appLogger = Logger.getLogger(FlatQueryExecutor.class);


    public FlatQueryExecutor(AppConfiguration configuration) {
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
        appLogger.info("Build flat query for domain " + supermartDpmainUri);
        StringBuilder name = new StringBuilder();
        PresentationGroupElement presentationGroupElement = dataIslandsContainer.
                getDataIslands().
                get(0);
        name.append(presentationGroupElement.getName()).append(".");
        PresentationGroupElement table = (PresentationGroupElement) presentationGroupElement.
                getElements().
                get(0);
        name.append(table.getName()).append(".");
        List<PresentationElement> fields = table.getElements();
        PresentationGroupElement groupElement = (PresentationGroupElement) fields.get(0);

        List<ClientQueryField> queryFields = singletonList(new ClientQueryField().
                setDataSourceField(new ClientDataSourceField().
                        setName(((PresentationSingleElement) groupElement.getElements().get(0)).getHierarchicalName())));

        this.query =
                new ClientMultiLevelQuery().
                setSelect(new ClientSelect().setFields(queryFields)).setLimit(1000);

        return this;
    }

    @Override
    public QueryExecutor executeQuery() {
        appLogger.info("Start to Execute flat query");
        ClientMultiLevelQueryExecution queryExecution = new ClientMultiLevelQueryExecution().
                setDataSourceUri(supermartDpmainUri).
                setQuery((ClientMultiLevelQuery) query).
                setParams(new ClientQueryParams().setOffset(new int[]{0}).setPageSize(new int[]{100}));

        operationResult = configuration.getSession().
                queryExecutionService().
                flatQuery().
                execute(queryExecution);
        if (operationResult.getResponseStatus() == 200) {
            appLogger.info("Flat query was executed successfully");
        } else {
            appLogger.warn("Executing of flat query failed with response status " + operationResult.getResponseStatus());
        }
        return this;
    }

    @Override
    public void saveQueryExecutionResults() {

    }

}
