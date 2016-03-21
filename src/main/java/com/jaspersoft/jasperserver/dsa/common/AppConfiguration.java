package com.jaspersoft.jasperserver.dsa.common;

import com.jaspersoft.jasperserver.jaxrs.client.core.JasperserverRestClient;
import com.jaspersoft.jasperserver.jaxrs.client.core.RestClientConfiguration;
import com.jaspersoft.jasperserver.jaxrs.client.core.Session;
import com.sun.istack.internal.logging.Logger;
import java.util.Properties;

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
    private RestClientConfiguration restClientConfiguration;
    protected String baseRepositoryFolder;
    protected String uri;
    protected String username;
    protected String password;
    protected JasperserverRestClient client;
    protected Session session;
    protected Properties properties;

    public AppConfiguration(Properties properties) {
        this.properties = properties;
        this.baseRepositoryFolder = properties.getProperty("baseFolder");
        this.uri = properties.getProperty("url");
        this.username = properties.getProperty("username");
        this.password = properties.getProperty("password");
    }

    public String getBaseRepositoryFolder() {
        return baseRepositoryFolder;
    }

    public void setBaseRepositoryFolder(String baseRepositoryFolder) {
        this.baseRepositoryFolder = baseRepositoryFolder;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void initClient() {
        restClientConfiguration = RestClientConfiguration.loadConfiguration(this.properties);
        client = new JasperserverRestClient(restClientConfiguration);
    }

    public void initSession() {
        initSession(properties.getProperty("username"),
                properties.getProperty("password"));
    }

    protected void initSession(String username, String password) {
        appLogger.info("Authentication on JasperReportsServer");
        session = client.authenticate(username, password);
        if (session == null){
            appLogger.warning("Authentication failed");
            System.exit(0);
        }
        appLogger.info("Authentication successful");

    }
    public void closeSession() {
        appLogger.info("Log out on JasperReportsServer");
        session.logout();
        appLogger.info("Logout successful");

    }


    public Session getSession() {
        if (session == null) this.initSession();
        return session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
