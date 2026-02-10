package ir.mjm.DBAO;

import ir.mjm.entities.MagazineType;
import ir.mjm.publisher.DescriptionChangeDialog;
import ir.mjm.publisher.KeywordChangeDialog;
import ir.mjm.util.AppearanceType;
import ir.mjm.util.FaceUtil;
import ir.mjm.util.LocaleBean;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

/**
 * Created by Serap on 6/9/14.
 */
@ManagedBean
@SessionScoped
public class Magazine_tblTable implements Serializable {
  static final Logger log = Logger.getLogger(Magazine_tblTable.class);

  private int magazine_id;
  private int user_id;
  private String publisher_name;

  private String category;
  private String uiCategory;
  private Date date_time;
  private String dir_path;
  private String title;
  private String issue_year;
  private int issue_num;
  private Date orig_issue_date;
  private String description = "";
  private boolean converted;
  private String fileName;
  private int maxPageNum;
  private int status;
  private String language;
  private int dlcount;
  private int favorcount;
  private boolean immediatedee;
  private double price;
  private MagazineType magazineType;
  private String uiMagazineType;
  private String magazineKeywords;

  private String imediate = "Immediatly";
  private String issue_appear_type = "Immediatly";
  private Date issue_appear;
  private String setSelectedIssueRep;

  public int getFavorcount() {
    return favorcount;
  }

  public void setFavorcount(int favorcount) {
    this.favorcount = favorcount;
  }

  public String getImediate() {

    return imediate;
  }

  public void setImediate(String imediate) {
    this.imediate = imediate;
  }


  public boolean isConverted() {
    return converted;
  }

