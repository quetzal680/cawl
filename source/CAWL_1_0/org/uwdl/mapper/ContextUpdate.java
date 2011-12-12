package org.uwdl.mapper;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class ContextUpdate {
//	private ContextOntology contextOntology = null;
//	
//	private OWLOntologyManager manager = ContextOntology.getInstance().getManager();
//	
//	private OWLOntologyID ontologyID = ontology.getOntologyID();
//	private OWLDataFactory dataFactory = manager.getOWLDataFactory();
//	
//	public ContextUpdate(ContextOntology contextOntology) {
//		
//	}
//	
//	public void updateIndividual(String subject, String verb, String object) {
////        try {
//			OWLIndividual subjectIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyID.getOntologyIRI() + "#" + subject));
//            OWLIndividual objectIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyID.getOntologyIRI() + "#" + object));
//
//            OWLObjectProperty objectProperty = dataFactory.getOWLObjectProperty(IRI.create(ontologyID.getOntologyIRI() + "#" + verb));
//
//         // Finally, remove the axiom to our ontology and save
//            OWLIndividual deleteIndividual = ContextComparator.getInstance().individualReasoner(subject, verb);
//            if( deleteIndividual!=null ) {
//                OWLObjectPropertyAssertionAxiom assertion1 = dataFactory.getOWLObjectPropertyAssertionAxiom(objectProperty, subjectIndividual, deleteIndividual);
//                manager.removeAxiom(ontology, assertion1);
//            }
//            
//            // Now create the actual assertion (triple), as an object property assertion axiom
//            // subjectIndividual --> objectProperty --> objectIndividual
//
//            // Finally, add the axiom to our ontology and save
//            OWLObjectPropertyAssertionAxiom assertion2 = dataFactory.getOWLObjectPropertyAssertionAxiom(objectProperty, subjectIndividual, objectIndividual);
//            AddAxiom addAxiomChange = new AddAxiom(ontology, assertion2);
//            manager.applyChange(addAxiomChange);
//
//            System.out.println("<update> ["+subject+", " + verb + ", " + object +"]");
//            // Save our ontology
////        	manager.saveOntology(ontology, new SystemOutDocumentTarget());
////        }
////        catch (OWLOntologyCreationException e) {
////            System.out.println("Could not create ontology: " + e.getMessage());
////        } catch (OWLOntologyStorageException e) {
////            System.out.println("Could not save ontology: " + e.getMessage());
////        }
//	}
//
//	public void createIndividual(String subject, String verb, String object) {
////		try {
//			OWLIndividual subjectIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyID.getOntologyIRI() + "#" + subject));
//			OWLIndividual objectIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyID.getOntologyIRI() + "#" + object));
//
//			OWLObjectProperty objectProperty = dataFactory.getOWLObjectProperty(IRI.create(ontologyID.getOntologyIRI() + "#" + verb));
//
//			OWLObjectPropertyAssertionAxiom assertion2 = dataFactory.getOWLObjectPropertyAssertionAxiom(objectProperty, subjectIndividual, objectIndividual);
//			AddAxiom addAxiomChange = new AddAxiom(ontology, assertion2);
//			manager.applyChange(addAxiomChange);
//            
////        	manager.saveOntology(ontology, new SystemOutDocumentTarget());
//        	
////		} catch (OWLOntologyStorageException e) {
////            System.out.println("Could not save ontology: " + e.getMessage());
////        }
//	}
//	
//	public void deleteIndividual(String subject, String verb, String object) {
////		try {
//			OWLIndividual subjectIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyID.getOntologyIRI() + "#" + subject));
//			OWLIndividual objectIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyID.getOntologyIRI() + "#" + object));
//
//			OWLObjectProperty objectProperty = dataFactory.getOWLObjectProperty(IRI.create(ontologyID.getOntologyIRI() + "#" + verb));
//
//            if( objectProperty!=null ) {
//                OWLObjectPropertyAssertionAxiom assertion1 = dataFactory.getOWLObjectPropertyAssertionAxiom(objectProperty, subjectIndividual, objectIndividual);
//                manager.removeAxiom(ontology, assertion1);
//            }
//            
////        	manager.saveOntology(ontology, new SystemOutDocumentTarget());
//        	
////		} catch (OWLOntologyStorageException e) {
////            System.out.println("Could not save ontology: " + e.getMessage());
////        }
//	}
//	
////	public static void main(String[] args) {
////		ContextUpdate contextUpdate = new ContextUpdate();
////		contextUpdate.createIndividual("Person", "Jung");
////	}
}
