package org.uwdl.mapper;

import java.util.Iterator;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class ContextComparator {
	private String namespace = "http://ss.ssu.ac.kr/Ontology.owl";
	private static String DOCUMENT_IRI;
	private static DefaultPrefixManager pm = new DefaultPrefixManager("http://owl.man.ac.uk/2005/07/sssw/people#");
	private OWLOntologyManager manager = null;
	private OWLReasoner reasoner = null;
	
	public ContextComparator(String documentIRI) {
		this.DOCUMENT_IRI = documentIRI;
		
		try {
			initOntology();
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}
	
	private static void print(Node<OWLClass> parent, OWLReasoner reasoner, int depth) {
		if (parent.isBottomNode()) {
			return;
		}
		printIndent(depth);
		printNode(parent);
		for (Node<OWLClass> child : reasoner.getSubClasses(parent.getRepresentativeElement(), true)) {
			print(child, reasoner, depth + 1);
		}
	}

	private static void printIndent(int depth) {
		for (int i = 0; i < depth; i++) {
			System.out.print("    ");
		}
	}

	private static void printNode(Node<OWLClass> node) {
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
	
	private void initOntology() throws OWLOntologyCreationException {
		// Create our ontology manager in the usual way.
		manager = OWLManager.createOWLOntologyManager();

		// loaded ontology
		IRI docIRI = IRI.create(DOCUMENT_IRI);
		OWLOntology ont = manager.loadOntologyFromOntologyDocument(docIRI);
		System.out.println("Loaded " + ont.getOntologyID());

		// Create a console progress monitor.
		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		
		// Create a reasoner
		OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
		reasoner = reasonerFactory.createReasoner(ont, config);

		// Ask the reasoner to do all the necessary work now
		reasoner.precomputeInferences();

		// We can determine if the ontology is actually consistent (in this case, it should be).
		boolean consistent = reasoner.isConsistent();
		System.out.println("Consistent: " + consistent);
		System.out.println("\n");

		Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
		Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
		if (!unsatisfiable.isEmpty()) {
			System.out.println("The following classes are unsatisfiable: ");
			for (OWLClass cls : unsatisfiable) {
				System.out.println("    " + cls);
			}
		} else {
			System.out.println("There are no unsatisfiable classes\n");
		}
		
		// Finally, let's print out the class hierarchy.
		Node<OWLClass> topNode = reasoner.getTopClassNode();
		print(topNode, reasoner, 0);
		System.out.println("");
	}
	
	public boolean reasoner(String individual, String objectProperty, String value) {
		boolean result = false;
		
		OWLDataFactory fac = manager.getOWLDataFactory();
		OWLNamedIndividual owlNamedIndividual = fac.getOWLNamedIndividual(IRI.create(namespace+"#"+individual));
		OWLObjectProperty owlObjectProperty = fac.getOWLObjectProperty(IRI.create(namespace+"#"+objectProperty));
		
		System.out.println(owlNamedIndividual.getIRI());
		System.out.println(owlObjectProperty.getIRI());
		
		// Now ask the reasoner for the has_pet property values for Mick
		NodeSet<OWLNamedIndividual> locateValuesNodeSet = reasoner.getObjectPropertyValues(owlNamedIndividual, owlObjectProperty);
		Set<OWLNamedIndividual> vs = locateValuesNodeSet.getFlattened();
		System.out.println("The "+objectProperty+" property values for "+individual+" are: ");
		for (OWLNamedIndividual ind : vs) {
			if( ind.getIRI().equals(IRI.create(namespace+"#"+value)) ) {
				System.out.println("    " + ind);
				return true;
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		try {
			ContextComparator contextOntology = new ContextComparator(DOCUMENT_IRI);
			contextOntology.initOntology();
			if( contextOntology.reasoner("Tom", "locatedIn", "Seoul") ) {
				 System.out.println("    true");
			}
			

		} catch (UnsupportedOperationException exception) {
			System.out.println("Unsupported reasoner operation.");
		} catch (OWLOntologyCreationException e) {
			System.out.println("Could not load the pizza ontology: " + e.getMessage());
		}
	}
}
