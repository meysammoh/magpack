package ir.mjm.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jafar Mirzaei (jm.csh2009@gmail.com)
 * @version 1.0 2015.1019
 * @since 1.0
 */
public final class CookieManager {
  public static final String COOKIE_NAME = "UNUTMA-BENI-COOKIE";
  public static final int COOKIE_AGE = 2592000;

  public static String getCookieValue(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (name.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  public static void removeCookie(HttpServletResponse response, String name) {
    addCookie(response, name, null, 0);
  }
}

