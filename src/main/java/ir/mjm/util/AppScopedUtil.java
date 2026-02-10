package ir.mjm.util;

import ir.mjm.DBAO.ConverterFacad;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * Created by Serap on 7/6/14.
 */
@ApplicationScoped
@ManagedBean
public class AppScopedUtil {
  public ConverterFacad converterFacad = new ConverterFacad();


}
