package ontology.swrl;

import java.io.FileNotFoundException;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * Hello world!
 *
 */
public class Demo 
{
    public static void main( String[] args ) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException
    {
        OntologyCreator ontologyCreator = new OntologyCreator();
        ontologyCreator.modifyOntology("ontology/sl_travel_owl.owl");
        
    }
}
