package ir.mjm.util;

/**
 * Created by meysam on 8/8/15.
 */


import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class LocaleBean {

  private Locale locale;
  private static ResourceBundle bundle;

  public LocaleBean() {

  }

  @PostConstruct
  public void init() {
    locale = new Locale("fa");
    bundle = ResourceBundle.getBundle("ir.mjm.strings", locale);
  }

  public static ResourceBundle getBundle() {
    return bundle;
  }

  public static String localMessageOf(String msg) {
    String result = null;
    if (msg == null) {
      return null;
    }
    try {
      result = getBundle().getString(msg);
    } catch (MissingResourceException e) {
      result = msg;
    }
    return result;
  }

  public static String keyOfLocalString(String localString) {
    String trimmedLocalString = localString.trim();
    for (String key : getBundle().keySet()) {
      if (LocaleBean.localMessageOf(key).trim().equals(trimmedLocalString)) {
        return key;
      }
    }
    return null;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getLanguage() {
    FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    return locale.getLanguage();

  }

  public void setLanguage(String language) {
    locale = new Locale(language);
    FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    bundle = ResourceBundle.getBundle("ir.mjm.strings", locale);

  }
}
