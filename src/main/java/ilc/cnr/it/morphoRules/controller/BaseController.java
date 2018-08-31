/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilc.cnr.it.morphoRules.controller;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author andrea
 */
public abstract class BaseController {

    public void error(String summary, String... details) {
        String detail = buildDetail(details);
        message(FacesMessage.SEVERITY_ERROR, summary, detail);
    }

    public void warn(String summary, String... details) {
//        summary = getLabel(summary);
        String detail = buildDetail(details);
        message(FacesMessage.SEVERITY_WARN, summary, detail);
    }

    public void info(String summary, String... details) {
//        summary = getLabel(summary);
        String detail = buildDetail(details);
        message(FacesMessage.SEVERITY_INFO, summary, detail);
    }

    protected String buildDetail(String[] details) {
//        String detail = getLabel(details[0]);
        String detail = details[0];
        for (int i = 1; i < details.length; i++) {
            detail = detail.replaceAll("\\{" + (i - 1) + "\\}", details[i]);
        }
        return detail;
    }

    public void message(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public String getLabel(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle resourceBundle = context.getApplication().getResourceBundle(context, "label");
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException ex) {
            return "???" + key + "???";
        }
    }

    public Locale getRequestLocale() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getRequestLocale();
    }

}
