package ir.mjm.DBAO.hiber;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Serap on 7/21/14.
 */
@Entity
@Table(name = "mob_usr_iss_bookmark", schema = "", catalog = "magazinedb")
@IdClass(IssuepagebookmarkHiberEntityPK.class)
public class IssuepagebookmarkHiberEntity {
  private int userId;
  private int magId;
  private int page;
  private MobileuserTblHiberEntity mobileuserTblByUserId;
  private MagazineTblHiberEntity magazineTblByMagId;
  private String description;

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

  @Id
  @Column(name = "page", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
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

    IssuepagebookmarkHiberEntity that = (IssuepagebookmarkHiberEntity) o;

    if (magId != that.magId) {
      return false;
    }
    if (page != that.page) {
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
    result = 31 * result + page;
    return result;
  }

  @ManyToOne
  @JoinColumn(updatable = false, insertable = false, name = "user_id", referencedColumnName = "user_id", nullable = false)
  public MobileuserTblHiberEntity getMobileuserTblByUserId() {
    return mobileuserTblByUserId;
  }

  public void setMobileuserTblByUserId(MobileuserTblHiberEntity mobileuserTblByUserId) {
    this.mobileuserTblByUserId = mobileuserTblByUserId;
  }

  @ManyToOne
  @JoinColumn(updatable = false, insertable = false, name = "mag_id", referencedColumnName = "magazine_id", nullable = false)
  public MagazineTblHiberEntity getMagazineTblByMagId() {
    return magazineTblByMagId;
  }

  public void setMagazineTblByMagId(MagazineTblHiberEntity magazineTblByMagId) {
    this.magazineTblByMagId = magazineTblByMagId;
  }

  @Basic
  @Column(name = "description", nullable = true, insertable = true, updatable = true)
  public String getDescription() {
    return description;
  }

  public void setDescription(String desc) {
    this.description = desc;
  }
}
