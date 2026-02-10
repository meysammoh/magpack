package ir.mjm.DBAO.hiber;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Serap on 7/21/14.
 */
@Entity
@Table(name = "roles", schema = "", catalog = "magazinedb")
public class RolesHiberEntity {
  private String rolename;

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

    RolesHiberEntity that = (RolesHiberEntity) o;

    if (rolename != null ? !rolename.equals(that.rolename) : that.rolename != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return rolename != null ? rolename.hashCode() : 0;
  }
}
