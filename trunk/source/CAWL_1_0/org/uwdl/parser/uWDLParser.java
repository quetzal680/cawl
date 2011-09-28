// 트리 생성
package org.uwdl.parser;

import java.io.IOException;

import org.sspl.parser.InvalidTokenException;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.symbolTable.Data;
import org.sspl.symbolTable.MTable;
import org.sspl.symbolTable.Message;
import org.sspl.symbolTable.VTable;
import org.uwdl.parser.ast.IConditionDescendant;
import org.uwdl.parser.ast.INodeDescendant;
import org.uwdl.parser.ast.UActivate;
import org.uwdl.parser.ast.UActivator;
import org.uwdl.parser.ast.UAndConstraint;
import org.uwdl.parser.ast.UBaseOntologies;
import org.uwdl.parser.ast.UCase;
import org.uwdl.parser.ast.UCondition;
import org.uwdl.parser.ast.UConstraint;
import org.uwdl.parser.ast.UContext;
import org.uwdl.parser.ast.UCopy;
import org.uwdl.parser.ast.UDeactivate;
import org.uwdl.parser.ast.UDocumentation;
import org.uwdl.parser.ast.UEvent;
import org.uwdl.parser.ast.UFlow;
import org.uwdl.parser.ast.UFrom;
import org.uwdl.parser.ast.UInitialize;
import org.uwdl.parser.ast.UInput;
import org.uwdl.parser.ast.UInvoke;
import org.uwdl.parser.ast.ULink;
import org.uwdl.parser.ast.ULocator;
import org.uwdl.parser.ast.UMessage;
import org.uwdl.parser.ast.UNode;
import org.uwdl.parser.ast.UNotConstraint;
import org.uwdl.parser.ast.UObject;
import org.uwdl.parser.ast.UOntology;
import org.uwdl.parser.ast.UOrConstraint;
import org.uwdl.parser.ast.UOutput;
import org.uwdl.parser.ast.UPart;
import org.uwdl.parser.ast.UPortType;
import org.uwdl.parser.ast.URule;
import org.uwdl.parser.ast.UServiceProvider;
import org.uwdl.parser.ast.UServiceProviderType;
import org.uwdl.parser.ast.USink;
import org.uwdl.parser.ast.USource;
import org.uwdl.parser.ast.USubject;
import org.uwdl.parser.ast.USynchronize;
import org.uwdl.parser.ast.UTo;
import org.uwdl.parser.ast.UUWDL;
import org.uwdl.parser.ast.UVariable;
import org.uwdl.parser.ast.UVerb;
import org.uwdl.parser.ast.UWait;

public class uWDLParser implements uWDLTokenTypes {
    private final uWDLLexer lexer;
    private uWDLToken current; /*Token + lexem class�� �����ϴ� �ӽ� �����*/
    private String[] logic;
    private VTable top = null; //uWDL, activator, flow, node���� ���
    private MTable mTable = null;
    private String type; // �ɺ����̺?�� Ű��ã�� ���� ... ���߿� ���� 
    private String vName; //���� �ʱ�ȭ�� ���
    
    public uWDLParser(uWDLLexer lexer) throws IOException, InvalidTokenException {
        this.lexer = lexer;
        this.logic = null;
        this.mTable = new MTable();
        advance();
    }
    
    private void advance() throws IOException, InvalidTokenException {
        current = lexer.next();
    }
    
    private Token type() {
        return current.getType();
    }
    
    private String text() {
        return current.getText();
    }
    
    // 파싱해서 attribute를 구함 ""안의 String 값을 리턴함 
    private final String attribute() throws IOException, InvalidTokenException, uWDLParseException {
        if (type() == Token.Eq) {
            advance();
            if (type() == Token.Value) {
            	final String value = text();
                advance();
                return value;
            }
            else throw new uWDLParseException();
        }
        else throw new uWDLParseException();
    }
    
