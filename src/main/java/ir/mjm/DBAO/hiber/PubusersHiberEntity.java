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
@Table(name = "pubusers", schema = "", catalog = "magazinedb")
public class PubusersHiberEntity {
  private int id;
  private String firstname;
  private String lastname;
  private String email;
  private String phonecode;
  private String phonenum;
  private String streetname;
  private String streetnum;
  private String cityname;
  private String citynum;
  private String country;

  @Id
  @Column(name = "id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "firstname", nullable = true, insertable = true, updatable = true, length = 30, precision = 0)
  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  @Basic
  @Column(name = "lastname", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  @Basic
  @Column(name = "email", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Basic
  @Column(name = "phonecode", nullable = true, insertable = true, updatable = true, length = 15, precision = 0)
  public String getPhonecode() {
    return phonecode;
  }

  public void setPhonecode(String phonecode) {
    this.phonecode = phonecode;
  }

  @Basic
  @Column(name = "phonenum", nullable = true, insertable = true, updatable = true, length = 15, precision = 0)
  public String getPhonenum() {
    return phonenum;
  }

  public void setPhonenum(String phonenum) {
    this.phonenum = phonenum;
  }

  @Basic
  @Column(name = "streetname", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
  public String getStreetname() {
    return streetname;
  }

  public void setStreetname(String streetname) {
    this.streetname = streetname;
  }

  @Basic
  @Column(name = "streetnum", nullable = true, insertable = true, updatable = true, length = 15, precision = 0)
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
  @Column(name = "citynum", nullable = true, insertable = true, updatable = true, length = 15, precision = 0)
  public String getCitynum() {
    return citynum;
  }

  public void setCitynum(String citynum) {
    this.citynum = citynum;
  }

  @Basic
  @Column(name = "country", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PubusersHiberEntity that = (PubusersHiberEntity) o;

    if (id != that.id) {
      return false;
    }
    if (cityname != null ? !cityname.equals(that.cityname) : that.cityname != null) {
      return false;
    }
    if (citynum != null ? !citynum.equals(that.citynum) : that.citynum != null) {
      return false;
    }
    if (country != null ? !country.equals(that.country) : that.country != null) {
      return false;
    }
    if (email != null ? !email.equals(that.email) : that.email != null) {
      return false;
    }
    if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) {
      return false;
    }
    if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) {
      return false;
    }
    if (phonecode != null ? !phonecode.equals(that.phonecode) : that.phonecode != null) {
      return false;
    }
    if (phonenum != null ? !phonenum.equals(that.phonenum) : that.phonenum != null) {
      return false;
    }
    if (streetname != null ? !streetname.equals(that.streetname) : that.streetname != null) {
      return false;
    }
    if (streetnum != null ? !streetnum.equals(that.streetnum) : that.streetnum != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
    result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (phonecode != null ? phonecode.hashCode() : 0);
    result = 31 * result + (phonenum != null ? phonenum.hashCode() : 0);
    result = 31 * result + (streetname != null ? streetname.hashCode() : 0);
    result = 31 * result + (streetnum != null ? streetnum.hashCode() : 0);
    result = 31 * result + (cityname != null ? cityname.hashCode() : 0);
    result = 31 * result + (citynum != null ? citynum.hashCode() : 0);
    result = 31 * result + (country != null ? country.hashCode() : 0);
    return result;
  }
}
