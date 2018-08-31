package it.cnr.ilc.plotino.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author oakgen
 */
public abstract class BaseController {

    public void fatal(String summary, String detail) {
        message(FacesMessage.SEVERITY_FATAL, summary, detail);
    }

    public void error(String summary, String detail) {
        message(FacesMessage.SEVERITY_ERROR, summary, detail);
    }

    public void warn(String summary, String detail) {
        message(FacesMessage.SEVERITY_WARN, summary, detail);
    }

    public void info(String summary, String detail) {
        message(FacesMessage.SEVERITY_INFO, summary, detail);
    }

    public void message(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(severity, summary, detail));
    }

}
