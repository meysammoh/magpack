package ir.mjm.DBAO.hiber;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Serap on 7/21/14.
 */
@Entity
@Table(name = "magazine_title", schema = "", catalog = "magazinedb")
public class MagazineTitleHiberEntity {
  private int titleId;
  private int pubId;
  private String title;

  @Id
  @Column(name = "title_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getTitleId() {
    return titleId;
  }

  public void setTitleId(int titleId) {
    this.titleId = titleId;
  }

  @Basic
  @Column(name = "pub_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getPubId() {
    return pubId;
  }

  public void setPubId(int pubId) {
    this.pubId = pubId;
  }

  @Basic
  @Column(name = "title", nullable = false, insertable = true, updatable = true, length = 150, precision = 0)
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MagazineTitleHiberEntity that = (MagazineTitleHiberEntity) o;

    if (pubId != that.pubId) {
      return false;
    }
    if (titleId != that.titleId) {
      return false;
    }
    if (title != null ? !title.equals(that.title) : that.title != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = titleId;
    result = 31 * result + pubId;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    return result;
  }
}
