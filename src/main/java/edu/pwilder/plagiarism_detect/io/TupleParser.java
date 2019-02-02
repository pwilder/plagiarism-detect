package edu.pwilder.plagiarism_detect.io;

import static java.lang.String.format;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;

import edu.pwilder.plagiarism_detect.model.Tuple;

public class TupleParser implements Iterator<Tuple> {
    private final Queue<String> stringQueue;
    private final WordTokenizer wordTokenizer;
    private final int tupleSize;

    public TupleParser(InputStream is, int tupleSize) {
        this(new InputStreamReader(is), tupleSize);
    }

    public TupleParser(Reader reader, int tupleSize) {
        if (tupleSize <= 0) {
            throw new IllegalArgumentException(format("invalid tuple size of %s, must be >= 1", tupleSize));
        }
        this.tupleSize = tupleSize;
        stringQueue = new ArrayDeque<String>(tupleSize);
        this.wordTokenizer = new WordTokenizer(reader);

        for (int i = 0; i < tupleSize - 1; i++) {
            if (!wordTokenizer.hasNext()) {
                break;
            }
            stringQueue.add(wordTokenizer.next());
        }
    }

    @Override
    public boolean hasNext() {
        return wordTokenizer.hasNext();
    }

    @Override
    public Tuple next() {
        if (!wordTokenizer.hasNext()) {
            return null;
        }

        if (stringQueue.size() == tupleSize) {
            stringQueue.poll();
        }
        stringQueue.add(wordTokenizer.next());

        return new Tuple(new ArrayList<String>(stringQueue));
    }

}
