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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

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

    protected PresentationSingleElement extractPresentationSingleElement(DataIslandsContainer container, String type) {
        return extractPresentationSingleElements(container, type, 1).get(0);
    }

    protected PresentationSingleElement findSingleElement(PresentationElement presentationElement) {
        if (presentationElement instanceof PresentationSingleElement) {
            return (PresentationSingleElement) presentationElement;
        }
        List<PresentationElement> elements = ((PresentationGroupElement) presentationElement).getElements();
        for (PresentationElement element : elements) {
            return findSingleElement(element);
        }
        return null;
    }

    protected List<PresentationSingleElement> extractPresentationSingleElements(DataIslandsContainer container, String type, int num) {
        List<PresentationGroupElement> dataIslands = container.getDataIslands();
        List<PresentationSingleElement> singleElements = new ArrayList<PresentationSingleElement>(num);
        for (PresentationGroupElement dataIsland : dataIslands) {
            singleElements.addAll(findSingleElementsByType(dataIsland, type, num));
            if (singleElements.size() == num) {
                return singleElements;
            }
        }
        return singleElements;
    }

    protected List<PresentationSingleElement> findSingleElementsByType(PresentationElement presentationElement, String type, int num) {
        if (presentationElement instanceof PresentationSingleElement) {

            if (((PresentationSingleElement) presentationElement).getType().equals(type)) {
                return asList((PresentationSingleElement) presentationElement);
            }
            return Collections.emptyList();
        }
        List<PresentationElement> elements = ((PresentationGroupElement) presentationElement).getElements();
        List<PresentationSingleElement> resultElements = new ArrayList<PresentationSingleElement>(num);
        for (PresentationElement element : elements) {
            List<PresentationSingleElement> singleElementsByType = findSingleElementsByType(element, type, num);
            resultElements.addAll(singleElementsByType);
            if (resultElements.size() == num) {
                return resultElements;
            }
        }
        return resultElements;
    }

}
