/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilc.cnr.it.morphoRules.controller;

//import ilc.cnr.it.morphoRules.manager.LexicalModel;
import ilc.cnr.it.morphoRules.manager.Form;
import ilc.cnr.it.morphoRules.manager.Legend;
import ilc.cnr.it.morphoRules.manager.Lexicon;
import ilc.cnr.it.morphoRules.manager.MorphoClass;
import ilc.cnr.it.morphoRules.manager.ResultTable;
import ilc.cnr.it.morphoRules.manager.Rule;
import ilc.cnr.it.morphoRules.manager.TraitComparator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryParseException;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;

/**
 *
 * @author andrea
 */
@ViewScoped
@Named
public class DataInputController extends BaseController implements Serializable {

    private final String sparqlService = "http://lari-datasets.ilc.cnr.it/pscMorphTest/query";

    private String prefix = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX psc: <http://lari-datasets.ilc.cnr.it/pscMorphTest#>\n"
            + "PREFIX lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#>\n"
            + "PREFIX lemon: <http://lemon-model.net/lemon#>\n";

    private String queryForLemma = "SELECT ?le ?c WHERE {\n"
            + " ?le lemon:writtenRep \"_WORD_\" .\n"
            + " ?le lexinfo:partOfSpeech lexinfo:_POS_ .\n"
            + " ?le psc:_CLASS_ ?c \n"
            + "} ";

    private String queryForPoSClasses = "SELECT ?c \n"
            + "WHERE {\n"
            + "  ?c a psc:_CLASS_\n"
            + "}";

    private String queryForClassExamples = "SELECT ?ex \n"
            + "WHERE {\n"
            + "  ?lex psc:has_PROPERTY_ psc:_CLASS_ . \n"
            + "  ?lex lemon:writtenRep ?ex \n"
            + "} LIMIT 2";

    private String queryForForms = "SELECT DISTINCT ?p ?infl\n"
            + "WHERE {\n"
            + "  ?le lemon:writtenRep \"_LEMMA_\" .\n"
            + "  ?le ?p ?infl .\n"
            + "  ?p rdfs:subPropertyOf psc:has_TRAIT_MorphologicalTrait\n"
            + "}";

    private ArrayList<Legend> traitLegend = new ArrayList();

    private ArrayList<MorphoClass> morphoClassesList = new ArrayList();
    private ArrayList<String> classList = new ArrayList();
    private ArrayList<Form> forms = new ArrayList();
    private String selectedMorphoClass = "";
    private String classSearchResultLabel;
    private String word = "";
    private String pos = "";
    private boolean isNeologism = false;

    public enum Constants {
        NO_DESCRIPTION_AVAILABLE("No class description available."),
        IS_NEOLOGISM("<p class='textMsgBlock'> The lemma you entered <b>is not already</b> in the dataset. Please, <b>select a derivational class</b> and click on the <b>generate button</b>. </p>"),
        IS_EXISTING_LEMMA("<p class='textMsgBlock'> The lemma <b>already exists</b>. If you want to know how it flexes, please <b>select its class</b> and click on the <b>generate button</b>.</p>");

        private String message;

        Constants(String message) {
            this.message = message;
        }

        public String message() {
            return message;
        }
    }

    public boolean isIsNeologism() {
        return isNeologism;
    }

    public ArrayList<Form> getForms() {
        return forms;
    }

    public String getClassSearchResultLabel() {
        return classSearchResultLabel;
    }

