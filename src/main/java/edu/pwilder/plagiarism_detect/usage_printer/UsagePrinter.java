package edu.pwilder.plagiarism_detect.usage_printer;

import static java.lang.String.format;

import java.io.FileNotFoundException;
import java.io.IOException;

import edu.pwilder.plagiarism_detect.io.FullReader;

public class UsagePrinter {
    private static final String USAGE_FILE = "usage.txt";
    private final String usage;

    public UsagePrinter() {
        try {
            usage = new FullReader().readAsString(UsagePrinter.class.getResourceAsStream(USAGE_FILE));
        } catch (final FileNotFoundException e) {
            throw new IllegalStateException(format("File <%s> was not found", USAGE_FILE), e);
        } catch (final IOException e) {
            throw new IllegalStateException(format("File <%s> could not be read", USAGE_FILE), e);
        }
    }

    public void printUsage() {
        System.out.println(usage);
    }
}
