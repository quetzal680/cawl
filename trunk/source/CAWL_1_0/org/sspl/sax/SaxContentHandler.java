package org.sspl.sax;

import org.uwdl.parser.UnvalidVariableException;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLUnknownLocatorTypeException;
import org.uwdl.parser.uWDLUnknownVersionException;
import org.uwdl.parser.ast.UActivate;
import org.uwdl.parser.ast.UActivator;
import org.uwdl.parser.ast.UBaseOntologies;
import org.uwdl.parser.ast.UCondition;
import org.uwdl.parser.ast.UConstraint;
import org.uwdl.parser.ast.UContext;
import org.uwdl.parser.ast.UElement;
import org.uwdl.parser.ast.UFlow;
import org.uwdl.parser.ast.UFrom;
import org.uwdl.parser.ast.UInitialize;
import org.uwdl.parser.ast.UInput;
import org.uwdl.parser.ast.UInvoke;
import org.uwdl.parser.ast.ULink;
import org.uwdl.parser.ast.ULocator;
import org.uwdl.parser.ast.UMessage;
import org.uwdl.parser.ast.UNSync;
import org.uwdl.parser.ast.UNode;
import org.uwdl.parser.ast.UObject;
import org.uwdl.parser.ast.UOntology;
import org.uwdl.parser.ast.UOutput;
import org.uwdl.parser.ast.UParallel;
import org.uwdl.parser.ast.UPart;
import org.uwdl.parser.ast.URule;
import org.uwdl.parser.ast.UServiceProvider;
import org.uwdl.parser.ast.USink;
import org.uwdl.parser.ast.USource;
import org.uwdl.parser.ast.USubject;
import org.uwdl.parser.ast.USynchronize;
import org.uwdl.parser.ast.UTransition;
import org.uwdl.parser.ast.UUWDL;
import org.uwdl.parser.ast.UVariable;
import org.uwdl.parser.ast.UVerb;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.Vector;

public class SaxContentHandler implements ContentHandler {
	private UUWDL uWDL;
	private static String pcdata;
	private static Vector<UElement> stack = new Vector<UElement>();
	private static UElement current;
	
	public void setDocumentLocator( Locator locator ) {}
	public void startDocument() throws SAXException {}
	public void endDocument() throws SAXException {}
	public void startPrefixMapping(String prefix, String uri) throws SAXException {}
	public void endPrefixMapping(String prefix) throws SAXException {}
	
	// 시작 태그를 만났을 때 발생하는 이벤트를 처리하는 메소드
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		System.out.println("<" + localName + ">");
		
