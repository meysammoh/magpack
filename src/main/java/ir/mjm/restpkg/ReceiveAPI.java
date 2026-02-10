package ir.mjm.restpkg;

import com.google.gson.Gson;
import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.Statistics;
import ir.mjm.DBAO.hiber.MobileuserTblHiberEntity;
import ir.mjm.DBAO.hiber.UsersHiberEntity;
import ir.mjm.restBeans.MobileUser;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Created by Serap on 7/9/14.
 */
@Path("users")
public class ReceiveAPI extends MobileUserAuth{
  static final Logger log = Logger.getLogger(ReceiveAPI.class);



  @GET
  @Path("setfavorite")
  @Produces("text/plain")
  public String userFavoriteAction(
      @DefaultValue("0") @QueryParam("mag_id") int mag_id,
      @DefaultValue("true") @QueryParam("favor") boolean favor) {
    int user_id = isLogrdInRecApi(request, req);

    if (user_id < 0) {
      return pleaseLoginMesage();
    }
    if ((mag_id <= 0)) {
      return "Please Fill mag_id   Query Parameters.";
    }
    String oked = "NOK";
    try {
      oked = HiberDBFacad.getInstance().updateUserFavorite(user_id, mag_id, favor);
    } catch (Exception e) {
      log.error("Exception in user_id: " + user_id, e);
    }
    return oked;

  }

  @GET
  @Path("addgcmid")
  @Produces("text/plain")
  public String addRegistration_id(@DefaultValue("") @QueryParam("registration_id") String reg_id) {

    int user_id = isLogrdInRecApi(request, req);
    if (user_id < 0) {
      return pleaseLoginMesage();
    }
    if (reg_id.equals("")) {
      return "Please Fill correct registration_id";
    }

    //        try {
    //            boolean oked=DBFacade.getInstance().addDownlaodAll(mag_id);
    boolean oked = HiberDBFacad.getInstance().addRegistrationIdFor(user_id, reg_id);
    return oked ? "OK" : "NOK";
    //        } catch (SQLException e) {
    //            log.error("Exception in user_id: "+user_id, e);
    //        }

  }

  @GET
  @Path("removegcmid")
  @Produces("text/plain")
  public String removeRegistration_id(@DefaultValue("") @QueryParam("registration_id") String reg_id) {

    int user_id = isLogrdInRecApi(request, req);
    if (user_id < 0) {
      return pleaseLoginMesage();
    }
    if (reg_id.equals("")) {
      return "Please Fill correct registration_id";
    }

    //        try {
    //            boolean oked=DBFacade.getInstance().addDownlaodAll(mag_id);
    boolean oked = HiberDBFacad.getInstance().removeRegistrationIdFor(user_id, reg_id);
    return oked ? "OK" : "NOK";
    //        } catch (SQLException e) {
    //            log.error("Exception in user_id: "+user_id, e);
    //        }

  }

  @GET
  @Path("removeallgcmid")
  @Produces("text/plain")
  public String removeRegistration_id() {

    int user_id = isLogrdInRecApi(request, req);
    if (user_id < 0) {
      return pleaseLoginMesage();
    }


    //        try {
    //            boolean oked=DBFacade.getInstance().addDownlaodAll(mag_id);
    boolean oked = HiberDBFacad.getInstance().removeAllRegistrationIdFor(user_id);
    return oked ? "OK" : "NOK";
    //        } catch (SQLException e) {
    //            log.error("Exception in user_id: "+user_id, e);
    //        }

  }


  @GET
  @Path("downloadall")
  @Produces("text/plain")
  public String downloadAll(@DefaultValue("0") @QueryParam("mag_id") int mag_id) {

    int user_id = isLogrdInRecApi(request, req);
    if (user_id < 0) {
      return pleaseLoginMesage();
    }
    if (mag_id <= 0) {
      return "Please Fill correct mag_id";
    }

    //        try {
    //            boolean oked=DBFacade.getInstance().addDownlaodAll(mag_id);
    boolean oked = HiberDBFacad.getInstance().addDownlaodAll(mag_id);
    return oked ? "OK" : "NOK";
    //        } catch (SQLException e) {
    //            log.error("Exception in user_id: "+user_id, e);
    //        }

  }

