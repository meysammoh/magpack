package ir.mjm.DBAO.hiber;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Created by Serap on 7/21/14.
 */
@Entity
@Table(name = "users_roles", schema = "", catalog = "magazinedb")
@IdClass(UsersRolesHiberEntityPK.class)
public class UsersRolesHiberEntity {
  private int userId;
  private String username;
  private String rolename;

  @Basic
  @Column(name = "user_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Id
  @Column(name = "username", nullable = false, insertable = true, updatable = true, length = 40, precision = 0)
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Id
  @Column(name = "rolename", nullable = false, insertable = true, updatable = true, length = 20, precision = 0)
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

    UsersRolesHiberEntity that = (UsersRolesHiberEntity) o;

    if (userId != that.userId) {
      return false;
    }
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
    int result = userId;
    result = 31 * result + (username != null ? username.hashCode() : 0);
    result = 31 * result + (rolename != null ? rolename.hashCode() : 0);
    return result;
  }
}
