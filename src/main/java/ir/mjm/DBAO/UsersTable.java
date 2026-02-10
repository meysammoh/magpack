package ir.mjm.DBAO;


import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Created by Serap on 6/9/14.
 */
@ManagedBean
@SessionScoped
public class UsersTable implements Serializable {
  private int user_id;
  private String username;
  private String password;
  private boolean isPublisher;


  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public String getUsername() {
    //        if(username==null||username=="")
    //            username=((LoginBean)((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().getAttribute("LoginBean")).getUsername();
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

  public boolean isPublisher() {
    return isPublisher;
  }

  public void setPublisher(boolean isPublisher) {
    this.isPublisher = isPublisher;
  }


}
