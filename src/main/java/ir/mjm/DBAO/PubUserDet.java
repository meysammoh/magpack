package ir.mjm.DBAO;


import ir.mjm.entities.LoginBean;
import ir.mjm.util.AppScopedUtil;
import ir.mjm.util.FaceUtil;
import ir.mjm.util.LocaleBean;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.UploadedFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 * Created by Serap on 6/19/14.
 */

@ManagedBean
@ViewScoped
public class PubUserDet implements Serializable {
  static final Logger log = Logger.getLogger(PubUserDet.class);
  private static final long serialVersionUID = -378638585055460857L;

  private String destination;

  private int id = -1;
  private String firstname;
  private String lastname;
  private String email;
  private String phonecode;
  private String phonenum;
  private String streetname;
  private String streetnum;
  private String cityname;
  private String citynum;
  private String country;
  private String currenttitle;
  private ArrayList<Magazine_tblTable> uploadedMagazine = new ArrayList<Magazine_tblTable>();
  private boolean canNotUpload;
  private String selectedTitleForRepIssues;
  private String selectedIssueForSpecIssue;
  private int selectedCurrentMag_id;
  private ArrayList<MagazinReportForPub> uploadedMagazineDB = new ArrayList<MagazinReportForPub>();
  private ArrayList<MagazineRepSpecTitle> magazineRepSpecTitles = new ArrayList<MagazineRepSpecTitle>();
  private UploadedFile file;
  private File pdfFile;
  public TabView tabView;
  private ArrayList<MagazineRepSpecTitle> selectedCurrentMagazine;
  private ArrayList<ReaderConversationPerPage> listOfPageImageAdress;
  int currentMagUploaded;

  public ArrayList<MagazineRepSpecTitle> getSelectedCurrentMagazine() {
    if (selectedCurrentMagazine == null) {
      selectedCurrentMagazine = new ArrayList<>();
    }
    return selectedCurrentMagazine;
  }

  public void setSelectedCurrentMagazine(ArrayList<MagazineRepSpecTitle> selectedCurrentMagazine) {
    this.selectedCurrentMagazine = selectedCurrentMagazine;
  }

  public int getSelectedCurrentMag_id() {
    return selectedCurrentMag_id;
  }

  public void setSelectedCurrentMag_id(int selectedCurrentMag_id) {
    this.selectedCurrentMag_id = selectedCurrentMag_id;
  }

  public String getCurrenttitle() {
    return currenttitle;
  }

  public void setCurrenttitle(String currenttitlee) {
    log.debug("------" + currenttitlee);
    this.currenttitle = currenttitle;
  }


  public ArrayList<MagazinReportForPub> getUploadedMagazineDB() {
    if (uploadedMagazineDB.size() <= 0) {
      //            try {
      uploadedMagazineDB.addAll(HiberDBFacad.getInstance().getLastUploadedMagazineByUserId(getId()));

      //            } catch (SQLException e) {
      //                log.error("Exception in: ", e);
      //            }
    }
    return uploadedMagazineDB;
  }

  public void setUploadedMagazineDB(ArrayList<MagazinReportForPub> uploadedMagazineDB) {
    this.uploadedMagazineDB = uploadedMagazineDB;
  }


  public UploadedFile getFile() {
    return file;
  }

  public void setFile(UploadedFile file) {
    this.file = file;
  }

  public TabView getTabView() {
    return tabView;
  }

  public void setTabView(TabView tabView) {
    this.tabView = tabView;
  }


  public File getPdfFile() {
    return pdfFile;
  }

  public void setPdfFile(File pdfFile) {
    this.pdfFile = pdfFile;
  }

  public ArrayList<Magazine_tblTable> getUploadedMagazine() {
    return uploadedMagazine;
  }


