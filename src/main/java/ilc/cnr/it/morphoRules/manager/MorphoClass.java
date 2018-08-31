/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilc.cnr.it.morphoRules.manager;

import java.io.Serializable;

/**
 *
 * @author andrea
 */
public class MorphoClass implements Serializable {

    private String name;
    private String definition;
    private int remove;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getRemove() {
        return remove;
    }

    public void setRemove(int remove) {
        this.remove = remove;
    }

    public MorphoClass(String name, String definition, int remove) {
        this.name = name;
        this.definition = definition;
        this.remove = remove;
    }

}
