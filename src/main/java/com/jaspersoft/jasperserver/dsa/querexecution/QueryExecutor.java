package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientQuery;
import com.jaspersoft.jasperserver.dto.resources.domain.DataIslandsContainer;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

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
    private static final Logger appLogger = Logger.getLogger(QueryExecutor.class);
    protected AppConfiguration configuration;
    protected String domainUri;
    protected File resultDir;

    public QueryExecutor() {
    }

    public QueryExecutor(AppConfiguration configuration) {
        this.configuration = configuration;
    }

    public abstract DataIslandsContainer retrieveMetadata(String domainUri);

    public abstract ClientQuery buildQuery(DataIslandsContainer metadata);

    public abstract String executeQuery(ClientQuery query);

    public void saveQueryExecutionResults(String resultDataSet, String filename) {
        // prepare result directory
        if (resultDataSet == null) {
            appLogger.warn("Result data set is null");
            return;
        }
        if (filename == null || filename.isEmpty()) {
            appLogger.warn("File name is not valid");
            return;
        }
        if (resultDir == null) {
            resultDir = new File(configuration.getResultDirectory());
        }
        if (!resultDir.exists()) {
            resultDir.mkdirs();
        }

        try {
            File file = new File(configuration.getResultDirectory() + File.separator + filename);
            FileOutputStream outputStream = new FileOutputStream(file);
            //write server response entity to file
            outputStream.write(resultDataSet.getBytes());
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// method finds specified number of single elements in group element of fetched metadata
    protected List<PresentationSingleElement> findSingleElements(PresentationElement presentationElement, int num) {
        if (presentationElement instanceof PresentationSingleElement) {
            return asList((PresentationSingleElement) presentationElement);
        }
        List<PresentationElement> elements = ((PresentationGroupElement) presentationElement).getElements();
        List<PresentationSingleElement> resulrList = new ArrayList<PresentationSingleElement>(num);
        for (PresentationElement element : elements) {
            List<PresentationSingleElement> singleElementList = findSingleElements(element, num);
            if (singleElementList != null) {
                resulrList.addAll(singleElementList);
            }
            if (resulrList.size() == num) {
                return resulrList;
            }
        }
        return null;
    }

    // method finds one single element in fetched metadata
    protected PresentationSingleElement extractPresentationSingleElement(DataIslandsContainer container, String type) {
        return extractPresentationSingleElements(container, type, 1).get(0);
    }

    // method finds specified number of single elements fetched metadata
    protected List<PresentationSingleElement> extractPresentationSingleElements(DataIslandsContainer container, String type, int num) {
        List<PresentationGroupElement> dataIslands = container.getDataIslands();
        List<PresentationSingleElement> singleElements = new ArrayList<PresentationSingleElement>(num);
        for (PresentationGroupElement dataIsland : dataIslands) {
            List<PresentationSingleElement> singleElementsByType = findSingleElementsByType(dataIsland, type, num);
            if (singleElementsByType != null) {
                singleElements.addAll(singleElementsByType);
            }
            if (singleElements.size() == num) {
                return singleElements;
            }
        }
        return singleElements;
    }

    // method finds specified number of single elements in group element of fetched metadata
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
