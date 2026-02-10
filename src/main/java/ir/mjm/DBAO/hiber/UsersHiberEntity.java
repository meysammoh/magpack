package ir.mjm.DBAO.hiber;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Created by Serap on 7/21/14.
 */
@Entity
@Table(name = "users", schema = "", catalog = "magazinedb")
@IdClass(UsersHiberEntityPK.class)
public class UsersHiberEntity {
  private int userId;
  private String username;
  private String password;
  private boolean isPublisher;
  private String uuid;


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "user_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Id
  @Column(name = "username", nullable = false, insertable = true, updatable = true, length = 255, precision = 0)
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Basic
  @Column(name = "password", nullable = false, insertable = true, updatable = true, length = 20, precision = 0)
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Basic
  @Column(name = "isPublisher", nullable = false, insertable = true, updatable = true, columnDefinition = "TINYINT(1)", precision = 0)
  public boolean getIsPublisher() {
    return isPublisher;
  }

  public void setIsPublisher(boolean isPublisher) {
    this.isPublisher = isPublisher;
  }

  @Basic
  @Column(name = "uuid", nullable = true, insertable = true, updatable = true, length = 36)
  public String getUuid() {
    return uuid;
  }

  public void setUuid(final String uuid) {
    this.uuid = uuid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UsersHiberEntity that = (UsersHiberEntity) o;

    if (isPublisher != that.isPublisher) {
      return false;
    }
    if (userId != that.userId) {
      return false;
    }
    if (password != null ? !password.equals(that.password) : that.password != null) {
      return false;
    }
    if (username != null ? !username.equals(that.username) : that.username != null) {
      return false;
    }
    if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) {
      return false;
    }


    return true;
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + (username != null ? username.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    result = 31 * result + (isPublisher ? 1 : 0);
    result = 31 * result + (uuid != null ? uuid.hashCode() : 0);

    return result;
  }
}
