package ontology.swrl;

import java.io.FileNotFoundException;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.sqwrl.exceptions.SQWRLException;

/**
 * Hello world!
 *
 */
public class Demo 
{
    public static void main( String[] args ) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException, SQWRLException, SWRLParseException
    {
        OntologyCreator ontologyCreator = new OntologyCreator();
        //ontologyCreator.modifyOntology("ontology/sl_travel_owl.owl");
        ontologyCreator.queryOntology("ontology/sl_travel_owl_modified.owl");
        
    }
}