  public void setUploadedMagazine(ArrayList<Magazine_tblTable> uploadedMagazine) {
    this.uploadedMagazine = uploadedMagazine;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public int getId() {
    if (id < 0) {
      setId(((LoginBean) FaceUtil.findBean("loginBean")).getUser_id());
    }
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhonecode() {
    return phonecode;
  }

  public void setPhonecode(String phonecode) {
    this.phonecode = phonecode;
  }

  public String getPhonenum() {
    return phonenum;
  }

  public void setPhonenum(String phonenum) {
    this.phonenum = phonenum;
  }

  public String getStreetname() {
    return streetname;
  }

  public void setStreetname(String streetname) {
    this.streetname = streetname;
  }

  public String getStreetnum() {
    return streetnum;
  }

  public void setStreetnum(String streetnum) {
    this.streetnum = streetnum;
  }

  public String getCityname() {
    return cityname;
  }

  public void setCityname(String cityname) {
    this.cityname = cityname;
  }

  public String getCitynum() {
    return citynum;
  }

  public void setCitynum(String citynum) {
    this.citynum = citynum;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }


  public void addOrUpdate() {
    FacesMessage msg = null;
    //        try {
    //            int okeed= DBFacade.getInstance().upadtePubUserDet(this);
    int okeed = HiberDBFacad.getInstance().upadtePubUserDet(this);
    if (okeed > 0) {
      msg = new FacesMessage(LocaleBean.localMessageOf("update.success"), "INFO MSG");
      msg.setSeverity(FacesMessage.SEVERITY_INFO);
    } else {
      msg = new FacesMessage(LocaleBean.localMessageOf("update.nochange.error"), "INFO MSG");
      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
    }
    //            preparePub();
    //        }catch (SQLException e) {
    //            msg = new FacesMessage("Update Error!", "INFO MSG");
    //            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
    //
    //            log.error("Exception in: ", e);
    //        }
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }


  //TODO Check
  public PubUserDet() {
    destination = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "/PDFs/";

  }

  public void uploadComplete() {


    FacesMessage msg = null;

    // Do what you want with the file

    ArrayList<Magazine_tblTable> tmpUploadedMagz = new ArrayList<Magazine_tblTable>();
    ArrayList<Magazine_tblTable> tmpUploadedMagz2 = new ArrayList<Magazine_tblTable>();
    tmpUploadedMagz2.addAll(uploadedMagazine);
    StringBuilder infoMessage = new StringBuilder();
    if (checkIssueNumberIsUniq(tmpUploadedMagz2)) {
      for (Magazine_tblTable magazine_tblTable : tmpUploadedMagz2) {


        String oked = checkAllFields(magazine_tblTable);
        if (oked.equals("ok")) {
          //                    try {
          log.debug("FileUploadEvent fired...okeddd");
          magazine_tblTable.setPublisher_name(((PubCompantDet) FaceUtil.findBean("pubCompantDet")).getCompname());
          magazine_tblTable.setDate_time((new Date()));
          if (magazine_tblTable.isImmediatedee()) {
            magazine_tblTable.setIssue_appear(new Date());
          }
          //                        magazine_tblTable.setMagazine_id(DBFacade.getInstance().addMagazine(magazine_tblTable));
          int magggiddd = HiberDBFacad.getInstance().addMagazine(magazine_tblTable);
          if (magggiddd > -1) {
            magazine_tblTable.setMagazine_id(magggiddd);

            infoMessage.append("Magazine " + magazine_tblTable.getFileName() + " Done.\n");


            tmpUploadedMagz.add(magazine_tblTable);
            getUploadedMagazine().remove(magazine_tblTable);
            setUploadedMagazineDB(HiberDBFacad.getInstance().getLastUploadedMagazineByUserId(getId()));
            log.debug("getUploadedMagazine().size()" + getUploadedMagazine().size());
            //                    } catch (SQLException e) {
          } else {
            log.error("---- Error add magazineeeeee");
            msg = new FacesMessage("Server Error.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
          }
        } else {
          log.debug("not ok: "+oked);
          msg = new FacesMessage(
              LocaleBean.localMessageOf("please") + "\n" + oked + LocaleBean.localMessageOf("on.magazine") +
              magazine_tblTable.getFileName(),
              "INFO MSG");
          msg.setSeverity(FacesMessage.SEVERITY_ERROR);
          FacesContext.getCurrentInstance().addMessage(null, msg);

        }
      }
    }
    if (tmpUploadedMagz.size() > 0) {
      infoMessage.append("please check your magazine in INFO MSG\n");
      msg = new FacesMessage(infoMessage.toString());
      msg.setSeverity(FacesMessage.SEVERITY_INFO);
      FacesContext.getCurrentInstance().addMessage(null, msg);
      ((AppScopedUtil) FaceUtil.findBean("appScopedUtil")).converterFacad.convertAll(tmpUploadedMagz);
    }
    log.debug("----------------" + getUploadedMagazine().size());

  }

  private boolean checkIssueNumberIsUniq(ArrayList<Magazine_tblTable> tmpUploadedMagz2) {
    boolean ret = true;
    HashMap<String, Integer> titleToIssueMap = new HashMap<>();
    for (Magazine_tblTable magazine_tblTable : tmpUploadedMagz2) {
      if (HiberDBFacad.getInstance().isIssueNumberUploaded(magazine_tblTable)) {
        ret = false;

        FacesContext.getCurrentInstance().addMessage(
            null, new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                LocaleBean.localMessageOf("issue.number.registered.error") + ": " + magazine_tblTable.getIssue_num() +
                ": " + magazine_tblTable.getTitle(),
                ""));
        //                magazine_tblTable.setIssue_num(0);
        break;

      }
      Integer iss0 = titleToIssueMap.get(magazine_tblTable.getTitle());

      if (iss0 != null && (iss0.equals(magazine_tblTable.getIssue_num()))) {
        ret = false;
        FacesContext.getCurrentInstance()
                    .addMessage(
                        null,
                        new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            LocaleBean.localMessageOf("two.magazine.with.same.name"),
                            ""));
        break;
      }
      titleToIssueMap.put(magazine_tblTable.getTitle(), magazine_tblTable.getIssue_num());
    }
    return ret;
  }

  private String checkAllFields(Magazine_tblTable magazine_tblTable) {
    if (magazine_tblTable.getIssue_num() <= 0) {
      return LocaleBean.localMessageOf("fill.issue.number");
    }
    /*if (magazine_tblTable.getIssue_year() == null) {
      return LocaleBean.localMessageOf("fill.issue.year");
    }*/

    if (magazine_tblTable.getCategory() == null) {
      return LocaleBean.localMessageOf("fill.category");
    }
    //        if(magazine_tblTable.getPrice()==0.0)
    //            return "fill Price";
  /*  if (magazine_tblTable.getAppertype() == null) {
      return LocaleBean.localMessageOf("fill.appearance.type");
    }*/
    if (magazine_tblTable.getMagazineType() == null) {
      return LocaleBean.localMessageOf("fill.magazine.type");
    }
    if (magazine_tblTable.getOrig_issue_date() == null) {
      return LocaleBean.localMessageOf("fill.original.issue.date");
    }
   /* if (magazine_tblTable.getIssue_appear() == null && (!magazine_tblTable.getImediate().equals("Immediatly"))) {
      return LocaleBean.localMessageOf("determine.issue.shall.appear.type");
    }*/
    return "ok";

  }

  public void upload(FileUploadEvent event) throws Exception {
    //    public void upload() throws Exception {
    FacesMessage msg = null;
    if (currenttitle == null || currenttitle.equals("")) {
      msg = new FacesMessage(LocaleBean.localMessageOf("select.title.or.fill"), "");
      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
    } else {
      if (getId() == -1) {

        msg = new FacesMessage(LocaleBean.localMessageOf("fill.publisher.details.first"), "");
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);

        FacesContext.getCurrentInstance().addMessage(":gridform:pdfformid:growlpubtabsfileupload", msg);

        return;

      }
      Magazine_tblTable currentMagazine1 = FaceUtil.findBean("magazine_tblTable");
      currentMagazine1.setUser_id(getId());
      currentMagazine1.setTitle(currenttitle);

      //        setPdfFile(copyFile(file.getFileName(), file.getInputstream(),currentMagazine1));

      //        FacesMessage msg = new FacesMessage("Success! ", file.getFileName() + " is uploaded. Complete Details.");
      setPdfFile(copyFile(event.getFile().getFileName(), event.getFile().getInputstream(), currentMagazine1));

      msg = new FacesMessage(
          LocaleBean.localMessageOf("update.success") + "\n" + LocaleBean.localMessageOf("complete.details") +
          event.getFile().getFileName());


      // Do what you want with the file


      currentMagazine1.setFileName(event.getFile().getFileName());
      getUploadedMagazine().add(currentMagazine1);
      FacesContext.getCurrentInstance()
                  .getExternalContext()
                  .getSessionMap()
                  .put("magazine_tblTable", new Magazine_tblTable());
    }
    //        log.debug("FileUploadEvent fired...");
    FacesContext.getCurrentInstance().addMessage("gridform:pdfformid:growlpubtabsfileupload", msg);
  }

