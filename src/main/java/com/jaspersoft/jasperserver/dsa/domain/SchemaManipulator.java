package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dsa.initialization.data.InitDataHelper;
import com.jaspersoft.jasperserver.dto.resources.domain.ClientDomain;
import com.jaspersoft.jasperserver.dto.resources.domain.ConstantsResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.Join;
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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.jaspersoft.jasperserver.dsa.initialization.data.InitDataHelper.FULL_TABLE_NAME_0;
import static com.jaspersoft.jasperserver.dsa.initialization.data.InitDataHelper.FULL_TABLE_NAME_1;
import static com.jaspersoft.jasperserver.dsa.initialization.data.InitDataHelper.FULL_TABLE_NAME_2;
import static com.jaspersoft.jasperserver.dsa.initialization.data.InitDataHelper.JAVA_LANG_INTEGER;


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
        String calcFieldNum = "CalcField_num";
        String calcFieldString = "CalcField_string";
        for (SchemaElement resource : resources) {
            if (resource.getName().equals(FULL_TABLE_NAME_0)) {
                ((ResourceGroupElement) resource).getElements().add(new ResourceSingleElement().
                        setName(calcFieldNum).
                        setType(InitDataHelper.JAVA_MATH_BIG_DECIMAL).
                        setExpression("store_cost*10"));
            }
            if (resource.getName().equals(FULL_TABLE_NAME_1)) {
                ((ResourceGroupElement) resource).getElements().add(new ResourceSingleElement().
                        setName(calcFieldString).
                        setType(InitDataHelper.JAVA_LANG_STRING).
                        setExpression("concat(fname,' ',lname)"));
            }

        }
        // add calculated fields to presentation
        List<PresentationElement> presentations = domain.getSchema().getPresentation().get(0).getElements();
        presentations.add(new PresentationSingleElement().
                setType(InitDataHelper.JAVA_MATH_BIG_DECIMAL)
                .setLabel(calcFieldNum)
                .setLabelId("")
                .setDescription(calcFieldNum)
                .setDescriptionId("")
                .setHierarchicalName("public_agg_ll_01_sales_fact_1997.CalcField_num")
                .setResourcePath("JoinTree_1.public_agg_ll_01_sales_fact_1997.CalcField_num")
                .setName(calcFieldNum));
        presentations.add(new PresentationSingleElement().
                setType(InitDataHelper.JAVA_LANG_STRING)
                .setLabel(calcFieldString)
                .setLabelId("")
                .setDescription(calcFieldString)
                .setDescriptionId("")
                .setHierarchicalName("public_customer.CalcField_string")
                .setResourcePath("JoinTree_1.public_customer.CalcField_string")
                .setName(calcFieldString));

        return domain;
    }

    public ClientDomain addCrossTableCalculatedFields(ClientDomain domain) {

        JoinResourceGroupElement joins = (JoinResourceGroupElement) domain.getSchema().getResources().get(1);
        List<SchemaElement> joinsElements = joins.getElements();

        // add calculated fields to particular tables in joinsElements
        String calcFieldNum = "CrossTableCalcField";
        joinsElements.add(new ResourceSingleElement().
                setName(calcFieldNum).
                setType(InitDataHelper.JAVA_LANG_STRING).
                setExpression("concat(public_customer.fullname,', ',public_product.brand_name)"));
        // add calculated fields to presentation
        List<PresentationElement> presentations = domain.getSchema().getPresentation().get(0).getElements();
        presentations.add(new PresentationSingleElement().
                setType(InitDataHelper.JAVA_LANG_STRING)
                .setLabel(calcFieldNum)
                .setLabelId("")
                .setDescription(calcFieldNum)
                .setDescriptionId("")
                .setHierarchicalName(calcFieldNum)
                .setResourcePath("JoinTree_1." + calcFieldNum)
                .setName(calcFieldNum));

        return domain;
    }

    public ClientDomain addConstantCalculatedField(ClientDomain domain, Integer value) {

        List<ResourceElement> resources = domain.getSchema().getResources();
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

        // add constant calculated field to presentation
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
        /*
        PresentationGroupElement constants = new PresentationGroupElement().
                setName("constant_fields_level").
                setDescription("constant_fields_level").
                setLabel("Constants").
                setElements(presentationSingleElements);

        List<PresentationElement> constantsList = new LinkedList<PresentationElement>();
        constantsList.add(constants);
*/
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
            if (resource.getName().equals(FULL_TABLE_NAME_1)) {
                ((ResourceGroupElement) resource).setFilterExpression("country == 'USA'");
            }
            if (resource.getName().equals(FULL_TABLE_NAME_2)) {
                ((ResourceGroupElement) resource).setFilterExpression("low_fat == true and net_weight < 10.0");
            }
        }

        return clientDomain;
    }

    public ClientDomain addDerivedTable(ClientDomain clientDomain) {

        // add  new query resource element to resources
        ResourceGroupElement datasource = (ResourceGroupElement) clientDomain.getSchema().getResources().get(0);
        List<SchemaElement> singleElements = new LinkedList<SchemaElement>();
        singleElements.add(new ResourceSingleElement().setName("customer_id").setType(InitDataHelper.JAVA_LANG_INTEGER));
        singleElements.add(new ResourceSingleElement().setName("fname").setType(InitDataHelper.JAVA_LANG_STRING));
        singleElements.add(new ResourceSingleElement().setName("fullname").setType(InitDataHelper.JAVA_LANG_STRING));
        singleElements.add(new ResourceSingleElement().setName("lname").setType(InitDataHelper.JAVA_LANG_STRING));
        datasource.getElements().add(new QueryResourceGroupElement()
                .setName("TestQueryCustomer")
                .setElements(singleElements)
                .setQuery("select * from public.customer"));

        // join new query resource with existing resources in data island
        JoinResourceGroupElement joinGroup = (JoinResourceGroupElement) clientDomain.getSchema().getResources().get(1);
        List<SchemaElement> referenceElements = joinGroup.getElements();
        referenceElements.add(new ReferenceElement().setName("TestQueryCustomer").setReferencePath("FoodmartDataSourceJNDI.TestQueryCustomer"));
        joinGroup.getJoinInfo().getJoins().add(new Join().
                setLeft("TestQueryCustomer").
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
                setType(InitDataHelper.JAVA_LANG_INTEGER));
        presentationSingleElements.add(new PresentationSingleElement().
                setName("fname1").
                setLabel("fname").
                setLabelId("").
                setDescription("fname1").
                setDescriptionId("").
                setHierarchicalName("TestQueryCustomer.fname").
                setResourcePath("JoinTree_1.TestQueryCustomer.fname").
                setType(InitDataHelper.JAVA_LANG_STRING));
        presentationSingleElements.add(new PresentationSingleElement().
                setName("fullname1").
                setLabel("fullname").
                setLabelId("").
                setDescription("fullname1").
                setDescriptionId("").
                setHierarchicalName("TestQueryCustomer.fullname").
                setResourcePath("JoinTree_1.TestQueryCustomer.fullname").
                setType(InitDataHelper.JAVA_LANG_STRING));
        presentationSingleElements.add(new PresentationSingleElement().
                setName("lname1").
                setLabel("lname").
                setLabelId("").
                setDescription("lname1").
                setDescriptionId("").
                setHierarchicalName("TestQueryCustomer.lname").
                setResourcePath("JoinTree_1.TestQueryCustomer.lname").
                setType(InitDataHelper.JAVA_LANG_STRING));
        presentations.add(3, new PresentationGroupElement().
                        setName("TestQueryCustomer")
                        .setLabel("TestQueryCustomer")
                        .setLabelId("")
                        .setDescription("TestQueryCustomer")
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
        copyElement.setName(tableName+1);
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

        Map<String,String> fieldsNames = new LinkedHashMap<String, String>();
        fieldsNames.put("account_num", "account_num1");
        fieldsNames.put("address1", "address11");
        fieldsNames.put("address2", "address21");
        fieldsNames.put("address3", "address31");
        fieldsNames.put("address4", "address41");
        fieldsNames.put("birthdate", "birthdate1");
        fieldsNames.put("CalcField_string", "CalcField_string1");
        fieldsNames.put("city", "city1");
        fieldsNames.put("country", "country1");
        fieldsNames.put("customer_id", "customer_id3");
        fieldsNames.put("customer_region_id", "customer_region_id1");
        fieldsNames.put("date_accnt_opened", "date_accnt_opened1");
        fieldsNames.put("education", "education1");
        fieldsNames.put("fname", "fname2");
        fieldsNames.put("fullname", "fullname2");
        fieldsNames.put("gender", "gender1");
        fieldsNames.put("houseowner", "houseowner1");
        fieldsNames.put("lname", "lname2");
        fieldsNames.put("marital_status", "marital_status1");
        fieldsNames.put("member_card", "member_card1");
        fieldsNames.put("mi", "mi1");
        fieldsNames.put("num_cars_owned", "num_cars_owned1");
        fieldsNames.put("num_children_at_home", "num_children_at_home1");
        fieldsNames.put("occupation", "occupation1");
        fieldsNames.put("phone1", "phone11");
        fieldsNames.put("phone2", "phone21");
        fieldsNames.put("postal_code", "postal_code1");
        fieldsNames.put("state_province", "state_province1");
        fieldsNames.put("total_children", "total_children1");
        fieldsNames.put("yearly_income", "yearly_income1");

        List<PresentationElement> presentationElements = new ArrayList<PresentationElement>();
        String elementName;
        String elementLabel;
        for (SchemaElement resourceElement : copyElement.getElements()) {
            ResourceSingleElement castedElement = (ResourceSingleElement) resourceElement;
            elementName = fieldsNames.get(castedElement.getName());
            elementLabel = castedElement.getName();
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
}
