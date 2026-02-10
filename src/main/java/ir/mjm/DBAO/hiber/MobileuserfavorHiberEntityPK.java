package ir.mjm.DBAO.hiber;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by Serap on 7/26/14.
 */
public class MobileuserfavorHiberEntityPK implements Serializable {
  private int userId;
  private int magId;

  @Column(name = "user_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  @Id
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Column(name = "mag_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  @Id
  public int getMagId() {
    return magId;
  }

  public void setMagId(int magId) {
    this.magId = magId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MobileuserfavorHiberEntityPK that = (MobileuserfavorHiberEntityPK) o;

    if (magId != that.magId) {
      return false;
    }
    if (userId != that.userId) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + magId;
    return result;
  }
}
