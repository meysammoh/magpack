package ir.mjm.admin;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.ImageFacade;
import ir.mjm.DBAO.MagazinReportForPub;
import ir.mjm.DBAO.MagazineRepSpecTitle;
import ir.mjm.DBAO.Magazine_tblTable;
import ir.mjm.DBAO.ReaderConversationPerPage;
import ir.mjm.DBAO.ReportingFacad;
import ir.mjm.DBAO.hiber.MagazineTblHiberEntity;
import ir.mjm.util.LocaleBean;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * Created by Serap on 8/13/14.
 */
@ManagedBean
@ViewScoped
public class AdminPublisherMagazines {
  static final Logger log = Logger.getLogger(AdminPublisherMagazines.class);

  ArrayList<MagazinReportForPub> uploadedMagazineDB;

  //add from admincontroler
  AdminPublisherInf selectedPublisher;

  private ArrayList<MagazineRepSpecTitle> magazineRepSpecTitles = new ArrayList<MagazineRepSpecTitle>();
  String selectedTitleForRepIssues;
  ArrayList<MagazineRepSpecTitle> selectedCurrentMagazine;
  int setSelectedCurrentMag_id;
  int currentMagUploaded;
  ArrayList<ReaderConversationPerPage> listOfPageImageAdress;
  Magazine_tblTable selectedIssue;
  boolean updateData = false;

  public void setUploadedMagazineDB(ArrayList<MagazinReportForPub> uploadedMagazineDB) {
    this.uploadedMagazineDB = uploadedMagazineDB;
  }

  public AdminPublisherInf getSelectedPublisher() {
    return selectedPublisher;
  }

  public void setSelectedPublisher(AdminPublisherInf selectedPublisher) {
    this.selectedPublisher = selectedPublisher;
  }

  public ArrayList<MagazinReportForPub> getUploadedMagazineDB() {
    if (uploadedMagazineDB == null || uploadedMagazineDB.size() <= 0 || updateData) {
      if (uploadedMagazineDB == null) {
        uploadedMagazineDB = new ArrayList<>();
      }
      //            try {
      uploadedMagazineDB.addAll(
          HiberDBFacad.getInstance()
                      .getLastUploadedMagazineByUserId(
                          selectedPublisher.getMagazineTblHiberEntity()
                                           .getUserId()));
      updateData = false;
      //            } catch (SQLException e) {
      //                log.error("Exception in: ",e);
      //            }
    }
    return uploadedMagazineDB;
  }

  public void getTitleIssuesAndRedirectToSelectedTitle(Magazine_tblTable magazine_tblTable) {
    this.setSelectedTitleForRepIssues(magazine_tblTable.getTitle());
    setSelectedIssue(magazine_tblTable);
    setMagazineRepSpecTitles(
        ReportingFacad.getInstance()
                      .getSpecTitleView(
                          magazine_tblTable.getUser_id(),
                          magazine_tblTable.getTitle()));
    try {
      FacesContext.getCurrentInstance()
                  .getExternalContext()
                  .redirect(
                      FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
                      "/adminpanel/AdminPublisherSpecMagazineReport.xhtml");
    } catch (IOException e) {
      log.error("Exception in: ", e);
    }
  }

  public ArrayList<MagazineRepSpecTitle> getMagazineRepSpecTitles() {
    return magazineRepSpecTitles;
  }

  public void setMagazineRepSpecTitles(ArrayList<MagazineRepSpecTitle> magazineRepSpecTitles) {
    this.magazineRepSpecTitles = magazineRepSpecTitles;
  }

  public String getSelectedTitleForRepIssues() {
    return selectedTitleForRepIssues;
  }

  public void setSelectedTitleForRepIssues(String selectedTitleForRepIssues) {
    this.selectedTitleForRepIssues = selectedTitleForRepIssues;
  }

  public ArrayList<MagazineRepSpecTitle> getSelectedCurrentMagazine() {
    if (selectedCurrentMagazine == null) {
      selectedCurrentMagazine = new ArrayList<>();
    }
    return selectedCurrentMagazine;
  }