    // hashmap에 Attribute에 name, value를 매칭하여 넣음
    private final XMLAttributeMap attributes() throws IOException, InvalidTokenException, uWDLParseException {
    	final XMLAttributeMap map = new XMLAttributeMap();
    	while (type() == Token.Attribute) {
    		final String name = text();
    		advance();
    		final String value = attribute();
    		map.put(name, value);  // ������ �̸��� ������ �Ӽ� ���� ���
    	}
    	return map;
    }
    
    private final String pcdata() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException{
    	Data data = null;
    	String text = text();
    	
    	if (type() == Token.Pcdata) {
    		if(text.charAt(0) == '?') {
    			//message variable
    			if(text.charAt(1) == '=') {
    				data = top.getData(text.substring(2));
    			} else {
    				//variable
    				data = top.getData(text.substring(1));
    				//System.out.println("pcdata()" + type + ":" + text.substring(1));
    			}
    			
    			if(data == null) 
    				throw new UnvalidVariableException();
    			
    			advance();
    			return data.getValue();
    		//pcdata
    		}else {
    			final String pcdata = text;
    			advance();
    			return pcdata;
    		}
    	}
    	throw new uWDLParseException();
    }
    
    // token 을 체크하는 역할을 하는 함수
    private void test(Token token) throws IOException, InvalidTokenException, uWDLParseException {
    	final int line = lexer.getCurrentLine();
    	final int column = lexer.getCurrentColumn();
    	if (type() == token) {
    		advance();
    	}
    	else throw new uWDLParseException("type :" + type() + " token :" + token.getName() + " - " + line + ":" + column + ", parse exception");
    }
    
    private String[] logic(String expression) {
    	String temp = expression.replace("and", ":and:").replace("or", ":or:").replace("not", ":not:");
    	return temp.split("[:]");
    }
    
    ////// --------------------------------------------------------------------------------------------------------
    ////// --------------------------------------------------------------------------------------------------------
    ////// --------------------------------------------------------------------------------------------------------

