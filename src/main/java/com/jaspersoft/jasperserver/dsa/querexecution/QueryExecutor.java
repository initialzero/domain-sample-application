package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientQuery;
import com.jaspersoft.jasperserver.dto.executions.ClientQueryResultData;
import com.jaspersoft.jasperserver.dto.resources.domain.DataIslandsContainer;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
    protected File resultDir;

    public abstract QueryExecutor retrieveMetadata();

    public abstract QueryExecutor buildQuery();

    public abstract QueryExecutor executeQuery();

    public void saveQueryExecutionResults(String filename) {
        if (resultDir == null) {
            resultDir = new File(configuration.getResultDirectory());
        }
        if (!resultDir.exists()) {
            resultDir.mkdirs();
        }
        try {
            File file = new File(configuration.getResultDirectory() + File.separator + filename);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(operationResult.getSerializedContent().getBytes());
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected PresentationGroupElement findGroupElement(DataIslandsContainer container, String elementName) {
        return (PresentationGroupElement) this.findElement(container.getDataIslands(), elementName);
    }

    protected PresentationGroupElement findGroupElement(PresentationGroupElement container, String elementName) {
        return (PresentationGroupElement) this.findElement(container.getElements(), elementName);
    }

    protected PresentationSingleElement findSingleElement(PresentationGroupElement groupElement, String elementName) {
        return (PresentationSingleElement) this.findElement(groupElement.getElements(), elementName);
    }

    protected PresentationElement findElement(List<? extends PresentationElement> presentationElements, String elementName) {
        for (PresentationElement element : presentationElements) {
            if (element.getName().equals(elementName)) {
                return element;
            }
        }
        return null;
    }
}
