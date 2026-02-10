package ir.mjm.DBAO.hiber;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Serap on 7/21/14.
 */
@Entity
@Table(name = "pubbillt", schema = "", catalog = "magazinedb")
public class PubbilltHiberEntity {
  private int id;
  private String bankaccnum;
  private String bankcode;
  private String bankname;
  private String recipname;

  @Id
  @Column(name = "id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "bankaccnum", nullable = true, insertable = true, updatable = true, length = 20, precision = 0)
  public String getBankaccnum() {
    return bankaccnum;
  }

  public void setBankaccnum(String bankaccnum) {
    this.bankaccnum = bankaccnum;
  }

  @Basic
  @Column(name = "bankcode", nullable = true, insertable = true, updatable = true, length = 15, precision = 0)
  public String getBankcode() {
    return bankcode;
  }

  public void setBankcode(String bankcode) {
    this.bankcode = bankcode;
  }

  @Basic
  @Column(name = "bankname", nullable = true, insertable = true, updatable = true, length = 40, precision = 0)
  public String getBankname() {
    return bankname;
  }

  public void setBankname(String bankname) {
    this.bankname = bankname;
  }

  @Basic
  @Column(name = "recipname", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
  public String getRecipname() {
    return recipname;
  }

  public void setRecipname(String recipname) {
    this.recipname = recipname;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PubbilltHiberEntity that = (PubbilltHiberEntity) o;

    if (id != that.id) {
      return false;
    }
    if (bankaccnum != null ? !bankaccnum.equals(that.bankaccnum) : that.bankaccnum != null) {
      return false;
    }
    if (bankcode != null ? !bankcode.equals(that.bankcode) : that.bankcode != null) {
      return false;
    }
    if (bankname != null ? !bankname.equals(that.bankname) : that.bankname != null) {
      return false;
    }
    if (recipname != null ? !recipname.equals(that.recipname) : that.recipname != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (bankaccnum != null ? bankaccnum.hashCode() : 0);
    result = 31 * result + (bankcode != null ? bankcode.hashCode() : 0);
    result = 31 * result + (bankname != null ? bankname.hashCode() : 0);
    result = 31 * result + (recipname != null ? recipname.hashCode() : 0);
    return result;
  }
}