  @GET
  @Path("addusersession")
  @Produces("text/plain")
  public String addUserSession(
      @DefaultValue("0") @QueryParam("mag_id") int mag_id,
      @DefaultValue("") @QueryParam("sessionstart") String sessionStart,
      @DefaultValue("0") @QueryParam("sessiondur") int sessionDuration,
      @DefaultValue("") @QueryParam("pgElaplarg") List<String> pels
                              ) {
    int user_id = isLogrdInRecApi(request, req);
    if (user_id < 0) {
      return pleaseLoginMesage();
    }
    sessionStart = sessionStart.trim();

    if ((mag_id <= 0) || (sessionStart.equals("")) || (sessionDuration <= 0) || (pels.size() <= 0)) {
      return "Please Fill mag_id , sessionstart , sessiondur , pgElaplarg Query Parameters.";
    }
    String encodeProbSessionStart = "";
    try {
      //            log.error("sessionstart: "+sessionStart);
      if (Integer.valueOf(sessionStart.substring(0, 3)) < 1970) {
        return "NOK: sessionstart parameter should be in format: " + Statistics.dateFullForm +
               "  Do you send jalali Date?!!!";
      }
      encodeProbSessionStart = Statistics.FullDateFormat.format(Statistics.FullDateFormat.parse(sessionStart));
      log.error("encodeProbSessionStart: " + encodeProbSessionStart);
      log.error("mag_id: " + mag_id);

    } catch (ParseException e) {
      log.error(
          "Exception in user_id: " + user_id + "\nsessionstart parameter should be in format: " +
          Statistics.dateFullForm, e);
      return "sessionstart parameter should be in format: " + Statistics.dateFullForm;
    } catch (Exception e) {
      log.error(
          "Exception in user_id: " + user_id + "\nsessionstart parameter should be in format: " +
          Statistics.dateFullForm, e);
      return "sessionstart parameter should be in format: " + Statistics.dateFullForm;
    }
    String oked = "";
    String noked = "";
    if (!HiberDBFacad.getInstance().isMagIdCorrect(mag_id)) {
      return "NOK: Magazine Id is not correct.";
    }

    for (String pel : pels) {
      try {
        String[] ppp = pel.split(",");
        int page = Integer.valueOf(ppp[0]);
        int elapsed = Integer.valueOf(ppp[1]);
        int clickToEnlargeCount = Integer.valueOf(ppp[2]);
        //                log.error("User id: "+user_id + " page: "+page+" elapsed: "+ elapsed+" click to enlarg: "+ clickToEnlargeCount);
        if (HiberDBFacad.getInstance()
                        .addUserSession(
                            user_id,
                            mag_id,
                            encodeProbSessionStart,
                            sessionDuration,
                            page,
                            elapsed,
                            clickToEnlargeCount)) {
          oked += " and " + pel;
        } else {
          noked += " and " + pel + " already Saved  - ";
          log.error(
              "Can not Save Session for user_id: " + "User id: " + user_id + " page: " + page + " elapsed: " + elapsed +
              " click to enlarg: " + clickToEnlargeCount);
        }


      } catch (Exception e) {
        log.error("Exception in user_id: " + user_id, e);

        noked += " and " + pel;
      }
    }
    return "OK: " + oked + "and NOK: " + noked;
  }



  @GET
  @Path("userbookmarked")
  @Produces("text/plain")
  public String userBookmarkAction(
      @DefaultValue("0") @QueryParam("mag_id") int mag_id,
      @DefaultValue("0") @QueryParam("page") int page,
      @DefaultValue("") @QueryParam("desc") String desc,
      @DefaultValue("") @QueryParam("bookmarked") String bookmarkedString


                                  ) {
    MobileuserTblHiberEntity user_id = isLogrdInRecApi(req, request);

    if (user_id == null) {
      return pleaseLoginMesage();
    }
    if (user_id.getChargeennd().compareTo(Statistics.FullDateFormat.format(new java.util.Date())) < 0) {
      return "NOK: Please Charge your Account";
    }

    if ((mag_id <= 0) || (page <= 0)) {
      return "Please Fill mag_id ,  page  Query Parameters.";
    }
    if ((bookmarkedString == null) || (bookmarkedString.equals("")) ||
        (!(bookmarkedString.equalsIgnoreCase("false") || bookmarkedString.equalsIgnoreCase("true")))) {
      return "Please bookmarked true Or false Parameters.";
    }

    boolean bookmarked = bookmarkedString.equalsIgnoreCase("true");

    String oked = "";
    try {
      oked = HiberDBFacad.getInstance().addUserBookmark(user_id.getUserId(), mag_id, page, desc, bookmarked);
    } catch (Exception e) {
      log.error("Exception in user_id: " + user_id, e);

    }
    return oked;
  }

