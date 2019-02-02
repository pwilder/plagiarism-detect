package edu.pwilder.plagiarism_detect;

import static java.lang.String.format;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.pwilder.plagiarism_detect.strategies.DetectionStrategy;
import edu.pwilder.plagiarism_detect.usage_printer.UsagePrinter;

public class Main {

    /*
     * In a real application there would be obvious benefits to fully supporting
     * configuration by config file.
     */
    private static final Level APPLICATION_LOG_LEVEL = Level.INFO;
    static {
        final Logger globalParentLogger = Logger.getGlobal().getParent();
        globalParentLogger.setLevel(APPLICATION_LOG_LEVEL);
        globalParentLogger.getHandlers()[0].setLevel(APPLICATION_LOG_LEVEL);
    }

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {

        final PlagarismDetectorFactory factory = new PlagarismDetectorFactory(new ArgParser());
        final PlagarismDetector pd = factory.buildPlagiarismDetector(DetectionStrategy.QUADRATIC);
        try {
            final double results = pd.process(args);
            System.out.println(format("%.2f%%", results));
        } catch (final IllegalArgumentException ex) {
            LOGGER.log(Level.SEVERE, "Problem processing given input", ex);
            new UsagePrinter().printUsage();
        }
    }
}
