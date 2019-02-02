package edu.pwilder;

import static java.lang.String.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import edu.pwilder.model.UserInput;
import edu.pwilder.usage_printer.UsagePrinter;

public class ArgParser {
    private final UsagePrinter usagePrinter;

    public ArgParser(UsagePrinter usagePrinter) {
        this.usagePrinter = usagePrinter;
    }

    public UserInput parseArgs(String[] args) throws IOException {
        if (args.length < 3) {
            usagePrinter.printUsage();
            return null;
        }
        final WordMatcher wordMatcher = new WordMatcher(toStream(args[0]));
        final InputStream firstInputStream = toStream(args[1]);
        final InputStream secondInputStream = toStream(args[2]);
        int tupleSize = 3;
        if (args.length >= 4) {
            tupleSize = Integer.parseInt(args[3]);
        }

        return new UserInput(wordMatcher, firstInputStream, secondInputStream, tupleSize);
    }

    private InputStream toStream(String filePath) throws FileNotFoundException {
        final File f = new File(filePath);
        if (!f.canRead()) {
            throw new IllegalStateException(format("Could not read file from path: %s", filePath));
        }

        return new FileInputStream(f);
    }

}
