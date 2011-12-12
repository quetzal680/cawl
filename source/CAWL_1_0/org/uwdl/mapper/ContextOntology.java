package org.uwdl.mapper;

import java.util.Iterator;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.uwdl.parser.ast.IConditionDescendant;
import org.uwdl.parser.ast.IRuleDescendant;
import org.uwdl.parser.ast.UCondition;
import org.uwdl.parser.ast.UConstraint;
import org.uwdl.parser.ast.UContext;
import org.uwdl.parser.ast.UOntology;
import org.uwdl.parser.ast.URule;

public class ContextOntology {
	public String namespace; // = "http://ss.ssu.ac.kr/Ontology.owl";
	private String DOCUMENT_IRI;
	private static DefaultPrefixManager pm = new DefaultPrefixManager("http://owl.man.ac.uk/2005/07/sssw/people#");
	private OWLOntology ontology = null;
	private OWLOntologyManager manager = null;
	private OWLReasoner reasoner = null;
	private OWLDataFactory factory = null;
	private OWLOntologyID ontologyID = null;
	private OWLDataFactory dataFactory = null;
	private OWLReasonerConfiguration config = null;
	private OWLReasonerFactory reasonerFactory = null;
	
	public ContextOntology(UOntology uOntology) throws OWLOntologyCreationException {
		this.namespace = uOntology.getNamespace();
		this.DOCUMENT_IRI = uOntology.getLocation();

		// Create our ontology manager in the usual way.
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();

		// loaded ontology
		IRI docIRI = IRI.create(DOCUMENT_IRI);
		ontology = manager.loadOntologyFromOntologyDocument(docIRI);
		System.out.println("Loaded " + ontology.getOntologyID());
		
		config = new SimpleConfiguration();
		reasonerFactory = new Reasoner.ReasonerFactory();
		reasoner = reasonerFactory.createReasoner(ontology, config);
		
		ontologyID = ontology.getOntologyID();
		dataFactory = manager.getOWLDataFactory();
	}
	
	public boolean compare(UCondition condition) {
		IConditionDescendant descendant = condition.getDescendant();
		if (descendant instanceof UContext) {
			return compare((UContext)descendant);
		}
		return false;
	}
	
	private boolean compare(UContext context) {
		for (URule i: context.getRules()) {
			if (compare(i)) return true;
		}
		return false;
	}
	
	// 마지막 조건만 맞으면 다 맞은거로 확인하고 참으로 판단하는 버그 존재
	private boolean compare(URule rule) {
		boolean test = false;
		for (IRuleDescendant i: rule.getConstraints()) {
			switch(i.getLogicTag()) {
			case CONSTRAINT: 
				test = compare((UConstraint)i.getConstraint()); 
				break;
			case AND:
				test &= compare((UConstraint)i.getConstraint()); 
				break;
			case OR:
				test |= compare((UConstraint)i.getConstraint()); 
				break;
			case NOT:
				test = !compare((UConstraint)i.getConstraint()); 
				break;
			}
		}
		return test;
	}
	
	private boolean compare(UConstraint constraint) {
		String individual = constraint.getSubject().getPcdata();
		String objectProperty = constraint.getVerb().getPcdata();
		String value = constraint.getObject().getPcdata();
		
		return reasoner(individual, objectProperty, value);
	}
	
	
	public boolean reasoner( String individual,	String objectProperty, String value ) {
		try {
//			System.out.print("<reasoner> ["+individual+", " + objectProperty + ", " + value +"]");

			reasoner = reasonerFactory.createReasoner(ontology, config);
			
			OWLNamedIndividual owlNamedIndividual = factory.getOWLNamedIndividual(IRI.create(namespace+"#"+individual));
			OWLObjectProperty owlObjectProperty = factory.getOWLObjectProperty(IRI.create(namespace+"#"+objectProperty));
			
//			System.out.println("\t<"+owlNamedIndividual.getIRI()+">");
//			System.out.println("\t<"+owlObjectProperty.getIRI()+">");
			
			// Now ask the reasoner for the has_pet property values for Mick
			NodeSet<OWLNamedIndividual> locateValuesNodeSet = reasoner.getObjectPropertyValues(owlNamedIndividual, owlObjectProperty);
			Set<OWLNamedIndividual> vs = locateValuesNodeSet.getFlattened();
			for (OWLNamedIndividual ind : vs) {
				if( ind.getIRI().equals(IRI.create(namespace+"#"+value)) ) {
					System.out.print("\t[The \""+objectProperty+"\" property values for \""+individual+"\" are] : ");
					System.out.print("\t" + ind);
					System.out.println("\ttrue\n");
					return true;
				}
			}
//			System.out.println("false");
		} catch(Exception e) {
			e.printStackTrace();
			System.out.print("<reasoner> ["+individual+", " + objectProperty + ", " + value +"]");
		}
		return false;
	}
	
