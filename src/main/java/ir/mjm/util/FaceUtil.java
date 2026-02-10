package ir.mjm.util;

import javax.faces.context.FacesContext;

/**
 * Created by Serap on 6/13/14.
 */
public class FaceUtil {
  @SuppressWarnings("unchecked")
  public static <T> T findBean(String beanName) {
    FacesContext context = FacesContext.getCurrentInstance();
    return (T) context.getApplication().createValueBinding("#{" + beanName + "}").getValue(context);


  }

  public static <T> T findBeanSession(String beanName) {
    return (T) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(beanName);


  }

}