    public void setClassSearchResultLabel(String classSearchResultLabel) {
        this.classSearchResultLabel = classSearchResultLabel;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSelectedMorphoClass() {
        return selectedMorphoClass;
    }

    public void setSelectedMorphoClass(String selectedMorphoClass) {
        this.selectedMorphoClass = selectedMorphoClass;
    }

    public ArrayList<String> getClassList() {
        return classList;
    }

    public void setClassList(ArrayList<String> classList) {
        this.classList = classList;
    }

    public ArrayList<Legend> getTraitLegend() {
        return traitLegend;
    }

    public void setTraitLegend(ArrayList<Legend> traitLegend) {
        this.traitLegend = traitLegend;
    }

    @PostConstruct
    public void init() {
        try (InputStream input = Rule.class.getResourceAsStream("/morphFeatLegend.txt")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line = null;
            while ((line = br.readLine()) != null) {
                String l[] = line.split("\t");
                Legend leg = new Legend(l[0], l[1] + " = " + l[2]);
                traitLegend.add(leg);
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reset(boolean search) {
        forms.clear();
        morphoClassesList.clear();
        classList.clear();
        selectedMorphoClass = "";
        classSearchResultLabel = "";
        isNeologism = false;
        if (!search) {
            word = "";
            pos = "";
        }
    }

    public void morphoClassSearcher() {
        reset(true);
        String propertyClass = "has" + (pos.equals("noun") ? "Noun" : (pos.equals("verb") ? "Verb" : "Adjective")) + "Class";
        String query = prefix + queryForLemma.replace("_WORD_", word.trim());
        query = query.replace("_POS_", pos.trim());
        query = query.replace("_CLASS_", propertyClass.trim());
        ArrayList<ResultTable> resultTable = execQuery(query);
        if (resultTable.size() > 0) {
            updateMorphoClassesList(resultTable);
            setClassSearchResultLabel(Constants.IS_EXISTING_LEMMA.message);
            isNeologism = false;
        } else {
            query = prefix + queryForPoSClasses.replace("_CLASS_", (pos.equals("noun") ? "NounClass" : (pos.equals("verb") ? "VerbClass" : "AdjectiveClass")));
            resultTable.clear();
            resultTable = execQuery(query);
            updateMorphoClassesList(resultTable);
            setClassSearchResultLabel(Constants.IS_NEOLOGISM.message);
            isNeologism = true;
        }
    }

    private ArrayList<ResultTable> execQuery(String query) {
        ArrayList<ResultTable> resultTable = new ArrayList();
        ArrayList<String> vars = new ArrayList();
        try {
            QueryExecution q = QueryExecutionFactory.sparqlService(sparqlService, query);
            ResultSet results = q.execSelect();
            setVariables(q, vars);
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                ResultTable row = new ResultTable();
                for (String var : vars) {
                    RDFNode n = soln.get(var);
                    if (n.isLiteral()) {
                        if (n.asLiteral().getDatatypeURI().contains("integer")) {
                            row.setRow(var, n.toString().split("\\^\\^")[0]);
                        } else {
                            row.setRow(var, n.asLiteral().getString());
                        }
                    } else {
                        row.setRow(var, n.asResource().getLocalName());
                    }
                }
                resultTable.add(row);
            }
//            createColumns(vars);
        } catch (QueryExceptionHTTP | QueryParseException qe) {
        }
        return resultTable;
    }

    private void setVariables(QueryExecution q, ArrayList<String> vars) {
        for (Var var : q.getQuery().getProjectVars()) {
            vars.add(var.getVarName());
        }
    }

    private void updateMorphoClassesList(ArrayList<ResultTable> rt) {
        morphoClassesList.clear();
        classList.clear();
        for (ResultTable _rt : rt) {
            String clazz = _rt.getRow().get("c");
            String query = prefix + queryForClassExamples.replace("_PROPERTY_",
                    (getPos().equals("noun") ? "NounClass" : (getPos().equals("verb") ? "VerbClass" : "AdjectiveClass"))).replace("_CLASS_", clazz);
            ArrayList<ResultTable> resultTable = execQuery(query);
            String examples = "";
            for (ResultTable rtb : resultTable) {
                examples = examples + rtb.getRow().get("ex") + " ";
            }
            examples = "Examples from this class: " + examples.trim().replace(" ", ", ");
            MorphoClass c = new MorphoClass(clazz, examples + " . . .", 0);
            classList.add(clazz);
            morphoClassesList.add(c);
        }
    }

    // ORIGINAL
//    private void updateMorphoClassesList(ArrayList<ResultTable> rt) {
//        morphoClassesList.clear();
//        classList.clear();
//        for (ResultTable _rt : rt) {
//            if (!(_rt.getRow().get("c").contains("A_AClass")) && !(_rt.getRow().get("c").contains("V_VClass"))) {
//                String clazz = _rt.getRow().get("c");
//                String splitClazz[] = clazz.split("Class");
//
//                String query = prefix + queryForClassExamples.replace("_PROPERTY_",
//                        (splitClazz[0].equals("N") ? "NounClass" : (splitClazz[0].equals("V") ? "VerbClass" : "AdjectiveClass"))).replace("_CLASS_", clazz);
//                ArrayList<ResultTable> resultTable = execQuery(query);
//                String examples = "";
//                for (ResultTable rtb : resultTable) {
//                    examples = examples + rtb.getRow().get("ex") + " ";
//                }
//                examples = "Examples from this class: " + examples.trim().replace(" ", ", ");
//                MorphoClass c = new MorphoClass(clazz, examples + " . . .", 0);
//                classList.add(clazz);
//                morphoClassesList.add(c);
//            }
//        }
//    }
    public String getMorphoClassDescription() {
        for (MorphoClass mc : morphoClassesList) {
            if (mc.getName().equals(selectedMorphoClass)) {
                return mc.getDefinition();
            }
        }
        return Constants.NO_DESCRIPTION_AVAILABLE.message;
    }

    public void generateForms() {
        forms.clear();
        if (isNeologism) {
//            Lexicon itaLex = new Lexicon();
//            setLexicalEntry(itaLex);
//            LexiconRuler lexRuler = new LexiconRuler();
//            lexRuler.setRules();
//            lexRuler.infer();
//            rtl = itaLex.getLexicalForms(lexRuler.getManager(), lexRuler.getOntology(), query);
            getNeologismForms(Rule.getFlexedForms(word, pos, selectedMorphoClass));
        } else {
            String query = prefix + queryForForms.replace("_LEMMA_", word.trim()).replace("_TRAIT_", (pos.equals("noun") ? "N" : (pos.equals("adjective") ? "A" : "V")));
            ArrayList<ResultTable> rtl = execQuery(query);
            getForms(rtl);
        }

    }

    private void setLexicalEntry(Lexicon itaLex) {
        itaLex.setPrefixes();
        itaLex.addImports();
        itaLex.createLexicon();
        itaLex.createLexicalEntry(word, pos);
        itaLex.save();
    }

    public void morphoClassSelected() {
        int i = 0;
    }

    private void getNeologismForms(ArrayList<Form> f) {
        for (Form _f : f) {
            _f.setMorphoDescription(setLegend(_f.getMorphoTag()));
        }
        forms.addAll(f);
    }

    private void getForms(ArrayList<ResultTable> rtl) {
        for (ResultTable rt : rtl) {
            Form f = new Form();
            for (Map.Entry<String, String> entry : rt.getRow().entrySet()) {
                switch (entry.getKey()) {
                    case "p":
                        f.setMorphoTag(entry.getValue());
                        break;
                    case "infl":
                        setFlexions(f, entry.getValue());
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid key: " + entry.getKey());
                }
            }
            f.setMorphoDescription(setLegend(f.getMorphoTag()));
            forms.add(f);
        }
    }

    private void setFlexions(Form f, String s) {
        s = accentRecover(s);
        String[] substrings = s.split("(?=\\p{Upper})|À|È|Ì|Ò|Ù");
        f.setStem(substrings[0].toLowerCase());
        f.setFlexion(s.replace(substrings[0], "").toLowerCase());
    }

    private String accentRecover(String s) {
        if (s.endsWith("A'")) {
            return s.replace("A'", "À");
        } else {
            if (s.endsWith("E'")) {
                return s.replace("E'", "È");
            } else {
                if (s.endsWith("I'")) {
                    return s.replace("I'", "Ì");
                } else {
                    if (s.endsWith("O'")) {
                        return s.replace("O'", "Ò");
                    } else {
                        if (s.endsWith("U'")) {
                            return s.replace("U'", "Ù");
                        }
                    }
                }
            }
        }
        return s;
    }

    private String setLegend(String morphoTag) {
        String legend = "";
        for (Legend l : traitLegend) {
            if (l.getName().trim().equals(morphoTag.trim().split("has")[1])) {
                legend = legend + l.getValue() + ", ";
            }
        }
        return legend;
    }

}
