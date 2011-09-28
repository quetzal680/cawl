package org.sspl.parser.xml.prolog;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.sspl.parser.InvalidTokenException;
import org.sspl.parser.SharedInputStream;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.Close;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.Encoding;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.Eq;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.Open;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.Value;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.Version;
import static org.sspl.parser.xml.prolog.XMLPrologToken.Type.WhiteSpace;

public class XMLPrologParser {
    private final XMLPrologLexer lexer;
    private XMLPrologToken token;
    
    private XMLPrologParser(XMLPrologLexer lexer) {
        this.lexer = lexer;
    }
    
    //��ū �ϳ� �о�ͼ� token�� ����
    private void advance() throws IOException, InvalidTokenException {
        token = lexer.next();
    }
    
    // <?xml 로 시작하는 맨 처음 라인에서만 수행되는 함수
    private Charset parse() throws IOException, InvalidTokenException {
        Charset charset = Charset.defaultCharset();		// UTF-8 로 설정
        advance();	// 1바이트 읽음
        if (token.getType() == Open) {
            advance();
            if (token.getType() == WhiteSpace) {
                advance();
                if (token.getType() == Version) {
                    advance();
                    if (token.getType() == WhiteSpace) {  // optional
                        advance();
                    }
                    if (token.getType() == Eq) {
                        advance();
                        if (token.getType() == WhiteSpace) {  // optional
                            advance();
                        }
                        if (token.getType() == Value && token.getText().equals("\"1.0\"")) {
                            advance();
                            if (token.getType() == WhiteSpace) {  // optional
                                advance();
                            }
                            if (token.getType() == Encoding) {  // optional
                                advance();
                                if (token.getType() == WhiteSpace) {  // optional
                                    advance();
                                }
                                if (token.getType() == Eq) {
                                    advance();
                                    if (token.getType() == WhiteSpace) {  // optional
                                        advance();
                                    }
                                    if (token.getType() == Value) {
                                        int end = token.getText().length();
                                        String name = token.getText().substring(1, end - 1);                                                
                                        charset = Charset.forName(name);  //???????????????????????????????????
                                        advance();
                                    }
                                }
                            }
                            if (token.getType() == Close) {
                                return charset;
                            }
                        }
                    }
                }
            }
        }
        exception();
        return null;
    }
    
    // 처음에 무조건 <?xml 이라는 문자가 온다고 생각하고 코딩함 예외란 없음
    public static Charset parse(SharedInputStream inputStream) throws IOException, InvalidTokenException {
        inputStream.mark(10);				// 입력 스트림 위치를 표시 reset으로 설정해 놓은 위치로 돌아 갈 수 있음 
        // 첫 라인인 <?xml 에서만 수행되는 조건
        if (inputStream.read() == '<') {
            if (inputStream.read() == '?') {
                if (inputStream.read() == 'x') {
                    if (inputStream.read() == 'm') {
                        if (inputStream.read() == 'l') {
                            inputStream.reset();	// <?xml 까지 읽고 입력 스트림의 처음으로 위치 이동
                            XMLPrologLexer lexer = new XMLPrologLexer(inputStream);		// 1 바이트 읽음
                            XMLPrologParser parser = new XMLPrologParser(lexer);		// XMLPrologParser 생성
                            return parser.parse();	// 첫 줄에 encoding이 있는지 파악하여 charset을 리턴함
                        }
                    }
                }
            }
        }
        
        // 두번째 라인부터 수행 됨
        inputStream.reset();
        return Charset.defaultCharset();
    }
    
    private void exception() throws InvalidTokenException {
        throw new InvalidTokenException(lexer.getLine(), lexer.getColumn(), "error");
    }

    public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream("uWDL Example.xml");
        Charset charset = parse(new SharedInputStream(in));
        System.out.println(charset);
//        ISharedReader reader = new SharedReader(new InputStreamReader(in));
//        XMLPrologLexer lexer = new XMLPrologLexer(reader);
//        XMLPrologParser parser = new XMLPrologParser(lexer);
//        System.out.println(parser.parse());
    }
}
