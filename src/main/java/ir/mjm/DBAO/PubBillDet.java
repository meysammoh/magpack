package ir.mjm.DBAO;

import ir.mjm.entities.LoginBean;
import ir.mjm.util.FaceUtil;
import ir.mjm.util.LocaleBean;
import org.apache.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * Created by Serap on 6/19/14.
 */

@ManagedBean
@ViewScoped
public class PubBillDet {
  static final Logger log = Logger.getLogger(PubBillDet.class);

  private int id;
  private String bankaccnum;
  private String bankcode;
  private String bankname;
  private String recipname;


  public String getRecipname() {
    return recipname;
  }

  public void setRecipname(String recipname) {
    this.recipname = recipname;
  }

  public int getId() {
    if (id < 0) {
      setId(((LoginBean) FaceUtil.findBean("loginBean")).getUser_id());
    }
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getBankaccnum() {
    return bankaccnum;
  }

  public void setBankaccnum(String bankaccnum) {
    this.bankaccnum = bankaccnum;
  }

  public String getBankcode() {
    return bankcode;
  }

  public void setBankcode(String bankcode) {
    this.bankcode = bankcode;
  }

  public String getBankname() {
    return bankname;
  }

  public void setBankname(String bankname) {
    this.bankname = bankname;
  }

  public void addOrUpdate() {
    FacesMessage msg = null;

    //        try {
    //                DBFacade.getInstance().upadtePubBillDet(this);
    int change = HiberDBFacad.getInstance().upadtePubBillDet(this);
    if (change > 0) {
      msg = new FacesMessage(LocaleBean.localMessageOf("update.success"), "INFO MSG");

      //            preparePub();
      //        }catch (SQLException e) {
    } else {
      msg = new FacesMessage(LocaleBean.localMessageOf("update.error"), "INFO MSG");
      log.error("Exception in: Hiber bill update");
      //        }
    }
    msg.setSeverity(FacesMessage.SEVERITY_INFO);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }
}
