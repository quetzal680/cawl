package org.sspl.sax;

import java.util.Arrays;
import java.util.Iterator;

import org.sspl.parser.xml.XMLAttribute;
import org.uwdl.parser.ast.INodeDescendant;
import org.uwdl.parser.ast.IRuleDescendant;
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
import org.uwdl.parser.ast.UElement;
import org.uwdl.parser.ast.UEvent;
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
import org.uwdl.parser.ast.UNotConstraint;
import org.uwdl.parser.ast.UObject;
import org.uwdl.parser.ast.UOntology;
import org.uwdl.parser.ast.UOrConstraint;
import org.uwdl.parser.ast.UOutput;
import org.uwdl.parser.ast.UParallel;
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
import org.uwdl.parser.ast.UTransition;
import org.uwdl.parser.ast.UUWDL;
import org.uwdl.parser.ast.UVariable;
import org.uwdl.parser.ast.UVerb;
import org.uwdl.parser.ast.UWait;

public class SaxUnParser implements SaxVisitor {
	private int tabs = 0;
	
	private void indent() {
		for (int i = 0; i < tabs; i++) {
			System.out.print("\t");
		}
	}
	
	private void open(UElement element) {
		indent();
		tabs++;
		System.out.print("<");
		System.out.print(element.getTag().getText());
		print(element.getAttributes());
		System.out.println(">");
	}
	
	private void close(UElement element) {
		tabs--;
		indent();
		System.out.print("</");
		System.out.print(element.getTag().getText());
		System.out.println(">");
	}
	
	private void printPcdata( String pcdata ) {
		indent();
		System.out.println( pcdata );
	}
	
	private void print(XMLAttribute[] attributes) {
		if (attributes == null) return;
		if (attributes.length == 0) return;
		for (Iterator<XMLAttribute> i = Arrays.asList(attributes).iterator(); i.hasNext(); ) {
			System.out.print(" ");
			System.out.print(i.next());
		}
	}

	public void visit(UUWDL uwdl) {
		open( uwdl );
//		visit( uwdl.getDocumentation() );
		visit( uwdl.getBaseOntologies() );
		System.out.println();
		for( UServiceProvider serviceProvider : uwdl.getServiceProviders() ) {
			visit( serviceProvider );
			System.out.println();
		}
		for( UMessage message : uwdl.getMessages() ) {
			visit( message );
			System.out.println();
		}
		for( UVariable variable : uwdl.getVariables() ) {
			visit( variable );
			System.out.println();
		}
		for( UActivator activator : uwdl.getActivators() ) {
			visit( activator );	
			System.out.println();
		}
		for( UFlow flow : uwdl.getFlows() ) {
			visit( flow );
			System.out.println();
		}
		close( uwdl );
	}
	
	public void visit(UActivate activate) {
		open( activate );
		close( activate );
	}
	
	public void visit(UCase uCase) {
		open( uCase );
		close( uCase );
	}
		
	public void visit(UCondition condition) {
		open( condition );
		condition.getDescendant().accept(this);
		close( condition );
	}
	
	public void visit(UAndConstraint andCondition) {
		open( andCondition );
		close( andCondition );
	}
	
	public void visit(UOrConstraint orCondition) {
		open( orCondition );
		close( orCondition );
	}
	
	public void visit(UNotConstraint notCondition) {
		open( notCondition );
		close( notCondition );
	}
	
	public void visit(UConstraint constraint) {
		open( constraint );
		visit(constraint.getSubject());
		visit(constraint.getVerb());
		visit(constraint.getObject());
		close( constraint );
	}
	
	public void visit(UContext context) {
		open( context );
		for( URule rule : context.getRules() ) {
			visit( rule );
		}
		close( context );
	}
	
	public void visit(UActivator activator) {
		open( activator );
		for( UVariable variable : activator.getVariables() ) {
			visit( variable );			
		}
		visit( activator.getCondition() );
		for( UActivate activate : activator.getActivates() ) {
			visit( activate );
		}
		close( activator );
	}
	
	public void visit(UBaseOntologies baseOntologies) {
		open( baseOntologies );
		for( UOntology ontology : baseOntologies.getOntologies() ) {
			visit(ontology);
		}
		close( baseOntologies );
	}
	
//	public void visit(UDocumentation description) {
//		open( description );
//		printPcdata( description.getPcdata() );
//		close( description );
//	}
	
