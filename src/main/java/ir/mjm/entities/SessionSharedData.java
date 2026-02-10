package ir.mjm.entities;

import ir.mjm.DBAO.PubUserDet;
import ir.mjm.DBAO.hiber.MagazineTblHiberEntity;
import ir.mjm.admin.AdminControler;
import ir.mjm.admin.AdminPublisherInf;
import ir.mjm.admin.AdminPublisherMagazines;
import ir.mjm.util.FaceUtil;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Serap on 8/13/14.
 */
@ManagedBean
@SessionScoped
public class SessionSharedData {
  static final Logger log = Logger.getLogger(SessionSharedData.class);

  public int getSelectedPublisherID() {
    int pubId = -1;
    try {
      if (((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).isUserInRole(
          "adminrole")) {
        AdminPublisherInf tmp =
            ((AdminPublisherMagazines) FaceUtil.findBean("adminPublisherMagazines")).getSelectedPublisher();
        if (tmp != null) {
          pubId = tmp.getMagazineTblHiberEntity().getUserId();
        }

      } else if (((HttpServletRequest) FacesContext.getCurrentInstance()
                                                   .getExternalContext()
                                                   .getRequest()).isUserInRole("publisherrole")) {
        PubUserDet pubData = ((PubUserDet) FaceUtil.findBean("pubUserDet"));
        if (pubData != null) {
          pubId = pubData.getId();
        }
      }
    } catch (Exception e) {
      log.error("IN: SessionSharedData.createPieModel2 " + e.getMessage());
    }
    //        if(pubId==-1){
    //
    //        }
    return pubId;
  }

  public int getSelectedCurrentMagIDForCharts() {
    int selectedMagazine = -1;
    try {
      if (((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).isUserInRole(
          "adminrole")) {
        AdminPublisherMagazines tmp = ((AdminPublisherMagazines) FaceUtil.findBean("adminPublisherMagazines"));
        if (tmp != null) {
          selectedMagazine = tmp.getSetSelectedCurrentMag_id();
        }
        if (selectedMagazine <= 0) {
          tmp = ((AdminPublisherMagazines) FaceUtil.findBean("adminPublisherMagazines"));
          if (tmp != null) {
            selectedMagazine = tmp.getSetSelectedCurrentMag_id();
          }
        }
      } else if (((HttpServletRequest) FacesContext.getCurrentInstance()
                                                   .getExternalContext()
                                                   .getRequest()).isUserInRole("publisherrole")) {
        if (selectedMagazine == -1) {
          PubUserDet pubData = ((PubUserDet) FaceUtil.findBean("pubUserDet"));
          if (pubData != null && (pubData.getSelectedCurrentMagazine().size() > 0)) {
            selectedMagazine = pubData.getSelectedCurrentMag_id();
          }
        }

      }
    } catch (Exception e) {
      log.error("IN: SessionSharedData.createPieModel2 ", e);
    }

    return selectedMagazine;
  }

  public int getSelectedMagazineId() {
    int selectedMagazine = -1;
    try {
      if (((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).isUserInRole(
          "adminrole")) {
        MagazineTblHiberEntity tmp = ((AdminControler) FaceUtil.findBean("adminControler")).getSelectedMagazine();
        if (tmp != null) {
          selectedMagazine = tmp.getMagazineId();
        }
        if (selectedMagazine <= 0) {
          AdminPublisherMagazines tmp1 = ((AdminPublisherMagazines) FaceUtil.findBean("adminPublisherMagazines"));
          if (tmp1 != null) {
            selectedMagazine = tmp1.getSetSelectedCurrentMag_id();
          }
        }
      } else if (((HttpServletRequest) FacesContext.getCurrentInstance()
                                                   .getExternalContext()
                                                   .getRequest()).isUserInRole("publisherrole")) {
        if (selectedMagazine == -1) {
          PubUserDet pubData = ((PubUserDet) FaceUtil.findBean("pubUserDet"));
          if (pubData != null && (pubData.getSelectedCurrentMagazine().size() > 0)) {
            selectedMagazine = pubData.getSelectedCurrentMag_id();
          }
        }

      }
    } catch (Exception e) {
      log.error("IN: SessionSharedData.createPieModel2 " + e.getMessage());
    }

    return selectedMagazine;
  }
}
