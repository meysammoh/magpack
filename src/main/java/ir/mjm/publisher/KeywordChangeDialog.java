package ir.mjm.publisher;

import ir.mjm.DBAO.Magazine_tblTable;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @author Jafar Mirzaei (jm.csh2009@gmail.com)
 * @version 1.0 2015.1008
 * @since 1.0
 */
@ManagedBean
@SessionScoped
public class KeywordChangeDialog {
  static final Logger log = Logger.getLogger(KeywordChangeDialog.class);

  private Magazine_tblTable currentMagazine;
  private String newKeyword;


  public void keywordsDialog(Magazine_tblTable currentMagazine) {
    initMAgazineTbl(currentMagazine);
  }

  public void showDialog() {
    Map<String, Object> options = new HashMap<String, Object>();
    options.put("closable", true);
    options.put("modal", true);
    options.put("draggable", true);
    options.put("resizable", true);

    RequestContext.getCurrentInstance().openDialog("keywordPanel", options, null);
  }

  public void initMAgazineTbl(final Magazine_tblTable currentMagazine) {
    this.currentMagazine = currentMagazine;

    final String magazineKeywordsStringTemp = this.currentMagazine.getMagazineKeywords();
    this.magazineKeywords = new HashSet<>();
    if (magazineKeywordsStringTemp != null) {
      final String[] splited = magazineKeywordsStringTemp.split("-");
      Collections.addAll(this.magazineKeywords, splited);
    }
    //    showDialog();
    log.error("Keywords current magazine change to: " + currentMagazine.getFileName());
  }


  public void onDialogReturn(SelectEvent event) {

  }

  public void close() {
    RequestContext.getCurrentInstance().closeDialog(null);
  }

  private Set<String> magazineKeywords;

  public Set<String> getMagazineKeywords() {
    return magazineKeywords;
  }

  public void setMagazineKeywords(final Set<String> magazineKeywords) {
    this.magazineKeywords = magazineKeywords;
  }

  public void setNewKeyword(String newKeyword) {this.newKeyword = newKeyword.trim();}

  public String getNewKeyword() { return newKeyword; }

  public void addNewKeyword() {
    newKeyword = newKeyword.trim();
    if (newKeyword.length() == 0) {
      return;
    }

    if (magazineKeywords.add(newKeyword)) {
      updateMagaizeTblObject();
    }
    newKeyword = "";
  }

/*  public void onEdit(RowEditEvent event) {
    updateMagaizeTblObject();
  }

  public void onCancel(RowEditEvent event) {

  }*/

  public void delete(String keyword) {
    if (magazineKeywords.remove(keyword)) {
      updateMagaizeTblObject();
    }
    newKeyword = "";
  }

  private void updateMagaizeTblObject() {
    if (magazineKeywords.size() < 1) {
      currentMagazine.setMagazineKeywords(null);
      return;
    }

    StringBuilder stringBuilder = new StringBuilder();
    final String[] obj = magazineKeywords.toArray(new String[magazineKeywords.size()]);
    for (int i = 0; i < magazineKeywords.size() - 1; i++) {
      stringBuilder.append(obj[i]).append("-");
    }
    stringBuilder.append(obj[magazineKeywords.size() - 1]);

    currentMagazine.setMagazineKeywords(stringBuilder.toString());
  }

  public void setCurrentMagazine(Magazine_tblTable currentMAgazine) {
    initMAgazineTbl(currentMAgazine);
  }

  public Magazine_tblTable getCurrentMagazine() { return currentMagazine; }
}