  public void getGetIssueSelectedAndRedirectToSelectdeSpecIssuePage(
      MagazineRepSpecTitle magazineRepSpecTitle,
      boolean redirect) {
    this.setSetSelectedCurrentMag_id(magazineRepSpecTitle.getMagazine_tblTable().getMagazine_id());
    getSelectedCurrentMagazine().clear();
    getSelectedCurrentMagazine().add(magazineRepSpecTitle);
    if (redirect) {
      try {
        FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                        FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
                        "/adminpanel/AdminPublisherSpecIssueReporting.xhtml");
      } catch (IOException e) {
        log.error("Exception in: ", e);
      }
    }
  }

  public void setSelectedCurrentMagazine(ArrayList<MagazineRepSpecTitle> selectedCurrentMagazine) {
    this.selectedCurrentMagazine = selectedCurrentMagazine;
  }

  public int getSetSelectedCurrentMag_id() {
    return setSelectedCurrentMag_id;
  }

  public void setSetSelectedCurrentMag_id(int setSelectedCurrentMag_id) {
    this.setSelectedCurrentMag_id = setSelectedCurrentMag_id;
  }

  public ArrayList<ReaderConversationPerPage> getListOfPageImageAdress() {

    if (this.listOfPageImageAdress == null || this.listOfPageImageAdress.size() <= 0 ||
        (currentMagUploaded != getSetSelectedCurrentMag_id()) || updateData) {
      this.listOfPageImageAdress = new ArrayList();
      fillListOfPageImageAdressForReaders(setSelectedCurrentMag_id);
    }
    return this.listOfPageImageAdress;
  }

  private void fillListOfPageImageAdressForReaders(int selectedCurrentMag_id) {
    currentMagUploaded = selectedCurrentMag_id;
    ArrayList<String> images = ImageFacade.getInstance().getListOfImageAddress(selectedCurrentMag_id, "s");
    int totlaDl = HiberDBFacad.getInstance().getTotalDownload(selectedCurrentMag_id);
    HashMap averagesPercents = HiberDBFacad.getInstance().getEachPageViewPercent(selectedCurrentMag_id, totlaDl);
    double totalMony = HiberDBFacad.getInstance().getRevenuesEarned(selectedCurrentMag_id);
    for (String img : images) {
      try {
        ReaderConversationPerPage readerConversationPerPage = new ReaderConversationPerPage();
        int lastindx = img.lastIndexOf(".png");
        if (lastindx < 0) {
          lastindx = img.lastIndexOf(".jpg");
        }
        int pageNum = Integer.parseInt(img.substring(img.lastIndexOf("_") + 1, lastindx));
        float dlPercent = 0;
        if (averagesPercents != null) {
          Object dlprTmp = averagesPercents.get(pageNum);
          dlPercent = dlprTmp == null ? 0 : ((float) dlprTmp);
        }
        readerConversationPerPage.setReadPercent((int) ((dlPercent) * 100));
        readerConversationPerPage.setRevenue(dlPercent * totalMony);
        readerConversationPerPage.setImagePath(img);
        readerConversationPerPage.setPageNumber(pageNum);

        readerConversationPerPage.setPageViews(
            HiberDBFacad.getInstance()
                        .getSumOfClickToEnlarg(selectedCurrentMag_id, pageNum));
        readerConversationPerPage.setAveLengthOfView(
            HiberDBFacad.getInstance()
                        .getAvePageViewForPage(
                            selectedCurrentMag_id,
                            pageNum));
        readerConversationPerPage.setPageType(HiberDBFacad.getInstance().getPageType(selectedCurrentMag_id, pageNum));
        this.listOfPageImageAdress.add(readerConversationPerPage);
      } catch (Exception e) {
        log.error("Exception in image: " + img);
        log.error("Exception in: ", e);
      }
    }


  }

  public int getCurrentMagUploaded() {
    return currentMagUploaded;
  }

  public void setCurrentMagUploaded(int currentMagUploaded) {
    this.currentMagUploaded = currentMagUploaded;
  }

  public void setListOfPageImageAdress(ArrayList<ReaderConversationPerPage> listOfPageImageAdress) {
    this.listOfPageImageAdress = listOfPageImageAdress;
  }

  public Magazine_tblTable getSelectedIssue() {
    return selectedIssue;
  }

  public void setSelectedIssue(Magazine_tblTable selectedIssue) {
    this.selectedIssue = selectedIssue;
  }

  public void updateMagazine() {

    MagazineTblHiberEntity oked =
        HiberDBFacad.getInstance().updateMagazine(selectedCurrentMagazine.get(0).getMagazine_tblTable());


    if (oked != null) {
      selectedPublisher.setMagazineTblHiberEntity(oked);
      selectedIssue = selectedCurrentMagazine.get(0).getMagazine_tblTable();
      selectedTitleForRepIssues = selectedIssue.getTitle();
      updateData = true;
      uploadedMagazineDB.clear();

      getUploadedMagazineDB();
      //MagazineRepSpecTitle sel=            selectedCurrentMagazine.get(0);
      //            selectedIssue=sel.getMagazine_tblTable();
      //            selectedPublisher.setMagazineTblHiberEntity(oked);
      //            uploadedMagazineDB.
      //            uploadedMagazineDB.remove(selectedPublisher);
      //            selectedPublisher.setMagazineTblHiberEntity(oked);
      //            uploadedMagazineDB.add(selectedPublisher);
      //            AdminControler adminControler=(AdminControler)FaceUtil.findBean("adminControler");
      //            adminControler.getAdminPublisherInfs().remove(selectedPublisher);
      //            adminControler.getAdminPublisherInfs().add(selectedPublisher);
      //            getGetIssueSelectedAndRedirectToSelectdeSpecIssuePage(sel,false);

      FacesContext.getCurrentInstance()
                  .addMessage(
                      "magazineupdate",
                      new FacesMessage(
                          FacesMessage.SEVERITY_INFO,
                          LocaleBean.localMessageOf("update.success"),
                          ""));
    } else {
      FacesContext.getCurrentInstance()
                  .addMessage(
                      "magazineupdate",
                      new FacesMessage(FacesMessage.SEVERITY_ERROR, LocaleBean.localMessageOf("update.error"), ""));
    }
  }

  public void deactiveMagazine() {
    boolean oked = HiberDBFacad.getInstance().deactivePubstatus(selectedCurrentMagazine.get(0).getMagazine_tblTable());


    if (oked) {


      FacesContext.getCurrentInstance().addMessage(
          "magazineupdate", new FacesMessage(
              FacesMessage.SEVERITY_INFO, LocaleBean
              .localMessageOf("magazine.deactivation.successful"), ""));

    } else {
      FacesContext.getCurrentInstance().addMessage(
          "magazineupdate", new FacesMessage(
              FacesMessage.SEVERITY_ERROR, LocaleBean
              .localMessageOf("magazine.deactivation.failed"), ""));
    }


  }

  public boolean isUpdateData() {
    return updateData;
  }

  public void setUpdateData(boolean updateData) {
    this.updateData = updateData;
  }
}
