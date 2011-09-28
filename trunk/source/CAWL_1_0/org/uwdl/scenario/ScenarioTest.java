package org.uwdl.scenario;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
//		InputStream in = new FileInputStream("scenario5.xml");
//        
//        uWDLLexer lexer = new uWDLLexer(in);
//        uWDLParser parser = new uWDLParser(lexer);
//        UUWDL uWDL = parser.parse();
//       
//        uWDLUnparser unparser = new uWDLUnparser(System.out);
//        uWDL.accept(unparser);
		
		UUWDL uWDL;
//		String xmlFile = "CAWL.xml";
//		String xmlFile = "__test.xml";
		String xmlFile = "scenario5.xml";
		
		// 파서 공장 생성
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		
		// namespace 인식
		parserFactory.setNamespaceAware(true);
		
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
        
        if(sensorData.getEntities(activator[0].getName()) != null)
        	System.out.println(activator[0].getName() + "  | " + activator[0].getCondition().getExpression());
        else
        	return ;
        
        if (mapper.find(sensorData.getEntities(activator[0].getName()), activator[0].getCondition())) {
        	System.out.print("activator condition : ");
        	System.out.print(".");
//        	Thread.sleep(1000);
        	System.out.print(".");
//        	Thread.sleep(1000);
        	System.out.println("OK");
        	UActivate[] activate = activator[0].getActivates();
        	System.out.println(activate[0].getFlow() + " execute");
        	
        	UFlow flow = flowSet.get(activate[0].getFlow());
        	ScenarioFlow scenarioFlow = new ScenarioFlow(flow, mapper);
        	scenarioFlow.execute();
        }
	}
}
