package ir.mjm.admin;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.hiber.BannersTblHiberEntity;
import ir.mjm.util.FaceUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import java.util.HashMap;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * @author Jafar Mirzaei (jm.csh2009@gmail.com)
 * @version 1.0 2015.1012
 * @since 1.0
 */
@ManagedBean
@ViewScoped
public class BannerConfigDialogMBean {
  private BannersTblHiberEntity bannersTblHiberEntity;
  public void showMagazineSelectorDialog(BannersTblHiberEntity bannersTblHiberEntity) {
    this.bannersTblHiberEntity=bannersTblHiberEntity;
    Map<String, Object> options = new HashMap<String, Object>();

    options.put("closable", true);
      options.put("modal", true);
      options.put("draggable", false);
      options.put("resizable", false);

    RequestContext.getCurrentInstance().openDialog("MagazineSelectionWizard", options, null);
  }

  public void onDialogReturn(SelectEvent event) {

    if (event.getObject() != null) {
      int selectedMagId = 0;
      try {
        selectedMagId = (int) event.getObject();
        this.bannersTblHiberEntity.setMagazineId(selectedMagId);
        HiberDBFacad.getInstance().updateBanner(this.bannersTblHiberEntity);
        BannerConfig bannerConfig= FaceUtil.findBean("bannerConfig");
        if(bannerConfig!=null)
          bannerConfig.getBannersTblHiberEntities().add(new BannersTblHiberEntity());
      } catch (Exception e) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error In MAg ID", null);

        FacesContext.getCurrentInstance().addMessage(null, message);

      }
    }

  }
}