	public OWLNamedIndividual individualReasoner( String individual, String objectProperty) {
//		System.out.println("<indireasoner> ["+individual+", " + objectProperty + "]");
		
		reasoner = reasonerFactory.createReasoner(ontology, config);
		
		OWLNamedIndividual owlNamedIndividual = factory.getOWLNamedIndividual(IRI.create(namespace+"#"+individual));
		OWLObjectProperty owlObjectProperty = factory.getOWLObjectProperty(IRI.create(namespace+"#"+objectProperty));
		
//		System.out.println("\t<"+owlNamedIndividual.getIRI()+">");
//		System.out.println("\t<"+owlObjectProperty.getIRI()+">");
		
		// Now ask the reasoner for the has_pet property values for Mick
		NodeSet<OWLNamedIndividual> locateValuesNodeSet = reasoner.getObjectPropertyValues(owlNamedIndividual, owlObjectProperty);
		Set<OWLNamedIndividual> vs = locateValuesNodeSet.getFlattened();
		
		for (OWLNamedIndividual ind : vs) {
			System.out.print("\t[The \""+objectProperty+"\" property values for \""+individual+"\" are] : ");
			System.out.println("\t" + ind +"\n");
			return ind;
		}
//		System.out.println("\tnull\n");
		return null;
	}
	
	public synchronized void updateIndividual(String subject, String verb, String object) {
//      try {
		  OWLIndividual subjectIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyID.getOntologyIRI() + "#" + subject));
          OWLIndividual objectIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyID.getOntologyIRI() + "#" + object));

          OWLObjectProperty objectProperty = dataFactory.getOWLObjectProperty(IRI.create(ontologyID.getOntologyIRI() + "#" + verb));

       // Finally, remove the axiom to our ontology and save
          OWLIndividual deleteIndividual = individualReasoner(subject, verb);
          if( deleteIndividual!=null ) {
              OWLObjectPropertyAssertionAxiom assertion1 = dataFactory.getOWLObjectPropertyAssertionAxiom(objectProperty, subjectIndividual, deleteIndividual);
              manager.removeAxiom(ontology, assertion1);
          }
          
          // Now create the actual assertion (triple), as an object property assertion axiom
          // subjectIndividual --> objectProperty --> objectIndividual

          // Finally, add the axiom to our ontology and save
          OWLObjectPropertyAssertionAxiom assertion2 = dataFactory.getOWLObjectPropertyAssertionAxiom(objectProperty, subjectIndividual, objectIndividual);
          AddAxiom addAxiomChange = new AddAxiom(ontology, assertion2);
          manager.applyChange(addAxiomChange);

//          System.out.println("<update> ["+subject+", " + verb + ", " + object +"]");
          // Save our ontology
//      	manager.saveOntology(ontology, new SystemOutDocumentTarget());
//      } catch (OWLOntologyStorageException e) {
//          System.out.println("Could not save ontology: " + e.getMessage());
//      }
	}

	public synchronized void createIndividual(String subject, String verb, String object) {
//		try {
			OWLIndividual subjectIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyID.getOntologyIRI() + "#" + subject));
			OWLIndividual objectIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyID.getOntologyIRI() + "#" + object));

			OWLObjectProperty objectProperty = dataFactory.getOWLObjectProperty(IRI.create(ontologyID.getOntologyIRI() + "#" + verb));

			OWLObjectPropertyAssertionAxiom assertion2 = dataFactory.getOWLObjectPropertyAssertionAxiom(objectProperty, subjectIndividual, objectIndividual);
			AddAxiom addAxiomChange = new AddAxiom(ontology, assertion2);
			manager.applyChange(addAxiomChange);
          
//			System.out.println("<create> ["+subject+", " + verb + ", " + object +"]");
//      	manager.saveOntology(ontology, new SystemOutDocumentTarget());
      	
//		} catch (OWLOntologyStorageException e) {
//          System.out.println("Could not save ontology: " + e.getMessage());
//      }
	}
	
	public synchronized void deleteIndividual(String subject, String verb, String object) {
//		try {
			OWLIndividual subjectIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyID.getOntologyIRI() + "#" + subject));
			OWLIndividual objectIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyID.getOntologyIRI() + "#" + object));

			OWLObjectProperty objectProperty = dataFactory.getOWLObjectProperty(IRI.create(ontologyID.getOntologyIRI() + "#" + verb));

          if( objectProperty!=null ) {
              OWLObjectPropertyAssertionAxiom assertion1 = dataFactory.getOWLObjectPropertyAssertionAxiom(objectProperty, subjectIndividual, objectIndividual);
              manager.removeAxiom(ontology, assertion1);
          }
          
//          System.out.println("<delete> ["+subject+", " + verb + ", " + object +"]");
//      	manager.saveOntology(ontology, new SystemOutDocumentTarget());
//      	
//		} catch (OWLOntologyStorageException e) {
//          System.out.println("Could not save ontology: " + e.getMessage());
//      }
	}
	
	public static void print(Node<OWLClass> parent, OWLReasoner reasoner, int depth) {
		if (parent.isBottomNode()) {
			return;
		}
		printIndent(depth);
		printNode(parent);
		for (Node<OWLClass> child : reasoner.getSubClasses(parent.getRepresentativeElement(), true)) {
			print(child, reasoner, depth + 1);
		}
	}

	public static void printIndent(int depth) {
		for (int i = 0; i < depth; i++) {
			System.out.print("    ");
		}
	}

	public static void printNode(Node<OWLClass> node) {
		System.out.print("{");
		for (Iterator<OWLClass> it = node.getEntities().iterator(); it.hasNext();) {
			OWLClass cls = it.next();
			System.out.print(pm.getShortForm(cls));
			if (it.hasNext()) {
				System.out.print(" ");
			}
		}
		System.out.println("}");
	}

	public String getNamespace() {
		return namespace;
	}

	public OWLOntology getOntology() {
		return ontology;
	}

	public OWLOntologyManager getManager() {
		return manager;
	}

	public OWLReasoner getReasoner() {
		return reasoner;
	}

	public OWLDataFactory getFactory() {
		return factory;
	}
	
}
