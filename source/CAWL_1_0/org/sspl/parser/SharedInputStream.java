/*
 * Created on 2005. 1. 10
 */
package org.sspl.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Delios
 */
public class SharedInputStream extends InputStream {
    private final InputStream inputStream;
    private int c;
    private int line = 1, column = 1;
    
    public SharedInputStream(InputStream inputStream) {
        if (!inputStream.markSupported()) {
            this.inputStream = new BufferedInputStream(inputStream);
        }
        else {
            this.inputStream = inputStream;
        }
    }
    
    // overrides methods of InputStream
    
    @Override
    public void mark(int readAheadLimit) {
        inputStream.mark(readAheadLimit);
    }

    @Override
    public int read() throws IOException {
        int c = inputStream.read();			// 한 바이트를 읽음
        column++;							// column 1 증가
        return c;							// 읽은 바이트 리턴
    }

    @Override
    public void reset() throws IOException {
        inputStream.reset();
    }
    
    @Override
    public int available() throws IOException {
        return inputStream.available();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    @Override
    public boolean markSupported() {
        return inputStream.markSupported();
    }

    //
    
    public void advance() throws IOException {
        c = read();
    }

    public int current() {
        return c;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getColumn() {
        return column;
    }
    
    public void newline() {
        line++;
        column = 1;
    }
}
