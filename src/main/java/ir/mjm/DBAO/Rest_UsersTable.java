package ir.mjm.DBAO;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Created by Serap on 6/9/14.
 */
@ManagedBean
@SessionScoped
public class Rest_UsersTable {
  private String user_id;
  private String magazine_id;

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public String getMagazine_id() {
    return magazine_id;
  }

  public void setMagazine_id(String magazine_id) {
    this.magazine_id = magazine_id;
  }
}
