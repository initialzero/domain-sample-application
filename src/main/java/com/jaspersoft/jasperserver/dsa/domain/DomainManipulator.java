package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dsa.initialization.data.InitDataHelper;
import com.jaspersoft.jasperserver.dto.resources.domain.ClientDomain;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationElement;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationSingleElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceGroupElement;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceSingleElement;
import com.jaspersoft.jasperserver.dto.resources.domain.SchemaElement;
import java.util.Collections;
import java.util.Comparator;
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
            if (resource.getName().equals("public_agg_ll_01_sales_fact_1997")) {
                ((ResourceGroupElement) resource).setFilterExpression("CalcField_num > 10");
            }
            if (resource.getName().equals("public_customer")) {
                ((ResourceGroupElement) resource).setFilterExpression("country == 'USA'");
            }
            if (resource.getName().equals("public_product")) {
                ((ResourceGroupElement) resource).setFilterExpression("low_fat == true and net_weight < 10.0");
            }

        }

        return clientDomain;
    }

    private void sortResources(List<SchemaElement> resources){
        Collections.sort(resources, new Comparator<SchemaElement>() {
            public int compare(SchemaElement element1, SchemaElement element2) {
                return element1.getName().toLowerCase().compareTo(element2.getName().toLowerCase());
            }
        });
    }
}
