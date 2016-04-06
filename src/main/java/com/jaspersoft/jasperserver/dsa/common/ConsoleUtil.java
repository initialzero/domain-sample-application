package com.jaspersoft.jasperserver.dsa.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class ConsoleUtil {

    private static Logger consoleLogger = Logger.getLogger("consoleLogger");

    public static Character readChar(Character[] validValues) {
        Character in;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String stringIn;
        while (true) {
            try {
                stringIn = reader.readLine();
                in = stringIn.trim().toLowerCase().charAt(0);
                if (isValueValid(in, validValues)) {
                    break;
                }
                consoleLogger.warn("Value is incorrect, please, enter valid value:");
            } catch (IOException e) {
                consoleLogger.warn("Error of reading from console", e);
            }
        }
        return in;

    }

    private static boolean isValueValid(Character value, Character[] validValues) {
        for (Character validValue : validValues) {
            if (value == validValue) return true;
        }
        return false;
    }
}
