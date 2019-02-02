package edu.pwilder.plagiarism_detect;

import static java.lang.String.format;

import edu.pwilder.plagiarism_detect.strategies.DetectionStrategy;
import edu.pwilder.plagiarism_detect.strategies.quadratic.QuadraticPlagiarismDetector;

/**
 * In the absence of injection we can use factories to leave our code more
 * loosely coupled.
 */
public class PlagarismDetectorFactory {
    private final ArgParser argParser;

    public PlagarismDetectorFactory(ArgParser argParser) {
        this.argParser = argParser;
    }

    public PlagarismDetector buildPlagiarismDetector(DetectionStrategy detectionStrategy) {
        switch (detectionStrategy) {
            case QUADRATIC:
                return new QuadraticPlagiarismDetector(argParser);
            default:
                throw new IllegalArgumentException(format("Unrecognized strategy: %s", detectionStrategy));
        }
    }
}
