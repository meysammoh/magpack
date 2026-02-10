package ir.mjm.publisher;

import ir.mjm.DBAO.Magazine_tblTable;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @author Jafar Mirzaei (jm.csh2009@gmail.com)
 * @version 1.0 2015.1008
 * @since 1.0
 */
@ManagedBean
@SessionScoped
public class DescriptionChangeDialog {
  static final Logger log = Logger.getLogger(DescriptionChangeDialog.class);

  private Magazine_tblTable currentMagazine;


  public void showDialog() {
    Map<String, Object> options = new HashMap<String, Object>();
    options.put("closable", true);
    options.put("modal", true);
    options.put("draggable", true);
    options.put("resizable", true);

    RequestContext.getCurrentInstance().openDialog("descriptionPanel", options, null);
  }

  public void initMAgazineTbl(final Magazine_tblTable currentMagazine) {
    this.currentMagazine = currentMagazine;


    //    showDialog();
    log.debug("Description current magazine change to: " + currentMagazine.getFileName());
  }


  public void onDialogReturn(SelectEvent event) {

  }

  public void close() {
    RequestContext.getCurrentInstance().closeDialog(null);
  }

  public void setCurrentMagazine(Magazine_tblTable currentMAgazine) {
    initMAgazineTbl(currentMAgazine);
  }

  public Magazine_tblTable getCurrentMagazine() { return currentMagazine; }
}

