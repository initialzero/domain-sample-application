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
    private static ClientDomain clientDomain;

    public static final String JAVA_LANG_STRING = "java.lang.String";
    public static final String JAVA_LANG_INTEGER = "java.lang.Integer";
    public static final String JAVA_UTIL_DATE = "java.util.Date";
    public static final String JAVA_MATH_BIG_DECIMAL = "java.math.BigDecimal";
    public static final String JAVA_LANG_LONG = "java.lang.Long";
    public static final String JAVA_LANG_SHORT = "java.lang.Short";
    public static final String JAVA_LANG_DOUBLE = "java.lang.Double";
    public static final String JAVA_LANG_BOOLEAN = "java.lang.Boolean";


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
        tables.add(fetchTable("public", "public_agg_ll_01_sales_fact_1997", "FoodmartDataSourceJNDI"));
        tables.add(fetchTable("public", "public_customer", "FoodmartDataSourceJNDI"));
        tables.add(fetchTable("public", "public_product", "FoodmartDataSourceJNDI"));
// init schemas
        ResourceGroupElement publicSchema = new ResourceGroupElement().
                setName("schema1").
                setSourceName("public").
                setElements(tables);
        List<SchemaElement> schemas = new ArrayList<SchemaElement>();
        schemas.add(publicSchema);

        appLogger.info("Add schema to datasource");
        //init data source
        ResourceGroupElement dataSource = new ResourceGroupElement().
                setName("FoodmartDataSourceJNDI").
                setElements(schemas);
        List<ResourceElement> resources = new ArrayList<ResourceElement>();
        appLogger.info("Add datasource to resources");
        resources.add(dataSource);

