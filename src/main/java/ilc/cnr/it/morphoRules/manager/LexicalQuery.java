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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

/**
 *
 * @author andrea
 */
public class LexicalQuery extends BaseController {

    private OWLOntologyManager ontologyManager;
    private OWLOntology ontology;
    private OWLDataFactory owlDataFactory;

    StructuralReasonerFactory reasonerFactory = null;
    OWLReasoner reasoner = null;

    // query prefixes
    public final static String QUERY_PREFIXES = "PREFIX lexicon: <http://italianMorphology#>\n"
            + "PREFIX lemon: <http://lemon-model.net/lemon#>\n"
            + "PREFIX lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#>\n";

    // queries
    public final static String GET_MORPHO_LEGEND
            = "SELECT ?dp \n"
            + "WHERE { DataProperty(?dp) }";

    public LexicalQuery(LexicalModel lm) {
        ontologyManager = lm.getManager();
        ontology = lm.getOntology();
        owlDataFactory = lm.getFactory();
        reasonerFactory = new StructuralReasonerFactory();
        reasoner = reasonerFactory.createReasoner(ontology);
    }

    public OWLOntologyManager getManager() {
        return ontologyManager;
    }

    public void setManager(OWLOntologyManager manager) {
        this.ontologyManager = manager;
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public void setOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }

    public OWLDataFactory getFactory() {
        return owlDataFactory;
    }

    public void setFactory(OWLDataFactory factory) {
        this.owlDataFactory = factory;
    }

    public Map<String, MorphoClass> getMorphoClassMap() {
//        return getList(processQuery(QUERY_PREFIXES + GET_LEMMA_INFO));
        return null;
    }

    public Map<String, String> getMorphoLegendMap() {
        Map<String, String> legend = new HashMap<>();
        for (Map<String, String> m : processQuery(QUERY_PREFIXES + GET_MORPHO_LEGEND)) {
            legend.put(m.get("dp"), "puppa");
        }
        return legend;
    }

    /*  private ArrayList<String> getList(List<Map<String, String>> l) {
        ArrayList<String> al = new ArrayList();
        for (Map<String, String> m : l) {
            for (Map.Entry<String, String> entry : m.entrySet()) {
                al.add(entry.getValue());
            }
        }
        if (al.isEmpty()) {
            al.add(NO_ENTRY_FOUND);
        }
        return al;
    }*/
    public List<Map<String, String>> processQuery(String q) {
        QueryResult result = null;
        QueryEngine engine = QueryEngine.create(ontologyManager, reasoner);
        Query query;
        try {
            query = Query.create(q);
            result = engine.execute(query);

        } catch (QueryEngineException | QueryParserException ex) {
        }
        return getQueryResults(result);
    }

    private List<Map<String, String>> getQueryResults(QueryResult qr) {
        List<Map<String, String>> resultsList = new ArrayList<>();
        if (qr != null) {
            Iterator<QueryBinding> itr = qr.iterator();
            if (itr.hasNext()) {
                while (itr.hasNext()) {
                    QueryBinding qb = itr.next();
                    Set<QueryArgument> keys = qb.getBoundArgs();
                    Map<String, String> map = new HashMap<>();
                    for (QueryArgument key : keys) {
                        switch (qb.get(key).getType().name()) {
                            case "LITERAL":
                                map.put(key.getValueAsVar().getName(), qb.get(key).getValueAsLiteral().getLiteral());
                                break;
                            case "URI":
                                map.put(key.getValueAsVar().getName(), qb.get(key).getValueAsIRI().getShortForm());
                                break;
                            default:
                        }
                    }
                    resultsList.add(map);
                }
            } else {
            }
        } else {
        }
        return resultsList;
    }
}