		switch( CawlToken.value(localName.toUpperCase()) )
		{
			case CAWL :
				try {
					uWDL = new UUWDL( atts.getValue("name"), atts.getValue("version"), atts.getValue("targetNamespace") );
					stack.add(uWDL);
				} catch (uWDLUnknownVersionException e) {
					e.printStackTrace();
				} catch (uWDLMissingRequiredAttributeException e) {
					e.printStackTrace();
				}
				break;
				
			case BASEONTOLOGIES :
				UBaseOntologies baseOntologies = new UBaseOntologies();
				((UUWDL)current).setBaseOntologies(baseOntologies);
				stack.add(baseOntologies);
				break;
				
//			case DOCUMENTATION :
//				UDocumentation documentation = new UDocumentation();
//				if( current instanceof UUWDL ) {
//					((UUWDL)current).setDocumentation(documentation);
//				}
//				break;
				
			case ONTOLOGY :
				UOntology ontology = new UOntology(atts.getValue("location"), atts.getValue("namespace"));
				((UBaseOntologies)current).addOntology(ontology);
				break;
				
			case SERVICEPROVIDERTYPE :
				break;
			case PORTTYPE :
				break;
				
			case SERVICEPROVIDER :
				UServiceProvider serviceProvider = new UServiceProvider(atts.getValue("name"), atts.getValue("type"));
				((UUWDL)current).addServiceProvider(serviceProvider);
				stack.add(serviceProvider);
				break;
				
			case LOCATOR :
				try {
					ULocator locator = new ULocator(atts.getValue("type"));
					((UServiceProvider)current).setLocator(locator);
				} catch (uWDLMissingRequiredAttributeException e) {
					e.printStackTrace();
				} catch (uWDLUnknownLocatorTypeException e) {
					e.printStackTrace();
				}
				break;
				
			case MESSAGE :
				UMessage message = new UMessage(atts.getValue("name"));
				if( current instanceof UUWDL ) {
					((UUWDL)current).addMessage(message);
				} else if( current instanceof UActivator ) {
					((UActivator)current).addMessage(message);
				} else if( current instanceof UNode ) {
					((UNode)current).addMessage(message);
				}
				stack.add(message);
				break;
				
			case PART :
				UPart part = new UPart(atts.getValue("name"), atts.getValue("element"), atts.getValue("type"));
				((UMessage)current).addPart(part);
				break;
				
			case VARIABLE :
				UVariable variable = new UVariable(atts.getValue("name"), atts.getValue("type"));
				if( current instanceof UUWDL ) {
					((UUWDL)current).addVariable(variable);
				} else if( current instanceof UActivator ) {
					((UActivator)current).addVariables(variable);
				} else if( current instanceof UNode ) {
					((UNode)current).addVariable(variable);
				}
				stack.add(variable);
				break;
				
			case INITIALIZE :
				UInitialize initialize = new UInitialize(atts.getValue("part"));
				((UVariable)current).addInitialize(initialize);
				stack.add(initialize);
				break;
				
			case FROM :
				UFrom from = new UFrom(atts.getValue("variable"), atts.getValue("part"), atts.getValue("expression"));
				((UInitialize)current).setFrom(from);
				break;
				
			case ACTIVATOR :
				UActivator activator = new UActivator(atts.getValue("name"));
				((UUWDL)current).addActivator(activator);
				stack.add(activator);
				break;
				
			case CONDITION :
				UCondition condition = new UCondition(atts.getValue("expression"));
				if( current instanceof UActivator ) {
					((UActivator)current).setCondition(condition);
				} else if ( current instanceof UNode ) {
					((UNode)current).setCondition(condition);
				}
				stack.add(condition);
				break;
				
			case CASE :
				break;
			case EVENT :
				break;
				
			case CONTEXT :
				UContext context = new UContext(atts.getValue("name"), atts.getValue("priority"));
				((UCondition)current).setDescendant(context);
				stack.add(context);
				break;
				
			case RULE :
				URule rule = new URule(atts.getValue("name"), atts.getValue("expression"), atts.getValue("contribute"));
				((UContext)current).addRule(rule);
				stack.add(rule);
				break;
				
			case CONSTRAINT :
				UConstraint constraint = new UConstraint(atts.getValue("name"), atts.getValue("expression"));
				((URule)current).addConstraint(constraint);
				stack.add(constraint);
				break;
				
			case SUBJECT :
				USubject subject = new USubject(atts.getValue("type"));
				((UConstraint)current).setSubject(subject);
				break;
				
			case VERB :
				UVerb verb = new UVerb();
				((UConstraint)current).setVerb(verb);
				break;
				
			case OBJECT :
				UObject object = new UObject(atts.getValue("type"));
				((UConstraint)current).setObject(object);
				break;
				
			case ACTIVATE :
				UActivate activate = new UActivate(atts.getValue("activator"), atts.getValue("flow"), atts.getValue("input"));
				((UActivator)current).addActivate(activate);
				break;
				
			case DEACTIVATE :
				break;
				
			case FLOW :
				UFlow flow = new UFlow(atts.getValue("name"));
				((UUWDL)current).addFlow(flow);
				stack.add(flow);
				break;
				
			case SOURCE :
				USource source = new USource();
				((UFlow)current).setSource(source);
				stack.add(source);
				break;
				
			case INPUT :
				UInput input = new UInput(atts.getValue("name"), atts.getValue("type"));
				((USource)current).setInput(input);
				break;
				
			case NODE :
				UNode node = new UNode(atts.getValue("name"));
				((UFlow)current).addNode(node);
				stack.add(node);
				break;
				
			case COPY :
				break;
			case TO :
				break;
				
			case INVOKE :
				UInvoke invoke = new UInvoke(atts.getValue("serviceProvider"), 
											 atts.getValue("portType"), 
											 atts.getValue("operation"),
											 atts.getValue("input"),
											 atts.getValue("output"),
											 atts.getValue("subflow"));
				((UNode)current).addDescendant(invoke);
				break;
				
			case WAIT :
				break;
			case CATCH :
				break;
				
			case LINK :
				ULink link = new ULink(atts.getValue("from"), atts.getValue("to"));
				((UFlow)current).addLink(link);
				break;
				
			case SINK :
				USink sink = new USink();
				((UFlow)current).setSink(sink);
				stack.add(sink);
				break;
				
			case OUTPUT :
				UOutput output = new UOutput(atts.getValue("name"), atts.getValue("type"));
				((USink)current).setOutput(output);
				break;
				
			case PARALLEL :
				UParallel parallel = new UParallel();
				((UNode)current).setParallel(parallel);
				stack.add(parallel);
				break;
				
			case TRANSITION :
				UTransition transition = new UTransition(atts.getValue("linkId"));
				((UParallel)current).addTransition(transition);
				break;
				
			case SYNCHRONIZE :
				USynchronize synchronize = new USynchronize();
				if( current instanceof USink ) {
					((USink)current).setSynchronize(synchronize);	
				} else if( current instanceof UNode ) {
					((UNode)current).setSynchronize(synchronize);
				}
				stack.add(synchronize);
				break;
				
			case NSYNC : 
				UNSync nSync = new UNSync(atts.getValue("name"));
				((USynchronize)current).addUNSync(nSync);
				break;

			case NOVALUE :
				break;
		}
		
