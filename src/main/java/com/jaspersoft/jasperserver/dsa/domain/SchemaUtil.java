package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dto.resources.domain.PresentationElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceSingleElement;
import com.jaspersoft.jasperserver.dto.resources.domain.SchemaElement;
import java.util.LinkedList;
import java.util.List;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class SchemaUtil {
    public static final String JAVA_LANG_STRING = "java.lang.String";
    public static final String JAVA_LANG_INTEGER = "java.lang.Integer";
    public static final String JAVA_UTIL_DATE = "java.util.Date";
    public static final String JAVA_MATH_BIG_DECIMAL = "java.math.BigDecimal";
    public static final String JAVA_LANG_LONG = "java.lang.Long";
    public static final String JAVA_LANG_SHORT = "java.lang.Short";
    public static final String JAVA_LANG_DOUBLE = "java.lang.Double";
    public static final String JAVA_LANG_BOOLEAN = "java.lang.Boolean";

    public static final String DATA_SOURCE = "FoodmartDataSourceJNDI";
    public static final String DATA_SOURCE_SCHEMA = "public";
    public static final String DATA_SOURCE_SCHEMA_ALIAS = "schema1";

    public static final String FULL_TABLE_NAME_0_AGG_11_01 = "public_agg_ll_01_sales_fact_1997";
    public static final String FULL_TABLE_NAME_1_CUSTOMER = "public_customer";
    public static final String FULL_TABLE_NAME_2_PRODUCT = "public_product";
    public static final String FULL_TABLE_NAME_3_AGG_C_14 = "public_agg_c_14_sales_fact_1997";
    public static final String FULL_TABLE_NAME_4_CUSTOMER_SALES = "public_customer_sales";

    public static final String TABLE_NAME_0_AGG_11_01 = "agg_ll_01_sales_fact_1997";
    public static final String TABLE_NAME_1_CUSTOMER = "customer";
    public static final String TABLE_NAME_2_PRODUCT = "product";
    public static final String TABLE_NAME_3_AGG_C_14 = "agg_c_14_sales_fact_1997";
    public static final String TABLE_NAME_4_CUSTOMER_SALES = "customer_sales";
    public static final String JOIN_TREE_1 = "JoinTree_1";
    public static final String JOIN_TREE_2 = "JoinTree_2";
    public static final String JOIN_TREE_3 = "JoinTree_3";
    public static final Character POINT = new Character('.');


    protected static String getPath(String source, String schema, String element) {
        return new StringBuilder().append(source).append(POINT).append(schema).append(POINT).append(element).toString();
    }


    protected static String getHierarchicalName(String fullTableName, String element) {
        return new StringBuilder().append(fullTableName).append(POINT).append(element).toString();
    }

    protected static PresentationGroupElement resourceToPresentationGroupElement(ResourceGroupElement resourceElement, String joinTreeName) {
        List<PresentationElement> presentationElements = new LinkedList<PresentationElement>();

        for (SchemaElement element : resourceElement.getElements()) {
            ResourceSingleElement castedElement = (ResourceSingleElement) element;
            PresentationSingleElement presentationSingleElement = resourceToPresentationSingleElement(castedElement, joinTreeName, resourceElement.getName());
            presentationElements.add(presentationSingleElement);
        }

        PresentationGroupElement presentationGroupElement = new PresentationGroupElement();
        presentationGroupElement.
                setName(resourceElement.getName()).
                setDescription(resourceElement.getName()).
                setDescriptionId("").
                setLabel(resourceElement.getName()).
                setLabelId("").
                setElements(presentationElements);
        return presentationGroupElement;
    }

    protected static PresentationSingleElement resourceToPresentationSingleElement(ResourceSingleElement resourceSingleElement,
                                                                                   String joinTreeName,
                                                                                   String parentElementName,
                                                                                   String elementName) {
        return new PresentationSingleElement().
                setName(elementName).
                setResourcePath(getPath(joinTreeName, parentElementName, elementName)).
                setDescription(elementName).
                setDescriptionId("").
                setLabel(resourceSingleElement.getName()).
                setLabelId("").
                setType(resourceSingleElement.getType()).
                setHierarchicalName(getHierarchicalName(parentElementName, elementName));
    }
    protected static PresentationSingleElement resourceToPresentationSingleElement(ResourceSingleElement resourceSingleElement,
                                                                                   String joinTreeName,
                                                                                   String parentElementName) {
         return resourceToPresentationSingleElement(resourceSingleElement,
                 joinTreeName,
                 parentElementName,
                 resourceSingleElement.getName());
    }

    protected static PresentationSingleElement getPresentationSingleElementName(PresentationGroupElement groupElement, String singleElementName) {
        List<PresentationElement> singleElements = groupElement.getElements();
        PresentationSingleElement result;
        for (PresentationElement singleElement : singleElements) {
            result = (PresentationSingleElement) singleElement;
            if (result.getName().equals(singleElementName)) {
                return result;
            }
        }
        return null;
    }
}
