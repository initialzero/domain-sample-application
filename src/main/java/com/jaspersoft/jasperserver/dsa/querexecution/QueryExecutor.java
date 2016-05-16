package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientQuery;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryResultData;
import com.jaspersoft.jasperserver.dto.resources.domain.DataIslandsContainer;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public abstract class QueryExecutor {
    protected final static String supermartDpmainUri = "/public/Samples/Domains/supermartDomain";
    protected final static String adhocViewUri = "/public/Samples/Ad_Hoc_Views/01__Geographic_Results_by_Segment";
    protected AppConfiguration configuration;
    protected DataIslandsContainer dataIslandsContainer;
    protected ClientQuery query;
    protected OperationResult<? extends ClientQueryResultData> operationResult;
    protected ClientQueryResultData queryResultData;

    public abstract QueryExecutor retrieveMetadata();

    public abstract QueryExecutor buildQuery();

    public abstract QueryExecutor executeQuery();

    public abstract void saveQueryExecutionResults();
}
