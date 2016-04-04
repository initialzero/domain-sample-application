package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dto.resources.ClientFolder;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.jasperserver.dto.resources.domain.ClientDomain;
import com.jaspersoft.jasperserver.jaxrs.client.core.JasperserverRestClient;
import com.jaspersoft.jasperserver.jaxrs.client.core.RestClientConfiguration;
import com.jaspersoft.jasperserver.jaxrs.client.core.Session;
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
public class DomainSamplesUtil {
    private static final Logger appLogger = Logger.getLogger(DomainSamplesUtil.class);
    public static final String DATA_SOURCE_URI = "/public/Samples/Data_Sources/FoodmartDataSourceJNDI";
    private final SchemaManipulator schemaManipulator = new SchemaManipulator();
    private AppConfiguration configuration;
    protected Session session;

    public DomainSamplesUtil(AppConfiguration configuration) {
        this.configuration = configuration;
    }

    public void initSession() {
        appLogger.info("Authentication on JasperReportsServer");
        RestClientConfiguration restClientConfiguration = RestClientConfiguration.loadConfiguration(configuration.getProperties());
        JasperserverRestClient client = new JasperserverRestClient(restClientConfiguration);
        session = client.authenticate(configuration.getUsername(),
                configuration.getPassword());
        if (session == null){
            appLogger.warn("Authentication failed");
            System.exit(-1);
        }
        appLogger.info("Authentication is successful");
    }

    public void createBaseFolder() {
        appLogger.info("Create demonstration resources (folders) on the server");

        ClientFolder folder = new ClientFolder();
        folder
                .setUri(configuration.getBaseRepositoryFolder())
                .setLabel("DomainDemo")
                .setDescription("Test folder")
                .setVersion(0);

        OperationResult<ClientResource> operationResult = session
                .resourcesService()
                .resource(folder.getUri())
                .createOrUpdate(folder);
        int status = operationResult.getResponse().getStatus();
        if (status >= 400) {
            appLogger.warn("Error of creating demonstration resources. Error code: " + status);
            stopApplication();
            System.exit(1);
        }
        appLogger.info(configuration.getBaseRepositoryFolder() + " was created successfully");
        appLogger.info("Demonstration resources were created successfully");
    }

    public void deleteBaseFolder(Boolean value) {
        if (!value) return;
        appLogger.info("Clean up demonstration resources on the server");

        OperationResult operationResult = session
                .resourcesService()
                .resource(configuration.getBaseRepositoryFolder())
                .delete();
        int status = operationResult.getResponse().getStatus();
        if (status >= 400) {
            appLogger.warn("Error of cleaning demonstration resources. Error code: " + status);
            stopApplication();
            System.exit(1);
        }
        appLogger.info(configuration.getBaseRepositoryFolder() + " was deleted  successfully");
        appLogger.info("Demonstration resources were deleted successfully");
    }

    public void createBaseDomain() {
        appLogger.info("Start to create domain with single data island on the server...");
        createDomainOnServer(InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain",
                DATA_SOURCE_URI));
    }

    public void addCalculatedFields() {
        appLogger.info("Add calculated fields to base domain...");
        createDomainOnServer(schemaManipulator.addCalculatedFields(
                InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                        "Base_domain_with_calculated_fields",
                        DATA_SOURCE_URI)));

    }

    public void addCrossTableCalculatedField() {
        appLogger.info("Add cross table calculated field to base domain...");
        createDomainOnServer(schemaManipulator.addCrossTableCalculatedFields(
                InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                        "Base_domain_with_cross_table_calculated_fields",
                        DATA_SOURCE_URI)));

    }

    public void addFilters() {
        appLogger.info("Add filters to particular tables in base domain...");
        createDomainOnServer(schemaManipulator.addFilters(
                InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                        "Base_domain_with_filters",
                        DATA_SOURCE_URI)));
    }


    public void addDerivedTable() {
        appLogger.info("Add derived table to base domain...");
        createDomainOnServer(schemaManipulator.addDerivedTable(
                InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                        "Base_domain_with_derived_table",
                        DATA_SOURCE_URI)));
    }

    public void copyTable(String tableName) {
        appLogger.info("Create copy of " + tableName + " table in base domain...");
        createDomainOnServer(schemaManipulator.createTableCopy(
                InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                        "Base_domain_with_copy_of_table",
                        DATA_SOURCE_URI), tableName));
    }

    public void addConstantCalculatedField(int constantValue) {
        appLogger.info("Add constant calculation field to base domain...");
        createDomainOnServer(schemaManipulator.addConstantCalculatedField(InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_constant_calculated_field",
                DATA_SOURCE_URI), constantValue));
    }

    public void addCrossTableFilter() {
        appLogger.info("Add cross table filter to base domain...");
        createDomainOnServer(schemaManipulator.addCrossTableFilter(InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_cross_table_filter",
                DATA_SOURCE_URI)));
    }

    public void addTwoFieldsFilter() {
        appLogger.info("Add two fields filter of one table to base domain...");
        createDomainOnServer(schemaManipulator.addTwoFieldsFilter(InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_two_fields_filter",
                DATA_SOURCE_URI)));
    }

    public void addDataIslands() {
        appLogger.info("Start to create domain with three data islands on the server...");
        createDomainOnServer(schemaManipulator.addDataIslands(InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_three_data_islands",
                DATA_SOURCE_URI)));
    }

    public void addFieldsWithRestructuring() {
        appLogger.info("Start to add fields to domain presentation with restructurng...");
        createDomainOnServer(schemaManipulator.addFieldsWithRestructuring(InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_fields_with_restructuring",
                DATA_SOURCE_URI)));

    }

    private void createDomainOnServer(ClientDomain domain) {
        OperationResult<ClientDomain> operationResult = session
                .domainService()
                .domain(configuration.getBaseRepositoryFolder())
                .create(domain);
        int status = operationResult.getResponse().getStatus();
        if (status >= 400) {
            appLogger.warn("Error of creating domain. Error code: " + status);
            stopApplication();
            System.exit(1);
        }
        appLogger.info(operationResult.getEntity().getLabel() + " was created on the server successfully");
    }

    public void stopApplication() {
        appLogger.info("Logout on JasperReportsServer");
        session.logout();
        appLogger.info("Logout is successful");

    }
}
