package edu.pwilder.plagiarism_detect.io;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * There is considerable overlap here with the WordTokenizer and some
 * consolidation could probably be done for better adherence to DRY.
 */
public class LineTokenizer implements Iterator<List<String>> {
    private static final Logger LOGGER = Logger.getLogger(WordTokenizer.class.getName());
    private final StreamTokenizer tokenizer;
    private final Reader reader;
    private List<String> nextLine;

    public LineTokenizer(InputStream is) {
        this(new InputStreamReader(is));
    }

    public LineTokenizer(Reader reader) {
        this.reader = reader;
        this.tokenizer = toStreamTokenizer(reader);
        seekNextLine();
    }

    private void seekNextLine() {
        int nextToken;
        try {
            nextLine = new LinkedList<String>();
            while ((nextToken = tokenizer.nextToken()) != StreamTokenizer.TT_EOF
                    && nextToken != StreamTokenizer.TT_EOL) {
                switch (nextToken) {
                    case StreamTokenizer.TT_WORD:
                        nextLine.add(tokenizer.sval);
                        break;
                    case StreamTokenizer.TT_NUMBER:
                        LOGGER.warning(format("Not expecting a number: %f", tokenizer.nval));
                        // fall through
                    default:
                        break;
                }
            }

            if (nextToken == StreamTokenizer.TT_EOF && nextLine.size() == 0) {
                IoUtils.closeQuietly(reader);
                nextLine = null;
            }

        } catch (final IOException e) {
            IoUtils.closeQuietly(reader);
            nextLine = null;
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
        return nextLine != null;
    }

    @Override
    public List<String> next() {
        if (!hasNext()) {
            return nextLine;
        }
        final List<String> returnList = nextLine;
        seekNextLine();
        return returnList;
    }
}
