package com.jaspersoft.jasperserver.dsa.common;

import com.jaspersoft.jasperserver.jaxrs.client.core.JasperserverRestClient;
import com.jaspersoft.jasperserver.jaxrs.client.core.RestClientConfiguration;
import com.jaspersoft.jasperserver.jaxrs.client.core.Session;
import com.jaspersoft.jasperserver.jaxrs.client.core.exceptions.AuthenticationFailedException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class AppConfiguration {

    private static final Logger appLogger = Logger.getLogger(AppConfiguration.class);

    protected String baseRepositoryFolder;
    protected String uri;
    protected String username;
    protected String password;
    protected Session session;

    public String getResponseFormat() {
        return responseFormat;
    }

    public void setResponseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
    }

    protected String responseFormat;

    public String getResultDirectory() {
        return resultDirectory;
    }

    public void setResultDirectory(String resultDirectory) {
        this.resultDirectory = resultDirectory;
    }

    protected String resultDirectory;

    protected Properties properties;
    public AppConfiguration(Properties properties) {
        this.properties = properties;
        this.baseRepositoryFolder = properties.getProperty("baseFolder");
        this.uri = properties.getProperty("url");
        this.username = properties.getProperty("username");
        this.password = properties.getProperty("password");
        this.resultDirectory = properties.getProperty("resultDirectory");
    }

    public String getBaseRepositoryFolder() {
        return baseRepositoryFolder;
    }

    public String getUri() {
        return uri;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Properties getProperties() {
        return properties;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }


    public void initSession() {
        appLogger.info("Authentication on JasperReportsServer " + uri);

        // init JavaRestClient and log in on the JasperReportsServer
        RestClientConfiguration restClientConfiguration = RestClientConfiguration.loadConfiguration(properties);
        try {
            JasperserverRestClient client = new JasperserverRestClient(restClientConfiguration);
            session = client.authenticate(username,
                    password);
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

    public  void stopApplication() {
        appLogger.info("Logout on JasperReportsServer");
        session.logout();
        appLogger.info("Logout is successful");

    }

}
