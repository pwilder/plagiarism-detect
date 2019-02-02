package edu.pwilder.plagiarism_detect.strategies.quadratic;

import static java.lang.String.format;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import edu.pwilder.plagiarism_detect.ArgParser;
import edu.pwilder.plagiarism_detect.PlagarismDetector;
import edu.pwilder.plagiarism_detect.io.TupleParser;
import edu.pwilder.plagiarism_detect.model.Tuple;
import edu.pwilder.plagiarism_detect.model.UserInput;

/**
 * The meat of the application. Performance considerations:
 * Bad:
 *   * We are reading the entirety of the second file into memory. This is bad if its a 1G
 * log file.
 *   * We walk through (most of) the second file for every tuple in the
 * first file. Loop inside a loop, hence the quadratic.
 *
 * Good:
 *   * The first file is streaming in and we never hold more in memory than
 * necessary.
 *   * We have slight savings in processing the second file as we are
 * removing duplicate tuples from the set. This obviously jumbles the contents
 * of the file in memory but per the acceptance criteria that doesn't matter. It
 * also doesn't matter that it matches more than once only that it matches.
 *
 */
public class QuadraticPlagiarismDetector implements PlagarismDetector {
    private static final Logger LOGGER = Logger.getLogger(QuadraticPlagiarismDetector.class.getName());

    private final ArgParser argParser;

    public QuadraticPlagiarismDetector(ArgParser argParser) {
        this.argParser = argParser;
    }

    @Override
    public double process(String[] args) {
        final long startTime = System.currentTimeMillis();
        final UserInput userInput = argParser.parseArgs(args);
        final WordMatcher wordMatcher = new WordMatcher(userInput.getSynonymInputStream());
        final TupleParser tupleParser = new TupleParser(userInput.getFirstStream(), userInput.getTupleSize());
        final Set<Tuple> tupleSet = toTupleSet(userInput.getSecondStream(), userInput.getTupleSize());

        /*
         * Using suppliers for the logging means that we don't have to do the work to
         * create the log string unless we are at the appropriate log level which is
         * helpful for performance.
         *
         * Arguably we want to use suppliers for all log statements but the default
         * logging is INFO so info or below can just be processed normally.
         */
        LOGGER.fine(() -> format("Parsed tupleSet: %s", tupleSet));
        int duplicatedTupleCount = 0;
        int totalTupleCount = 0;
        final TupleMatcher tupleMatcher = new TupleMatcher(wordMatcher);

        while (tupleParser.hasNext()) {
            totalTupleCount++;
            final Tuple parsedTuple = tupleParser.next();
            LOGGER.finer(() -> format("Current file1 tuple: %s", parsedTuple));
            for (final Tuple currentSetTuple : tupleSet) {
                LOGGER.finer(() -> format("Current file2 tuple: %s", currentSetTuple));
                if (tupleMatcher.doMatch(parsedTuple, currentSetTuple)) {
                    LOGGER.finer("tuples match");
                    duplicatedTupleCount++;
                    break;
                }
            }
        }

        // If file 1 has 0 tuples of appropriate size is it plagiarism? I say no.
        if (totalTupleCount == 0) {
            return 0;
        }

        final double duplicatedPercent = (duplicatedTupleCount / (double) totalTupleCount) * 100;
        final long endTime = System.currentTimeMillis() - startTime;
        LOGGER.fine(() -> format("Process time millis: %d", endTime));

        return duplicatedPercent;
    }

    private Set<Tuple> toTupleSet(InputStream is, int tupleSize) {
        final TupleParser tupleParser = new TupleParser(is, tupleSize);
        final Set<Tuple> tupleSet = new HashSet<Tuple>();
        while (tupleParser.hasNext()) {
            tupleSet.add(tupleParser.next());
        }
        return tupleSet;
    }
}
