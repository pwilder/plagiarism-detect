package edu.pwilder.model;

import java.io.InputStream;

import edu.pwilder.WordMatcher;

public class UserInput {
    private final WordMatcher wordMatcher;
    private final InputStream firstStream;
    private final InputStream secondStream;
    private final int tupleSize;

    public UserInput(WordMatcher wordMatcher, InputStream firstStream, InputStream secondStream, int tupleSize) {
        super();
        this.wordMatcher = wordMatcher;
        this.firstStream = firstStream;
        this.secondStream = secondStream;
        this.tupleSize = tupleSize;
    }

    public WordMatcher getWordMatcher() {
        return wordMatcher;
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
