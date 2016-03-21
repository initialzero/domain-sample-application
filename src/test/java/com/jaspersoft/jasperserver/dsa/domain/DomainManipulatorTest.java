package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dsa.initialization.data.InitDataHelper;
import com.jaspersoft.jasperserver.dto.resources.domain.ClientDomain;
import org.junit.Before;
import org.junit.Test;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class DomainManipulatorTest {

    @Before
    public void before() {

    }

    @Test
    public void compareSchemas() {
        ClientDomain fetchedDomain = InitDataHelper.fetchDomain("/publoc/DomainDemo", "New_domain", "/public/Samples/Data_Sources/FoodmartDataSourceJNDI");

        DomainManipulator manipulator = new DomainManipulator();
        ClientDomain updatedDomain = manipulator.addCalculatedFields(fetchedDomain);


    }
}