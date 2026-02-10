package ir.mjm.DBAO.hiber;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.Statistics;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Jafar Mirzaei (jm.csh2009@gmail.com)
 * @version 1.0 2015.1008
 * @since 1.0
 */
@Entity
@Table(name = "banners_tbl", schema = "", catalog = "magazinedb")
public final class BannersTblHiberEntity {
  @Transient
  static final Logger log = Logger.getLogger(BannersTblHiberEntity.class);

  private int id;
  private int magazineId;
  private String imgPath;
  private String desc = "";
  private boolean active;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "magazine_id", nullable = false, insertable = true, updatable = true, length = 10)
  public int getMagazineId() {
    return magazineId;
  }

  public void setMagazineId(final int magazineId) {
    this.magazineId = magazineId;
  }

  @Basic
  @Column(name = "img_path", nullable = false, insertable = true, updatable = true, length = 500)
  public String getImgPath() {
    return imgPath;
  }

  public void setImgPath(final String imgPath) {
    this.imgPath = imgPath;
  }

  @Basic
  @Column(name = "description", nullable = true, insertable = true, updatable = true, length = 500)
  public String getDesc() {
    return desc;
  }

  public void setDesc(final String desc) {
    this.desc = desc;
  }

  @Basic
  @Column(name = "active", columnDefinition = "TINYINT(1)", nullable = true, insertable = true, updatable = true)
  public boolean getActive() {
    return active;
  }

  public void setActive(final boolean active) {
    this.active = active;
  }

  @Override public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final BannersTblHiberEntity that = (BannersTblHiberEntity) o;

    if (id != that.id) {
      return false;
    }
    if (magazineId != that.magazineId) {
      return false;
    }
    if (imgPath != null ? !imgPath.equals(that.imgPath) : that.imgPath != null) {
      return false;
    }
    if (desc != null ? !desc.equals(that.desc) : that.desc != null) {
      return false;
    }
    if (active != that.active) {
      return false;
    }

    return true;
  }

  @Override public int hashCode() {
    int result = id;
    result = 31 * result + magazineId;
    result = 31 * result + (imgPath != null ? imgPath.hashCode() : 0);
    result = 31 * result + (desc != null ? desc.hashCode() : 0);
    result = 31 * result + (active ? 1 : 0);
    return result;
  }




  @Transient
  public void updateBannerImage(FileUploadEvent event) {
    FacesMessage message;
    try {
      this.setImgPath("");
      HiberDBFacad.getInstance().updateBanner(this);
      Statistics.updateBannerImageAndThisParam(this, event.getFile());
      HiberDBFacad.getInstance().updateBanner(this);

      message = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
    } catch (Exception e) {
      log.error(e);
      message = new FacesMessage("Error", event.getFile().getFileName() + " Upload Error.\n Please try again");
    }
    FacesContext.getCurrentInstance().addMessage(null, message);
  }



}

