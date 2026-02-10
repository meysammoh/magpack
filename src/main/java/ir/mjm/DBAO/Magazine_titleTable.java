package ir.mjm.DBAO;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Created by Serap on 6/9/14.
 */
@ManagedBean
@SessionScoped
public class Magazine_titleTable {
  private int title_id;

  public int getCat_id() {
    return cat_id;
  }

  public void setCat_id(int cat_id) {
    this.cat_id = cat_id;
  }

  private int cat_id;
  private String title;

  public int getTitle_id() {
    return title_id;
  }

  public void setTitle_id(int title_id) {
    this.title_id = title_id;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }


}
