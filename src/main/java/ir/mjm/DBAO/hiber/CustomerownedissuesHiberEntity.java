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
@Table(name = "customerownedissues", schema = "", catalog = "magazinedb")
@IdClass(CustomerownedissuesHiberEntityPK.class)
public class CustomerownedissuesHiberEntity {
  private int userId;
  private int magId;
  private int titleId;
  private int pubId;
  private String buyDate;
  private double payment;
  private boolean favorite;
  private MobileuserTblHiberEntity mobileuserTblByUserId;
  private MagazineTblHiberEntity magazineTblByMagId;
  private String chargestart;
  private String chargend;

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

  @Basic
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
  @Column(name = "buy_date", nullable = false, insertable = true, updatable = true, length = 20, precision = 0)
  public String getBuyDate() {
    return buyDate;
  }

  public void setBuyDate(String buyDate) {
    this.buyDate = buyDate;
  }

  @Basic
  @Column(name = "payment", nullable = false, insertable = true, updatable = true, length = 22, precision = 0)
  public double getPayment() {
    return payment;
  }

  public void setPayment(double payment) {
    this.payment = payment;
  }

  @Basic
  @Column(name = "favorite", nullable = false, insertable = true, updatable = true, columnDefinition = "TINYINT(1)", precision = 0)
  public boolean getFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CustomerownedissuesHiberEntity that = (CustomerownedissuesHiberEntity) o;

    if (favorite != that.favorite) {
      return false;
    }
    if (magId != that.magId) {
      return false;
    }
    if (Double.compare(that.payment, payment) != 0) {
      return false;
    }
    if (pubId != that.pubId) {
      return false;
    }
    if (titleId != that.titleId) {
      return false;
    }
    if (userId != that.userId) {
      return false;
    }
    if (buyDate != null ? !buyDate.equals(that.buyDate) : that.buyDate != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = userId;
    result = 31 * result + magId;
    result = 31 * result + titleId;
    result = 31 * result + pubId;
    result = 31 * result + (buyDate != null ? buyDate.hashCode() : 0);
    temp = Double.doubleToLongBits(payment);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (favorite ? 1 : 0);
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
  @Column(name = "chargestart", nullable = false, insertable = true, updatable = true, length = 20, precision = 0)
  public String getChargestart() {
    return chargestart;
  }

  public void setChargestart(String chargestart) {
    this.chargestart = chargestart;
  }

  @Basic
  @Column(name = "chargend", nullable = false, insertable = true, updatable = true, length = 20, precision = 0)
  public String getChargend() {
    return chargend;
  }

  public void setChargend(String chargend) {
    this.chargend = chargend;
  }
}