		current = stack.lastElement();
	}
	
	// 끝 태그를 만났을 때 발생하는 이벤트를 처리하는 메소드
	public void endElement(String uri, String localName, String qName) throws SAXException {
		System.out.println("</" + localName + ">");
		
		switch( CawlToken.value(localName.toUpperCase()) )
		{
			case CAWL :
			case BASEONTOLOGIES :
			case SERVICEPROVIDER :
			case MESSAGE :
			case VARIABLE :
			case INITIALIZE :
			case ACTIVATOR :
			case CONDITION :
			case CONTEXT :
			case RULE :
			case CONSTRAINT :
			case NODE :
			case FLOW :
			case SOURCE :
			case SINK :
			case PARALLEL :
			case SYNCHRONIZE :
				stack.remove(stack.size()-1);	
				break;
				
			case ONTOLOGY :
			case LOCATOR :
			case PART :
			case INPUT :
			case INVOKE :
			case ACTIVATE :
			case LINK :
			case OUTPUT :
			case TRANSITION :
			case NSYNC : 
				break;

//			case DOCUMENTATION :
//				if( current instanceof UUWDL ) {
//					((UUWDL)current).getDocumentation().setPcdata(pcdata);
//				}
//				break;
				
			case SUBJECT :
				((UConstraint)current).getSubject().setPcdata(pcdata);
				break;
				
			case VERB :
				((UConstraint)current).getVerb().setPcdata(pcdata);
				break;
				
			case OBJECT :
				((UConstraint)current).getObject().setPcdata(pcdata);
				break;				
				
			case SERVICEPROVIDERTYPE :
				break;
			case PORTTYPE :
				break;
			case FROM :
				break;
			case CASE :
				break;
			case EVENT :
				break;
			case DEACTIVATE :
				break;
			case COPY :
				break;
			case TO :
				break;
			case WAIT :
				break;
			case CATCH :
				break;
			case NOVALUE :
				break;
		}

		if( !stack.isEmpty() ) {
			current = stack.lastElement();	
		}
	}
	
	// 문자 데이터를 만났을 때 발생하는 이벤트를 처리하는 메소드
	public void characters( char[] ch, int start, int length ) throws SAXException {
		String content = new String(ch, start, length);
		String tmp = null;
		
		if( current instanceof UConstraint ) {
			if( !content.trim().equals("") ) {
	    		if(content.charAt(0) == '?') {
	    			//message variable
	    			if(content.charAt(1) == '=') {
	    				tmp = find(content.substring(2));
//	    				data = top.getData(text.substring(2));
	    			} else {
	    				//variable
	    				tmp = find(content.substring(1));
//	    				data = top.getData(text.substring(1));
	    				//System.out.println("pcdata()" + type + ":" + text.substring(1));
	    			}
//    				throw new UnvalidVariableException();
	    		}
				System.out.println("tmp : " + tmp);
			}
		}
		if(tmp==null)
			pcdata = content;
		else
			pcdata = tmp;
	}
	
	public void ignorableWhitespace( char[] ch, int start, int length ) throws SAXException {}
	public void processingInstruction( String target, String data ) throws SAXException {}
	public void skippedEntity(String name) throws SAXException {}

	public UUWDL getUWDL() {
		return uWDL;
	}
	
	private String find(String content) {
		String tmp[] = content.split("/");
		
		for( int i = stack.size()-1; i>=0; i-- ) {
			if( stack.get(i) instanceof UUWDL ) {
				for( UMessage message : ((UUWDL)stack.get(i)).getMessages() ) {
					if( message.getName().equals(tmp[0]) ) {
						for( UPart part : message.getParts() ) {
							if( part.getName().equals(tmp[1]) ) {
								return part.getType();
							}
						}	
					}
				}
				for( UVariable variable : ((UUWDL)stack.get(i)).getVariables() ) {
					if( variable.getName().equals(tmp[0]) ) {
						for( UInitialize initialize : variable.getInitializes() ) {
							if( initialize.getPart().equals(tmp[1]) ) {
								return initialize.getFrom().getExpression();
							}
						}	
					}
				}
			} 
			else if( stack.get(i) instanceof UActivator ) {
				for( UMessage message : ((UActivator)stack.get(i)).getMessages() ) {
					if( message.getName().equals(tmp[0]) ) {
						for( UPart part : message.getParts() ) {
							if( part.getName().equals(tmp[1]) ) {
								return part.getType();
							}
						}	
					}
				}
				for( UVariable variable : ((UActivator)stack.get(i)).getVariables() ) {
					if( variable.getName().equals(tmp[0]) ) {
						for( UInitialize initialize : variable.getInitializes() ) {
							if( initialize.getPart().equals(tmp[1]) ) {
								return initialize.getFrom().getExpression();
							}
						}	
					}
				}
			} 
			else if( stack.get(i) instanceof UNode ) {
				for( UMessage message : ((UNode)stack.get(i)).getMessages() ) {
					if( message.getName().equals(tmp[0]) ) {
						for( UPart part : message.getParts() ) {
							if( part.getName().equals(tmp[1]) ) {
								return part.getType();
							}
						}	
					}
				}
				for( UVariable variable : ((UNode)stack.get(i)).getVariable() ) {
					if( variable.getName().equals(tmp[0]) ) {
						for( UInitialize initialize : variable.getInitializes() ) {
							if( initialize.getPart().equals(tmp[1]) ) {
								return initialize.getFrom().getExpression();
							}
						}	
					}
				}
			}
		}
		
		return null;
	}
}
