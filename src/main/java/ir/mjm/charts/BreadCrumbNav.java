package ir.mjm.charts;

import ir.mjm.DBAO.MagazineRepSpecTitle;
import ir.mjm.DBAO.PubUserDet;
import ir.mjm.DBAO.Statistics;
import ir.mjm.util.FaceUtil;
import ir.mjm.util.LocaleBean;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Created by Serap on 7/24/14.
 */
@ManagedBean
@SessionScoped
public class BreadCrumbNav {

  private MenuModel menuModel = new DefaultMenuModel();


  public MenuModel getMenuModel() {
    return menuModel;
  }

  public void setMenuModel(MenuModel menuModel) {
    this.menuModel = menuModel;
  }

  public void navigateAllTitle() {
    // Initialize
    this.menuModel = new DefaultMenuModel();

    DefaultMenuItem home = new DefaultMenuItem();
    home.setValue(
        LocaleBean.localMessageOf("all.magazines.of") + ((PubUserDet) FaceUtil.findBean("pubUserDet")).getFirstname());
    home.setUrl("/pubpanel/PubMagazines.xhtml");

    DefaultMenuItem allMags = new DefaultMenuItem();
    allMags.setValue(
        LocaleBean.localMessageOf("all.magazines.of") + ((PubUserDet) FaceUtil.findBean("pubUserDet")).getFirstname());
    allMags.setUrl("/pubpanel/PubMagazines.xhtml");

    // Add menuItems
    this.menuModel.addElement(home);
    this.menuModel.addElement(allMags);


  }

  public void navigateSpecTitle() {
    // Initialize
    this.menuModel = new DefaultMenuModel();

    DefaultMenuItem home = new DefaultMenuItem();
    home.setValue(
        LocaleBean.localMessageOf("all.magazines.of") + ((PubUserDet) FaceUtil.findBean("pubUserDet")).getFirstname());
    home.setUrl("/pubpanel/PubMagazines.xhtml");

    DefaultMenuItem allMags = new DefaultMenuItem();
    allMags.setValue(
        LocaleBean.localMessageOf("all.magazines.of") + ((PubUserDet) FaceUtil.findBean("pubUserDet")).getFirstname());
    allMags.setUrl("/pubpanel/PubMagazines.xhtml");

    DefaultMenuItem specTitle = new DefaultMenuItem();
    specTitle.setValue(((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedTitleForRepIssues());
    specTitle.setUrl("/pubpanel/PubMagazineSpecificTitle.xhtml");


    // Add menuItems
    this.menuModel.addElement(home);
    this.menuModel.addElement(allMags);
    this.menuModel.addElement(specTitle);
  }

  public void navigateSpecIssue() {
    this.menuModel = new DefaultMenuModel();

    DefaultMenuItem home = new DefaultMenuItem();
    home.setValue(
        LocaleBean.localMessageOf("all.magazines.of") + ((PubUserDet) FaceUtil.findBean("pubUserDet")).getFirstname());
    home.setUrl("/pubpanel/PubMagazines.xhtml");

    DefaultMenuItem allMags = new DefaultMenuItem();
    allMags.setValue(
        LocaleBean.localMessageOf("all.magazines.of") + ((PubUserDet) FaceUtil.findBean("pubUserDet")).getFirstname());
    allMags.setUrl("/pubpanel/PubMagazines.xhtml");

    DefaultMenuItem specTitle = new DefaultMenuItem();
    specTitle.setValue(((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedTitleForRepIssues());
    specTitle.setUrl("/pubpanel/PubMagazineSpecificTitle.xhtml");


    DefaultMenuItem specIssue = new DefaultMenuItem();
    MagazineRepSpecTitle pubUserDet =
        ((PubUserDet) ((PubUserDet) FaceUtil.findBean("pubUserDet"))).getSelectedCurrentMagazine().get(0);
    if (pubUserDet != null) {
      specIssue.setValue(
          Statistics.nonFullDateFormat.format(
              pubUserDet.getMagazine_tblTable()
                        .getOrig_issue_date()));
    } else {
      specIssue.setValue(
          LocaleBean.localMessageOf("issue.of") +
          ((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedTitleForRepIssues());
    }
    specIssue.setUrl("/pubpanel/PubReportSpecIssue.xhtml");


    // Add menuItems
    this.menuModel.addElement(home);
    this.menuModel.addElement(allMags);
    this.menuModel.addElement(specTitle);
    this.menuModel.addElement(specIssue);
  }

  public void navigatespecIssuePageView() {
    this.menuModel = new DefaultMenuModel();

    DefaultMenuItem home = new DefaultMenuItem();
    home.setValue(
        LocaleBean.localMessageOf("all.magazines.of") + ((PubUserDet) FaceUtil.findBean("pubUserDet")).getFirstname());
    home.setUrl("/pubpanel/PubMagazines.xhtml");

    DefaultMenuItem allMags = new DefaultMenuItem();
    allMags.setValue(
        LocaleBean.localMessageOf("all.magazines.of") + ((PubUserDet) FaceUtil.findBean("pubUserDet")).getFirstname());
    allMags.setUrl("/pubpanel/PubMagazines.xhtml");

    DefaultMenuItem specTitle = new DefaultMenuItem();
    specTitle.setValue(((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedTitleForRepIssues());
    specTitle.setUrl("/pubpanel/PubMagazineSpecificTitle.xhtml");


    DefaultMenuItem specIssue = new DefaultMenuItem();
    MagazineRepSpecTitle pubUserDet =
        ((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedCurrentMagazine().get(0);
    if (pubUserDet != null) {
      specIssue.setValue(
          Statistics.nonFullDateFormat.format(
              pubUserDet.getMagazine_tblTable()
                        .getOrig_issue_date()));
    } else {
      specIssue.setValue(
          LocaleBean.localMessageOf("issue.of") +
          ((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedTitleForRepIssues());
    }

    specIssue.setUrl("/pubpanel/PubReportSpecIssue.xhtml");


    DefaultMenuItem specIssuePageView = new DefaultMenuItem();
    specIssuePageView.setValue(LocaleBean.localMessageOf("specific.issue.page.view"));
    specIssuePageView.setUrl("/pubpanel/PubReportSpecIssuePageView.xhtml");


    // Add menuItems
    this.menuModel.addElement(home);
    this.menuModel.addElement(allMags);
    this.menuModel.addElement(specTitle);
    this.menuModel.addElement(specIssue);
    this.menuModel.addElement(specIssuePageView);
  }
}
