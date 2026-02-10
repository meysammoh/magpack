package ir.mjm.admin;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.ImageFacade;
import ir.mjm.DBAO.Statistics;
import ir.mjm.DBAO.hiber.MagazineTblHiberEntity;
import ir.mjm.DBAO.hiber.PagesHiberEntity;
import ir.mjm.util.LocaleBean;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * Created by Serap on 7/25/14.
 */
@ManagedBean
@SessionScoped
public class AdminControler {
  static final Logger log = Logger.getLogger(AdminControler.class);

  ArrayList<AdminPublisherInf> adminPublisherInfs;
  ArrayList<AdminPublisherInf> selectedPublisher;
  ArrayList<AdminMobileUser> mobileUsers;
  ArrayList<ClassificationData> classificationDatas;

  MagazineTblHiberEntity selectedMagazine;

  public ArrayList<AdminPublisherInf> getAdminPublishers(boolean reload) {
    if (reload||adminPublisherInfs == null) {
      fetchPubsInfs();
    }
    return adminPublisherInfs;
  }

  public void setAdminPublisherInfs(ArrayList<AdminPublisherInf> adminPublisherInfs) {
    this.adminPublisherInfs = adminPublisherInfs;
  }

  public void fetchPubsInfs() {
    this.adminPublisherInfs = HiberDBFacad.getInstance().fetchPubsInfsForAdmin();
  }

  public void getBillDetailsAndGotoAdminPubDet(AdminPublisherInf adminPublisherInf) {
    if (this.selectedPublisher == null) {
      this.selectedPublisher = new ArrayList<>();

    }
    this.selectedPublisher.clear();
    this.selectedPublisher.add(adminPublisherInf);
    try {
      FacesContext.getCurrentInstance()
                  .getExternalContext()
                  .redirect(
                      FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
                      "/adminpanel/EditPublisher.xhtml");
    } catch (IOException e) {
      log.error("Exception in: ", e);
    }

  }

  public ArrayList<AdminPublisherInf> getSelectedPublisher() {
    return selectedPublisher;
  }

  public void setSelectedPublisher(ArrayList<AdminPublisherInf> selectedPublisher) {
    this.selectedPublisher = selectedPublisher;
  }

  public void updateSelectedPublisherInfos() {
    if (HiberDBFacad.getInstance().updatePublisherInfos(selectedPublisher.get(0))) {
      FacesContext.getCurrentInstance()
                  .addMessage(
                      null,
                      new FacesMessage(
                          FacesMessage.SEVERITY_INFO,
                          LocaleBean.localMessageOf("update.success"),
                          ""));
    } else {
      FacesContext.getCurrentInstance()
                  .addMessage(
                      null,
                      new FacesMessage(
                          FacesMessage.SEVERITY_ERROR,
                          LocaleBean.localMessageOf("update.error"),
                          ""));
    }

  }

  public void gotoReporting() {
    if (selectedPublisher != null && selectedPublisher.size() > 0) {
      AdminPublisherInf sel = selectedPublisher.get(0);
      if (sel != null) {

        AdminPublisherMagazines adminpubmagz =
            null;// (AdminPublisherMagazines)FaceUtil.findBean("adminPublisherMagazines");
        if (adminpubmagz == null) {
          adminpubmagz = new AdminPublisherMagazines();
        }
        try {
          adminpubmagz.setSelectedPublisher(sel);
          FacesContext.getCurrentInstance()
                      .getExternalContext()
                      .getSessionMap()
                      .put("adminPublisherMagazines", adminpubmagz);
          FacesContext.getCurrentInstance()
                      .getExternalContext()
                      .redirect(
                          FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
                          "/adminpanel/AdminPublisherReporting.xhtml");
          return;
        } catch (IOException e) {
          log.error("Exception in: ", e);
        }
      }

    }
    FacesContext.getCurrentInstance().addMessage(
        null, new FacesMessage(
            FacesMessage.SEVERITY_ERROR,
            LocaleBean.localMessageOf("can.not.redirect.to.reporting.page"), ""));

  }

