package com.jaspersoft.jasperserver.dsa.common;

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


    protected String baseRepositoryFolder;
    protected String uri;
    protected String username;
    protected String password;
    protected String domainUri;

    protected Properties properties;
    public AppConfiguration(Properties properties) {
        this.properties = properties;
        this.baseRepositoryFolder = properties.getProperty("baseFolder");
        this.uri = properties.getProperty("url");
        this.username = properties.getProperty("username");
        this.password = properties.getProperty("password");
        this.domainUri = properties.getProperty("domainUri");
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

    public String getDomainUri() {
        return domainUri;
    }
}
