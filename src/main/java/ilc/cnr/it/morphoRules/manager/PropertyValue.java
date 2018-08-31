/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilc.cnr.it.morphoRules.manager;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author andrea
 */
@ApplicationScoped
public class PropertyValue {

    @Inject
    private LexicalManager lexicalManager;

    private Map<String, MorphoClass> morphoClassMap = new HashMap<String, MorphoClass>();
    private Map<String, String> morphoLegendMap = new HashMap<String, String>();

    public Map<String, MorphoClass> getMorphoClassMap() {
        return morphoClassMap;
    }

    public Map<String, String> getMorphoLegendMap() {
        return morphoLegendMap;
    }

    public void init() {
    }

    @PostConstruct
    public void load() {
        morphoClassMap = lexicalManager.getMorphoClassMap();
        morphoLegendMap = lexicalManager.getMorphoLegendMap();
    }

}
