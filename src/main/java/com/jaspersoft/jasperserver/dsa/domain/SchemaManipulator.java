package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dto.resources.domain.ClientDomain;
import com.jaspersoft.jasperserver.dto.resources.domain.ConstantsResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.Join;
import com.jaspersoft.jasperserver.dto.resources.domain.JoinInfo;
import com.jaspersoft.jasperserver.dto.resources.domain.JoinResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import com.jaspersoft.jasperserver.dto.resources.domain.QueryResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ReferenceElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceSingleElement;
import com.jaspersoft.jasperserver.dto.resources.domain.SchemaElement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.jaspersoft.jasperserver.dsa.domain.InitDomainHelper.fetchTable;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.DATA_SOURCE;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.DATA_SOURCE_SCHEMA;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.FULL_TABLE_NAME_0_AGG_11_01;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.FULL_TABLE_NAME_1_CUSTOMER;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.FULL_TABLE_NAME_2_PRODUCT;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_LANG_INTEGER;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_LANG_STRING;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_MATH_BIG_DECIMAL;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JOIN_TREE_1;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.TABLE_NAME_3_AGG_C_14;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.TABLE_NAME_4_CUSTOMER_SALES;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.getHierarchicalName;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.getPresentationSingleElementName;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.resourceToPresentationGroupElement;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.resourceToPresentationSingleElement;


