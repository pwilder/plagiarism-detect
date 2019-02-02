package edu.pwilder.plagiarism_detect.io;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Iterator;
import java.util.logging.Logger;

public class WordTokenizer implements Iterator<String> {
    private static final Logger LOGGER = Logger.getLogger(WordTokenizer.class.getName());
    private final StreamTokenizer tokenizer;
    private String nextWord;
    private final Reader reader;

    public WordTokenizer(InputStream is) {
        this(new InputStreamReader(is));
    }

    public WordTokenizer(Reader reader) {
        this.reader = reader;
        this.tokenizer = toStreamTokenizer(reader);
        seekNextWord();
    }

    private void seekNextWord() {
        int nextToken;
        try {
            while ((nextToken = tokenizer.nextToken()) != StreamTokenizer.TT_EOF) {
                switch (nextToken) {
                    case StreamTokenizer.TT_WORD:
                        nextWord = tokenizer.sval;
                        return;
                    case StreamTokenizer.TT_NUMBER:
                        LOGGER.warning(format("Not expecting a number: %f", tokenizer.nval));
                        // fallthrough
                    default:
                        break;
                }
            }
            nextWord = null;
        } catch (final IOException e) {
            nextWord = null;
            IoUtils.closeQuietly(reader);
            throw new IllegalStateException("Unable to parse input stream", e);
        }

    }

    private StreamTokenizer toStreamTokenizer(Reader reader) {
        final StreamTokenizer streamTokenizer = new StreamTokenizer(reader);
        ignoreNumbers(streamTokenizer);
        return streamTokenizer;
    }

    private void ignoreNumbers(StreamTokenizer streamTokenizer) {
        streamTokenizer.ordinaryChars('0', '9');
        streamTokenizer.ordinaryChar('.');
        streamTokenizer.ordinaryChar('-');

        streamTokenizer.wordChars('0', '9');
        streamTokenizer.wordChars('.', '.');
        streamTokenizer.wordChars('-', '-');
    }

    @Override
    public boolean hasNext() {
        return nextWord != null;
    }

    @Override
    public String next() {
        final String returnWord = nextWord;
        seekNextWord();
        return returnWord;
    }
}
