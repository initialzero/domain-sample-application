package com.jaspersoft.jasperserver.dsa.initialization.app;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import java.util.Properties;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class InitAppHelper {

    public static AppConfiguration initApplication(Properties properties) {
        AppConfiguration configuration = new AppConfiguration(properties);
        configuration.initClient();
        configuration.initSession();
        return configuration;
    }

}
