package ir.mjm.entities;

import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 * Created by pcd iran on 6/3/14.
 */
@ManagedBean
@SessionScoped
public class UserBean {
  static final Logger log = Logger.getLogger(UserBean.class);

  //  @EJB
  //private IPriceModelService priceModelService;
  //    private String password;
  //    private User current;
  public String validateUser() {
    //        HttpServletRequest request = FacesContext.getCurrentInstance().getExternalContext( ).getRequest( );
    log.debug("logined-------------------------------------------------" + username + pass);

    return "successs";
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPass() {
    return pass;
  }

  public void setPass(String pass) {
    this.pass = pass;
  }

  @ManagedProperty("HIII ")
  private String username;

  private String pass;


}
