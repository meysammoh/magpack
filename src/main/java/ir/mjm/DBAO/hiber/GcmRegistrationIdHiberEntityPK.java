package ir.mjm.DBAO.hiber;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by Serap on 12/1/2014.
 */
public class GcmRegistrationIdHiberEntityPK implements Serializable {
  private int id;
  private int userId;

  @Column(name = "id", nullable = false, insertable = true, updatable = true)
  @Id
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Column(name = "user_id", nullable = false, insertable = true, updatable = true)
  @Id
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GcmRegistrationIdHiberEntityPK that = (GcmRegistrationIdHiberEntityPK) o;

    if (id != that.id) {
      return false;
    }
    if (userId != that.userId) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + userId;
    return result;
  }
}
