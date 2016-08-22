package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dto.resources.ClientFolder;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.jasperserver.dto.resources.domain.ClientDomain;
import com.jaspersoft.jasperserver.jaxrs.client.core.JasperserverRestClient;
import com.jaspersoft.jasperserver.jaxrs.client.core.RestClientConfiguration;
import com.jaspersoft.jasperserver.jaxrs.client.core.Session;
import com.jaspersoft.jasperserver.jaxrs.client.core.exceptions.AuthenticationFailedException;
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
        appLogger.info("Authentication on JasperReportsServer " + configuration.getUri());
        // init JavaRestClient and log in on the JasperReportsServer
        RestClientConfiguration restClientConfiguration = RestClientConfiguration.loadConfiguration(configuration.getProperties());
        try {
            JasperserverRestClient client = new JasperserverRestClient(restClientConfiguration);
            session = client.authenticate(configuration.getUsername(),
                    configuration.getPassword());
            if (session == null) {
                throw new AuthenticationFailedException();
            }
        } catch (Exception e) {
            appLogger.warn("Authentication failed " , e);
            System.exit(-1);
        }
        // set property of RestClient not to handle negative responses to get
        restClientConfiguration.setHandleErrors(false);
        appLogger.info("Authentication is successful");
    }

    public void createBaseFolder() {
        appLogger.info("Create demonstration resources (folders) on the server");

        ClientFolder folder = new ClientFolder();
        String label = configuration.getBaseRepositoryFolder().substring(configuration.getBaseRepositoryFolder().lastIndexOf("/") + 1);
        folder
                .setUri(configuration.getBaseRepositoryFolder())
                .setLabel(label)
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
        // create base domain
        ClientDomain baseDomain = InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain",
                DATA_SOURCE_URI);

        // save domain on the server
        saveDomain(baseDomain);

    }

    public void addCalculatedFields() {
        appLogger.info("Add calculated fields to base domain...");
        // create base domain
        ClientDomain domain = InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_calculated_fields",
                DATA_SOURCE_URI);

        // add calculated fields to domain schema
        schemaManipulator.addCalculatedFields(domain.getSchema());

        // save domain on the server
        saveDomain(domain);

    }

    public void addCrossTableCalculatedField() {
        appLogger.info("Add cross table calculated field to base domain...");
        // create base domain
        ClientDomain domain = InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_cross_table_calculated_fields",
                DATA_SOURCE_URI);

        // add cross table calculated fields to domain schema
        schemaManipulator.addCrossTableCalculatedFields(domain.getSchema());

        // save domain on the server
         saveDomain(domain);

    }

    public void addFilters() {
        appLogger.info("Add filters to particular tables in base domain...");
        // create base domain
        ClientDomain domain = InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_filters",
                DATA_SOURCE_URI);

        // add filters to domain schema
        schemaManipulator.addFilters(domain.getSchema());

        // save domain on the server
        saveDomain(domain);
    }


    public void addDerivedTable() {
        appLogger.info("Add derived table to base domain...");
        // create base domain
        ClientDomain domain = InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_derived_table",
                DATA_SOURCE_URI);

        // add derived table to domain schema
        schemaManipulator.addDerivedTable(domain.getSchema());

        // save domain on the server
        saveDomain(domain);
    }

    public void copyTable(String tableName) {
        appLogger.info("Create copy of " + tableName + " table in base domain...");
        // create base domain
        ClientDomain domain = InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_copy_of_table",
                DATA_SOURCE_URI);
        schemaManipulator.createTableCopy(domain.getSchema(), tableName);

        // save domain on the server
        saveDomain(domain);
    }

    public void addConstantCalculatedField(int constantValue) {
        appLogger.info("Add constant calculation field to base domain...");
        // create base domain
        ClientDomain domain = InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_constant_calculated_field",
                DATA_SOURCE_URI);

        // add constant calculated field to domain schema
        schemaManipulator.addConstantCalculatedField(domain.getSchema(), constantValue);

        // save domain on the server
        saveDomain(domain);
    }

    public void addCrossTableFilter() {
        appLogger.info("Add cross table filter to base domain...");
        // create base domain
        ClientDomain domain = InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_cross_table_filter",
                DATA_SOURCE_URI);

        // add cross table filter to domain schema
        schemaManipulator.addCrossTableFilter(domain.getSchema());

        // save domain on the server
        saveDomain(domain);
    }

    public void addTwoFieldsFilter() {
        appLogger.info("Add two fields filter of one table to base domain...");
        // create base domain
        ClientDomain domain = InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_two_fields_filter",
                DATA_SOURCE_URI);

        // add two fields filter to domain schema
        schemaManipulator.addTwoFieldsFilter(domain.getSchema());

        // save domain on the server
        saveDomain(domain);
    }

    public void addDataIslands() {
        appLogger.info("Start to create domain with three data islands on the server...");
        // create base domain
        ClientDomain domain = InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_three_data_islands",
                DATA_SOURCE_URI);

        // add two more data islands to domain schema
        schemaManipulator.addDataIslands(domain.getSchema());

        // save domain on the server
        saveDomain(domain);
    }

    public void addFieldsWithRestructuring() {
        appLogger.info("Start to add fields to domain presentation with restructurng...");
        // create base domain
        ClientDomain domain = InitDomainHelper.buildDomain(configuration.getBaseRepositoryFolder(),
                "Base_domain_with_fields_with_restructuring",
                DATA_SOURCE_URI);

        // add fields with restructuring to domain schema
        schemaManipulator.addFieldsWithRestructuring(domain.getSchema());

        // save domain on the server
        saveDomain(domain);

    }

    private void saveDomain(ClientDomain domain) {
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
