package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dto.resources.ClientSemanticLayerDataSource;
import com.jaspersoft.jasperserver.dto.resources.domain.DataIslandsContainer;
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
public class DomainMetadataUtil {
    private static final Logger appLogger = Logger.getLogger(DomainMetadataUtil.class);
    private AppConfiguration configuration;
    protected Session session;

    public DomainMetadataUtil(AppConfiguration configuration) {
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
            appLogger.warn("Authentication failed " + e.getCause());
            System.exit(-1);
        }
        restClientConfiguration.setHandleErrors(false);
        appLogger.info("Authentication is successful");
    }

    public DataIslandsContainer fetchMetadata(String domainUri) {
        appLogger.info("Fetch metadata for " + domainUri);
        DataIslandsContainer metadata = null;
        ClientSemanticLayerDataSource domain = new ClientSemanticLayerDataSource().setUri(domainUri);

        OperationResult<DataIslandsContainer> operationResult = session.
                dataDiscoveryService().
                domainContext().
                fetchMetadataByContext(domain);
        try {
            metadata = operationResult.getEntity();
            appLogger.info("Metadata was fetched successfully");
        } catch (Exception e) {
            appLogger.warn("Getting metadata failed (" + e.getClass().getSimpleName() + ")");
            stopApplication();
            System.exit(1);
        }
        return metadata;
    }

    public void stopApplication() {
        appLogger.info("Logout on JasperReportsServer");
        session.logout();
        appLogger.info("Logout is successful");

    }
}
