package ir.mjm.DBAO.hiber;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Serap on 8/15/14.
 */
@Entity
@Table(name = "mobileuser_tbl", schema = "", catalog = "magazinedb")
public class MobileuserTblHiberEntity {
  private int userId;
  private String bdate;
  private String postcode;
  private String fname;
  private String lname;
  private String job;
  private String gener;
  private Double charge;
  private String chargestart;
  private String chargeennd;
  private int totalpremiummonth;
  private String regdate;

  @Id
  @Column(name = "user_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Basic
  @Column(name = "bdate", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
  public String getBdate() {
    return bdate;
  }

  public void setBdate(String bdate) {
    this.bdate = bdate;
  }

  @Basic
  @Column(name = "postcode", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  @Basic
  @Column(name = "fname", nullable = true, insertable = true, updatable = true, length = 30, precision = 0)
  public String getFname() {
    return fname;
  }

  public void setFname(String fname) {
    this.fname = fname;
  }

  @Basic
  @Column(name = "lname", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
  public String getLname() {
    return lname;
  }

  public void setLname(String lname) {
    this.lname = lname;
  }

  @Basic
  @Column(name = "job", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
  public String getJob() {
    return job;
  }

  public void setJob(String job) {
    this.job = job;
  }

  @Basic
  @Column(name = "gener", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
  public String getGener() {
    return gener;
  }

  public void setGener(String gener) {
    this.gener = gener;
  }

  @Basic
  @Column(name = "charge", nullable = true, insertable = true, updatable = true, length = 22, precision = 0)
  public Double getCharge() {
    return charge;
  }

  public void setCharge(Double charge) {
    this.charge = charge;
  }

  @Basic
  @Column(name = "chargestart", nullable = true, insertable = true, updatable = true, length = 20, precision = 0)
  public String getChargestart() {
    return chargestart;
  }

  public void setChargestart(String chargestart) {
    this.chargestart = chargestart;
  }

  @Basic
  @Column(name = "chargeennd", nullable = true, insertable = true, updatable = true, length = 20, precision = 0)
  public String getChargeennd() {
    return chargeennd;
  }

  public void setChargeennd(String chargeennd) {
    this.chargeennd = chargeennd;
  }

  @Basic
  @Column(name = "totalpremiummonth", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getTotalpremiummonth() {
    return totalpremiummonth;
  }

  public void setTotalpremiummonth(int totalpremiummonth) {
    this.totalpremiummonth = totalpremiummonth;
  }

  @Basic
  @Column(name = "regdate", nullable = false, insertable = true, updatable = true, length = 20, precision = 0)
  public String getRegdate() {
    return regdate;
  }

  public void setRegdate(String regdate) {
    this.regdate = regdate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MobileuserTblHiberEntity that = (MobileuserTblHiberEntity) o;

    if (totalpremiummonth != that.totalpremiummonth) {
      return false;
    }
    if (userId != that.userId) {
      return false;
    }
    if (bdate != null ? !bdate.equals(that.bdate) : that.bdate != null) {
      return false;
    }
    if (charge != null ? !charge.equals(that.charge) : that.charge != null) {
      return false;
    }
    if (chargeennd != null ? !chargeennd.equals(that.chargeennd) : that.chargeennd != null) {
      return false;
    }
    if (chargestart != null ? !chargestart.equals(that.chargestart) : that.chargestart != null) {
      return false;
    }
    if (fname != null ? !fname.equals(that.fname) : that.fname != null) {
      return false;
    }
    if (gener != null ? !gener.equals(that.gener) : that.gener != null) {
      return false;
    }
    if (job != null ? !job.equals(that.job) : that.job != null) {
      return false;
    }
    if (lname != null ? !lname.equals(that.lname) : that.lname != null) {
      return false;
    }
    if (postcode != null ? !postcode.equals(that.postcode) : that.postcode != null) {
      return false;
    }
    if (regdate != null ? !regdate.equals(that.regdate) : that.regdate != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + (bdate != null ? bdate.hashCode() : 0);
    result = 31 * result + (postcode != null ? postcode.hashCode() : 0);
    result = 31 * result + (fname != null ? fname.hashCode() : 0);
    result = 31 * result + (lname != null ? lname.hashCode() : 0);
    result = 31 * result + (job != null ? job.hashCode() : 0);
    result = 31 * result + (gener != null ? gener.hashCode() : 0);
    result = 31 * result + (charge != null ? charge.hashCode() : 0);
    result = 31 * result + (chargestart != null ? chargestart.hashCode() : 0);
    result = 31 * result + (chargeennd != null ? chargeennd.hashCode() : 0);
    result = 31 * result + totalpremiummonth;
    result = 31 * result + (regdate != null ? regdate.hashCode() : 0);
    return result;
  }
}
