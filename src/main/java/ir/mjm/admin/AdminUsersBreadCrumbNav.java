package ir.mjm.admin;

import ir.mjm.util.LocaleBean;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Created by Serap on 8/12/14.
 */
@ManagedBean
@SessionScoped
public class AdminUsersBreadCrumbNav {

  private MenuModel menuModel = new DefaultMenuModel();


  public MenuModel getMenuModel() {
    return menuModel;
  }

  public void setMenuModel(MenuModel menuModel) {
    this.menuModel = menuModel;
  }

  public void navigateUsers() {
    // Initialize
    this.menuModel = new DefaultMenuModel();

    DefaultMenuItem home = new DefaultMenuItem();
    home.setValue(LocaleBean.localMessageOf("users.breadcrumb"));
    home.setUrl("/adminpanel/AdminUsers.xhtml");

    DefaultMenuItem allMags = new DefaultMenuItem();
    allMags.setValue(LocaleBean.localMessageOf("users.breadcrumb"));
    allMags.setUrl("/adminpanel/AdminUsers.xhtml");

    // Add menuItems
    this.menuModel.addElement(home);
    this.menuModel.addElement(allMags);


  }

  public void navigateEditUser() {
    // Initialize
    this.menuModel = new DefaultMenuModel();

    DefaultMenuItem home = new DefaultMenuItem();
    home.setValue(LocaleBean.localMessageOf("users.breadcrumb"));
    home.setUrl("/adminpanel/AdminUsers.xhtml");

    DefaultMenuItem allMags = new DefaultMenuItem();
    allMags.setValue(LocaleBean.localMessageOf("users.breadcrumb"));
    allMags.setUrl("/adminpanel/AdminUsers.xhtml");

    DefaultMenuItem specTitle = new DefaultMenuItem();
    specTitle.setValue(LocaleBean.localMessageOf("edit.user.breadcrumb"));
    specTitle.setUrl("/adminpanel/AdminEditUsers.xhtml");


    // Add menuItems
    this.menuModel.addElement(home);
    this.menuModel.addElement(allMags);
    this.menuModel.addElement(specTitle);
  }


}
