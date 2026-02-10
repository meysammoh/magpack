package ir.mjm.DBAO.hiber;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by Serap on 7/21/14.
 */
public class IssuepagesessionHiberEntityPK implements Serializable {
  private int userId;

  @Column(name = "user_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  @Id
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  private int magId;

  @Column(name = "mag_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  @Id
  public int getMagId() {
    return magId;
  }

  public void setMagId(int magId) {
    this.magId = magId;
  }

  private String sessionstarttime;

  @Column(name = "sessionstarttime", nullable = false, insertable = true, updatable = true, length = 20, precision = 0)
  @Id
  public String getSessionstarttime() {
    return sessionstarttime;
  }

  public void setSessionstarttime(String sessionstarttime) {
    this.sessionstarttime = sessionstarttime;
  }

  private int page;

  @Column(name = "page", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  @Id
  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    IssuepagesessionHiberEntityPK that = (IssuepagesessionHiberEntityPK) o;

    if (magId != that.magId) {
      return false;
    }
    if (page != that.page) {
      return false;
    }
    if (userId != that.userId) {
      return false;
    }
    if (sessionstarttime != null ? !sessionstarttime.equals(that.sessionstarttime) : that.sessionstarttime != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + magId;
    result = 31 * result + (sessionstarttime != null ? sessionstarttime.hashCode() : 0);
    result = 31 * result + page;
    return result;
  }
}
