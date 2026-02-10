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
public class PubCompantDet {
  static final Logger log = Logger.getLogger(PubCompantDet.class);

  private int id;
  private String compname;
  private String corpform;
  private String streetname;
  private String streetnum;
  private String cityname;
  private String citypostcode;
  private String country;
  private String taxnum;
  private String corpregnum;


  public String getCompname() {
    return compname;
  }

  public void setCompname(String compname) {
    this.compname = compname;
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


  public String getCorpform() {
    return corpform;
  }

  public void setCorpform(String corpform) {
    this.corpform = corpform;
  }

  public String getStreetname() {
    return streetname;
  }

  public void setStreetname(String streetname) {
    this.streetname = streetname;
  }

  public String getStreetnum() {
    return streetnum;
  }

  public void setStreetnum(String streetnum) {
    this.streetnum = streetnum;
  }

  public String getCityname() {
    return cityname;
  }

  public void setCityname(String cityname) {
    this.cityname = cityname;
  }

  public String getCitypostcode() {
    return citypostcode;
  }

  public void setCitypostcode(String citypostcode) {
    this.citypostcode = citypostcode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getTaxnum() {
    return taxnum;
  }

  public void setTaxnum(String taxnum) {
    this.taxnum = taxnum;
  }

  public String getCorpregnum() {
    return corpregnum;
  }

  public void setCorpregnum(String corpregnum) {
    this.corpregnum = corpregnum;
  }

  public void addOrUpdate() {
    FacesMessage msg = null;

    //        try {
    //            DBFacade.getInstance().upadtePubCompanyDetailes(this);
    int change = HiberDBFacad.getInstance().upadtePubCompanyDetailes(this);
    if (change > 0) {
      msg = new FacesMessage(LocaleBean.localMessageOf("update.success"), "INFO MSG");

      //            preparePub();
      //        }catch (SQLException e) {
    } else {
      msg = new FacesMessage(LocaleBean.localMessageOf("update.error"), "INFO MSG");

      log.error("Exception in update pub company details. ");
    }
    msg.setSeverity(FacesMessage.SEVERITY_INFO);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  public boolean dataIsCompleted() {
    if ((getCompname() == null || equals("") ||
         corpform == null || "".equals(corpform)) ||
        streetname == null || "".equals(streetname) ||
        streetnum == null || "".equals(streetnum) ||
        cityname == null || "".equals(cityname) ||
        country == null || "".equals(country)) {
      return false;
    }
    return true;
  }
}