  private File copyFile(String fileName, InputStream in, Magazine_tblTable magazine_tblTable) throws Exception {

    //TODO I think we should create Directory for each publisher
    File rootDir = new File(destination);
    log.debug("DestDir " + destination);
    Date date = new Date(System.currentTimeMillis());
    //        LoginBean loginBean= (LoginBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("loginBean");

    //        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    //            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy_MM_dd_HH_mm_s_SSSS");

    String dirPath = rootDir + "/";

    File PDFNewDire = new File(dirPath);

    if (!rootDir.exists()) {
      if (!rootDir.mkdir()) {
        throw new Exception("Can't Create root pdf Directory...");

      }
    }

    dirPath += getId() + "/";
    File publisherDir = new File(dirPath);

    if (!publisherDir.exists()) {
      if (!publisherDir.mkdir()) {
        throw new Exception("Can't Create publisher  Directory...");

      }


    }
    dirPath += Statistics.PDF_DIR_DATE_TIME_FORMAT.format(new Date(System.currentTimeMillis())) + "/";

    PDFNewDire = new File(dirPath);
    if ((!PDFNewDire.exists()) && (!PDFNewDire.mkdir())) {
      throw new Exception("Can't Create  pdf Directory..." + PDFNewDire.getAbsolutePath());
    } else {
      log.debug("Dir created " + PDFNewDire.getPath());
    }


    // write the inputStream to a FileOutputStream
    File destinationFile = new File(PDFNewDire.getPath() + "/" + fileName);
    if (destinationFile.exists()) {
      return null;
    }
    OutputStream out = new FileOutputStream(destinationFile);

    int read = 0;
    byte[] bytes = new byte[1024];

    while ((read = in.read(bytes)) != -1) {
      out.write(bytes, 0, read);
    }

    in.close();
    out.flush();
    out.close();
    log.debug("Dire Path Setted to: " + dirPath);
    //        int intIndex=dirPath.indexOf("PDFs");
    //        dirPath=dirPath.substring(intIndex-1);
    //        log.debug("Dire Path Change to: "+dirPath);
    magazine_tblTable.setDir_path(dirPath);

    log.debug("New file created! in: " + destinationFile.getPath());
    return destinationFile;

  }

