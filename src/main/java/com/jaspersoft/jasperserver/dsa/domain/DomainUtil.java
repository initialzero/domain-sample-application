package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dsa.initialization.data.InitDataHelper;
import com.jaspersoft.jasperserver.dto.resources.ClientFolder;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.jasperserver.dto.resources.domain.ClientDomain;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import org.apache.log4j.Logger;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class DomainUtil {
    private static final Logger appLogger = Logger.getLogger(DomainUtil.class);
    private InitDataHelper initDataHelper = new InitDataHelper();
    private AppConfiguration configuration;


    public DomainUtil(AppConfiguration configuration) {
        this.configuration = configuration;
    }

    public void createBaseFolder() {
        appLogger.info("Create demonstration resources on the server");

        ClientFolder folder = new ClientFolder();
        folder
                .setUri(configuration.getBaseRepositoryFolder())
                .setLabel("DomainDemo")
                .setDescription("Test folder")
                .setVersion(0);

        OperationResult<ClientResource> operationResult = configuration.getSession()
                .resourcesService()
                .resource(folder.getUri())
                .createOrUpdate(folder);
        int status = operationResult.getResponse().getStatus();
        if (status >= 400) {
            appLogger.warn("Error of creating demonstration resources. Error code: " + status);
            System.exit(0);
        }
        appLogger.info(configuration.getBaseRepositoryFolder() + " was created successful");
        appLogger.info("Demonstration resources were created successful");
    }

    public void deleteBaseFolder() {
        appLogger.info("Clean demonstration resources on the server");

        OperationResult operationResult = configuration.getSession()
                .resourcesService()
                .resource(configuration.getBaseRepositoryFolder())
                .delete();
        int status = operationResult.getResponse().getStatus();
        if (status >= 400) {
            appLogger.warn("Error of cleaning demonstration resources. Error code: " + status);
            System.exit(0);
        }
        appLogger.info(configuration.getBaseRepositoryFolder() + " was deleted  successful");
        appLogger.info("Demonstration resources were deleted successful");
    }

    public void createDomain() {
        appLogger.info("Start to create domain with single data island.");
        OperationResult<ClientDomain> operationResult = configuration.getSession()
                .domainService()
                .domain(configuration.getBaseRepositoryFolder())
                .create(InitDataHelper.fetchDomain(configuration.getBaseRepositoryFolder(),
                        "New_domain",
                        "/public/Samples/Data_Sources/FoodmartDataSourceJNDI"));
        int status = operationResult.getResponse().getStatus();
        if (status >= 400) {
            appLogger.warn("Error of creating domain. Error code: " + status);
            System.exit(0);
        }
        appLogger.info( operationResult.getEntity().getLabel() + " was created successful");
        appLogger.info("Domain was created successful");
    }


}
