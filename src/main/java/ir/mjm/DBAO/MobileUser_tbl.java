package ir.mjm.DBAO;

/**
 * Created by Serap on 7/3/14.
 */
public class MobileUser_tbl {
  private UsersTable usersTable;
  private String bdate;
  private String postcode;
  private String fname;
  private String lname;
  private String job;
  private String gener;

  public UsersTable getUsersTable() {
    return usersTable;
  }

  public void setUsersTable(UsersTable usersTable) {
    this.usersTable = usersTable;
  }

  public String getBdate() {
    return bdate;
  }

  public void setBdate(String bdate) {
    this.bdate = bdate;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public String getFname() {
    return fname;
  }

  public void setFname(String fname) {
    this.fname = fname;
  }

  public String getLname() {
    return lname;
  }

  public void setLname(String lname) {
    this.lname = lname;
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
}
