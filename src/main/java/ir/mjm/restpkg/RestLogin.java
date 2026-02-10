package ir.mjm.restpkg;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.Statistics;
import ir.mjm.DBAO.hiber.MobileuserTblHiberEntity;
import ir.mjm.DBAO.hiber.UsersHiberEntity;
import org.apache.log4j.Logger;

import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 * Created by Serap on 7/2/14.
 */
@Path("userman")
public class RestLogin {
  static final Logger log = Logger.getLogger(RestLogin.class);
  private final double MOBILE_USER_DEFAULT_CHARGE_VALUE;
  private final int MOBILE_USER_DEFAULT_CHARGE_DAYS;

  @Context
  HttpServletRequest req;
  java.text.SimpleDateFormat fullDateFormat =
      new java.text.SimpleDateFormat("yyyy-MM-dd");

  public RestLogin() {
    MOBILE_USER_DEFAULT_CHARGE_VALUE = 14.99;
    MOBILE_USER_DEFAULT_CHARGE_DAYS = 31;
  }

  @GET
  @Path("login")
  public String loginMe(
      @DefaultValue("") @QueryParam("user") String username,
      @DefaultValue("") @QueryParam("pass") String pass
                       ) {
    UsersHiberEntity usersTable = null;
    username = username.trim();
    pass = pass.trim();

    usersTable = HiberDBFacad.getInstance().loginMobileUser(username, pass);

    if (usersTable == null) {
      return "NOK";
    }

    if (req != null && req.getSession() != null) {
      req.getSession().setAttribute("taninip", "ok");
    }

    return "OK:ID=" + usersTable.getUserId();

  }

  @GET
  @Path("register")
  public String regUserDaa(
      @DefaultValue("") @QueryParam("user") String userName,
      @DefaultValue("") @QueryParam("pass") String pass
                          ) {
    userName = userName.trim();

    if (userName.equals("") || pass.equals("")) {
      return "NOK: Fill user and/or pass parameters";
    }
    if (!userName.matches(Statistics.getEmailPattern())) {
      return "NOK: Invalid Email pattern in username.";
    }

    MobileuserTblHiberEntity mobileuserTblHiberEntity = new MobileuserTblHiberEntity();

    mobileuserTblHiberEntity.setCharge(MOBILE_USER_DEFAULT_CHARGE_VALUE);
    mobileuserTblHiberEntity.setTotalpremiummonth(1);


    Calendar cal = Calendar.getInstance();
    java.util.Date nowDate = cal.getTime();
    cal.add(Calendar.DATE, MOBILE_USER_DEFAULT_CHARGE_DAYS);
    java.util.Date oneMonthLate = cal.getTime();

    mobileuserTblHiberEntity.setRegdate(Statistics.FullDateFormat.format(nowDate));

    mobileuserTblHiberEntity.setChargestart(Statistics.FullDateFormat.format(nowDate));
    mobileuserTblHiberEntity.setChargeennd(Statistics.FullDateFormat.format(oneMonthLate));

    UsersHiberEntity usersHiberEntity = new UsersHiberEntity();
    usersHiberEntity.setUsername(userName);
    usersHiberEntity.setPassword(pass);

    int newuserId;
    try {
      newuserId = HiberDBFacad.getInstance().addMobileUserToUserTbl(usersHiberEntity, mobileuserTblHiberEntity);
      if (newuserId > 0) {
        return "OK: ID=" + newuserId;
      } else if (newuserId == -2) {
        return "NOK: Username " + userName + " reserved. Please change it.";
      } else {
        return "NOK: Cann't Add User. Server Error.";
      }
    } catch (Exception e) {
      log.error(e);
      return "NOK: Cann't Add User. Server Error.";
    }


  }


}
