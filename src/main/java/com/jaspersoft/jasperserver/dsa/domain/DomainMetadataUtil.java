package com.jaspersoft.jasperserver.dsa.domain;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dto.resources.ClientSemanticLayerDataSource;
import com.jaspersoft.jasperserver.dto.resources.domain.PresentationGroupElement;
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


    public DomainMetadataUtil(AppConfiguration configuration) {
        this.configuration = configuration;
    }


    public PresentationGroupElement fetchMetadata(String domainUri) {
        appLogger.info("Fetch metadata for " + domainUri);
        PresentationGroupElement metadata = null;
        ClientSemanticLayerDataSource domain = new ClientSemanticLayerDataSource().setUri(domainUri);

        // send metadata request to server
        OperationResult<PresentationGroupElement> operationResult = configuration.getSession().
                dataDiscoveryService().
                domainContext().
                fetchMetadataByContext(domain);
        try {
            metadata = operationResult.getEntity();
            appLogger.info("Metadata was fetched successfully");
        } catch (Exception e) {
            appLogger.warn("Getting metadata failed (" + e.getClass().getSimpleName() + ")");
            configuration.stopApplication();
            System.exit(1);
        }
        return metadata;
    }

}
