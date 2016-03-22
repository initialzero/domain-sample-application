package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dsa.initialization.data.InitDataHelper;
import com.jaspersoft.jasperserver.dto.resources.domain.ClientDomain;
import com.jaspersoft.jasperserver.dto.resources.domain.Join;
import com.jaspersoft.jasperserver.dto.resources.domain.JoinResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import com.jaspersoft.jasperserver.dto.resources.domain.QueryResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ReferenceElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceSingleElement;
import com.jaspersoft.jasperserver.dto.resources.domain.SchemaElement;
import java.util.Collections;
import java.util.Comparator;
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
public class DomainManipulator {

    public ClientDomain addCalculatedFields(ClientDomain domain) {
        ResourceGroupElement datasource = (ResourceGroupElement) domain.getSchema().getResources().get(0);
        ResourceGroupElement datasourceSchema = (ResourceGroupElement) datasource.getElements().get(0);
        List<SchemaElement> resources = datasourceSchema.getElements();
        for (SchemaElement resource : resources) {
            if (resource.getName().equals("public_agg_ll_01_sales_fact_1997")) {
                ((ResourceGroupElement) resource).getElements().add(new ResourceSingleElement().
                        setName("CalcField_num").
                        setType(InitDataHelper.JAVA_MATH_BIG_DECIMAL).
                        setExpression("store_cost*10"));
                sortResources(((ResourceGroupElement) resource).getElements());
            }
            if (resource.getName().equals("public_customer")) {
                ((ResourceGroupElement) resource).getElements().add(new ResourceSingleElement().
                        setName("CalcField_string").
                        setType(InitDataHelper.JAVA_LANG_STRING).
                        setExpression("concat(fname,' ',lname)"));
                sortResources(((ResourceGroupElement) resource).getElements());
            }

        }

        List<PresentationElement> presentations = domain.getSchema().getPresentation();
        presentations.add(new PresentationSingleElement().
                setType(InitDataHelper.JAVA_MATH_BIG_DECIMAL)
                .setLabel("CalcField_num")
                .setLabelId("")
                .setDescription("CalcField_num")
                .setDescriptionId("")
                .setResourcePath("JoinTree_1.public_agg_ll_01_sales_fact_1997.CalcField_num")
                .setName("CalcField_num"));
        presentations.add(new PresentationSingleElement().
                setType(InitDataHelper.JAVA_LANG_STRING)
                .setLabel("CalcField_string")
                .setLabelId("")
                .setDescription("CalcField_string")
                .setDescriptionId("")
                .setResourcePath("JoinTree_1.public_customer.CalcField_string")
                .setName("CalcField_string"));

        return domain;
    }

    public ClientDomain addFilters(ClientDomain clientDomain) {
        ResourceGroupElement datasource = (ResourceGroupElement) clientDomain.getSchema().getResources().get(0);
        ResourceGroupElement datasourceSchema = (ResourceGroupElement) datasource.getElements().get(0);
        List<SchemaElement> resources = datasourceSchema.getElements();
        for (SchemaElement resource : resources) {
            //uncomment this when calculated fields will be fixed
//            if (resource.getName().equals("public_agg_ll_01_sales_fact_1997")) {
//                ((ResourceGroupElement) resource).setFilterExpression("CalcField_num > 10");
//            }
            if (resource.getName().equals("public_customer")) {
                ((ResourceGroupElement) resource).setFilterExpression("country == 'USA'");
            }
            if (resource.getName().equals("public_product")) {
                ((ResourceGroupElement) resource).setFilterExpression("low_fat == true and net_weight < 10.0");
            }
        }

        return clientDomain;
    }

    public ClientDomain addDerivedTable(ClientDomain clientDomain) {
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

        JoinResourceGroupElement joinGroup = (JoinResourceGroupElement) clientDomain.getSchema().getResources().get(1);
        List<SchemaElement> referenceElements = joinGroup.getElements();
        referenceElements.add(new ReferenceElement().setName("TestQueryCustomer").setReferencePath("FoodmartDataSourceJNDI.TestQueryCustomer"));
        joinGroup.getJoinInfo().getJoins().add(new Join().
                setLeft("TestQueryCustomer").
                setRight("public_customer").
                setExpression("public_customer.customer_id == TestQueryCustomer.customer_id").
                setWeight(1).
                setType(Join.JoinType.inner));

        List<PresentationElement> presentations = clientDomain.getSchema().getPresentation();
        List<PresentationElement> presentationSingleElements = new LinkedList<PresentationElement>();
        presentationSingleElements.add(new PresentationSingleElement().
                setName("customer_id2").
                setLabel("customer_id").
                setLabelId("").
                setDescription("customer_id2").
                setDescriptionId("").
                setResourcePath("JoinTree_1.TestQueryCustomer.customer_id").
                setType(InitDataHelper.JAVA_LANG_INTEGER));
        presentationSingleElements.add(new PresentationSingleElement().
                setName("fname1").
                setLabel("fname").
                setLabelId("").
                setDescription("fname1").
                setDescriptionId("").
                setResourcePath("JoinTree_1.TestQueryCustomer.fname").
                setType(InitDataHelper.JAVA_LANG_STRING));
        presentationSingleElements.add(new PresentationSingleElement().
                setName("fullname1").
                setLabel("fullname").
                setLabelId("").
                setDescription("fullname1").
                setDescriptionId("").
                setResourcePath("JoinTree_1.TestQueryCustomer.fullname").
                setType(InitDataHelper.JAVA_LANG_STRING));
        presentationSingleElements.add(new PresentationSingleElement().
                setName("lname1").
                setLabel("lname").
                setLabelId("").
                setDescription("lname1").
                setDescriptionId("").
                setResourcePath("JoinTree_1.TestQueryCustomer.lname").
                setType(InitDataHelper.JAVA_LANG_STRING));
        presentations.add(3, new PresentationGroupElement().
                        setName("TestQueryCustomer")
                        .setLabel("TestQueryCustomer")
                        .setLabelId("")
                        .setDescription("TestQueryCustomer")
                        .setDescriptionId("")
                        .setResourcePath("JoinTree_1")
                        .setElements(presentationSingleElements)
        );
        return clientDomain;
    }

    private void sortResources(List<SchemaElement> resources) {
        Collections.sort(resources, new Comparator<SchemaElement>() {
            public int compare(SchemaElement element1, SchemaElement element2) {
                return element1.getName().toLowerCase().compareTo(element2.getName().toLowerCase());
            }
        });
    }
}