  @GET
  @Path("updateuser")
  public String regUser(
      @DefaultValue("") @QueryParam("firstname") String fName,
      @DefaultValue("") @QueryParam("lastname") String lName,
      @DefaultValue("") @QueryParam("job") String job,
      @DefaultValue("") @QueryParam("gen") String gen,
      @DefaultValue("") @QueryParam("birth") String bDate,
      @DefaultValue("") @QueryParam("postcode") String postCode
                       ) {
    int user_id = isLogrdInRecApi(request, req);

    if (user_id < 0) {
      return pleaseLoginMesage();
    }


    bDate = bDate.trim();
    postCode = postCode.trim();

    if (postCode.equals("") || postCode.length() != 5) {
      return "NOK: Fill postcode parameter should be 5 number and try again";
    }

    if (fName.equals("") || lName.equals("") || job.equals("")) {
      return "NOK: Fill firstname , lastname and job  then try again";
    }

    if (job.equals("") || postCode.length() != 5) {
      return "NOK: Fill postcode parameter should be 5 number and try again";
    }

    if (gen.equals("") || (!(gen.equalsIgnoreCase("f") || gen.equalsIgnoreCase("m") || gen.equalsIgnoreCase("n")))) {
      return "NOK: Fill gen(gener) parameter with m of f or n then try again";
    }


    {
      if (!bDate.matches("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}")) {
        return "NOK: birth parameter should be in true format:yyyy-MM-dd";
      }

      try {
        Date.valueOf(bDate);
        //                fullDateFormat.parse(bDate);
      } catch (Exception e) {
        return "NOK: birth parameter is not trust Date.";
      }


    }
    MobileuserTblHiberEntity mobileuserTblHiberEntity = new MobileuserTblHiberEntity();
    mobileuserTblHiberEntity.setBdate(bDate);
    mobileuserTblHiberEntity.setPostcode(postCode);
    mobileuserTblHiberEntity.setFname(fName);
    mobileuserTblHiberEntity.setLname(lName);
    mobileuserTblHiberEntity.setJob(job);
    mobileuserTblHiberEntity.setGener(gen);

    mobileuserTblHiberEntity.setUserId(user_id);

    String ret;
    try {
      ret = HiberDBFacad.getInstance().updateMobileUserToUserTbl(mobileuserTblHiberEntity);
    } catch (Exception e) {
      log.error("Exception in user_id: " + user_id, e);
      return "NOK: Cann't Update User. Server Error.";
    }

    return ret;

  }

  @GET
  @Path("updateserpass")
  public String regUserEla(
      @DefaultValue("") @QueryParam("oldpass") String oldpass,
      @DefaultValue("") @QueryParam("newpass") String newpass

                          ) {
    int user_id = isLogrdInRecApi(request, req);

    if (user_id < 0) {
      return pleaseLoginMesage();
    }


    oldpass = oldpass.trim();
    newpass = newpass.trim();

    if (("".equals(oldpass)) || (oldpass.equals(""))) {
      return "NOK: Fill oldpass parameter  and try again";
    }

    if (("".equals(newpass)) || (newpass.equals(""))) {
      return "NOK: Fill newpass parameter  and try again";
    }


    UsersHiberEntity usersHiberEntity = new UsersHiberEntity();

    usersHiberEntity = new UsersHiberEntity();
    usersHiberEntity.setUserId(user_id);

    usersHiberEntity.setPassword(newpass);

    String ret;
    try {
      ret = HiberDBFacad.getInstance().updateMobileUserPass(usersHiberEntity, oldpass);

    } catch (Exception e) {
      log.error("Exception in user_id: " + user_id, e);
      return "NOK: Cann't Update User. Server Error.";
    }

    return ret;

  }


  //    @GET
  //    @Path("ownedissue")
  //    @Produces("text/plain")
  //    public String ownedMagazine(@DefaultValue("0") @QueryParam("mag_id")  int mag_id,
  //                                @DefaultValue("") @QueryParam("buydate") String buyDate,
  //                                @DefaultValue("-1.0") @QueryParam("Payment") double payed
  //    ){
  //        int user_id=isLogrdInRecApi(request,req);
  //
  //        if(user_id<0)
  //            return "Please Login by API "+uriInfo.getAbsolutePath().toString().substring(0,uriInfo.getAbsolutePath().toString().indexOf("users/"))+"userman/login?user= &pass=  then set header WWW-Authenticate value";
  //        buyDate=buyDate.trim();
  //        if((mag_id<=0)||(buyDate.equals(""))||(payed==-1.0))
  //            return "Please Fill mag_id ,  buydate ,   Query , Payment Parameters.";
  //
  //        try {
  //            Statistics.FullDateFormat.parse(buyDate);
  //        } catch (ParseException e) {
  //            log.error("Exception in: ",e);
  //            return "buydate parameter should be in format: "+Statistics.dateFullForm;
  //        }
  //
  //        return HiberDBFacad.getInstance().addUserOwnedIssue(user_id, mag_id,buyDate,payed);
  //
  //    }


  @GET
  @Path("getuserinfo")
  @Produces("application/json")
  public String getUserInfo() {
    int user_id = isLogrdInRecApi(request, req);

    if (user_id < 0) {
      return pleaseLoginMesage();
    }
    MobileUser mobileUserPOJO = null;
    try {
      mobileUserPOJO = HiberDBFacad.getInstance().getMobileUserInfoPojo(user_id);
    } catch (Exception e) {
      log.error("Exception in user_id: " + user_id, e);
    }
    return new Gson().toJson(mobileUserPOJO);

  }

  @GET
  @Path("validCharge")
  @Produces("text/plain")
  public String hasCharge() {
    MobileuserTblHiberEntity user_id = isLogrdInRecApi(req, request);

    if (user_id == null) {
      return pleaseLoginMesage();
    }
    if (user_id.getChargeennd().compareTo(Statistics.FullDateFormat.format(new java.util.Date())) < 0) {
      return "false";
    }
    return "true";
  }



}
