package ir.mjm.DBAO;

import java.io.Serializable;

/**
 * Created by Serap on 8/3/14.
 */
public class ReaderConversationPerPage implements Serializable {
  int pageNumber;
  String imagePath;
  int readPercent;
  double revenue;
  String pageType;
  int pageViews;
  double aveLengthOfView;

  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public double getRevenue() {
    return revenue;
  }

  public void setRevenue(double revenue) {
    this.revenue = revenue;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public int getReadPercent() {
    return readPercent;
  }

  public void setReadPercent(int readPercent) {
    this.readPercent = readPercent;
  }

  public String getPageType() {
    return pageType;
  }

  public void setPageType(String pageType) {
    this.pageType = pageType;
  }

  public int getPageViews() {
    return pageViews;
  }

  public void setPageViews(int pageViews) {
    this.pageViews = pageViews;
  }

  public double getAveLengthOfView() {
    return aveLengthOfView;
  }

  public void setAveLengthOfView(double aveLengthOfView) {
    this.aveLengthOfView = aveLengthOfView;
  }
}
