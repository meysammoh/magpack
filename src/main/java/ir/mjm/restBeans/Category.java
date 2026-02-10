package ir.mjm.restBeans;

/**
 * @author Jafar Mirzaei (jm.csh2009@gmail.com)
 * @version 1.0 2015.1007
 * @since 1.0
 */
public final class Category {
  private final Integer id;
  private final String cat;
  private final String url;

  public Category(final Integer id, final String cat, final String url) {
    this.id = id;
    this.cat = cat;
    this.url = url;
  }

  public Integer getId() {
    return id;
  }

  public String getCat() {
    return cat;
  }

  public String getUrl() {
    return url;
  }
}

