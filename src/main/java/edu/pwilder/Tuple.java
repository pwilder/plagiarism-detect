package edu.pwilder;

import static java.lang.String.format;

import java.util.List;
import java.util.Objects;

public class Tuple {
    private final List<String> wordList;

    public Tuple(List<String> wordList) {
        this.wordList = wordList;
    }

    public List<String> getWordList() {
        return wordList;
    }

    public int size() {
        return wordList.size();
    }

    public String getWordAt(int index) {
        return wordList.get(index);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(wordList);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Tuple)) {
            return false;
        }

        return Objects.equals(wordList, ((Tuple) other).wordList);
    }

    @Override
    public String toString() {
        return format("Tuple %s", wordList);
    }
}
