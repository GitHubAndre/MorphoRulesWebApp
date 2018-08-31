/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilc.cnr.it.morphoRules.manager;

import de.derivo.sparqldlapi.Query;
import de.derivo.sparqldlapi.QueryArgument;
import de.derivo.sparqldlapi.QueryBinding;
import de.derivo.sparqldlapi.QueryEngine;
import de.derivo.sparqldlapi.QueryResult;
import de.derivo.sparqldlapi.exceptions.QueryEngineException;
import de.derivo.sparqldlapi.exceptions.QueryParserException;
import ilc.cnr.it.morphoRules.controller.BaseController;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;

/**
 *
 * @author andrea
 */
public class Lexicon extends BaseController implements Serializable {

    private final String LEMON_NS = "http://lemon-model.net/lemon";
    private final String LEXINFO_NS = "http://www.lexinfo.net/ontology/2.0/lexinfo";
    private final String LEXICON_NS = "http://italianMorphology";

    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLDataFactory factory;
    private PrefixManager pm;
    
    private StructuralReasonerFactory reasonerFactory = null;
    private OWLReasoner reasoner = null;

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

    public Lexicon() {
        try {
            IRI ontologyIRI = IRI.create(LEXICON_NS);
            manager = OWLManager.createOWLOntologyManager();
            ontology = manager.createOntology(ontologyIRI);
            factory = manager.getOWLDataFactory();
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

    public void addImports() {
        OWLImportsDeclaration importLexinfoDeclaration = factory.getOWLImportsDeclaration(IRI.create("http://www.lexinfo.net/ontology/2.0/lexinfo"));
        manager.applyChange(new AddImport(ontology, importLexinfoDeclaration));
    }

    public void createLexicon() {
        OWLClass lexiconClass = factory.getOWLClass(IRI.create(LEMON_NS + "#Lexicon"));
        OWLNamedIndividual lex = factory.getOWLNamedIndividual(IRI.create(LEXICON_NS + "#italianLex"));
        addIndividualAxiom(lexiconClass, lex);
    }

    public void createLexicalEntry(String lemma, String partOfSpeech) {
        OWLClass lexEntry = factory.getOWLClass(IRI.create(LEMON_NS + "#LexicalEntry"));
        OWLNamedIndividual lexiconInd = factory.getOWLNamedIndividual(IRI.create(LEXICON_NS + "#italianLex"));
        OWLNamedIndividual i = factory.getOWLNamedIndividual(IRI.create(LEXICON_NS + "#" + lemma));
        OWLNamedIndividual pos = factory.getOWLNamedIndividual(IRI.create(LEXINFO_NS + "#" + partOfSpeech));
        addIndividualAxiom(lexEntry, i);
        addObjectPropertyAxiom("entry", lexiconInd, i, LEMON_NS);
        addObjectPropertyAxiom("PartOfSpeech", i, pos, LEXINFO_NS);
        addDataPropertyAxiom("writtenRep", i, lemma, LEMON_NS);
    }

    private void addIndividualAxiom(OWLClass c, OWLNamedIndividual i) {
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(c, i);
        manager.addAxiom(ontology, classAssertion);
    }

    private void addObjectPropertyAxiom(String objProp, OWLNamedIndividual src, OWLNamedIndividual trg, String propNS) {
        OWLObjectProperty p = factory.getOWLObjectProperty(IRI.create(propNS + "#" + objProp));
        OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(p, src, trg);
        manager.addAxiom(ontology, propertyAssertion);
    }

    private void addDataPropertyAxiom(String dataProp, OWLNamedIndividual src, String trg, String propNS) {
        OWLDataProperty p = factory.getOWLDataProperty(IRI.create(propNS + "#" + dataProp));
        OWLDataPropertyAssertionAxiom dataPropertyAssertion = factory.getOWLDataPropertyAssertionAxiom(p, src, trg);
        manager.addAxiom(ontology, dataPropertyAssertion);
    }

    private void addSuperDataPropertyAxiom(String dataProp, String superDataProp, String propNS) {
        OWLDataProperty p = factory.getOWLDataProperty(IRI.create(propNS + "#" + dataProp));
        OWLDataProperty superP = factory.getOWLDataProperty(IRI.create(propNS + "#" + superDataProp));
        OWLSubDataPropertyOfAxiom ax = factory.getOWLSubDataPropertyOfAxiom(p, superP);
        manager.addAxiom(ontology, ax);
    }

    public ArrayList<ResultTable> getLexicalForms(OWLOntologyManager m, OWLOntology o, String q) {
        ArrayList<ResultTable> rtl = null;
        reasonerFactory = new StructuralReasonerFactory();
        reasoner = reasonerFactory.createReasoner(o);
        try {
            QueryEngine engine = QueryEngine.create(m, reasoner);
            rtl = execQuery(engine.execute(Query.create(q)));
        } catch (QueryParserException | QueryEngineException ex) {
            Logger.getLogger(Lexicon.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rtl;
    }

    private ArrayList<ResultTable> execQuery(QueryResult qr) {
        ArrayList<ResultTable> resultTable = new ArrayList();
        if (qr != null) {
            Iterator<QueryBinding> itr = qr.iterator();
            if (itr.hasNext()) {
                while (itr.hasNext()) {
                    QueryBinding qb = itr.next();
                    Set<QueryArgument> keys = qb.getBoundArgs();
                    ResultTable row = new ResultTable();
                    for (QueryArgument key : keys) {
                        switch (qb.get(key).getType().name()) {
                            case "LITERAL":
                                row.setRow(key.getValueAsVar().getName(), qb.get(key).getValueAsLiteral().getLiteral());
                                break;
                            case "URI":
                                row.setRow(key.getValueAsVar().getName(), qb.get(key).getValueAsIRI().getShortForm());
                                break;
                            default:
                        }
                    }
                    resultTable.add(row);
                }
            }
        }
        return resultTable;
    }
    
    public void save() {
        try {
            manager.saveOntology(ontology, IRI.create(((new File("/tmp/psc-m.owl")).toURI())));
        } catch (OWLOntologyStorageException ex) {
            Logger.getLogger(Lexicon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