/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class SchemaManipulator {

    public ClientDomain addCalculatedFields(ClientDomain domain) {

        ResourceGroupElement datasource = (ResourceGroupElement) domain.getSchema().getResources().get(0);
        ResourceGroupElement datasourceSchema = (ResourceGroupElement) datasource.getElements().get(0);
        List<SchemaElement> resources = datasourceSchema.getElements();

        // add calculated fields to particular tables in resources
        String calcFieldNumName = "CalcField_num";
        String calcFieldStringName = "CalcField_string";
        ResourceSingleElement resourceSingleElement1 = new ResourceSingleElement();
        ResourceSingleElement resourceSingleElement2 = new ResourceSingleElement();
        for (SchemaElement resource : resources) {
            if (resource.getName().equals(FULL_TABLE_NAME_0_AGG_11_01)) {
                ((ResourceGroupElement) resource).getElements().add(resourceSingleElement1.
                        setName(calcFieldNumName).
                        setType(JAVA_MATH_BIG_DECIMAL).
                        setExpression("store_cost*10"));
            }
            if (resource.getName().equals(FULL_TABLE_NAME_1_CUSTOMER)) {
                ((ResourceGroupElement) resource).getElements().add(resourceSingleElement2.
                        setName(calcFieldStringName).
                        setType(JAVA_LANG_STRING).
                        setExpression("concat(fname,' ',lname)"));
            }

        }
        // add calculated fields to presentation
        List<PresentationElement> presentations = domain.getSchema().getPresentation().get(0).getElements();
        presentations.add(resourceToPresentationSingleElement(resourceSingleElement1, JOIN_TREE_1, FULL_TABLE_NAME_0_AGG_11_01));
        presentations.add(resourceToPresentationSingleElement(resourceSingleElement2, JOIN_TREE_1, FULL_TABLE_NAME_1_CUSTOMER));

        return domain;
    }

    public ClientDomain addCrossTableCalculatedFields(ClientDomain domain) {

        JoinResourceGroupElement joins = (JoinResourceGroupElement) domain.getSchema().getResources().get(1);
        List<SchemaElement> joinsElements = joins.getElements();

        // add calculated fields to particular tables in joinsElements
        String calcFieldName = "CrossTableCalcField";
        joinsElements.add(new ResourceSingleElement().
                setName(calcFieldName).
                setType(JAVA_LANG_STRING).
                setExpression("concat(public_customer.fullname,', ',public_product.brand_name)"));
        // add calculated fields to presentation
        List<PresentationElement> presentations = domain.getSchema().getPresentation().get(0).getElements();
        presentations.add(new PresentationSingleElement().
                setType(JAVA_LANG_STRING)
                .setLabel(calcFieldName)
                .setLabelId("")
                .setDescription(calcFieldName)
                .setDescriptionId("")
                .setHierarchicalName(calcFieldName)
                .setResourcePath(JOIN_TREE_1 + "." + calcFieldName)
                .setName(calcFieldName));

        return domain;
    }

    public ClientDomain addConstantCalculatedField(ClientDomain domain, Integer value) {

        List<ResourceElement> resources = domain.getSchema().getResources();
        // create constant elements group
        ConstantsResourceGroupElement constantsResourceGroupElement = new ConstantsResourceGroupElement().
                setName("constant_fields_level");

        List<ResourceSingleElement> constantElements = new LinkedList<ResourceSingleElement>();
        String constantField = "ConstantField";
        constantElements.add(new ResourceSingleElement().
                setName(constantField).
                setType(JAVA_LANG_INTEGER).
                setExpression(value.toString()));

        constantsResourceGroupElement.setElements(constantElements);
        resources.add(1, constantsResourceGroupElement);

        // add constant group to presentation
        List<PresentationGroupElement> presentations = domain.getSchema().getPresentation();

        List<PresentationElement> presentationSingleElements = new LinkedList<PresentationElement>();
        presentationSingleElements.add(new PresentationSingleElement().
                setType(JAVA_LANG_INTEGER)
                .setLabel(constantField)
                .setLabelId("")
                .setDescription(constantField)
                .setDescriptionId("")
                .setHierarchicalName("constant_fields_level.ConstantField")
                .setResourcePath("constant_fields_level.ConstantField")
                .setName(constantField));

        presentations.add(new PresentationGroupElement().
                setName("constant_fields_level").
                setElements(presentationSingleElements));

        return domain;
    }

    public ClientDomain addFilters(ClientDomain clientDomain) {

        ResourceGroupElement datasource = (ResourceGroupElement) clientDomain.getSchema().getResources().get(0);
        ResourceGroupElement datasourceSchema = (ResourceGroupElement) datasource.getElements().get(0);
        List<SchemaElement> resources = datasourceSchema.getElements();

        // add filters to particular tables in resources
        for (SchemaElement resource : resources) {
            //uncomment this when calculated fields will be fixed
//            if (resource.getName().equals("public_agg_ll_01_sales_fact_1997")) {
//                ((ResourceGroupElement) resource).setFilterExpression("CalcField_num > 10");
//            }
            if (resource.getName().equals(FULL_TABLE_NAME_1_CUSTOMER)) {
                ((ResourceGroupElement) resource).setFilterExpression("country == 'USA'");
            }
            if (resource.getName().equals(FULL_TABLE_NAME_2_PRODUCT)) {
                ((ResourceGroupElement) resource).setFilterExpression("low_fat == true and net_weight < 10.0");
            }
        }

        return clientDomain;
    }

    public ClientDomain addCrossTableFilter(ClientDomain domain) {
        JoinResourceGroupElement joinGroup = (JoinResourceGroupElement) domain.getSchema().getResources().get(1);
        // add cross table filter to join group
        joinGroup.setFilterExpression("not contains(public_product.brand_name, public_customer.fname)");

        return domain;
    }

    public ClientDomain addTwoFieldsFilter(ClientDomain domain) {

        ResourceGroupElement datasource = (ResourceGroupElement) domain.getSchema().getResources().get(0);
        ResourceGroupElement datasourceSchema = (ResourceGroupElement) datasource.getElements().get(0);
        List<SchemaElement> resources = datasourceSchema.getElements();

        // add filters to particular tables in resources
        for (SchemaElement resource : resources) {

            if (resource.getName().equals(FULL_TABLE_NAME_1_CUSTOMER)) {
                ((ResourceGroupElement) resource).setFilterExpression("contains(fullname, fname)");
            }
        }

        return domain;
    }

    public ClientDomain addDerivedTable(ClientDomain clientDomain) {

        // add  new query resource element to resources
        ResourceGroupElement datasource = (ResourceGroupElement) clientDomain.getSchema().getResources().get(0);
        List<SchemaElement> singleElements = new LinkedList<SchemaElement>();

        singleElements.add(new ResourceSingleElement().setName("customer_id").setType(JAVA_LANG_INTEGER));
        singleElements.add(new ResourceSingleElement().setName("fname").setType(JAVA_LANG_STRING));
        singleElements.add(new ResourceSingleElement().setName("fullname").setType(JAVA_LANG_STRING));
        singleElements.add(new ResourceSingleElement().setName("lname").setType(JAVA_LANG_STRING));
        String queryElementName = "TestQueryCustomer";
        datasource.getElements().add(new QueryResourceGroupElement()
                .setName(queryElementName)
                .setElements(singleElements)
                .setQuery("select * from public.customer"));

        // join new query resource with existing resources in data island
        JoinResourceGroupElement joinGroup = (JoinResourceGroupElement) clientDomain.getSchema().getResources().get(1);
        List<SchemaElement> referenceElements = joinGroup.getElements();
        referenceElements.add(new ReferenceElement().setName(queryElementName).setReferencePath("FoodmartDataSourceJNDI.TestQueryCustomer"));
        joinGroup.getJoinInfo().getJoins().add(new Join().
                setLeft(queryElementName).
                setRight("public_customer").
                setExpression("public_customer.customer_id == TestQueryCustomer.customer_id").
                setWeight(1).
                setType(Join.JoinType.inner));

        // ann new query resource to presentation
        List<PresentationElement> presentations = clientDomain.getSchema().getPresentation().get(0).getElements();
        List<PresentationElement> presentationSingleElements = new LinkedList<PresentationElement>();
        presentationSingleElements.add(new PresentationSingleElement().
                setName("customer_id2").
                setLabel("customer_id").
                setLabelId("").
                setDescription("customer_id2").
                setDescriptionId("").
                setHierarchicalName("TestQueryCustomer.customer_id").
                setResourcePath("JoinTree_1.TestQueryCustomer.customer_id").
                setType(JAVA_LANG_INTEGER));
        presentationSingleElements.add(new PresentationSingleElement().
                setName("fname1").
                setLabel("fname").
                setLabelId("").
                setDescription("fname1").
                setDescriptionId("").
                setHierarchicalName("TestQueryCustomer.fname").
                setResourcePath("JoinTree_1.TestQueryCustomer.fname").
                setType(JAVA_LANG_STRING));
        presentationSingleElements.add(new PresentationSingleElement().
                setName("fullname1").
                setLabel("fullname").
                setLabelId("").
                setDescription("fullname1").
                setDescriptionId("").
                setHierarchicalName("TestQueryCustomer.fullname").
                setResourcePath("JoinTree_1.TestQueryCustomer.fullname").
                setType(JAVA_LANG_STRING));
        presentationSingleElements.add(new PresentationSingleElement().
                setName("lname1").
                setLabel("lname").
                setLabelId("").
                setDescription("lname1").
                setDescriptionId("").
                setHierarchicalName("TestQueryCustomer.lname").
                setResourcePath("JoinTree_1.TestQueryCustomer.lname").
                setType(JAVA_LANG_STRING));
        presentations.add(new PresentationGroupElement().
                        setName(queryElementName)
                        .setLabel(queryElementName)
                        .setLabelId("")
                        .setDescription(queryElementName)
                        .setDescriptionId("")
                        .setElements(presentationSingleElements)
        );
        return clientDomain;
    }

    public ClientDomain createTableCopy(ClientDomain domain, String tableName) {

        // Create copy of table in resources
        ResourceGroupElement datasource = (ResourceGroupElement) domain.getSchema().getResources().get(0);
        ResourceGroupElement datasourceSchema = (ResourceGroupElement) datasource.getElements().get(0);
        List<SchemaElement> resources = datasourceSchema.getElements();
        ResourceGroupElement copyElement = null;
        for (SchemaElement resource : resources) {
            if (resource.getName().equals(tableName)) {
                copyElement = new ResourceGroupElement((ResourceGroupElement) resource);
                copyElement.setName(copyElement.getName() + "1");
            }
        }
        copyElement.setName(tableName + 1);
        resources.add(copyElement);

        // Join copied table with existing table in data island
        JoinResourceGroupElement joinGroup = (JoinResourceGroupElement) domain.getSchema().getResources().get(1);
        List<SchemaElement> referenceElements = joinGroup.getElements();
        referenceElements.add(new ReferenceElement().setName(copyElement.getName()).setReferencePath("FoodmartDataSourceJNDI.schema1.public_customer1"));
        joinGroup.getJoinInfo().getJoins().add(new Join().
                setLeft("public_agg_ll_01_sales_fact_1997").
                setRight(copyElement.getName()).
                setExpression("public_agg_ll_01_sales_fact_1997.customer_id == public_customer1.customer_id").
                setWeight(1).
                setType(Join.JoinType.inner));

        // Add copied table to presentation
        List<PresentationElement> presentations = domain.getSchema().getPresentation().get(0).getElements();

        List<PresentationElement> presentationElements = new ArrayList<PresentationElement>();
        String elementName;
        String elementLabel;
        for (SchemaElement resourceElement : copyElement.getElements()) {
            ResourceSingleElement castedElement = (ResourceSingleElement) resourceElement;
            elementLabel = castedElement.getName();
            elementName = elementLabel + 2;
            presentationElements.add(new PresentationSingleElement().
                    setName(elementName).
                    setLabel(elementLabel).
                    setLabelId("").
                    setDescription(elementName).
                    setDescriptionId("").
                    setHierarchicalName(copyElement.getName() + elementLabel).
                    setResourcePath("JoinTree_1." + copyElement.getName() + "." + elementLabel).
                    setType(castedElement.getType()));
        }
        PresentationGroupElement presentationGroupElement = new PresentationGroupElement().
                setName(copyElement.getName()).
                setDescription(copyElement.getName()).
                setDescriptionId("").
                setLabel(copyElement.getName()).
                setLabelId("").
                setElements(presentationElements);

        presentations.add(3, presentationGroupElement);
        return domain;
    }

    public ClientDomain addDataIslands(ClientDomain domain) {

        // add resources for new data islands
        ResourceGroupElement datasource = (ResourceGroupElement) domain.getSchema().getResources().get(0);
        ResourceGroupElement datasourceSchema = (ResourceGroupElement) datasource.getElements().get(0);
        List<SchemaElement> resources = datasourceSchema.getElements();
        ResourceGroupElement table0_agg_c_14 = fetchTable(DATA_SOURCE_SCHEMA, TABLE_NAME_3_AGG_C_14, DATA_SOURCE);
        ResourceGroupElement table1_customer1 = new ResourceGroupElement(InitDomainHelper.table1_customer).setName(InitDomainHelper.table1_customer.getName() + 1);
        ResourceGroupElement table2_customer11 = new ResourceGroupElement(table1_customer1).setName(table1_customer1.getName() + 1);
        ResourceGroupElement table3_customer_sales = fetchTable(DATA_SOURCE_SCHEMA, TABLE_NAME_4_CUSTOMER_SALES, DATA_SOURCE);
        ResourceGroupElement table4_customer_sales1 = new ResourceGroupElement(table3_customer_sales).setName(table3_customer_sales.getName() + 1);
        resources.add(table0_agg_c_14);
        resources.add(table1_customer1);
        resources.add(table2_customer11);
        resources.add(table3_customer_sales);
        resources.add(table4_customer_sales1);

        // join resources in data islands
        List<Join> joinsList0 = new LinkedList<Join>();
        joinsList0.add(new Join().
                setLeft("public_customer1").
                setRight("public_agg_c_14_sales_fact_1997").
                setExpression("public_agg_c_14_sales_fact_1997.customer_id == public_customer1.customer_id").
                setWeight(1).
                setType(Join.JoinType.rightOuter));
        joinsList0.add(new Join().
                setLeft("public_customer1").
                setRight("public_customer_sales").
                setExpression("public_customer_sales.customer_id == public_customer1.customer_id").
                setWeight(1).
                setType(Join.JoinType.inner));
        List<SchemaElement> referenceElements0 = new LinkedList<SchemaElement>();
        referenceElements0.add(new ReferenceElement().
                setName("public_agg_c_14_sales_fact_1997").
                setReferencePath("FoodmartDataSourceJNDI.schema1.public_agg_c_14_sales_fact_1997"));
        referenceElements0.add(new ReferenceElement().
                setName("public_customer1").
                setReferencePath("FoodmartDataSourceJNDI.schema1.public_customer1"));
        referenceElements0.add(new ReferenceElement().
                setName("public_customer_sales").
                setReferencePath("FoodmartDataSourceJNDI.schema1.public_customer_sales"));
        String joinTree_2 = "JoinTree_2";
        JoinResourceGroupElement joinTree2 = new JoinResourceGroupElement().
                setName(joinTree_2).
                setJoinInfo(new JoinInfo().setIncludeAllDataIslandJoins(false).setSuppressCircularJoins(false).setJoins(joinsList0)).
                setElements(referenceElements0);
        List<Join> joinsList1 = new LinkedList<Join>();
        joinsList1.add(new Join().
                setLeft("public_customer_sales1").
                setRight("public_customer11").
                setExpression("public_customer11.customer_id == public_customer_sales1.customer_id").
                setWeight(1).
                setType(Join.JoinType.fullOuter));
        List<SchemaElement> referenceElements1 = new LinkedList<SchemaElement>();
        referenceElements1.add(new ReferenceElement().
                setName("public_customer_sales1").
                setReferencePath("FoodmartDataSourceJNDI.schema1.public_customer_sales1"));
        referenceElements1.add(new ReferenceElement().
                setName("public_customer11").
                setReferencePath("FoodmartDataSourceJNDI.schema1.public_customer11"));
        String joinTree_3 = "JoinTree_3";
        JoinResourceGroupElement joinTree3 = new JoinResourceGroupElement().
                setName(joinTree_3).
                setJoinInfo(new JoinInfo().setIncludeAllDataIslandJoins(false).setSuppressCircularJoins(false).setJoins(joinsList1)).
                setElements(referenceElements1);

        domain.getSchema().getResources().add(joinTree2);
        domain.getSchema().getResources().add(joinTree3);

        PresentationGroupElement joinTree2PresentationGroupElement = new PresentationGroupElement().setName(joinTree_2);
        List<PresentationElement> joinTree2PresentationGroupElements = new LinkedList<PresentationElement>();

        PresentationGroupElement presentationTable0AggC14 = resourceToPresentationGroupElement(table0_agg_c_14, joinTree_2);
        addPostfix(presentationTable0AggC14, 4);
        joinTree2PresentationGroupElements.add(presentationTable0AggC14);

        PresentationGroupElement presentationTable1Customer1 = resourceToPresentationGroupElement(table1_customer1, joinTree_2);
        addPostfix(presentationTable1Customer1, 5);
        joinTree2PresentationGroupElements.add(presentationTable1Customer1);

        PresentationGroupElement presentationTable3CustomerSales = resourceToPresentationGroupElement(table3_customer_sales, joinTree_2);
        addPostfix(presentationTable3CustomerSales, 6);
        joinTree2PresentationGroupElements.add(presentationTable3CustomerSales);

        joinTree2PresentationGroupElement.setElements(joinTree2PresentationGroupElements);

        PresentationGroupElement joinTree3PresentationGroupElement = new PresentationGroupElement().setName(joinTree_3);
        List<PresentationElement> joinTree3PresentationGroupElements = new LinkedList<PresentationElement>();

        PresentationGroupElement presentationTable2Customer1 = resourceToPresentationGroupElement(table2_customer11, joinTree_3);
        addPostfix(presentationTable2Customer1, 8);
        joinTree3PresentationGroupElements.add(presentationTable2Customer1);

        PresentationGroupElement presentationTable4CustomerSales = resourceToPresentationGroupElement(table4_customer_sales1, joinTree_3);
        addPostfix(presentationTable4CustomerSales, 7);
        joinTree3PresentationGroupElements.add(presentationTable4CustomerSales);

        joinTree3PresentationGroupElement.setElements(joinTree3PresentationGroupElements);

        domain.getSchema().getPresentation().add(joinTree2PresentationGroupElement);
        domain.getSchema().getPresentation().add(joinTree3PresentationGroupElement);

        return domain;
    }

    private void addPostfix(PresentationGroupElement groupElement, int postfix) {
        for (PresentationElement presentationElement : groupElement.getElements()) {
            presentationElement.setName(presentationElement.getName() + postfix);
            presentationElement.setDescription(presentationElement.getDescription() + postfix);
            ((PresentationSingleElement)presentationElement).setHierarchicalName(((PresentationSingleElement) presentationElement).getHierarchicalName ()+ postfix);
        }
    }

    public ClientDomain addFieldsWithRestructuring(ClientDomain domain) {
        List<PresentationElement> presentationElements = domain.getSchema().getPresentation().get(0).getElements();
        PresentationSingleElement presentationSingleElement0 = null;
        PresentationSingleElement presentationSingleElement1 = null;
        PresentationSingleElement presentationSingleElement2 = null;
        PresentationSingleElement presentationSingleElement3 = null;
        PresentationSingleElement presentationSingleElement4 = null;
        PresentationSingleElement presentationSingleElement5 = null;

        for (PresentationElement presentationElement : presentationElements) {
            if (presentationElement.getName().equals(FULL_TABLE_NAME_1_CUSTOMER)) {
                presentationSingleElement0 = getPresentationSingleElementName((PresentationGroupElement) presentationElement, "customer_id");
                ((PresentationGroupElement) presentationElement).getElements().remove(presentationSingleElement0);
            }
            if (presentationElement.getName().equals(FULL_TABLE_NAME_2_PRODUCT)) {
                presentationSingleElement1 = getPresentationSingleElementName((PresentationGroupElement) presentationElement, "product_id");
                ((PresentationGroupElement) presentationElement).getElements().remove(presentationSingleElement1);
                presentationSingleElement2 = getPresentationSingleElementName((PresentationGroupElement) presentationElement, "net_weight");
                ((PresentationGroupElement) presentationElement).getElements().remove(presentationSingleElement2);
                presentationSingleElement3 = getPresentationSingleElementName((PresentationGroupElement) presentationElement, "low_fat");
                ((PresentationGroupElement) presentationElement).getElements().remove(presentationSingleElement3);
                presentationSingleElement4 = getPresentationSingleElementName((PresentationGroupElement) presentationElement, "product_name");
                ((PresentationGroupElement) presentationElement).getElements().remove(presentationSingleElement4);
            }
            if (presentationElement.getName().equals(FULL_TABLE_NAME_0_AGG_11_01)) {
                presentationSingleElement5 = getPresentationSingleElementName((PresentationGroupElement) presentationElement, "product_id1");
                ((PresentationGroupElement) presentationElement).getElements().remove(presentationSingleElement5);
            }
        }
        PresentationGroupElement presentationGroupElement = new PresentationGroupElement();
        String demoSetName = "DemoSet";
        List<PresentationElement> presentationSingleElements = new LinkedList<PresentationElement>();
        presentationSingleElements.add(presentationSingleElement0.
                setHierarchicalName(getHierarchicalName(demoSetName, "customer_id")));
        presentationSingleElements.add(presentationSingleElement1.
                setHierarchicalName(getHierarchicalName(demoSetName, "product_id")));
        presentationSingleElements.add(presentationSingleElement4.
                setHierarchicalName(getHierarchicalName(demoSetName, "product_name")));

        presentationGroupElement.
                setName(demoSetName).
                setLabel(demoSetName).
                setDescription(demoSetName).
                setElements(presentationSingleElements);

        presentationElements.add(presentationGroupElement);
        presentationElements.add(presentationSingleElement3.
                setHierarchicalName("low_fat"));
        presentationElements.add(presentationSingleElement5.
                setHierarchicalName("product_id1"));
        presentationElements.add(presentationSingleElement2.
                setHierarchicalName("net_weight"));


        return domain;
    }
}
