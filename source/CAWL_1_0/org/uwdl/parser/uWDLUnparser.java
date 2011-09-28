package org.uwdl.parser;

import java.io.PrintStream;
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

public class uWDLUnparser implements uWDLVisitor {

	private final PrintStream o;
	private int tabs = 0;
	private final String tab;
	
	public uWDLUnparser(PrintStream o) {
		this(o, "\t");
	}
	
	public uWDLUnparser(PrintStream o, String tab) {
		this.o = o;
		this.tab = tab;
	}
	
	private void print(XMLAttribute attribute) {
		o.print(attribute.name());
		o.print("=");
		o.print(quote(attribute.value()));
	}
	
	private void print(XMLAttribute[] attributes) {
		if (attributes == null) return;
		if (attributes.length == 0) return;
		for (Iterator<XMLAttribute> i = Arrays.asList(attributes).iterator(); i.hasNext(); ) {
			o.print(" ");
			print(i.next());
		}
	}
	
	private void indent() {
		for (int i = 0; i < tabs; i++) o.print(tab); 
	}
	
	private void newline() {
		o.println();
	}
	
	private String quote(String text) {
		return '\"' + text + '\"';
	}
	
	private void open(UElement element) {
		indent();
		tabs++;
		o.print("<");
		o.print(element.getTag().getText());
		print(element.getAttributes());
		o.print(">");
		newline();
	}
	
	private void close(UElement element) {
		tabs--;
		indent();
		o.print("</");
		o.print(element.getTag().getText());
		o.print(">");
		newline();
	}

	private void pcdata(UElement element) {
		indent();
		o.print("<");
		o.print(element.getTag().getText());
		print(element.getAttributes());
		o.print(">");
		o.print(element.getPcdata());
		o.print("</");
		o.print(element.getTag().getText());
		o.print(">");
		newline();
	}

	////// --------------------------------------------------------------------------------------------------------
    ////// --------------------------------------------------------------------------------------------------------
    ////// --------------------------------------------------------------------------------------------------------
	
	public void visit(UActivate activate) {
		pcdata(activate);
	}

	public void visit(UCase case_) {
		open(case_);
		case_.getEvent().accept(this);
		close(case_);
	}

	public void visit(UCondition condition) {
		open(condition);
		condition.getDescendant().accept(this);
		close(condition);
	}

	public void visit(UConstraint constraint) {
		open(constraint);
		constraint.getSubject().accept(this);
		constraint.getVerb().accept(this);
		constraint.getObject().accept(this);
		close(constraint);
	}

	public void visit(UAndConstraint constraint) {
		open(constraint);
		constraint.getSubject().accept(this);
		constraint.getVerb().accept(this);
		constraint.getObject().accept(this);
		close(constraint);
	}
	
	public void visit(UOrConstraint constraint) {
		open(constraint);
		constraint.getSubject().accept(this);
		constraint.getVerb().accept(this);
		constraint.getObject().accept(this);
		close(constraint);
	}
	
	public void visit(UNotConstraint constraint) {
		open(constraint);
		constraint.getSubject().accept(this);
		constraint.getVerb().accept(this);
		constraint.getObject().accept(this);
		close(constraint);
	}
	
	public void visit(UContext context) {
		open(context);
		for(URule rule : context.getRules()) rule.accept(this);
		close(context);
	}

	public void visit(UActivator activator) {
		open(activator);
		for(UMessage message : activator.getMessages()) message.accept(this);
		for(UVariable variable : activator.getVariables()) variable.accept(this);
		activator.getCondition().accept(this);
		for(UActivate activate : activator.getActivates()) activate.accept(this);
		for(UDeactivate deactivate : activator.getDeactivates()) deactivate.accept(this);
		close(activator);
	}

	public void visit(UBaseOntologies baseOntologies) {
		open(baseOntologies);
		for(UOntology ontology : baseOntologies.getOntologies()) ontology.accept(this);
		close(baseOntologies);
	}

	public void visit(UDocumentation documentation) {
		pcdata(documentation);
	}

	public void visit(UEvent event) {
		open(event);
		close(event);
	}

