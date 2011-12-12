package org.uwdl.mapper;

import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.uwdl.parser.ast.IConditionDescendant;
import org.uwdl.parser.ast.IRuleDescendant;
import org.uwdl.parser.ast.UCondition;
import org.uwdl.parser.ast.UConstraint;
import org.uwdl.parser.ast.UContext;
import org.uwdl.parser.ast.URule;

public class ContextComparator {
////	private OWLReasoner reasoner = null;
//	private static ContextComparator instance = new ContextComparator();
//	
//	private ContextComparator() {
//		// Create a reasoner
//		OWLReasonerConfiguration config = new SimpleConfiguration();
//		OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
//		OWLReasoner reasoner = reasonerFactory.createReasoner(ContextOntology.getInstance().getOntology(), config);
//		String namespace = ContextOntology.getInstance().getNamespace();
//		
////		// Create a console progress monitor.
////		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
////		
////		// Create a reasoner
////		OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
////		OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
////		reasoner = reasonerFactory.createReasoner(ContextOntology.getInstance().getOntology(), config);
////
////		// Ask the reasoner to do all the necessary work now
//////		reasoner.precomputeInferences();
////
////		// We can determine if the ontology is actually consistent (in this case, it should be).
////		boolean consistent = reasoner.isConsistent();
////		System.out.println("Consistent: " + consistent);
////		System.out.println("\n");
////
////		Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
////		Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
////		if (!unsatisfiable.isEmpty()) {
////			System.out.println("The following classes are unsatisfiable: ");
////			for (OWLClass cls : unsatisfiable) {
////				System.out.println("    " + cls);
////			}
////		} else {
////			System.out.println("There are no unsatisfiable classes\n");
////		}
////		
////		// Finally, let's print out the class hierarchy.
////		Node<OWLClass> topNode = reasoner.getTopClassNode();
////		ContextOntology.print(topNode, reasoner, 0);
////		System.out.println("");
//	}
//	
//	public static ContextComparator getInstance() {
//		return instance;
//	}
//	
//	public boolean compare(UCondition condition) {
//		IConditionDescendant descendant = condition.getDescendant();
//		if (descendant instanceof UContext) {
//			return compare((UContext)descendant);
//		}
//		return false;
//	}
//	
//	private boolean compare(UContext context) {
//		for (URule i: context.getRules()) {
//			if (compare(i)) return true;
//		}
//		return false;
//	}
//	
//	// 마지막 조건만 맞으면 다 맞은거로 확인하고 참으로 판단하는 버그 존재
//	private boolean compare(URule rule) {
//		boolean test = false;
//		for (IRuleDescendant i: rule.getConstraints()) {
//			switch(i.getLogicTag()) {
//			case CONSTRAINT: 
//				test = compare((UConstraint)i.getConstraint()); 
//				break;
//			case AND:
//				test &= compare((UConstraint)i.getConstraint()); 
//				break;
//			case OR:
//				test |= compare((UConstraint)i.getConstraint()); 
//				break;
//			case NOT:
//				test = !compare((UConstraint)i.getConstraint()); 
//				break;
//			}
//		}
//		return test;
//	}
//	
//	private boolean compare(UConstraint constraint) {
//		String individual = constraint.getSubject().getPcdata();
//		String objectProperty = constraint.getVerb().getPcdata();
//		String value = constraint.getObject().getPcdata();
//		
//		return reasoner(individual, objectProperty, value);
//	}
//	
//	
//	public boolean reasoner( String individual,	String objectProperty, String value ) {
//		System.out.println("<reasoner> ["+individual+", " + objectProperty + ", " + value +"]");
//		// Create a reasoner
//		OWLReasonerConfiguration config = new SimpleConfiguration();
//		OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
//		OWLReasoner reasoner = reasonerFactory.createReasoner(ContextOntology.getInstance().getOntology(), config);
//
//		String namespace = ContextOntology.getInstance().getNamespace();
//		
//		OWLDataFactory fac = ContextOntology.getInstance().getManager().getOWLDataFactory();
//		OWLNamedIndividual owlNamedIndividual = fac.getOWLNamedIndividual(IRI.create(namespace+"#"+individual));
//		OWLObjectProperty owlObjectProperty = fac.getOWLObjectProperty(IRI.create(namespace+"#"+objectProperty));
//		
////		System.out.println("\t<"+owlNamedIndividual.getIRI()+">");
////		System.out.println("\t<"+owlObjectProperty.getIRI()+">");
//		
//		// Now ask the reasoner for the has_pet property values for Mick
//		NodeSet<OWLNamedIndividual> locateValuesNodeSet = reasoner.getObjectPropertyValues(owlNamedIndividual, owlObjectProperty);
//		Set<OWLNamedIndividual> vs = locateValuesNodeSet.getFlattened();
//		for (OWLNamedIndividual ind : vs) {
//			if( ind.getIRI().equals(IRI.create(namespace+"#"+value)) ) {
//				System.out.print("\t[The \""+objectProperty+"\" property values for \""+individual+"\" are] : ");
//				System.out.print("\t" + ind);
//				System.out.println("\ttrue\n");
//				return true;
//			}
//		}
////		System.out.println("false");
//		return false;
//	}
//	
//	public OWLNamedIndividual individualReasoner( String individual, String objectProperty) {
//		System.out.println("<indireasoner> ["+individual+", " + objectProperty + "]");
//
//
//
//		
//		
//		OWLDataFactory fac = ContextOntology.getInstance().getManager().getOWLDataFactory();
//		OWLNamedIndividual owlNamedIndividual = fac.getOWLNamedIndividual(IRI.create(namespace+"#"+individual));
//		OWLObjectProperty owlObjectProperty = fac.getOWLObjectProperty(IRI.create(namespace+"#"+objectProperty));
//		
////		System.out.println("\t<"+owlNamedIndividual.getIRI()+">");
////		System.out.println("\t<"+owlObjectProperty.getIRI()+">");
//		
//		// Now ask the reasoner for the has_pet property values for Mick
//		NodeSet<OWLNamedIndividual> locateValuesNodeSet = reasoner.getObjectPropertyValues(owlNamedIndividual, owlObjectProperty);
//		Set<OWLNamedIndividual> vs = locateValuesNodeSet.getFlattened();
//		
//		for (OWLNamedIndividual ind : vs) {
//			System.out.print("\t[The \""+objectProperty+"\" property values for \""+individual+"\" are] : ");
//			System.out.println("\t" + ind +"\n");
//			return ind;
//		}
////		System.out.println("\tnull\n");
//		return null;
//	}
}
