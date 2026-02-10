package ir.mjm.restpkg;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.hiber.MobileuserTblHiberEntity;
import org.glassfish.jersey.server.ContainerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

/**
 * @author Jafar Mirzaei (jm.csh2009@gmail.com)
 * @version 1.0 2015.1011
 * @since 1.0
 */
public abstract class MobileUserAuth {
  static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MobileUserAuth.class);

  @Context
    UriInfo uriInfo;
   @Context
   Request request;
   @Resource
   ServletContext servletContext;
   @Context
   HttpServletRequest req;

  public static int isLogrdInRecApi(Request request, HttpServletRequest req) {
    if (request == null) {
      return -1;
    }

    if (((ContainerRequest) request).getRequestHeader("WWW-Authenticate") == null ||
        ((ContainerRequest) request).getRequestHeader("WWW-Authenticate").isEmpty()) {
      return -1;
    }
    try {
      if (req != null && (req.getSession() != null) && (req.getSession().getAttribute("taninip") != null) &&
          ((String) req.getSession().getAttribute("taninip")).equals("ok")) {

        return Integer.valueOf(((ContainerRequest) request).getRequestHeader("WWW-Authenticate").get(0));
      }
      int user_id = Integer.valueOf(((ContainerRequest) request).getRequestHeader("WWW-Authenticate").get(0));
      MobileuserTblHiberEntity mobileuserTblHiberEntity = HiberDBFacad.getInstance().getMobileUser(user_id);
      if (mobileuserTblHiberEntity != null) {
        if (req != null && req.getSession() != null) {
          req.getSession().setAttribute("taninip", "ok");
        }
        return user_id;
      }
      return -1;
    } catch (Exception e) {
      log.error("Exception in : ", e);
      return -1;
    }
  }
  public static MobileuserTblHiberEntity isLogrdInRecApi(HttpServletRequest req, Request request) {
    if (request == null) {
      return null;
    }

    if (((ContainerRequest) request).getRequestHeader("WWW-Authenticate") == null ||
        ((ContainerRequest) request).getRequestHeader("WWW-Authenticate").isEmpty()) {
      return null;
    }
    try {

      int user_id = Integer.valueOf(((ContainerRequest) request).getRequestHeader("WWW-Authenticate").get(0));
      return HiberDBFacad.getInstance().getMobileUser(user_id);

    } catch (Exception e) {
      log.error("Exception in : ", e);
      return null;
    }
  }
  protected String pleaseLoginMesage() {
    return "Please Login by API " +
           uriInfo.getAbsolutePath().toString().substring(0, uriInfo.getAbsolutePath().toString().indexOf("users/")) +
           "userman/login?user= &pass=  then set header WWW-Authenticate value";
  }
  public static int isLogrdInGetMags(Request request, HttpServletRequest req) {
      if (request == null) {
        return -1;
      }

      if (((ContainerRequest) request).getRequestHeader("WWW-Authenticate") == null ||
          ((ContainerRequest) request).getRequestHeader("WWW-Authenticate").isEmpty()) {
        return -1;
      }
      try {
        //            if(req!=null&&(req.getSession()!=null)&&(req.getSession().getAttribute("taninip")!=null)&&((String) req.getSession().getAttribute("taninip")).equals("ok")){
        //
        //                return Integer.valueOf(((ContainerRequest) request).getRequestHeader("WWW-Authenticate").get(0));
        //            }
        int user_id = Integer.valueOf(((ContainerRequest) request).getRequestHeader("WWW-Authenticate").get(0));

        try {
          log.debug(" session WWW-Authenticate: " + user_id);
          //                log.error(" session taninip: " + ((ContainerRequest) request).getRequestHeader("taninip").get(0));
        } catch (Exception e) {
          log.error(e);
        }
        MobileuserTblHiberEntity mobileuserTblHiberEntity = HiberDBFacad.getInstance().getMobileUser(user_id);


        if (mobileuserTblHiberEntity != null) {
          //                if(mobileuserTblHiberEntity.getChargeennd().compareTo(Statistics.FullDateFormat.format(new Date()))<0)
          //                    return -100;
          if (req != null && req.getSession() != null) {
            req.getSession().setAttribute("taninip", "ok");
          }
          return user_id;
        }
        return -1;
      } catch (Exception e) {
        log.error(e);
        return -1;
      }
    }

    public static MobileuserTblHiberEntity isLogrdInGetMags(HttpServletRequest req, Request request) {
      if (request == null) {
        return null;
      }

      if (((ContainerRequest) request).getRequestHeader("WWW-Authenticate") == null ||
          ((ContainerRequest) request).getRequestHeader("WWW-Authenticate").isEmpty()) {
        return null;
      }
      try {
        //            if(req!=null&&(req.getSession()!=null)&&(req.getSession().getAttribute("taninip")!=null)&&((String) req.getSession().getAttribute("taninip")).equals("ok")){
        //
        //                return Integer.valueOf(((ContainerRequest) request).getRequestHeader("WWW-Authenticate").get(0));
        //            }
        int user_id = Integer.valueOf(((ContainerRequest) request).getRequestHeader("WWW-Authenticate").get(0));


        return HiberDBFacad.getInstance().getMobileUser(user_id);
      } catch (Exception e) {
        log.error(e);
        return null;
      }
    }

}