  public void onTabChange(TabChangeEvent event) {
    //        DBFacade.getInstance().addPublisherUser(newPubNaame,newPubEmail,newPubNaame+"123");
    //
    //        msg = new FacesMessage("New Publisher Added", "INFO MSG");
    //        msg.setSeverity(FacesMessage.SEVERITY_INFO);
    Tab tabView = event.getTab();


    log.debug(tabView.getId());
  }
  //    public void onTabClose(TabChangeEvent event)
  //    {
  //        DBFacade.getInstance().addPublisherUser(newPubNaame,newPubEmail,newPubNaame+"123");
  //
  //        msg = new FacesMessage("New Publisher Added", "INFO MSG");
  //        msg.setSeverity(FacesMessage.SEVERITY_INFO);
  //        Tab tabView =  event.getTab();
  //        log.debug(tabView.getId    ());
  //    }

  public void titleListener(SelectEvent event) {
    currenttitle = (String) event.getObject();
  }

  public void titleCahngeListener(ValueChangeEvent event) {

    currenttitle = (String) event.getNewValue();
  }

  public void addMagazine() {
    FacesMessage msg = null;
    Magazine_tblTable magazine_tblTable = FaceUtil.findBeanSession("magazine_tblTable");
    if (currenttitle == null || currenttitle.equals("")) {
      msg = new FacesMessage(LocaleBean.localMessageOf("title.fill.error"), "");
      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
      FacesContext.getCurrentInstance().addMessage(null, msg);

    }
    if (magazine_tblTable.getCategory() == null || magazine_tblTable.getCategory().equals("")) {
      msg = new FacesMessage(LocaleBean.localMessageOf("select.category.error"), "");
      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
      FacesContext.getCurrentInstance().addMessage(null, msg);

    } else {
      try {
        //                DBFacade.getInstance().addTitles(currenttitle,magazine_tblTable.getUser_id());
        HiberDBFacad.getInstance().addTitles(currenttitle, magazine_tblTable.getUser_id());
        msg = new FacesMessage(LocaleBean.localMessageOf("title.added"), "");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);

      } catch (Exception e) {
        log.error("Exception in: ", e);
        msg = new FacesMessage(LocaleBean.localMessageOf("title.add.exception"), "");
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, msg);

      }
    }
  }


  public boolean isCanNotUpload() {
    this.canNotUpload = (currenttitle == null || (currenttitle.length() <= 0));
    return this.canNotUpload;
  }

  public void setCanNotUpload(boolean canNotUpload) {
    this.canNotUpload = (currenttitle == null || (currenttitle.length() <= 0));
  }

  public void getIssuesFromDB() {


  }

  public void setSelectedIssueRepTitle(String title) {
    selectedTitleForRepIssues = title;
  }

  public String getSelectedTitleForRepIssues() {
    return selectedTitleForRepIssues;
  }

  public void setSelectedTitleForRepIssues(String selectedTitleForRepIssues) {
    this.selectedTitleForRepIssues = selectedTitleForRepIssues;
  }

  public ArrayList<MagazineRepSpecTitle> getMagazineRepSpecTitles() {
    return magazineRepSpecTitles;
  }

  public void setMagazineRepSpecTitles(ArrayList<MagazineRepSpecTitle> magazineRepSpecTitles) {
    this.magazineRepSpecTitles = magazineRepSpecTitles;
  }

  public String getSelectedIssueForSpecIssue() {
    return selectedIssueForSpecIssue;
  }

  public void setSelectedIssueForSpecIssue(String selectedIssueForSpecIssue) {
    this.selectedIssueForSpecIssue = selectedIssueForSpecIssue;
  }


  public void getTitleIssuesAndRedirectToSelectedTitle(Magazine_tblTable magazine_tblTable) {
    this.selectedTitleForRepIssues = magazine_tblTable.getTitle();
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
                      "/pubpanel/PubMagazineSpecificTitle.xhtml");
    } catch (IOException e) {
      log.error("Exception in: ", e);
    }
  }

  public void getGetIssueSelectedAndRedirectToSelectdeSpecIssue(MagazineRepSpecTitle magazineRepSpecTitle) {
    this.setSelectedCurrentMag_id(magazineRepSpecTitle.getMagazine_tblTable().getMagazine_id());
    getSelectedCurrentMagazine().clear();
    getSelectedCurrentMagazine().add(magazineRepSpecTitle);

    try {
      FacesContext.getCurrentInstance()
                  .getExternalContext()
                  .redirect(
                      FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
                      "/pubpanel/PubReportSpecIssue.xhtml");
    } catch (IOException e) {
      log.error("Exception in: ", e);
    }
  }

  public void getGetIssueSelectedAndRedirectToSelectdeSpecIssuePage(MagazineRepSpecTitle magazineRepSpecTitle) {
    this.setSelectedCurrentMag_id(magazineRepSpecTitle.getMagazine_tblTable().getMagazine_id());
    getSelectedCurrentMagazine().clear();
    getSelectedCurrentMagazine().add(magazineRepSpecTitle);

    try {
      FacesContext.getCurrentInstance()
                  .getExternalContext()
                  .redirect(
                      FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
                      "/pubpanel/PubReportSpecIssuePageView.xhtml");
    } catch (IOException e) {
      log.error("Exception in: ", e);
    }
  }

  public ArrayList<ReaderConversationPerPage> getListOfPageImageAdress() {

    if (this.listOfPageImageAdress == null || this.listOfPageImageAdress.size() <= 0 ||
        (currentMagUploaded != getSelectedCurrentMag_id())) {
      this.listOfPageImageAdress = new ArrayList();
      fillListOfPageImageAdressForReaders(selectedCurrentMag_id);
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

  public void setListOfPageImageAdress(ArrayList<ReaderConversationPerPage> listOfPageImageAdress) {
    this.listOfPageImageAdress = listOfPageImageAdress;
  }

  public void canUploadImage() {
    PubCompantDet pubCompantDet = ((PubCompantDet) FaceUtil.findBean("pubCompantDet"));
    if (!(isDataCompleted() && pubCompantDet.dataIsCompleted())) {
      try {
        FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                        FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
                        "/pubpanel/PubDetails.xhtml");

      } catch (IOException e) {
        log.error("Exception in: ", e);
      }
    }


  }

  public void isDataCompletedByPublisher() {
    PubCompantDet pubCompantDet = ((PubCompantDet) FaceUtil.findBean("pubCompantDet"));
    if (!(isDataCompleted() && pubCompantDet.dataIsCompleted())) {

      FacesMessage fm = new FacesMessage(
          FacesMessage.SEVERITY_ERROR,
          LocaleBean.localMessageOf("detail.completion.msg.text"),
          "");

      if (FacesContext.getCurrentInstance().getMessageList().size() <= 0) {
        FacesContext.getCurrentInstance().addMessage("prerenderform:userdeterr", fm);
      }
    }

  }

  public boolean isDataCompleted() {
    if (firstname == null || "".equals(firstname) ||
        lastname == null || "".equals(lastname) ||
        email == null || "".equals(email) ||
        phonecode == null || "".equals(phonecode) ||
        phonenum == null || "".equals(phonenum) ||
        streetname == null || "".equals(streetname) ||
        streetnum == null || "".equals(streetnum) ||
        citynum == null || "".equals(citynum) ||
        cityname == null || "".equals(cityname) ||
        country == null || "".equals(country)) {
      return false;
    }
    return true;
  }

  public void deleteSelectedMag(Magazine_tblTable magazine_tblTable) {

    boolean removeThis = getUploadedMagazine().remove(magazine_tblTable);
    FacesMessage msg = new FacesMessage(LocaleBean.localMessageOf("magazine.delete.error"), "");
    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
    if (removeThis) {
      try {
        File fl = new File(((Magazine_tblTable) magazine_tblTable).getDir_path());

        //                File flParent=new File(fl.getParent());
        FileUtils.forceDelete(fl);
        msg = new FacesMessage(LocaleBean.localMessageOf("success.delete.magazine"), "");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
      } catch (IOException e) {
        log.error(e);

      }
    }

    FacesContext.getCurrentInstance().addMessage(null, msg);

  }
}
