package edu.pwilder.plagiarism_detect;

import static java.lang.String.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Logger;

import edu.pwilder.plagiarism_detect.model.UserInput;

/**
 * Function of the ArgParser is to verify the correct number of arguments and
 * that the relevant inputs exist.
 */
public class ArgParser {

    private static final Logger LOGGER = Logger.getLogger(ArgParser.class.getName());

    public ArgParser() {
    }

    public UserInput parseArgs(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Must have at least 3 arguments");
        }

        LOGGER.fine(format("Args to parse: %s", Arrays.asList(args).toString()));

        final InputStream synInputStream = toStream(args[0]);
        final InputStream firstInputStream = toStream(args[1]);
        final InputStream secondInputStream = toStream(args[2]);

        // This default value could be a good target for a config property.
        int tupleSize = 3;
        if (args.length >= 4) {
            tupleSize = Integer.parseInt(args[3]);
        }

        return new UserInput(synInputStream, firstInputStream, secondInputStream, tupleSize);
    }

    private InputStream toStream(String filePath) {
        final File f = new File(filePath);
        if (!f.canRead()) {
            throw new IllegalArgumentException(format("Could not read file from path: %s", filePath));
        }

        try {
            return new FileInputStream(f);
        } catch (final FileNotFoundException e) {
            // Is this even possible if I check canRead first?
            throw new IllegalArgumentException(format("File not found for path: %s", filePath));
        }
    }

}
