package ir.mjm.admin;

import ir.mjm.DBAO.Statistics;
import ir.mjm.DBAO.hiber.MagazineTblHiberEntity;
import ir.mjm.DBAO.hiber.PubbilltHiberEntity;
import ir.mjm.DBAO.hiber.PubcompanyHiberEntity;
import ir.mjm.DBAO.hiber.PubusersHiberEntity;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Created by Serap on 7/25/14.
 */
@ManagedBean
@ViewScoped
public class AdminPublisherInf {
  static final Logger log = Logger.getLogger(AdminPublisherInf.class);

  PubcompanyHiberEntity pubcompanyHiberEntity;
  PubbilltHiberEntity pubbilltHiberEntity;
  PubusersHiberEntity pubusersHiberEntity;
  MagazineTblHiberEntity magazineTblHiberEntity;
  String lastUploaded = "";
  String lastIssueFileName = "";
  int filesUploaded;

  public PubcompanyHiberEntity getPubcompanyHiberEntity() {
    return pubcompanyHiberEntity;
  }

  public void setPubcompanyHiberEntity(PubcompanyHiberEntity pubcompanyHiberEntity) {
    this.pubcompanyHiberEntity = pubcompanyHiberEntity;
  }

  public PubusersHiberEntity getPubusersHiberEntity() {
    return pubusersHiberEntity;
  }

  public void setPubusersHiberEntity(PubusersHiberEntity pubusersHiberEntity) {
    this.pubusersHiberEntity = pubusersHiberEntity;
  }

  public String getLastUploaded() {
    if (lastUploaded.equals("") && magazineTblHiberEntity != null && magazineTblHiberEntity.getDateTime() != null) {
      try {
        setLastUploaded(Statistics.nonFullDateFormat.format(Statistics.nonFullDateFormat.parse(magazineTblHiberEntity.getDateTime())));
      } catch (Exception e) {
        log.error("Exception in: ", e);
      }
    }
    return lastUploaded;
  }

  public void setLastUploaded(String lastUploaded) {
    this.lastUploaded = lastUploaded;
  }

  public String getLastIssueFileName() {
    if (lastIssueFileName.equals("") && magazineTblHiberEntity != null && magazineTblHiberEntity.getDirPath() != null) {

      setLastIssueFileName(Statistics.getFileNameFromDirPath(magazineTblHiberEntity.getDirPath()));
    }
    return lastIssueFileName;
  }

  public void setLastIssueFileName(String lastIssueFileName) {
    this.lastIssueFileName = lastIssueFileName;
  }

  public int getFilesUploaded() {
    return filesUploaded;
  }

  public void setFilesUploaded(int filesUploaded) {
    this.filesUploaded = filesUploaded;
  }

  public MagazineTblHiberEntity getMagazineTblHiberEntity() {
    if (magazineTblHiberEntity == null) {
      magazineTblHiberEntity = new MagazineTblHiberEntity();

    }
    return magazineTblHiberEntity;
  }

  public void setMagazineTblHiberEntity(MagazineTblHiberEntity magazineTblHiberEntity) {
    this.magazineTblHiberEntity = magazineTblHiberEntity;
  }

  public PubbilltHiberEntity getPubbilltHiberEntity() {
    return pubbilltHiberEntity;
  }

  public void setPubbilltHiberEntity(PubbilltHiberEntity pubbilltHiberEntity) {
    this.pubbilltHiberEntity = pubbilltHiberEntity;
  }
}
