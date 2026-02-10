package ir.mjm.DBAO;

import ir.mjm.DBAO.hiber.IssuepagesessionHiberEntity;
import ir.mjm.DBAO.hiber.MagazineTblHiberEntity;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Serap on 7/17/14.
 */
public class ReportingFacad {
  static final Logger log = Logger.getLogger(ReportingFacad.class);

  private static ReportingFacad ourInstance = new ReportingFacad();

  public static ReportingFacad getInstance() {
    return ourInstance;
  }

  private ReportingFacad() {
  }

  public ArrayList<MagazineRepSpecTitle> getSpecTitleView(int pu_id, String title) {
    ArrayList<MagazineRepSpecTitle> ret = new ArrayList<MagazineRepSpecTitle>();
    for (Magazine_tblTable magazine_tblTable : getIssuesOfPubInTitle(pu_id, title)) {
      MagazineRepSpecTitle magazineRepSpecTitle = new MagazineRepSpecTitle();

      magazineRepSpecTitle.setMagazine_tblTable(magazine_tblTable);

      magazineRepSpecTitle.setAverageLengthPerReadingSession(getAverageLengthPerReadingSession(magazine_tblTable.getMagazine_id()));

      magazineRepSpecTitle.setAveragePageViews(getAveragePageViews(magazine_tblTable.getMagazine_id()));

      magazineRepSpecTitle.setGeogrHotspotPostCode(getGeogrHotspotPostCode(magazine_tblTable.getMagazine_id()));

      magazineRepSpecTitle.setTotalaReadingsessions(getTotalaReadingsessions(magazine_tblTable.getMagazine_id()));

      magazineRepSpecTitle.setTotalPageViews(getTotalPageViews(magazine_tblTable.getMagazine_id()));

      magazineRepSpecTitle.setTotalAdPageViews(getTotalAdPageViews(magazine_tblTable.getMagazine_id()));

      magazineRepSpecTitle.setRevenuesEarned(getRevenuesEarned(magazine_tblTable.getMagazine_id()));

      magazineRepSpecTitle.setTotalDownloadsPerReach(getTotalDownloadsPerReach(magazine_tblTable.getMagazine_id()));

      magazineRepSpecTitle.setAverageReaderAge(getAverageReaderAgeFor(magazine_tblTable.getMagazine_id()));

      ret.add(magazineRepSpecTitle);
    }
    return ret;
  }


  public double getAverageReaderAgeFor(int Mag_id) {
    return HiberDBFacad.getInstance().getAverageReaderAgeFor(Mag_id);
  }


  public String getGeogrHotspotPostCode(int Mag_id) {
    String res = HiberDBFacad.getInstance().getHotSpotFor(Mag_id);
    return res;
  }

  public double getAveragePageViews(int Mag_id) {
    return HiberDBFacad.getInstance().getAveragePageViews(Mag_id);
  }

  public int getTotalPageViews(int Mag_id) {
    return HiberDBFacad.getInstance().getTotalPageViews(Mag_id);

  }

  public int getTotalAdPageViews(int Mag_id) {
    return HiberDBFacad.getInstance().getTotalAdPageViews(Mag_id);
  }

  public double getAverageLengthPerReadingSession(int Mag_id) {
    return HiberDBFacad.getInstance().getAverageLengthPerReadingSession(Mag_id);
  }

  public int getTotalaReadingsessions(int Mag_id) {
    return HiberDBFacad.getInstance().getTotalReadingSessions(Mag_id);
  }

  public double getRevenuesEarned(int Mag_id) {
    return HiberDBFacad.getInstance().getRevenuesEarned(Mag_id);
  }

  public int getTotalDownloadsPerReach(int Mag_id) {
    int total = HiberDBFacad.getInstance().getTotalDownload(Mag_id);
    return total;
  }

  public ArrayList<Magazine_tblTable> getIssuesOfPubInTitle(int pu_id, String title) {
    ArrayList<Magazine_tblTable> ret = new ArrayList<Magazine_tblTable>();
    ArrayList<MagazineTblHiberEntity> magazineTblHiberEntities =
        HiberDBFacad.getInstance().getMagazinesOfPubInTitle(pu_id, title);
    if (magazineTblHiberEntities != null) {
      for (MagazineTblHiberEntity magazineTblHiberEntity : magazineTblHiberEntities) {
        ret.add(convertMagazineTblHiberEntityToMagazine_tblTable(magazineTblHiberEntity));
      }
    }
    return ret;
  }


