package edu.pwilder;

public class TupleMatcher {
    private final WordMatcher wordMatcher;

    public TupleMatcher(WordMatcher wordMatcher) {
        this.wordMatcher = wordMatcher;
    }

    public boolean doMatch(Tuple tupleA, Tuple tupleB) {
        if (tupleA.size() != tupleB.size()) {
            return false;
        }

        for (int i = 0; i < tupleA.size(); i++) {
            final String wordA = tupleA.getWordAt(i);
            final String wordB = tupleB.getWordAt(i);
            if (!wordMatcher.doMatch(wordA, wordB)) {
                return false;
            }
        }
        return true;
    }
}
