/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilc.cnr.it.morphoRules.manager;

import ilc.cnr.it.morphoRules.controller.BaseController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author andrea
 */
@ApplicationScoped
public class LexicalManager extends BaseController implements Serializable {

    private LexicalModel lexicalModel;
    private LexicalQuery lexicalQuery;

    public void deafult_loadLexicon() {
        if (lexicalModel == null) {
            lexicalModel = new LexicalModel();
            lexicalQuery = new LexicalQuery(lexicalModel);
        }
    }

    public synchronized Map<String, MorphoClass> getMorphoClassMap() {
        return lexicalQuery.getMorphoClassMap();
//        return null;
    }

    public synchronized Map<String, String> getMorphoLegendMap() {
        return lexicalQuery.getMorphoLegendMap();
//        return null;
    }

    public synchronized ArrayList<ResultTable> getFlexedForms(String word, String pos) {
        lexicalModel.createLexicalEntry(word, pos);
//        return lexicalQuery.getMorphoLegendMap();
        return null;
    }

}