    // uwdl 문서를 파싱하여 uWDL 클래스를 생성해서 리턴함
    public UUWDL parse() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	return uWDL();
    }
    
    // uWDL 클래스를 생성하는 함수
    private UUWDL uWDL() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenUwdl);	// <uWDL 확인
        final XMLAttributeMap map = attributes();	// <uWDL>의 attribute를 hashmap으로 만듬 
        test(Token.Close);		// > 확인
        // uWDL 클래스 생성, uWDL 클래스에 생성자에서 map=attribute, <documentation>, <baseOntologies> 값을 설정함    
		final UUWDL uWDL = new UUWDL(map, documentation(), baseOntologies());
		
		mTable = new MTable(); 	//message table
		VTable saved = top;		// saved = top = null
		top = new VTable(top); 	//variable table

        while (type() == Token.OpenServiceProviderType) uWDL.addServiceProviderType(serviceProviderType());
        while (type() == Token.OpenServiceProvider) uWDL.addServiceProvider(serviceProvider());
        while (type() == Token.OpenMessage) uWDL.addMessage(message());
        while (type() == Token.OpenVariable) uWDL.addVariable(variable());
        while (type() == Token.OpenActivator) uWDL.addActivator(activator());
        while (type() == Token.OpenFlow) uWDL.addFlow(flow());
        
        top = saved;			// top = saved
        test(Token.CloseUwdl);	// </uWDL> 확인

        return uWDL;
    }

    private UDocumentation documentation() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenDocumentation);
    	test(Token.Close);
    	final String pcdata = pcdata();
    	
    	test(Token.CloseDocumentation);
        return new UDocumentation(pcdata);
    }
    
    private UBaseOntologies baseOntologies() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenBaseOntologies);
    	test(Token.Close);
    	final UBaseOntologies baseOntologies = new UBaseOntologies();
    	
    	while (type() == Token.OpenOntology) baseOntologies.addOntology(ontology());
    	
        test(Token.CloseBaseOntologies);
        return baseOntologies;
    }

    private UOntology ontology() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenOntology);
        final XMLAttributeMap map = attributes();
       
        test(Token.EmptyClose);
    	return new UOntology(map);
    }
    
    private UServiceProviderType serviceProviderType() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenServiceProviderType);
        final XMLAttributeMap map = attributes();
        test(Token.Close);
        final UServiceProviderType serviceProviderType = new UServiceProviderType(map);
        
        while (type() == Token.OpenPortType) serviceProviderType.addPortType(portType());
        
        test(Token.CloseServiceProviderType);
    	return serviceProviderType;
    }

    private UPortType portType() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenPortType);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	//operation element �� ����?
    	test(Token.ClosePortType);
		return new UPortType(map);
    }
    
    private UServiceProvider serviceProvider() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenServiceProvider);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	UServiceProvider serviceProvider = new UServiceProvider(map, locator());
    	test(Token.CloseServiceProvider);
    	return serviceProvider;
    }
    
    private ULocator locator() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenLocator);
    	final XMLAttributeMap map = attributes();
    	//test(Token.Close);
    	
    	test(Token.EmptyClose);
    	return new ULocator(map);
    }
    
    private UMessage message() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenMessage);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	
    	final UMessage message = new UMessage(map);
    	
    	while (type() == Token.OpenPart) message.addPart(part());
    	mTable.addMessageSet(map.require("name"));
    	test(Token.CloseMessage);
    	return message;
    }

    // <part 를 확인하고 파싱하여 attribute를 구해서 mTable에 type/name 형태로 값을 넣음  
    private UPart part() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenPart);
    	final XMLAttributeMap map = attributes();
    	mTable.addName(map.require("type") + "/" + map.require("name"));
    	//mTable.addName(map.require("name"));
    	test(Token.EmptyClose);
    	return new UPart(map);
    }
    
    private UVariable variable() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenVariable);
    	final XMLAttributeMap map = attributes();
    	final UVariable variable = new UVariable(map);
    	vName = map.require("name");
    	String vType = map.require("type");
    	if(mTable.exist(vType)) {
    		System.out.println("vName is " + vName + " and vType is " + vType);
    		top.put(vName, new Message(mTable.getNames(vType)));
    	}
    	else
    		top.put(vName, new Data(map.require("type"), vName));
    	
    	switch (type()) {
    		case Close :
    			test(Token.Close);
    			while (type() == Token.OpenInitialize) variable.addInitialize(initialize());
    			test(Token.CloseVariable);
    			break;
    		case EmptyClose : 
    			test(Token.EmptyClose);
    			break;
    		default :
    			throw new uWDLParseException("undefined tag");
    	}
    	return variable;
    }
    
    private UInitialize initialize() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenInitialize);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	final UFrom from = from();

    	//���� �ʱ�ȭ
    	Data data;
    	String value = null;
    	
    	//System.out.println("initialize attribute part : " + map.require("part"));
    	if(map.optional("part") != null) {
    		System.out.println("\n\t" + vName + "\n");
    		data = top.getData(vName + "/" + map.require("part"));
    	}
    	else
    		data = top.getData(vName);
    	
    	//�ƹ������ �� ¥������ �ݵ�� ����
    	if(data == null)
    		throw new UnvalidVariableException();
    	
    	if(from.getVariable() != null){
    		value = data.getValue();
    	}
    	else if(from.getPart() != null){
    		value = data.getValue();
    	}
    	else if(from.getExpression() != null){
    		value = from.getExpression();
    	}
    	
    	//System.out.println("initialize : " + value);
    	data.setValue(value);
    	
    	test(Token.CloseInitialize);
    	return new UInitialize(map, from);
    }
    
    private UFrom from() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenFrom);
    	final XMLAttributeMap map = attributes();
    	/*Data data = top.getData(vName);
    	
    	if(data == null)
    		throw new UnvalidVariableException();
    	
    	data.setValue(map.require("expression"));*/    	
    	test(Token.EmptyClose);
    	return new UFrom(map);
    }
    
    private UActivator activator() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenActivator);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	final UActivator activator = new UActivator(map); //error ���� ����..
    	
    	VTable saved = top;
		top = new VTable(top);
    	
    	while (type() == Token.OpenMessage) activator.addMessage(message());
    	while (type() == Token.OpenVariable) activator.addVariables(variable());
    	
    	activator.setCondition(condition());
    	
    	while (type() == Token.OpenActivate) activator.addActivate(activate());
    	while (type() == Token.OpenDeactivate) activator.addDeactivate(deactivate());
    	
    	top = saved;
    	test(Token.CloseActivator);
    	return activator;
    }
    
    private UCondition condition() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenCondition);
    	final XMLAttributeMap map = attributes();
    	logic = logic(map.require("expression"));
    	
    	test(Token.Close);
    	IConditionDescendant descendant = null;

    	switch (type()) {
    		case OpenContext:
    			descendant = context();
    			break;
    		case OpenCase:
    			descendant = case_();
    			break;
    		default:
    			throw new uWDLParseException();
    	}
    	
    	test(Token.CloseCondition);
    	return new UCondition(map, descendant);
    }
    
    private UContext context() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenContext);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	final UContext context = new UContext(map);
    	
    	while (type() == Token.OpenRule) context.addRule(rule());
    	
    	test(Token.CloseContext);
    	return context;
    }
    
    private URule rule() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenRule);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	final URule rule = new URule(map);
    	
    	while (type() == Token.OpenConstraint) {
    		for (int i=0; i<logic.length; i++) {
    			if (logic[i].equals("and")) {
    				rule.addConstraint(andConstraint());
    				i++;
    			}
    			else if (logic[i].equals("or")) {
    				rule.addConstraint(orConstraint());
    				i++;
    			}
    			else if (logic[i].equals("not")) {
    				rule.addConstraint(notConstraint());
    				i++;
    			}
    			else
    				rule.addConstraint(constraint());
    		}
    	}
    	
    	test(Token.CloseRule);
    	return rule;
    }
    
    // and or not method ����
    private UConstraint constraint() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenConstraint);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	USubject subject = subject();
    	UVerb verb = verb();
    	UObject object = object();
    	
    	test(Token.CloseConstraint);
    	return new UConstraint(map, subject, verb, object);
    }
     
    private UAndConstraint andConstraint() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenConstraint);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	USubject subject = subject();
    	UVerb verb = verb();
    	UObject object = object();
    	
    	test(Token.CloseConstraint);
    	return new UAndConstraint(map, subject, verb, object);
    }
    
    private UOrConstraint orConstraint() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenConstraint);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	USubject subject = subject();
    	UVerb verb = verb();
    	UObject object = object();
    	
    	test(Token.CloseConstraint);
    	return new UOrConstraint(map, subject, verb, object);
    }
    
    private UNotConstraint notConstraint() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenConstraint);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	USubject subject = subject();
    	UVerb verb = verb();
    	UObject object = object();
    	
    	test(Token.CloseConstraint);
    	return new UNotConstraint(map, subject, verb, object);
    }
    
    private USubject subject() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenSubject);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	type = map.require("type"); // �ҽ��� ��...
    	String pcdata = pcdata();
    	test(Token.CloseSubject);
    	return new USubject(map, pcdata);
    }
    
    private UVerb verb() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenVerb);
    	test(Token.Close);
    	String pcdata = pcdata();
    	
    	test(Token.CloseVerb);
    	return new UVerb(pcdata);
    }

    private UObject object() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenObject);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	type = map.require("type"); // �ҽ��� ��...
    	String pcdata = pcdata();
    	
    	test(Token.CloseObject);
    	return new UObject(map, pcdata);
    }
    
    private UCase case_() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenCase);
    	final XMLAttributeMap map = attributes();
    	UCase case_ = null;
    	
    	switch (type()) {
    	case Close :
    		test(Token.Close);
    		case_ = new UCase(map, event());
    		test(Token.CloseCase);
    		break;
    	case EmptyClose :
    		case_ = new UCase(map, null);
    		break;
    	default :
    		throw new uWDLParseException(type().getName());
    	}
    	
    	test(Token.EmptyClose);
    	return case_;
    }
    
    private UEvent event() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenEvent);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	
    	test(Token.CloseEvent);
    	return new UEvent(map);
    }
    
    private UActivate activate() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenActivate);
    	final XMLAttributeMap map = attributes();
    	
    	test(Token.EmptyClose);
    	return new UActivate(map);
    }
    
    private UDeactivate deactivate() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenDeactivate);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	
    	test(Token.CloseDeactivate);
    	return new UDeactivate(map);
    }
    
    private UFlow flow() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenFlow);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	final UFlow flow = new UFlow(map);
    	VTable saved = top;
		top = new VTable(top);
		
    	while (type() == Token.OpenMessage) flow.addMessage(message());
    	while (type() == Token.OpenVariable) flow.addVariable(variable());
    	flow.setSource(source());
    	while (type() == Token.OpenNode) flow.addNode(node());
    	while (type() == Token.OpenLink) flow.addLink(link());
    	flow.setSink(sink());
    	
    	top = saved;
    	test(Token.CloseFlow);
    	return flow;
    }
    
    private USource source() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenSource);
    	test(Token.Close);
    	UInput input = input();
    	
    	test(Token.CloseSource);
    	return new USource(input);
    }
    
    private UInput input() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenInput);
    	final XMLAttributeMap map = attributes();
    	
    	test(Token.EmptyClose);
    	return new UInput(map);
    }
    
    private USink sink() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenSink);
    	test(Token.Close);
    	final UOutput output = output();
    	
    	test(Token.CloseSink);
    	return new USink(output);
    }
    
    private UOutput output() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenOutput);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	
    	test(Token.CloseOutput);
    	return new UOutput(map);
    }
    
    private UNode node() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenNode);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	final UNode node = new UNode(map); 
    	INodeDescendant descendant = null;
    	VTable saved = top;
		top = new VTable(top);
		
    	while (type() == Token.OpenMessage) node.addMessage(message());
    	while (type() == Token.OpenVariable) node.addVariable(variable());
    	node.setCondition(condition());
    	
    	//���� ���� 
    	while (type() == Token.OpenCopy || type() == Token.OpenInvoke || type() == Token.OpenWait) {
    		switch (type()) {
    		case OpenCopy:
    			descendant = copy();
    			break;
    		case OpenInvoke:
    			descendant = invoke();
    			break;
    		case OpenWait:
    			descendant = wait_();
    			break;
    		default:
    			throw new uWDLParseException();	
    		}
    		node.addDescendant(descendant);
    	}
    	if(type() == Token.OpenSynchronize) node.setSynchronize(synchronize());
    	top = saved;
    	test(Token.CloseNode);
    	return node;
    }
    
    private USynchronize synchronize() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenSynchronize);
    	
    	test(Token.EmptyClose);
    	return new USynchronize();
    }
    
    private UCopy copy() throws IOException, InvalidTokenException, uWDLParseException, UnvalidVariableException {
    	test(Token.OpenCopy);
    	test(Token.Close);
    	
    	test(Token.CloseCopy);
    	return new UCopy(from(), to());
    }
    
    private UTo to() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenTo);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	
    	test(Token.CloseTo);
    	return new UTo(map);
    }
    
    private UInvoke invoke() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenInvoke);
    	final XMLAttributeMap map = attributes();
    	
    	test(Token.EmptyClose);
    	return new UInvoke(map);
    }
    
    private UWait wait_() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenWait);
    	final XMLAttributeMap map = attributes();
    	test(Token.Close);
    	
    	test(Token.CloseWait);
    	return new UWait(map);
    }
    
    private ULink link() throws IOException, InvalidTokenException, uWDLParseException {
    	test(Token.OpenLink);
    	final XMLAttributeMap map = attributes();
    	
    	test(Token.EmptyClose);
    	return new ULink(map);
    }
}
