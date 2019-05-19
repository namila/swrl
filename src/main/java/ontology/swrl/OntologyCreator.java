package ontology.swrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.factory.DefaultIRIResolver;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.sqwrl.SQWRLResult;
import org.swrlapi.sqwrl.exceptions.SQWRLException;

public class OntologyCreator {
	
	void modifyOntology(String filePath) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException {
		
	    String DOCUMENT_IRI = "http://travel.org/ontologies/sl_travel";
	    
		InputStream inputStream = new FileInputStream(new File(filePath));
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		OWLOntology slTravelOntology = ontologyManager.loadOntologyFromOntologyDocument(inputStream);
	
		OWLDataFactory slTravelDataFactory = ontologyManager.getOWLDataFactory();
		
		// Setting Prefixes
		DefaultPrefixManager defaultPrefixManager = new DefaultPrefixManager();
		defaultPrefixManager.setDefaultPrefix(DOCUMENT_IRI+ "#");
		
		
		// Defining Sigiriya individual
		OWLNamedIndividual sigiriya = createIndividual(slTravelOntology, defaultPrefixManager, ontologyManager, ":Sigiriya");
		
		// Adding data properties
		OWLDataProperty placeNameProperty = slTravelDataFactory.getOWLDataProperty(":PlaceName", defaultPrefixManager);
		String placeNameValue = "Sigiriya";
		ontologyManager.addAxiom(slTravelOntology, slTravelDataFactory.getOWLDataPropertyAssertionAxiom(placeNameProperty, sigiriya, placeNameValue));
		
		OWLDataProperty distanceFromCapitalProperty = slTravelDataFactory.getOWLDataProperty(":DistanceFromCapital", defaultPrefixManager);
		String distanceFromCapitalValue = "150";
		ontologyManager.addAxiom(slTravelOntology, slTravelDataFactory.getOWLDataPropertyAssertionAxiom(distanceFromCapitalProperty, sigiriya, distanceFromCapitalValue));
		
		OWLDataProperty locationProperty = slTravelDataFactory.getOWLDataProperty(":Location", defaultPrefixManager);
		String locationValue = "Dambulla";
		ontologyManager.addAxiom(slTravelOntology, slTravelDataFactory.getOWLDataPropertyAssertionAxiom(locationProperty, sigiriya, locationValue));
		
		OWLDataProperty locationTypeProperty = slTravelDataFactory.getOWLDataProperty(":LocationType", defaultPrefixManager);
		String locationTypeValue = "Memorial";
		ontologyManager.addAxiom(slTravelOntology, slTravelDataFactory.getOWLDataPropertyAssertionAxiom(locationTypeProperty, sigiriya, locationTypeValue));
		
		// Assigning the class
		OWLClass memorialClass = slTravelDataFactory.getOWLClass(":Memorial", defaultPrefixManager);
		ontologyManager.addAxiom(slTravelOntology, slTravelDataFactory.getOWLClassAssertionAxiom(memorialClass, sigiriya));
		
		
		
		// Defining Romantic Trip to Dambulla individual
		OWLNamedIndividual tripToDambulla = createIndividual(slTravelOntology, defaultPrefixManager, ontologyManager, ":Romantic_Trip_to_Dambulla");
		
		OWLDataProperty tripNameProperty = slTravelDataFactory.getOWLDataProperty(":TripName", defaultPrefixManager);
		String tripNameValue = "Romantic Trip to Dambulla";
		ontologyManager.addAxiom(slTravelOntology, slTravelDataFactory.getOWLDataPropertyAssertionAxiom(tripNameProperty, tripToDambulla, tripNameValue));
		
		OWLDataProperty noOfPeopleProperty = slTravelDataFactory.getOWLDataProperty(":NoOfPeople", defaultPrefixManager);
		String noOfPeopleValue = "2";
		ontologyManager.addAxiom(slTravelOntology, slTravelDataFactory.getOWLDataPropertyAssertionAxiom(noOfPeopleProperty, tripToDambulla, noOfPeopleValue));
		
		// Assigning the class
		OWLClass romanticTripClass = slTravelDataFactory.getOWLClass(":RomanticTrip", defaultPrefixManager);
		ontologyManager.addAxiom(slTravelOntology, slTravelDataFactory.getOWLClassAssertionAxiom(romanticTripClass, tripToDambulla));
		
		
		// Adding Sigiriya as a destination to dambulla trip
		OWLObjectProperty destinationAtPropery = slTravelDataFactory.getOWLObjectProperty(":DestinationAt", defaultPrefixManager);
		ontologyManager.addAxiom(slTravelOntology,slTravelDataFactory.getOWLObjectPropertyAssertionAxiom(destinationAtPropery, tripToDambulla, sigiriya));
		
		
		// Saving new file
		OWLXMLDocumentFormat ontologyFormat = new OWLXMLDocumentFormat();
		ontologyFormat.copyPrefixesFrom(defaultPrefixManager);
		ontologyManager.saveOntology(slTravelOntology, ontologyFormat, IRI.create(new File("ontology/sl_travel_owl_modified.owl").toURI()));
			
		
		System.out.println("Done");
	}
	
	void queryOntology(String filePath) throws FileNotFoundException, OWLOntologyCreationException, SQWRLException, SWRLParseException {
		String DOCUMENT_IRI = "http://travel.org/ontologies/sl_travel";
		InputStream inputStream = new FileInputStream(new File(filePath));
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		OWLOntology slTravelOntology = ontologyManager.loadOntologyFromOntologyDocument(inputStream);
		//ontologyManager.getOntologyFormat(slTravelOntology).asPrefixOWLOntologyFormat().setDefaultPrefix(DOCUMENT_IRI+ "#");

		
		//DefaultIRIResolver defaultResolver = new DefaultIRIResolver(DOCUMENT_IRI+"#");
		SQWRLQueryEngine sqwrlQueryEngine = SWRLAPIFactory.createSQWRLQueryEngine(slTravelOntology);
		
		// What are the memorials in Dambulla?
		SQWRLResult result1 = sqwrlQueryEngine.runSQWRLQuery("Q1","#Memorial(?memorial) ^ #Location(?memorial, ?location) ^ swrlb:stringEqualIgnoreCase(?location, \"Dambulla\") -> sqwrl:select(?memorial)");
		System.out.println(result1);	
//		while(result1.next()) {
//		  System.out.println(result1);	
//		}
		
		// What are the trips which include Sigiriya?
//		SQWRLResult result2 = sqwrlQueryEngine.runSQWRLQuery("Query2","#Trip(?trip) ^ #DestinationAt(?trip, #Sigiriya) -> sqwrl:select(?trip)");
//		while(result2.next()) {
//			System.out.println(result2.getValue("trip"));
//		}
		
	}
	
	
	
    private static OWLNamedIndividual createIndividual(OWLOntology ontology, DefaultPrefixManager pm, OWLOntologyManager manager, String name) {
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(name, pm);
        manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(individual));
        return individual;
    }
	
	

}
