package ir.mjm.DBAO;

import ir.mjm.DBAO.hiber.BannersTblHiberEntity;
import ir.mjm.DBAO.hiber.CategoriesTblHiberEntity;
import ir.mjm.DBAO.hiber.CustomerownedissuesHiberEntity;
import ir.mjm.DBAO.hiber.GcmRegistrationIdHiberEntity;
import ir.mjm.DBAO.hiber.IssuepagebookmarkHiberEntity;
import ir.mjm.DBAO.hiber.IssuepagerequestHiberEntity;
import ir.mjm.DBAO.hiber.IssuepagesessionHiberEntity;
import ir.mjm.DBAO.hiber.MagazineTblHiberEntity;
import ir.mjm.DBAO.hiber.MagazineTitleHiberEntity;
import ir.mjm.DBAO.hiber.MobileuserTblHiberEntity;
import ir.mjm.DBAO.hiber.MobileuserfavorHiberEntity;
import ir.mjm.DBAO.hiber.PagesHiberEntity;
import ir.mjm.DBAO.hiber.PubbilltHiberEntity;
import ir.mjm.DBAO.hiber.PubcompanyHiberEntity;
import ir.mjm.DBAO.hiber.PubusersHiberEntity;
import ir.mjm.DBAO.hiber.UsersHiberEntity;
import ir.mjm.DBAO.hiber.UsersRolesHiberEntity;
import ir.mjm.admin.AdminMobileUser;
import ir.mjm.admin.AdminPublisherInf;
import ir.mjm.restBeans.Category;
import ir.mjm.restBeans.MobileUser;
import ir.mjm.restBeans.Suggestion;
import ir.mjm.restBeans.UserBookmarked;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Serap on 7/10/14.
 */
public class HiberDBFacad {
  static final Logger log = Logger.getLogger(HiberDBFacad.class);

  private static HiberDBFacad ourInstance = new HiberDBFacad();
  private ArrayList<String> sugCatsByHibernate;
  private ArrayList<CategoriesTblHiberEntity> allCategories;


  private HiberDBFacad() {
  }

  public static HiberDBFacad getInstance() {
    return ourInstance;
  }

  public boolean
  addUserSession(
      int user_id, int mag_id, String sessionStart, int sessionDuration,
      int page, int elapsed, int clickToEnlargeCount) {

    IssuepagesessionHiberEntity issuepagesessionHiberEntity = new IssuepagesessionHiberEntity();
    issuepagesessionHiberEntity.setUserId(user_id);
    issuepagesessionHiberEntity.setMagId(mag_id);
    issuepagesessionHiberEntity.setSessionstarttime(sessionStart);
    issuepagesessionHiberEntity.setSessionduration(sessionDuration);
    issuepagesessionHiberEntity.setPage(page);
    issuepagesessionHiberEntity.setTimeelapsed(elapsed);
    issuepagesessionHiberEntity.setClicktoenlargcount(clickToEnlargeCount);

    return saveToDBHiber(issuepagesessionHiberEntity);

  }

  public boolean isMagIdCorrect(int mag_id) {
    boolean ret = false;
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();


    try {
      Query query = session.createQuery("select  magazineId from MagazineTblHiberEntity where magazineId=" + mag_id);

      MagazineTblHiberEntity res = null;

      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        ret = true;
      }

    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }

