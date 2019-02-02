package edu.pwilder;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.pwilder.model.UserInput;

public class PlagiarismDetector {
    private final ArgParser argParser;

    public PlagiarismDetector(ArgParser argParser) {
        this.argParser = argParser;
    }

    public String process(String[] args) throws IOException {
        final UserInput userInput = argParser.parseArgs(args);
        if (userInput == null) {
            return null;
        }
        final WordMatcher wordMatcher = userInput.getWordMatcher();
        final TupleParser tupleParser = new TupleParser(userInput.getFirstStream(), userInput.getTupleSize());
        final Set<Tuple> tupleSet = toTupleSet(userInput.getSecondStream(), userInput.getTupleSize());
        int duplicatedTupleCount = 0;
        int totalTupleCount = 0;
        final List<Tuple> seenTuples = new ArrayList<Tuple>();
        final TupleMatcher tupleMatcher = new TupleMatcher(wordMatcher);
        while (tupleParser.hasNext()) {
            totalTupleCount++;
            final Tuple parsedTuple = tupleParser.next();
            seenTuples.add(parsedTuple);
            for (final Tuple currentSetTuple : tupleSet) {
                if (tupleMatcher.doMatch(parsedTuple, currentSetTuple)) {
                    duplicatedTupleCount++;
                    break;
                }
            }
        }

        final double duplicatedPercent = (duplicatedTupleCount / (double) totalTupleCount) * 100;

        return format("%1.2f%s", duplicatedPercent, "%");
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
