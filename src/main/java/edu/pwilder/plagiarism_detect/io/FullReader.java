package edu.pwilder.plagiarism_detect.io;

import java.io.IOException;
import java.io.InputStream;

public class FullReader {

    /**
     * Warning does close input stream.
     */
    public String readAsString(InputStream inputStream) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int readByte = -1;
        while ((readByte = inputStream.read()) != -1) {
            sb.append((char) readByte);
        }

        inputStream.close();
        return sb.toString();
    }
}
