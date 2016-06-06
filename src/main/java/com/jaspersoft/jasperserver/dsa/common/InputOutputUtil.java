package com.jaspersoft.jasperserver.dsa.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class InputOutputUtil {


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

    public static void saveStreamToFile(InputStream resultDataSetStream, String resultDir, String filename) {
        if (resultDataSetStream == null) {
            consoleLogger.warn("Result dataset is null");
            return;
        }
        if (resultDir == null || resultDir.isEmpty()) {
            consoleLogger.warn("Result directory name is not valid");
            return;
        }
        if (filename == null || filename.isEmpty()) {
            consoleLogger.warn("File name is not valid");
            return;
        }
        File resultDirAsFile = new File(resultDir);
        if (!resultDirAsFile.exists()) {
            resultDirAsFile.mkdirs();
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(resultDir + File.separator + filename);
            IOUtils.copy(resultDataSetStream, out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                resultDataSetStream.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
