package ir.mjm.restBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serap on 8/4/14.
 */
public class MobileUser {
  private String userName;
  private String birthDate;
  private String postCode;
  private String firstName;
  private String lastName;
  private String job;
  private String gener;
  private Double charge;
  private String chargeStart;
  private String chargeEnd;
  private List<Integer> favoredMagazineIds = new ArrayList<>();
  private UserBookmarked userBookmarkedPojo;


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  public String getPostCode() {
    return postCode;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getJob() {
    return job;
  }

  public void setJob(String job) {
    this.job = job;
  }

  public String getGener() {
    return gener;
  }

  public void setGener(String gener) {
    this.gener = gener;
  }

  public Double getCharge() {
    return charge;
  }

  public void setCharge(Double charge) {
    this.charge = charge;
  }

  public String getChargeStart() {
    return chargeStart;
  }

  public void setChargeStart(String chargeStart) {
    this.chargeStart = chargeStart;
  }

  public String getChargeEnd() {
    return chargeEnd;
  }

  public void setChargeEnd(String chargeEnd) {
    this.chargeEnd = chargeEnd;
  }

  public List<Integer> getFavoredMagazineIds() {
    return favoredMagazineIds;
  }

  public void setFavoredMagazineIds(List<Integer> favoredMagazineIds) {
    this.favoredMagazineIds = favoredMagazineIds;
  }

  public UserBookmarked getUserBookmarkedPojo() {
    return userBookmarkedPojo;
  }

  public void setUserBookmarkedPojo(UserBookmarked userBookmarkedPojo) {
    this.userBookmarkedPojo = userBookmarkedPojo;
  }
}
