package ir.mjm.admin;

import ir.mjm.DBAO.hiber.PubusersHiberEntity;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Created by Serap on 8/13/14.
 */
@ManagedBean
@ViewScoped
public class ClassificationOfMagazines implements Serializable {
  PubusersHiberEntity pubusersHiberEntity;
  int dayUntilPublish;
  String fileName;
  String magazine;

  public PubusersHiberEntity getPubusersHiberEntity() {
    return pubusersHiberEntity;
  }

  public void setPubusersHiberEntity(PubusersHiberEntity pubusersHiberEntity) {
    this.pubusersHiberEntity = pubusersHiberEntity;
  }

  public int getDayUntilPublish() {
    return dayUntilPublish;
  }

  public void setDayUntilPublish(int dayUntilPublish) {
    this.dayUntilPublish = dayUntilPublish;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getMagazine() {
    return magazine;
  }

  public void setMagazine(String magazine) {
    this.magazine = magazine;
  }
}
