package ir.mjm.DBAO;

import ir.mjm.publisher.MagazineBean;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * Created by Serap on 6/8/14.
 */
@ManagedBean
@ApplicationScoped
public class DBFacade {
  static final Logger log = Logger.getLogger(DBFacade.class);

  private LinkedBlockingDeque<AbstractMap.SimpleEntry<Long, Connection>> connectionsQeueu;
  private long databaseTimeout = 600000;
  private final Object queueLock = new Object();
  private Statement statement;
  private PreparedStatement preparedStatement;
  private ResultSet resultSet;
  private String destination;


  private String usersTable = "users";


  private String dbname;
  private String username;
  private String pass;
  private String url;
  private String magazineTable = "magazine_tbl";
  private String connectionString;
  private static DBFacade ourInstance;


  //    public boolean addCatString(String cat) {
  //        if (allCatsCache ==null)
  //            allCatsCache =getCatStringsSet();
  //        if(allCatsCache.contains(cat))
  //            return false;
  //
  //        return allCatsCache.add(cat);
  //    }

  //    public void setCatStringsSet(ArrayList<String> catStringsSet) {
  //        for(String f:catStringsSet)
  //            addCatString(f);
  //    }

  private static HashSet<String> allCatsCache;
  private static HashSet<String> sugCatsCache;
  //    private HashMap<Integer ,String> titleStringsSet;


  private String port = "3306";

  public static DBFacade getInstance() {
    if (ourInstance == null) {
      ourInstance = new DBFacade();

      ourInstance.destination = Statistics.getApplicationPath();

    }
    return ourInstance;
  }

  private DBFacade() {
    initDbConfig();
    connectionString = url + ":" + port + "/" + dbname + "?user=" + username + "&password=" + pass;
    initConnectionPool();

  }

