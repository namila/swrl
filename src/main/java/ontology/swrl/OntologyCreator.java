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
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

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
		
		
		
		
		// Saving new file
		OWLXMLDocumentFormat ontologyFormat = new OWLXMLDocumentFormat();
		ontologyFormat.copyPrefixesFrom(defaultPrefixManager);
		ontologyManager.saveOntology(slTravelOntology, ontologyFormat, IRI.create(new File("ontology/sl_travel_owl_modified.owl").toURI()));
		
	
		
		System.out.println("Done");
	}
	
    private static OWLNamedIndividual createIndividual(OWLOntology ontology, DefaultPrefixManager pm, OWLOntologyManager manager, String name) {
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(name, pm);
        manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(individual));
        return individual;
    }
	
	

}
