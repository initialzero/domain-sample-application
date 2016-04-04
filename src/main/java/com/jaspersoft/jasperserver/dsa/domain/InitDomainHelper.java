package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dto.resources.ClientReference;
import com.jaspersoft.jasperserver.dto.resources.domain.ClientDomain;
import com.jaspersoft.jasperserver.dto.resources.domain.Join;
import com.jaspersoft.jasperserver.dto.resources.domain.JoinInfo;
import com.jaspersoft.jasperserver.dto.resources.domain.JoinResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ReferenceElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceSingleElement;
import com.jaspersoft.jasperserver.dto.resources.domain.Schema;
import com.jaspersoft.jasperserver.dto.resources.domain.SchemaElement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.DATA_SOURCE;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.DATA_SOURCE_SCHEMA;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.DATA_SOURCE_SCHEMA_ALIAS;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.FULL_TABLE_NAME_0_AGG_11_01;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.FULL_TABLE_NAME_1_CUSTOMER;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.FULL_TABLE_NAME_2_PRODUCT;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.FULL_TABLE_NAME_3_AGG_C_14;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.FULL_TABLE_NAME_4_CUSTOMER_SALES;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_LANG_BOOLEAN;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_LANG_DOUBLE;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_LANG_INTEGER;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_LANG_LONG;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_LANG_SHORT;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_LANG_STRING;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_MATH_BIG_DECIMAL;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_UTIL_DATE;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JOIN_TREE_1;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.TABLE_NAME_0_AGG_11_01;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.TABLE_NAME_1_CUSTOMER;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.TABLE_NAME_2_PRODUCT;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.TABLE_NAME_3_AGG_C_14;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.TABLE_NAME_4_CUSTOMER_SALES;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.getHierarchicalName;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.getPresentationSingleElementName;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class InitDomainHelper {

    private static Schema schema;
    public static ResourceGroupElement table0_agg_11_01;
    public static ResourceGroupElement table1_customer;
    public static ResourceGroupElement table2_product;


    public static ClientDomain buildDomain(String baseFolder, String domainName, String dataSourceUri) {
        ClientDomain clientDomain = new ClientDomain().
                setUri(baseFolder + "/" + domainName).
                setLabel(domainName).
                setDataSource(new ClientReference().setUri(dataSourceUri)).
                setSchema(buildSchema());

        return clientDomain;
    }

    private static Schema buildSchema() {
        schema = new Schema();
        // add resources to domain schema
        schema.setResources(initResources());
        // add presentations to domain schema
        schema.setPresentation(initPresentations());
        return schema;
    }

    private static List<ResourceElement> initResources() {
        // init tables
        List<SchemaElement> tables = new ArrayList<SchemaElement>();
        table0_agg_11_01 = fetchTable(DATA_SOURCE_SCHEMA, TABLE_NAME_0_AGG_11_01, DATA_SOURCE);
        table1_customer = fetchTable(DATA_SOURCE_SCHEMA, TABLE_NAME_1_CUSTOMER, DATA_SOURCE);
        table2_product = fetchTable(DATA_SOURCE_SCHEMA, TABLE_NAME_2_PRODUCT, DATA_SOURCE);

        tables.add(table0_agg_11_01);
        tables.add(table1_customer);
        tables.add(table2_product);
        // init datasource schema and add tables to schema
        ResourceGroupElement publicSchema = new ResourceGroupElement().
                setName(DATA_SOURCE_SCHEMA_ALIAS).
                setSourceName(DATA_SOURCE_SCHEMA).
                setElements(tables);
        List<SchemaElement> schemas = new ArrayList<SchemaElement>();
        schemas.add(publicSchema);

        //init data source and add schema to datasource
        ResourceGroupElement dataSource = new ResourceGroupElement().
                setName(DATA_SOURCE).
                setElements(schemas);
        List<ResourceElement> resources = new ArrayList<ResourceElement>();

        // add datasource to domain schema
        resources.add(dataSource);

        List<Join> joins = new ArrayList<Join>();

        // init joins of tables
        joins.add(new Join().
                setLeft(FULL_TABLE_NAME_0_AGG_11_01).
                setRight(FULL_TABLE_NAME_1_CUSTOMER).
                setExpression("public_agg_ll_01_sales_fact_1997.customer_id == public_customer.customer_id").
                setWeight(1)
                .setType(Join.JoinType.inner));
        joins.add(new Join().
                setLeft(FULL_TABLE_NAME_0_AGG_11_01).
                setRight(FULL_TABLE_NAME_2_PRODUCT).
                setExpression("public_agg_ll_01_sales_fact_1997.product_id == public_product.product_id").
                setWeight(1).setType(Join.JoinType.inner));

        // add joins to JoinGroup section of domain schema
        JoinInfo joinInfo = new JoinInfo().
                setIncludeAllDataIslandJoins(false).
                setSuppressCircularJoins(false).
                setJoins(joins);

        // add references to JoinGroup section of domain schema
        List<SchemaElement> joinResourceElements = new ArrayList<SchemaElement>();
        joinResourceElements.add(new ReferenceElement().
                setName(FULL_TABLE_NAME_2_PRODUCT).
                setReferencePath(SchemaUtil.getPath(DATA_SOURCE, DATA_SOURCE_SCHEMA_ALIAS, FULL_TABLE_NAME_2_PRODUCT)));
        joinResourceElements.add(new ReferenceElement().
                setName(FULL_TABLE_NAME_1_CUSTOMER).
                setReferencePath(SchemaUtil.getPath(DATA_SOURCE, DATA_SOURCE_SCHEMA_ALIAS, FULL_TABLE_NAME_1_CUSTOMER)));
        joinResourceElements.add(new ReferenceElement().
                setName(FULL_TABLE_NAME_0_AGG_11_01).
                setReferencePath(SchemaUtil.getPath(DATA_SOURCE, DATA_SOURCE_SCHEMA_ALIAS,FULL_TABLE_NAME_0_AGG_11_01)));

        JoinResourceGroupElement joinResourceGroupElement = new JoinResourceGroupElement().
                setName(JOIN_TREE_1).
                setJoinInfo(joinInfo).
                setElements(joinResourceElements);

        // add JoinGroup to domain schema
        resources.add(joinResourceGroupElement);
        return resources;

    }

    private static List<PresentationGroupElement> initPresentations() {
        // fetch presentation elements from resources
        PresentationGroupElement presentationGroupElement0 = SchemaUtil.resourceToPresentationGroupElement(table0_agg_11_01, JOIN_TREE_1);
        getPresentationSingleElementName(presentationGroupElement0, "customer_id").setName("customer_id1").setDescription("customer_id1").setHierarchicalName(getHierarchicalName(FULL_TABLE_NAME_0_AGG_11_01, "customer_id1"));
        getPresentationSingleElementName(presentationGroupElement0, "product_id").setName("product_id1").setDescription("product_id1").setHierarchicalName(getHierarchicalName(FULL_TABLE_NAME_0_AGG_11_01, "product_id1"));
        PresentationGroupElement presentationGroupElement1 = SchemaUtil.resourceToPresentationGroupElement(table1_customer, JOIN_TREE_1);
        PresentationGroupElement presentationGroupElement2 = SchemaUtil.resourceToPresentationGroupElement(table2_product, JOIN_TREE_1);
        List<PresentationElement> presentationElements = new ArrayList<PresentationElement>();

        // add presentation elements to presentation section of domain schema
        presentationElements.add(presentationGroupElement0);
        presentationElements.add(presentationGroupElement1);
        presentationElements.add(presentationGroupElement2);
        PresentationGroupElement presentationDataIsland = new PresentationGroupElement().
                setName(JOIN_TREE_1).setElements(presentationElements);
        List<PresentationGroupElement> presentations = new LinkedList<PresentationGroupElement>();
        presentations.add(presentationDataIsland);
        return presentations;
    }

    public static ResourceGroupElement fetchTable(String dataSourceSchema, String tableName, String daraSource) {
        String fullTableName = dataSourceSchema + "_" + tableName;
        ResourceGroupElement resourceGroupElement = new ResourceGroupElement();

        if (fullTableName.equals(FULL_TABLE_NAME_0_AGG_11_01)) {
            resourceGroupElement.
                    setName(FULL_TABLE_NAME_0_AGG_11_01).
                    setSourceName(TABLE_NAME_0_AGG_11_01).
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
            return resourceGroupElement;
        }
        if (fullTableName.equals(FULL_TABLE_NAME_3_AGG_C_14)) {
            resourceGroupElement.
                    setName(FULL_TABLE_NAME_3_AGG_C_14).
                    setSourceName(TABLE_NAME_3_AGG_C_14).
                    setElements(new ArrayList<SchemaElement>());
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("customer_id").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("fact_count").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("month_of_year").setType(JAVA_LANG_SHORT));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("product_id").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("promotion_id").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("quarter").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("store_cost").setType(JAVA_MATH_BIG_DECIMAL));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("store_id").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("store_sales").setType(JAVA_MATH_BIG_DECIMAL));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("the_year").setType(JAVA_LANG_SHORT));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("unit_sales").setType(JAVA_MATH_BIG_DECIMAL));
            return resourceGroupElement;
        }

        if (fullTableName.equals(FULL_TABLE_NAME_1_CUSTOMER)) {
            resourceGroupElement.
                    setName(FULL_TABLE_NAME_1_CUSTOMER).
                    setSourceName(TABLE_NAME_1_CUSTOMER).
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
            return resourceGroupElement;
        }
        if (fullTableName.equals(FULL_TABLE_NAME_2_PRODUCT)) {
            resourceGroupElement.
                    setName(FULL_TABLE_NAME_2_PRODUCT).
                    setSourceName(TABLE_NAME_2_PRODUCT).
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
            return resourceGroupElement;
        }
        if (fullTableName.equals(FULL_TABLE_NAME_4_CUSTOMER_SALES)) {
            resourceGroupElement.
                    setName(FULL_TABLE_NAME_4_CUSTOMER_SALES).
                    setSourceName(TABLE_NAME_4_CUSTOMER_SALES).
                    setElements(new ArrayList<SchemaElement>());
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("customer_fullname").setType(JAVA_LANG_STRING));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("customer_id").setType(JAVA_LANG_INTEGER));
            resourceGroupElement.getElements().add(new ResourceSingleElement().
                    setName("store_sales").setType(JAVA_MATH_BIG_DECIMAL));
            return resourceGroupElement;
        }
        return null;
    }

}
