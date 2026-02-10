package ir.mjm.DBAO.hiber;

import ir.mjm.entities.MagazineType;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Serap on 7/21/14.
 */
@Entity
@Table(name = "magazine_tbl", schema = "", catalog = "magazinedb")
public class MagazineTblHiberEntity {
  private int magazineId;
  private int userId;
  private String dirPath;
  private String title;
  private String description;
  private String category;
  private Integer issueNum;
  private String issueYear;
  private String appertype;
  private String origIssueDate;
  private String issueAppear;
  private String dateTime;
  private String lang;
  private int constatus;
  private int dlcount;
  private int favorcount;
  private int pubstatus;
  private double price;
  private long sumOfBookmark;
  private String magKeywords;
  private MagazineType magType;

  @Id
  @Column(name = "magazine_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getMagazineId() {
    return magazineId;
  }

  public void setMagazineId(int magazineId) {
    this.magazineId = magazineId;
  }

  @Basic
  @Column(name = "user_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Basic
  @Column(name = "dir_path", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
  public String getDirPath() {
    return dirPath;
  }

  public void setDirPath(String dirPath) {
    this.dirPath = dirPath;
  }

  @Basic
  @Column(name = "title", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Basic
  @Column(name = "description", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  @Column(name = "category", nullable = false, insertable = true, updatable = true, length = 255, precision = 0)
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @Basic
  @Column(name = "issue_num", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
  public Integer getIssueNum() {
    return issueNum;
  }

  public void setIssueNum(Integer issueNum) {
    this.issueNum = issueNum;
  }

  @Basic
  @Column(name = "issue_year", nullable = true, insertable = true, updatable = true, length = 4, precision = 0)
  public String getIssueYear() {
    return issueYear;
  }

  public void setIssueYear(String issueYear) {
    this.issueYear = issueYear;
  }

  @Basic
  @Column(name = "appertype", nullable = true, insertable = true, updatable = true, length = 30, precision = 0)
  public String getAppertype() {
    return appertype;
  }

  public void setAppertype(String appertype) {
    this.appertype = appertype;
  }

  @Basic
  @Column(name = "orig_issue_date", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
  public String getOrigIssueDate() {
    return origIssueDate;
  }

  public void setOrigIssueDate(String origIssueDate) {
    this.origIssueDate = origIssueDate;
  }

  @Basic
  @Column(name = "issue_appear", nullable = true, insertable = true, updatable = true, length = 20, precision = 0)
  public String getIssueAppear() {
    return issueAppear;
  }

  public void setIssueAppear(String issueAppear) {
    this.issueAppear = issueAppear;
  }

  @Basic
  @Column(name = "date_time", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  @Basic
  @Column(name = "lang", nullable = true, insertable = true, updatable = true, length = 15, precision = 0)
  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  @Basic
  @Column(name = "constatus", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getConstatus() {
    return constatus;
  }

  public void setConstatus(int constatus) {
    this.constatus = constatus;
  }

  @Basic
  @Column(name = "dlcount", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getDlcount() {
    return dlcount;
  }

  public void setDlcount(int dlcount) {
    this.dlcount = dlcount;
  }

  @Basic
  @Column(name = "favorcount", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getFavorcount() {
    return favorcount;
  }

  public void setFavorcount(int favorcount) {
    this.favorcount = favorcount;
  }

  @Basic
  @Column(name = "pubstatus", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getPubstatus() {
    return pubstatus;
  }

  public void setPubstatus(int pubstatus) {
    this.pubstatus = pubstatus;
  }

  @Basic
  @Column(name = "price", nullable = false, insertable = true, updatable = true, length = 22, precision = 0)
  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  @Basic
  @Column(name = "sumOfBookmark", nullable = false, insertable = true, updatable = true, length = 19, precision = 0)
  public long getSumOfBookmark() {
    return sumOfBookmark;
  }

  public void setSumOfBookmark(long sumOfBookmark) {
    this.sumOfBookmark = sumOfBookmark;
  }


  @Basic
  @javax.persistence.Column(name = "mag_keywords", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
  public String getMagKeywords() {
    return magKeywords;
  }

  public void setMagKeywords(final String magKeywords) {
    this.magKeywords = magKeywords;
  }


  @Basic
  @javax.persistence.Column(name = "mag_type", nullable = false, insertable = true, updatable = true)
  @Enumerated(EnumType.STRING)
  public MagazineType getMagType() {
    return magType;
  }

  public void setMagType(final MagazineType magType) {
    this.magType = magType;
  }

  @Override public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final MagazineTblHiberEntity that = (MagazineTblHiberEntity) o;

    if (magazineId != that.getMagazineId()) {
      return false;
    }
    if (userId != that.getUserId()) {
      return false;
    }
    if (constatus != that.getConstatus()) {
      return false;
    }
    if (dlcount != that.getDlcount()) {
      return false;
    }
    if (favorcount != that.getFavorcount()) {
      return false;
    }
    if (pubstatus != that.getPubstatus()) {
      return false;
    }
    if (Double.compare(that.getPrice(), price) != 0) {
      return false;
    }
    if (sumOfBookmark != that.getSumOfBookmark()) {
      return false;
    }
    if (dirPath != null ? !dirPath.equals(that.getDirPath()) : that.getDirPath() != null) {
      return false;
    }
    if (title != null ? !title.equals(that.getTitle()) : that.getTitle() != null) {
      return false;
    }
    if (description != null ? !description.equals(that.getDescription()) : that.getDescription() != null) {
      return false;
    }
    if (category != null ? !category.equals(that.getCategory()) : that.getCategory() != null) {
      return false;
    }
    if (issueNum != null ? !issueNum.equals(that.getIssueNum()) : that.getIssueNum() != null) {
      return false;
    }
    if (issueYear != null ? !issueYear.equals(that.getIssueYear()) : that.getIssueYear() != null) {
      return false;
    }
    if (appertype != null ? !appertype.equals(that.getAppertype()) : that.getAppertype() != null) {
      return false;
    }
    if (origIssueDate != null ? !origIssueDate.equals(that.getOrigIssueDate()) : that.getOrigIssueDate() != null) {
      return false;
    }
    if (issueAppear != null ? !issueAppear.equals(that.getIssueAppear()) : that.getIssueAppear() != null) {
      return false;
    }
    if (dateTime != null ? !dateTime.equals(that.getDateTime()) : that.getDateTime() != null) {
      return false;
    }
    if (lang != null ? !lang.equals(that.getLang()) : that.getLang() != null) {
      return false;
    }
    if (magKeywords != null ? !magKeywords.equals(that.getMagKeywords()) : that.getMagKeywords() != null) {
      return false;
    }
    if (magType != null ? !magType.equals(that.getMagType()) : that.getMagType() != null) {
      return false;
    }

    return true;
  }

  @Override public int hashCode() {
    int result;
    long temp;
    result = magazineId;
    result = 31 * result + userId;
    result = 31 * result + (dirPath != null ? dirPath.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (category != null ? category.hashCode() : 0);
    result = 31 * result + (issueNum != null ? issueNum.hashCode() : 0);
    result = 31 * result + (issueYear != null ? issueYear.hashCode() : 0);
    result = 31 * result + (appertype != null ? appertype.hashCode() : 0);
    result = 31 * result + (origIssueDate != null ? origIssueDate.hashCode() : 0);
    result = 31 * result + (issueAppear != null ? issueAppear.hashCode() : 0);
    result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
    result = 31 * result + (lang != null ? lang.hashCode() : 0);
    result = 31 * result + constatus;
    result = 31 * result + dlcount;
    result = 31 * result + favorcount;
    result = 31 * result + pubstatus;
    temp = Double.doubleToLongBits(price);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (int) (sumOfBookmark ^ (sumOfBookmark >>> 32));
    result = 31 * result + (magKeywords != null ? magKeywords.hashCode() : 0);
    result = 31 * result + (magType != null ? magType.hashCode() : 0);
    return result;
  }
}