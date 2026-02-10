package ir.mjm.admin;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.util.LocaleBean;
import org.primefaces.context.RequestContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * Created by Serap on 8/15/14.
 */
@ManagedBean
@SessionScoped
public class PageTypeDfinationView implements Serializable {
  ClassificationData classificationData;
  DefinePageType currentViewPage;
  private int currentIndex;

  public void viewClassifyPanel(ClassificationData classificationData) {
    this.classificationData = classificationData;
    currentIndex = 0;
    final ArrayList<DefinePageType> changePageTypes = classificationData.getChangePageTypes();
    if (changePageTypes == null || changePageTypes.size() == 0) {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(LocaleBean.localMessageOf("magazine.convert.problem"), "INFO MSG"));
      return;
    }
    currentViewPage = changePageTypes.get(currentIndex);
    Map<String, Object> options = new HashMap<String, Object>();

    options.put("closable", true);
    options.put("modal", true);
    options.put("draggable", false);
    options.put("resizable", false);

    RequestContext.getCurrentInstance().openDialog("ClassifyPanel", options, null);

  }

  public void closeNetStat() {
    RequestContext.getCurrentInstance().closeDialog("ClassifyPanel");
  }

  public void setAsTextOnlyPageType() {
    HiberDBFacad.getInstance().deletePageType(currentViewPage.getPagesHiberEntity());
    currentViewPage.getPagesHiberEntity().setPageType(0);
    changecurrentViewPage();
  }

  public void setAsAdPageType() {
    currentViewPage.pagesHiberEntity.setPageType(2);
    HiberDBFacad.getInstance().addOrUpdatePageType(currentViewPage.getPagesHiberEntity());

    changecurrentViewPage();


  }

  public void setAsMixedPageType() {
    currentViewPage.pagesHiberEntity.setPageType(1);
    HiberDBFacad.getInstance().addOrUpdatePageType(currentViewPage.getPagesHiberEntity());
    changecurrentViewPage();


  }

  private void changecurrentViewPage() {
    currentIndex = (currentIndex + 1) % classificationData.getChangePageTypes().size();
    currentViewPage = classificationData.getChangePageTypes().get(currentIndex);


  }

  public ClassificationData getClassificationData() {
    return classificationData;
  }

  public void setClassificationData(ClassificationData classificationData) {
    this.classificationData = classificationData;
  }

  public DefinePageType getCurrentViewPage() {
    return currentViewPage;
  }

  public void setCurrentViewPage(DefinePageType currentViewPage) {
    this.currentViewPage = currentViewPage;
  }
}
