package ir.mjm.admin;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Created by Serap on 8/11/14.
 */
@ManagedBean
@SessionScoped
public class AddPublisherView implements Serializable {
  public void viewNetStat() {
    Map<String, Object> options = new HashMap<String, Object>();
    // options.put("contentHeight", 340);
    //        options.put("height", 500);
    //        options.put("width",800);
    //        options.put("contentWidth",550);
    //        options.put("contentHeight",400);
    options.put("closable", false);
    options.put("modal", true);
    options.put("draggable", false);
    options.put("resizable", false);


    RequestContext.getCurrentInstance().openDialog("AddPublisher", options, null);

  }

  public void closeNetStat() {
    RequestContext.getCurrentInstance().closeDialog("AddPublisher");
  }
}
