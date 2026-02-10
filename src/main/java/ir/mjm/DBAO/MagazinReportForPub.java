package ir.mjm.DBAO;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Created by Serap on 7/11/14.
 */
@ManagedBean
@ViewScoped
public class MagazinReportForPub implements Serializable {
  private Magazine_tblTable magazine_tblTable;
  private int downloads_current_issue;
  private int issues;
  private int download_all_issues;

  public Magazine_tblTable getMagazine_tblTable() {
    return magazine_tblTable;
  }

  public void setMagazine_tblTable(Magazine_tblTable magazine_tblTable) {
    this.magazine_tblTable = magazine_tblTable;
  }

  public int getDownloads_current_issue() {
    return downloads_current_issue;
  }

  public void setDownloads_current_issue(int downloads_current_issue) {
    this.downloads_current_issue = downloads_current_issue;
  }

  public int getIssues() {
    return issues;
  }

  public void setIssues(int issues) {
    this.issues = issues;
  }

  public int getDownload_all_issues() {
    return download_all_issues;
  }

  public void setDownload_all_issues(int download_all_issues) {
    this.download_all_issues = download_all_issues;
  }


}
