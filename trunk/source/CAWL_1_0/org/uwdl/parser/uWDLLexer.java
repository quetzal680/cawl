/*
 * Created on 2005. 1. 3
 *
 * Lexical Analyzer for uWDL
 */
package org.uwdl.parser;

import org.sspl.parser.InvalidTokenException;
import org.sspl.parser.SharedInputStream;
import org.sspl.parser.xml.prolog.XMLPrologParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author Delios
 */
public class uWDLLexer implements uWDLTokenTypes {
    private enum State { Normal, Attributes };	

    private SharedInputStream inputStream;		// xml 파일을 읽음
	private InputStreamReader reader;			// 파일에서 읽은 것을 encoding 한 inputStream 
	private int c = (int)' ';					// 공백 문자
    private State state = State.Normal;			// attribute가 있는지 여부를 판단 
	
	public uWDLLexer(InputStream inputStream) throws UnsupportedEncodingException, IOException, InvalidTokenException {
        this.inputStream = new SharedInputStream(inputStream);
        //xml �������� �˻� �ϴ� �ڵ�???? Charset charset = XMLPrologParser.parse(this.inputStream);
        Charset charset = XMLPrologParser.parse(this.inputStream);
		this.reader = new InputStreamReader(this.inputStream, charset);
	}
    
	private void advance() throws IOException {
		c = reader.read();
	}
	
	private uWDLToken makeToken(Token type) {
		return new uWDLToken(type, type.getName());
	}
	
	private uWDLToken makeToken(Token type, String text) {
		return new uWDLToken(type, text);
	}
	
	public int getCurrentLine() {
		return inputStream.getLine();
	}
	
	public int getCurrentColumn() {
		return inputStream.getColumn();
	}
	
	private void exception() throws InvalidTokenException {
		throw new InvalidTokenException(inputStream.getLine(), inputStream.getColumn(), "token");
	}
    
    private void whitespaces() throws IOException {
        while (Character.isWhitespace(c)) {
            if (c == '\n') inputStream.newline();	// line 1증가 column 1로 변경
            advance();	// 1 바이트 읽음 
        }        
    }
    
	public uWDLToken next() throws IOException, InvalidTokenException {
		uWDLToken token = null;
		do {
            whitespaces();	// 공백이 아닌 경우 일 때까지 읽음
            
            if (c == -1) {  // end of file
                return makeToken(Token.EOF);
            }
            else if (state == State.Normal) {
            	// open
            	if (c == '<') {
    				advance();
    				if (c == '!') {  // comments,  no save
    					advance();
    					if (c == '-') {
    						advance();
    						if (c == '-') {
    							advance();
    							comment();
    						}
    						else exception();
    					}
    					else exception();
    				}
    				else if (c == '/') {
    					advance();
                        if (Character.isLetter(c)) {
                            StringBuffer b = new StringBuffer("</");
                            b.append((char)c);
                            advance();
                            while (Character.isLetterOrDigit(c)) {
                                b.append((char)c);
                                advance();
                            }
                            if (c == '>') {
                                b.append((char)c);
                                advance();
                            }
                            else exception();
                            Token t = Token.getInstance(b.toString().toLowerCase());
                            if (t == null) exception();
                            return makeToken(t);
                        }
                        else exception();
    				}
    				else if (Character.isLetter(c) || c == '_') {
    					StringBuffer b = new StringBuffer("<");
                        b.append((char)c);
    					advance();
    					while (Character.isLetterOrDigit(c)) {
    						b.append((char)c);
    						advance();
    					}
    					//�����±׸��� �а� �Ӽ��� �б����� state �� ���� 
                        state = State.Attributes;
                        Token t = Token.getInstance(b.toString().toLowerCase());
                        if (t == null) exception();
                        return makeToken(t);
    				}
    			}
            	else {
	                // pcdata
	                StringBuffer b = new StringBuffer();
	                do {
	                    b.append((char)c);
	                    advance();
	                }
	                while (c != '<');
	                return makeToken(Token.Pcdata, b.toString());
            	}
            }
            else {  // state == Attributes
                if (c == '?') { //???????????????????????????
                    advance();
                    if (c == '>') {
                        advance();
                        state = State.Normal;
                        return makeToken(Token.CloseXml);
                    }
                    else exception();
                }
                else if (c == '/') {
                    advance();
                    if (c == '>') {
                        advance();
                        state = State.Normal;
                        return makeToken(Token.EmptyClose);
                    }
                    else exception();
                }
                else if (c == '>') {
                    advance();
                    state = State.Normal;
                    return makeToken(Token.Close);
                }
                else if (c == '=') {
                    advance();
                    return makeToken(Token.Eq);
                }
                else if (c == '\"') {
                    StringBuffer b = new StringBuffer();
                    advance();  // ignore double quotation mark
                    while (c != '\"') {
                        b.append((char)c);
                        advance();
                    }
                    advance();  // ignore double quotation mark
                    return makeToken(Token.Value, b.toString());
                }
                else if (c == '\'') {
                    StringBuffer b = new StringBuffer();
                    advance();  // ignore single quotation mark
                    while (c != '\'') {
                        b.append((char)c);
                        advance();
                    }
                    advance();  // ignore single quotation mark
                    return makeToken(Token.Value, b.toString());
                }
                else if (Character.isLetter(c) || c == '_') {
                    StringBuffer b = new StringBuffer();
                    b.append((char)c);
                    advance();
                    while (Character.isLetterOrDigit(c) || c == ':') {
                        b.append((char)c);
                        advance();
                    }
                    return makeToken(Token.Attribute, b.toString());
                }
                else exception();
            }
		}
		while (token == null);
		return token;
	}
	    
	private uWDLToken comment() throws IOException, InvalidTokenException {
		do {
			if (c == '-') {
				advance();
				if (c == '-') {
					advance();
					if (c == '>') {
						advance();
						return null;  // ignore current token
					}
				}
			}
			advance();
		}
		while (c != -1);
        return null;
	}
    
/*    private static boolean isWhiteSpace(char c) {
        switch (c) {
        case 0x09:
        case 0x0a:
        case 0x0d:
        case 0x20:
            return true;
        }
        return false;
    }
    
    private static boolean isNameChar(char c) {
        
    }
    
    private static boolean isLetter(char c) {
        return 
    }
    
    private static boolean isDigit(char c) {
        switch (c) {
        case 0x0030:
        case 0x00
        }
        return false;
    }
    
    private static boolean isBaseChar(char c) {
        
    }
    
    private static boolean isIdeographic(char c) {
        
    }
    
    private static boolean isCombiningChar(char c) {
        
    }
    
    private static boolean isExtender(char c) {
        switch (c) {
        case 0x00b7:
        case 0x02d0:
        case 0x02d1:
        case 0x0387:
        case 0x0640:
        case 0x0e46:
        case 0x0ec6:
        case 0x3005:
        case 0x3031:
        case 0x3032:
        case 0x3033:
        case 0x3034:
        case 0x3035:
        case 0x309d:
        case 0x309e:
        case 0x30fc:
        case 0x30fd:
        case 0x30fe:
            return true;
        }
        return false;
    }*/
    
	public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream("uWDL Example.xml");
        
		uWDLLexer lexer = new uWDLLexer(in);
		uWDLToken token;
		while ((token = lexer.next()).getType() != Token.EOF) {
			System.out.println(token);
		}
        System.out.println(token);
	}
}