  public void gotoSelectedPublisherReporting(AdminPublisherInf sel) {
    if (this.selectedPublisher == null) {
      this.selectedPublisher = new ArrayList<>();

    }
    this.selectedPublisher.clear();
    this.selectedPublisher.add(sel);
    if (sel != null) {

      AdminPublisherMagazines adminpubmagz =
          null;// (AdminPublisherMagazines)FaceUtil.findBean("adminPublisherMagazines");
      if (adminpubmagz == null) {
        adminpubmagz = new AdminPublisherMagazines();
      }
      try {
        adminpubmagz.setSelectedPublisher(sel);
        FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("adminPublisherMagazines", adminpubmagz);
        FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                        FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
                        "/adminpanel/AdminPublisherReporting.xhtml");
        return;
      } catch (IOException e) {
        log.error("Exception in: ", e);
      }
    }


    FacesContext.getCurrentInstance()
                .addMessage(
                    null,
                    new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        LocaleBean.localMessageOf("can.not.redirect.to.reporting.page"),
                        ""));

  }

  public MagazineTblHiberEntity getSelectedMagazine() {
    return selectedMagazine;
  }

  public void setSelectedMagazine(MagazineTblHiberEntity selectedMagazine) {
    this.selectedMagazine = selectedMagazine;
  }


  public ArrayList<AdminMobileUser> getMobileUsers() {
    if (mobileUsers == null) {
      mobileUsers = new ArrayList<>();
    }
    if (mobileUsers.size() <= 0) {
      setMobileUsers(HiberDBFacad.getInstance().getMobileUsersForAdminPanel());
    }
    return mobileUsers;
  }

  public void setMobileUsers(ArrayList<AdminMobileUser> mobileUsers) {
    this.mobileUsers = mobileUsers;
  }

  public ArrayList<ClassificationData> getClassificationDatas() {
    if (classificationDatas == null) {
      classificationDatas = new ArrayList<>();
    }
    if (classificationDatas.size() <= 0) {
      ArrayList<MagazineTblHiberEntity> mags = HiberDBFacad.getInstance().getMagazinesForClassification();
      for (MagazineTblHiberEntity mag : mags) {
        ClassificationData classificationData = new ClassificationData();
        classificationData.setMagazine(mag);
        classificationData.setPublisherInfo(findAdminPublisherInf(mag.getUserId()));
        classificationData.setDayUntilPublish(Statistics.getDayUntilPublish(mag.getIssueAppear()));
        classificationData.setMagazineName(mag.getTitle());
        int lIndex = mag.getDirPath().lastIndexOf("/");
        lIndex = lIndex < 0 ? mag.getDirPath().lastIndexOf("\\") : lIndex;

        classificationData.setFileName(mag.getDirPath().substring(lIndex + 1));
        classificationData.setChangePageTypes(setCahngeType(mag));

        classificationDatas.add(classificationData);


      }

    }
    return classificationDatas;
  }

  private ArrayList<DefinePageType> setCahngeType(MagazineTblHiberEntity mag) {
    ArrayList<DefinePageType> ret = new ArrayList<>();
    ArrayList<String> smallImages = ImageFacade.getInstance().getListOfImageAddress(mag, "s");
    ArrayList<String> mediumImages = ImageFacade.getInstance().getListOfImageAddress(mag, "m");
    //        ArrayList<String> mediumImages= smallImages;
    for (int i = 0; i < smallImages.size(); i++) {
      DefinePageType definePageType = new DefinePageType();
      definePageType.setSmallImg(smallImages.get(i));
      if ((mediumImages.size() - 1) >= i) {
        definePageType.setLargImg(mediumImages.get(i));
      }
      PagesHiberEntity pagesHiberEntity = HiberDBFacad.getInstance().getPageTypeEntity(mag.getMagazineId(), i + 1);
      if (pagesHiberEntity == null) {
        pagesHiberEntity = new PagesHiberEntity();
        pagesHiberEntity.setPageNumber(i + 1);
        pagesHiberEntity.setMagId(mag.getMagazineId());
      }

      definePageType.setPagesHiberEntity(pagesHiberEntity);
      ret.add(definePageType);
    }


    return ret;


  }

  public void setClassificationDatas(ArrayList<ClassificationData> classificationDatas) {
    this.classificationDatas = classificationDatas;
  }

  private AdminPublisherInf findAdminPublisherInf(int pubId) {
    for (AdminPublisherInf adminPublisherInf : adminPublisherInfs) {
      if (adminPublisherInf.getPubusersHiberEntity().getId() == pubId) {
        return adminPublisherInf;
      }
    }

    return null;
  }
}