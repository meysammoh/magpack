package ir.mjm.publisher;

/**
 * Created by Serap on 6/8/14.
 */
public class UserBean {
  private String username;
  private String password;
  private boolean isPublisher;

  public boolean isPublisher() {
    return isPublisher;
  }

  public void setPublisher(boolean isPublisher) {
    this.isPublisher = isPublisher;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  private int user_id;

}
