package ir.mjm.util;

import java.util.HashMap;
import java.util.Map;

public enum AppearanceType {
  WEEKLY("weekly"),
  BI_WEEKLY("bi-weekly"),
  MONTHLY("monthly"),
  QUARTERLY("quarterly"),
  SEMI_ANNUALLY("semi-annually"),
  ANNUALLY("annually");
  private String name;

  // Reverse-lookup map for getting a day from an abbreviation
  private static final Map<String, AppearanceType> lookup = new HashMap<String, AppearanceType>();

  static {
    for (AppearanceType d : AppearanceType.values()) {
      lookup.put(d.getName(), d);
    }
  }

  private AppearanceType(final String typeName) {
    this.name = typeName;
  }

  public String localName() {
    return LocaleBean.localMessageOf(this.name);
  }

  public String getName() {
    return name;
  }

  public static AppearanceType nameToAppearanceType(String name) {
    return lookup.get(name);
  }

  public static String[] appearanceTypesName() {
    return lookup.keySet().toArray(new String[values().length]);
  }

  public static AppearanceType localNameToAppearanceType(String localName) {
    for (AppearanceType appearanceType : values()) {
      if (appearanceType.localName().equals(localName)) {
        return appearanceType;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return localName();
  }
}