	public void visit(UEvent event) {
		open( event );
		close( event );
	}
	public void visit(UDeactivate deactivate) {
		open( deactivate );
		close( deactivate );
	}
	public void visit(UFlow flow) {
		open( flow );
		visit( flow.getSource() );
		for( UNode node : flow.getNodes() ) {
			visit( node );	
			System.out.println();
		}
		for( ULink link : flow.getLinks() ) {
			visit( link );	
		}
		System.out.println();
		visit( flow.getSink() );
		close( flow );
	}
	public void visit(ULink link) {
		open( link );
		close( link );
	}
	public void visit(UFrom from) {
		open( from );
		close( from );
	}
	
	public void visit(UNode node) {
		open( node );
		for( UVariable variable : node.getVariable() ) {
			visit( variable );
		}
		visit( node.getCondition() );
		for( INodeDescendant iNodeDescendant : node.getINodeDescendant() ) {
			visit( (UInvoke)iNodeDescendant );	
		}
		if( node.getParallel()!=null )
			visit( node.getParallel() );
		close( node );
	}
	
	public void visit(UObject object) {
		open( object );
		printPcdata( object.getPcdata() );
		close( object );
	}
	
	public void visit(UInitialize initialize) {
		open( initialize );
		visit( initialize.getFrom() );
		close( initialize );
	}
	
	public void visit(ULocator locator) {
		open( locator );
		close( locator );
	}
	
	public void visit(UPortType portType) {
		open( portType );
		close( portType );
	}
	
	public void visit(UMessage message) {
		open( message );
		for( UPart part : message.getParts() ) {
			visit( part );	
		}
		close( message );
	}
	
	public void visit(UPart part) {
		open( part );
		close( part );
	}
	
	public void visit(UServiceProvider serviceProvider) {
		open( serviceProvider );
		visit( serviceProvider.getLocator() );
		close( serviceProvider );
	}
	
	public void visit(URule rule) {
		open( rule );
		for( IRuleDescendant descendant : rule.getConstraints() ) {
			descendant.accept(this);
		}
		close( rule );
	}
	
	public void visit(UServiceProviderType serviceProviderType) {
		open( serviceProviderType );
		close( serviceProviderType );
	}
	
	public void visit(USink sink) {
		open( sink );
		if( sink.getSynchronize()!=null )
			visit( sink.getSynchronize() );
		visit( sink.getOutput() );
		close( sink );
	}
	
	public void visit(USource source) {
		open( source );
		visit(source.getInput());
		close( source );
	}
	
	public void visit(UVariable variable) {
		open( variable );
		for( UInitialize initialize : variable.getInitializes() ) {
			visit( initialize );
		}
		close( variable );
	}
	
	public void visit(USubject subject) {
		open( subject );
		printPcdata( subject.getPcdata() );
		close( subject );
	}
	
	public void visit(UVerb verb) {
		open( verb );
		printPcdata( verb.getPcdata() );
		close( verb );
	}
	
	public void visit(UCopy copy) {
		open( copy );
		close( copy );
	}
	public void visit(UTo to) {
		open( to );
		close( to );
	}
	public void visit(UWait wait) {
		open( wait );
		close( wait );
	}
	public void visit(UInvoke invoke) {
		open( invoke );
		close( invoke );
	}
	
	public void visit(UOntology ontology) {
		open( ontology );
		close( ontology );
	}
	
	public void visit(UInput input) {
		open( input );
		close( input );
	}
	public void visit(UOutput output) {
		open( output );
		close( output );
	}
	
	public void visit(USynchronize synchronize) {
		open( synchronize );
		for( UNSync nSync : synchronize.getnSyncs() ) {
			visit( nSync );
		}
		close( synchronize );
	}

	public void visit(UParallel parallel) {
		open( parallel );
		for( UTransition transition : parallel.getTransitions() ) {
			visit( transition );	
		}
		close( parallel );
	}

	public void visit(UTransition transition) {
		open( transition );
		close( transition );		
	}

	public void visit(UNSync nSync) {
		open( nSync );
		close( nSync );		
	}
}