  public static synchronized MagazineTblHiberEntity convertMagazine_tblTableToMagazineTblHiberEntity(Magazine_tblTable magazine_tblTable) {
    MagazineTblHiberEntity magazineTblHiberEntity = new MagazineTblHiberEntity();

    magazineTblHiberEntity.setTitle(magazine_tblTable.getTitle());
    magazineTblHiberEntity.setAppertype(magazine_tblTable.getAppertype()!=null?magazine_tblTable.getAppertype().getName():null);
    magazineTblHiberEntity.setPrice(magazine_tblTable.getPrice());
    magazineTblHiberEntity.setCategory(magazine_tblTable.getCategory());
    magazineTblHiberEntity.setDescription(magazine_tblTable.getDescription());
    magazineTblHiberEntity.setDirPath(magazine_tblTable.getDir_path());
    magazineTblHiberEntity.setUserId(magazine_tblTable.getUser_id());
    magazineTblHiberEntity.setMagType(magazine_tblTable.getMagazineType());
    magazineTblHiberEntity.setMagKeywords(magazine_tblTable.getMagazineKeywords());
    //        try {
    magazineTblHiberEntity.setDateTime(Statistics.FullDateFormat.format(magazine_tblTable.getDate_time()));
    //        } catch (ParseException e) {
    //            log.error("Exception in: ", e);
    //        }
    //        try {
    String isuuAppTime =
        (magazine_tblTable.getIssue_appear() == null ? Statistics.nonFullDateFormat.format(new Date()) :
             Statistics.nonFullDateFormat.format(magazine_tblTable.getIssue_appear()));
    magazineTblHiberEntity.setIssueAppear(isuuAppTime);
    //        } catch (ParseException e) {
    //            log.error("Exception in: ", e);
    //        }
    //        try {
    magazineTblHiberEntity.setOrigIssueDate(
        magazine_tblTable.getOrig_issue_date() == null ? Statistics.nonFullDateFormat.format(new Date()) :
            Statistics.nonFullDateFormat.format(magazine_tblTable.getOrig_issue_date()));
    //        } catch (ParseException e) {
    //            log.error("Exception in: ", e);
    //        }


    magazineTblHiberEntity.setMagazineId(magazine_tblTable.getMagazine_id());
    magazineTblHiberEntity.setIssueNum(magazine_tblTable.getIssue_num());
    magazineTblHiberEntity.setIssueYear(magazine_tblTable.getIssue_year());
    magazineTblHiberEntity.setLang(
        magazine_tblTable.getLanguage() == null ? "English" : magazine_tblTable.getLanguage());
    magazineTblHiberEntity.setConstatus(magazine_tblTable.getStatus());
    magazineTblHiberEntity.setDlcount(magazine_tblTable.getDlcount());
    magazineTblHiberEntity.setFavorcount(magazine_tblTable.getFavorcount());
    magazineTblHiberEntity.setPubstatus(magazine_tblTable.getStatus());


    return magazineTblHiberEntity;
  }

