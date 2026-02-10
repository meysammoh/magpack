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
@Table(name = "issuepagerequest", schema = "", catalog = "magazinedb")
@IdClass(IssuepagerequestHiberEntityPK.class)
public class IssuepagerequestHiberEntity {
  private int userId;
  private int magId;
  private int page;
  private int titleId;
  private int pageType;
  private String requestTime;
  private MobileuserTblHiberEntity mobileuserTblByUserId;
  private MagazineTblHiberEntity magazineTblByMagId;

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

  @Basic
  @Column(name = "title_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getTitleId() {
    return titleId;
  }

  public void setTitleId(int titleId) {
    this.titleId = titleId;
  }

  @Basic
  @Column(name = "page_type", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getPageType() {
    return pageType;
  }

  public void setPageType(int pageType) {
    this.pageType = pageType;
  }

  @Basic
  @Column(name = "requestTime", nullable = false, insertable = true, updatable = true, length = 20, precision = 0)
  public String getRequestTime() {
    return requestTime;
  }

  public void setRequestTime(String requestTime) {
    this.requestTime = requestTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    IssuepagerequestHiberEntity that = (IssuepagerequestHiberEntity) o;

    if (magId != that.magId) {
      return false;
    }
    if (page != that.page) {
      return false;
    }
    if (pageType != that.pageType) {
      return false;
    }
    if (titleId != that.titleId) {
      return false;
    }
    if (userId != that.userId) {
      return false;
    }
    if (requestTime != null ? !requestTime.equals(that.requestTime) : that.requestTime != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + magId;
    result = 31 * result + page;
    result = 31 * result + titleId;
    result = 31 * result + pageType;
    result = 31 * result + (requestTime != null ? requestTime.hashCode() : 0);
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
}
