package ir.mjm.entities;

import ir.mjm.DBAO.DBFacade;
import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.PubBillDet;
import ir.mjm.DBAO.PubCompantDet;
import ir.mjm.DBAO.PubUserDet;
import ir.mjm.DBAO.UsersTable;
import ir.mjm.admin.AdminControler;
import ir.mjm.util.CookieManager;
import ir.mjm.util.FaceUtil;
import ir.mjm.util.LocaleBean;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Serap on 6/4/14.
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
  static final Logger log = Logger.getLogger(LoginBean.class);

  public LoginBean() {
    //        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", ""));
    DBFacade.getInstance();


  }

  // Simple user database :)

  private String username;
  private String password;

  public String getNewpass() {
    return newpass;
  }

  public void setNewpass(String newpass) {
    this.newpass = newpass;
  }

  private String newpass;
  private String newPubNaame;
  private String newPubEmail;
  private UsersTable usersTable;
  private int user_id = -1;
  private boolean loggedIn;
  private String publisherName = null;
  private PubUserDet pubUserDet;
  private boolean rememberMe;


  public PubUserDet getPubUserDet() {
    return pubUserDet;
  }

  public void setPubUserDet(PubUserDet pubUserDet) {
    this.pubUserDet = pubUserDet;
  }


  public String getNewPubNaame() {
    return newPubNaame;
  }

  public void setNewPubNaame(String newPubNaame) {
    this.newPubNaame = newPubNaame;
  }

  public String getNewPubEmail() {
    return newPubEmail;
  }

  public void setNewPubEmail(String newPubEmail) {
    this.newPubEmail = newPubEmail;
  }


  public void setPublisherName(String publisherName) {
    this.publisherName = publisherName;
  }


  public int getUser_id() {

    //        try {
    if (user_id == -1) {
      setUser_id(HiberDBFacad.getInstance().getUserByName_onlyUserID(getUsername()).getUser_id());
    }
    //                setUser_id(DBFacade.getInstance().getUserByName_onlyUserID(getUsername()).getUser_id());
    //        } catch (SQLException e) {
    //            log.error("Exception in: ",e);
    //        }
    log.debug("User id: " + user_id);
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public boolean isPublisher() {
    return isPublisher;
  }

  public void setPublisher(boolean isPublisher) {
    this.isPublisher = isPublisher;
  }

  boolean isPublisher;

  public UsersTable getUserbean() {
    return usersTable;
  }

  public void setUserbean(UsersTable userbean) {
    this.usersTable = userbean;
  }


  /**
   * Login operation.
   *
   * @return
   */
  public String doLogin() {
    // Get every user from our sample database :)
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    try {
      try {
        ((HttpServletRequest) context.getRequest()).login(username, password);
        if (rememberMe) {
          userWantRemeberMe(context, username);
        } else {
          notRememberUser();
        }
        password = "";

      } catch (ServletException e) {
        log.error(username + ":" + password + " and Exception in: " + e.getMessage());

        //                context.redirect("error.xhtml");
        FacesContext.getCurrentInstance()
                    .addMessage(
                        "message",
                        new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            LocaleBean.localMessageOf("invalid.username.or.password"),
                            ""));
        //                log.debug("Redirect to: "+FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath()+"/web/login.xhtml");
        //                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath()+"/web/login.xhtml");

      } catch (Exception e) {
        log.error("Exception in login process", e);
      }

      if (((HttpServletRequest) context.getRequest()).isUserInRole("publisherrole")) {
        loggedIn = true;
        //                log.error("user direct to: " + "/pubpanel/fileUpload.xhtml");
        log.error(
            "((HttpServletRequest)context.getRequest()).getUserPrincipal():" +
            ((HttpServletRequest) context.getRequest()).getUserPrincipal());
        log.error("user name: " + username);
        //                try {
        usersTable = HiberDBFacad.getInstance().getUsersByUsername(username);

        PubBillDet pubBillDet;
        PubCompantDet pubCompantDet;
        PubUserDet pubUserDet;
        //                    try {
        //                        pubBillDet=  DBFacade.getInstance().getPubBillDet(usersTable.getUser_id());
        pubBillDet = HiberDBFacad.getInstance().getPubBillDet(usersTable.getUser_id());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pubBillDet", pubBillDet);

        //                        pubCompantDet=DBFacade.getInstance().getPubCompanyDetailes(usersTable.getUser_id());
        pubCompantDet = HiberDBFacad.getInstance().getPubCompanyDetailes(usersTable.getUser_id());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pubCompantDet", pubCompantDet);

        //                        pubUserDet=DBFacade.getInstance().getPubUserDet(usersTable.getUser_id());
        pubUserDet = HiberDBFacad.getInstance().getPubUserDet(usersTable.getUser_id());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pubUserDet", pubUserDet);


        //                    } catch (SQLException e) {
        //                        log.log(Priority.FATAL,"Failed!!!:",e);
        //                    }
        log.error("user_id:" + usersTable.getUser_id());
        setPublisher(true);

        //                } catch (SQLException e) {
        //                    log.error("Exception in: ",e);
        //                }

        context.redirect(
            FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
            "/pubpanel/PubDetails.xhtml");
        //                return "/pubpanel/pubpanel.xhtml";


      } else if (((HttpServletRequest) context.getRequest()).isUserInRole("adminrole")) {
        loggedIn = true;
        //                log.error("user direct to: " + "/adminpanel/Adminpublishers.xhtml");
        FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("adminControler", new AdminControler());


        context.redirect(
            FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
            "/adminpanel/Adminpublishers.xhtml");
        //                try {
        usersTable = HiberDBFacad.getInstance().getUsersByUsername(username);
        //                } catch (SQLException e) {
        //                    log.error("Exception in: ",e);
        //                }

      } else if (((HttpServletRequest) context.getRequest()).isUserInRole("userrole")) {
        loggedIn = true;

        log.error("user direct to: " + "/userpages/home.xhtml");

        context.redirect(
            FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
            "/userpages/home.xhtml");
      }
    } catch (IOException e) {
      log.error("Exception in: ", e);

    }
    if (loggedIn) {
      return "";
    }
    return "/web/login.xhtml";
  }

  public void notRememberUser() {
    if (HiberDBFacad.getInstance().updateUserUUID(null, username)) {
      CookieManager.removeCookie((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse(), CookieManager.COOKIE_NAME);
    }
  }

  public static void userWantRemeberMe(final ExternalContext context, final String username) {
    String uuid = UUID.randomUUID().toString();
    if (HiberDBFacad.getInstance().updateUserUUID(uuid, username)) {
      CookieManager.addCookie(
          (HttpServletResponse) context.getResponse(),
          CookieManager.COOKIE_NAME,
          uuid,
          CookieManager.COOKIE_AGE);
    }
  }

  // Set login ERROR

  /**
   * Logout operation.
   *
   * @return
   */

  public String doLogout() {
    // Set the paremeter indicating that user is logged in to false
    try {
      ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).logout();
      notRememberUser();
      ExternalContext tmpEC;
      Map sMap;
      tmpEC = FacesContext.getCurrentInstance().getExternalContext();
      sMap = tmpEC.getSessionMap();
      LocaleBean localeBean = (LocaleBean) sMap.get("localeBean");
      ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession()
                                                                                                .invalidate();
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("localeBean", localeBean);
      loggedIn = false;
      FacesMessage msg = new FacesMessage(LocaleBean.localMessageOf("logout.success"), "INFO MSG");
      msg.setSeverity(FacesMessage.SEVERITY_INFO);
      FacesContext.getCurrentInstance().addMessage(null, msg);
    } catch (ServletException e) {
      log.error("Exception in: ", e);
    }
    // Set logout message

    try {
      FacesContext.getCurrentInstance()
                  .getExternalContext()
                  .redirect(
                      FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
                      "/web/login.xhtml");
    } catch (IOException e) {
      log.error("Exception in: ", e);

    }
    return "/web/login.xhtml";
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isLoggedIn() {
    return loggedIn;
  }

  public void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

  public boolean updatePass() {
    if (newpass == null || newpass == "") {
      FacesMessage msg = new FacesMessage("New password can not be empty string.");
      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
      FacesContext.getCurrentInstance().addMessage(null, msg);
      return true;
    }
    try {
      FacesMessage msg =
          new FacesMessage(HiberDBFacad.getInstance().updateUserPass(getUsername(), getPassword(), newpass));
      msg.setSeverity(FacesMessage.SEVERITY_INFO);
      FacesContext.getCurrentInstance().addMessage(null, msg);
      return true;
    } catch (SQLException e) {
      log.error("Exception in: ", e);
    }
    FacesMessage msg = new FacesMessage("Failed To change password.");
    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
    FacesContext.getCurrentInstance().addMessage(null, msg);
    return false;
  }

  public void addPublisher() {
    FacesMessage msg = null;
    FacesMessage.Severity severity = null;

    //            DBFacade.getInstance().addPublisherUser(newPubNaame,newPubEmail,newPubNaame+"123");
    String res = HiberDBFacad.getInstance().addPublisherUser(newPubNaame, newPubEmail, newPubNaame + "123");
    if (res.startsWith("A")) {
      severity = FacesMessage.SEVERITY_INFO;
      ((AdminControler) FaceUtil.findBean("adminControler")).fetchPubsInfs();
    } else {
      severity = FacesMessage.SEVERITY_ERROR;
    }

    msg = new FacesMessage(res);
    msg.setSeverity(severity);

    log.error("Login Publisher Adding: " + res);


    FacesContext.getCurrentInstance().addMessage(null, msg);
    this.newPubNaame = null;
    this.newPubEmail = null;
  }

  public void navigateIfLogin() {
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

    if (((HttpServletRequest) context.getRequest()).isUserInRole("publisherrole")) {
      loggedIn = true;
      //            log.error("Logined user redirect  " + ((HttpServletRequest) context.getRequest()).getUserPrincipal());
      log.error("user name: " + username);
      //                try {
      usersTable = HiberDBFacad.getInstance().getUsersByUsername(username);

      log.error("user_id:" + usersTable.getUser_id());
      setPublisher(true);


      try {
        context.redirect(
            FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
            "/pubpanel/PubDetails.xhtml");
      } catch (IOException e) {
        log.error(e);
      }


    } else if (((HttpServletRequest) context.getRequest()).isUserInRole("adminrole")) {
      loggedIn = true;
      log.error("logined admin redirect : " + "/adminpanel/Adminpublishers.xhtml");


      try {
        context.redirect(
            FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
            "/adminpanel/Adminpublishers.xhtml");
      } catch (IOException e) {
        log.error(e);
      }


    }
  }

  public void navigateLogin() {
    try {
      FacesContext.getCurrentInstance()
                  .getExternalContext()
                  .redirect(
                      FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() +
                      "/web/login.xhtml");
    } catch (IOException e) {
      log.error(e);
    }

  }

  public boolean isRememberMe() {
    return rememberMe;
  }

  public void setRememberMe(final boolean rememberMe) {
    this.rememberMe = rememberMe;
  }
}
