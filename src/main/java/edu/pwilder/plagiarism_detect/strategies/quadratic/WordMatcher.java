package edu.pwilder.plagiarism_detect.strategies.quadratic;

import static java.lang.String.format;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import edu.pwilder.plagiarism_detect.io.LineTokenizer;

public class WordMatcher {
    private static final Logger LOGGER = Logger.getLogger(WordMatcher.class.getName());
    private final Map<String, Integer> synMap;

    public WordMatcher(InputStream is) {
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

        /*
         * We are using a LineTokenizer here rather than a
         */
        final LineTokenizer lineTokenizer = new LineTokenizer(is);

        // We use integers for synonym comparisons because it is cheaper than String
        // compares.
        final Map<String, Integer> synonymMap = new HashMap<String, Integer>();
        int synGroupCounter = 0;
        int synVal = -1;

        while (lineTokenizer.hasNext()) {
            synVal = synGroupCounter;
            final List<String> currentList = lineTokenizer.next();
            if (currentList.size() <= 1) {
                // Blank lines are ignored for obvious reasons and single word lines are ignored
                // because they are not synonyms of anything and deserve no special treatment.
                LOGGER.warning(format("Ignoring %s with a word count of %d", currentList, currentList.size()));
                continue;
            }

            final Integer existingVal = getExistingVal(currentList, synonymMap);
            if (existingVal != null) {
                synVal = existingVal;
            } else {
                synGroupCounter++;
            }

            for (final String currentWord : currentList) {
                synonymMap.put(currentWord, synVal);
            }
        }

        return synonymMap;
    }

    private Integer getExistingVal(List<String> vals, Map<String, Integer> synMap) {
        for (final String currentVal : vals) {
            if (synMap.containsKey(currentVal)) {
                return synMap.get(currentVal);
            }
        }
        return null;
    }
}