	public void visit(UDeactivate deactivate) {
		open(deactivate);
		close(deactivate);
	}

	public void visit(UFlow flow) {
		open(flow);
		for(UMessage message : flow.getMessages()) message.accept(this);
		for(UVariable variable : flow.getVariables()) variable.accept(this);
		flow.getSource().accept(this);
		for(UNode node : flow.getNodes()) node.accept(this);
		for(ULink link : flow.getLinks()) link.accept(this);
		flow.getSink().accept(this);
		close(flow);
	}

	public void visit(ULink link) {
		open(link);
		close(link);
	}

	public void visit(UFrom from) {
		open(from);
		close(from);
	}

	public void visit(UNode node) {
		open(node);
		node.getCondition().accept(this);
		for(UMessage message : node.getMessages()) message.accept(this);
		for(UVariable variable : node.getVariable()) variable.accept(this);
		for(INodeDescendant nodeDescendant : node.getINodeDescendant()) nodeDescendant.accept(this);
		if(node.getSynchronize() != null) node.getSynchronize().accept(this);
		close(node);
	}

	public void visit(UObject object) {
		//open(object);
		//close(object);
		pcdata(object);
	}

	public void visit(UInitialize initialize) {
		open(initialize);
		close(initialize);
	}

	public void visit(ULocator locator) {
		pcdata(locator);
	}

	public void visit(UPortType portType) {
		open(portType);
		//operation ó�� �κ� ���� operation�� ����...
		close(portType);
	}

	public void visit(UMessage message) {
		open(message);
		for(UPart part : message.getParts()) part.accept(this);
		close(message);
	}

	public void visit(UPart part) {
		open(part);
		close(part);
	}

	public void visit(UServiceProvider serviceProvider) {
		open(serviceProvider);
		serviceProvider.getLocator().accept(this);
		close(serviceProvider);
	}

	public void visit(URule rule) {
		open(rule);
		for(IRuleDescendant constraint : rule.getConstraints()) constraint.accept(this);
		close(rule);
	}

	public void visit(UServiceProviderType serviceProviderType) {
		open(serviceProviderType);
		for(UPortType portType : serviceProviderType.getPortTypes()) portType.accept(this);
		close(serviceProviderType);
	}

	public void visit(USink sink) {
		open(sink);
		sink.getOutput().accept(this);
		close(sink);
	}

	public void visit(USource source) {
		open(source);
		source.getInput().accept(this);
		close(source);
	}

	public void visit(UVariable variable) {
		open(variable);
		for(UInitialize initialize : variable.getInitializes()) initialize.accept(this);
		close(variable);
	}

	public void visit(USubject subject) {
		pcdata(subject);
	}

	public void visit(UUWDL uwdl) {
		open(uwdl);
		uwdl.getBaseOntologies().accept(this);
		for(UServiceProviderType serviceProviderType : uwdl.getServiceProviderTypes()) serviceProviderType.accept(this);
		for(UServiceProvider serviceProvider : uwdl.getServiceProviders()) serviceProvider.accept(this);
		for(UMessage message : uwdl.getMessages()) message.accept(this);
		for(UVariable variable : uwdl.getVariables()) variable.accept(this);
		for(UActivator activator : uwdl.getActivators()) activator.accept(this);
		for(UFlow flow : uwdl.getFlows()) flow.accept(this);
		close(uwdl);
	}

	public void visit(UVerb verb) {
		pcdata(verb);
	}

	public void visit(UCopy copy) {
		open(copy);
		copy.getFrom().accept(this);
		copy.getTo().accept(this);
		close(copy);
	}

	public void visit(UTo to) {
		open(to);
		close(to);
	}

	public void visit(UWait wait) {
		open(wait);
		close(wait);
	}

	public void visit(UInvoke invoke) {
		open(invoke);
		close(invoke);
	}

	public void visit(UOntology ontology) {
		open(ontology);
		close(ontology);
		
	}

	public void visit(UInput input) {
		open(input);
		close(input);
		
	}

	public void visit(UOutput output) {
		open(output);
		close(output);
		
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
