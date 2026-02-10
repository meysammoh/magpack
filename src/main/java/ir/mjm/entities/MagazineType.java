package ir.mjm.entities;

import ir.mjm.util.LocaleBean;

public enum MagazineType {
  NEWSPAPER("magazine.type.newspaper"), MONTHLY("magazine.type.monthly"), WEEKLY("magazine.type.weekly"), PERIODICAL(
      "magazine.type.periodical");
  private final String val;

  MagazineType(final String ordinal) {
    val = ordinal;
  }

  public String localName() {
    return LocaleBean.localMessageOf(this.val);
  }

  public String getVal() { return val; }

  public static String[] localValues() {
    String[] localValues = new String[values().length];
    int i = 0;
    for (MagazineType magazineType : values()) {
      localValues[i] = magazineType.localName();
      i++;
    }
    return localValues;
  }
  public static MagazineType localNameToAppearanceType(String localName){
    for (MagazineType magazineType:values()){
      if(magazineType.localName().equals(magazineType.localName()))
        return magazineType;
    }
    return null;
  }
}

