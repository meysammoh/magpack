package ir.mjm.DBAO.hiber;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Created by Serap on 7/26/14.
 */
@Entity
@Table(name = "mobileuserfavor", schema = "", catalog = "magazinedb")
@IdClass(MobileuserfavorHiberEntityPK.class)
public class MobileuserfavorHiberEntity {
  private int userId;
  private int magId;
  private boolean favorite;

  @Id
  @Column(name = "user_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Id
  @Column(name = "mag_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getMagId() {
    return magId;
  }

  public void setMagId(int magId) {
    this.magId = magId;
  }

  @Basic
  @Column(name = "favorite", columnDefinition = "TINYINT(1)", nullable = false, insertable = true, updatable = true, length = 3, precision = 0)
  public boolean getFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MobileuserfavorHiberEntity that = (MobileuserfavorHiberEntity) o;

    if (favorite != that.favorite) {
      return false;
    }
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
    result = 31 * result + (favorite ? 1 : 0);
    return result;
  }
}
