package com.jaspersoft.jasperserver.dsa;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dsa.common.ConsoleUtil;
import com.jaspersoft.jasperserver.dsa.initialization.FileInitializationStrategy;
import com.jaspersoft.jasperserver.dsa.initialization.InitHelper;
import com.jaspersoft.jasperserver.dsa.initialization.InitializationStrategy;
import com.jaspersoft.jasperserver.dsa.initialization.ManualInitializationStrategy;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("Choose way of configuration (file or manual) [f/m]:");

        Character in = ConsoleUtil.readChar();

        InitializationStrategy strategy = null;
        switch (in) {
            case 'f': strategy = new FileInitializationStrategy(); break;
            case 'm': strategy = new ManualInitializationStrategy();break;
            default: break;
        }

        AppConfiguration configuration = InitHelper.initConfiguration(strategy.initConfiguration());

    }

}