  private void initDbConfig() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(Statistics.getConfigDirPath() + "dbconfig.properties");
    } catch (FileNotFoundException e) {
      log.error(e);
    }
    if (fileReader == null) {
      log.error("Can not find dbconfig.properties File in config dir.");
      return;
    }


    Properties properties = new Properties();
    try {
      properties.load(fileReader);
      url = properties.getProperty("url");
      port = properties.getProperty("port");
      dbname = properties.getProperty("dbname");
      username = properties.getProperty("username");
      pass = properties.getProperty("pass");

    } catch (IOException e) {
      log.error("Can not Load dbconfig.properties properties in config dir.", e);

    }
  }

  private void initConnectionPool() {
    if (connectionsQeueu == null) {
      try {
        connectionsQeueu = new LinkedBlockingDeque<>();
        Class.forName("com.mysql.jdbc.Driver");
        for (int i = 0; i < 20; i++) {
          Connection connectionTmp = DriverManager.getConnection(connectionString);
          connectionTmp.setAutoCommit(false);
          connectionsQeueu.put(new AbstractMap.SimpleEntry<>(new Date().getTime(), connectionTmp));
        }
      } catch (ClassNotFoundException e) {
        log.error(e);
      } catch (SQLException e) {
        log.error(e);
      } catch (InterruptedException e) {
        log.error(e);
      }
    }

  }

  private AbstractMap.SimpleEntry<Long, Connection> connect() throws SQLException {
    //       synchronized (queueLock){
    //           while (connectionsQeueu.size()<=0)
    //           try {
    //               Thread.sleep(200);
    //           } catch (InterruptedException e) {
    //              log.error(e);
    //           }
    //       }

    AbstractMap.SimpleEntry<Long, Connection> ret = null;
    try {
      ret = connectionsQeueu.take();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return checkTimeout(ret);

  }

  private AbstractMap.SimpleEntry<Long, Connection> checkTimeout(AbstractMap.SimpleEntry<Long, Connection> ret) {

    try {
      if (((System.currentTimeMillis() - ret.getKey()) > databaseTimeout) || ret.getValue().isClosed()) {
        Connection connectionTmp = null;
        try {
          try {
            if (!ret.getValue().isClosed()) {
              ret.getValue().close();
            }
          } catch (SQLException e) {
            log.error(e);
          }
          Class.forName("com.mysql.jdbc.Driver");

          connectionTmp = DriverManager.getConnection(connectionString);
          connectionTmp.setAutoCommit(false);
          ret = new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), connectionTmp);
        } catch (SQLException e) {
          log.error(e);
          ret = null;
        } catch (ClassNotFoundException e) {
          log.error(e);
        }

      } else {
        ret = new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), ret.getValue());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ret;

  }


  public void closeConnection(AbstractMap.SimpleEntry<Long, Connection> ret) throws SQLException {
    try {
      connectionsQeueu.put(ret);
    } catch (InterruptedException e) {
      log.error(e);
    }
  }


  public UsersTable getUserByName_onlyUserID(String userName) throws SQLException {
    String ssql = "Select user_id from magazinedb." + usersTable + " where username='" + userName + "' ";
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(ssql);
    resultSet = preparedStatement.executeQuery();
    if (!resultSet.next()) {
      return null;
    }
    UsersTable ret = new UsersTable();
    ret.setUser_id(resultSet.getInt("user_id"));
    //        log.error(" find user ID:"+ret);

    closeConnection(hasConnection);
    return ret;
  }


  public MagazineBean[] getMagazineByPub_ID(int pubUser_ID) throws SQLException {


    String ssql = "Select * from " + dbname + "." + magazineTable + " where user_id=" + pubUser_ID + " ";
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(ssql);
    resultSet = preparedStatement.executeQuery();
    if (resultSet.getFetchSize() <= 0) {
      return null;
    }
    MagazineBean[] ret = new MagazineBean[resultSet.getFetchSize()];
    int i = 0;
    while (resultSet.next()) {
      ret[i].setUser_id(resultSet.getInt("user_id"));
      ret[i].setMagazine_id(resultSet.getInt("magazine_id"));
      ret[i].setMagazineName(resultSet.getString("name"));


      try {
        ret[i].setDate(Statistics.FullDateFormat.parse(resultSet.getString("date_time")));
      } catch (ParseException e) {
        log.error("Exception in: ", e);
      }
      ret[i].setPath(resultSet.getString("dir_path"));
      i++;
    }

    closeConnection(hasConnection);

    return ret;
  }

  public String getMagazinePath(int magazine_id) throws Exception {

    String nowString = Statistics.nonFullDateFormat.format(new Date());
    String ssql = "Select dir_path from " + magazineTable + " where pubstatus=0 and issue_appear<='" + nowString +
                  "' and  magazine_id=" + magazine_id + ";";
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(ssql);
    resultSet = preparedStatement.executeQuery();
    if (!resultSet.next()) {
      return null;
    }

    closeConnection(hasConnection);
    return resultSet.getString("dir_path");


  }


  public Magazine_tblTable[] getMagazinesTitlteInCategory(int catid) throws Exception {
    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();


    String selSql = "select category from categories_tbl where cat_id=" + catid;
    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    resultSet1 = preparedStatement.executeQuery();
    ArrayList<Magazine_tblTable> ret = new ArrayList<Magazine_tblTable>();
    if (resultSet1.next()) {
      String category = resultSet1.getString("category");
      log.debug("category : " + category);
      String nowString = Statistics.nonFullDateFormat.format(new Date());
      String selSql2 =
          "select *  from magazine_tbl where pubstatus=0 and issue_appear<='" + nowString + "' and  category='" +
          category + "'";
      preparedStatement = hasConnection.getValue().prepareStatement(selSql2);
      resultSet1 = preparedStatement.executeQuery();
      while (resultSet1.next()) {
        Magazine_tblTable mgtbl = new Magazine_tblTable();
        mgtbl.setMagazine_id(resultSet1.getInt("magazine_id"));
        mgtbl.setCategory(resultSet1.getString("category"));
        mgtbl.setTitle(resultSet1.getString("title"));
        mgtbl.setPrice(resultSet1.getDouble("price"));
        ret.add(mgtbl);
      }
    }

    closeConnection(hasConnection);
    return ret.toArray(new Magazine_tblTable[ret.size()]);

  }


  //    Category
  public ArrayList<String> getSugCats() throws SQLException {
    ArrayList ret = new ArrayList();

    if (sugCatsCache != null && !sugCatsCache.isEmpty()) {

      ret.addAll(sugCatsCache);
      return ret;
    } else if (sugCatsCache == null) {
      sugCatsCache = new HashSet<String>();
    }

    String ssql = "Select * from " + "categories_tbl where sug= ?";
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(ssql);
    preparedStatement.setBoolean(1, true);
    resultSet = preparedStatement.executeQuery();

    while (resultSet.next()) {
      sugCatsCache.add(resultSet.getString("category"));
    }
    ret.addAll(sugCatsCache);

    closeConnection(hasConnection);
    return ret;

  }

  public ArrayList<String> getCategouries() throws SQLException {

    String ssql = "Select * from " + "categories_tbl";
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(ssql);
    resultSet = preparedStatement.executeQuery();

    ArrayList ret = new ArrayList();
    while (resultSet.next()) {
      ret.add(resultSet.getString("category"));
    }
    closeConnection(hasConnection);

    return ret;
  }

  public HashMap<Integer, String> getAllCategories() throws Exception {

    String selSql = "select * from categories_tbl";
    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    resultSet1 = preparedStatement.executeQuery();
    HashMap ret = new HashMap<Integer, String>();
    if (allCatsCache == null) {
      allCatsCache = new HashSet<String>();
    }
    while (resultSet1.next()) {
      String tmpCat = resultSet1.getString("category");

      if ((!allCatsCache.contains(tmpCat))) {
        allCatsCache.add(tmpCat);
      }
      ret.put(resultSet1.getInt("cat_id"), tmpCat);
    }

    closeConnection(hasConnection);
    return ret;

  }


  //    //    utility
  //    public List<String> getGetAllTitles(String query) {
  //        ArrayList<String> ret=new ArrayList<String>();
  //        for(String tit: getAllTitles()){
  //            if(tit.toLowerCase().contains(query.toLowerCase()))
  //                ret.add(tit);
  //        }
  //
  //        return ret;
  //    }
  public List<String> getCategories(String query) {
    ArrayList<String> ret = new ArrayList<String>();
    //        try {
    for (String tit : HiberDBFacad.getInstance().getCategouries()) {
      if (tit.toLowerCase().contains(query.toLowerCase())) {
        ret.add(tit);
      }
    }
    //        } catch (SQLException e) {
    //            log.error("Exception in: ", e);
    //        }

    return ret;
  }


  //TODO complete this

  //    initilize
  public ArrayList<String> getCatStringsSet() {
    if (allCatsCache == null) {
      allCatsCache = new HashSet<String>();

      //            try {
      allCatsCache.addAll(HiberDBFacad.getInstance().getCategouries());
      //            } catch (SQLException e) {
      //                log.error("Exception in: ", e);
      //            }
      log.debug(allCatsCache.size() + " Categories Fetched.");
    }
    ArrayList<String> ret = new ArrayList<String>();
    ret.addAll(allCatsCache);

    return ret;
  }


  //Magazine_tblTable
  public ArrayList<Magazine_tblTable> getMagazineByUserId(int user_id, boolean forMobileUser) throws SQLException {
    return HiberDBFacad.getInstance().getMagazineByUserId(user_id, -1, forMobileUser);
  }

  public ArrayList<Magazine_tblTable> getMagazineByMagID(int magazine_id, boolean forMobileUser) throws SQLException {
    return HiberDBFacad.getInstance().getMagazineByUserId(-1, magazine_id, forMobileUser);
  }

  public ArrayList<Magazine_tblTable> getMagazineInCats(String cat, int limit, boolean forMobileUser) throws
                                                                                                      SQLException {
    if (cat.equals("")) {
      return null;
    }
    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();

    ArrayList<Magazine_tblTable> ret = new ArrayList<Magazine_tblTable>();
    StringBuilder selSql = new StringBuilder(
        "SELECT * FROM `magazine_tbl` WHERE " +
        (forMobileUser ? Statistics.getIssueAppearWhereLimit() + " and pubstatus=0 and " : "") + " category= ? ");
    //        " IN ( ");
    //        for (String cat:cats){
    //            selSql.append(" '"+cat+"' ,");
    //        }
    //        selSql.replace(selSql.lastIndexOf(","),selSql.length(),"");

    selSql.append(" ORDER BY magazine_id DESC LIMIT ?");


    preparedStatement = hasConnection.getValue().prepareStatement(selSql.toString());
    preparedStatement.setString(1, cat);
    preparedStatement.setInt(2, limit);
    resultSet1 = preparedStatement.executeQuery();

    ArrayList<Magazine_tblTable> rett = featchMags(resultSet1);
    closeConnection(hasConnection);
    return rett;
  }

  public ArrayList<Magazine_tblTable> getMagazineInCats(ArrayList<String> cats, int limit, boolean forMobileUser) throws
                                                                                                                  SQLException {
    ArrayList<Magazine_tblTable> ret = new ArrayList<Magazine_tblTable>();

    for (String cat : cats) {
      ret.addAll(HiberDBFacad.getInstance().getMagazineInCats(cat, (limit), forMobileUser));
    }
    return ret;
  }

  public ArrayList<Magazine_tblTable> getSuggestedMagazines(int limit, boolean forMobileUser) throws SQLException {
    return getMagazineInCats(getSugCats(), limit, forMobileUser);
  }

  public ArrayList<Magazine_tblTable> getIssues(int mag_id, int num, boolean forMobileUser) throws SQLException {
    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();

    String selSql = "SELECT * FROM magazine_tbl where ";
    if (forMobileUser) {
      selSql += Statistics.getIssueAppearWhereLimit() + " and ";
    }
    selSql += " magazine_id= ?";
    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    preparedStatement.setInt(1, mag_id);
    resultSet1 = preparedStatement.executeQuery();
    int user_id = -1;
    String title = "";
    while (resultSet1.next()) {

      user_id = resultSet1.getInt("user_id");
      title = resultSet1.getString("title");
    }
    if (user_id == -1 || title.equals("")) {
      return null;
    }
    selSql = "SELECT * FROM magazine_tbl where  " +
             (forMobileUser ? Statistics.getIssueAppearWhereLimit() + " and pubstatus=0 and " : "") +
             " user_id= ? and title= ? order by magazine_id desc limit ?";
    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    preparedStatement.setInt(1, user_id);
    preparedStatement.setString(2, title);
    preparedStatement.setInt(3, num);
    resultSet1 = preparedStatement.executeQuery();
    ArrayList<Magazine_tblTable> ret = featchMags(resultSet1);
    closeConnection(hasConnection);
    return ret;

  }

  public boolean updateMagazinConvertedto(int mag_id, boolean oked) throws SQLException {

    String sqlUpdate = "UPDATE `magazine_tbl` SET `constatus`= ? WHERE magazine_id= ?";

    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();

    preparedStatement = hasConnection.getValue().prepareStatement(sqlUpdate);
    preparedStatement.setBoolean(1, oked);
    preparedStatement.setInt(2, mag_id);
    int changed = preparedStatement.executeUpdate();
    hasConnection.getValue().commit();
    closeConnection(hasConnection);
    return changed > 0;


  }

  private ArrayList<Magazine_tblTable> featchMags(ResultSet resultSet1) throws SQLException {
    ArrayList<Magazine_tblTable> ret = new ArrayList<Magazine_tblTable>();
    while (resultSet1.next()) {

      Magazine_tblTable mgtbl = new Magazine_tblTable();
      mgtbl.setMagazine_id(resultSet1.getInt("magazine_id"));
      mgtbl.setUser_id(resultSet1.getInt("user_id"));

      mgtbl.setDir_path(destination + resultSet1.getString("dir_path"));


      mgtbl.setTitle(resultSet1.getString("title"));
      mgtbl.setDescription(resultSet1.getString("description"));
      mgtbl.setCategory(resultSet1.getString("category"));
      mgtbl.setAppertype((resultSet1.getString("appertype")));
      mgtbl.setIssue_num(resultSet1.getInt("issue_num"));
      mgtbl.setLanguage(resultSet1.getString("lang"));
      mgtbl.setStatus(resultSet1.getInt("constatus"));
      mgtbl.setDlcount(resultSet1.getInt("dlcount"));
      mgtbl.setFavorcount(resultSet1.getInt("favorcount"));
      mgtbl.setPrice(resultSet1.getDouble("price"));
      try {
        mgtbl.setOrig_issue_date(Statistics.nonFullDateFormat.parse(resultSet1.getString("orig_issue_date")));
      } catch (ParseException e) {
        log.error("Exception in: ", e);
      }
      try {
        mgtbl.setIssue_appear(Statistics.nonFullDateFormat.parse(resultSet1.getString("issue_appear")));
      } catch (ParseException e) {
        log.error("Exception in: ", e);
      }

      mgtbl.setIssue_year(resultSet1.getString("issue_year"));

      try {
        mgtbl.setDate_time(Statistics.FullDateFormat.parse(resultSet1.getString("date_time")));
      } catch (ParseException e) {
        log.error("Exception in: ", e);
      }
      ret.add(mgtbl);
    }
    return ret;
  }


  /**
   * You set one( Other parameter should set to -1) or two parameter
   *
   * @param user_id
   * @param magazine_id
   * @return
   * @throws SQLException
   */
  public ArrayList<Magazine_tblTable> getMagazineByUserId(int user_id, int magazine_id, boolean forMobileUser) throws
                                                                                                               SQLException {


    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();

    ArrayList<Magazine_tblTable> ret = new ArrayList<Magazine_tblTable>();
    String selSql = "SELECT * FROM magazine_tbl where ";
    if (forMobileUser) {
      selSql += Statistics.getIssueAppearWhereLimit() + " and pubstatus=0  and ";
    }
    if (magazine_id >= 0 && user_id >= 0) {
      selSql += "user_id=" + user_id + " and magazine_id=" + magazine_id;
    } else if (magazine_id >= 0) {
      selSql += " magazine_id=" + magazine_id;
    } else if (user_id >= 0) {
      selSql += " user_id=" + user_id;
    } else {
      return ret;
    }

    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    resultSet1 = preparedStatement.executeQuery();

    while (resultSet1.next()) {

      Magazine_tblTable mgtbl = new Magazine_tblTable();
      mgtbl.setMagazine_id(resultSet1.getInt("magazine_id"));
      mgtbl.setUser_id(resultSet1.getInt("user_id"));
      mgtbl.setDir_path(resultSet1.getString("dir_path"));
      mgtbl.setTitle(resultSet1.getString("title"));
      mgtbl.setDescription(resultSet1.getString("description"));
      mgtbl.setCategory(resultSet1.getString("category"));
      mgtbl.setAppertype((resultSet1.getString("appertype")));
      mgtbl.setIssue_num(resultSet1.getInt("issue_num"));
      mgtbl.setLanguage(resultSet1.getString("lang"));
      mgtbl.setStatus(resultSet1.getInt("constatus"));
      mgtbl.setDlcount(resultSet1.getInt("dlcount"));
      mgtbl.setFavorcount(resultSet1.getInt("favorcount"));
      mgtbl.setPrice(resultSet1.getDouble("price"));
      try {
        mgtbl.setOrig_issue_date(Statistics.nonFullDateFormat.parse(resultSet1.getString("orig_issue_date")));
      } catch (ParseException e) {
        log.error("Exception in: ", e);
      }
      try {
        mgtbl.setIssue_appear(Statistics.nonFullDateFormat.parse(resultSet1.getString("issue_appear")));
      } catch (ParseException e) {
        log.error("Exception in: ", e);
      }

      mgtbl.setIssue_year(resultSet1.getString("issue_year"));

      try {
        mgtbl.setDate_time(Statistics.FullDateFormat.parse(resultSet1.getString("date_time")));
      } catch (ParseException e) {
        log.error("Exception in: ", e);
      }
      ret.add(mgtbl);
    }
    closeConnection(hasConnection);
    return ret;
  }


  public ArrayList<MagazinReportForPub> getLastUploadedMagazineByUserId(int user_id) throws SQLException {

    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();

    ArrayList<MagazinReportForPub> ret = new ArrayList<MagazinReportForPub>();
    String selSql =
        "SELECT  * , count(*) as issues FROM (select *   From magazine_tbl  where user_id= ? order by orig_issue_date desc) as mags group by  mags.title ";


    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    preparedStatement.setInt(1, user_id);
    resultSet1 = preparedStatement.executeQuery();

    while (resultSet1.next()) {

      Magazine_tblTable mgtbl = new Magazine_tblTable();
      MagazinReportForPub reportMag = new MagazinReportForPub();
      mgtbl.setMagazine_id(resultSet1.getInt("magazine_id"));
      mgtbl.setUser_id(resultSet1.getInt("user_id"));
      mgtbl.setDir_path(destination + resultSet1.getString("dir_path"));
      mgtbl.setTitle(resultSet1.getString("title"));
      mgtbl.setDescription(resultSet1.getString("description"));
      mgtbl.setCategory(resultSet1.getString("category"));
      mgtbl.setAppertype((resultSet1.getString("appertype")));
      mgtbl.setIssue_num(resultSet1.getInt("issue_num"));
      mgtbl.setLanguage(resultSet1.getString("lang"));
      mgtbl.setStatus(resultSet1.getInt("constatus"));
      mgtbl.setDlcount(resultSet1.getInt("dlcount"));
      mgtbl.setFavorcount(resultSet1.getInt("favorcount"));
      mgtbl.setPrice(resultSet1.getDouble("price"));
      try {
        mgtbl.setOrig_issue_date(Statistics.nonFullDateFormat.parse(resultSet1.getString("orig_issue_date").trim()));
      } catch (ParseException e) {
        //                log.error(resultSet1.getString("orig_issue_date"));
        log.error("Exception in: ", e);
      }
      try {
        Date dt = Statistics.nonFullDateFormat.parse(resultSet1.getString("issue_appear").trim());
        //                dt.setHours(3);
        //                dt.setHours(3);

        mgtbl.setIssue_appear(dt);
      } catch (ParseException e) {
        //                log.error(resultSet1.getString("issue_appear"));
        log.error("Exception in: ", e);
      }

      mgtbl.setIssue_year(resultSet1.getString("issue_year"));

      try {
        mgtbl.setDate_time(Statistics.FullDateFormat.parse(resultSet1.getString("date_time").trim()));
      } catch (ParseException e) {
        //                log.error(resultSet1.getString("date_time"));
        log.error("Exception in: ", e);
      }
      reportMag.setMagazine_tblTable(mgtbl);
      reportMag.setIssues(resultSet1.getInt("issues"));
      ret.add(reportMag);
    }
    for (MagazinReportForPub magreport : ret) {
      int mag_id = magreport.getMagazine_tblTable().getMagazine_id();
      String magTitle = magreport.getMagazine_tblTable().getTitle();
      int publisher_id = magreport.getMagazine_tblTable().getUser_id();
      int downloadsOfLastIssue = HiberDBFacad.getInstance().getTotalDownload(mag_id);
      int downloadsOfAllIssues = HiberDBFacad.getInstance().getTotalDownloadOfTitle(publisher_id, magTitle);
      magreport.setDownloads_current_issue(downloadsOfLastIssue);
      magreport.setDownload_all_issues(downloadsOfAllIssues);

    }

    return ret;

  }


  public int addMagazine(Magazine_tblTable magazine) throws SQLException {
    String addSql = "INSERT INTO  magazine_tbl (`magazine_id`, `user_id`, `dir_path`, `title`, `description`," +
                    " `category`, `issue_num`, `issue_year`, `appertype`, `orig_issue_date`," +
                    " `issue_appear`, `date_time`, `price` ) " +
                    "VALUES ( DEFAULT , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ;";
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(addSql, PreparedStatement.RETURN_GENERATED_KEYS);
    //        log.debug("pre: "+preparedStatement.toString());
    //        log.debug("1: "+magazine.getUser_id());

    preparedStatement.setInt(1, magazine.getUser_id());
    int intIndex = magazine.getDir_path().indexOf("PDFs");
    magazine.setDir_path(magazine.getDir_path().substring(intIndex - 1).replaceAll("\\\\", "/"));
    log.debug("Dire Path Change to: " + magazine.getDir_path());
    preparedStatement.setString(2, magazine.getDir_path() + magazine.getFileName());
    preparedStatement.setString(3, magazine.getTitle());
    preparedStatement.setString(4, magazine.getDescription());

    preparedStatement.setString(5, magazine.getCategory());
    preparedStatement.setInt(6, magazine.getIssue_num());
    preparedStatement.setString(7, magazine.getIssue_year());
    preparedStatement.setString(8, magazine.getAppertype()!=null? magazine.getAppertype().getName():null);
    preparedStatement.setString(
        9, magazine.getOrig_issue_date() == null ? Statistics.nonFullDateFormat.format(new Date()) :
            Statistics.nonFullDateFormat.format(magazine.getOrig_issue_date()));

    String isuuAppTime = (magazine.getIssue_appear() == null ? Statistics.nonFullDateFormat.format(new Date()) :
                              Statistics.nonFullDateFormat.format(magazine.getIssue_appear()));
    preparedStatement.setString(10, isuuAppTime);
    preparedStatement.setString(11, Statistics.FullDateFormat.format(magazine.getDate_time()));
    preparedStatement.setDouble(12, magazine.getPrice());


    int rowChanged = preparedStatement.executeUpdate();
    ResultSet idinserted = preparedStatement.getGeneratedKeys();
    int magid = 0;
    if (idinserted.next()) {
      magid = idinserted.getInt(1);
    }
    hasConnection.getValue().commit();
    try {
      //            addCategory(magazine.getCategory());
      //            addTitles(magazine.getTitle(),magazine.getUser_id());
      HiberDBFacad.getInstance().addTitles(magazine.getTitle(), magazine.getUser_id());
    } catch (Exception e) {
      log.error("Exception in: ", e);
    }
    closeConnection(hasConnection);
    return magid;

  }


  //    Magazine_titleTable
  public HashSet<String> getAllTitles(int pub_id) {
    HashSet<String> ret = new HashSet<String>();

    try {
      Collections.addAll(ret, getTitles(pub_id));
    } catch (SQLException e) {
      log.error("Exception in: ", e);
    }
    return ret;
  }

  private String[] getTitles(int pub_id) throws SQLException {
    String ssql = "SELECT title FROM magazine_title where pub_id=" + pub_id;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(ssql);
    resultSet = preparedStatement.executeQuery();

    ArrayList ret = new ArrayList();
    while (resultSet.next()) {
      ret.add(resultSet.getString("title"));
    }

    closeConnection(hasConnection);

    return (String[]) ret.toArray(new String[ret.size()]);
  }

  public int addTitles(String title, int pub_id) throws Exception {
    if (HiberDBFacad.getInstance().getAllTitles(pub_id).contains(title)) {
      return -1;
    }

    String ssql = "Insert into magazine_title (title_id, pub_id, title) VALUES  (default , ? , ? ) ";
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(ssql);
    preparedStatement.setInt(1, pub_id);
    preparedStatement.setString(2, title);
    int res = preparedStatement.executeUpdate();
    hasConnection.getValue().commit();
    closeConnection(hasConnection);
    return res;
  }


  //    public int addCategory(String category) throws Exception {
  //        if(!addCatString(category)){
  ////            throw new Exception("Duplicate");
  //            return -1;
  //        }
  //        String addSsql="INSERT INTO categories_tbl (cat_id, category) VALUES (default, '"+category+"');";
  //
  //        ResultSet resultSet1;
  //        AbstractMap.SimpleEntry<Long, Connection> hasConnection= connect();
  //        preparedStatement= hasConnection.getValue().prepareStatement(addSsql);
  //        int changed= preparedStatement.executeUpdate();
  //
  //        int ret=getCategoryID(category);
  //
  //        if(changed<=0)
  //            if(ret==-1)
  //                throw new Exception("Can't add Category!!!");
  //
  //         hasConnection.getValue().commit();
  ////        closeConnection( hasConnection );
  //        return ret;
  //
  //    }
  public int getCategoryID(String category) throws SQLException {
    String selSql = "select cat_id from categories_tbl where category='" + category + "'";

    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();

    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    resultSet1 = preparedStatement.executeQuery();


    if (!resultSet1.next()) {
      return -1;
    }
    return resultSet1.getInt("cat_id");

  }


  //


  //    Publishe User


  //users
  public UsersTable addUser(String usernameOrEmail, String pass, boolean isPublisher) throws SQLException {

    String insertPub =
        "INSERT INTO  users (`user_id`, `username`, `password`, `isPublisher`) VALUES ( default , ? , ? , ? );";

    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(insertPub);
    preparedStatement.setString(1, usernameOrEmail);
    preparedStatement.setString(2, pass);
    preparedStatement.setBoolean(3, isPublisher);

    int changed = preparedStatement.executeUpdate();
    hasConnection.getValue().commit();
    closeConnection(hasConnection);
    if (changed > 0) {
      return HiberDBFacad.getInstance().getUsersByUsername(usernameOrEmail);
    }

    return null;

  }

  public int addPublisherUser(String pubName, String email, String pass) throws SQLException {


    UsersTable usersTable1 = addUser(email, pass, true);

    String insertPub5 =
        "INSERT INTO  users_roles (`user_id`, `username`, `rolename`)  VALUES (  ? , ? , ? );";
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(insertPub5);
    preparedStatement.setInt(1, usersTable1.getUser_id());
    preparedStatement.setString(2, usersTable1.getUsername());
    preparedStatement.setString(3, "publisherrole");

    int changed5 = preparedStatement.executeUpdate();

    String insertSQL = "INSERT INTO pubbillt (`id`) VALUES ( ?  );";

    preparedStatement = hasConnection.getValue().prepareStatement(insertSQL);
    preparedStatement.setInt(1, usersTable1.getUser_id());

    int changed2 = preparedStatement.executeUpdate();


    insertSQL = "INSERT INTO  pubcompany ( `id` , `compname`) VALUES ( ? , ? );";

    preparedStatement = hasConnection.getValue().prepareStatement(insertSQL);
    preparedStatement.setInt(1, usersTable1.getUser_id());
    preparedStatement.setString(2, pubName);

    int changed3 = preparedStatement.executeUpdate();


    insertSQL = "INSERT INTO  pubusers (`id`,  `email`) VALUES ( ? , ? );";

    preparedStatement = hasConnection.getValue().prepareStatement(insertSQL);
    preparedStatement.setInt(1, usersTable1.getUser_id());
    preparedStatement.setString(2, email);

    int changed4 = preparedStatement.executeUpdate();

    hasConnection.getValue().commit();
    return changed2 + changed3 + changed4;


  }

  public UsersTable getUsersByUsername(String username) throws SQLException {
    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();


    String selSql = "SELECT * FROM users WHERE username='" + username + "'";
    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    resultSet1 = preparedStatement.executeQuery();
    UsersTable usersTable1 = new UsersTable();
    usersTable1.setUser_id(-1);
    if (resultSet1.next()) {
      usersTable1.setUser_id(resultSet1.getInt("user_id"));
      usersTable1.setUsername(resultSet1.getString("username"));
      usersTable1.setPublisher(resultSet1.getBoolean("isPublisher"));
    }
    closeConnection(hasConnection);
    return usersTable1;

  }

  public UsersTable getUsersByUserID(int user_id) throws SQLException {
    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();


    String selSql = "SELECT * FROM `users` WHERE user_id='" + user_id + "'";
    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    resultSet1 = preparedStatement.executeQuery();
    UsersTable usersTable1 = new UsersTable();
    if (resultSet1.next()) {
      usersTable1.setUser_id(resultSet1.getInt("user_id"));
      usersTable1.setUsername(resultSet1.getString("username"));
      usersTable1.setPublisher(resultSet1.getBoolean("isPublisher"));
    }
    return usersTable1;

  }


  public UsersTable getUser(String username, String password) throws SQLException {
    String selSql = "select * from users where username= ? and password= ? ";
    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    preparedStatement.setString(1, username);
    preparedStatement.setString(2, password);
    resultSet1 = preparedStatement.executeQuery();
    String currentPass = "";
    if (resultSet1.next()) {
      UsersTable usersTable1 = new UsersTable();
      usersTable1.setUser_id(resultSet1.getInt("user_id"));
      usersTable1.setPublisher(resultSet1.getBoolean("isPublisher"));
      usersTable1.setUsername(resultSet1.getString("username"));
      return usersTable1;
    }
    return null;
  }

  public String updateUserPass(String username, String oldPass, String newpass) throws SQLException {
    String selSql = "select password from users where username= ?";
    ResultSet resultSet1;
    String ret = "Can Not Change Password";
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    preparedStatement.setString(1, username);
    resultSet1 = preparedStatement.executeQuery();
    String currentPass = "";
    if (resultSet1.next()) {
      currentPass = resultSet1.getString("password");

    }

    if (!currentPass.equals(oldPass)) {
      return "Error Current Password Do Not Match";
    }


    String updateSQL = "UPDATE users SET password= ?  where username= ? ";


    preparedStatement = hasConnection.getValue().prepareStatement(updateSQL);
    preparedStatement.setString(1, newpass);
    preparedStatement.setString(2, username);
    int changed = preparedStatement.executeUpdate();
    if (changed > 0) {
      hasConnection.getValue().commit();
      ret = "Password Changed.";
    } else {
      hasConnection.getValue().rollback();
    }

    closeConnection(hasConnection);
    return ret;
  }

  public int upadtePubCompanyDetailes(PubCompantDet pubCompantDet) throws SQLException {

    String updateSQL = "UPDATE pubcompany SET " +
                       "compname= ? ,corpform= ? ,streetname= ? ,streetnum= ? ,cityname= ? ,citypostcode= ? ,country= ? ,taxnum= ? ,corpregnum= ? WHERE id= ?;";

    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();

    preparedStatement = hasConnection.getValue().prepareStatement(updateSQL);
    preparedStatement.setString(1, pubCompantDet.getCompname());
    preparedStatement.setString(2, pubCompantDet.getCorpform());
    preparedStatement.setString(3, pubCompantDet.getStreetname());
    preparedStatement.setString(4, pubCompantDet.getStreetnum());
    preparedStatement.setString(5, pubCompantDet.getCityname());
    preparedStatement.setString(6, "" + pubCompantDet.getCitypostcode());
    preparedStatement.setString(7, pubCompantDet.getCountry());
    preparedStatement.setString(8, pubCompantDet.getTaxnum());
    preparedStatement.setString(9, pubCompantDet.getCorpregnum());
    preparedStatement.setInt(10, pubCompantDet.getId());

    int changed = preparedStatement.executeUpdate();
    hasConnection.getValue().commit();


    closeConnection(hasConnection);
    return changed;

  }

  public int upadtePubBillDet(PubBillDet pubBillDet) throws SQLException {

    String updateSQL =
        "UPDATE `pubbillt` SET `bankaccnum`= ? ,`bankcode`= ? ,`bankname`= ? ,`recipname`= ?  WHERE  id= ?";

    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();

    preparedStatement = hasConnection.getValue().prepareStatement(updateSQL);
    preparedStatement.setString(1, pubBillDet.getBankaccnum());
    preparedStatement.setString(2, pubBillDet.getBankcode());
    preparedStatement.setString(3, pubBillDet.getBankname());
    preparedStatement.setString(4, pubBillDet.getRecipname());
    preparedStatement.setInt(5, pubBillDet.getId());
    int changed = preparedStatement.executeUpdate();
    hasConnection.getValue().commit();

    closeConnection(hasConnection);
    return changed;

  }

  public int upadtePubUserDet(PubUserDet pubUserDet) throws SQLException {

    String updateSQL = "UPDATE `pubusers` SET " +
                       " `firstname`= ? ,`lastname`= ? ,`email`= ? ,`phonecode`= ? ,`phonenum`= ? " +
                       ",`streetname`= ? ,`streetnum`= ? ,`cityname`= ? ,`citynum`= ? ,`country`= ? " +
                       " WHERE id= ? ";

    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();

    preparedStatement = hasConnection.getValue().prepareStatement(updateSQL);
    preparedStatement.setString(1, pubUserDet.getFirstname());
    preparedStatement.setString(2, pubUserDet.getLastname());
    preparedStatement.setString(3, pubUserDet.getEmail());
    preparedStatement.setString(4, pubUserDet.getPhonecode());
    preparedStatement.setString(5, pubUserDet.getPhonenum());
    preparedStatement.setString(6, pubUserDet.getStreetname());
    preparedStatement.setString(7, pubUserDet.getStreetnum());
    preparedStatement.setString(8, pubUserDet.getCityname());
    preparedStatement.setString(9, pubUserDet.getCitynum());
    preparedStatement.setString(10, pubUserDet.getCountry());
    preparedStatement.setInt(11, pubUserDet.getId());
    int changed = preparedStatement.executeUpdate();
    hasConnection.getValue().commit();
    closeConnection(hasConnection);
    return changed;

  }

  public PubCompantDet getPubCompanyDetailes(int id) throws SQLException {


    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();


    String selSql = "SELECT * FROM `pubcompany` WHERE id=" + id;
    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    resultSet1 = preparedStatement.executeQuery();
    PubCompantDet pubCompantDet = new PubCompantDet();

    if (resultSet1.next()) {
      pubCompantDet.setId(resultSet1.getInt("id"));
      pubCompantDet.setCompname(resultSet1.getString("compname"));
      pubCompantDet.setCorpform(resultSet1.getString("corpform"));
      pubCompantDet.setStreetname(resultSet1.getString("streetname"));
      pubCompantDet.setStreetnum(resultSet1.getString("streetnum"));
      pubCompantDet.setCityname(resultSet1.getString("cityname"));
      pubCompantDet.setCitypostcode(resultSet1.getString("citypostcode"));
      pubCompantDet.setCountry(resultSet1.getString("country"));
      pubCompantDet.setTaxnum(resultSet1.getString("taxnum"));
      pubCompantDet.setCorpregnum(resultSet1.getString("corpregnum"));


    }

    return pubCompantDet;

  }

  public PubBillDet getPubBillDet(int id) throws SQLException {

    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();


    String selSql = "SELECT * FROM `pubbillt` WHERE id=" + id;
    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    resultSet1 = preparedStatement.executeQuery();
    PubBillDet pubBillDet = new PubBillDet();
    if (resultSet1.next()) {
      pubBillDet.setId(resultSet1.getInt("id"));
      pubBillDet.setBankaccnum(resultSet1.getString("bankaccnum"));
      pubBillDet.setBankcode(resultSet1.getString("bankcode"));
      pubBillDet.setBankname(resultSet1.getString("bankname"));
      pubBillDet.setRecipname(resultSet1.getString("recipname"));
    }


    return pubBillDet;

  }

  public PubUserDet getPubUserDet(int id) throws SQLException {
    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();


    String selSql = "SELECT * FROM `pubusers` WHERE id=" + id;
    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    resultSet1 = preparedStatement.executeQuery();
    PubUserDet pubUserDet = new PubUserDet();
    if (resultSet1.next()) {
      pubUserDet.setId(resultSet1.getInt("id"));
      pubUserDet.setFirstname(resultSet1.getString("firstname"));
      pubUserDet.setLastname(resultSet1.getString("lastname"));
      pubUserDet.setEmail(resultSet1.getString("email"));
      pubUserDet.setPhonecode(resultSet1.getString("phonecode"));
      pubUserDet.setPhonenum(resultSet1.getString("phonenum"));
      pubUserDet.setStreetname(resultSet1.getString("streetname"));
      pubUserDet.setStreetnum(resultSet1.getString("streetnum"));
      pubUserDet.setCityname(resultSet1.getString("cityname"));
      pubUserDet.setCitynum(resultSet1.getString("citynum"));
      pubUserDet.setCountry(resultSet1.getString("country"));
    }
    return pubUserDet;
  }


  public ArrayList<Magazine_tblTable> getLastMagazines(int numOfMag, boolean mobileUser) throws SQLException {
    ResultSet resultSet1;
    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();


    String selSql = "SELECT * FROM `magazine_tbl` " +
                    (mobileUser ? ("WHERE " + Statistics.getIssueAppearWhereLimit() + " and pubstatus=0  ") : "") +
                    " order by magazine_id desc limit " + numOfMag;
    preparedStatement = hasConnection.getValue().prepareStatement(selSql);
    resultSet1 = preparedStatement.executeQuery();
    ArrayList<Magazine_tblTable> ret = new ArrayList<Magazine_tblTable>();
    while (resultSet1.next()) {

      Magazine_tblTable mgtbl = new Magazine_tblTable();
      mgtbl.setMagazine_id(resultSet1.getInt("magazine_id"));
      mgtbl.setUser_id(resultSet1.getInt("user_id"));
      mgtbl.setDir_path(resultSet1.getString("dir_path"));
      mgtbl.setTitle(resultSet1.getString("title"));
      mgtbl.setDescription(resultSet1.getString("description"));
      mgtbl.setCategory(resultSet1.getString("category"));
      mgtbl.setAppertype((resultSet1.getString("appertype")));
      mgtbl.setIssue_num(resultSet1.getInt("issue_num"));
      mgtbl.setLanguage(resultSet1.getString("lang"));
      mgtbl.setStatus(resultSet1.getInt("constatus"));
      mgtbl.setDlcount(resultSet1.getInt("dlcount"));
      mgtbl.setFavorcount(resultSet1.getInt("favorcount"));
      mgtbl.setPrice(resultSet1.getDouble("price"));

      try {
        mgtbl.setOrig_issue_date(Statistics.nonFullDateFormat.parse(resultSet1.getString("orig_issue_date")));
      } catch (ParseException e) {
        log.error("Exception in: ", e);
      }
      try {
        mgtbl.setIssue_appear(Statistics.nonFullDateFormat.parse(resultSet1.getString("issue_appear")));
      } catch (ParseException e) {
        log.error("Exception in: ", e);
      }

      mgtbl.setIssue_year(resultSet1.getString("issue_year"));

      try {
        mgtbl.setDate_time(Statistics.FullDateFormat.parse(resultSet1.getString("date_time")));
      } catch (ParseException e) {
        log.error("Exception in: ", e);
      }
      ret.add(mgtbl);
    }
    closeConnection(hasConnection);
    return ret;
  }

  public int addMobileUser(MobileUser_tbl mobileUser_tbl) throws SQLException {

    //        UsersTable i=getUsersByUsername(mobileUser_tbl.getUsersTable().getUsername());
    UsersTable i = HiberDBFacad.getInstance().getUsersByUsername(mobileUser_tbl.getUsersTable().getUsername());
    if (i == null || i.getUser_id() > 0) {
      return -1;
    }

    UsersTable usersTable1 = addUser(
        mobileUser_tbl.getUsersTable().getUsername(),
        mobileUser_tbl.getUsersTable().getPassword(),
        mobileUser_tbl.getUsersTable().isPublisher());


    String insertSQl =
        "INSERT INTO `mobileuser_tbl` (`user_id`, `bdate`, `postcode`,`fname`, `lname`, `job`, `gener`) VALUES ( ? , ? , ? ,? ,? ,? ,? );";

    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(insertSQl);
    preparedStatement.setInt(1, usersTable1.getUser_id());
    preparedStatement.setString(2, mobileUser_tbl.getBdate());
    preparedStatement.setString(3, mobileUser_tbl.getPostcode());
    preparedStatement.setString(4, mobileUser_tbl.getFname());
    preparedStatement.setString(5, mobileUser_tbl.getLname());
    preparedStatement.setString(6, mobileUser_tbl.getJob());
    preparedStatement.setString(7, mobileUser_tbl.getGener());

    int changed = preparedStatement.executeUpdate();
    if (changed > 0) {
      hasConnection.getValue().commit();

    }


    return usersTable1.getUser_id();
  }


  //    USER ACTION Querries


  public boolean addDownlaodAll(int mag_id) throws SQLException {

    String updateSQL = "UPDATE `magazine_tbl` SET `dlcount`=`dlcount`+1   WHERE magazine_id= ? ";

    AbstractMap.SimpleEntry<Long, Connection> hasConnection = connect();
    preparedStatement = hasConnection.getValue().prepareStatement(updateSQL);
    preparedStatement.setInt(1, mag_id);


    int changed = preparedStatement.executeUpdate();
    if (changed > 0) {
      hasConnection.getValue().commit();

      return true;

    }
    closeConnection(hasConnection);
    return false;
  }
}

