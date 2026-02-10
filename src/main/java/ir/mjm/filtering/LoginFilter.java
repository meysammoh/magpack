package ir.mjm.filtering;


import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.hiber.UsersHiberEntity;
import ir.mjm.entities.LoginBean;
import ir.mjm.util.CookieManager;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Serap on 6/4/14.
 */
public class LoginFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
                                                                                                                IOException,
    
                                                                                                                ServletException {
    
    
    
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    
    // Get the loginBean from session attribute

    LoginBean loginBean = (LoginBean) ((HttpServletRequest) servletRequest).getSession().getAttribute("loginBean");
    
    if (loginBean == null || !loginBean.isLoggedIn()) {
        String uuid = CookieManager.getCookieValue(request, CookieManager.COOKIE_NAME);
    
        if (uuid != null) {
            UsersHiberEntity user = HiberDBFacad.getInstance().userUUID(uuid);
    
            if (user != null) {
                request.login(user.getUsername(), user.getPassword());
                request.getSession().setAttribute("user", user); // Login.
                CookieManager.addCookie(response, CookieManager.COOKIE_NAME, uuid, CookieManager.COOKIE_AGE); // Extends age.
            } else {
              CookieManager.removeCookie(response, CookieManager.COOKIE_NAME);
            }
        }
    }
    
    if (loginBean == null || !loginBean.isLoggedIn()) {
        response.sendRedirect(request.getContextPath() + "/web/login.xhtml");
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }
    //        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", ""));


    // For the first application request there is no loginBean in the session so user needs to log in
    // For other requests loginBean is present but we need to check if user has logged in successfully



  }

  @Override
  public void destroy() {

  }
}
