package edu.pwilder;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class WordMatcher {
    private static final Logger LOGGER = Logger.getLogger(WordMatcher.class.getName());
    private final Map<String, Integer> synMap;

    public WordMatcher(InputStream is) {
        requireNonNull(is);
        synMap = toSynMap(is);
    }

    public boolean doMatch(String wordA, String wordB) {
        final Integer valA = synMap.get(wordA);
        final Integer valB = synMap.get(wordB);
        if (allNotNull(valA, valB)) {
            return valA.equals(valB);
        }
        return Objects.equals(wordA, wordB);
    }

    private boolean allNotNull(Object... objects) {
        for (final Object current : objects) {
            if (current == null) {
                return false;
            }
        }
        return true;
    }

    private Map<String, Integer> toSynMap(InputStream is) {
        final BufferedReader r = new BufferedReader(new InputStreamReader(is));
        String currentLine = null;

        // We use integers for synonym comparisons because it is cheaper than String
        // compares.
        final Map<String, Integer> synonymMap = new HashMap<String, Integer>();
        int synGroupCounter = 0;
        int synVal = -1;
        try {
            while ((currentLine = r.readLine()) != null) {
                synVal = synGroupCounter;
                final String[] words = currentLine.trim().split("\\s+");
                if (words.length <= 1) {
                    // Blank lines are ignored for obvious reasons and single word lines are ignored
                    // because they are not synonyms of anything and deserve no special treatment.
                    LOGGER.warning(format("Ignoring %s with a word count of %d", currentLine, words.length));
                    continue;
                }

                if (synonymMap.containsKey(words[0])) {
                    synVal = synGroupCounter;
                } else {
                    synGroupCounter++;
                }

                for (final String currentWord : words) {
                    synonymMap.put(currentWord, synVal);
                }
            }
        } catch (final IOException e) {
            throw new IllegalStateException("Unable to process input stream", e);
        }

        return synonymMap;
    }
}
