package ir.mjm.admin;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.hiber.CategoriesTblHiberEntity;
import ir.mjm.util.LocaleBean;
import org.primefaces.context.RequestContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 * Created by Serap on 9/5/14.
 */
@ManagedBean
@ViewScoped
public class SuggestionCats {
  ArrayList<CategoriesTblHiberEntity> categoriesTblHiberEntities;

  public void viewSuggestions() {

    Map<String, Object> options = new HashMap<String, Object>();

    options.put("closable", true);
    options.put("modal", true);
    options.put("draggable", false);
    options.put("resizable", false);

    RequestContext.getCurrentInstance().openDialog("changeSug", options, null);

  }


  public ArrayList<CategoriesTblHiberEntity> getCategoriesTblHiberEntities() {
    if (categoriesTblHiberEntities == null || categoriesTblHiberEntities.size() == 0) {
      categoriesTblHiberEntities = HiberDBFacad.getInstance().getAllcategories();
    }
    return categoriesTblHiberEntities;
  }

  public void setCategoriesTblHiberEntities(ArrayList<CategoriesTblHiberEntity> categoriesTblHiberEntities) {
    this.categoriesTblHiberEntities = categoriesTblHiberEntities;
  }

  public void updateCategories() {
    boolean oked = HiberDBFacad.getInstance().updateCategories(categoriesTblHiberEntities);
    FacesMessage fmsg;
    if (oked) {
      fmsg =
          new FacesMessage(FacesMessage.SEVERITY_INFO, LocaleBean.localMessageOf("categories.update.successfully"), "");
    } else {
      fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, LocaleBean.localMessageOf("categories.update.failed"), "");

    }
    FacesContext.getCurrentInstance().addMessage(null, fmsg);
  }

  public void changeSug(ValueChangeEvent event) {
    CategoriesTblHiberEntity categoriesTblHiberEntity = (CategoriesTblHiberEntity) event.getNewValue();

    for (CategoriesTblHiberEntity cat : categoriesTblHiberEntities) {
      if (cat.getCatId() == categoriesTblHiberEntity.getCatId()) {
        cat.setSug(categoriesTblHiberEntity.getSug());
        break;
      }
    }
  }

  public void changeSug1(CategoriesTblHiberEntity categoriesTblHiberEntity) {
    for (CategoriesTblHiberEntity cat : categoriesTblHiberEntities) {
      if (cat.getCatId() == categoriesTblHiberEntity.getCatId()) {
        cat.setSug(categoriesTblHiberEntity.getSug());
        break;
      }
    }
  }
}
