package com.jaspersoft.jasperserver.dsa.querexecution;

import com.jaspersoft.jasperserver.dto.resources.domain.PresentationElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
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
public class QueryBuilderUtil {

    // method finds specified number of single elements in group element of fetched metadata
    public static List<PresentationSingleElement> findSingleElements(PresentationElement presentationElement, int num) {
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
    public static PresentationSingleElement extractPresentationSingleElement(PresentationGroupElement container, String type) {
        return extractPresentationSingleElements(container, type, 1).get(0);
    }

    // method finds specified number of single elements fetched metadata
    public static List<PresentationSingleElement> extractPresentationSingleElements(PresentationGroupElement container, String type, int num) {
        List<PresentationElement> dataIslands = container.getElements();
        List<PresentationSingleElement> singleElements = new ArrayList<PresentationSingleElement>(num);
        for (PresentationElement dataIsland : dataIslands) {
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
    public static List<PresentationSingleElement> findSingleElementsByType(PresentationElement presentationElement, String type, int num) {
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
