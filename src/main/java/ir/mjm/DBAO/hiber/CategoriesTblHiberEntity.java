package ir.mjm.DBAO.hiber;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.Statistics;
import org.primefaces.event.FileUploadEvent;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by Serap on 7/21/14.
 */
@Entity
@Table(name = "categories_tbl", schema = "", catalog = "magazinedb")
public class CategoriesTblHiberEntity {
  private int catId;
  private String category;
  private boolean sug;
  private String imgPath;


  @Id
  @Column(name = "cat_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
  public int getCatId() {
    return catId;
  }

  public void setCatId(int catId) {
    this.catId = catId;
  }

  @Basic
  @Column(name = "category", nullable = true, insertable = true, updatable = true, length = 45, precision = 0)
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @Basic
  @Column(name = "sug", columnDefinition = "TINYINT(1)", nullable = false, insertable = true, updatable = true, precision = 0)
  public boolean getSug() {
    return sug;
  }

  public void setSug(boolean sug) {
    this.sug = sug;
  }

  @Basic @Column(name = "img_path", nullable = true, insertable = true, updatable = true)
  public String getImgPath() {
    return imgPath;
  }

  public void setImgPath(final String imgPath) {
    this.imgPath = imgPath;
  }


  @Override public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final CategoriesTblHiberEntity that = (CategoriesTblHiberEntity) o;

    if (catId != that.catId) {
      return false;
    }
    if (sug != that.sug) {
      return false;
    }
    if (category != null ? !category.equals(that.category) : that.category != null) {
      return false;
    }
    if (imgPath != null ? !imgPath.equals(that.imgPath) : that.imgPath != null) {
      return false;
    }

    return true;
  }


  @Transient
  public void updateCategoryImage(FileUploadEvent event) {
    FacesMessage message;
    try {
      Statistics.updateCategoryImageAndThisParam(this, event.getFile());
      HiberDBFacad.getInstance().updateCategory(this);
      message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
    } catch (Exception e) {
      message = new FacesMessage("Error", event.getFile().getFileName() + " Upload Error.\n Please try again");
    }
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

}
