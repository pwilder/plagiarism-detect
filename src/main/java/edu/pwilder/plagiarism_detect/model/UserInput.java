package edu.pwilder.plagiarism_detect.model;

import java.io.InputStream;

/**
 * The User input is the stepping stone from the commandline args to useful
 * constructs. We've limited ourselves to generic constructs like InputStreams
 * because we don't want to tie ourselves to anything implementation specific
 * yet.
 *
 * Potentially worth mentioning that UserInput just carries the InputStreams
 * it's expected that the user of UserInput should close the streams when done
 * reading them.
 */
public class UserInput {
    private final InputStream synonymInputStream;
    private final InputStream firstStream;
    private final InputStream secondStream;
    private final int tupleSize;

    public UserInput(InputStream synonymInputStream, InputStream firstStream, InputStream secondStream, int tupleSize) {
        super();
        this.synonymInputStream = synonymInputStream;
        this.firstStream = firstStream;
        this.secondStream = secondStream;
        this.tupleSize = tupleSize;
    }

    public InputStream getSynonymInputStream() {
        return synonymInputStream;
    }

    public InputStream getFirstStream() {
        return firstStream;
    }

    public InputStream getSecondStream() {
        return secondStream;
    }

    public int getTupleSize() {
        return tupleSize;
    }

}
