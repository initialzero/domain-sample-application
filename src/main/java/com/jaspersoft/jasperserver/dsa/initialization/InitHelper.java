package com.jaspersoft.jasperserver.dsa.initialization;

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
public class InitHelper {

    public static AppConfiguration initConfiguration(Properties properties) {
        AppConfiguration configuration = new AppConfiguration(properties);
        return configuration;
    }
}
