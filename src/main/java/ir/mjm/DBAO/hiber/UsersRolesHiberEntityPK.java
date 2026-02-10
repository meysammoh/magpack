package ir.mjm.DBAO.hiber;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by Serap on 7/21/14.
 */
public class UsersRolesHiberEntityPK implements Serializable {
  private String username;
  private String rolename;

  @Column(name = "username", nullable = false, insertable = true, updatable = true, length = 40, precision = 0)
  @Id
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Column(name = "rolename", nullable = false, insertable = true, updatable = true, length = 20, precision = 0)
  @Id
  public String getRolename() {
    return rolename;
  }

  public void setRolename(String rolename) {
    this.rolename = rolename;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UsersRolesHiberEntityPK that = (UsersRolesHiberEntityPK) o;

    if (rolename != null ? !rolename.equals(that.rolename) : that.rolename != null) {
      return false;
    }
    if (username != null ? !username.equals(that.username) : that.username != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (rolename != null ? rolename.hashCode() : 0);
    return result;
  }
}
