package org.uwdl.scenario;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.sspl.sax.SaxContentHandler;
import org.sspl.sax.SaxUnParser;
import org.uwdl.entities.ScenarioEntitySet;
import org.uwdl.mapper.uWDLMapper;
import org.uwdl.parser.uWDLLexer;
import org.uwdl.parser.uWDLParser;
import org.uwdl.parser.uWDLUnparser;
import org.uwdl.parser.ast.UActivate;
import org.uwdl.parser.ast.UActivator;
import org.uwdl.parser.ast.UFlow;
import org.uwdl.parser.ast.UUWDL;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class ScenarioTest {
	static Hashtable<String, UFlow> flowSet = new Hashtable<String, UFlow>();
	
	public static void main(String[] args) throws Exception {
		UUWDL uWDL;
		String xmlFile = "__test.xml";
//		String xmlFile = "scenario5.xml";
//		String xmlFile = "CAWL.xml";
//		String xmlFile = "_paper.xml";
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
//		try {
//			validator.validate(source);
//			System.out.println(xmlFile + " is valid.\n");
			
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
				// 결과 확인
				SaxUnParser unParser = new SaxUnParser();
				uWDL = ((SaxContentHandler) contentHandler).getUWDL();
				uWDL.accept(unParser);
				
		        System.out.println("\n----------------- simulating start ------------------");
		        //Hashtable<String, UFlow> flowSet = new Hashtable<String, UFlow>();
		        uWDLMapper mapper = new uWDLMapper(); 
		        ScenarioEntitySet sensorData = ScenarioEntitySet.getInstance();
		        
		        UActivator[] activator = uWDL.getActivators();
		        UFlow[] flows = uWDL.getFlows();
		        
		        for(UFlow flow : flows)
		        	flowSet.put(flow.getName(), flow);
		        
//		        if(sensorData.getEntities(activator[0].getName()) != null)
//		        	System.out.println(activator[0].getName() + "  | " + activator[0].getCondition().getExpression());
//		        else
//		        	return ;
		        
//		        if (mapper.find(sensorData.getEntities(activator[0].getName()), activator[0].getCondition())) {
		        	System.out.print("activator condition : ");
		        	System.out.println("OK");
		        	
		        	UActivate[] activate = activator[0].getActivates();
		        	System.out.println(activate[0].getFlow() + " execute");
		        	
		        	for(UActivate uActivate : activator[0].getActivates()) {
			        	UFlow flow = flowSet.get(uActivate.getFlow());
			        	ScenarioFlow scenarioFlow = new ScenarioFlow(flow, mapper);
			        	scenarioFlow.start();
		        	}
//		        }
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
//		} catch (SAXException ex) {
//			System.out.println(xmlFile + " is not valid because ");
//			System.out.println(ex.getMessage());
//		}
	}
}



// 예전꺼
//InputStream in = new FileInputStream("scenario5.xml");
//
//uWDLLexer lexer = new uWDLLexer(in);
//uWDLParser parser = new uWDLParser(lexer);
//UUWDL uWDL = parser.parse();
//
//uWDLUnparser unparser = new uWDLUnparser(System.out);
//uWDL.accept(unparser);