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
@Table(name = "pages", schema = "", catalog = "magazinedb")
@IdClass(PagesHiberEntityPK.class)
public class PagesHiberEntity {
  private int pageNumber;
  private int magId;
  private int pageType;//adpage  pageType==1 OR pageType==2 Mixed and not found Article

  @Id
  @Column(name = "page_number", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
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
  @Column(name = "pageType", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getPageType() {
    return pageType;
  }

  public void setPageType(int pageType) {
    this.pageType = pageType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PagesHiberEntity that = (PagesHiberEntity) o;

    if (magId != that.magId) {
      return false;
    }
    if (pageNumber != that.pageNumber) {
      return false;
    }
    if (pageType != that.pageType) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = pageNumber;
    result = 31 * result + magId;
    result = 31 * result + pageType;
    return result;
  }
}
