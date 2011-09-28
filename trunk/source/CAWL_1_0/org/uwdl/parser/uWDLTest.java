package org.uwdl.parser;

import java.io.FileInputStream;
import java.io.InputStream;

import org.uwdl.parser.ast.UUWDL;

public class uWDLTest {
    public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream("final.xml");
        
        uWDLLexer lexer = new uWDLLexer(in);		// <?xml 첫 라인만 파싱하여 charset을 구함,
        											// 구한 charset으로 인코딩하여 InputStreamReader 을 생성함  
        uWDLParser parser = new uWDLParser(lexer);	//
        UUWDL uWDL = parser.parse();
       
        uWDLUnparser unparser = new uWDLUnparser(System.out);
        uWDL.accept(unparser);
    }
}