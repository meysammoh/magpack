package ir.mjm.DBAO;

import ir.mjm.DBAO.hiber.BannersTblHiberEntity;
import ir.mjm.DBAO.hiber.CategoriesTblHiberEntity;
import ir.mjm.entities.LoginBean;
import ir.mjm.entities.MagazineType;
import ir.mjm.util.AppearanceType;
import ir.mjm.util.FaceUtil;
import ir.mjm.util.LocaleBean;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 * Created by Serap on 6/18/14.
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class Statistics {
  static final Logger log = Logger.getLogger(Statistics.class);
  private static final String EMAIL_PATTERN =
      "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
      + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
  private static final String CATEGORIES_IMAGES_PARENT_DIR = "/categories";
  private static final String BANNER_IMAGES_PARENT_DIR = "/banners";
  public static String appPath;
  public static String configDir;
  public static File hiberConfigXml;
  public final static SimpleDateFormat PDF_DIR_DATE_TIME_FORMAT = new SimpleDateFormat ("yyyy_MM_dd__hh_mm_ss_a_zzz");
  public static String dateFullForm = "yyyy-MM-dd HH:mm:ss";
  public static String dateNonFullForm = "yyyy-MM-dd";
  public static String dateYyyyForm = "yyyy";
  public static String HHmmssForm = "HH:mm:ss";

  //orig_issue_date and issue_appear
  public static java.text.SimpleDateFormat nonFullDateFormat = new java.text.SimpleDateFormat(dateNonFullForm);

  //date_time
  public static java.text.SimpleDateFormat FullDateFormat = new java.text.SimpleDateFormat(dateFullForm);

  //Page Types
  public static final int AD = 2;
  public static final int MIXED = 1;
  public static final int TEXT_ONLY = 0;
  public static final String AD_STRING = "Ad";
  public static final String MIXED_STRING = "Mixed";
  public static final String TEXT_ONLY_STRING = "Text Only";
  public static final String NO_DEF_STRING = "NO Def";


  public static java.text.SimpleDateFormat yyyy = new java.text.SimpleDateFormat(dateYyyyForm);
  public static java.text.SimpleDateFormat hHmmss = new java.text.SimpleDateFormat(HHmmssForm);

  String width = "1200px";

  @PostConstruct
  public void init() {
    initLog4j();
    initGost4jProrpety();
  }

  private void initGost4jProrpety() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(getConfigDirPath() + "application_config.properties");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    if (fileReader == null) {
      System.out.println("Can not find application_config.properties File in config dir.");
      return;
    }


    Properties properties = new Properties();
    try {
      properties.load(fileReader);
      System.setProperty("jna.library.path", properties.getProperty("gostscriptbinpath"));


    } catch (IOException e) {
      log.error("Can not Load application_config.properties File in config dir.");

      e.printStackTrace();

    }
  }

  public static String getEmailPattern() {
    return EMAIL_PATTERN;
  }

  private void initLog4j() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(getConfigDirPath() + "log4j.properties");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    if (fileReader == null) {
      System.out.println("Can not find Log4j File in config dir.");
      return;
    }


    Properties properties = new Properties();
    try {
      properties.load(fileReader);
      properties.setProperty("log4j.appender.R.File", getConfigDirPath() + "/log/loggingFile.log");
      //            properties.setProperty("log4j.appender.R.RollingPolicy","org.apache.log4j.rolling.TimeBasedRollingPolicy");
      //            properties.setProperty("log4j.appender.R.RollingPolicy.FileNamePattern","/logs/loggingFile_%d{yyyy_MM_dd}.log");

      PropertyConfigurator.configure(properties);

    } catch (IOException e) {
      log.error("Can not Load Log4j File in config dir.", e);


    }
  }


  public Statistics() {
    //        appPath= getClass().getResource("").getPath();
    //        appPath=appPath.substring(0,appPath.lastIndexOf("WEB-INF"));
    //        if(appPath.charAt(2)==':')
    //            appPath=appPath.substring(1);
    appPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
    for (File f : (new File(appPath)).listFiles()) {
      if (f.getName().equals("config")) {
        configDir = f.getPath() + (appPath.indexOf("\\") >= 0 ? "\\" : "/");
        break;
      }

    }
    log.debug("App Path_________________" + appPath);
    log.debug("Config Path_________________" + configDir);

  }

  private String[] countries =
      {"Afghanistan", "Åland Islands", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Ascension and Tristan da Cunha", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivarian Republic of", "Bolivia",
          "Bonaire",
          "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "British", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros",
          "Congo", "Cook Islands", "Costa Rica", "Côte d'Ivoire", "Croatia", "Cuba", "Curaçao", "Cyprus", "Czech Republic", "Democratic People's Republic of", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Federated States of", "Fiji", "Finland", "France", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guernsey", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard Island and McDonald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran",
          "Iraq", "Ireland", "Islamic Republic of", "Isle of Man", "Israel", "Italy", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati",
          "Korea",
          "Kuwait", "Kyrgyzstan", "Lao People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macao", "Macedonia",
          "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia",
          "Moldova",
          "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Occupied", "Oman", "Pakistan", "Palau", "Palestinian Territory",
          "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Plurinational State of", "Poland", "Portugal", "Province of China", "Puerto Rico", "Qatar", "Republic of", "Republic of", "Réunion", "Romania", "Russian Federation", "Rwanda", "Saint Barthélemy", "Saint Helena",
          "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin (French part)", "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Sint Eustatius and Saba", "Sint Maarten (Dutch part)", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan",
          "Tajikistan", "Tanzania",
          "Thailand", "the Democratic Republic of the", "the former Yugoslav Republic of", "Timor-Leste", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "U.S.", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United Republic of", "United States Minor Outlying Islands", "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela",
          "Viet Nam", "Virgin Islands", "Virgin Islands",
          "Wallis and Futuna", "Western Sahara", "Yemen", "Zambia", "Zimbabwe"
      };


  public ArrayList<String> getAllTitles(String query) {
    try {
      ((PubUserDet) FaceUtil.findBean("pubUserDet")).setCurrenttitle(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
    ArrayList<String> allTitles = new ArrayList<String>();

    log.debug("    return DBFacade.getInstance().getAllTitles();");
    LoginBean loginBean = FaceUtil.findBeanSession("loginBean");
    if ((loginBean != null) && (loginBean.getUser_id() > -1))
    //            for(String s:DBFacade.getInstance().getAllTitles(loginBean.getUser_id()))
    {
      for (String s : HiberDBFacad.getInstance().getAllTitles(loginBean.getUser_id())) {
        if (s.toLowerCase().startsWith(query.toLowerCase())) {
          if (!allTitles.contains(s)) {
            allTitles.add(s);
          }
        }
      }
    }
    return allTitles;
  }

  public List<String> getAllCategoriesInLocalString(String query) {
    List<String> ret = new ArrayList<String>();
    //        try {
    //            for(String s:DBFacade.getInstance().getCategouries())
    for (String s : HiberDBFacad.getInstance().getCategouries()) {
      String locals=LocaleBean.localMessageOf(s);
      if (locals.toLowerCase().startsWith(query.toLowerCase())) {
        ret.add(locals);
      }
    }
    //        } catch (SQLException e) {
    //
    //        }

    return ret;
  }

  public ArrayList<String> getAllCategoriesNonQuery() {
    //        try {
    //            return DBFacade.getInstance().getCategouries();
    return HiberDBFacad.getInstance().getCategouries();
    //        } catch (SQLException e) {
    //e.printStackTrace();        }
    //        return null;
  }

  public ArrayList<String> getYearsRange() {
    ArrayList<String> ret = new ArrayList<String>();
    for (int i = 0; i < 50; i++) {
      ret.add((2026 - i) + "");
    }
    return ret;
  }

  public void setAllTitles(String[] allTitles) {
    AllTitles = allTitles;
  }

  private String[] AllTitles;

  public ArrayList<String> getAppreanceTypes(String query) {
    ArrayList<String> ret = new ArrayList<String>();
    LocaleBean local = FaceUtil.findBean("localeBean");
    for (AppearanceType a : appreanceTypes) {
      if (LocaleBean.localMessageOf(a.localName()).toLowerCase().startsWith(query.toLowerCase())) {
        ret.add(a.localName());
      }
    }
    return ret;
  }

  public String[] getAppreanceTypesSel() {

    return AppearanceType.appearanceTypesName();
  }


  AppearanceType[] appreanceTypes = AppearanceType.values();

  public String[] getCountries() {
    return countries;
  }

  public void setCountries(String[] countries) {
    this.countries = countries;
  }

  ArrayList<String> retCount = null;

  public ArrayList<String> getcou(String query) {
    log.debug("    public ArrayList<String> getcou(String query){\n");
    retCount = new ArrayList<String>();
    for (String cn : countries) {
      if (cn.toLowerCase().startsWith(query.toLowerCase())) {
        retCount.add(cn);
      }
    }

    return retCount;
  }


  public String handleFlow(FlowEvent event) {

    String currentstr = event.getOldStep();
    String defNext = event.getNewStep();
    log.debug("1 current: " + currentstr + "\t 1 new Step:" + defNext);
    String nexstr = currentstr;
    List<UIComponent> children = event.getComponent().findComponent(currentstr).getParent().getChildren();
    for (int i = 0; i < children.size(); i++) {
      if (children.get(i).getId().equals(currentstr)) {
        nexstr = children.get(i + 1).getId();
        break;
      }
    }


    log.debug("2 current: " + currentstr + "\t2 new Step: " + nexstr);

    //        if(skip) {
    //            skip = false;   //reset in case user goes back
    //            return "confirm";
    //        }
    //        else {
    return nexstr;
    //        }
  }


  public void isIssueValid(FacesContext ctx, UIComponent component, Object value)
      throws ValidatorException {
    String issuenum = value.toString();

    // if the username hasn't changed, is valid
    if (issuenum.matches("[0-9]+]")) {
      return;
    }

    // if the username has changed, but it is
    // available in the database, is correct

    throw new ValidatorException(new FacesMessage("Issue Number is not valid"));


  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }


  public static String getApplicationPath() {

    return appPath;
  }

  public static String getConfigDirPath() {

    return configDir;
  }

  public static File getHiberConfigFile() {
    if (hiberConfigXml == null) {
      for (File f : (new File(configDir)).listFiles()) {
        if (f.getName().equals("hibernate.cfg.xml")) {
          hiberConfigXml = f;
          break;
        }

      }

    }
    return hiberConfigXml;
  }

  public static String getIssueAppearWhereLimit() {
    return "issue_appear <= '" + nonFullDateFormat.format(new Date()) + "'";
  }

  public static String getIssuePublisherStatusLimit() {
    return "pubstatus == 0 ";
  }

  public String getNonFullFormatDate(Date date) {
    return nonFullDateFormat.format(date);
  }

  public String getInHHmmssFormat(int sec) {
    return hHmmss.format(new Date(sec * 1000));
  }

  public String getInHHmmssFormatDouble(double sec) {
    int second = (int) sec;
    int HH = second / (60 * 60);
    int mm = (second / (60) % 60);
    int ss = (second % (60));


    return HH + ":" + mm + ":" + ss;
  }

  public String encode(String val) {
    try {
      val = URLDecoder.decode(val, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return val;
  }

  public static int getMaxPageNumber(String dir_path) {
    int ret = 0;

    if (dir_path == null || dir_path == "") {
      return 0;
    }
    String tmpdir = dir_path;
    if (!dir_path.startsWith(Statistics.getApplicationPath())) {
      tmpdir = Statistics.getApplicationPath() + dir_path;
    }
    File dir = new File(tmpdir);
    ArrayList<File> allfiless = new ArrayList<File>();

    if (dir.getName().toLowerCase().endsWith(".pdf")) {
      dir = dir.getParentFile();
    }
    if (dir != null) {
      File[] listFiles = dir.listFiles();
      if (listFiles != null) {
        Collections.addAll(allfiless, dir.listFiles());
      }
    }
    while (!allfiless.isEmpty()) {
      File f = allfiless.remove(0);
      if (f.isDirectory()) {
        Collections.addAll(allfiless, f.listFiles());
        continue;
      }
      String fname = f.getName().toLowerCase();
      if (fname.startsWith("s") && (fname.endsWith(".png") || fname.endsWith(".jpg"))) {
        ret++;
      }

    }
    return ret;
  }

  public static boolean isIssueAppearNew(String issueAppear) {

    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, (-7));
    Date aTimeAgo = cal.getTime();
    int comp = issueAppear.compareTo(Statistics.nonFullDateFormat.format(aTimeAgo));
    return comp > 0 ? true : false;
  }

  public static boolean isPremiumNow(String chargeennd) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, 0);
    Date aTimeAgo = cal.getTime();
    int comp = chargeennd.compareTo(Statistics.FullDateFormat.format(aTimeAgo));
    return comp < 0 ? false : true;
  }

  public static int getDayUntilPublish(String issueAppear) {
    int days = -1;
    try {
      days = daysBetween(new Date(), nonFullDateFormat.parse(issueAppear));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    if (days < 0) {
      return 0;
    } else {
      return days;
    }
  }

  public static int daysBetween(Date d1, Date d2) {
    return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
  }

  public static String getFileNameFromDirPath(String dirPath) {
    int lIndex = dirPath.lastIndexOf("/");
    lIndex = lIndex < 0 ? dirPath.lastIndexOf("\\") : lIndex;
    return dirPath.substring(lIndex + 1);
  }


  public static String getPageType(int pageType) {
    if (pageType == AD) {
      return AD_STRING;
    } else if (pageType == MIXED) {
      return MIXED_STRING;
    } else if (pageType == TEXT_ONLY) {
      return TEXT_ONLY_STRING;
    }
    return NO_DEF_STRING;

  }

  public static String suggestionImageUrl(CategoriesTblHiberEntity categoriesTblHiberEntity) {
    return Statistics.appPath + categoriesTblHiberEntity.getImgPath();
  }

  public static String bannerImageUrl(BannersTblHiberEntity bannersTblHiberEntity) {
    if (bannersTblHiberEntity.getImgPath() != null &&
        bannersTblHiberEntity.getImgPath().contains(BANNER_IMAGES_PARENT_DIR)) {
      return Statistics.appPath + bannersTblHiberEntity.getImgPath();
    } else {
      return BANNER_IMAGES_PARENT_DIR + File.separator + bannersTblHiberEntity.getId();
    }
  }

  public static void updateCategoryImageAndThisParam(
      final CategoriesTblHiberEntity categoriesTblHiberEntity,
      final UploadedFile file) throws Exception {
    final File catImageParentDir;
    if (categoriesTblHiberEntity.getImgPath() == null) {
      catImageParentDir = categoryParentDire(categoriesTblHiberEntity);
      if (!catImageParentDir.exists()) {
        try {
          FileUtils.forceMkdir(
              catImageParentDir);
        } catch (IOException e) {
          log.error(e);
          throw e;
        }
      }
      categoriesTblHiberEntity.setImgPath(
          CATEGORIES_IMAGES_PARENT_DIR + File.separator + categoriesTblHiberEntity.getCatId() + File.separator +
          categoriesTblHiberEntity.getCategory());
    }

    File oldImageFile = new File(Statistics.suggestionImageUrl(categoriesTblHiberEntity));
    boolean oldFileDisposed = true;
    if (oldImageFile.exists() && oldImageFile.isFile()) {
      oldFileDisposed = oldImageFile.delete();
    }
    if (oldImageFile.exists() && !oldFileDisposed) {
      log.error("Can not delete file of category( file: " + oldImageFile.getPath() + " )");
      throw new Exception("Can not delete file of category( file: " + oldImageFile.getPath() + " )");
    }
    try {
      FileUtils.copyInputStreamToFile(file.getInputstream(), new File(suggestionImageUrl(categoriesTblHiberEntity)));
    } catch (IOException e) {
      log.error(e);
      throw e;
    }
  }

  private static File categoryParentDire(final CategoriesTblHiberEntity categoriesTblHiberEntity) {
    return new File(
        Statistics.appPath + CATEGORIES_IMAGES_PARENT_DIR + File.separator + categoriesTblHiberEntity.getCatId());
  }

  public static String[] magazineTypes() {
    return MagazineType.localValues();
  }

  public static void updateBannerImageAndThisParam(
      final BannersTblHiberEntity bannersTblHiberEntity,
      final UploadedFile file) throws Exception {
    final File bannerImageParentDir;
    bannerImageParentDir = bannerParentDire(bannersTblHiberEntity);
    if (!bannerImageParentDir.exists()) {
      try {
        FileUtils.forceMkdir(
            bannerImageParentDir);
      } catch (IOException e) {
        log.error(e);
        throw e;
      }
    }
    if (bannersTblHiberEntity.getImgPath() == null ||
        !bannersTblHiberEntity.getImgPath().contains(BANNER_IMAGES_PARENT_DIR)) {
      bannersTblHiberEntity.setImgPath(
          BANNER_IMAGES_PARENT_DIR + File.separator + bannersTblHiberEntity.getId());
    }

    File oldImageFile = new File(Statistics.bannerImageUrl(bannersTblHiberEntity));
    boolean oldFileDisposed = true;
    if (oldImageFile.exists() && oldImageFile.isFile()) {
      oldFileDisposed = FileUtils.deleteQuietly(oldImageFile);
    }
    if (oldImageFile.exists() && !oldFileDisposed) {
      log.error("Can not delete file of Banner( file: " + oldImageFile.getPath() + " )");
      throw new Exception("Can not delete file of Banner( file: " + oldImageFile.getPath() + " )");
    }
    try {
      FileUtils.copyInputStreamToFile(file.getInputstream(), new File(bannerImageUrl(bannersTblHiberEntity)));
    } catch (IOException e) {
      log.error(e);
      throw e;
    }
  }

  private static File bannerParentDire(final BannersTblHiberEntity bannersTblHiberEntity) {
    return new File(
        Statistics.appPath + BANNER_IMAGES_PARENT_DIR);


  }

  public static File bannerFile(final BannersTblHiberEntity banner) {
    return new File(bannerImageUrl(banner));

  }

  public static void removeBanner(final BannersTblHiberEntity bannersTblHiberEntity) throws IOException {
    final File bannerFile = bannerFile(bannersTblHiberEntity);
    if(bannerFile!=null&&bannerFile.exists()){
      FileUtils.forceDelete(bannerFile);
    }
    HiberDBFacad.getInstance().removeBanner(bannersTblHiberEntity);
  }
}
