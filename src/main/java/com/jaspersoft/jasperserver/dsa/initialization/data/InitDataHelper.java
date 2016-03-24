package com.jaspersoft.jasperserver.dsa.initialization.data;

import com.jaspersoft.jasperserver.dto.resources.ClientReference;
import com.jaspersoft.jasperserver.dto.resources.domain.ClientDomain;
import com.jaspersoft.jasperserver.dto.resources.domain.Join;
import com.jaspersoft.jasperserver.dto.resources.domain.JoinInfo;
import com.jaspersoft.jasperserver.dto.resources.domain.JoinResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ReferenceElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceSingleElement;
import com.jaspersoft.jasperserver.dto.resources.domain.Schema;
import com.jaspersoft.jasperserver.dto.resources.domain.SchemaElement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class InitDataHelper {

    private static final Logger appLogger = Logger.getLogger(InitDataHelper.class);

    public static final String DATA_SOURCE = "FoodmartDataSourceJNDI";
    public static final String DATA_SOURCE_SCHEMA = "public";
    public static final String DATA_SOURCE_SCHEMA_ALIAS = "schema1";
    public static final String FULL_TABLE_NAME_0 = "public_agg_ll_01_sales_fact_1997";
    public static final String FULL_TABLE_NAME_1 = "public_customer";
    public static final String FULL_TABLE_NAME_2 = "public_product";

    public static final String JOIN_TREE_1 = "JoinTree_1";

    public static final String JAVA_LANG_STRING = "java.lang.String";
    public static final String JAVA_LANG_INTEGER = "java.lang.Integer";
    public static final String JAVA_UTIL_DATE = "java.util.Date";
    public static final String JAVA_MATH_BIG_DECIMAL = "java.math.BigDecimal";
    public static final String JAVA_LANG_LONG = "java.lang.Long";
    public static final String JAVA_LANG_SHORT = "java.lang.Short";
    public static final String JAVA_LANG_DOUBLE = "java.lang.Double";
    public static final String JAVA_LANG_BOOLEAN = "java.lang.Boolean";
    public static final String TABLE_NAME_0 = "agg_ll_01_sales_fact_1997";
    public static final String TABLE_NAME_1 = "customer";
    public static final String TABLE_NAME_2 = "product";


    private static ClientDomain clientDomain;
    private static ResourceGroupElement table0;
    private static ResourceGroupElement table1;
    private static ResourceGroupElement table2;


    public static ClientDomain fetchDomain(String baseFolder, String domainName, String dataSourceUri) {
        if (clientDomain != null) {
            return clientDomain;
        }
        appLogger.info("Start to create domain");
        clientDomain = new ClientDomain().
                setUri(baseFolder + "/" + domainName).
                setLabel(domainName).
                setDataSource(new ClientReference().setUri(dataSourceUri)).
                setSchema(fetchSchema());

        return clientDomain;
    }

    private static Schema fetchSchema() {
        appLogger.info("Start to create domain schema");
        Schema schema = new Schema();
        schema.setResources(initResources());
        schema.setPresentation(initPresentations());
        appLogger.info("Domain schema was created successfully");
        return schema;
    }

    private static List<ResourceElement> initResources() {

        appLogger.info("Start to add resources to domain");

        // init tables
        List<SchemaElement> tables = new ArrayList<SchemaElement>();
        appLogger.info("Add tables to schema");
        table0 = fetchTable(DATA_SOURCE_SCHEMA, TABLE_NAME_0, DATA_SOURCE);
        table1 = fetchTable(DATA_SOURCE_SCHEMA, TABLE_NAME_1, DATA_SOURCE);
        table2 = fetchTable(DATA_SOURCE_SCHEMA, TABLE_NAME_2, DATA_SOURCE);
        tables.add(table0);
        tables.add(table1);
        tables.add(table2);
// init schemas
        ResourceGroupElement publicSchema = new ResourceGroupElement().
                setName(DATA_SOURCE_SCHEMA_ALIAS).
                setSourceName(DATA_SOURCE_SCHEMA).
                setElements(tables);
        List<SchemaElement> schemas = new ArrayList<SchemaElement>();
        schemas.add(publicSchema);

        appLogger.info("Add schema to datasource");
        //init data source
        ResourceGroupElement dataSource = new ResourceGroupElement().
                setName(DATA_SOURCE).
                setElements(schemas);
        List<ResourceElement> resources = new ArrayList<ResourceElement>();
        appLogger.info("Add datasource to resources");
        resources.add(dataSource);

        appLogger.info("Join resources");
        List<Join> joins = new ArrayList<Join>();

// init joins
        joins.add(new Join().
                setLeft(FULL_TABLE_NAME_0).
                setRight(FULL_TABLE_NAME_1).
                setExpression("public_agg_ll_01_sales_fact_1997.customer_id == public_customer.customer_id").
                setWeight(1)
                .setType(Join.JoinType.inner));
        joins.add(new Join().
                setLeft(FULL_TABLE_NAME_0).
                setRight(FULL_TABLE_NAME_2).
                setExpression("public_agg_ll_01_sales_fact_1997.product_id == public_product.product_id").
                setWeight(1).setType(Join.JoinType.inner));
        JoinInfo joinInfo = new JoinInfo().setIncludeAllDataIslandJoins(false).setSuppressCircularJoins(false).setJoins(joins);

        List<SchemaElement> joinResourceElements = new ArrayList<SchemaElement>();
        joinResourceElements.add(new ReferenceElement().
                setName(FULL_TABLE_NAME_2).
                setReferencePath("FoodmartDataSourceJNDI.schema1.public_product"));
        joinResourceElements.add(new ReferenceElement().
                setName(FULL_TABLE_NAME_1).
                setReferencePath("FoodmartDataSourceJNDI.schema1.public_customer"));
        joinResourceElements.add(new ReferenceElement().
                setName(FULL_TABLE_NAME_0).
                setReferencePath("FoodmartDataSourceJNDI.schema1.public_agg_ll_01_sales_fact_1997"));

        appLogger.info("Add joins to join group");
        JoinResourceGroupElement joinResourceGroupElement = new JoinResourceGroupElement().
                setName(JOIN_TREE_1).
                setJoinInfo(joinInfo).
                setElements(joinResourceElements);
        appLogger.info("add joinGroup to schema");
        resources.add(joinResourceGroupElement);
        appLogger.info("Join resources");
        appLogger.info("Adding  resources to domain finished successfully");
        return resources;

    }

    private static List<PresentationElement> initPresentations() {
        appLogger.info("Start to add presentations to domain");

        PresentationGroupElement presentationGroupElement0 = resourceToPresentationGroupElement(table0);

        PresentationGroupElement presentationGroupElement1 = resourceToPresentationGroupElement(table1);

        PresentationGroupElement presentationGroupElement2 = resourceToPresentationGroupElement(table2);

        List<PresentationElement> presentationElements = new ArrayList<PresentationElement>();
        presentationElements.add(presentationGroupElement0);
        presentationElements.add(presentationGroupElement1);
        presentationElements.add(presentationGroupElement2);
        appLogger.info("Adding  presentations  to domain finished successfully");
        return presentationElements;
    }


    private static ResourceGroupElement fetchTable(String dataSourceSchema, String tableName, String daraSource) {
        String fullTableName = dataSourceSchema + "_" + tableName;

        ResourceGroupElement resourceGroupElement = new ResourceGroupElement();

        if (fullTableName.equals(FULL_TABLE_NAME_0)) {
            resourceGroupElement.
                    setName(FULL_TABLE_NAME_0).
                    setSourceName(TABLE_NAME_0).
                    setElements(new ArrayList<SchemaElement>());
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("customer_id").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("fact_count").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("product_id").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("store_cost").setType(JAVA_MATH_BIG_DECIMAL));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("store_sales").setType(JAVA_MATH_BIG_DECIMAL));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("time_id").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("unit_sales").setType(JAVA_MATH_BIG_DECIMAL));
            appLogger.info("Table " + FULL_TABLE_NAME_0 + " fetched successfully");
            return resourceGroupElement;
        }

        if (fullTableName.equals(FULL_TABLE_NAME_1)) {
            resourceGroupElement.
                    setName(FULL_TABLE_NAME_1).
                    setSourceName(TABLE_NAME_1).
                    setElements(new ArrayList<SchemaElement>());
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("account_num").setType(JAVA_LANG_LONG));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("address1").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("address2").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("address3").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("address4").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("birthdate").setType(JAVA_UTIL_DATE));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("city").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("country").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("customer_id").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("customer_region_id").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("date_accnt_opened").setType(JAVA_UTIL_DATE));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("education").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("fname").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("fullname").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("gender").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("houseowner").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("lname").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("marital_status").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("member_card").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("mi").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("num_cars_owned").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("num_children_at_home").setType(JAVA_LANG_SHORT));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("occupation").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("phone1").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("phone2").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("postal_code").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("state_province").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("total_children").setType(JAVA_LANG_SHORT));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("yearly_income").setType(JAVA_LANG_STRING));
            appLogger.info("Table " + FULL_TABLE_NAME_0 + " fetched successfully");
            return resourceGroupElement;
        }
        if (fullTableName.equals(FULL_TABLE_NAME_2)) {
            resourceGroupElement.
                    setName(FULL_TABLE_NAME_2).
                    setSourceName(TABLE_NAME_2).
                    setElements(new ArrayList<SchemaElement>());
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("brand_name").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("cases_per_pallet").setType(JAVA_LANG_SHORT));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("gross_weight").setType(JAVA_LANG_DOUBLE));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("low_fat").setType(JAVA_LANG_BOOLEAN));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("net_weight").setType(JAVA_LANG_DOUBLE));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("product_class_id").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("product_id").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("product_name").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("recyclable_package").setType(JAVA_LANG_BOOLEAN));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("shelf_depth").setType(JAVA_LANG_DOUBLE));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("shelf_height").setType(JAVA_LANG_DOUBLE));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("shelf_width").setType(JAVA_LANG_DOUBLE));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("sku").setType(JAVA_LANG_LONG));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("srp").setType(JAVA_MATH_BIG_DECIMAL));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("units_per_case").setType(JAVA_LANG_SHORT));
            appLogger.info("Table " + FULL_TABLE_NAME_0 + " fetched successfully");
            return resourceGroupElement;
        }
        return null;
    }

    public static PresentationGroupElement resourceToPresentationGroupElement(ResourceGroupElement resourceElement) {
        List<PresentationElement> presentationElements = new LinkedList<PresentationElement>();

        for (SchemaElement element : resourceElement.getElements()) {
            ResourceSingleElement castedElement = (ResourceSingleElement) element;
            PresentationSingleElement presentationSingleElement = new PresentationSingleElement().
                    setName(castedElement.getName()).
                    setResourcePath(getPath(JOIN_TREE_1, resourceElement.getName(), castedElement.getName())).
                    setDescription(castedElement.getName()).
                    setDescriptionId("").
                    setLabel(castedElement.getName()).
                    setLabelId("").
                    setType(castedElement.getType());
            presentationElements.add(presentationSingleElement);

            if ((resourceElement.getName().equals(table1.getName()) && castedElement.getName().equals("customer_id")) ||
                    (resourceElement.getName().equals(table2.getName()) && castedElement.getName().equals("product_id"))) {
                String newName = castedElement.getName() + "1";
                presentationSingleElement.setName(newName).setDescription(newName);
            }
        }
        PresentationGroupElement presentationGroupElement = new PresentationGroupElement();
        presentationGroupElement.
                setName(resourceElement.getName()).
                setResourcePath(JOIN_TREE_1).
                setDescription(resourceElement.getName()).
                setDescriptionId("").
                setLabel(resourceElement.getName()).
                setLabelId("").
                setElements(presentationElements);
        return presentationGroupElement;
    }

    private static String getPath(String source, String schema, String element) {
        Character point = new Character('.');
        return new StringBuilder().append(source).append(point).append(schema).append(point).append(element).toString();
    }

}
