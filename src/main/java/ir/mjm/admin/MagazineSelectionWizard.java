package ir.mjm.admin;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.hiber.MagazineTblHiberEntity;
import ir.mjm.DBAO.hiber.PubcompanyHiberEntity;
import org.primefaces.context.RequestContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * @author Jafar Mirzaei (jm.csh2009@gmail.com)
 * @version 1.0 2015.1009
 * @since 1.0
 */
@ManagedBean
@ViewScoped
public final class MagazineSelectionWizard {
  boolean skip;
  private String queryPublisherId;
  private String queryPublisherName;
  private List<MagazineTblHiberEntity> publishersMagazines;
  private Map<Integer, String> publisherCompaniesNameMap = new HashMap<>();
  private List<Integer> publisherCompaniesIds = new ArrayList<>();
  private List<String> publisherCompaniesNames = new ArrayList<>();
  private MagazineTblHiberEntity selectedMagazine;
  private boolean allPublisherCompanyLoaded = false;


  public List<String> pulisherCompanyName(String query) {
    if (!allPublisherCompanyLoaded) {
      fillPubCompanyName(query);
    }
    allPublisherCompanyLoaded = query.trim().equals("");
    return publisherCompaniesNames;
  }

  private void fillPubCompanyName(final String query) {
    for (PubcompanyHiberEntity pubComp : HiberDBFacad.getInstance().publisherNameLike(query)) {
      publisherCompaniesNameMap.put(pubComp.getId(), pubComp.getCompname());
      publisherCompaniesIds.add(pubComp.getId());
      publisherCompaniesNames.add(pubComp.getCompname());
    }
  }

  public List<Integer> pulisherCompanyIdLike(String query) {
    if (!allPublisherCompanyLoaded) {
      if (!query.equals("")) {
        int pubIdLike;
        try {
          pubIdLike = Integer.valueOf(query);
          for (PubcompanyHiberEntity pubComp : HiberDBFacad.getInstance().publisherIdLike(pubIdLike)) {
                   publisherCompaniesNameMap.put(pubComp.getId(), pubComp.getCompname());
                   publisherCompaniesIds.add(pubComp.getId());
                   publisherCompaniesNames.add(pubComp.getCompname());
                 }
        } catch (Exception e) {
        }

      } else {
        fillPubCompanyName("");
        allPublisherCompanyLoaded = true;
      }
    }
    return publisherCompaniesIds;
  }

  public void searchMagazineOfPublisher() {
    if (publisherCompaniesNameMap.size() == 0) {
      publishersMagazines = HiberDBFacad.getInstance().allMagazines();
    } else {
      try {
        if((!"0".equals(queryPublisherId))&&((!"".equals(queryPublisherId)))){
        final Integer pubIds = Integer.valueOf(queryPublisherId);
   publishersMagazines = HiberDBFacad.getInstance().MagazinesOf(pubIds);
        }else{
          publishersMagazines = HiberDBFacad.getInstance().MagazinesOf(publisherCompaniesIds);
        }
      } catch (NumberFormatException e) {
      }
    }

  }

  public void setQueryPublisherId(String queryPublisherId) {this.queryPublisherId = queryPublisherId;}

  public String getQueryPublisherId() { return queryPublisherId; }

  public MagazineTblHiberEntity getSelectedMagazine() {
    return selectedMagazine;
  }

  public String getQueryPublisherName() {
    return queryPublisherName;
  }

  public void setQueryPublisherName(final String queryPublisherName) {
    this.queryPublisherName = queryPublisherName;
  }

  public void setSelectedMagazine(final MagazineTblHiberEntity selectedMagazine) {
    this.selectedMagazine = selectedMagazine;
  }

  public void setPublishersMagazines(List<MagazineTblHiberEntity> publishersMagazines) {
    this.publishersMagazines = publishersMagazines;
  }

  public List<MagazineTblHiberEntity> getPublishersMagazines() { return publishersMagazines; }

  public void userSelectMagazine() {
    RequestContext.getCurrentInstance()
                  .closeDialog(selectedMagazine == null ? selectedMagazine : selectedMagazine.getMagazineId());

  }

  public Map<Integer, String> getPublisherCompaniesNameMap() {
    return publisherCompaniesNameMap;
  }

  public void setPublisherCompaniesNameMap(final Map<Integer, String> publisherCompaniesNameMap) {
    this.publisherCompaniesNameMap = publisherCompaniesNameMap;
  }
}

