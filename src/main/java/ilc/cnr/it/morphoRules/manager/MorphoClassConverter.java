/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilc.cnr.it.morphoRules.manager;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author andrea
 */
@FacesConverter("morphoClassConverter")
public class MorphoClassConverter implements Converter {

    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        String[] s = value.split("\\|");
        MorphoClass mc = new MorphoClass(s[0], s[1], Integer.parseInt(s[2]));
        return mc;
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return ((MorphoClass) object).getName() + "|" + ((MorphoClass) object).getDefinition()+ "|" + ((MorphoClass) object).getRemove();
        } else {
            return null;
        }
    }
}
