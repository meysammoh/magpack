package ir.mjm.admin;

import ir.mjm.DBAO.hiber.MagazineTblHiberEntity;

import java.util.ArrayList;

/**
 * Created by Serap on 8/15/14.
 */
public class ClassificationData {
  AdminPublisherInf publisherInfo;
  MagazineTblHiberEntity magazine;
  int dayUntilPublish;
  String fileName;
  String magazineName;
  ArrayList<DefinePageType> changePageTypes;

  public AdminPublisherInf getPublisherInfo() {
    return publisherInfo;
  }

  public void setPublisherInfo(AdminPublisherInf publisherInfo) {
    this.publisherInfo = publisherInfo;
  }

  public MagazineTblHiberEntity getMagazine() {
    return magazine;
  }

  public void setMagazine(MagazineTblHiberEntity magazine) {
    this.magazine = magazine;
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

  public String getMagazineName() {
    return magazineName;
  }

  public void setMagazineName(String magazineName) {
    this.magazineName = magazineName;
  }

  public ArrayList<DefinePageType> getChangePageTypes() {
    return changePageTypes;
  }

  public void setChangePageTypes(ArrayList<DefinePageType> changePageTypes) {
    this.changePageTypes = changePageTypes;
  }


}
