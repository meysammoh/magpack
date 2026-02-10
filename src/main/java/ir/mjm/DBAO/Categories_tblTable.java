package ir.mjm.DBAO;

import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;

/**
 * Created by Serap on 6/12/14.
 */
@ManagedBean
public class Categories_tblTable {
  static final Logger log = Logger.getLogger(Categories_tblTable.class);

  private int cat_id;
  private String category;


  public int getCat_id() {
    return cat_id;
  }

  public void setCat_id(int cat_id) {
    this.cat_id = cat_id;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  //    public  void addCategory( ){
  //        try {
  ////            (String) ((InputText) actionEvent.getComponent().findComponent("magazinecat")).getValue()
  //            DBFacade.getInstance().addCategory(getCategory());
  //            FacesContext.getCurrentInstance().addMessage("Success", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info ", "Category: "+getCategory()+" added."));
  //
  //        } catch (Exception e) {
  //            if(e.getMessage().indexOf("Duplicate")!=-1)
  //                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There Are Category    with same name: "+getCategory()+"."));
  //
  //            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "can't add category"));
  //
  //            log.error("Exception in: ",e);
  //
  //        }
  //    }
}