  public static synchronized Magazine_tblTable convertMagazineTblHiberEntityToMagazine_tblTable(MagazineTblHiberEntity magazineTblHiberEntity) {
    Magazine_tblTable magazine_tblTable = new Magazine_tblTable();

    magazine_tblTable.setTitle(magazineTblHiberEntity.getTitle());
    magazine_tblTable.setAppertype((magazineTblHiberEntity.getAppertype()));
    magazine_tblTable.setPrice(magazineTblHiberEntity.getPrice());
    magazine_tblTable.setCategory(magazineTblHiberEntity.getCategory());
    magazine_tblTable.setDir_path(magazineTblHiberEntity.getDirPath());
    magazine_tblTable.setUser_id(magazineTblHiberEntity.getUserId());
    magazine_tblTable.setMagazineKeywords(magazineTblHiberEntity.getMagKeywords());
    magazine_tblTable.setMagazineType(magazineTblHiberEntity.getMagType());
    try {
      magazine_tblTable.setDate_time(Statistics.FullDateFormat.parse(magazineTblHiberEntity.getDateTime()));
    } catch (ParseException e) {
      log.error("Exception in: ", e);
    }
    try {
      magazine_tblTable.setIssue_appear(Statistics.nonFullDateFormat.parse(magazineTblHiberEntity.getIssueAppear()));
    } catch (ParseException e) {
      log.error("Exception in: ", e);
    }
    try {
      magazine_tblTable.setOrig_issue_date(Statistics.nonFullDateFormat.parse(magazineTblHiberEntity.getOrigIssueDate()));
    } catch (ParseException e) {
      log.error("Exception in: ", e);
    }


    magazine_tblTable.setMagazine_id(magazineTblHiberEntity.getMagazineId());
    magazine_tblTable.setIssue_num(magazineTblHiberEntity.getIssueNum());
    magazine_tblTable.setIssue_year(magazineTblHiberEntity.getIssueYear());
    magazine_tblTable.setLanguage(magazineTblHiberEntity.getLang());
    magazine_tblTable.setConverted(magazineTblHiberEntity.getConstatus() == 1 ? true : false);
    magazine_tblTable.setDlcount(magazineTblHiberEntity.getDlcount());
    magazine_tblTable.setFavorcount(magazineTblHiberEntity.getFavorcount());
    magazine_tblTable.setStatus(magazineTblHiberEntity.getPubstatus());


    return magazine_tblTable;
  }

