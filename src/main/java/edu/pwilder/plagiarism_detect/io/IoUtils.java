package edu.pwilder.plagiarism_detect.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Poor mans Apache IOUtils
 */
public class IoUtils {
    private static final Logger LOGGER = Logger.getLogger(IoUtils.class.getName());

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException e) {
            LOGGER.log(Level.FINE, "Could not close", e);
        }
    }
}