// add resources to schema
        appLogger.info("Join resources");
        List<Join> joins = new ArrayList<Join>();


        joins.add(new Join().
                setLeft("public_agg_ll_01_sales_fact_1997").
                setRight("public_customer").
                setExpression("public_agg_ll_01_sales_fact_1997.customer_id == public_customer.customer_id").
                setWeight(1)
                .setType(Join.JoinType.inner));
        joins.add(new Join().
                setLeft("public_agg_ll_01_sales_fact_1997").
                setRight("public_product").
                setExpression("public_agg_ll_01_sales_fact_1997.product_id == public_product.product_id").
                setWeight(1).setType(Join.JoinType.inner));
        JoinInfo joinInfo = new JoinInfo().setIncludeAllDataIslandJoins(false).setSuppressCircularJoins(false).setJoins(joins);

        List<SchemaElement> joinResourceElements = new ArrayList<SchemaElement>();
        joinResourceElements.add(new ReferenceElement().setName("public_product").setReferencePath("FoodmartDataSourceJNDI.schema1.public_product"));
        joinResourceElements.add(new ReferenceElement().setName("public_customer").setReferencePath("FoodmartDataSourceJNDI.schema1.public_customer"));
        joinResourceElements.add(new ReferenceElement().setName("public_agg_ll_01_sales_fact_1997").setReferencePath("FoodmartDataSourceJNDI.schema1.public_agg_ll_01_sales_fact_1997"));

        appLogger.info("Add joins to join group");
        JoinResourceGroupElement joinResourceGroupElement = new JoinResourceGroupElement().
                setName("JoinTree_1").
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

        List<PresentationElement> presentationElements1 = new ArrayList<PresentationElement>();
        String name = "customer_id";
        presentationElements1.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_agg_ll_01_sales_fact_1997.customer_id").
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_INTEGER));
        name = "fact_count";
        presentationElements1.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_agg_ll_01_sales_fact_1997.fact_count").
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_INTEGER));
        name = "product_id";
        presentationElements1.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_agg_ll_01_sales_fact_1997.product_id").
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_INTEGER));
        name = "store_cost";
        presentationElements1.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_agg_ll_01_sales_fact_1997.store_cost").
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_MATH_BIG_DECIMAL));
        name = "store_sales";
        presentationElements1.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_agg_ll_01_sales_fact_1997.store_sales").
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_MATH_BIG_DECIMAL));
        name = "time_id";
        presentationElements1.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_agg_ll_01_sales_fact_1997.time_id").
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_INTEGER));
        name = "unit_sales";
        presentationElements1.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_agg_ll_01_sales_fact_1997.unit_sales").
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_MATH_BIG_DECIMAL));
        PresentationGroupElement presentationGroupElement1 = new PresentationGroupElement().
                setName("public_agg_ll_01_sales_fact_1997").
                setResourcePath("JoinTree_1").
                setDescription("public_agg_ll_01_sales_fact_1997").
                setDescriptionId("").
                setLabel("public_agg_ll_01_sales_fact_1997").
                setLabelId("").
                setElements(presentationElements1);

        List<PresentationElement> presentationElements2 = new ArrayList<PresentationElement>();
        name = "account_num";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_LONG));
        name = "address1";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "address2";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "address3";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "address4";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "birthdate";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_UTIL_DATE));
        name = "city";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "country";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "customer_id1";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer.customer_id").
                setDescriptionId("").
                setDescription("customer_id1").
                setLabel("customer_id").
                setLabelId("").
                setType(JAVA_LANG_INTEGER));
        name = "customer_region_id";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_INTEGER));
        name = "date_accnt_opened";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_UTIL_DATE));
        name = "education";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "fname";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "fullname";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "gender";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "houseowner";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "lname";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "marital_status";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "member_card";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "mi";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "num_cars_owned";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_INTEGER));
        name = "num_children_at_home";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_SHORT));
        name = "occupation";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "phone1";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "phone2";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "postal_code";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "state_province";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "total_children";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_SHORT));
        name = "yearly_income";
        presentationElements2.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_customer." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        PresentationGroupElement presentationGroupElement2 = new PresentationGroupElement().
                setName("public_customer").
                setResourcePath("JoinTree_1").
                setDescription("public_customer").
                setDescriptionId("").
                setLabel("public_customer").
                setLabelId("").
                setElements(presentationElements2);

        List<PresentationElement> presentationElements3 = new ArrayList<PresentationElement>();
        name = "brand_name";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "cases_per_pallet";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_SHORT));
        name = "gross_weight";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_DOUBLE));
        name = "low_fat";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_BOOLEAN));
        name = "net_weight";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_DOUBLE));
        name = "product_class_id";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_INTEGER));
        name = "product_id1";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product.product_id").
                setDescriptionId("").
                setDescription(name).
                setLabel("product_id").
                setLabelId("").
                setType(JAVA_LANG_INTEGER));
        name = "product_name";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_STRING));
        name = "recyclable_package";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_BOOLEAN));
        name = "shelf_depth";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_DOUBLE));
        name = "shelf_height";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_DOUBLE));
        name = "shelf_width";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_DOUBLE));
        name = "sku";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_LONG));
        name = "srp";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_MATH_BIG_DECIMAL));
        name = "units_per_case";
        presentationElements3.add(new PresentationSingleElement().
                setName(name).
                setResourcePath("JoinTree_1.public_product." + name).
                setDescriptionId("").
                setDescription(name).
                setLabel(name).
                setLabelId("").
                setType(JAVA_LANG_SHORT));
        PresentationGroupElement presentationGroupElement3 = new PresentationGroupElement().
                setName("public_product").
                setResourcePath("JoinTree_1").
                setDescription("public_product").
                setDescriptionId("").
                setLabel("public_product").
                setLabelId("").
                setElements(presentationElements3);

        List<PresentationElement> presentationElements = new ArrayList<PresentationElement>();
        presentationElements.add(presentationGroupElement1);
        presentationElements.add(presentationGroupElement2);
        presentationElements.add(presentationGroupElement3);
        appLogger.info("Adding  presentations  to domain finished successfully");
        return presentationElements;
    }


    private static ResourceGroupElement fetchTable(String schema, String tableNAme, String daraSource) {
        String tableName0 = "public_agg_ll_01_sales_fact_1997";
        String tableName1 = "public_customer";
        String tableName2 = "public_product";

        ResourceGroupElement resourceGroupElement = new ResourceGroupElement();

        if (tableNAme.equals(tableName0)) {
            resourceGroupElement.
                    setName(tableName0).
                    setSourceName("agg_ll_01_sales_fact_1997").
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
            appLogger.info("Table " + tableName0 + " fetched successfully");
            return resourceGroupElement;
        }

        if (tableNAme.equals(tableName1)) {
            resourceGroupElement.
                    setName(tableName1).
                    setSourceName("customer").
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
            appLogger.info("Table " + tableName0 + " fetched successfully");
            return resourceGroupElement;
        }
        if (tableNAme.equals(tableName2)) {
            resourceGroupElement.
                    setName(tableName2).
                    setSourceName("product").
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
            appLogger.info("Table " + tableName0 + " fetched successfully");
            return resourceGroupElement;
        }
        return null;
    }


}
