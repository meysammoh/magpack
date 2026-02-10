package ir.mjm.admin;

import ir.mjm.DBAO.Statistics;
import ir.mjm.util.FaceUtil;
import ir.mjm.util.LocaleBean;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Created by Serap on 7/24/14.
 */
@ManagedBean
@SessionScoped
public class AdminPublishersBreadCrumbNav {

  private MenuModel menuModel = new DefaultMenuModel();


  public MenuModel getMenuModel() {
    return menuModel;
  }

  public void setMenuModel(MenuModel menuModel) {
    this.menuModel = menuModel;
  }

  //All Publishers
  public void navigateAllPublishers() {
    // Initialize
    this.menuModel = new DefaultMenuModel();

    DefaultMenuItem home = new DefaultMenuItem();
    home.setValue(LocaleBean.localMessageOf("all.publishers.breadcrumb"));
    home.setUrl("/adminpanel/Adminpublishers.xhtml");

    DefaultMenuItem allMags = new DefaultMenuItem();
    allMags.setValue(LocaleBean.localMessageOf("all.publishers.breadcrumb"));
    allMags.setUrl("/adminpanel/Adminpublishers.xhtml");

    // Add menuItems
    this.menuModel.addElement(home);
    this.menuModel.addElement(allMags);


  }

  //All publishers-> Publisher name    ---- 1) goto from Adminpublishers   2) from EditPublisher
  //1)
  public void navigateSpecPublisherEdit() {
    // Initialize
    this.menuModel = new DefaultMenuModel();

    DefaultMenuItem home = new DefaultMenuItem();
    home.setValue(LocaleBean.localMessageOf("all.publishers.breadcrumb"));
    home.setUrl("/adminpanel/Adminpublishers.xhtml");

    DefaultMenuItem allMags = new DefaultMenuItem();
    allMags.setValue(LocaleBean.localMessageOf("all.publishers.breadcrumb"));
    allMags.setUrl("/adminpanel/Adminpublishers.xhtml");

    DefaultMenuItem specTitle = new DefaultMenuItem();
    ArrayList<AdminPublisherInf> selected = ((AdminControler) FaceUtil.findBean("adminControler")).selectedPublisher;
    if (selected != null && selected.size() > 0) {
      specTitle.setValue(selected.get(0).getPubusersHiberEntity().getFirstname());
    }
    specTitle.setUrl("/adminpanel/EditPublisher.xhtml");


    // Add menuItems
    this.menuModel.addElement(home);
    this.menuModel.addElement(allMags);
    this.menuModel.addElement(specTitle);
  }

  //2)
  public void navigateSpecPublisherAllMags() {
    // Initialize
    this.menuModel = new DefaultMenuModel();

    DefaultMenuItem home = new DefaultMenuItem();
    home.setValue(LocaleBean.localMessageOf("all.publishers.breadcrumb"));
    home.setUrl("/adminpanel/Adminpublishers.xhtml");

    DefaultMenuItem allMags = new DefaultMenuItem();
    allMags.setValue(LocaleBean.localMessageOf("all.publishers.breadcrumb"));
    allMags.setUrl("/adminpanel/Adminpublishers.xhtml");

    DefaultMenuItem specTitle = new DefaultMenuItem();
    AdminPublisherMagazines selected = ((AdminPublisherMagazines) FaceUtil.findBean("adminPublisherMagazines"));
    if (selected != null && selected.getSelectedPublisher() != null &&
        selected.getSelectedPublisher().getPubusersHiberEntity() != null) {
      specTitle.setValue(selected.getSelectedPublisher().getPubusersHiberEntity().getFirstname());
    }
    specTitle.setUrl("/adminpanel/AdminPublisherReporting.xhtml");


    // Add menuItems
    this.menuModel.addElement(home);
    this.menuModel.addElement(allMags);
    this.menuModel.addElement(specTitle);
  }


