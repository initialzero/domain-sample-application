package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dsa.initialization.data.InitDataHelper;
import com.jaspersoft.jasperserver.dto.resources.domain.ClientDomain;
import com.jaspersoft.jasperserver.dto.resources.domain.ResourceElement;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class DomainSamplesUtilTest {
    AppConfiguration configuration;
    DomainSamplesUtil domainSamplesUtil;

    @Before
    public void before() {
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration = new AppConfiguration(properties);
        domainSamplesUtil = new DomainSamplesUtil(configuration);
        domainSamplesUtil.initSession();

    }

    @Test
    public void compareSchemas() {
        OperationResult<ClientDomain> operationResult = domainSamplesUtil.session.domainService().
                domain("/public/New_Domain").
                get();

        ClientDomain domainRetrieved = operationResult.getEntity();

        ClientDomain fetchedDomain = InitDataHelper.buildDomain("/publoc/DomainDemo", "New_domain", "/public/Samples/Data_Sources/FoodmartDataSourceJNDI");

        assertEquals(domainRetrieved.getSchema(), fetchedDomain.getSchema());
        List<ResourceElement> resources = domainRetrieved.getSchema().getResources();
        List<ResourceElement> resources1 = fetchedDomain.getSchema().getResources();
        assertEquals(resources, resources1);

        assertEquals(domainRetrieved.getSchema().getPresentation(), fetchedDomain.getSchema().getPresentation());


    }


}