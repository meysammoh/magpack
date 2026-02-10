package ir.mjm.DBAO;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Created by Serap on 7/17/14.
 */
@ManagedBean
@ViewScoped
public class MagazineRepSpecTitle implements Serializable {
  Magazine_tblTable magazine_tblTable;
  double averageReaderAge;
  String geogrHotspotPostCode;
  double averagePageViews;
  int totalPageViews;
  int totalAdPageViews;
  double averageLengthPerReadingSession;
  int totalaReadingsessions;
  double revenuesEarned;
  int totalDownloadsPerReach;

  public Magazine_tblTable getMagazine_tblTable() {
    return magazine_tblTable;
  }

  public void setMagazine_tblTable(Magazine_tblTable magazine_tblTable) {
    this.magazine_tblTable = magazine_tblTable;
  }

  public double getAverageReaderAge() {
    return averageReaderAge;
  }

  public void setAverageReaderAge(double averageReaderAge) {
    this.averageReaderAge = averageReaderAge;
  }

  public String getGeogrHotspotPostCode() {
    return geogrHotspotPostCode;
  }

  public void setGeogrHotspotPostCode(String geogrHotspotPostCode) {
    this.geogrHotspotPostCode = geogrHotspotPostCode;
  }

  public double getAveragePageViews() {
    return averagePageViews;
  }

  public void setAveragePageViews(double averagePageViews) {
    this.averagePageViews = averagePageViews;
  }

  public double getTotalPageViews() {
    return totalPageViews;
  }

  public void setTotalPageViews(int totalPageViews) {
    this.totalPageViews = totalPageViews;
  }

  public int getTotalAdPageViews() {
    return totalAdPageViews;
  }

  public void setTotalAdPageViews(int totalAdPageViews) {
    this.totalAdPageViews = totalAdPageViews;
  }

  public double getAverageLengthPerReadingSession() {
    return averageLengthPerReadingSession;
  }

  public void setAverageLengthPerReadingSession(double averageLengthPerReadingSession) {
    this.averageLengthPerReadingSession = averageLengthPerReadingSession;
  }

  public int getTotalaReadingsessions() {
    return totalaReadingsessions;
  }

  public void setTotalaReadingsessions(int totalaReadingsessions) {
    this.totalaReadingsessions = totalaReadingsessions;
  }

  public double getRevenuesEarned() {
    return revenuesEarned;
  }

  public void setRevenuesEarned(double revenuesEarned) {
    this.revenuesEarned = revenuesEarned;
  }

  public int getTotalDownloadsPerReach() {
    return totalDownloadsPerReach;
  }

  public void setTotalDownloadsPerReach(int totalDownloadsPerReach) {
    this.totalDownloadsPerReach = totalDownloadsPerReach;
  }
}
