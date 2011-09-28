package org.sspl.parser.xml.prolog;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.sspl.parser.InvalidTokenException;
import org.sspl.parser.SharedInputStream;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.Close;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.EOF;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.Encoding;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.Eq;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.Open;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.Value;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.Version;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.WhiteSpace;

public class XMLPrologLexer {
    private final SharedInputStream inputStream;
    
    public XMLPrologLexer(SharedInputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        this.inputStream.advance();		// inputStream.read() 한 바이트를 읽어서 SharedInputStream안에 c에 읽은 바이트를 넣음
        								// column 1증가 시킴
    }
    
    public int getColumn() {
        return inputStream.getColumn();
    }

    public int getLine() {
        return inputStream.getLine();
    }

    public void newline() {
        inputStream.newline();
    }
    
    private void advance() throws IOException {
        inputStream.advance();
    }
    
    private int current() {
        return inputStream.current();
    }
    
    private XMLPrologToken makeToken(XMLPrologToken.Type type, String text) {
        return new XMLPrologToken(type, text);
    }
    
    // advance() ���� �ϳ��� �о��
    public XMLPrologToken next() throws IOException, InvalidTokenException {
        switch (current()) {
        case -1:
            return makeToken(EOF, "<EOF");
            
        case '<':
            advance();
            if (current() == '?') {
                advance();
                if (current() == 'x') {
                    advance();
                    if (current() == 'm') {
                        advance();
                        if (current() == 'l') {
                            advance();
                            return makeToken(Open, "<?xml");
                        }
                    }
                }
            }
            exception();
            break;
            
        case '=':
            advance();
            return new XMLPrologToken(Eq, "=");
            
        case 'v':
            advance();
            if (current() == 'e') {
                advance();
                if (current() == 'r') {
                    advance();
                    if (current() == 's') {
                        advance();
                        if (current() == 'i') {
                            advance();
                            if (current() == 'o') {
                                advance();
                                if (current() == 'n') {
                                    advance();
                                    return makeToken(Version, "version");
                                }
                            }
                        }
                    }
                }
            }        
            exception();
            break;
            
        case 'e':
            advance();
            if (current() == 'n') {
                advance();
                if (current() == 'c') {
                    advance();
                    if (current() == 'o') {
                        advance();
                        if (current() == 'd') {
                            advance();
                            if (current() == 'i') {
                                advance();
                                if (current() == 'n') {
                                    advance();
                                    if (current() == 'g') {
                                        advance();
                                        return makeToken(Encoding, "encoding");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            exception();
            break;
            
        case '\"':
        case '\'':
            int heading = current();
            StringBuffer b = new StringBuffer();
            b.append((char)heading);
            advance();
            while (current() != heading) {
                b.append((char)current());
                advance();                
            }
            b.append((char)heading);
            advance();
            return makeToken(Value, b.toString());
            
        case '?':
            advance();
            if (current() == '>') {
                advance();
                return makeToken(Close, "?>");
            }
            break;
        }
        
        if (isWhiteSpace((char)current())) {
            StringBuffer b = new StringBuffer();
            b.append((char)current());
            advance();
            while (isWhiteSpace((char)current())) {
                b.append((char)current());
                advance();
            }
            return makeToken(WhiteSpace, b.toString());
        }
        exception();
        return null;
    }

    private static boolean isWhiteSpace(char c) {
        switch (c) {
        case 0x09:
        case 0x0a:
        case 0x0d:
        case 0x20:
            return true;
        }
        return false;
    }

    private void exception() throws InvalidTokenException {
        throw new InvalidTokenException(inputStream.getLine(), inputStream.getColumn(), "invalid");
    }
    
    public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream("uWDL Example.xml");
        SharedInputStream inputStream = new SharedInputStream(in);
        XMLPrologLexer lexer = new XMLPrologLexer(inputStream);
        XMLPrologToken token;
        while ((token = lexer.next()).getType() != Close) {
            System.out.println(token);
        }
        System.out.println(token);
    }
}
