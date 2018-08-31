/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilc.cnr.it.morphoRules.controller;

import ilc.cnr.it.morphoRules.manager.ResultTable;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.event.AjaxBehaviorEvent;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author andrea
 */
@ViewScoped
@Named

public class EditorController extends BaseController implements Serializable {

    private String query = "";
    private String currentQueryItemValue = "";
    private ArrayList<String> vars = new ArrayList();
    private String qMess = "";

//    PREFIX ontolex: <http://www.w3.org/ns/lemon/ontolex#>
    private String prefix = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
            + "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
            + "PREFIX lime: <http://www.w3.org/ns/lemon/lime#> \n"
            + "PREFIX ontolex: <http://www.w3.org/ns/lemon/ontolex#> \n"
            + "PREFIX polyLemon: <http://lari-datasets.ilc.cnr.it/polyLemon#> \n"
            + "PREFIX lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> \n"
            + "PREFIX lsj-lemon: <http://lari-datasets.ilc.cnr.it/lemonLSJ#> \n"
            + "PREFIX lsj: <http://lari-datasets.ilc.cnr.it/lsj#> \n\n";

    private String default_query = "SELECT ?x ?y ?z WHERE { ?x ?y ?z } LIMIT 10 ";

    private ArrayList<ResultTable> resultTable = new ArrayList();
    private List<ColumnModel> columns = new ArrayList();

    private final String sparqlService = "http://lari-datasets.ilc.cnr.it/lsj/sparql";

    public String getQuery() {
        return query.isEmpty() ? prefix + default_query : query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getqMess() {
        return qMess;
    }

    public void setqMess(String qMess) {
        this.qMess = qMess;
    }

    public ArrayList<ResultTable> getResultTable() {
        return resultTable;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getCurrentQueryItemValue() {
        return currentQueryItemValue;
    }

    public void setCurrentQueryItemValue(String currentQueryItemValue) {
        this.currentQueryItemValue = currentQueryItemValue;
    }

    public void clearQuery() {
        setQuery(prefix + default_query);
        setCurrentQueryItemValue("");
        resultTable.clear();
    }

    public void questionChanged(AjaxBehaviorEvent e) {
        String q = (String) e.getComponent().getAttributes().get("value");
        String query = "";
        if (q.equals("q1")) {
            query = "SELECT ?s ?wr\n"
                    + "WHERE {\n"
                    + "\t?s a ontolex:LexicalEntry; \n"
                    + "\tontolex:canonicalForm ?cf .\n"
                    + "\t?cf ontolex:writtenRep ?wr .\n"
                    + "}\n"
                    + "LIMIT 50";
        } else {
            if (q.equals("q2")) {
                query = "SELECT ?s ?wr\n"
                        + "WHERE {\n"
                        + "\t?s  lexinfo:partOfSpeech lexinfo:Noun; \n"
                        + "\tontolex:canonicalForm ?cf .\n"
                        + "\t?cf ontolex:writtenRep ?wr .\n"
                        + "}";
            } else {
                if (q.equals("q3")) {
                    query = "SELECT ?wr ?t WHERE {\n"
                            + "\t?l a ontolex:LexicalEntry; \n"
                            + "\tontolex:canonicalForm ?c;\n"
                            + "\tontolex:sense ?s .\n"
                            + "\t?c ontolex:writtenRep ?wr .\n"
                            + "\t?s lsj-lemon:usage <http://dbpedia.org/resource/Plato>; \n"
                            + "\tlexinfo:translation ?t }";
                } else {
                    if (q.equals("q4")) {
                        query = "SELECT (count(?s) as ?c)\n"
                                + "WHERE {\n"
                                + "\t?lsj lime:entry ?s\n"
                                + "}";
                    } else {
                        query = default_query;
                    }
                }
            }
        }
        setQuery(prefix + query);
    }

    public void execQuery() {
        resultTable.clear();
        qMess = "ok";
        try {
            QueryExecution q = QueryExecutionFactory.sparqlService(sparqlService, query);
            ResultSet results = q.execSelect();
            setVariables(q);
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                ResultTable row = new ResultTable();
                for (String var : vars) {
                    RDFNode n = soln.get(var);
                    if (n.isLiteral()) {
                        if (n.asLiteral().getDatatypeURI().contains("integer")) {
                            row.setRow(var, n.toString().split("\\^\\^")[0]);
                        } else {
                            row.setRow(var, n.toString());
                        }
                    } else {
                        row.setRow(var, n.toString());
                    }
                }
                resultTable.add(row);
            }
            createColumns(vars);
        } catch (QueryExceptionHTTP qe) {
            qMess = "qe";
            error("ERROR", "SPARQL server down", "ccccc");
        } catch (QueryParseException qp) {
            qMess = "qp";
            error("ERROR", "Malformed query", "ccccc");
        }
        if (resultTable.size() > 0) {
            info("Info", resultTable.size() + " result(s) found", "OK");
        } else {
            if (qMess.equals("ok")) {
                info("Info", "No results", "OK");
            }
        }
    }

    private void setVariables(QueryExecution q) {
        vars.clear();
        for (Var var : q.getQuery().getProjectVars()) {
            vars.add(var.getVarName());
        }
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public int getColumnsNumber() {
        return columns.size() - 1;
    }

    public void createColumns(ArrayList<String> template) {
        columns.clear();
        for (String t : template) {
            columns.add(new ColumnModel(t));
        }
    }

    public StreamedContent getCsv() throws UnsupportedEncodingException {
        InputStream stream;
        stream = new ByteArrayInputStream(getCSVResult().getBytes("UTF-8"));
        return new DefaultStreamedContent(stream, "text/csv", "results.csv");
    }

    private String getCSVResult() {
        StringBuilder result = new StringBuilder();
        for (ResultTable rt : resultTable) {
            Map<String, String> row = rt.getRow();
            for (Map.Entry entry : row.entrySet()) {
                result.append(entry.getValue().toString().replaceAll(",", "")).append("\t");
            }
            result.append("\n");
        }
        return result.toString();
    }

    static public class ColumnModel implements Serializable {

        private String header;

        public ColumnModel(String header) {
            this.header = header;
        }

        public String getHeader() {
            return header;
        }
    }

}
