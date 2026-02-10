package ir.mjm.admin;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.Statistics;
import ir.mjm.DBAO.hiber.BannersTblHiberEntity;

import java.io.IOException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * @author Jafar Mirzaei (jm.csh2009@gmail.com)
 * @version 1.0 2015.1009
 * @since 1.0
 */
@ManagedBean
@ViewScoped
public final class BannerConfig {
  static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BannerConfig.class);
  private final static Integer[] BANNERS_IDS = {1, 2};

  public static Integer[] getBannersIds() {
    return BANNERS_IDS;
  }

  List<BannersTblHiberEntity> bannersTblHiberEntities;


  public List<BannersTblHiberEntity> getBannersTblHiberEntities() {
    bannersTblHiberEntities = HiberDBFacad.getInstance().banners();
    return bannersTblHiberEntities;
  }

  public void setBannersTblHiberEntities(final List<BannersTblHiberEntity> bannersTblHiberEntities) {
    this.bannersTblHiberEntities = bannersTblHiberEntities;
  }

  public void deleteSelectedBanner(BannersTblHiberEntity bannersTblHiberEntity) {
    if ((0 != bannersTblHiberEntity.getId()) && bannersTblHiberEntities.contains(bannersTblHiberEntity)) {
      try {
        Statistics.removeBanner(bannersTblHiberEntity);
        bannersTblHiberEntities.remove(bannersTblHiberEntity);
      } catch (IOException e) {
        log.error(e);
      }
    }
  }
}