  public void setConverted(boolean converted) {
    this.converted = converted;
  }


  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }


  private AppearanceType appertype;

  public AppearanceType getAppertype() {
    return appertype;
  }

  public void setAppertype(AppearanceType appertype) {
    this.appertype = appertype;
  }

  public void setAppertype(String apperTypeName) {
    this.appertype = AppearanceType.nameToAppearanceType(apperTypeName);
  }

  public String getUiAppertype() {
    return this.appertype != null ? this.appertype.localName() : null;
  }

  public void setUiAppertype(final String uiAppertype) {
    this.appertype = AppearanceType.localNameToAppearanceType(uiAppertype);
  }

  public String getDescription() {
    if (description == null) {
      description = "";
    }
    return description;
  }

  public void setDescription(String description) {
    if (description == null) {
      description = "";
    }

    this.description = description;
  }


  //    public File getPdfFile() {
  //        return new File(getFileName());
  //    }
  //
  //
  //    public void setPdfFile(File pdfFile) {
  //        this.fileName = pdfFile.getPath();
  //    }


  public void changeCat() {

    log.debug("Selected cat: " + getCategory());
  }


  public Date getDate_time() {
    return date_time;
  }

  public void setDate_time(Date date_time) {
    this.date_time = date_time;
  }


  public int getIssue_num() {
    return issue_num;
  }

  public void setIssue_num(int issue_num) {
    this.issue_num = issue_num;
  }

  public int getMagazine_id() {
    return magazine_id;
  }

  public void setMagazine_id(int magazine_id) {
    this.magazine_id = magazine_id;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public String getCategory() {
    return category;
  }

  public String getUiCategory() {
     uiCategory=LocaleBean.localMessageOf(category);
    uiCategory=uiCategory==null?"":uiCategory;
    return uiCategory;
  }

  public void setUiCategory(final String uiCategory) {
    this.uiCategory = uiCategory;
    this.category= LocaleBean.keyOfLocalString(uiCategory);
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getPublisher_name() {

    return publisher_name;
  }

  public void setPublisher_name(String publisher_name) {
    this.publisher_name = publisher_name;
    log.debug("publisher_name: " + publisher_name);
  }


  public String getDir_path() {
    return dir_path;
  }

  public void setDir_path(String dir_path) {
    this.dir_path = dir_path;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getIssue_year() {
    return issue_year;
  }

  public void setIssue_year(String issue_year) {

    this.issue_year = issue_year;

  }

  public Date getOrig_issue_date() {
    return orig_issue_date;
  }

  public void setOrig_issue_date(Date orig_issue_date) {
    this.orig_issue_date = orig_issue_date;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Date getIssue_appear() {
    return issue_appear;
  }


  public void setIssue_appear(Date issue_appear) {
    this.issue_appear = issue_appear;
  }


  public int getMaxPageNum() {
    if (maxPageNum > 0) {
      return maxPageNum;
    }

    setMaxPageNum(Statistics.getMaxPageNumber(getDir_path()));
    return maxPageNum;
  }

  public void setMaxPageNum(int maxPageNum) {
    this.maxPageNum = maxPageNum;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public int getDlcount() {
    return dlcount;
  }

  public void setDlcount(int dlcount) {
    this.dlcount = dlcount;
  }

  public void handleDateSelectOrigIssue(SelectEvent event) {
    setOrig_issue_date((Date) event.getObject());
  }

  public void handleEditDate_Time(SelectEvent event) {
    setDate_time((Date) event.getObject());
  }

  public void handleDateSelectissue_year(SelectEvent event) {
    setIssue_year((String) event.getObject());
  }

  public void handleDateSelectissue_appear(SelectEvent event) {
    setIssue_appear((Date) event.getObject());
  }

  public void issueNumberValueChangeListenerMethod(AjaxBehaviorEvent event) {
    setIssue_num((Integer) event.getComponent().getAttributes().get("value"));

  }

  public void nameOfTitleValueChangeListenerMethod(AjaxBehaviorEvent event) {
    setDescription((String) event.getComponent().getAttributes().get("value"));

  }

  public void changeIssueAppear(final AjaxBehaviorEvent event) {
    setIssue_appear_type((String) event.getComponent().getAttributes().get("value"));
    immediatedee = getIssue_appear_type().equals(imediate);

  }

  public boolean isImmediatedee() {
    immediatedee = getIssue_appear_type().equals(imediate);
    if (immediatedee) {
      issue_appear = new Date();
    }
    return immediatedee;
  }

  public void setImmediatedee(boolean immediatedee) {
    this.immediatedee = getIssue_appear_type().equals(imediate);
  }

  public String getIssue_appear_type() {
    return issue_appear_type;
  }

  public void setIssue_appear_type(String issue_appear_type) {
    this.issue_appear_type = issue_appear_type;
  }

  public void setSelectedIssueRep() {
    ((PubUserDet) FaceUtil.findBeanSession("pubUserDet")).setSelectedIssueRepTitle(getTitle());

  }

  public MagazineType getMagazineType() {
    return magazineType;
  }

  public String getUiMagazineType() {
    return magazineType!=null?magazineType.localName():null;
  }

  public void setUiMagazineType(final String uiMagazineType) {
    this.magazineType =MagazineType.localNameToAppearanceType(uiMagazineType);
  }

  public void setMagazineType(final MagazineType magazineType) {
    this.magazineType = magazineType;
  }

  public String getMagazineKeywords() {
    return magazineKeywords;
  }

  public void setMagazineKeywords(final String magazineKeywords) {
    this.magazineKeywords = magazineKeywords;
  }

  public void showdialog(ActionEvent actionEvent) {

    initKeywordsMBean(actionEvent);

    Map<String, Object> options = new HashMap<String, Object>();
     options.put("closable", true);
     options.put("modal", true);
     options.put("draggable", false);
     options.put("resizable", false);

     RequestContext.getCurrentInstance().openDialog("keywordPanel", options, null);
  }

  public void initKeywordsMBean(ActionEvent actionEvent) {
    log.debug("in initKeywordsMBean ");
    ((KeywordChangeDialog) FaceUtil.findBean("keywordChangeDialog")).initMAgazineTbl(this);
  }
  public void initDescriptionMBean(ActionEvent actionEvent) {
    log.debug("in initDescriptionMBean ");
    ((DescriptionChangeDialog) FaceUtil.findBean("descriptionChangeDialog")).initMAgazineTbl(this);
  }
}
