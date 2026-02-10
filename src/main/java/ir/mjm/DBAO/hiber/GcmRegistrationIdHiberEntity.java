package ir.mjm.DBAO.hiber;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Created by Serap on 12/1/2014.
 */
@Entity
@Table(name = "gcm_registration_id", schema = "", catalog = "magazinedb")
@IdClass(GcmRegistrationIdHiberEntityPK.class)
public class GcmRegistrationIdHiberEntity {
  private int id;
  private int userId;
  private String registrationId;

  @Id
  @Column(name = "id", nullable = false, insertable = true, updatable = true)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Id
  @Column(name = "user_id", nullable = false, insertable = true, updatable = true)
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Basic
  @Column(name = "Registration_id", nullable = false, insertable = true, updatable = true, length = 65535)
  public String getRegistrationId() {
    return registrationId;
  }

  public void setRegistrationId(String registrationId) {
    this.registrationId = registrationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GcmRegistrationIdHiberEntity that = (GcmRegistrationIdHiberEntity) o;

    if (id != that.id) {
      return false;
    }
    if (userId != that.userId) {
      return false;
    }
    if (registrationId != null ? !registrationId.equals(that.registrationId) : that.registrationId != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + userId;
    result = 31 * result + (registrationId != null ? registrationId.hashCode() : 0);
    return result;
  }
}
