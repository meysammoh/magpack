package ir.mjm.DBAO.hiber;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by Serap on 7/21/14.
 */
public class PagesHiberEntityPK implements Serializable {
  private int pageNumber;
  private int magId;

  @Column(name = "page_number", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  @Id
  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
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

    PagesHiberEntityPK that = (PagesHiberEntityPK) o;

    if (magId != that.magId) {
      return false;
    }
    if (pageNumber != that.pageNumber) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = pageNumber;
    result = 31 * result + magId;
    return result;
  }
}
