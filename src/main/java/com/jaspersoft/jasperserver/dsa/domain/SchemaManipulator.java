package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dto.adhoc.query.el.ClientExpressionContainer;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.ClientVariable;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.literal.ClientBoolean;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.literal.ClientDecimal;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.literal.ClientInteger;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.literal.ClientString;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.operator.ClientFunction;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.operator.arithmetic.ClientMultiply;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.operator.comparison.ClientEquals;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.operator.comparison.ClientLess;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.operator.logical.ClientAnd;
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
import com.jaspersoft.jasperserver.dto.resources.domain.Schema;
import com.jaspersoft.jasperserver.dto.resources.domain.SchemaElement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.jaspersoft.jasperserver.dsa.domain.InitDomainHelper.fetchTable;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.DATA_SOURCE;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.DATA_SOURCE_SCHEMA;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.DATA_SOURCE_SCHEMA_ALIAS;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.FULL_TABLE_NAME_0_AGG_11_01;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.FULL_TABLE_NAME_1_CUSTOMER;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.FULL_TABLE_NAME_2_PRODUCT;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_LANG_INTEGER;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_LANG_STRING;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JAVA_MATH_BIG_DECIMAL;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JOIN_TREE_1;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JOIN_TREE_2;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.JOIN_TREE_3;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.TABLE_NAME_3_AGG_C_14;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.TABLE_NAME_4_CUSTOMER_SALES;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.getHierarchicalName;
import static com.jaspersoft.jasperserver.dsa.domain.SchemaUtil.getPath;
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

    public static final String JOIN_TYPE_INNER = "inner";
    public static final String JOIN_TYPE_RIGHT_OUTER = "rightOuter";
    public static final String JOIN_TYPE_FULL_OUTER = "fullOuter";

    public void addCalculatedFields(Schema schema) {

        ResourceGroupElement datasource = (ResourceGroupElement) schema.getResources().get(0);
        ResourceGroupElement datasourceSchema = (ResourceGroupElement) datasource.getElements().get(0);
        List<SchemaElement> resources = datasourceSchema.getElements();

        // add calculated fields to particular tables in resources
        String calcFieldNumName = "CalcField_num";
        String calcFieldStringName = "CalcField_string";
        ResourceSingleElement resourceSingleElement1 = new ResourceSingleElement();
        ResourceSingleElement resourceSingleElement2 = new ResourceSingleElement();
        for (SchemaElement resource : resources) {
            if (resource.getName().equals(FULL_TABLE_NAME_0_AGG_11_01)) {
                // Create expression object that equal string expression "store_cost*10"
                ClientMultiply clientMultiply = new ClientMultiply().
                        addOperand(new ClientVariable("store_cost")).
                        addOperand(new ClientInteger(10));
                // and expression object as expression for new resource single element
                ((ResourceGroupElement) resource).getElements().add(resourceSingleElement1.
                        setName(calcFieldNumName).
                        setType(JAVA_MATH_BIG_DECIMAL).
                        setExpression(new ClientExpressionContainer().setObject(clientMultiply)));
            }
            // Create expression object that equal string expression "concat(fname,' ',lname)"
            ClientFunction function = new ClientFunction("concat").
                    addOperand(new ClientVariable("fname")).
                    addOperand(new ClientString(", ")).
                    addOperand(new ClientVariable("lname"));
            // and expression object as expression for new resource single element
            if (resource.getName().equals(FULL_TABLE_NAME_1_CUSTOMER)) {
                ((ResourceGroupElement) resource).getElements().add(resourceSingleElement2.
                        setName(calcFieldStringName).
                        setType(JAVA_LANG_STRING).
                        setExpression(new ClientExpressionContainer().setObject(function)));
            }
        }
        // add calculated fields to presentation
        List<PresentationElement> presentations = schema.getPresentation().get(0).getElements();
        presentations.add(resourceToPresentationSingleElement(resourceSingleElement1, JOIN_TREE_1, FULL_TABLE_NAME_0_AGG_11_01));
        presentations.add(resourceToPresentationSingleElement(resourceSingleElement2, JOIN_TREE_1, FULL_TABLE_NAME_1_CUSTOMER));

    }

    public void addCrossTableCalculatedFields(Schema schema) {

        JoinResourceGroupElement joins = (JoinResourceGroupElement) schema.getResources().get(1);
        List<SchemaElement> joinsElements = joins.getElements();

        // add calculated fields to particular tables in joinsElements

        String calcFieldName = "CrossTableCalcField";
        // Create expression object that equal string expression "concat(public_customer.fullname,', ',public_product.brand_name)"
        ClientFunction function = new ClientFunction("concat").
                addOperand(new ClientVariable("public_customer.fullname")).
                addOperand(new ClientString(", ")).
                addOperand(new ClientVariable("public_product.brand_name"));
        // and expression object as expression for new resource single element
        joinsElements.add(new ResourceSingleElement().
                setName(calcFieldName).
                setType(JAVA_LANG_STRING).
                setExpression(new ClientExpressionContainer().setObject(function)));

        // add calculated fields to presentation
        List<PresentationElement> presentations = schema.getPresentation().get(0).getElements();
        presentations.add(new PresentationSingleElement().
                setType(JAVA_LANG_STRING)
                .setLabel(calcFieldName)
                .setLabelId("")
                .setDescription(calcFieldName)
                .setDescriptionId("")
                .setHierarchicalName(calcFieldName)
                .setResourcePath(getHierarchicalName(JOIN_TREE_1, calcFieldName))
                .setName(calcFieldName));

    }

    public void addConstantCalculatedField(Schema schema, Integer value) {

        List<ResourceElement> resources = schema.getResources();
        String constantField = "ConstantField";
        String constant_fields_level = "constant_fields_level";
        // create constant elements group
        ConstantsResourceGroupElement constantsResourceGroupElement = new ConstantsResourceGroupElement().
                setName(constant_fields_level);

        List<ResourceSingleElement> constantElements = new LinkedList<ResourceSingleElement>();
        constantElements.add(new ResourceSingleElement().
                setName(constantField).
                setType(JAVA_LANG_INTEGER).
                setExpression(new ClientExpressionContainer().setString(value.toString())));

        constantsResourceGroupElement.setElements(constantElements);
        resources.add(1, constantsResourceGroupElement);

        // add constant group to presentation
        List<PresentationGroupElement> presentations = schema.getPresentation();

        List<PresentationElement> presentationSingleElements = new LinkedList<PresentationElement>();
        presentationSingleElements.add(new PresentationSingleElement().
                setType(JAVA_LANG_INTEGER)
                .setLabel(constantField)
                .setLabelId("")
                .setDescription(constantField)
                .setDescriptionId("")
                .setHierarchicalName(getHierarchicalName(constant_fields_level, constantField))
                .setResourcePath(getHierarchicalName(constant_fields_level, constantField))
                .setName(constantField));

        presentations.add(new PresentationGroupElement().
                setName(constant_fields_level).
                setElements(presentationSingleElements));

    }

    public void addFilters(Schema schema) {

        ResourceGroupElement datasource = (ResourceGroupElement) schema.getResources().get(0);
        ResourceGroupElement datasourceSchema = (ResourceGroupElement) datasource.getElements().get(0);
        List<SchemaElement> resources = datasourceSchema.getElements();

        // add filters to particular tables in resources
        for (SchemaElement resource : resources) {
            if (resource.getName().equals(FULL_TABLE_NAME_1_CUSTOMER)) {

                // Create expression object that equal string expression "country == 'USA'"
                ClientEquals clientEquals = new ClientEquals().
                        addOperand(new ClientVariable("country")).
                        addOperand(new ClientString("USA"));
                // and expression object as expression for new resource single element
                ((ResourceGroupElement) resource).setFilterExpression(new ClientExpressionContainer().
                        setObject(clientEquals));
            }
            if (resource.getName().equals(FULL_TABLE_NAME_2_PRODUCT)) {

                // Create expression object that equal string expression "low_fat == true and net_weight < 10.0"
                ClientEquals clientEquals = new ClientEquals().
                        addOperand(new ClientVariable("low_fat")).
                        addOperand(new ClientBoolean(Boolean.TRUE));
                ClientLess clientLess = new ClientLess().
                        addOperand(new ClientVariable("net_weight")).
                        addOperand(new ClientDecimal(10D));

                ClientAnd clientAnd = new ClientAnd().
                        addOperand(clientEquals).
                        addOperand(clientLess);

                // set expression object as expression
                ((ResourceGroupElement) resource).setFilterExpression(new ClientExpressionContainer().
                        setObject(clientAnd));
            }
        }
    }

    public void addCrossTableFilter(Schema schema) {
        JoinResourceGroupElement joinGroup = (JoinResourceGroupElement) schema.getResources().get(1);
        // add cross table filter to join group
        joinGroup.setFilterExpression(new ClientExpressionContainer().
                setString("not contains(public_product.brand_name, public_customer.fname)"));
    }

    public void addTwoFieldsFilter(Schema schema) {

        ResourceGroupElement datasource = (ResourceGroupElement) schema.getResources().get(0);
        ResourceGroupElement datasourceSchema = (ResourceGroupElement) datasource.getElements().get(0);
        List<SchemaElement> resources = datasourceSchema.getElements();

        // add filters to particular tables in resources
        for (SchemaElement resource : resources) {

            if (resource.getName().equals(FULL_TABLE_NAME_1_CUSTOMER)) {
                ((ResourceGroupElement) resource).setFilterExpression(new ClientExpressionContainer().
                        setString("contains(fullname, fname)"));
            }
        }
    }

    public void addDerivedTable(Schema schema) {

        // add  new query resource element to resources
        ResourceGroupElement datasource = (ResourceGroupElement) schema.getResources().get(0);
        List<SchemaElement> singleElements = new LinkedList<SchemaElement>();

        ResourceSingleElement resourceSingleElement0 = new ResourceSingleElement().setName("customer_id").setType(JAVA_LANG_INTEGER);
        ResourceSingleElement resourceSingleElement1 = new ResourceSingleElement().setName("fname").setType(JAVA_LANG_STRING);
        ResourceSingleElement resourceSingleElement2 = new ResourceSingleElement().setName("fullname").setType(JAVA_LANG_STRING);
        ResourceSingleElement resourceSingleElement3 = new ResourceSingleElement().setName("lname").setType(JAVA_LANG_STRING);
        singleElements.add(resourceSingleElement0);
        singleElements.add(resourceSingleElement1);
        singleElements.add(resourceSingleElement2);
        singleElements.add(resourceSingleElement3);
        String queryElementName = "TestQueryCustomer";
        datasource.getElements().add(new QueryResourceGroupElement()
                .setName(queryElementName)
                .setElements(singleElements)
                .setQuery("select * from public.customer"));

        // join new query resource with existing resources in data island
        JoinResourceGroupElement joinGroup = (JoinResourceGroupElement) schema.getResources().get(1);
        List<SchemaElement> referenceElements = joinGroup.getElements();
        referenceElements.add(new ReferenceElement().
                setName(queryElementName).
                setReferencePath(getHierarchicalName(DATA_SOURCE, queryElementName)));
        joinGroup.getJoinInfo().getJoins().add(new Join().
                setLeft(queryElementName).
                setRight(FULL_TABLE_NAME_1_CUSTOMER).
                setExpression(new ClientExpressionContainer().
                        setString("public_customer.customer_id == TestQueryCustomer.customer_id")).
                setWeight(1).
                setType(JOIN_TYPE_INNER));

        // ann new query resources to presentation
        List<PresentationElement> presentations = schema.getPresentation().get(0).getElements();
        List<PresentationElement> presentationSingleElements = new LinkedList<PresentationElement>();
        presentationSingleElements.add(resourceToPresentationSingleElement(resourceSingleElement0, JOIN_TREE_1, queryElementName));
        presentationSingleElements.add(resourceToPresentationSingleElement(resourceSingleElement1, JOIN_TREE_1, queryElementName));
        presentationSingleElements.add(resourceToPresentationSingleElement(resourceSingleElement2, JOIN_TREE_1, queryElementName));
        presentationSingleElements.add(resourceToPresentationSingleElement(resourceSingleElement3, JOIN_TREE_1, queryElementName));

        PresentationGroupElement presentationGroupElement = new PresentationGroupElement().
                setName(queryElementName)
                .setLabel(queryElementName)
                .setLabelId("")
                .setDescription(queryElementName)
                .setDescriptionId("")
                .setElements(presentationSingleElements);
        addPostfix(presentationGroupElement, 2);
        presentations.add(presentationGroupElement);
    }

    public void createTableCopy(Schema schema, String tableName) {

        // Create copy of table in resources
        ResourceGroupElement datasource = (ResourceGroupElement) schema.getResources().get(0);
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
        JoinResourceGroupElement joinGroup = (JoinResourceGroupElement) schema.getResources().get(1);
        List<SchemaElement> referenceElements = joinGroup.getElements();
        referenceElements.add(new ReferenceElement().
                setName(copyElement.getName()).
                setReferencePath(getPath(DATA_SOURCE, DATA_SOURCE_SCHEMA_ALIAS, copyElement.getName())));
        joinGroup.getJoinInfo().getJoins().add(new Join().
                setLeft(FULL_TABLE_NAME_0_AGG_11_01).
                setRight(copyElement.getName()).
                setExpression(new ClientExpressionContainer().
                        setString("public_agg_ll_01_sales_fact_1997.customer_id == public_customer1.customer_id")).
                setWeight(1).
                setType(JOIN_TYPE_INNER));

        // Add copied table to presentation
        List<PresentationElement> presentations = schema.getPresentation().get(0).getElements();

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
                    setHierarchicalName(getHierarchicalName(copyElement.getName(), elementLabel)).
                    setResourcePath(getPath(JOIN_TREE_1, copyElement.getName(), elementLabel)).
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
    }

    public void addDataIslands(Schema schema) {

        // fetch  and get copy of resources  and add them to resources to join in new data islands
        ResourceGroupElement datasource = (ResourceGroupElement) schema.getResources().get(0);
        ResourceGroupElement datasourceSchema = (ResourceGroupElement) datasource.getElements().get(0);
        List<SchemaElement> resources = datasourceSchema.getElements();

        ResourceGroupElement table0AggC14 = fetchTable(DATA_SOURCE_SCHEMA, TABLE_NAME_3_AGG_C_14, DATA_SOURCE);
        String table0AggC14Name = table0AggC14.getName();

        ResourceGroupElement table1Customer1 = new ResourceGroupElement(InitDomainHelper.table1_customer).setName(InitDomainHelper.table1_customer.getName() + 1);
        String table1Customer1Name = table1Customer1.getName();

        ResourceGroupElement table2Customer11 = new ResourceGroupElement(table1Customer1).setName(table1Customer1.getName() + 1);
        String table2Customer11Name = table2Customer11.getName();

        ResourceGroupElement table3CustomerSales = fetchTable(DATA_SOURCE_SCHEMA, TABLE_NAME_4_CUSTOMER_SALES, DATA_SOURCE);
        String table3CustomerSalesName = table3CustomerSales.getName();

        ResourceGroupElement table4CustomerSales1 = new ResourceGroupElement(table3CustomerSales).setName(table3CustomerSales.getName() + 1);
        String table4CustomerSales1Name = table4CustomerSales1.getName();

        resources.add(table0AggC14);
        resources.add(table1Customer1);
        resources.add(table2Customer11);
        resources.add(table3CustomerSales);
        resources.add(table4CustomerSales1);

        // join resources in data islands
        List<Join> joinsList0 = new LinkedList<Join>();
        joinsList0.add(new Join().
                setLeft(table1Customer1Name).
                setRight(table0AggC14Name).
                setExpression(new ClientExpressionContainer().
                        setString("public_agg_c_14_sales_fact_1997.customer_id == public_customer1.customer_id")).
                setWeight(1).
                setType(JOIN_TYPE_RIGHT_OUTER));
        joinsList0.add(new Join().
                setLeft(table1Customer1Name).
                setRight(table3CustomerSalesName).
                setExpression(new ClientExpressionContainer().
                        setString("public_customer_sales.customer_id == public_customer1.customer_id")).
                setWeight(1).
                setType(JOIN_TYPE_INNER));
        List<SchemaElement> referenceElements0 = new LinkedList<SchemaElement>();
        referenceElements0.add(new ReferenceElement().
                setName(table0AggC14Name).
                setReferencePath(getPath(DATA_SOURCE, DATA_SOURCE_SCHEMA_ALIAS, table0AggC14Name)));
        referenceElements0.add(new ReferenceElement().
                setName(table1Customer1Name).
                setReferencePath(getPath(DATA_SOURCE, DATA_SOURCE_SCHEMA_ALIAS, table1Customer1Name)));
        referenceElements0.add(new ReferenceElement().
                setName(table3CustomerSalesName).
                setReferencePath(getPath(DATA_SOURCE, DATA_SOURCE_SCHEMA_ALIAS, table3CustomerSalesName)));
        JoinResourceGroupElement joinTree2 = new JoinResourceGroupElement().
                setName(JOIN_TREE_2).
                setJoinInfo(new JoinInfo().setIncludeAllDataIslandJoins(false).setSuppressCircularJoins(false).setJoins(joinsList0)).
                setElements(referenceElements0);
        List<Join> joinsList1 = new LinkedList<Join>();
        joinsList1.add(new Join().
                setLeft(table4CustomerSales1Name).
                setRight(table2Customer11Name).
                setExpression(new ClientExpressionContainer().
                        setString("public_customer11.customer_id == public_customer_sales1.customer_id")).
                setWeight(1).
                setType(JOIN_TYPE_FULL_OUTER));
        List<SchemaElement> referenceElements1 = new LinkedList<SchemaElement>();
        referenceElements1.add(new ReferenceElement().
                setName(table4CustomerSales1Name).
                setReferencePath(getPath(DATA_SOURCE, DATA_SOURCE_SCHEMA_ALIAS, table4CustomerSales1Name)));
        referenceElements1.add(new ReferenceElement().
                setName(table2Customer11Name).
                setReferencePath(getPath(DATA_SOURCE, DATA_SOURCE_SCHEMA_ALIAS, table2Customer11Name)));
        JoinResourceGroupElement joinTree3 = new JoinResourceGroupElement().
                setName(JOIN_TREE_3).
                setJoinInfo(new JoinInfo().setIncludeAllDataIslandJoins(false).setSuppressCircularJoins(false).setJoins(joinsList1)).
                setElements(referenceElements1);

        schema.getResources().add(joinTree2);
        schema.getResources().add(joinTree3);

        // add new data islands to presentation
        PresentationGroupElement joinTree2PresentationGroupElement = new PresentationGroupElement().setName(JOIN_TREE_2);
        List<PresentationElement> joinTree2PresentationGroupElements = new LinkedList<PresentationElement>();

        PresentationGroupElement presentationTable0AggC14 = resourceToPresentationGroupElement(table0AggC14, JOIN_TREE_2);
        addPostfix(presentationTable0AggC14, 4);
        joinTree2PresentationGroupElements.add(presentationTable0AggC14);

        PresentationGroupElement presentationTable1Customer1 = resourceToPresentationGroupElement(table1Customer1, JOIN_TREE_2);
        addPostfix(presentationTable1Customer1, 5);
        joinTree2PresentationGroupElements.add(presentationTable1Customer1);

        PresentationGroupElement presentationTable3CustomerSales = resourceToPresentationGroupElement(table3CustomerSales, JOIN_TREE_2);
        addPostfix(presentationTable3CustomerSales, 6);
        joinTree2PresentationGroupElements.add(presentationTable3CustomerSales);

        joinTree2PresentationGroupElement.setElements(joinTree2PresentationGroupElements);

        PresentationGroupElement joinTree3PresentationGroupElement = new PresentationGroupElement().setName(JOIN_TREE_3);
        List<PresentationElement> joinTree3PresentationGroupElements = new LinkedList<PresentationElement>();

        PresentationGroupElement presentationTable2Customer1 = resourceToPresentationGroupElement(table2Customer11, JOIN_TREE_3);
        addPostfix(presentationTable2Customer1, 8);
        joinTree3PresentationGroupElements.add(presentationTable2Customer1);

        PresentationGroupElement presentationTable4CustomerSales = resourceToPresentationGroupElement(table4CustomerSales1, JOIN_TREE_3);
        addPostfix(presentationTable4CustomerSales, 7);
        joinTree3PresentationGroupElements.add(presentationTable4CustomerSales);

        joinTree3PresentationGroupElement.setElements(joinTree3PresentationGroupElements);

        schema.getPresentation().add(joinTree2PresentationGroupElement);
        schema.getPresentation().add(joinTree3PresentationGroupElement);
    }

    public void addFieldsWithRestructuring(Schema schema) {
        List<PresentationElement> presentationElements = schema.getPresentation().get(0).getElements();
        PresentationSingleElement presentationSingleElement0 = null;
        PresentationSingleElement presentationSingleElement1 = null;
        PresentationSingleElement presentationSingleElement2 = null;
        PresentationSingleElement presentationSingleElement3 = null;
        PresentationSingleElement presentationSingleElement4 = null;
        PresentationSingleElement presentationSingleElement5 = null;

        // find particular fields and remove them from presentation sets
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

        // add some of removed elements to new set
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

        // add removed elements to presentation without adding to any set
        presentationElements.add(presentationSingleElement3.
                setHierarchicalName("low_fat"));
        presentationElements.add(presentationSingleElement5.
                setHierarchicalName("product_id1"));
        presentationElements.add(presentationSingleElement2.
                setHierarchicalName("net_weight"));
    }

    private void addPostfix(PresentationGroupElement groupElement, int postfix) {
        for (PresentationElement presentationElement : groupElement.getElements()) {
            presentationElement.setName(presentationElement.getName() + postfix);
            presentationElement.setDescription(presentationElement.getDescription() + postfix);
            ((PresentationSingleElement) presentationElement).setHierarchicalName(((PresentationSingleElement) presentationElement).getHierarchicalName() + postfix);
        }
    }
}
