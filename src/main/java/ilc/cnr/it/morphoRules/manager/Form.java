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
public class Form implements Serializable {

    private String stem;
    private String flexion;
    private String morphoTag;
    private String morphoDescription;

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public String getFlexion() {
        return flexion;
    }

    public void setFlexion(String flexion) {
        this.flexion = flexion;
    }

    public String getMorphoTag() {
        return morphoTag;
    }

    public void setMorphoTag(String morphoTag) {
        this.morphoTag = morphoTag;
    }

    public String getMorphoDescription() {
        return morphoDescription;
    }

    public void setMorphoDescription(String morphoDescription) {
        this.morphoDescription = morphoDescription;
    }

    public void Form() {
        this.morphoDescription = "TBD.";
    }

}