  //All come from AdminPublisherReporting.xhml  all publisher-> publisher name -> magazine title
  public void navigateSpecTitle() {
    this.menuModel = new DefaultMenuModel();

    DefaultMenuItem home = new DefaultMenuItem();
    home.setValue(LocaleBean.localMessageOf("all.publishers.breadcrumb"));
    home.setUrl("/adminpanel/Adminpublishers.xhtml");

    DefaultMenuItem allMags = new DefaultMenuItem();
    allMags.setValue(LocaleBean.localMessageOf("all.publishers.breadcrumb"));
    allMags.setUrl("/adminpanel/Adminpublishers.xhtml");

    DefaultMenuItem specTitle = new DefaultMenuItem();
    AdminPublisherMagazines selected = ((AdminPublisherMagazines) FaceUtil.findBean("adminPublisherMagazines"));
    if (selected != null && selected.getSelectedPublisher() != null &&
        selected.getSelectedPublisher().getPubusersHiberEntity() != null) {
      specTitle.setValue(selected.getSelectedPublisher().getPubusersHiberEntity().getFirstname());
    }
    specTitle.setUrl("/adminpanel/AdminPublisherReporting.xhtml");


    DefaultMenuItem specIssue = new DefaultMenuItem();
    //        if(pubUserDet!=null)
    //        specIssue.setValue(Statistics.nonFullDateFormat.format(pubUserDet.getMagazine_tblTable().getOrig_issue_date()));
    //        else
    if (selected != null && selected.getSelectedTitleForRepIssues() != null &&
        !selected.getSelectedTitleForRepIssues().equals("")) {
      specIssue.setValue(selected.getSelectedTitleForRepIssues());
    } else {
      specIssue.setValue(LocaleBean.localMessageOf("no.title.breadcrumb"));
    }

    specIssue.setUrl("/adminpanel/AdminPublisherSpecMagazineReport.xhtml");


    // Add menuItems
    this.menuModel.addElement(home);
    this.menuModel.addElement(allMags);
    this.menuModel.addElement(specTitle);
    this.menuModel.addElement(specIssue);
  }


  //All come from AdminPublisherSpecMagazineReport.xhtml   all publisher-> publisher name -> magazine title -> issue orig date
  public void navigatePublisherpecMagazineReport() {
    this.menuModel = new DefaultMenuModel();

    DefaultMenuItem home = new DefaultMenuItem();
    home.setValue(LocaleBean.localMessageOf("all.publishers.breadcrumb"));
    home.setUrl("/adminpanel/Adminpublishers.xhtml");

    DefaultMenuItem allMags = new DefaultMenuItem();
    allMags.setValue(LocaleBean.localMessageOf("all.publishers.breadcrumb"));
    allMags.setUrl("/adminpanel/Adminpublishers.xhtml");


    this.menuModel.addElement(home);
    this.menuModel.addElement(allMags);


    DefaultMenuItem specTitle = new DefaultMenuItem();
    AdminPublisherMagazines selected = ((AdminPublisherMagazines) FaceUtil.findBean("adminPublisherMagazines"));
    if (selected != null && selected.getSelectedPublisher() != null &&
        selected.getSelectedPublisher().getPubusersHiberEntity() != null) {
      specTitle.setValue(selected.getSelectedPublisher().getPubusersHiberEntity().getFirstname());
    }
    specTitle.setUrl("/adminpanel/AdminPublisherReporting.xhtml");


    DefaultMenuItem specIssue = new DefaultMenuItem();
    //        if(pubUserDet!=null)
    //        specIssue.setValue(Statistics.nonFullDateFormat.format(pubUserDet.getMagazine_tblTable().getOrig_issue_date()));
    //        else
    if (selected != null && !selected.getSelectedTitleForRepIssues().equals("")) {
      specIssue.setValue(selected.getSelectedTitleForRepIssues());
    } else {
      specIssue.setValue(LocaleBean.localMessageOf("no.title.breadcrumb"));
    }

    specIssue.setUrl("/adminpanel/AdminPublisherSpecMagazineReport.xhtml");


    DefaultMenuItem specIssuePageView = new DefaultMenuItem();

    if (selected != null && selected.getSelectedCurrentMagazine() != null && selected.getSelectedIssue() != null) {
      specIssuePageView.setValue(
          Statistics.nonFullDateFormat.format(
              selected.getSelectedIssue()
                      .getOrig_issue_date()));
    }
    specIssuePageView.setUrl("/adminpanel/AdminPublisherSpecIssueReporting.xhtml");


    // Add menuItems

    this.menuModel.addElement(specTitle);
    this.menuModel.addElement(specIssue);
    this.menuModel.addElement(specIssuePageView);

  }
}
