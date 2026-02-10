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
@Table(name = "pubcompany", schema = "", catalog = "magazinedb")
public class PubcompanyHiberEntity {
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

  @Id
  @Column(name = "id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "compname", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
  public String getCompname() {
    return compname;
  }

  public void setCompname(String compname) {
    this.compname = compname;
  }

  @Basic
  @Column(name = "corpform", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
  public String getCorpform() {
    return corpform;
  }

  public void setCorpform(String corpform) {
    this.corpform = corpform;
  }

  @Basic
  @Column(name = "streetname", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
  public String getStreetname() {
    return streetname;
  }

  public void setStreetname(String streetname) {
    this.streetname = streetname;
  }

  @Basic
  @Column(name = "streetnum", nullable = true, insertable = true, updatable = true, length = 20, precision = 0)
  public String getStreetnum() {
    return streetnum;
  }

  public void setStreetnum(String streetnum) {
    this.streetnum = streetnum;
  }

  @Basic
  @Column(name = "cityname", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
  public String getCityname() {
    return cityname;
  }

  public void setCityname(String cityname) {
    this.cityname = cityname;
  }

  @Basic
  @Column(name = "citypostcode", nullable = true, insertable = true, updatable = true, length = 20, precision = 0)
  public String getCitypostcode() {
    return citypostcode;
  }

  public void setCitypostcode(String citypostcode) {
    this.citypostcode = citypostcode;
  }

  @Basic
  @Column(name = "country", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Basic
  @Column(name = "taxnum", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
  public String getTaxnum() {
    return taxnum;
  }

  public void setTaxnum(String taxnum) {
    this.taxnum = taxnum;
  }

  @Basic
  @Column(name = "corpregnum", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
  public String getCorpregnum() {
    return corpregnum;
  }

  public void setCorpregnum(String corpregnum) {
    this.corpregnum = corpregnum;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PubcompanyHiberEntity that = (PubcompanyHiberEntity) o;

    if (id != that.id) {
      return false;
    }
    if (cityname != null ? !cityname.equals(that.cityname) : that.cityname != null) {
      return false;
    }
    if (citypostcode != null ? !citypostcode.equals(that.citypostcode) : that.citypostcode != null) {
      return false;
    }
    if (compname != null ? !compname.equals(that.compname) : that.compname != null) {
      return false;
    }
    if (corpform != null ? !corpform.equals(that.corpform) : that.corpform != null) {
      return false;
    }
    if (corpregnum != null ? !corpregnum.equals(that.corpregnum) : that.corpregnum != null) {
      return false;
    }
    if (country != null ? !country.equals(that.country) : that.country != null) {
      return false;
    }
    if (streetname != null ? !streetname.equals(that.streetname) : that.streetname != null) {
      return false;
    }
    if (streetnum != null ? !streetnum.equals(that.streetnum) : that.streetnum != null) {
      return false;
    }
    if (taxnum != null ? !taxnum.equals(that.taxnum) : that.taxnum != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (compname != null ? compname.hashCode() : 0);
    result = 31 * result + (corpform != null ? corpform.hashCode() : 0);
    result = 31 * result + (streetname != null ? streetname.hashCode() : 0);
    result = 31 * result + (streetnum != null ? streetnum.hashCode() : 0);
    result = 31 * result + (cityname != null ? cityname.hashCode() : 0);
    result = 31 * result + (citypostcode != null ? citypostcode.hashCode() : 0);
    result = 31 * result + (country != null ? country.hashCode() : 0);
    result = 31 * result + (taxnum != null ? taxnum.hashCode() : 0);
    result = 31 * result + (corpregnum != null ? corpregnum.hashCode() : 0);
    return result;
  }
}
