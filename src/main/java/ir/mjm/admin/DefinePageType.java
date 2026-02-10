package ir.mjm.admin;

import ir.mjm.DBAO.Statistics;
import ir.mjm.DBAO.hiber.PagesHiberEntity;

/**
 * Created by Serap on 8/15/14.
 */
public class DefinePageType {
  PagesHiberEntity pagesHiberEntity;
  String smallImg;
  String largImg;
  String pageType = "Text Only";

  public PagesHiberEntity getPagesHiberEntity() {
    return pagesHiberEntity;
  }

  public void setPagesHiberEntity(PagesHiberEntity pagesHiberEntity) {
    this.pagesHiberEntity = pagesHiberEntity;
  }

  public String getSmallImg() {
    return smallImg;
  }

  public void setSmallImg(String smallImg) {
    this.smallImg = smallImg;
  }

  public String getLargImg() {
    return largImg;
  }

  public void setLargImg(String largImg) {
    this.largImg = largImg;
  }

  public String getPageType() {

    if (pagesHiberEntity != null) {
      pageType = Statistics.getPageType(pagesHiberEntity.getPageType());
    }
    //            if(pagesHiberEntity.getPageType()==2)
    //                pageType="Ad";
    //            else if(pagesHiberEntity.getPageType()==1)
    //                pageType="Mixed";
    //            else if(pagesHiberEntity.getPageType()==0)
    //                pageType="Text Only";
    return pageType;


  }

  public void setPageType(String pageType) {
    this.pageType = pageType;
  }
}
