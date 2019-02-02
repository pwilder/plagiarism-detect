package edu.pwilder;

import java.io.IOException;

import edu.pwilder.usage_printer.UsagePrinter;

public class Main {
    public static void main(String[] args) throws IOException {
        final PlagiarismDetector pd = new PlagiarismDetector(new ArgParser(new UsagePrinter()));
        final String results = pd.process(new String[] {});
        if (results == null) {
            return;
        }
        System.out.println(results);
    }
}
