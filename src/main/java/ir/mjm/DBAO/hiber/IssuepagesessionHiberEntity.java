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
@Table(name = "issuepagesession", schema = "", catalog = "magazinedb")
@IdClass(IssuepagesessionHiberEntityPK.class)
public class IssuepagesessionHiberEntity {
  private int userId;
  private int magId;
  private String sessionstarttime;
  private int sessionduration;
  private int page;
  private int timeelapsed;
  private int clicktoenlargcount;
  private MagazineTblHiberEntity magazineTblByMagId;
  private MobileuserTblHiberEntity mobileuserTblByUserId;

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
  @Column(name = "sessionstarttime", nullable = false, insertable = true, updatable = true, length = 20, precision = 0)
  public String getSessionstarttime() {
    return sessionstarttime;
  }

  public void setSessionstarttime(String sessionstarttime) {
    this.sessionstarttime = sessionstarttime;
  }

  @Basic
  @Column(name = "sessionduration", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getSessionduration() {
    return sessionduration;
  }

  public void setSessionduration(int sessionduration) {
    this.sessionduration = sessionduration;
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
  @Column(name = "timeelapsed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getTimeelapsed() {
    return timeelapsed;
  }

  public void setTimeelapsed(int timeelapsed) {
    this.timeelapsed = timeelapsed;
  }

  @Basic
  @Column(name = "clicktoenlargcount", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getClicktoenlargcount() {
    return clicktoenlargcount;
  }

  public void setClicktoenlargcount(int clicktoenlargcount) {
    this.clicktoenlargcount = clicktoenlargcount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    IssuepagesessionHiberEntity that = (IssuepagesessionHiberEntity) o;

    if (clicktoenlargcount != that.clicktoenlargcount) {
      return false;
    }
    if (magId != that.magId) {
      return false;
    }
    if (page != that.page) {
      return false;
    }
    if (sessionduration != that.sessionduration) {
      return false;
    }
    if (timeelapsed != that.timeelapsed) {
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
    result = 31 * result + sessionduration;
    result = 31 * result + page;
    result = 31 * result + timeelapsed;
    result = 31 * result + clicktoenlargcount;
    return result;
  }

  @ManyToOne
  @JoinColumn(updatable = false, insertable = false, name = "mag_id", referencedColumnName = "magazine_id", nullable = false)
  public MagazineTblHiberEntity getMagazineTblByMagId() {
    return magazineTblByMagId;
  }

  public void setMagazineTblByMagId(MagazineTblHiberEntity magazineTblByMagId) {
    this.magazineTblByMagId = magazineTblByMagId;
  }

  @ManyToOne
  @JoinColumn(updatable = false, insertable = false, name = "user_id", referencedColumnName = "user_id", nullable = false)
  public MobileuserTblHiberEntity getMobileuserTblByUserId() {
    return mobileuserTblByUserId;
  }

  public void setMobileuserTblByUserId(MobileuserTblHiberEntity mobileuserTblByUserId) {
    this.mobileuserTblByUserId = mobileuserTblByUserId;
  }
}
