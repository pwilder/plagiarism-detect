package edu.pwilder.plagiarism_detect.model;

import static java.lang.String.format;

import java.util.List;
import java.util.Objects;

/**
 * A wrapper around a wordList. Technically the List in isolation is more
 * powerful but some of the code becomes ugly with generic inside of generic.
 */
public class Tuple {
    private final List<String> wordList;

    public Tuple(List<String> wordList) {
        this.wordList = wordList;
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

        // Of interest The basic java provided list implements are considered equal
        // if they have the same elements. Admittedly this doesn't necessarily hold true
        // for more exotic list implementations.
        // TODO Provide more robust list equality.
        return Objects.equals(wordList, ((Tuple) other).wordList);
    }

    @Override
    public String toString() {
        return format("Tuple %s", wordList);
    }
}
