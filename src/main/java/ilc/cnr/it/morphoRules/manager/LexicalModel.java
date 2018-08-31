/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilc.cnr.it.morphoRules.manager;

import ilc.cnr.it.morphoRules.controller.BaseController;
import java.io.IOException;
import java.io.InputStream;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 *
 * @author andrea
 */
public class LexicalModel extends BaseController {

    // http://lemon-model.net/lemon#
    // http://www.lexinfo.net/ontology/2.0/lexinfo#
//    private final String LEMON_NS = "http://ditmao.ilc.cnr.it:8082/DitamoOntologies/lemon.rdf";
//    private final String LEXINFO_NS = "http://ditmao.ilc.cnr.it:8082/DitamoOntologies/lexinfo.owl";
//    private final String LEXICON_NS = "http://italianMorphology";
//    private final String DITMAO_LEMON_NS = "http://ditmao.ilc.cnr.it/ditmaoLemon.owl";

    private final String LEMON_NS = "http://lemon-model.net/lemon";
    private final String LEXINFO_NS = "http://www.lexinfo.net/ontology/2.0/lexinfo";
    private final String LEXICON_NS = "http://italianMorphology";
    
    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLDataFactory factory;

    private PrefixManager pm;

    public OWLOntologyManager getManager() {
        return manager;
    }

    public void setManager(OWLOntologyManager manager) {
        this.manager = manager;
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public void setOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }

    public OWLDataFactory getFactory() {
        return factory;
    }

    public void setFactory(OWLDataFactory factory) {
        this.factory = factory;
    }

    public LexicalModel() {
        manager = OWLManager.createOWLOntologyManager();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("italianMorpho.owl")) {
            ontology = manager.loadOntologyFromOntologyDocument(inputStream);
            factory = manager.getOWLDataFactory();
            setPrefixes();
        } catch (OWLOntologyCreationException | IOException ex) {
        }
    }

    private void setPrefixes() {
        pm = new DefaultPrefixManager();
        pm.setPrefix("lexicon", LEXICON_NS);
        pm.setPrefix("lemon", LEMON_NS);
        pm.setPrefix("lexinfo", LEXINFO_NS);
    }
    
    public void createLexicalEntry(String word, String pos) {
        
    }

}
