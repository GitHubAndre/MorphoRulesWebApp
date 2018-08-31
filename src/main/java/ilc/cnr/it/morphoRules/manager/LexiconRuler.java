/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilc.cnr.it.morphoRules.manager;

import ilc.cnr.it.morphoRules.controller.BaseController;
import java.io.File;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;

/**
 *
 * @author andrea
 */
public class LexiconRuler extends BaseController implements Serializable {

    private final String LEMON_NS = "http://lemon-model.net/lemon";
    private final String LEXINFO_NS = "http://www.lexinfo.net/ontology/2.0/lexinfo";
    private final String LEXICON_NS = "http://italianMorphology";

    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLDataFactory factory;
    private PrefixManager pm;
    private SWRLRuleEngine ruleEngine;

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

    public SWRLRuleEngine getRuleEngine() {
        return ruleEngine;
    }

    public void setRuleEngine(SWRLRuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    public LexiconRuler() {
        try {
            manager = OWLManager.createOWLOntologyManager();
            ontology = manager.loadOntologyFromOntologyDocument(new File("/tmp/psc-m.owl"));
            ruleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology);
            factory = manager.getOWLDataFactory();
            setPrefixes();
        } catch (OWLOntologyCreationException ex) {
            Logger.getLogger(Lexicon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPrefixes() {
        pm = new DefaultPrefixManager();
        pm.setPrefix("lexicon", LEXICON_NS);
        pm.setPrefix("lemon", LEMON_NS);
        pm.setPrefix("lexinfo", LEXINFO_NS);
    }
    
    public void setRules() {
        try {
            ruleEngine.createSWRLRule("r1", "Person(?p)^hasAge(?p,?age)^swrlb:greaterThan(?age,17) -> Adult(?p)");
        } catch (SWRLParseException | SWRLBuiltInException ex) {
            Logger.getLogger(LexiconRuler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void infer() {
        ruleEngine.infer();
    }

}
