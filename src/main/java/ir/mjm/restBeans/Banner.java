package ir.mjm.restBeans;

import ir.mjm.DBAO.hiber.BannersTblHiberEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jafar Mirzaei (jm.csh2009@gmail.com)
 * @version 1.0 2015.1011
 * @since 1.0
 */
public final class Banner {
  private final int id;
  private final int magazineID;

  public Banner(final int id, final int magazineID) {
    this.id = id;
    this.magazineID = magazineID;
  }

  public Banner(BannersTblHiberEntity bannersTblHiberEntity) {
    this.id=bannersTblHiberEntity.getId();
    this.magazineID=bannersTblHiberEntity.getMagazineId();
  }

  public int getId() {
    return id;
  }

  public int getMagazineID() {
    return magazineID;
  }
}

