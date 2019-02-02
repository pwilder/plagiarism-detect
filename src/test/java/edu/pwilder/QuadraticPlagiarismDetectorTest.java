package edu.pwilder;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.pwilder.plagiarism_detect.ArgParser;
import edu.pwilder.plagiarism_detect.strategies.quadratic.QuadraticPlagiarismDetector;

public class QuadraticPlagiarismDetectorTest {

    // We'll keep in case we want to tweak logs for testing.
    //    private static final Level APPLICATION_LOG_LEVEL = Level.FINE;
    //
    //    static {
    //        final Logger globalParentLogger = Logger.getGlobal().getParent();
    //        globalParentLogger.setLevel(APPLICATION_LOG_LEVEL);
    //        globalParentLogger.getHandlers()[0].setLevel(APPLICATION_LOG_LEVEL);
    //    }

    private static final String BASE_PATH = "src/test/resources/fixtures";
    private QuadraticPlagiarismDetector plagiarismDetector;

    @Before
    public void setup() {
        plagiarismDetector = new QuadraticPlagiarismDetector(new ArgParser());
    }

    @Test
    public void testProcess_example() {
        final double actual = plagiarismDetector.process(createArgs("example", 3));
        assertEquals(100f, actual, .1);
    }

    @Test
    public void testProcess_example2() {
        final double actual = plagiarismDetector.process(createArgs("example2", 3));
        assertEquals(50f, actual, .1);
    }

    @Test
    public void testProcess_singleWordExactMatch() {
        final double actual = plagiarismDetector.process(createArgs("singleWordExactMatch", 1));
        assertEquals(100f, actual, .1);
    }

    @Test
    public void testProcess_singleWordSynMatch() {
        final double actual = plagiarismDetector.process(createArgs("singleWordSynMatch", 1));
        assertEquals(100f, actual, .1);
    }

    @Test
    public void testProcess_transitiveSynonyms() {
        final double actual = plagiarismDetector.process(createArgs("transitiveSynonyms", 1));
        assertEquals(100f, actual, .1);
    }

    @Test
    public void testProcess_splitByNewline() {
        final double actual = plagiarismDetector.process(createArgs("splitByNewline", 2));
        assertEquals(100f, actual, .1);
    }

    @Test
    public void testProcess_multiSynMatch() {
        final double actual = plagiarismDetector.process(createArgs("multiSynMatch", 2));
        assertEquals(100f, actual, .1);
    }

    @Test
    public void testProcess_noMatch() {
        final double actual = plagiarismDetector.process(createArgs("noMatch", 2));
        assertEquals(0f, actual, .1);
    }

    @Test
    public void testProcess_testNumbers() {
        final double actual = plagiarismDetector.process(createArgs("numbers", 2));
        assertEquals(100f, actual, .1);
    }

    /**
     * Interestingly this works because we are only checking that a tuple from file
     * 1 occurs in file 2 not where in the document it occurs.
     */
    @Test
    public void testProcess_jumbled() {
        final double actual = plagiarismDetector.process(createArgs("jumbled", 2));
        assertEquals(100f, actual, .1);
    }

    /**
     * Isolated punctuation is not picked up by the StreamTokenizer by default and
     * I'm OK with that.
     */
    @Test
    public void testProcess_punctuation() {
        final double actual = plagiarismDetector.process(createArgs("punctuation", 2));
        assertEquals(0f, actual, .1);
    }

    /**
     * As mentioned above the stream tokenizer doesn't recognize punctuation by
     * default which means "word!" and "word?" are equivalent. I am also OK with
     * this.
     */
    @Test
    public void testProcess_wordsWithPunc() {
        final double actual = plagiarismDetector.process(createArgs("wordsWithPunc", 1));
        assertEquals(100f, actual, .1);
    }

    /**
     * Another side effect of the of the punctuation behavior is that punctuation
     * effectively becomes another form of white space so "a, b" and "a? b" are
     * considered equivalent. Again OK with this.
     */
    @Test
    public void testProcess_wordsSeparatedByPunc() {
        final double actual = plagiarismDetector.process(createArgs("wordsSeparatedByPunc", 2));
        assertEquals(100f, actual, .1);
    }

    /**
     * Regrettably there is special handling for the '.' character. This is because
     * we want to treat "2.0" all as one word rather than "2 0". The upshot of this
     * is that "a." and "a?" are not equivalent. Not ideal but probably tolerable.
     */
    @Test
    public void testProcess_periodHandling() {
        final double actual = plagiarismDetector.process(createArgs("periodHandling", 1));
        assertEquals(0f, actual, .1);
    }

    /**
     * We are looking at ~ 450 millisecond processing for two ~50 k files, including
     * vm startup.
     */
    @Test
    public void testProcess_ipsumTest() {
        final double actual = plagiarismDetector.process(createArgs("ipsumTest", 8));
        assertEquals(100f, actual, .1);
    }

    /**
     * random series of hits and misses.
     */
    @Test
    public void testProcess_misc() {
        final double actual = plagiarismDetector.process(createArgs("misc", 2));
        assertEquals(40, actual, .1);
    }

    private String[] createArgs(String folder, int tupleSize) {
        final String[] returnArray = new String[4];
        returnArray[0] = format("%s/%s/%s", BASE_PATH, folder, "synGroups.txt");
        returnArray[1] = format("%s/%s/%s", BASE_PATH, folder, "file1.txt");
        returnArray[2] = format("%s/%s/%s", BASE_PATH, folder, "file2.txt");
        returnArray[3] = Integer.toString(tupleSize);
        return returnArray;
    }
}
