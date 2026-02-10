package ir.mjm.restBeans;

import ir.mjm.entities.MagazineType;

import java.util.Date;

/**
 * Created by Serap on 6/27/14.
 */
public class Suggestion {
  int pub_id;
  int mag_id;
  String title;
  String cat;
  String url;
  Date PublishDate;
  String imageName;
  String description;
  int issue_num;
  int maxPageNumber;
  String language;
  int downloadCount;
  int favorCount;
  long bookmarkCount;
  MagazineType magazineType;
  boolean isFree;
  boolean isNew;
  public String[] nameAndExt;


  public long getBookmarkCount() {
    return bookmarkCount;
  }

  public void setBookmarkCount(long bookmarkCount) {
    this.bookmarkCount = bookmarkCount;
  }

  public int getDownloadCount() {
    return downloadCount;
  }

  public void setDownloadCount(int downloadCount) {
    this.downloadCount = downloadCount;
  }

  public int getFavorCount() {
    return favorCount;
  }

  public void setFavorCount(int favorCount) {
    this.favorCount = favorCount;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public int getMaxPageNumber() {
    return maxPageNumber;
  }

  public void setMaxPageNumber(int maxPageNumber) {
    this.maxPageNumber = maxPageNumber;
  }

  public int getIssue_num() {
    return issue_num;
  }

  public void setIssue_num(int issue_num) {
    this.issue_num = issue_num;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }


  public int getMag_id() {
    return mag_id;
  }

  public void setMag_id(int mag_id) {
    this.mag_id = mag_id;
  }

  public Date getPublishDate() {
    return PublishDate;
  }

  public void setPublishDate(Date publishDate) {
    PublishDate = publishDate;
  }


  public int getPub_id() {
    return pub_id;
  }

  public void setPub_id(int pub_id) {
    this.pub_id = pub_id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCat() {
    return cat;
  }

  public void setCat(String cat) {
    this.cat = cat;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public boolean isFree() {
    return isFree;
  }

  public void setFree(boolean isFree) {
    this.isFree = isFree;
  }

  public boolean isNew() {
    return isNew;
  }

  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }

  public MagazineType getMagazineType() {
    return magazineType;
  }

  public void setMagazineType(final MagazineType magazineType) {
    this.magazineType = magazineType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }
}
