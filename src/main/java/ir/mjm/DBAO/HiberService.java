package ir.mjm.DBAO;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

/**
 * Created by Serap on 7/10/14.
 */
public class HiberService {
  static final Logger log = Logger.getLogger(HiberService.class);

  private static SessionFactory sessionFactory;
  private static Configuration configuration;
  private static HiberService ourInstance;

  public static HiberService getInstance() {
    if (ourInstance == null) {
      ourInstance = new HiberService();
    }
    return ourInstance;
  }

  private HiberService() {
    if (configuration == null) {
      File configFile = Statistics.getHiberConfigFile();
      log.error("Config File: " + configFile.getPath() + " Exist= " + configFile.exists());
      configuration = new Configuration().configure(configFile);
    }
  }

  public SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      sessionFactory = configuration.buildSessionFactory();
    }
    return sessionFactory;
  }

  public Session getCurrentSession() {
    return getSessionFactory().getCurrentSession();
  }


}
