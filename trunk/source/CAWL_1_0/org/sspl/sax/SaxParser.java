package org.sspl.sax;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.sspl.symbolTable.MTable;
import org.sspl.symbolTable.VTable;
import org.uwdl.parser.uWDLLexer;
import org.uwdl.parser.uWDLToken;
import org.uwdl.parser.uWDLUnparser;
import org.xml.sax.*;

public class SaxParser {
//	private final uWDLLexer lexer;
	private uWDLToken current; /* Token + lexem class */
	private String[] logic;
	private VTable top = null; // uWDL, activator, flow, node
	private MTable mTable = null;
	private String type;
	private String vName;

	public static void main(String args[]) throws Exception {
		String xmlFile = "CAWL.xml";
		String schemaFile = "/root/Dropbox/workspace/uwdl/1_uWDL2_0_pre_a/CAWL_v1.0.xsd";
		
		System.out.println("valid check");
		
		// 1. Lookup a factory for the W3C XML Schema language
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

		// 2. Compile the schema.
		// Here the schema is loaded from a java.io.File, but you could use
		// a java.net.URL or a javax.xml.transform.Source instead.
		File schemaLocation = new File(schemaFile);
		Schema schema = factory.newSchema(schemaLocation);

		// 3. Get a validator from the schema.
		Validator validator = schema.newValidator();

		// 4. Parse the document you want to check.
		Source source = new StreamSource(xmlFile);

		// 5. Check the document
		try {
			validator.validate(source);
			System.out.println(xmlFile + " is valid.\n");
			
			// 파서 공장 생성
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			
			// namespace 인식
			parserFactory.setNamespaceAware(true);
			
			try {
				// 파서 생성
				SAXParser saxParser = parserFactory.newSAXParser();
				XMLReader xmlReader = saxParser.getXMLReader();
				
				// Handler 객체 생성
				ContentHandler contentHandler = new SaxContentHandler();

				// contentHandler 객체 등록
				xmlReader.setContentHandler(contentHandler);

				// 파싱 지시
				xmlReader.parse(xmlFile);

				// 결과 확인
				SaxUnParser unParser = new SaxUnParser();
				((SaxContentHandler) contentHandler).getUWDL().accept(unParser);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
		} catch (SAXException ex) {
			System.out.println(xmlFile + " is not valid because ");
			System.out.println(ex.getMessage());
		}
	}
}
