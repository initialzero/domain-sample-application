package com.jaspersoft.jasperserver.dsa.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class ConsoleUtil {

    public static Character readChar () {
        Character in = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String stringIn = null;
        try {
            stringIn = reader.readLine();
            in = stringIn.trim().toLowerCase().charAt(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
}