  public HashMap<String, Float> getAgeDistribution(int pubid, int magid) {
    HashMap<String, Float> result = null;
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "select  cus.mobileuserTblByUserId.bdate from CustomerownedissuesHiberEntity as cus where cus.magId=" + magid);

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
        result = new HashMap<String, Float>();
        int counter = 0;
        for (Object bdate : bdtaes) {
          try {
            Date birthdate = Statistics.nonFullDateFormat.parse((String) bdate);
            int age = today.getYear() - birthdate.getYear();
            if (age < 16) {
              //Nothing!
            } else if (age < 23) {
              String key = "16-22";
              Float val = result.get(key);
              result.put(key, (val == null ? 1 : val + 1));
            } else if (age < 30) {
              String key = "23-29";
              Float val = result.get(key);
              result.put(key, (val == null ? 1 : val + 1));
            } else if (age < 35) {
              String key = "30-34";
              Float val = result.get(key);
              result.put(key, (val == null ? 1 : val + 1));

            } else if (age < 40) {
              String key = "35-39";
              Float val = result.get(key);
              result.put(key, (val == null ? 1 : val + 1));

            } else if (age < 45) {
              String key = "40-44";
              Float val = result.get(key);
              result.put(key, (val == null ? 1 : val + 1));

            } else if (age < 50) {
              String key = "45-49";
              Float val = result.get(key);
              result.put(key, (val == null ? 1 : val + 1));

            } else if (age < 60) {
              String key = "50-59";
              Float val = result.get(key);
              result.put(key, (val == null ? 1 : val + 1));

            } else {
              String key = "+60";
              Float val = result.get(key);
              result.put(key, (val == null ? 1 : val + 1));

            }
            counter++;
          } catch (ParseException e) {
            log.error("Exception in: ", e);
          }
        }
      }
    }


    if (result != null) {
      float sum = 0;
      for (float value : result.values()) {
        sum += value;
      }
      for (Map.Entry<String, Float> keyvalue : result.entrySet()) {

        float value = (keyvalue.getValue() * 100) / sum;
        keyvalue.setValue(value);
      }
    }


    return result;
  }


  public HashMap<String, Float> getGeoDistribution(int i, int selectedMag) {
    HashMap<String, Float> result = null;
    Session session = HiberService.getInstance().getSessionFactory().openSession();


    //        Transaction tra = session.beginTransaction();
    Query query = session.createQuery(
        "select  cus.mobileuserTblByUserId.postcode from CustomerownedissuesHiberEntity as cus where cus.magId=" +
        selectedMag);

    List postcodelist = null;
    try {
      postcodelist = query.list();
      //            tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }

    if (postcodelist != null) {
      if (postcodelist.size() > 0) {
        Date today = new Date();
        result = new HashMap<>();
        //                int counter=0;
        for (Object postcodeobj : postcodelist) {

          String postcode = postcodeobj.toString();
          if (postcode.compareTo("10000") == -1) {
            //Nothing!
          } else if (postcode.compareTo("20000") == -1) {
            String key = "10000-19999";
            Float val = result.get(key);
            result.put(key, (val == null ? 1 : val + 1));
          } else if (postcode.compareTo("30000") == -1) {
            String key = "20000-29999";
            Float val = result.get(key);
            result.put(key, (val == null ? 1 : val + 1));
          } else if (postcode.compareTo("40000") == -1) {
            String key = "30000-39999";
            Float val = result.get(key);
            result.put(key, (val == null ? 1 : val + 1));

          } else if (postcode.compareTo("50000") == -1) {
            String key = "40000-49999";
            Float val = result.get(key);
            result.put(key, (val == null ? 1 : val + 1));

          } else if (postcode.compareTo("60000") == -1) {
            String key = "50000-59999";
            Float val = result.get(key);
            result.put(key, (val == null ? 1 : val + 1));

          } else if (postcode.compareTo("70000") == -1) {
            String key = "60000-69999";
            Float val = result.get(key);
            result.put(key, (val == null ? 1 : val + 1));

          } else if (postcode.compareTo("80000") == -1) {
            String key = "70000-79999";
            Float val = result.get(key);
            result.put(key, (val == null ? 1 : val + 1));

          } else if (postcode.compareTo("90000") == -1) {
            String key = "80000-89999";
            Float val = result.get(key);
            result.put(key, (val == null ? 1 : val + 1));

          } else if (postcode.compareTo("99999") <= 0) {
            String key = "90000-99999";
            Float val = result.get(key);
            result.put(key, (val == null ? 1 : val + 1));

          }
          //                        counter++;

        }
      }
    }


    if (result != null) {
      float sum = 0;
      for (float value : result.values()) {
        sum += value;
      }
      for (Map.Entry<String, Float> keyvalue : result.entrySet()) {

        float value = (keyvalue.getValue() * 100) / sum;
        keyvalue.setValue(value);
      }
    }


    return result;
  }

  public int[] getTotalPageViewsInDatesNewAndOld(int mag_id, String startDate, String endDate) {
    int[] ret = new int[]{0, 0};
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -30);
    Date oneWeekAgo = cal.getTime();

    //        Query query=session.createQuery("select  userId  from  CustomerownedissuesHiberEntity  where buyDate<='"+onweekagoString+"' and magId in (select magazineId from MagazineTblHiberEntity where userId="+pubId+")");
    Query query = session.createQuery(
        "select distinct userId from IssuepagesessionHiberEntity where magId=" + mag_id +
        " and sessionstarttime < '" + startDate + "' ");
    List res = null;
    try {
      res = query.list();
      ret[0] = res.size();
    } catch (Exception e) {

    }
    Query query2 = session.createQuery(
        "select distinct userId from  IssuepagesessionHiberEntity where magId=" + mag_id +
        " and sessionstarttime >= '" + startDate + "' and sessionstarttime < '" + endDate + "'" +
        "and userId not in (select distinct userId from IssuepagesessionHiberEntity where magId=" + mag_id +
        " and sessionstarttime < '" + startDate + "' )");

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

  public int[] getTotalAdPageViewsInDatesNewAndOld(int mag_id, String startDate, String endDate) {
    int[] ret = new int[]{0, 0};
    Session session = HiberService.getInstance().getSessionFactory().openSession();

    Query query = session.createQuery(
        "select distinct userId from IssuepagesessionHiberEntity where magId=" + mag_id +
        " and sessionstarttime < '" + startDate +
        "' and (magId,page) in   (select magId,pageNumber from PagesHiberEntity where pageType=" + Statistics.AD +
        " and magId=" + mag_id + ")");
    List res = null;
    try {
      res = query.list();
      ret[0] = res.size();
    } catch (Exception e) {

    }
    Query query2 = session.createQuery(
        "select distinct userId from  IssuepagesessionHiberEntity where magId=" + mag_id +
        " and sessionstarttime >= '" + startDate + "' and sessionstarttime < '" + endDate + "' " +
        "and (magId,page) in   (select magId,pageNumber from PagesHiberEntity where pageType=" + Statistics.AD +
        " and magId=" + mag_id + ") and "

        + "userId not in (select distinct userId from IssuepagesessionHiberEntity where magId=" + mag_id +
        " and sessionstarttime < '" + startDate +
        "'  and (magId,page) in (select magId,pageNumber from PagesHiberEntity where pageType=1 and magId=" + mag_id +
        ") )");

    try {

      ret[1] = query2.list().size();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;
  }

  public double[] getAverageReadingSessionsNewAndOld(int mag_id, String startDate, String endDate) {
    double[] ret = new double[]{0.0, 0.0};


    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();
    Criteria criteria = session.createCriteria(IssuepagesessionHiberEntity.class);

    ProjectionList projList = Projections.projectionList();
    projList.add((Projections.property("sessionduration")));
    projList.add((Projections.property("sessionstarttime")));
    projList.add((Projections.property("magId")));
    projList.add((Projections.property("userId")));

    criteria.setProjection(Projections.distinct(projList));
    //        criteria.setProjection(Projections.avg("sessionduration"));

    criteria.add(Restrictions.lt("sessionstarttime", startDate));
    criteria.add(Restrictions.eq("magId", mag_id));
    //        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

    //        Query query=session.createSQLQuery("select avg(sessionduration) from (select distinct user_id,sessionduration from issuepagesession where mag_id="+mag_id+
    //                " and sessionstarttime < '"+startDate+"') ");
    //        Query query1=session.createQuery("select distinct userId from  IssuepagesessionHiberEntity  where magId="+mag_id+" and  (select issue2.magId issue2.  from IssuepagesessionHiberEntity as issue2 distinct issue2. issue2.sessionduration  where mag_id="+mag_id+
    //                " and sessionstarttime < '"+startDate+"') ");
    List res = null;
    try {
      res = criteria.list();
      int tmptSum = 0;
      if (res != null && res.size() > 0 && res.get(0) != null) {
        for (Object ob : res) {
          tmptSum += (int) ((Object[]) ob)[0];
        }
        log.debug("getAverageReadingSessionsNewAndOld: [0] size:" + res.size());
        ret[0] = (tmptSum * 1.0) / res.size();
        log.debug("getAverageReadingSessionsNewAndOld: ret[0]=" + ret[0]);

      }

    } catch (Exception e) {
      log.error("Exception in: ", e);
    }

    Criteria criteria3 = session.createCriteria(IssuepagesessionHiberEntity.class);

    ProjectionList projList3 = Projections.projectionList();

    projList3.add((Projections.property("userId")));

    criteria3.setProjection(Projections.distinct(projList3));
    //        criteria.setProjection(Projections.avg("sessionduration"));

    criteria3.add(Restrictions.lt("sessionstarttime", startDate));
    criteria3.add(Restrictions.eq("magId", mag_id));
    //        Query query1=session.createQuery("select distinct userId from  IssuepagesessionHiberEntity  where magId="+mag_id+" and   sessionstarttime < '"+startDate+"') ");
    List res3 = null;
    try {
      res3 = criteria3.list();
      //            if(res3.size()>0 && res3.get(0)!=null)
      //                ret[0]= Double.parseDouble(res3.get(0)+"");

    } catch (Exception e) {
      log.error("Exception in: ", e);
    }

    //        Session session3 =  HiberService.getInstance().getSessionFactory().openSession();
    //        Criteria  criteria3 = session3.createCriteria( IssuepagesessionHiberEntity.class );
    //        criteria3.setProjection(Projections.distinct(Projections.property("userId")));
    //        criteria3.setProjection(Projections.property("userId"));
    //        criteria3.add(Restrictions.lt("sessionstarttime", startDate));
    //        criteria3.add(Restrictions.eq("magId", mag_id));

    Session session2 = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra2 = session2.beginTransaction();
    Criteria criteria2 = session2.createCriteria(IssuepagesessionHiberEntity.class);


    ProjectionList projList2 = Projections.projectionList();
    projList2.add((Projections.property("sessionduration")));
    projList2.add((Projections.property("sessionstarttime")));
    projList2.add((Projections.property("magId")));
    projList2.add((Projections.property("userId")));

    criteria2.setProjection(Projections.distinct(projList2));


    criteria2.add(Restrictions.gt("sessionstarttime", startDate));
    criteria2.add(Restrictions.lt("sessionstarttime", endDate));
    if (res3 != null && res3.size() > 0 && res3.get(0) != null) {
      criteria2.add(Restrictions.not(Restrictions.in("userId", res3)));
    }
    criteria2.add(Restrictions.eq("magId", mag_id));

    //        Query query2=session.createSQLQuery("select avg(sessionduration) from (select distinct user_id,sessionduration from issuepagesession where mag_id="+mag_id+
    //                " and sessionstarttime < '"+startDate+"') a");
    List res2 = null;
    try {
      res2 = criteria2.list();
      int tmptSum = 0;
      if (res2 != null && res2.size() > 0 && res2.get(0) != null) {
        for (Object ob : res2) {
          tmptSum += (int) ((Object[]) ob)[0];
        }
        ret[1] = (tmptSum * 1.0) / res2.size();
        log.debug("getAverageReadingSessionsNewAndOld: ret[1]=" + ret[1]);
        log.debug("getAverageReadingSessionsNewAndOld: [1] size:" + res2.size());

      }
      tra.commit();
      tra2.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();
      session2.close();
    }
    return ret;
  }

  public int[] getTotalReadingSessionsNewAndOld(int mag_id, String startDate, String endDate) {
    int[] ret = new int[]{0, 0};
    Session session = HiberService.getInstance().getSessionFactory().openSession();
    Transaction tra = session.beginTransaction();

    Query query = session.createQuery(
        "select distinct userId,sessionduration from IssuepagesessionHiberEntity where magId=" + mag_id +
        " and sessionstarttime < '" + startDate + "'");
    List res = query.list();
    try {
      ret[0] = res.size();
    } catch (Exception e) {

    }

    Query query2 = session.createQuery(
        "select distinct userId,sessionduration from IssuepagesessionHiberEntity where magId=" + mag_id +
        " and sessionstarttime >= '" + startDate + "' and sessionstarttime <'" + endDate + "' and userId not in" +
        "(select distinct userId from IssuepagesessionHiberEntity where magId=" + mag_id +
        " and sessionstarttime < '" + startDate + "')");

    res = query2.list();
    try {

      ret[1] = res.size();
      tra.commit();
    } catch (Exception e) {
      log.error("Exception in: ", e);
    } finally {
      session.close();

    }
    return ret;
  }

  public int[] getTotalDownloadsNewAndOld(int mag_id, String startDate, String endDate) {

    int[] ret = new int[]{0, 0};
        /*Session session =  HiberService.getInstance().getSessionFactory().openSession();
        Transaction tra= session.beginTransaction();
        Query query=session.createQuery("select count(*) from CustomerownedissuesHiberEntity where magId="+mag_id+
                " and buyDate < '"+startDate+"'");
//        Query query=session.createQuery("select distinct userId,sessionduration from IssuepagesessionHiberEntity where magId="+mag_id+
//                " and sessionstarttime < '"+startDate+"'");
        List res=query.list();
        try {
            if(res!=null&&res.size()>0 && res.get(0)!=null)
                ret[0]= Integer.valueOf(res.get(0).toString());
        }catch (Exception e){

        }

        Query query2=session.createQuery("select distinct userId,sessionduration from IssuepagesessionHiberEntity where magId="+mag_id+
                " and sessionstarttime >= '"+startDate+"' and sessionstarttime <'"+endDate+"' and userId not in"+
                "(select distinct userId from IssuepagesessionHiberEntity where magId="+mag_id+
                " and sessionstarttime < '"+startDate+"')");

        res= query2.list();
        try{

            ret[1]= res.size();
            tra.commit();
        }catch (Exception e){
            log.error("Exception in: ",e);
        }
        finally {
            session.close();

        }*/
    return ret;
  }
}
