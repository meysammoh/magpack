package ir.mjm.DBAO.hiber;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by Serap on 7/21/14.
 */
public class UsersHiberEntityPK implements Serializable {
  private int userId;
  private String username;

  @Column(name = "user_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  @Id
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Column(name = "username", nullable = false, insertable = true, updatable = true, length = 255, precision = 0)
  @Id
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UsersHiberEntityPK that = (UsersHiberEntityPK) o;

    if (userId != that.userId) {
      return false;
    }
    if (username != null ? !username.equals(that.username) : that.username != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + (username != null ? username.hashCode() : 0);
    return result;
  }
}
