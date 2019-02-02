package edu.pwilder.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class FullReader {
	public String readAsString(InputStream inputStream) throws IOException {
		Objects.requireNonNull(inputStream);
		StringBuilder sb = new StringBuilder();
		int readByte = -1;
		while ((readByte = inputStream.read()) != -1) {
			sb.append((char)readByte);
		}
		return sb.toString();
	}
}
