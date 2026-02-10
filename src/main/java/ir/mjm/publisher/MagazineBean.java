package ir.mjm.publisher;

import java.util.Date;

/**
 * Created by Serap on 6/8/14.
 */
public class MagazineBean {
  private int magazine_id;
  private int user_id;
  private String magazineName;
  private String publisherName;
  private String path;

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  private Date date;

  public String getMagazineName() {
    return magazineName;
  }

  public void setMagazineName(String magazineName) {
    this.magazineName = magazineName;
  }


  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getPublisherName() {
    return publisherName;
  }

  public void setPublisherName(String publisherName) {
    this.publisherName = publisherName;
  }


  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public int getMagazine_id() {
    return magazine_id;
  }

  public void setMagazine_id(int magazine_id) {
    this.magazine_id = magazine_id;
  }


}