    return ret;
  }


  public String addUserBookmark(int user_id, int mag_id, int page, final String desc, boolean bookmarked) {
    String ret = "";
    if (!isMagIdCorrect(mag_id)) {
      return "NOK: Incorrect Magazine id";
    }
    IssuepagebookmarkHiberEntity
        issuepagebookmarkHiberEntity = new IssuepagebookmarkHiberEntity();
    issuepagebookmarkHiberEntity.setUserId(user_id);
    issuepagebookmarkHiberEntity.setMagId(mag_id);
    issuepagebookmarkHiberEntity.setPage(page);
    issuepagebookmarkHiberEntity.setDescription(desc);


    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra;
    if ((tra = session.getTransaction()) == null) {
      tra = session.beginTransaction();
    }
    tra.begin();
    try {
      Query query = session.createQuery(
          "from IssuepagebookmarkHiberEntity where  magId=" + mag_id + " and page=" + page + " and userId=" + user_id);
      List res = query.list();
      if (bookmarked) {
        if (res != null && res.size() > 0 && res.get(0) != null) {
          ret = "NOK: Used Bookmarked This page.";

        } else {
          saveOrUpdateToDBHiber(issuepagebookmarkHiberEntity);
          HiberDBFacad.getInstance().updateUserBookmarkedPageInMagazineTbl(mag_id, bookmarked);
          ret = "OK";

        }
      } else {
        if (res == null || res.size() <= 0 || res.get(0) == null) {
          ret = "NOK: First Bookmark Then  remove bookmark for This page.";
        } else {
          deleteTiDBHiber(issuepagebookmarkHiberEntity);
          HiberDBFacad.getInstance().updateUserBookmarkedPageInMagazineTbl(mag_id, bookmarked);
          ret = "OK";
        }
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
      ret = "NOK: Server Error";
    } finally {
      session.close();
    }

    return ret;
  }

  public List<IssuepagebookmarkHiberEntity> mobileUserAllBookmarked(int userId) {
    {
      Session session = HiberService.getInstance().getSessionFactory().openSession();
      Query query = session.createQuery("from IssuepagebookmarkHiberEntity where userId=" + userId);
      List bdtaes = null;
      try {
        bdtaes = query.list();
      } catch (Exception e) {
        log.error("Exception in: ", e);
      } finally {
        session.close();

      }
      if (bdtaes != null && bdtaes.size() > 0 && bdtaes.get(0) != null) {
        return bdtaes;
      } else {
        return null;
      }

    }
  }


  private boolean updateUserBookmarkedPageInMagazineTbl(int mag_id, boolean bookmarked) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery("from MagazineTblHiberEntity where magazineId=" + mag_id);

    MagazineTblHiberEntity res = null;
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {

        res = (MagazineTblHiberEntity) resList.get(0);
        res.setSumOfBookmark(res.getSumOfBookmark() + (bookmarked ? 1 : -1));
        session.saveOrUpdate(res);
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
      return false;
    } finally {
      session.close();

    }
    return true;
  }

  public List<Integer> mobileUserAllFavored(int userId) {

    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Query query = session.createQuery("from MobileuserfavorHiberEntity where userId=" + userId);
    List bdtaes = null;
    List<Integer> ret = new ArrayList<>();
    try {
      bdtaes = query.list();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    if (bdtaes != null && bdtaes.size() > 0 && bdtaes.get(0) != null) {
      for (MobileuserfavorHiberEntity mobileuserfavorHiberEntity : (List<MobileuserfavorHiberEntity>) bdtaes) {
        ret.add(mobileuserfavorHiberEntity.getMagId());
      }
    }
    return ret;
  }

  public String updateUserFavorite(int user_id, int mag_id, boolean favorite) {
    String ret = "";
    MobileuserfavorHiberEntity mobileuserfavorHiberEntity = new MobileuserfavorHiberEntity();
    mobileuserfavorHiberEntity.setMagId(mag_id);
    mobileuserfavorHiberEntity.setUserId(user_id);
    mobileuserfavorHiberEntity.setFavorite(favorite);
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();


    try {
      Query query = session.createQuery("from MagazineTblHiberEntity where magazineId=" + mag_id);

      MagazineTblHiberEntity res = null;

      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {

        res = (MagazineTblHiberEntity) resList.get(0);
        res.setFavorcount(res.getFavorcount() + (favorite ? 1 : -1));
        session.saveOrUpdate(res);
        Query query1 =
            session.createQuery("from MobileuserfavorHiberEntity where magId=" + mag_id + " and userId=" + user_id);

        MobileuserfavorHiberEntity res1 = null;

        List resList1 = query1.list();
        if (resList1 != null && resList1.size() > 0 && resList1.get(0) != null) {
          ((MobileuserfavorHiberEntity) resList1.get(0)).setFavorite(favorite);
          session.saveOrUpdate((MobileuserfavorHiberEntity) resList1.get(0));
        } else {
          session.saveOrUpdate(mobileuserfavorHiberEntity);
        }

        ret = "OK";
        tra.commit();
      } else {
        ret = "NOK: Check magazine id.";
        tra.rollback();
      }


    } catch (Exception e) {

      log.error("Exception in: ", e);
      if (!tra.wasRolledBack()) {
        tra.rollback();
      }

      ret = "NOK:server Error";
    } finally {
      session.close();
    }
    return ret;
  }

  public boolean addFavorCountToMagazineTbl(int mag_id, int count) {
    boolean ret = false;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery("from MagazineTblHiberEntity where magazineId=" + mag_id);

    MagazineTblHiberEntity res = null;
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {

        res = (MagazineTblHiberEntity) resList.get(0);
        res.setFavorcount(res.getFavorcount() + count);
        session.saveOrUpdate(res);
        ret = true;
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
      return false;
    } finally {
      session.close();

    }
    return ret;
  }


  //    public String addUserOwnedIssue(int user_id, int mag_id, String buyDate, double payed) {
  //        Session session =  HiberService.getInstance().getSessionFactory().openSession();
  //        Transaction tra= session.beginTransaction();
  //        Query query=session.createQuery("select userId,title from MagazineTblHiberEntity as magent where magent.magazineId=" + mag_id+")");
  //
  //        Object[] pubId=null;
  //        try{
  //            pubId= ( Object[]) query.list().get(0);
  //        }catch (Exception e){
  //
  //        }
  //        if(pubId==null)
  //            return "NOK: mag_id is not currect.";
  //
  //        query=session.createQuery("from MagazineTitleHiberEntity where pubId=" + pubId[0]+" and title='"+pubId[1]+"'");
  //
  //        MagazineTitleHiberEntity magazinetitle=null;
  //        try{
  //            magazinetitle= (MagazineTitleHiberEntity) query.list().get(0);
  //        }catch (Exception e){
  //            log.error("Exception in: ",e);
  //        }
  //        if(magazinetitle==null||magazinetitle.getTitleId()<=0)
  //            return "NOK: DB Error";
  //
  //        CustomerownedissuesHiberEntity customerownedissuesHiberEntity=new CustomerownedissuesHiberEntity();
  //        customerownedissuesHiberEntity.setMagId(mag_id);
  //        customerownedissuesHiberEntity.setUserId(user_id);
  //        customerownedissuesHiberEntity.setBuyDate(buyDate);
  //
  //        customerownedissuesHiberEntity.setPayment(payed);
  //        customerownedissuesHiberEntity.setPubId(magazinetitle.getPubId());
  //        customerownedissuesHiberEntity.setTitleId(magazinetitle.getTitleId());
  //
  //        try{
  //            session.save(customerownedissuesHiberEntity);
  //        }catch (Exception e){
  //            log.error("Exception in: ",e);
  //            tra.rollback();
  //            return "NOK: System Error.";
  //        }
  //
  //
  //        session.getTransaction().commit();
  //        return "OK";
  //
  //    }

  //TODO Find title id and page type
  public String addIssuepagerequest(IssuepagerequestHiberEntity issuepagerequest) {
    String ret = "";
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    Query query = session.createQuery(
        "select userId,title from MagazineTblHiberEntity as magent where magent.magazineId=" +
        issuepagerequest.getMagId());

    Object[] pubId = null;
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        pubId = (Object[]) resList.get(0);
      } else {
        ret = "NOK: Magazine Id not found.";
        log.error("DB Error for add user dl action: can not find magazine for mag_id:" + issuepagerequest.getMagId());

      }

      if (pubId != null) {
        query = session.createQuery(
            "from MagazineTitleHiberEntity where pubId=" + pubId[0] + " and title='" + pubId[1] + "'");


        MagazineTitleHiberEntity magazinetitle = null;
        try {
          List resList1 = query.list();
          if (resList1 != null && resList1.size() > 0 && resList1.get(0) != null) {
            magazinetitle = (MagazineTitleHiberEntity) resList1.get(0);
          } else if (magazinetitle == null || magazinetitle.getTitleId() <= 0) {
            log.error(
                "DB Error for add user dl action: can not find title id for mag_id:" + issuepagerequest.getMagId());
            ret = "NOK: Server Error.";
          }
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }

        if (magazinetitle != null) {
          issuepagerequest.setTitleId(magazinetitle.getTitleId());


          query = session.createQuery(
              "from PagesHiberEntity where magId=" + issuepagerequest.getMagId() +
              " and pageNumber=" + issuepagerequest.getPage());

          PagesHiberEntity pageType = null;
          try {
            List tmp = query.list();
            if (tmp != null && tmp.size() > 0 && tmp.get(0) != null) {
              pageType = (PagesHiberEntity) tmp.get(0);
            }
          } catch (Exception e) {
            log.error("Exception in: ", e);
          }
          if (pageType != null) {
            issuepagerequest.setPageType(pageType.getPageType());
          }

          try {
            session.saveOrUpdate(issuepagerequest);
            tra.commit();
            ret = "OK";
          } catch (Exception e) {
            log.error("DB Error for add user dl action: can not save mag_id:" + issuepagerequest.getMagId());
            ret = "NOK: Server Error";
            tra.rollback();
          }
        }
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }

    return ret;
  }

  private synchronized boolean saveToDBHiber(Object object) {
    boolean ret = false;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      session.save(object);
      tra.commit();
      ret = true;
    } catch (Exception e) {
      ret = false;
      log.error("Exception in Save: " + e.getMessage());
      tra.rollback();
    } finally {
      session.close();
    }
    return ret;
  }

  private synchronized boolean saveOrUpdateToDBHiber(Object object) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {

      session.saveOrUpdate(object);
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
      return false;
    } finally {
      session.close();
    }

    return true;
  }

  private synchronized boolean saveOrUpdateToDBHiberMobileUserFavor(MobileuserfavorHiberEntity object) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    ;

    try {
      Query query = session.createQuery(
          "from MobileuserfavorHiberEntity where  magId=" + object.getMagId() + " and userId=" + object.getUserId());
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        ((MobileuserfavorHiberEntity) resList.get(0)).setFavorite(object.getFavorite());
        session.saveOrUpdate((resList.get(0)));
      } else {
        session.saveOrUpdate(object);
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
      return false;
    } finally {
      session.close();
    }


    return true;
  }

  private boolean deleteTiDBHiber(Object obj) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      session.delete(obj);
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
      return false;
    } finally {
      session.close();
    }
    return true;


  }

  public MobileuserTblHiberEntity getMobileUser(int user_id) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery("from MobileuserTblHiberEntity where userId=" + user_id);
    query.setMaxResults(1);
    List bdtaes = null;
    try {
      bdtaes = query.list();
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    if (bdtaes != null && bdtaes.size() > 0 && bdtaes.get(0) != null) {
      return (MobileuserTblHiberEntity) bdtaes.get(0);
    } else {
      return null;
    }

  }

  public ArrayList<MagazineTblHiberEntity> getMagazinesOfPubInTitle(int pu_id, String title) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query =
        session.createQuery("from MagazineTblHiberEntity where userId=" + pu_id + " and title='" + title + "'");

    List res = null;
    try {
      res = query.list();
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }

    return (ArrayList<MagazineTblHiberEntity>) res;

  }

  public double getAverageReaderAgeFor(int mag_id) {
    double result = 0;
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "select cus.mobileuserTblByUserId.bdate from CustomerownedissuesHiberEntity as cus where cus.magId=" + mag_id);

    List bdtaes = null;
    try {
      bdtaes = query.list();
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }

    if (bdtaes != null) {
      if (bdtaes.size() > 0) {
        Date today = new Date();
        int counter = 0;
        double sum = 0;
        for (Object bdate : bdtaes) {
          try {
            Date birthdate = Statistics.nonFullDateFormat.parse((String) bdate);
            int age = today.getYear() - birthdate.getYear();
            sum += age;
            counter++;
          } catch (ParseException e) {
            log.error("Exception in: ", e);
          }
        }
        result = sum / counter;
      }
    }

    return result;
  }

  public int getTotalDownload(int mag_id) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery("select count(*) from CustomerownedissuesHiberEntity where magId=" + mag_id);

    int res = 0;
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        res = Integer.valueOf(resList.get(0).toString());
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }

    return res;
  }

  public double getRevenuesEarned(int mag_id) {

    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "select sum(cus.payment) from CustomerownedissuesHiberEntity as cus where cus.magId=" + mag_id);

    double res = 0;
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        res = Double.valueOf(resList.get(0).toString());
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }

    return (res * (7.0 / 10.0));
  }


  public double getAverageLengthPerReadingSession(int mag_id) {

    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "select avg (cus.sessionduration) from IssuepagesessionHiberEntity as cus where cus.magId=" + mag_id);

    double res = 0;
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        res = Double.valueOf(resList.get(0).toString());
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }

    return res;
  }

  public int getTotalReadingSessions(int mag_id) {
    int ret = 0;

    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Criteria criteria = session.createCriteria(IssuepagesessionHiberEntity.class);

    ProjectionList projList = Projections.projectionList();
    projList.add((Projections.property("sessionduration")));
    projList.add((Projections.property("sessionstarttime")));

    projList.add((Projections.property("userId")));

    criteria.setProjection(Projections.distinct(projList));
    criteria.add(Restrictions.eq("magId", mag_id));
    List res = null;
    try {
      res = criteria.list();

      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret = res.size();
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }

    return ret;
  }

  public int getTotalAdPageViews(int mag_id) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    //        Criteria criteria = session.createCriteria( IssuepagesessionHiberEntity.class );
    //        criteria.setProjection(Projections.distinct(Projections.property("sessionstarttime")));
    //        criteria.setProjection(Projections.distinct(Projections.property("userId")));
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "select count(*) from IssuepagesessionHiberEntity  where magId=" + mag_id +
        " and (magId,page) in   (select magId,pageNumber from PagesHiberEntity where pageType=" + Statistics.AD +
        " and magId=" + mag_id + ")");

    int res = 0;
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        res = Integer.valueOf(resList.get(0).toString());
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }

    return res;
  }

  public String getHotSpotFor(int mag_id) {
    String result = "---";
    Session session = HiberService.getInstance().getSessionFactory().openSession();

    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "select cus.mobileuserTblByUserId.postcode as postco from " +
        "CustomerownedissuesHiberEntity as cus where cus.magId=" + mag_id);

    List postcodes = null;
    try {
      postcodes = query.list();

      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    if (postcodes != null && postcodes.size() > 0) {
      HashMap<String, Integer> countedPostCodes = new HashMap<>();
      int maxValue = 0;

      for (Object postCodeObj : postcodes) {
        String postCode = postCodeObj.toString();
        Integer count = countedPostCodes.get(postCode);
        if (count != null) {
          count = count + 1;
        } else {
          count = 1;
        }
        countedPostCodes.put(postCode, count);
        if (maxValue < count) {
          maxValue = count;
          result = postCode;
        }
      }
    }

    return result;
  }

  public int getTotalPageViews(int mag_id) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery("select count(*) from IssuepagesessionHiberEntity where magId=" + mag_id);

    int res = 0;
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        res = Integer.valueOf(resList.get(0).toString());
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }

    return res;
  }

  public double getAveragePageViews(int mag_id) {
    {
      Session session = HiberService.getInstance().getSessionFactory().openSession();
      Criteria criteria = session.createCriteria(IssuepagesessionHiberEntity.class);
      criteria.setProjection(Projections.distinct(Projections.property("userId")));
      Transaction tra = session.beginTransaction();
      Query query = session.createQuery(
          "select count(*) from IssuepagesessionHiberEntity where magId=" + mag_id + " group by userId");

      int res = 0;
      try {
        List resList = query.list();
        if (resList != null && resList.size() > 0 && resList.get(0) != null) {
          res = resList.size();
        }
        //                    res= Integer.valueOf(resList.get(0).toString());
        tra.commit();
      } catch (Exception e) {
        log.error("Exception in: ", e);
      } finally {
        session.close();

      }
      int dd = getTotalPageViews(mag_id);
      if (dd > 0) {
        return (dd / (1.0 * res));
      } else {
        return 0.0;
      }
    }

  }

  public MagazineTblHiberEntity getMagazineById(int mag_id) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery("from MagazineTblHiberEntity where magazineId=" + mag_id);

    List res = null;
    try {
      res = query.list();
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return res != null ? (MagazineTblHiberEntity) res.get(0) : null;
  }

  public int[] getNewAndOldReadersInMonthForPublisher(int pubId) {


    int[] ret = new int[]{0, 0};
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -30);
    Date oneWeekAgo = cal.getTime();
    String onweekagoString = Statistics.FullDateFormat.format(oneWeekAgo);
    Query query = session.createQuery(
        "select  userId  from  CustomerownedissuesHiberEntity  where buyDate<='" + onweekagoString +
        "' and magId in (select magazineId from MagazineTblHiberEntity where userId=" + pubId + ")");

    List res = null;
    try {
      res = query.list();
      ret[1] = res.size();
    } catch (Exception e) {

    }
    Query query2 = session.createQuery(
        "select  userId from  CustomerownedissuesHiberEntity where buyDate>='" + onweekagoString +
        "' and magId in (select magazineId from MagazineTblHiberEntity where userId=" + pubId +
        ") and userId not in (select  userId  from  CustomerownedissuesHiberEntity  where buyDate<='" +
        onweekagoString + "' and magId in (select magazineId from MagazineTblHiberEntity where userId=" + pubId + "))");

    try {

      ret[0] = query2.list().size();
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;


  }

  public int[] getNewAndOldReadersInMonthForMAgazine(int magId, int daysAgo) {


    int[] ret = new int[]{0, 0};
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, (-1 * daysAgo));
    Date oneWeekAgo = cal.getTime();
    String onweekagoString = Statistics.FullDateFormat.format(oneWeekAgo);
    Query query = session.createQuery(
        "select  distinct userId  from  CustomerownedissuesHiberEntity  where buyDate>='" + onweekagoString +
        "' and magId=" + magId +
        " and userId not in (select distinct userId from CustomerownedissuesHiberEntity where buyDate<'" +
        onweekagoString + "' and magId=" + magId + ")");

    List res = null;
    try {
      res = query.list();
      ret[0] = res.size();
    } catch (Exception e) {

    }
    Query query2 = session.createQuery(
        "select distinct userId from CustomerownedissuesHiberEntity where buyDate<'" + onweekagoString +
        "' and magId=" + magId);

    try {

      ret[1] = query2.list().size();
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;


  }


  public double getThisDaysAgoRevenue(int pubId, int daysAgo) {


    double ret = 0.0;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, (-1 * daysAgo));
    Date oneWeekAgo = cal.getTime();
    String onMonthAgoString = Statistics.FullDateFormat.format(oneWeekAgo);
    Query query = session.createQuery(
        "select  sum(payment)  from  CustomerownedissuesHiberEntity  where buyDate>='" + onMonthAgoString +
        "' and magId in (select magazineId from MagazineTblHiberEntity where userId=" + pubId + ")");


    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        ret = Double.valueOf(resList.get(0).toString());
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return (ret * (7.0 / 10));


  }

  public int getTotalDownloadOfTitle(int publisher_id, String magTitle) {
    int result = 0;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "select count(*) from CustomerownedissuesHiberEntity where pubId=" + publisher_id +
        " and magazineTblByMagId.title='" + magTitle + "'");
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        result = Integer.valueOf(resList.get(0).toString());
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return result;
  }

  public int getTotalDownloadsOfPublisherInRange(int publisher_id, String startDate, String endDate) {
    int result = 0;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "select count(*) from CustomerownedissuesHiberEntity where pubId=" + publisher_id +
        " and buyDate >='" + startDate + "' and buyDate<'" + endDate + "'");
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        result = Integer.valueOf(resList.get(0).toString());
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return result;
  }

  public int addMobileUserToUserTbl(
      UsersHiberEntity usersHiberEntity,
      MobileuserTblHiberEntity mobileuserTblHiberEntity) {

    int id = -1;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      Query query =
          session.createQuery("from UsersHiberEntity where  username='" + usersHiberEntity.getUsername() + "'");
      List res = query.list();
      if (res != null && res.size() > 0) {
        return -2;
      }

      session.save(usersHiberEntity);
      //            tra.commit();

      query = session.createQuery("from UsersHiberEntity where  username='" + usersHiberEntity.getUsername() + "'");
      res = query.list();
      if (res != null && res.size() > 0) {
        id = ((UsersHiberEntity) res.get(0)).getUserId();
      }
      if (id <= 0) {
        tra.rollback();
        return -1;
      }

      mobileuserTblHiberEntity.setUserId(id);
      session.save(mobileuserTblHiberEntity);
      tra.commit();


    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
      return -1;
    } finally {
      session.close();

    }
    return id;

  }


  //TODO I should Check this Chert Function and add comment for checking logic
  public void addOwnedIssueForMobileUser(int magId, int userId) {

    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "select userId from CustomerownedissuesHiberEntity where userId=" + userId +
        " and magId=" + magId);
    try {
      List resList = query.list();
      //Is User's First visit for this magazine id
      if (resList == null || resList.size() <= 0) {
        String nowDate = Statistics.FullDateFormat.format(new Date());
        query = session.createQuery("from MobileuserTblHiberEntity where userId=" + userId);
        List userimiz = query.list();
        //Find Current mobile user
        if (userimiz != null && userimiz.size() > 0 && userimiz.get(0) != null) {

          MobileuserTblHiberEntity user = (MobileuserTblHiberEntity) userimiz.get(0);
          double userinChargi = user.getCharge();
          double userBaxmishAllPriceSumDouble = 0.0;
          //Find magazine that user seen
          query = session.createQuery("from MagazineTblHiberEntity where magazineId=" + magId);
          MagazineTblHiberEntity magazineSeen = null;

          List listOfSeenMagazines = query.list();
          double tazaninPricee = 0.0;
          if (listOfSeenMagazines != null && listOfSeenMagazines.size() > 0 && listOfSeenMagazines.get(0) != null) {
            magazineSeen = (MagazineTblHiberEntity) listOfSeenMagazines.get(0);
            tazaninPricee = magazineSeen.getPrice();
          }
          //if user has charge
          if (userinChargi > 0.0) {

            query = session.createQuery(
                "select sum(price) from MagazineTblHiberEntity where magazineId in " +
                "(select magId from CustomerownedissuesHiberEntity where userId=" + userId + " and  buyDate<='" +
                user.getChargeennd() + "' and  buyDate>='" + user.getChargestart() + "')");
            List userBaxmishAllPriceSum = query.list();
            if (userBaxmishAllPriceSum != null && userBaxmishAllPriceSum.size() > 0 &&
                userBaxmishAllPriceSum.get(0) != null) {
              userBaxmishAllPriceSumDouble = (double) userBaxmishAllPriceSum.get(0);
            }


            if (tazaninPricee > 0.0) {
              userBaxmishAllPriceSumDouble += tazaninPricee;

              query = session.createQuery(
                  "from CustomerownedissuesHiberEntity where userId=" + userId + " and  buyDate<='" +
                  user.getChargeennd() + "' and  buyDate>='" + user.getChargestart() + "')");
              List userBaxmishMaglar = query.list();
              if (userBaxmishMaglar != null && userBaxmishMaglar.size() > 0 && userBaxmishMaglar.get(0) != null) {
                for (Object buChargedaKi : userBaxmishMaglar) {
                  if (buChargedaKi != null) {
                    CustomerownedissuesHiberEntity buChargeDaa = (CustomerownedissuesHiberEntity) buChargedaKi;
                    query = session.createQuery(
                        "select price from MagazineTblHiberEntity where magazineId=" + buChargeDaa.getMagId());
                    List baxmishinPrice = query.list();
                    if (baxmishinPrice != null && baxmishinPrice.size() > 0 && baxmishinPrice.get(0) != null) {
                      buChargeDaa.setPayment(
                          (((double) baxmishinPrice.get(0)) / userBaxmishAllPriceSumDouble) * userinChargi);
                      session.saveOrUpdate(buChargeDaa);
                    }

                  }

                }


              }


            }
          }

          CustomerownedissuesHiberEntity tazaCustomerownedissuesHiberEntity = new CustomerownedissuesHiberEntity();
          tazaCustomerownedissuesHiberEntity.setUserId(userId);
          tazaCustomerownedissuesHiberEntity.setMagId(magId);
          double upper = userBaxmishAllPriceSumDouble > 0.0 ? (tazaninPricee / userBaxmishAllPriceSumDouble) : 0.0;
          tazaCustomerownedissuesHiberEntity.setPayment((upper) * userinChargi);
          tazaCustomerownedissuesHiberEntity.setFavorite(false);
          tazaCustomerownedissuesHiberEntity.setChargestart(user.getChargestart());
          tazaCustomerownedissuesHiberEntity.setChargend(user.getChargeennd());
          query = session.createQuery(
              "from MagazineTitleHiberEntity where pubId=" + magazineSeen.getUserId() + " and title='" +
              magazineSeen.getTitle() + "'");

          MagazineTitleHiberEntity magazinetitle = null;
          try {
            magazinetitle = (MagazineTitleHiberEntity) query.list().get(0);
            tazaCustomerownedissuesHiberEntity.setTitleId(magazinetitle.getTitleId());
            tazaCustomerownedissuesHiberEntity.setBuyDate(Statistics.FullDateFormat.format(new Date()));
            tazaCustomerownedissuesHiberEntity.setPubId(magazineSeen.getUserId());

            session.saveOrUpdate(tazaCustomerownedissuesHiberEntity);
          } catch (Exception e) {
            log.error("Exception in: ", e);
          }

        }
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
    } finally {
      session.close();

    }

  }


  public double[] getTotalRevenuesEarnedBeforAfter(int mag_id, int startOfDaysAgo, int endOfDaysAgo) {


    double[] ret = new double[]{0, 0};
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, (-1 * startOfDaysAgo));
    Date aTimeAgo = cal.getTime();
    String startTimeAgoString = Statistics.FullDateFormat.format(aTimeAgo);
    cal = Calendar.getInstance();
    cal.add(Calendar.DATE, (-1 * endOfDaysAgo));
    aTimeAgo = cal.getTime();
    String endOfTimeAgoString = Statistics.FullDateFormat.format(aTimeAgo);
    Query query = session.createQuery(
        "select sum(payment) from CustomerownedissuesHiberEntity where magId=" + mag_id +
        " and userId in (select  userId  from  CustomerownedissuesHiberEntity  where magId=" + mag_id +
        " and buyDate<'" + startTimeAgoString + "'     )  ");

    List res = null;
    try {
      res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret[0] = (double) res.get(0);
      }
    } catch (Exception e) {

    }
    Query query2 = session.createQuery(
        "select sum(payment) from CustomerownedissuesHiberEntity where magId=" + mag_id + " and  buyDate<='" +
        endOfTimeAgoString + "' and userId not in (select  userId  from  CustomerownedissuesHiberEntity  where magId=" +
        mag_id + " and buyDate<'" + startTimeAgoString + "'     )  ");

    try {

      res = query2.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret[1] = (double) res.get(0);
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    ret[0] = ret[0] * (7.0 / 10.0);
    ret[1] = ret[1] * (7.0 / 10.0);
    return ret;


  }

  public ArrayList<AdminPublisherInf> fetchPubsInfsForAdmin() {
    ArrayList<AdminPublisherInf> ret = new ArrayList<>();
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    Query query = session.createQuery(
        "from PubcompanyHiberEntity as comp , PubusersHiberEntity as pubuser , PubbilltHiberEntity as pubbill where pubuser.id=comp.id and pubuser.id=pubbill.id");

    List res = null;
    try {
      res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        for (Object obj : res) {
          Object[] objs = (Object[]) obj;
          AdminPublisherInf adminPublisherInf = new AdminPublisherInf();
          adminPublisherInf.setPubcompanyHiberEntity((PubcompanyHiberEntity) objs[0]);
          adminPublisherInf.setPubusersHiberEntity((PubusersHiberEntity) objs[1]);
          adminPublisherInf.setPubbilltHiberEntity((PubbilltHiberEntity) objs[2]);
          adminPublisherInf.setMagazineTblHiberEntity(
              getLastMagazineOfPub(
                  adminPublisherInf.getPubusersHiberEntity()
                                   .getId()));
          adminPublisherInf.setFilesUploaded(
              getCountOfMagazinesForPub(
                  adminPublisherInf.getPubusersHiberEntity()
                                   .getId()));

          ret.add(adminPublisherInf);


        }
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    log.error("Featched " + ret.size() + " Publisher detailes in admin panel.");
    return ret;
  }

  private int getCountOfMagazinesForPub(int id) {
    int ret = 0;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    Query query = session.createQuery("select count (*) from MagazineTblHiberEntity where userId=" + id);
    query.setMaxResults(1);

    List res = null;
    try {
      res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret = ((Long) res.get(0)).intValue();
      }


      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;

  }

  private MagazineTblHiberEntity getLastMagazineOfPub(int id) {
    MagazineTblHiberEntity ret = null;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    Query query = session.createQuery("from MagazineTblHiberEntity where userId=" + id + " order by magazineId desc");
    query.setMaxResults(1);

    List res = null;
    try {
      res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret = (MagazineTblHiberEntity) res.get(0);
      }


      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;
  }

  public UsersHiberEntity loginMobileUser(String username, String pass) {
    UsersHiberEntity ret = null;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    Query query =
        session.createQuery("from UsersHiberEntity where username='" + username + "' and password='" + pass + "'");
    query.setMaxResults(1);

    List res = null;
    try {
      res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret = (UsersHiberEntity) res.get(0);
      }


      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;
  }

  public ArrayList<MagazineTblHiberEntity> getLastMagazinesHiber(int numOfMag, boolean requestFromMobileUser) {
    ArrayList<MagazineTblHiberEntity> ret = new ArrayList<>();
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    Query query = session.createQuery(
        "from MagazineTblHiberEntity " +
        (requestFromMobileUser ?
             "where issueAppear <='" + Statistics.nonFullDateFormat.format(new Date()) + "' and pubstatus=0 " : " ") +
        " order by magazineId desc ");

    query.setMaxResults(numOfMag);

    List res = null;
    try {
      res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret = (ArrayList<MagazineTblHiberEntity>) res;
      }


      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;

  }

  public ArrayList<Magazine_tblTable> getLastMagazines(int numOfMag, boolean mobileUser) {
    ArrayList<MagazineTblHiberEntity> lastmagz = getLastMagazinesHiber(numOfMag, mobileUser);
    ArrayList<Magazine_tblTable> ret = new ArrayList<>();
    for (MagazineTblHiberEntity mag : lastmagz) {
      ret.add(ReportingFacad.convertMagazineTblHiberEntityToMagazine_tblTable(mag));
    }
    return ret;
  }

  public long[] getTotalDownloadsNewAndOld(int mag_id, int startOfDaysAgo, int endOfDaysAgo) {


    long[] ret = new long[]{0, 0};
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, (-1 * startOfDaysAgo));
    Date aTimeAgo = cal.getTime();
    String startTimeAgoString = Statistics.FullDateFormat.format(aTimeAgo);
    cal = Calendar.getInstance();
    cal.add(Calendar.DATE, (-1 * endOfDaysAgo));
    aTimeAgo = cal.getTime();
    String endOfTimeAgoString = Statistics.FullDateFormat.format(aTimeAgo);
    Query query = session.createQuery(
        "select count(userId) from CustomerownedissuesHiberEntity where magId=" + mag_id +
        " and userId in (select  userId  from  CustomerownedissuesHiberEntity  where magId=" + mag_id +
        " and buyDate<'" + startTimeAgoString + "'     )  ");

    List res = null;
    try {
      res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret[0] = (long) res.get(0);
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
    }
    Query query2 = session.createQuery(
        "select  count(userId) from CustomerownedissuesHiberEntity where magId=" + mag_id + " and  buyDate<='" +
        endOfTimeAgoString + "' and userId not in (select  userId  from  CustomerownedissuesHiberEntity  where magId=" +
        mag_id + " and buyDate<'" + startTimeAgoString + "'     )  ");

    try {

      res = query2.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret[1] = (long) res.get(0);
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }

    return ret;
  }

  public HashMap getEachPageViewPercent(int magid, int totalCount) {
    HashMap result = null;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createSQLQuery(
        "select page,count(page) from issuepagesession where mag_id=" + magid + " group by page");
    List qres = null;
    try {
      qres = query.list();

    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {

      session.close();
    }

    if (totalCount > 0 && qres != null) {
      if (qres.size() > 0) {

        result = new HashMap<>();
        for (Object pobj : qres) {
          Object[] pair = (Object[]) pobj;
          Float tmp = Float.valueOf(pair[1].toString());
          result.put(pair[0], tmp >= totalCount ? 1 : (tmp / totalCount));
        }
      }
    }

    return result;
  }

  public MobileUser getMobileUserInfoPojo(int user_id) {
    MobileUser ret = new MobileUser();
    Session session = HiberService.getInstance().getSessionFactory().openSession();

    try {
      Query query = session.createQuery("from UsersHiberEntity where  userId=" + user_id);
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret.setUserName(((UsersHiberEntity) res.get(0)).getUsername());
        ret.setFavoredMagazineIds(mobileUserAllFavored(user_id));
        try {
          final UserBookmarked userBookmarkedPojo = new UserBookmarked(mobileUserAllBookmarked(user_id));
          ret.setUserBookmarkedPojo(userBookmarkedPojo);
        } catch (Exception e) {
          ret.setUserBookmarkedPojo(new UserBookmarked(user_id));
        }
      } else {
        return null;
      }


      //            tra.commit();

      query = session.createQuery("from MobileuserTblHiberEntity where  userId=" + user_id);
      res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        MobileuserTblHiberEntity mobuser = (MobileuserTblHiberEntity) res.get(0);
        ret.setBirthDate(mobuser.getBdate());
        ret.setCharge(mobuser.getCharge());
        ret.setChargeEnd(mobuser.getChargeennd());
        ret.setChargeStart(mobuser.getChargestart());
        ret.setFirstName(mobuser.getFname());
        ret.setLastName(mobuser.getLname());
        ret.setGener(mobuser.getGener());
        ret.setJob(mobuser.getJob());
        ret.setPostCode(mobuser.getPostcode());
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
      return null;
    } finally {
      session.close();
    }
    return ret;
  }

  public String updateMobileUserToUserTbl(MobileuserTblHiberEntity mobileuserTblHiberEntity) {
    String ret = "";
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      Query query =
          session.createQuery("from MobileuserTblHiberEntity where  userId=" + mobileuserTblHiberEntity.getUserId());
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {

        MobileuserTblHiberEntity mobileuserTblHiberEntity1 = (MobileuserTblHiberEntity) res.get(0);

        mobileuserTblHiberEntity1.setBdate(mobileuserTblHiberEntity.getBdate());
        mobileuserTblHiberEntity1.setGener(mobileuserTblHiberEntity.getGener());
        mobileuserTblHiberEntity1.setJob(mobileuserTblHiberEntity.getJob());
        mobileuserTblHiberEntity1.setFname(mobileuserTblHiberEntity.getFname());
        mobileuserTblHiberEntity1.setLname(mobileuserTblHiberEntity.getLname());
        mobileuserTblHiberEntity1.setPostcode(mobileuserTblHiberEntity.getPostcode());
        session.saveOrUpdate(mobileuserTblHiberEntity1);
        ret = "OK: Update Successful.";
      }


      tra.commit();


    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
      return null;
    } finally {
      if (!tra.wasCommitted() && !tra.wasRolledBack()) {
        tra.commit();
      }
      session.close();

    }
    return ret;
  }

  public String updateMobileUserPass(UsersHiberEntity usersHiberEntity, String oldpass) {

    String ret = "";
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      Query query = session.createQuery("from UsersHiberEntity where  userId=" + usersHiberEntity.getUserId());
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        UsersHiberEntity usersHiberEntity1 = (UsersHiberEntity) res.get(0);
        if (!usersHiberEntity1.getPassword().trim().equals(oldpass)) {
          ret = "NOK: oldpass is not correct.";
        } else {
          usersHiberEntity1.setPassword(usersHiberEntity.getPassword());
          session.update(usersHiberEntity1);
          ret = "OK: password Updated.";
        }
      }


      tra.commit();


    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
      ret = "NOK: Server Error.";
    } finally {
      if (!tra.wasCommitted() && !tra.wasRolledBack()) {
        tra.commit();
      }
      session.close();

    }
    return ret;


  }

  public int getSumOfClickToEnlarg(int selectedCurrentMag_id, int pageNum) {

    int ret = 0;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      Query query = session.createQuery(
          "select sum(clicktoenlargcount) from IssuepagesessionHiberEntity where  magId=" + selectedCurrentMag_id +
          " and page=" + pageNum);
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret = (int) ((long) res.get(0));

      }


      tra.commit();


    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
    } finally {
      if (!tra.wasCommitted() && !tra.wasRolledBack()) {
        tra.commit();
      }
      session.close();

    }
    return ret;
  }

  public double getAvePageViewForPage(int selectedCurrentMag_id, int pageNum) {

    double ret = 0.0;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      Query query = session.createQuery(
          "select avg (sessionduration) from IssuepagesessionHiberEntity where  magId=" + selectedCurrentMag_id +
          " and page=" + pageNum);
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret = ((double) res.get(0));

      }


      tra.commit();


    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
    } finally {
      if (!tra.wasCommitted() && !tra.wasRolledBack()) {
        tra.commit();
      }
      session.close();

    }
    return ret;
  }

  public String getPageType(int selectedCurrentMag_id, int pageNum) {
    String ret = "Article";
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Query query = session.createQuery(
        "from PagesHiberEntity where magId=" + selectedCurrentMag_id + " and pageNumber=" + pageNum);

    query.setMaxResults(1);
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        int typee = ((PagesHiberEntity) resList.get(0)).getPageType();
        ret = Statistics.getPageType(typee);
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;
  }

  public PagesHiberEntity getPageTypeEntity(int selectedCurrentMag_id, int pageNum) {
    PagesHiberEntity ret = null;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Query query = session.createQuery(
        "from PagesHiberEntity where magId=" + selectedCurrentMag_id + " and pageNumber=" + pageNum);

    query.setMaxResults(1);
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        ret = ((PagesHiberEntity) resList.get(0));
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;
  }

  public boolean updatePublisherInfos(AdminPublisherInf adminPublisherInf) {
    boolean ret = false;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query =
        session.createQuery("from PubusersHiberEntity where id=" + adminPublisherInf.getPubusersHiberEntity().getId());

    query.setMaxResults(1);
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        PubusersHiberEntity pubusersHiberEntity = ((PubusersHiberEntity) resList.get(0));
        pubusersHiberEntity.setCityname(adminPublisherInf.getPubusersHiberEntity().getCityname());
        pubusersHiberEntity.setCitynum(adminPublisherInf.getPubusersHiberEntity().getCitynum());
        pubusersHiberEntity.setCountry(adminPublisherInf.getPubusersHiberEntity().getCountry());
        pubusersHiberEntity.setEmail(adminPublisherInf.getPubusersHiberEntity().getEmail());
        pubusersHiberEntity.setFirstname(adminPublisherInf.getPubusersHiberEntity().getFirstname());
        pubusersHiberEntity.setLastname(adminPublisherInf.getPubusersHiberEntity().getLastname());
        pubusersHiberEntity.setPhonecode(adminPublisherInf.getPubusersHiberEntity().getPhonecode());
        pubusersHiberEntity.setPhonenum(adminPublisherInf.getPubusersHiberEntity().getPhonenum());
        pubusersHiberEntity.setStreetname(adminPublisherInf.getPubusersHiberEntity().getStreetname());
        pubusersHiberEntity.setStreetnum(adminPublisherInf.getPubusersHiberEntity().getStreetnum());

        session.saveOrUpdate(pubusersHiberEntity);
      }
      query = session.createQuery(
          "from PubcompanyHiberEntity where id=" + adminPublisherInf.getPubusersHiberEntity().getId());

      query.setMaxResults(1);
      resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        PubcompanyHiberEntity pubcompanyHiberEntity = ((PubcompanyHiberEntity) resList.get(0));
        pubcompanyHiberEntity.setCityname(adminPublisherInf.getPubcompanyHiberEntity().getCityname());
        pubcompanyHiberEntity.setStreetname(adminPublisherInf.getPubcompanyHiberEntity().getStreetname());
        pubcompanyHiberEntity.setCountry(adminPublisherInf.getPubcompanyHiberEntity().getCountry());
        pubcompanyHiberEntity.setStreetnum(adminPublisherInf.getPubcompanyHiberEntity().getStreetnum());
        pubcompanyHiberEntity.setCitypostcode(adminPublisherInf.getPubcompanyHiberEntity().getCitypostcode());
        pubcompanyHiberEntity.setCompname(adminPublisherInf.getPubcompanyHiberEntity().getCompname());
        pubcompanyHiberEntity.setCorpform(adminPublisherInf.getPubcompanyHiberEntity().getCorpform());
        pubcompanyHiberEntity.setCorpregnum(adminPublisherInf.getPubcompanyHiberEntity().getCorpregnum());
        pubcompanyHiberEntity.setTaxnum(adminPublisherInf.getPubcompanyHiberEntity().getTaxnum());

        session.saveOrUpdate(pubcompanyHiberEntity);
      }
      query = session.createQuery(
          "from PubbilltHiberEntity where id=" + adminPublisherInf.getPubusersHiberEntity().getId());

      query.setMaxResults(1);
      resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        PubbilltHiberEntity pubbilltHiberEntity = ((PubbilltHiberEntity) resList.get(0));
        pubbilltHiberEntity.setBankaccnum(adminPublisherInf.getPubbilltHiberEntity().getBankaccnum());
        pubbilltHiberEntity.setBankcode(adminPublisherInf.getPubbilltHiberEntity().getBankcode());
        pubbilltHiberEntity.setBankname(adminPublisherInf.getPubbilltHiberEntity().getBankname());
        pubbilltHiberEntity.setRecipname(adminPublisherInf.getPubbilltHiberEntity().getRecipname());


        session.saveOrUpdate(pubbilltHiberEntity);
      }

      tra.commit();
      ret = true;
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;
  }

  public ArrayList<Suggestion> searchMagazineByTitleOrKeywords(String title, boolean forMobileUsers) {

    ArrayList<Suggestion> ret = new ArrayList<>();
    ArrayList<MagazineTblHiberEntity> mags = getMagazineByTitleOrKeywordsLike(title, forMobileUsers);
    for (MagazineTblHiberEntity mag : mags) {
      Suggestion testnull = ImageFacade.getSugPojo(mag);
      if (testnull != null) {
        ret.add(testnull);
      }
    }
    return ret;
  }

  public ArrayList<MagazineTblHiberEntity> getMagazineByTitleOrKeywordsLike(String title, boolean forMobileUsers) {

    ArrayList<MagazineTblHiberEntity> ret = new ArrayList<>();
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "from MagazineTblHiberEntity where " + Statistics.getIssueAppearWhereLimit() + " and " +
        (forMobileUsers ? "pubstatus=0 and " : "") + "( title like '%" + title + "%' or magKeywords like '%" + title + "%')");


    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        ret = (ArrayList<MagazineTblHiberEntity>) resList;
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;
  }


  public ArrayList<AdminMobileUser> getMobileUsersForAdminPanel() {
    ArrayList<AdminMobileUser> ret = new ArrayList<>();
    ArrayList<MobileuserTblHiberEntity> mobUsers = new ArrayList<>();

    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery("from MobileuserTblHiberEntity ");


    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        mobUsers = (ArrayList<MobileuserTblHiberEntity>) resList;
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    for (MobileuserTblHiberEntity mobileuserTblHiberEntity : mobUsers) {
      AdminMobileUser adminMobileUser = new AdminMobileUser();
      try {
        adminMobileUser.setBithday(Statistics.nonFullDateFormat.parse(mobileuserTblHiberEntity.getBdate()));

      } catch (ParseException e) {
        log.error("Exception in: ", e);
      }

      try {
        adminMobileUser.setRegDate(Statistics.FullDateFormat.parse(mobileuserTblHiberEntity.getRegdate()));
      } catch (Exception e) {
        log.error("Exception in: ", e);
      }

      adminMobileUser.setfName(mobileuserTblHiberEntity.getFname());
      adminMobileUser.setlName(mobileuserTblHiberEntity.getLname());
      adminMobileUser.setGeneder(
          mobileuserTblHiberEntity.getGener()
                                  .equalsIgnoreCase("m") ? "Male" : (mobileuserTblHiberEntity.getGener()
                                                                                             .equalsIgnoreCase(
                                                                                                 "f") ? "Female" : "None"));
      adminMobileUser.setPostCode(mobileuserTblHiberEntity.getPostcode());
      adminMobileUser.setTotalMountPremium(mobileuserTblHiberEntity.getTotalpremiummonth());
      adminMobileUser.setIsPremiume(Statistics.isPremiumNow(mobileuserTblHiberEntity.getChargeennd()) ? "Yes" : "No");
      ret.add(adminMobileUser);

    }
    return ret;
  }

  public ArrayList<MagazineTblHiberEntity> getMagazinesForClassification() {
    ArrayList<MagazineTblHiberEntity> mags = new ArrayList<>();
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery("from MagazineTblHiberEntity order by issueAppear desc ");


    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        mags = (ArrayList<MagazineTblHiberEntity>) resList;
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return mags;
  }

  public void addOrUpdatePageType(PagesHiberEntity pagesHiberEntity) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "from PagesHiberEntity where magId=" + pagesHiberEntity.getMagId() + " and pageNumber=" +
        pagesHiberEntity.getPageNumber());

    query.setMaxResults(1);
    try {
      PagesHiberEntity page = null;
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        page = (PagesHiberEntity) resList.get(0);
      }
      if (page != null) {
        page.setPageType(pagesHiberEntity.getPageType());


      } else {
        page = new PagesHiberEntity();
        page.setMagId(pagesHiberEntity.getMagId());
        page.setPageNumber(pagesHiberEntity.getPageNumber());
        page.setPageType(pagesHiberEntity.getPageType());
      }
      session.saveOrUpdate(page);
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
  }

  public void deletePageType(PagesHiberEntity pagesHiberEntity) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "from PagesHiberEntity where magId=" + pagesHiberEntity.getMagId() + " and pageNumber=" +
        pagesHiberEntity.getPageNumber());

    query.setMaxResults(1);
    try {
      PagesHiberEntity page = null;
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        page = (PagesHiberEntity) resList.get(0);
        session.delete(page);

      }


      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
  }

  public ArrayList<String> getSugCatsByHibernate() {
    ArrayList<String> ret = new ArrayList<>();
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery("select  category from CategoriesTblHiberEntity where sug=" + true);

    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        ret = (ArrayList<String>) resList;

      }


      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;
  }

  public MagazineTblHiberEntity updateMagazine(Magazine_tblTable magazineRepSpecTitle) {
    MagazineTblHiberEntity oked = null;
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();


    try {
      Query query =
          session.createQuery("from MagazineTblHiberEntity where magazineId=" + magazineRepSpecTitle.getMagazine_id());

      MagazineTblHiberEntity res = null;

      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        res = (MagazineTblHiberEntity) resList.get(0);
        if (res == null) {
          res = new MagazineTblHiberEntity();
        }
        res.setDateTime(Statistics.FullDateFormat.format(magazineRepSpecTitle.getDate_time()));
        res.setTitle(magazineRepSpecTitle.getTitle());
        res.setCategory(magazineRepSpecTitle.getCategory());
        res.setDescription(magazineRepSpecTitle.getDescription());
        res.setIssueNum(magazineRepSpecTitle.getIssue_num());
        res.setIssueYear(magazineRepSpecTitle.getIssue_year());
        res.setAppertype(magazineRepSpecTitle.getIssue_appear_type());
        res.setPrice(magazineRepSpecTitle.getPrice());
        res.setMagType(magazineRepSpecTitle.getMagazineType());
        res.setMagKeywords(magazineRepSpecTitle.getMagazineKeywords());
        res.setOrigIssueDate(Statistics.nonFullDateFormat.format(magazineRepSpecTitle.getOrig_issue_date()));
        res.setIssueAppear(Statistics.nonFullDateFormat.format(magazineRepSpecTitle.getOrig_issue_date()));

        session.save(res);

      }
      tra.commit();

      oked = res;
    } catch (Exception e) {
      tra.rollback();
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }


    return oked;
  }

  public ArrayList<MagazinReportForPub> getLastUploadedMagazineByUserId(int userId) {

    List resultSet;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    SQLQuery sqlquery = session.createSQLQuery(
        "SELECT  * , count(*) as issues FROM (select *   From magazine_tbl  where user_id= " + userId +
        " order by orig_issue_date desc) as mags group by  mags.title ");
    ArrayList<MagazinReportForPub> ret = new ArrayList<MagazinReportForPub>();
    //        String selSql="SELECT  * , count(*) as issues FROM (select *   From magazine_tbl  where user_id= ? order by orig_issue_date desc) as mags group by  mags.title " ;


    //        preparedStatement=connection.prepareStatement(selSql);
    //        sqlquery.setInteger(1, userId);
    try {


      resultSet = sqlquery.list();
      int lastObj = resultSet.size() - 1;
      while (resultSet.size() > 0) {
        Object[] resultSet1 = (Object[]) resultSet.remove(0);
        Magazine_tblTable mgtbl = new Magazine_tblTable();
        MagazinReportForPub reportMag = new MagazinReportForPub();
        try {
          mgtbl.setMagazine_id((Integer) resultSet1[0]);
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }
        //            mgtbl.setUser_id(resultSet1.getInt("user_id"));
        try {
          mgtbl.setUser_id((Integer) resultSet1[1]);

        } catch (Exception e) {
          log.error("Exception in: ", e);
        }
        //            mgtbl.setDir_path(destination+resultSet1.getString("dir_path"));
        try {
          mgtbl.setDir_path(Statistics.getApplicationPath() + resultSet1[2]);
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }
        try {
          mgtbl.setTitle((String) resultSet1[3]);
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }
        //            mgtbl.setTitle(resultSet1.getString("title"));
        try {
          mgtbl.setDescription((String) resultSet1[4]);
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }
        try {
          mgtbl.setCategory((String) resultSet1[5]);
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }
        try {
          mgtbl.setIssue_num((Integer) resultSet1[6]);
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }
        try {
          mgtbl.setIssue_year((String) resultSet1[7]);
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }
        try {
          mgtbl.setAppertype(((String) resultSet1[8]));
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }

        try {
          mgtbl.setOrig_issue_date(Statistics.nonFullDateFormat.parse(((String) resultSet1[9]).trim()));
        } catch (ParseException e) {
          log.error("Exception in: ", e);
          log.error(resultSet1[9]);
        }
        try {
          Date dt = Statistics.nonFullDateFormat.parse(((String) resultSet1[10]).trim());
          //                dt.setHours(3);
          //                dt.setHours(3);

          mgtbl.setIssue_appear(dt);
        } catch (ParseException e) {
          log.error(resultSet1[10]);
          log.error("Exception in: ", e);
        }
        try {
          mgtbl.setDate_time(Statistics.FullDateFormat.parse(((String) resultSet1[11]).trim()));
        } catch (ParseException e) {
          log.error(resultSet1[11]);
          log.error("Exception in: ", e);
        }
        try {
          mgtbl.setLanguage((String) resultSet1[12]);
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }
        try {
          mgtbl.setStatus((Integer) resultSet1[13]);
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }
        try {
          mgtbl.setDlcount((Integer) resultSet1[14]);
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }
        try {
          mgtbl.setFavorcount((Integer) resultSet1[15]);

        } catch (Exception e) {
          log.error("Exception in: ", e);
        }
        try {
          mgtbl.setPrice((Double) resultSet1[17]);
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }


        reportMag.setMagazine_tblTable(mgtbl);
        try {
          reportMag.setIssues(Integer.valueOf("" + resultSet1[resultSet1.length - 1]));
        } catch (Exception e) {
          log.error("Exception in: ", e);
        }
        ret.add(reportMag);
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
      try {
        tra.commit();
      } catch (Exception e1) {
        log.error("Exception in: ", e1);

      }
    } finally {
      session.close();
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

  public ArrayList<MagazinReportForPub> getLastUploadedMagazineByUserId2(int userId) {

    List resultSet;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
 /*   SQLQuery sqlquery = session.createSQLQuery(
        "SELECT  * , count(*) as issues FROM (select *   From magazine_tbl  where user_id= " + userId +
        " order by orig_issue_date desc) as mags group by  mags.title ");*/

    ArrayList<MagazinReportForPub> ret = new ArrayList<MagazinReportForPub>();
    //        String selSql="SELECT  * , count(*) as issues FROM (select *   From magazine_tbl  where user_id= ? order by orig_issue_date desc) as mags group by  mags.title " ;


    //        preparedStatement=connection.prepareStatement(selSql);
    //        sqlquery.setInteger(1, userId);
    try {


      resultSet = session.createCriteria(MagazineTblHiberEntity.class)
                         .add(Restrictions.eq("userId", userId))
                         .setProjection(
                             Projections.projectionList()
                                        .add(Projections.groupProperty("title"))
                             //                                        .add(Projections.rowCount())
                                       ).list();
      int lastObj = resultSet.size() - 1;
      for (MagazineTblHiberEntity magazine_tblTable : (ArrayList<MagazineTblHiberEntity>) resultSet) {
        Magazine_tblTable mgtbl = new Magazine_tblTable();
        MagazinReportForPub reportMag = new MagazinReportForPub();

        mgtbl.setMagazine_id(magazine_tblTable.getMagazineId());

        //            mgtbl.setUser_id(resultSet1.getInt("user_id"));

        mgtbl.setUser_id(magazine_tblTable.getUserId());

        mgtbl.setDir_path(Statistics.getApplicationPath() + magazine_tblTable.getDirPath());

        mgtbl.setTitle(magazine_tblTable.getTitle());

        mgtbl.setDescription(magazine_tblTable.getDescription());

        mgtbl.setCategory(magazine_tblTable.getCategory());

        mgtbl.setIssue_num(magazine_tblTable.getIssueNum());

        mgtbl.setIssue_year(magazine_tblTable.getIssueYear());

        mgtbl.setAppertype(magazine_tblTable.getAppertype());
        try {
          mgtbl.setOrig_issue_date(Statistics.nonFullDateFormat.parse(magazine_tblTable.getOrigIssueDate().trim()));
        } catch (ParseException e) {
          log.error("Exception in: ", e);
        }
        try {
          Date dt = Statistics.nonFullDateFormat.parse(magazine_tblTable.getIssueAppear().trim());
          //                dt.setHours(3);
          //                dt.setHours(3);

          mgtbl.setIssue_appear(dt);
        } catch (ParseException e) {
          log.error(magazine_tblTable.getIssueAppear());
          log.error("Exception in: ", e);
        }
        try {
          mgtbl.setDate_time(Statistics.FullDateFormat.parse(magazine_tblTable.getDateTime().trim()));
        } catch (ParseException e) {
          log.error(magazine_tblTable.getDateTime());
          log.error("Exception in: ", e);
        }

        mgtbl.setLanguage(magazine_tblTable.getLang());

        mgtbl.setStatus(magazine_tblTable.getPubstatus());

        mgtbl.setDlcount(magazine_tblTable.getDlcount());

        mgtbl.setFavorcount(magazine_tblTable.getFavorcount());

        mgtbl.setPrice(magazine_tblTable.getPrice());


        reportMag.setMagazine_tblTable(mgtbl);

        reportMag.setIssues(resultSet.size());

        ret.add(reportMag);
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
      try {
        tra.commit();
      } catch (Exception e1) {
        log.error("Exception in: ", e1);

      }
    } finally {
      session.close();
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

  public String addPublisherUser(String pubName, String email, String pass) {
    String ret = "";
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();


    try {
      UsersHiberEntity usersHiberEntity = addOnlyUser(email, pass, true);
      if (usersHiberEntity == null) {
        ret = "Can not add Publisher. Server Error.";


      } else if (usersHiberEntity.getUserId() == -1) {
        ret = "Can not add Publisher. Email: " + email + " Reserved. Please select an other email address.";

      } else {


        PubbilltHiberEntity pubbilltHiberEntity = new PubbilltHiberEntity();
        pubbilltHiberEntity.setId(usersHiberEntity.getUserId());
        session.save(pubbilltHiberEntity);

        PubcompanyHiberEntity pubcompanyHiberEntity = new PubcompanyHiberEntity();
        pubcompanyHiberEntity.setId(usersHiberEntity.getUserId());
        session.save(pubcompanyHiberEntity);

        PubusersHiberEntity pubusersHiberEntity = new PubusersHiberEntity();
        pubusersHiberEntity.setId(usersHiberEntity.getUserId());
        pubusersHiberEntity.setFirstname(pubName);
        pubusersHiberEntity.setEmail(email);
        session.save(pubusersHiberEntity);


        session.flush();
      }

      tra.commit();
      if (ret.equals("")) {
        ret = "Added New Publisher. Password is:" + pass;
      }
    } catch (Exception e) {
      ret = "Can not add Publisher. Server Error.";

      tra.rollback();
      log.error("publisher add Failed", e);
    } finally {
      session.close();
    }
    return ret;
  }


  public UsersHiberEntity addOnlyUser(String email, String pass, boolean isPublisherUser) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();

    UsersHiberEntity usersHiberEntity = new UsersHiberEntity();
    usersHiberEntity.setPassword(pass);
    usersHiberEntity.setIsPublisher(isPublisherUser);
    usersHiberEntity.setUsername(email);

    try {
      Query query = session.createQuery("from UsersHiberEntity where username='" + email + "'");
      List hasuser = query.list();

      if (!(hasuser != null && hasuser.size() > 0 && hasuser.get(0) != null)) {


        session.save(usersHiberEntity);
        session.flush();
        query = session.createQuery("from UsersHiberEntity where username='" + email + "'");
        hasuser = query.list();

        if ((hasuser != null && hasuser.size() > 0 && hasuser.get(0) != null)) {
          usersHiberEntity = (UsersHiberEntity) hasuser.get(0);

          if (isPublisherUser) {
            UsersRolesHiberEntity usersRolesHiberEntity = new UsersRolesHiberEntity();
            usersRolesHiberEntity.setUserId(usersHiberEntity.getUserId());
            usersRolesHiberEntity.setUsername(usersHiberEntity.getUsername());
            usersRolesHiberEntity.setRolename("publisherrole");
            session.save(usersRolesHiberEntity);
          }


        }

      } else {
        usersHiberEntity.setUserId(-1);
      }
      tra.commit();

    } catch (Exception e) {
      usersHiberEntity = null;

      tra.rollback();
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    return usersHiberEntity;
  }

  public PubBillDet getPubBillDet(int user_id) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    PubBillDet ret = new PubBillDet();
    PubbilltHiberEntity pubbilltHiberEntity = null;


    try {
      Query query = session.createQuery("from PubbilltHiberEntity where id=" + user_id);
      List hasuser = query.list();

      if (hasuser != null && hasuser.size() > 0 && hasuser.get(0) != null) {
        pubbilltHiberEntity = (PubbilltHiberEntity) hasuser.get(0);
      }


      tra.commit();

    } catch (Exception e) {

      tra.rollback();
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    if (pubbilltHiberEntity != null) {
      ret.setId(pubbilltHiberEntity.getId());
      ret.setRecipname(pubbilltHiberEntity.getRecipname());
      ret.setBankaccnum(pubbilltHiberEntity.getBankaccnum());
      ret.setBankcode(pubbilltHiberEntity.getBankcode());
      ret.setBankname(pubbilltHiberEntity.getBankname());

    }
    return ret;
  }

  public UsersTable getUsersByUsername(String username) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    UsersTable ret = new UsersTable();
    UsersHiberEntity usersHiberEntity = null;


    try {
      Query query = session.createQuery("from UsersHiberEntity where username='" + username + "'");
      List hasuser = query.list();

      if (hasuser != null && hasuser.size() > 0 && hasuser.get(0) != null) {
        usersHiberEntity = (UsersHiberEntity) hasuser.get(0);
      }


      tra.commit();

    } catch (Exception e) {

      tra.rollback();
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    if (usersHiberEntity != null) {
      ret.setUser_id(usersHiberEntity.getUserId());
      ret.setUsername(usersHiberEntity.getUsername());
      ret.setPublisher(usersHiberEntity.getIsPublisher());

    }
    return ret;
  }

  public PubCompantDet getPubCompanyDetailes(int user_id) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    PubCompantDet ret = new PubCompantDet();
    PubcompanyHiberEntity pubcompanyHiberEntity = null;


    try {
      Query query = session.createQuery("from PubcompanyHiberEntity where id=" + user_id);
      List hasuser = query.list();

      if (hasuser != null && hasuser.size() > 0 && hasuser.get(0) != null) {
        pubcompanyHiberEntity = (PubcompanyHiberEntity) hasuser.get(0);
      }


      tra.commit();

    } catch (Exception e) {

      tra.rollback();
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    if (pubcompanyHiberEntity != null) {
      ret.setId(pubcompanyHiberEntity.getId());
      ret.setCorpregnum(pubcompanyHiberEntity.getCorpregnum());
      ret.setCityname(pubcompanyHiberEntity.getCityname());
      ret.setCitypostcode(pubcompanyHiberEntity.getCitypostcode());
      ret.setCompname(pubcompanyHiberEntity.getCompname());
      ret.setCorpform(pubcompanyHiberEntity.getCorpform());
      ret.setCountry(pubcompanyHiberEntity.getCountry());
      ret.setStreetname(pubcompanyHiberEntity.getStreetname());
      ret.setStreetnum(pubcompanyHiberEntity.getStreetnum());
      ret.setTaxnum(pubcompanyHiberEntity.getTaxnum());


    }
    return ret;
  }

  public PubUserDet getPubUserDet(int user_id) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    PubUserDet ret = new PubUserDet();
    PubusersHiberEntity pubcompanyHiberEntity = null;


    try {
      Query query = session.createQuery("from PubusersHiberEntity where id=" + user_id);
      List hasuser = query.list();

      if (hasuser != null && hasuser.size() > 0 && hasuser.get(0) != null) {
        pubcompanyHiberEntity = (PubusersHiberEntity) hasuser.get(0);
      }


      tra.commit();

    } catch (Exception e) {

      tra.rollback();
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    if (pubcompanyHiberEntity != null) {
      ret.setId(pubcompanyHiberEntity.getId());
      ret.setCityname(pubcompanyHiberEntity.getCityname());
      ret.setCitynum(pubcompanyHiberEntity.getCitynum());
      ret.setCountry(pubcompanyHiberEntity.getCountry());
      ret.setEmail(pubcompanyHiberEntity.getEmail());
      ret.setFirstname(pubcompanyHiberEntity.getFirstname());
      ret.setLastname(pubcompanyHiberEntity.getLastname());
      ret.setPhonecode(pubcompanyHiberEntity.getPhonecode());
      ret.setPhonenum(pubcompanyHiberEntity.getPhonenum());
      ret.setStreetname(pubcompanyHiberEntity.getStreetname());
      ret.setStreetnum(pubcompanyHiberEntity.getStreetnum());


    }
    return ret;
  }

  public boolean deactivePubstatus(Magazine_tblTable magazine_tblTable) {
    boolean oked = false;
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();


    try {
      Query query =
          session.createQuery("from MagazineTblHiberEntity where magazineId=" + magazine_tblTable.getMagazine_id());

      MagazineTblHiberEntity res = null;

      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        res = (MagazineTblHiberEntity) resList.get(0);
        //TODO pubstatus=1 make deactive this magazine for mobile users
        res.setPubstatus(1);
        session.update(res);

        session.flush();


      }

      tra.commit();
      oked = true;
    } catch (Exception e) {
      oked = false;
      tra.rollback();
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    return oked;
  }

  public boolean isIssueNumberUploaded(Magazine_tblTable magazine_tblTable) {
    boolean oked = false;
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();


    try {
      Query query = session.createQuery(
          "from MagazineTblHiberEntity where issueNum=" + magazine_tblTable.getIssue_num() + " and  title='" +
          magazine_tblTable.getTitle() + "'");

      MagazineTblHiberEntity res = null;

      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        oked = true;
      }

      tra.commit();

    } catch (Exception e) {
      oked = true;
      log.error("Hiber Exception", e);
    } finally {
      session.close();
    }
    return oked;
  }


  public ArrayList<CategoriesTblHiberEntity> getAllcategories() {
    ArrayList<CategoriesTblHiberEntity> ret = new ArrayList<>();
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();


    try {
      Query query = session.createQuery("from CategoriesTblHiberEntity ");


      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        ret = (ArrayList<CategoriesTblHiberEntity>) resList;
      }

      tra.commit();

    } catch (Exception e) {

      log.error("Hiber Exception", e);
    } finally {
      session.close();
    }

    return ret;
  }

  public boolean updateCategories(ArrayList<CategoriesTblHiberEntity> categoriesTblHiberEntities) {
    boolean ret = true;

    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      for (CategoriesTblHiberEntity cat : categoriesTblHiberEntities) {
        Query query = session.createQuery("from CategoriesTblHiberEntity where  catId=" + cat.getCatId());
        List res = query.list();
        if (res != null && res.size() > 0 && res.get(0) != null) {

          CategoriesTblHiberEntity dbcat = (CategoriesTblHiberEntity) res.get(0);

          dbcat.setSug(cat.getSug());
          dbcat.setImgPath(cat.getImgPath());
          dbcat.setCategory(cat.getCategory());

          session.saveOrUpdate(dbcat);
        }


      }

      tra.commit();


    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
      ret = false;
    } finally {

      session.close();

    }
    return ret;
  }

  public boolean updateCategory(CategoriesTblHiberEntity categoriesTblHiberEntity) {
    boolean ret = true;

    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      Query query =
          session.createQuery("from CategoriesTblHiberEntity where  catId=" + categoriesTblHiberEntity.getCatId());
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {

        CategoriesTblHiberEntity dbcat = (CategoriesTblHiberEntity) res.get(0);

        dbcat.setSug(categoriesTblHiberEntity.getSug());
        dbcat.setImgPath(categoriesTblHiberEntity.getImgPath());
        dbcat.setCategory(categoriesTblHiberEntity.getCategory());

        session.saveOrUpdate(dbcat);
      }


      tra.commit();


    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
      ret = false;
    } finally {

      session.close();

    }
    return ret;
  }

  public int upadtePubUserDet(PubUserDet pubUserDet) {
    int ret = 0;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      Query query = session.createQuery("from PubusersHiberEntity where  id=" + pubUserDet.getId());
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {

        PubusersHiberEntity pubusersHiberEntity = (PubusersHiberEntity) res.get(0);

        pubusersHiberEntity.setFirstname(pubUserDet.getFirstname());
        pubusersHiberEntity.setLastname(pubUserDet.getLastname());
        pubusersHiberEntity.setEmail(pubUserDet.getEmail());
        pubusersHiberEntity.setPhonecode(pubUserDet.getPhonecode());
        pubusersHiberEntity.setPhonenum(pubUserDet.getPhonenum());
        pubusersHiberEntity.setStreetname(pubUserDet.getStreetname());
        pubusersHiberEntity.setStreetnum(pubUserDet.getStreetnum());
        pubusersHiberEntity.setCityname(pubUserDet.getCityname());
        pubusersHiberEntity.setCitynum(pubUserDet.getCitynum());
        pubusersHiberEntity.setCountry(pubUserDet.getCountry());
        session.update(pubusersHiberEntity);


      }

      tra.commit();
      ret = 1;

    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
    } finally {

      session.close();

    }
    return ret;
  }

  public int upadtePubBillDet(PubBillDet pubBillDet) {
    int ret = 0;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      Query query = session.createQuery("from PubbilltHiberEntity where  id=" + pubBillDet.getId());
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {

        PubbilltHiberEntity pubbilltHiberEntity = (PubbilltHiberEntity) res.get(0);

        pubbilltHiberEntity.setRecipname(pubBillDet.getRecipname());
        pubbilltHiberEntity.setBankname(pubBillDet.getBankname());
        pubbilltHiberEntity.setBankaccnum(pubBillDet.getBankaccnum());
        pubbilltHiberEntity.setBankcode(pubBillDet.getBankcode());
        session.update(pubbilltHiberEntity);


      }

      tra.commit();
      ret = 1;

    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
    } finally {

      session.close();

    }
    return ret;
  }

  public int upadtePubCompanyDetailes(PubCompantDet pubCompantDet) {
    int ret = 0;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      Query query = session.createQuery("from PubcompanyHiberEntity where  id=" + pubCompantDet.getId());
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {

        PubcompanyHiberEntity pubcompanyHiberEntity = (PubcompanyHiberEntity) res.get(0);

        pubcompanyHiberEntity.setCompname(pubCompantDet.getCompname());
        pubcompanyHiberEntity.setCorpform(pubCompantDet.getCorpform());
        pubcompanyHiberEntity.setStreetname(pubCompantDet.getStreetname());
        pubcompanyHiberEntity.setStreetnum(pubCompantDet.getStreetnum());
        pubcompanyHiberEntity.setCityname(pubCompantDet.getCityname());
        pubcompanyHiberEntity.setCitypostcode("" + pubCompantDet.getCitypostcode());
        pubcompanyHiberEntity.setCountry(pubCompantDet.getCountry());
        pubcompanyHiberEntity.setTaxnum(pubCompantDet.getTaxnum());
        pubcompanyHiberEntity.setCorpregnum(pubCompantDet.getCorpregnum());
        session.update(pubcompanyHiberEntity);


      }

      tra.commit();
      ret = 1;

    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
    } finally {

      session.close();

    }
    return ret;
  }

  public String updateUserPass(String username, String oldPass, String newpass) throws SQLException {
    String selSql = "select password from users where username= ?";
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    String ret = "Can Not Change Password";
    try {
      Query query = session.createQuery("from UsersHiberEntity where  username='" + username + "'");
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        UsersHiberEntity usersHiberEntity = (UsersHiberEntity) res.get(0);

        if (!usersHiberEntity.getPassword().equals(oldPass)) {
          return "Error Current Password Do Not Match";
        }


        usersHiberEntity.setPassword(newpass);

        session.update(usersHiberEntity);
        tra.commit();
        ret = "Password Changed.";
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
    } finally {

      session.close();

    }

    return ret;
  }

  public int addTitles(String title, int pub_id) {
    int ret = 0;

    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    try {
      Query query =
          session.createQuery("from MagazineTitleHiberEntity where pubId=" + pub_id + " and  title='" + title + "'");
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret = -1;
      } else {
        MagazineTitleHiberEntity magazineTitleHiberEntity = new MagazineTitleHiberEntity();
        magazineTitleHiberEntity.setTitle(title);
        magazineTitleHiberEntity.setPubId(pub_id);
        session.save(magazineTitleHiberEntity);
        tra.commit();
        ret = 1;
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
    } finally {

      session.close();

    }
    return ret;
  }

  public HashSet<String> getAllTitles(int pub_id) {
    HashSet<String> ret = new HashSet<>();
    ArrayList<MagazineTitleHiberEntity> magazineTitleHiberEntities = null;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    try {
      Query query = session.createQuery("from MagazineTitleHiberEntity where pubId=" + pub_id);
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        magazineTitleHiberEntities = (ArrayList<MagazineTitleHiberEntity>) res;
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {

      session.close();

    }
    if (magazineTitleHiberEntities != null) {
      for (MagazineTitleHiberEntity title : magazineTitleHiberEntities) {
        ret.add(title.getTitle());
      }
    }
    return ret;
  }

  public ArrayList<Magazine_tblTable> getMagazineByUserId(int user_id, int magazine_id, boolean forMobileUser) {
    ArrayList<Magazine_tblTable> ret = new ArrayList<>();
    ArrayList<MagazineTblHiberEntity> magazineTblHiberEntities = null;
    String hiberQuery = "from MagazineTblHiberEntity where ";
    if (forMobileUser) {
      hiberQuery +=
          (" issueAppear  <= '" + Statistics.nonFullDateFormat.format(new Date()) + "'" + " and pubstatus=0  and ");
    }
    if (magazine_id >= 0 && user_id >= 0) {
      hiberQuery += "userId=" + user_id + " and magazineId=" + magazine_id;
    } else if (magazine_id >= 0) {
      hiberQuery += " magazineId=" + magazine_id;
    } else if (user_id >= 0) {
      hiberQuery += " userId=" + user_id;
    } else {
      return ret;
    }

    Session session = HiberService.getInstance().getSessionFactory().openSession();
    try {
      Query query = session.createQuery(hiberQuery);
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        magazineTblHiberEntities = (ArrayList<MagazineTblHiberEntity>) res;
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {

      session.close();

    }
    if (magazineTblHiberEntities != null) {
      for (MagazineTblHiberEntity mag : magazineTblHiberEntities) {
        ret.add(ReportingFacad.convertMagazineTblHiberEntityToMagazine_tblTable(mag));
      }
    }
    return ret;
  }

  public ArrayList<Magazine_tblTable> getMagazineInCats(String cat, int limit, boolean forMobileUser) {
    ArrayList<Magazine_tblTable> ret = new ArrayList<Magazine_tblTable>();
    ArrayList<MagazineTblHiberEntity> magazineTblHiberEntities = null;
    String hiberQuery = "from MagazineTblHiberEntity where " +
                        (forMobileUser ? " issueAppear  <= '" + Statistics.nonFullDateFormat.format(new Date()) + "'" +
                                         " and pubstatus=0 and " : "") + " category='" + cat +
                        "' order by magazineId desc ";


    Session session = HiberService.getInstance().getSessionFactory().openSession();
    try {
      Query query = session.createQuery(hiberQuery);
      query.setMaxResults(limit);
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        magazineTblHiberEntities = (ArrayList<MagazineTblHiberEntity>) res;
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {

      session.close();

    }
    if (magazineTblHiberEntities != null) {
      for (MagazineTblHiberEntity mag : magazineTblHiberEntities) {
        ret.add(ReportingFacad.convertMagazineTblHiberEntityToMagazine_tblTable(mag));
      }
    }
    return ret;
  }

  public ArrayList<Magazine_tblTable> getMagazineInCats(ArrayList<String> cats, int limit, boolean forMobileUser) {
    ArrayList<Magazine_tblTable> ret = new ArrayList<Magazine_tblTable>();

    for (String cat : cats) {
      ret.addAll(HiberDBFacad.getInstance().getMagazineInCats(cat, (limit), forMobileUser));
    }
    return ret;
  }

  public ArrayList<Magazine_tblTable> getIssues(int mag_id, int num, boolean forMobileUser) {
    ArrayList<Magazine_tblTable> ret = new ArrayList<Magazine_tblTable>();
    ArrayList<MagazineTblHiberEntity> magazineTblHiberEntities = null;
    String hiberQuery = "from MagazineTblHiberEntity where " +
                        (forMobileUser ? " issueAppear  <= '" + Statistics.nonFullDateFormat.format(new Date()) + "'" +
                                         " and pubstatus=0 and " : "") + " magazineId=" + mag_id +
                        " order by magazineId desc ";


    Session session = HiberService.getInstance().getSessionFactory().openSession();
    try {
      Query query = session.createQuery(hiberQuery);
      query.setMaxResults(1);
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        MagazineTblHiberEntity tmpmag = (MagazineTblHiberEntity) res.get(0);
        hiberQuery = "from MagazineTblHiberEntity where " +
                     (forMobileUser ? " issueAppear  <= '" + Statistics.nonFullDateFormat.format(new Date()) + "'" +
                                      " and pubstatus=0 and " : "") + " title='" + tmpmag.getTitle() +
                     "' order by magazineId desc ";
        query = session.createQuery(hiberQuery);
        query.setMaxResults(num);
        res = query.list();
        if (res != null && res.size() > 0 && res.get(0) != null) {
          magazineTblHiberEntities = (ArrayList<MagazineTblHiberEntity>) res;
        }
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {

      session.close();

    }
    if (magazineTblHiberEntities != null) {
      for (MagazineTblHiberEntity mag : magazineTblHiberEntities) {
        ret.add(ReportingFacad.convertMagazineTblHiberEntityToMagazine_tblTable(mag));
      }
    }
    return ret;
  }

  public ArrayList<MagazineTblHiberEntity> getIssues1(int mag_id, int num, boolean forMobileUser) {
    ArrayList<Magazine_tblTable> ret = new ArrayList<Magazine_tblTable>();
    ArrayList<MagazineTblHiberEntity> magazineTblHiberEntities = null;
    String hiberQuery = "from MagazineTblHiberEntity where " +
                        (forMobileUser ? " issueAppear  <= '" + Statistics.nonFullDateFormat.format(new Date()) + "'" +
                                         " and pubstatus=0 and " : "") + " magazineId=" + mag_id +
                        " order by magazineId desc ";


    Session session = HiberService.getInstance().getSessionFactory().openSession();
    try {
      Query query = session.createQuery(hiberQuery);
      query.setMaxResults(1);
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        MagazineTblHiberEntity tmpmag = (MagazineTblHiberEntity) res.get(0);
        hiberQuery = "from MagazineTblHiberEntity where " +
                     (forMobileUser ? " issueAppear  <= '" + Statistics.nonFullDateFormat.format(new Date()) + "'" +
                                      " and pubstatus=0 and " : "") + " title='" + tmpmag.getTitle() +
                     "' order by magazineId desc ";
        query = session.createQuery(hiberQuery);
        query.setMaxResults(num);
        res = query.list();
        if (res != null && res.size() > 0 && res.get(0) != null) {
          magazineTblHiberEntities = (ArrayList<MagazineTblHiberEntity>) res;
        }
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {

      session.close();

    }

    return magazineTblHiberEntities;
  }

  public boolean updateMagazinConvertedto(int mag_id, boolean oked) {
    boolean ret = false;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    try {
      Query query = session.createQuery("from MagazineTblHiberEntity where  magazineId=" + mag_id);
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        MagazineTblHiberEntity magazineTblHiberEntity = (MagazineTblHiberEntity) res.get(0);
        magazineTblHiberEntity.setConstatus(oked ? 1 : 0);


        session.update(magazineTblHiberEntity);
        tra.commit();
        ret = true;
      }
    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
    } finally {

      session.close();

    }

    return ret;
  }

  public int addMagazine(Magazine_tblTable magazine) {


    int id = -1;

    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      int intIndex = magazine.getDir_path().indexOf("PDFs");
      magazine.setDir_path(magazine.getDir_path().substring(intIndex - 1).replaceAll("\\\\", "/"));
      log.debug("Dire Path Change to: " + magazine.getDir_path());
      MagazineTblHiberEntity magazineTbl = ReportingFacad.convertMagazine_tblTableToMagazineTblHiberEntity(magazine);
      session.save(magazineTbl);
      tra.commit();

      Query query = session.createQuery(
          "from MagazineTblHiberEntity where   userId=" + magazine.getUser_id() + " and title='" + magazine.getTitle() +
          "' and category='" + magazine.getCategory() +
          "' and issueNum=" + magazine.getIssue_num() + " and origIssueDate='" + magazineTbl.getOrigIssueDate() + "'");
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        id = ((MagazineTblHiberEntity) res.get(0)).getMagazineId();
      }

      try {
        //            addCategory(magazine.getCategory());
        //            addTitles(magazine.getTitle(),magazine.getUser_id());
        HiberDBFacad.getInstance().addTitles(magazine.getTitle(), magazine.getUser_id());
      } catch (Exception e) {
        log.error("Exception in: ", e);
      }

    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
      return -1;
    } finally {
      session.close();

    }
    return id;

  }


  public Magazine_tblTable[] getMagazinesTitlteInCategory(int catid, boolean forMobileUser) {
    ArrayList<Magazine_tblTable> ret = new ArrayList<Magazine_tblTable>();
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    CategoriesTblHiberEntity categoriesTblHiberEntity = null;
    try {
      Query query = session.createQuery("from CategoriesTblHiberEntity where  catId=" + catid);
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        categoriesTblHiberEntity = (CategoriesTblHiberEntity) res.get(0);


      }

    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
    } finally {

      session.close();

    }
    if (categoriesTblHiberEntity != null) {
      return getMagazinesTitlteInCategory(categoriesTblHiberEntity.getCategory(), forMobileUser);
    }

    return null;
  }

  public Magazine_tblTable[] getMagazinesTitlteInCategory(String cat, boolean mobileUser) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    ArrayList<MagazineTblHiberEntity> ret = null;
    ArrayList<Magazine_tblTable> retarr = null;

    try {
      Query query = session.createQuery(
          "from MagazineTblHiberEntity where  category='" + cat + "' " +
          (mobileUser ? " issueAppear  <= '" + Statistics.nonFullDateFormat.format(new Date()) + "'" +
                        " and pubstatus=0 and " : ""));
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        ret = (ArrayList<MagazineTblHiberEntity>) res;


      }

    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
    } finally {

      session.close();

    }

    if (ret != null) {
      retarr = new ArrayList<>();
      for (MagazineTblHiberEntity mag : ret) {
        retarr.add(ReportingFacad.convertMagazineTblHiberEntityToMagazine_tblTable(mag));
      }
    }
    return retarr.toArray(new Magazine_tblTable[retarr.size()]);
  }

  public ArrayList<String> getCategouries() {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    ArrayList<String> ret = new ArrayList<>();
    ArrayList<CategoriesTblHiberEntity> retarr = null;

    try {
      Query query = session.createQuery("from CategoriesTblHiberEntity ");
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        retarr = (ArrayList<CategoriesTblHiberEntity>) res;
      }

    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
    } finally {

      session.close();

    }
    if (retarr != null) {
      for (CategoriesTblHiberEntity cat : retarr) {
        ret.add(cat.getCategory());
      }
    }
    return ret;
  }

  public String getMagazinePath(int magazine_id) {
    MagazineTblHiberEntity mag = getMagazineById(magazine_id);

    if (mag != null) {
      return mag.getDirPath();
    }
    return null;
  }

  public UsersTable getUserByName_onlyUserID(String userName) {
    return getUsersByUsername(userName);

  }

  public List<Category> getAllCategories(final String url) {
    List<Category> ret = new ArrayList<>();
    ArrayList<CategoriesTblHiberEntity> cats = getAllcategories();
    for (CategoriesTblHiberEntity cat : cats) {
      ret.add(new Category(cat.getCatId(), cat.getCategory(), url + "/id=" + cat.getCatId()));
    }
    return ret;
  }

  public boolean addDownlaodAll(int mag_id) {
    MagazineTblHiberEntity oked = null;
    boolean ret = false;
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();


    try {
      Query query = session.createQuery("from MagazineTblHiberEntity where magazineId=" + mag_id);

      MagazineTblHiberEntity res = null;

      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        res = (MagazineTblHiberEntity) resList.get(0);
        if (res != null) {
          int dlcount = res.getDlcount();
          res.setDlcount(res.getDlcount() + 1);
          session.save(res);
          tra.commit();
          ret = true;
        }

      }

    } catch (Exception e) {
      ret = false;
      tra.rollback();
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }


    return ret;
  }

  public boolean addRegistrationIdFor(int user_id, String reg_id) {
    boolean ret = true;
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();


    try {
      GcmRegistrationIdHiberEntity gcmRegistrationIdHiberEntity = new GcmRegistrationIdHiberEntity();
      gcmRegistrationIdHiberEntity.setUserId(user_id);
      gcmRegistrationIdHiberEntity.setRegistrationId(reg_id);
      session.save(gcmRegistrationIdHiberEntity);
      tra.commit();

      session.flush();


    } catch (Exception e) {
      ret = false;
      tra.rollback();
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    return ret;

  }

  public boolean removeAllRegistrationIdFor(int user_id) {
    return removeRegistrationIdFor(user_id, null);
  }

  public boolean removeRegistrationIdFor(int user_id, String reg_id) {

    boolean ret = true;
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();


    try {
      String allRegs = reg_id == null ? " " : " and registrationId='" + reg_id + "'";
      Query query = session.createQuery("from GcmRegistrationIdHiberEntity where userId= " + user_id + allRegs);


      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        for (Object gcmRegistrationIdHiberEntity : resList) {
          session.delete(gcmRegistrationIdHiberEntity);
          tra.commit();

        }
      }


      session.flush();


    } catch (Exception e) {
      ret = false;
      tra.rollback();
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    return ret;

  }

  public List<String> getSubscribeGCMUsersForMagazine(int mag_id) {
    List<Integer> UsersId = getUserFavorideMagazine(mag_id);
    if (UsersId == null || UsersId.size() <= 0) {
      return null;
    }
    return getUsersReg_ids(UsersId);


  }

  private List<String> getUsersReg_ids(List<Integer> usersId) {
    List<String> ret = new ArrayList<>();
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    StringBuilder inStmt = new StringBuilder(" (");
    for (Integer user : usersId) {
      inStmt.append(user).append(", ");
    }
    int lindx = inStmt.lastIndexOf(", ");
    if (lindx > 0) {
      inStmt.replace(lindx, lindx + 2, "");
    }
    inStmt.append(" ) ");

    Transaction tra = session.beginTransaction();
    List<GcmRegistrationIdHiberEntity> res = null;

    try {
      Query query = session.createQuery("from GcmRegistrationIdHiberEntity where userId in " + inStmt.toString());


      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        res = (List<GcmRegistrationIdHiberEntity>) resList;


      }

    } catch (Exception e) {
      ret = null;
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    if (res == null) {
      return ret;

    }
    for (GcmRegistrationIdHiberEntity gcmRegistrationIdHiberEntity : res) {
      ret.add(gcmRegistrationIdHiberEntity.getRegistrationId());
    }

    return ret;

  }

  private List<Integer> getUserFavorideMagazine(int mag_id) {

    List<MagazineTblHiberEntity> allMagz = getPublisherAllMagazineByOneOfMag_id(mag_id);

    List<Integer> ret = new ArrayList<>();
    if (allMagz.size() <= 0) {
      return ret;
    }
    StringBuilder usersString = new StringBuilder("(");
    for (MagazineTblHiberEntity magazineTblHiberEntity : allMagz) {
      usersString.append(magazineTblHiberEntity.getMagazineId()).append(", ");
    }
    int lindex = usersString.lastIndexOf(", ");
    usersString.replace(lindex, lindex + 2, "").append(") ");
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();
    List<MobileuserfavorHiberEntity> res = null;

    try {
      Query query = session.createQuery("from MobileuserfavorHiberEntity where magId in " + usersString.toString());


      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        res = (List<MobileuserfavorHiberEntity>) resList;


      }

    } catch (Exception e) {
      ret = null;
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    if (res == null) {
      return ret;

    }
    for (MobileuserfavorHiberEntity mobileuserfavorHiberEntity : res) {
      ret.add(mobileuserfavorHiberEntity.getUserId());
    }

    return ret;
  }

  private List<MagazineTblHiberEntity> getPublisherAllMagazineByOneOfMag_id(int mag_id) {
    List<MagazineTblHiberEntity> ret = new ArrayList<>();
    MagazineTblHiberEntity mag = getMagazineById(mag_id);
    if (mag == null) {
      return ret;
    }
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();
    List<MobileuserfavorHiberEntity> res = null;

    try {

      Query query = session.createQuery(
          "from MagazineTblHiberEntity where  pubstatus=0 and " + Statistics.getIssueAppearWhereLimit() +
          " and title='" + mag.getTitle() + "' ");


      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        ret = (List<MagazineTblHiberEntity>) resList;


      }

    } catch (Exception e) {
      ret = null;
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    return ret;
  }

  public CategoriesTblHiberEntity category(final String id) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    CategoriesTblHiberEntity categoriesTblHiberEntity = null;
    try {
      Query query = session.createQuery("from CategoriesTblHiberEntity where  catId=" + id);
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        categoriesTblHiberEntity = (CategoriesTblHiberEntity) res.get(0);
      }

    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    return categoriesTblHiberEntity;
  }

  public List<BannersTblHiberEntity> banners() {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    List<BannersTblHiberEntity> bannersTblHiberEntities = new ArrayList<>();
    try {
      Query query = session.createQuery("from BannersTblHiberEntity order by id desc");
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        bannersTblHiberEntities = (List<BannersTblHiberEntity>) res;
      }

    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    return bannersTblHiberEntities;
  }

  public boolean updateBanner(final BannersTblHiberEntity bannersTblHiberEntity) {
    boolean ret = false;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    try {
      Query query =
          session.createQuery("from BannersTblHiberEntity where  id=" + bannersTblHiberEntity.getId());
      List res = query.list();
      BannersTblHiberEntity dbBanner;
      if (res != null && res.size() > 0 && res.get(0) != null) {

        dbBanner = (BannersTblHiberEntity) res.get(0);
        dbBanner.setActive(bannersTblHiberEntity.getActive());
        dbBanner.setDesc(bannersTblHiberEntity.getDesc());
        dbBanner.setImgPath(bannersTblHiberEntity.getImgPath());
        dbBanner.setMagazineId(bannersTblHiberEntity.getMagazineId());
        session.update(dbBanner);
        ret = true;
      }


      tra.commit();


    } catch (Exception e) {
      log.error("Exception in: ", e);
      ret = false;
      tra.rollback();
    } finally {
      session.close();
    }
    return ret;
  }

  public List<PubcompanyHiberEntity> publisherNameLike(
      final String publisherCompantNameLike) {

    List<PubcompanyHiberEntity> ret = new ArrayList<>();
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "from PubcompanyHiberEntity where compname like '%" + publisherCompantNameLike + "%'");


    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        ret = (ArrayList<PubcompanyHiberEntity>) resList;
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;
  }

  public List<PubcompanyHiberEntity> publisherIdLike(final Integer publisherIdLike) {
    List<PubcompanyHiberEntity> ret = new ArrayList<>();
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "from PubcompanyHiberEntity where (" +
        "(id=" + publisherIdLike + ") or " +
        "(" +
        "(id>" + publisherIdLike + ") and " +
        "(" +
        " or (id/10)=" + publisherIdLike +
        " or (id/100)=" + publisherIdLike +
        " or (id/1000)=" + publisherIdLike +
        " or (id/10000) =" + publisherIdLike +
        " or (id/100000) =" + publisherIdLike +
        " or (id/1000000)=" + publisherIdLike +
        ")" +
        ")" +
        ")"
                                     );
    try {
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        ret = (ArrayList<PubcompanyHiberEntity>) resList;
      }
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;
  }

  public List<MagazineTblHiberEntity> allMagazines() {
    List<MagazineTblHiberEntity> ret = new ArrayList<>();
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    try {
      Query query = session.createQuery("from MagazineTblHiberEntity");
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        ret = (List<MagazineTblHiberEntity>) resList;
      }
    } catch (Exception e) {
      ret = null;
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    return ret;
  }

  public List<MagazineTblHiberEntity> MagazinesOf(final int pubIds) {

    List<MagazineTblHiberEntity> ret = new ArrayList<>();

    Session session = HiberService.getInstance().getSessionFactory().openSession();
    try {
      Query query = session.createQuery("from MagazineTblHiberEntity where userId=" + pubIds);
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        ret = (List<MagazineTblHiberEntity>) resList;
      }

    } catch (Exception e) {
      ret = null;
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    return ret;
  }

  public List<MagazineTblHiberEntity> MagazinesOf(final Collection<Integer> pubIds) {

    List<MagazineTblHiberEntity> ret = new ArrayList<>();
    StringBuilder usersString = new StringBuilder("(");
    for (int pubId : pubIds) {
      usersString.append(pubId).append(", ");
    }
    int lindex = usersString.lastIndexOf(", ");
    usersString.replace(lindex, lindex + 2, "").append(") ");
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    try {
      Query query = session.createQuery("from MagazineTblHiberEntity where userId in " + usersString.toString());
      List resList = query.list();
      if (resList != null && resList.size() > 0 && resList.get(0) != null) {
        ret = (List<MagazineTblHiberEntity>) resList;
      }

    } catch (Exception e) {
      ret = null;
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    return ret;
  }


  public List<Integer> bannerIds() {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    List<Integer> bannerIds = new ArrayList<>();
    try {
      Query query = session.createQuery("select ban.id from BannersTblHiberEntity ban");
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        bannerIds = (List<Integer>) res;
      }

    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    return bannerIds;
  }

  public BannersTblHiberEntity banner(int id) {
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    BannersTblHiberEntity banner = null;
    try {
      Query query = session.createQuery("from BannersTblHiberEntity where id=" + id);
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        banner = (BannersTblHiberEntity) res.get(0);
      }

    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    return banner;
  }

  public boolean removeBanner(final BannersTblHiberEntity bannersTblHiberEntity) {
    boolean ret = false;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    try {
      session.delete(bannersTblHiberEntity);

      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
      return false;
    } finally {
      session.close();
    }
    return true;
  }

  public UsersHiberEntity userUUID(final String uuid) {
    if ("".equals(uuid)) {
      return null;
    }
    UsersHiberEntity usersHiberEntity = null;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    try {
      Query query = session.createQuery("from UsersHiberEntity where uuid='" + uuid + "'");
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        usersHiberEntity = (UsersHiberEntity) res.get(0);
      }


    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();
    }
    return usersHiberEntity;
  }

  public boolean updateUserUUID(final String uuid, final String username) {
    boolean ret = false;
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    try {
      Query query = session.createQuery("from UsersHiberEntity where username='" + username + "'");
      List res = query.list();
      if (res != null && res.size() > 0 && res.get(0) != null) {
        UsersHiberEntity usersHiberEntity = (UsersHiberEntity) res.get(0);
        usersHiberEntity.setUuid(uuid);
        session.update(usersHiberEntity);
      }
      tra.commit();
      ret = true;
    } catch (Exception e) {
      log.error("Exception in: ", e);
      tra.rollback();
      ret = false;
    } finally {
      session.close();
    }
    return ret;

  }
}